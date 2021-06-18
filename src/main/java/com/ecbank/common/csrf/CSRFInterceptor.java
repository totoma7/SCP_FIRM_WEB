package com.ecbank.common.csrf;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;


public class CSRFInterceptor extends HandlerInterceptorAdapter {

	protected final static Log logger = LogFactory.getLog(CSRFInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		// POST 방식의 요청에 대해서만 인터셉트를 적용해야 함
		if (!request.getMethod().equalsIgnoreCase("POST") ) {
			/*if( logger.isDebugEnabled()) {
				logger.debug("Not a POST - allow the request");
			}*/
		    return true;
		} else {
			String contentType = StringUtils.defaultIfBlank(request.getContentType(), "");

			// This is a POST request - need to check the CSRF token
			//파라메터로 전달된 csrf 토큰값과 세션에 저장된 csrf토큰값을 비교하여 일치하는 경우
			//에만 요청을 처리한다.
			String sessionToken = CSRFTokenManager.getTokenForSession(request.getSession());
			String requestToken = CSRFTokenManager.getTokenFromRequest(request);



			if (sessionToken.equals(requestToken)) {
				return true;
			} else {
				// 유효하지 않은요청(CSRF_TOKEN이 없거나 틀린경우)
				if( logger.isInfoEnabled()) {
					logger.info("==== CSRF TOKEN NOT EQUAL sessionToken = " + sessionToken + " , requestToken = " + requestToken);
				}

				response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bad or missing CSRF value");
				//response.sendRedirect("main.do");
				return false;
			}
		}
	}
}