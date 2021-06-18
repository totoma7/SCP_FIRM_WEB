package com.ecbank.common.session;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ecbank.common.crypt.DigestUtil;
import com.ecbank.common.util.WebUtil;



/**
 * Spring에서 제공하는 RequestContextHolder 를 이용하여
 * request 객체를 service까지 전달하지 않고 사용할 수 있게 해줌
 * </pre>
 */

public class UserSessionUtil {

	private static final Logger logger = LoggerFactory.getLogger(UserSessionUtil.class);

	/**
	 * 사용자 session 정보
	 *
	 * @param String  attribute key name
	 * @return Object attribute obj
	 */
	public static Map<String,Object> getUserSession(HttpServletRequest request) throws Exception {

		try {
			HttpSession session = request.getSession();

			if( session != null ) {
				return getUserSession(session);
			} else {
				return null;
			}
		} catch(Exception e) {

			logger.error(e.getLocalizedMessage(),e);
			throw e;
		}

	}

	/**
	 * 사용자 session 정보
	 *
	 * @param String  attribute key name
	 * @return Object attribute obj
	 */
	@SuppressWarnings("unchecked")
	public static Map<String,Object> getUserSession(HttpSession session)  throws Exception {

		try  {
			if( session != null ) {
				return (Map<String,Object>)(session.getAttribute("LOGIN_INFO"));
			} else {
				return null;
			}
		} catch(Exception e) {
			logger.error(e.getLocalizedMessage(),e);
			throw e;
		}

	}

	/**
     * 사용자 session 정보 설정
     *
     * @param String  attribute key name
     * @param Object  attribute obj
     * @return void
     */
	public static void setUserSession(HttpServletRequest request, Map<String,Object> userInfo) throws Exception {

		String sessionKey = getSessionKey(request, (String)(userInfo).get("USER_ID"));
		userInfo.put("SESSION_KEY", sessionKey);
		request.getSession().setAttribute("LOGIN_INFO", userInfo);
	}


   /**
    * 사용자 session 정보 삭제
    *
    * @param String  attribute key name
    * @param Object  attribute obj
    * @return void
    */
	public static void removeUserSession(HttpServletRequest request) {

		HttpSession session = request.getSession(false);

		if( session != null ) {
			removeUserSession(session);
		}

	}


   /**
    * 사용자 session 정보 삭제
    *
    * @param String  attribute key name
    * @param Object  attribute obj
    * @return void
    */
	public static void removeUserSession(HttpSession session) {

		try {
			if( session != null ) {
				session.invalidate();
				/*
				String sessionId = session.getId();

				Map<String,Object> userInfo = getUserSession(session);
				if( userInfo != null ) {
					userInfo.clear();
					userInfo = null;

					// 처리 사항 없음
					//session.removeAttribute("LOGIN_INFO");

				}
				*/
			}
		} catch(Exception e) {
			logger.error(e.getLocalizedMessage(),e);
			//throw e;
		}
	}

	/**
	 * session 재사용 방지를 위한 고유키 생성
	 * @param request
	 * @param userId
	 * @param userIp
	 * @return
	 */
	public static String getSessionKey(HttpServletRequest request, String userId) throws Exception {

		String userIp = WebUtil.getClientIP(request);
		String userAgent = request.getHeader("user-agent");

		String token = userId + "|" + userIp + "|" + userAgent;
		String sessionKey = DigestUtil.digest(token);

		if( logger.isDebugEnabled() ) {
			logger.debug( "getSessionKey USER_ID = [" + userId + "], USER_IP = [" + userIp + "], USER-AGENT = [" + userAgent + "]");
			logger.debug( "getSessionKey TOKEN = [" + token + "], SESSION_KEY= [" + sessionKey + "]");
		}

		return sessionKey;
	}

	/**
	 * sesson key 동일여부 체크
	 * @param request
	 * @param userId
	 * @param userIp
	 * @return
	 */
	public static boolean matchSessionKey(HttpServletRequest request, String userId, String currSessionKey) throws Exception {

		String sessionKey = getSessionKey(request, userId);

		/*if( logger.isDebugEnabled() ) {
			logger.debug( "matchSessionKey SESSION_KEY = [" + sessionKey + "], CURR_SESSION_KEY= [" + currSessionKey + "]");
		}
*/
		if( sessionKey.equals(currSessionKey )) {
			return true;
		}

		if( logger.isDebugEnabled() ) {
			logger.debug( "matchSessionKey NOT EQUAL SESSION_KEY = [" + sessionKey + "], CURR_SESSION_KEY= [" + currSessionKey + "]");
		}
		return false;
	}
}
