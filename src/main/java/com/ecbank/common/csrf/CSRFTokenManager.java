package com.ecbank.common.csrf;

import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

/**
 * A manager for the CSRF token for a given session. The {@link #getTokenForSession(HttpSession)} should used to
 * obtain the token value for the current session (and this should be the only way to obtain the token value).
 *

 */
public class CSRFTokenManager {

	/**
	 * The token parameter name
	 */
	public static final String CSRF_PARAM_NAME = "__CSRF_TOKEN__";

	/**
	 * The location on the session which stores the token
	 */
	private final static String CSRF_TOKEN_FOR_SESSION_ATTR_NAME = CSRFTokenManager.class.getName() + ".tokenval";

	public static String getTokenForSession (HttpSession session) {
		String token = null;
		// I cannot allow more than one token on a session - in the case of two requests trying to
		// init the token concurrently

		// 세션에서 하나의 CSRF 토큰을 만들어서 사용하기 위해
		// sesson에 대해 동기화를 설정하고
		// 세션에 CSRF 토큰이 저장되어 있지 않은 경우에 새로운 CSRF 토큰을 생성하여 세션에 저장한다.
		// 이미 CSRF 토큰이 생성되어 세션에 저장되어 있다면 저장된 세션에  저장되어 있는 CSRF 토큰을 이용한다.
		//
		synchronized (session) {
			token = (String) session.getAttribute(CSRF_TOKEN_FOR_SESSION_ATTR_NAME);
			if (null==token) {
				token=UUID.randomUUID().toString();
				//logger.info("TOKEN: "+token);
				session.setAttribute(CSRF_TOKEN_FOR_SESSION_ATTR_NAME, token);
			}
		}
		return token;
	}

	/**
	 * Extracts the token value from the session
	 * @param request
	 * @return
	 */
	public static String getTokenFromRequest(HttpServletRequest request) {

		String token = StringUtils.defaultString(request.getHeader(CSRF_PARAM_NAME));
		if( "".equals(token)) {
			token = request.getParameter(CSRF_PARAM_NAME);
		}
		return token;
	}


}