package com.ecbank.common.session;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;


public class WebSessionListener implements HttpSessionListener {

	protected final Log logger = LogFactory.getLog(WebSessionListener.class);

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

			UserSessionUtil.removeUserSession(session);

			if( logger.isDebugEnabled() ) {
	        	logger.debug(" Session is invalidated |SESSION_ID :" + sessionId );
	        }
		} catch(Exception e) {
			logger.error("ignore User Session Info Remove Error " );
		}

	}
}


