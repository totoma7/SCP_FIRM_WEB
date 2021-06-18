package com.ecbank.common.session;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionAttributeListener;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


public class WebSessionAttributeListener implements HttpSessionListener, HttpSessionAttributeListener {

	protected final Log logger = LogFactory.getLog(WebSessionAttributeListener.class);

	private static ConcurrentHashMap<String, String>  sessionMap = new ConcurrentHashMap<String,String>();



	ApplicationContext getContext(ServletContext servletContext) {
		return WebApplicationContextUtils.getWebApplicationContext(servletContext);
	}

	/**
	 *
	 * @param event HttpSessionEvent passed in by the container
	 */
	public void sessionCreated(HttpSessionEvent event) {
		HttpSession session = event.getSession();

		String sessionId = session.getId();
		//ApplicationContext ctx = getContext(event.getSession().getServletContext());

		if( logger.isDebugEnabled() ) {
     	   logger.debug(" Session is Created |SESSION_ID :" + sessionId );
        }
	}

	/**
	 *
	 * @param event The HttpSessionEvent pass in by the container
	 */
	public void sessionDestroyed(HttpSessionEvent event) {

		if( logger.isDebugEnabled() ) {
        	logger.debug(" sessionDestroyed called"  );
        }

		try {
			HttpSession session = event.getSession();

			String sessionId = session.getId();

			if( logger.isDebugEnabled() ) {
	        	logger.debug(" Session is invalidated |SESSION_ID :" + sessionId );
	        }

			for (Map.Entry<String, String> entry : sessionMap.entrySet()) {
				String strKey = entry.getKey();
				String strValue = entry.getValue();

				if( sessionId.equals( strValue) ) {
					sessionMap.remove( strKey);
					if( logger.isDebugEnabled() ) {
			        	logger.debug(" sessionMap removed : USER_ID :" + strKey + ", SESSION_ID = " + strValue);
			        }
				}

			}

		} catch(Exception e) {
			logger.error("ignore User Session Info Remove Error " );
		}

	}


	@SuppressWarnings("unchecked")
	@Override
	public void attributeAdded(HttpSessionBindingEvent bindingEvent) {
		if( logger.isDebugEnabled() ) {
        	logger.debug(" attributeAdded :" + bindingEvent.getName() );
        }
		if( "LOGIN_INFO".equals(bindingEvent.getName()) ) {
			HttpSession session = bindingEvent.getSession();
			if( session != null ) {

				Map<String,Object>userInfo = (Map<String,Object>)(session.getAttribute("LOGIN_INFO"));
				if( userInfo != null ) {
					String userId = (String)userInfo.get("USER_ID");

					// 이미 로그인 한 계정이 존재하면 삭제
					if( StringUtils.isNotEmpty(userId) ) {
						if( sessionMap.get( userId ) != null ) {
							sessionMap.remove(userId);
							if( logger.isDebugEnabled() ) {
					        	logger.debug("Session attributeAdded sessionMap.remove() USER_ID = " + userId );
					        }
						}
					}

					// 세션 저장
					sessionMap.put(userId, session.getId());
					if( logger.isDebugEnabled() ) {
			        	logger.debug("Session attributeAdded sessionMap.put() USER_ID = " + userId + ", SESSION_ID =" + session.getId());
			        }
				}
			}
		}

	}

	@Override
	public void attributeRemoved(HttpSessionBindingEvent bindingEvent) {
/*
		HttpSession session = bindingEvent.getSession();
		if( logger.isDebugEnabled() ) {
        	logger.debug(" attributeRemoved :" + bindingEvent.getName() );
        	logger.debug(" attributeRemoved :" + session.getId() );
        }
*/
/*		if( "LOGIN_INFO".equals(bindingEvent.getName()) ) {
			HttpSession session = bindingEvent.getSession();
			if( session != null ) {
				Map<String,Object>userInfo = (Map<String,Object>)(session.getAttribute("LOGIN_INFO"));

				if( userInfo != null ) {
					String userId = (String)userInfo.get("USER_ID");

					String sessionKey = sessionMap.get( userId );
					if( sessionKey != null && sessionKey.equals( session.getId()) ) {
						sessionMap.remove(userId);
						if( logger.isDebugEnabled() ) {
				        	logger.debug(" sessionMap.put() USER_ID = " + userId + ", sessionKey =" + sessionKey);
				        }
					}

				}
			}
		}
*/
	}

	@Override
	public void attributeReplaced(HttpSessionBindingEvent bindingEvent) {
		// TODO Auto-generated method stub
		if( logger.isDebugEnabled() ) {
        	logger.debug(" attributeReplaced :" + bindingEvent.getName() );
        }
	}


	public static boolean checkExistSessoon(String userId, String sessionId) {

		String sessionKey = sessionMap.get( userId );
		if( sessionKey == null ) {
			return false;
		}
		if( sessionKey.equals( sessionId )) {
			return true;
		}

		return false;
	}
}


