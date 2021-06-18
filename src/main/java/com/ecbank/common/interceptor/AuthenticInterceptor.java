package com.ecbank.common.interceptor;


import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;

import com.ecbank.common.json.JsonData;
import com.ecbank.common.session.UserSessionUtil;
import com.ecbank.common.session.WebSessionAttributeListener;
import com.fasterxml.jackson.databind.ObjectMapper;






/**
 * 인증여부 체크 인터셉터
 *
 */
public class AuthenticInterceptor implements HandlerInterceptor {

	protected final Log logger = LogFactory.getLog(getClass());

	@Value("${api.user.key}")
    private String userKey;


	private Set<String> uncheckUrl;

	public void setUncheckUrl(Set<String> uncheckUrl){

		this.uncheckUrl = uncheckUrl;
	}

	/**
	 * 세션에 로그인정보가 있는지 여부로 인증 여부를 체크한다.
	 * 로그인정보가 없다면, 로그인 페이지로 이동한다.
	 */
	@Autowired(required=false)
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws ServletException {

		try {

			String strReqMapping = request.getRequestURI().replaceFirst(request.getContextPath(), "");
			String contentType = StringUtils.defaultIfBlank(request.getContentType(), "");
		/*
			//String[] reqMappingArray = strReqMapping.split("/");
			if( logger.isDebugEnabled()) {
				logger.debug("strReqMapping = " + strReqMapping);
				logger.debug("contentType = " + contentType);
				//for( int i = 0; i < reqMappingArray.length; i++) {
				//	logger.debug("reqMappingArray = " + reqMappingArray[i]);
				//}
				//logger.debug("getContextPath = " + request.getContextPath());
				logger.debug("getRequestedSessionId = " + request.getRequestedSessionId());
				//logger.debug("isRequestedSessionIdFromCookie = " + request.isRequestedSessionIdFromCookie());
				//logger.debug("isRequestedSessionIdValid = " + request.isRequestedSessionIdValid());
				//logger.debug("getScheme = " + request.getScheme());
				//logger.debug("getServerName = " + request.getServerName());
				//logger.debug("getRequestURL = " + request.getRequestURL());
				//logger.debug("getServletPath = " + request.getServletPath());
			}

			Enumeration headers = request.getHeaderNames();
			while( headers.hasMoreElements()) {
				String headerName = (String)headers.nextElement();
				String value = request.getHeader( headerName);
				if( logger.isDebugEnabled()) {
					logger.debug("headerName=" + headerName + ", value="+ value);
				}
			}
			*/
			HttpSession session = request.getSession();
			Map<String,Object> loginInfo =  UserSessionUtil.getUserSession(session);
			if( logger.isDebugEnabled()) {
//				logger.debug("loginInfo = " + loginInfo);
			}

			boolean isAuthenticated = false;
			boolean isDupLogin = false;

			if( loginInfo == null ) {
				isAuthenticated = false;
			} else {
				String userId = (String)loginInfo.get("USER_ID");
				if( userId == null ) {
					isAuthenticated = false;
				} else {
					isAuthenticated = true;

					if( WebSessionAttributeListener.checkExistSessoon( userId, session.getId() ) == false) {
						isDupLogin = true;
					}
				}
			}



			if( logger.isDebugEnabled()) {
				logger.debug(" URI = " +  request.getRequestURI() + ",  isAuthenticated = " + isAuthenticated);
			}

			if ( isAuthenticated == false) {
				//Ajax 콜인지 아닌지 판단
                if(isAjaxRequest(request)){
                    //response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                	response.setCharacterEncoding("UTF-8");
                    response.getWriter().write( this.makeJsonErrorMsg("_ACCESS_DENIED_", "사용자 세션이 만료되었거나 로그인 하지 않았습니다.") );
                    return false;
                } else {
                	ModelAndView modelAndView = new ModelAndView("redirect:/common/accessDenied.jsp");
                	throw new ModelAndViewDefiningException(modelAndView);
                }
			} else {


				// 중복 로그인 방지
				if( isDupLogin == true ) {
					if( logger.isDebugEnabled()) {
						logger.debug("======= DUPLICATE USER  =======");
					}
					UserSessionUtil.removeUserSession(request);
					if(isAjaxRequest(request)){

	                    //response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
	                    //ModelAndView modelAndView = new ModelAndView("redirect:/common/dupAccessDenied.jsp");
	                	//throw new ModelAndViewDefiningException(modelAndView);
						response.setCharacterEncoding("UTF-8");
	                	response.getWriter().write( this.makeJsonErrorMsg("_DUP_USER_", "다른 곳에서 해당 계정으로 로그인 되어 로그아웃 처리되었습니다.") );
	                	return false;
	                } else {
	                	ModelAndView modelAndView = new ModelAndView("redirect:/common/dupAccessDenied.jsp");
	                	throw new ModelAndViewDefiningException(modelAndView);
	                }

				}

				// 세션 재사용 방지
				if( UserSessionUtil.matchSessionKey(request, (String)loginInfo.get("USER_ID"), (String)loginInfo.get("SESSION_KEY")) == false ) {
					if( logger.isDebugEnabled()) {
						logger.debug("======= NO AUTH USER  =======");
					}

					if(isAjaxRequest(request)){
						response.setCharacterEncoding("UTF-8");
	                	response.getWriter().write( this.makeJsonErrorMsg("_NO_AUTH_", "권한이 없습니다.(허용되지 않는 요청을 하셨습니다)") );
	                	return false;
	                } else {
	                	ModelAndView modelAndView = new ModelAndView("redirect:/common/noAuthError.jsp");
	                	throw new ModelAndViewDefiningException(modelAndView);
	                }
				}
			}


			if( isValidContextByUser(request, (String)loginInfo.get("USER_ID"),  (String)loginInfo.get("ROLE_CD")) == false ) {

				ModelAndView modelAndView = new ModelAndView("redirect:/common/noAuthError.jsp");
				throw new ModelAndViewDefiningException(modelAndView);
			}


			boolean isPassUrl = false;
			for(Iterator<String> it = this.uncheckUrl.iterator(); it.hasNext();){

				if(Pattern.matches((String)it.next(), strReqMapping)){// 정규표현식을 이용해서 요청 URI가 허용된 URL에 맞는지 점검함.
					isPassUrl = true;
					break;
				}
			}

			/*if( logger.isDebugEnabled()) {
				logger.debug("MENU AUTH CHECK : isPassUrl=" + isPassUrl );
			}*/
			if( isPassUrl == false) {
				/*if( logger.isDebugEnabled()) {
					logger.debug("MENU AUTH CHECK : contentType=" + contentType );
				}*/
				/*if( reqMappingArray.length <= 2 ) {
					if( logger.isDebugEnabled()) {
						logger.debug("RequestMapping depth <= 2");
					}
				} else */
				if ("".equals(contentType) || "application/x-www-form-urlencoded".equals(contentType)) {
					/*if( logger.isDebugEnabled()) {
						logger.debug("메뉴 권한 체크 ");
					}*/


					Boolean existPageAuth = false;
					String gMenuNo = StringUtils.defaultIfBlank((String)request.getParameter("G_MENU_NO"), "");
					String topMenuNo = "";

					if( ! "".equals(gMenuNo) ) {
						Map<String,Object> menuMap = (Map<String,Object>)loginInfo.get("MENU_MAP");

						if( menuMap.containsKey(gMenuNo)) {
							existPageAuth = true;

							topMenuNo = (String)((Map<String,Object>)menuMap.get(gMenuNo)).get("TOP_MENU_NO");
						} else {
							existPageAuth = false;
						}
					} else {
						existPageAuth = false;
					}

					if( logger.isDebugEnabled()) {
						logger.debug("MENU AUTH CHECK : gMenuNo=" + gMenuNo + ", existPageAuth ="+ existPageAuth);
					}

					if( existPageAuth == false) {

						ModelAndView modelAndView = new ModelAndView("redirect:/common/noAuthError.jsp");
						throw new ModelAndViewDefiningException(modelAndView);
					}


					if( isValidMenu(request, topMenuNo) == false ) {
						ModelAndView modelAndView = new ModelAndView("redirect:/common/noAuthError.jsp");
						throw new ModelAndViewDefiningException(modelAndView);
					}
				}
			}



		} catch ( ModelAndViewDefiningException mde) {
			throw mde;
		} catch ( Exception e ) {
			logger.error(e.getLocalizedMessage(),e);

			ModelAndView modelAndView = new ModelAndView("redirect:/common/error.jsp");
			throw new ModelAndViewDefiningException(modelAndView);
		}
		return true;
	}


	/**
	 * Ajax Request 확인
	 * @param request
	 * @return
	 */
	public boolean isAjaxRequest(HttpServletRequest request) {
		/*Enumeration headers = request.getHeaderNames();
		while( headers.hasMoreElements()) {
			String headerName = (String)headers.nextElement();
			String value = request.getHeader( headerName);
			if( logger.isDebugEnabled()) {
				logger.debug("headerName=" + headerName + ", value="+ value);
			}
		}*/

        String ajaxHeader = "ajax";
        /*if( logger.isDebugEnabled()) {
        	logger.info("======= req.getHeader('ajax') : " + request.getHeader(ajaxHeader));
        }*/
        return request.getHeader(ajaxHeader) != null && "true".equals(request.getHeader(ajaxHeader));

    }




	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object controller, ModelAndView modelAndView) throws Exception {
		/*
		if( logger.isDebugEnabled()) {
			logger.debug("postHandle  called");

			logger.debug("modelAndView" + modelAndView);
			//logger.debug("modelAndView.getModelMap() = " + modelAndView.getModel());
			//logger.debug("response" + response.toString());

			logger.debug("request.getRequestURI() = " + request.getRequestURI());
			logger.debug("contentType = " + request.getContentType());
			logger.debug("getContextPath = " + request.getContextPath());
			logger.debug("getRequestedSessionId = " + request.getRequestedSessionId());


		}
	*/

		try {
			String gTopMenuNo = StringUtils.defaultIfBlank(request.getParameter("G_TOP_MENU_NO"), "");
			String gMenuNo = StringUtils.defaultIfBlank(request.getParameter("G_MENU_NO"), "");

			if( modelAndView != null ) {
				Map<String,Object> model = modelAndView.getModel();
				//logger.debug("model" + model);

				if(StringUtils.isNotEmpty((String)model.get("G_MENU_NO")) ) {
					gTopMenuNo = (String)(String)model.get("G_TOP_MENU_NO");
					gMenuNo = (String)(String)model.get("G_MENU_NO");
				}
			}
			/*
			if( logger.isDebugEnabled()) {
				logger.debug("gTopMenuNo=" + gTopMenuNo + ", gMenuNo=" + gMenuNo);
			}
			*/

			response.setHeader("Cache-Control","no-store");
            response.setHeader("Pragma","no-cache");
            response.setDateHeader("Expires",0);
            if (request.getProtocol().equals("HTTP/1.1"))
                    response.setHeader("Cache-Control", "no-cache");

			if( modelAndView != null ) {

				if( ! "".equals(gTopMenuNo)) {
					modelAndView.addObject("G_TOP_MENU_NO", gTopMenuNo);
				}

				if( ! "".equals(gMenuNo)) {
					modelAndView.addObject("G_MENU_NO", gMenuNo);
				}
			}

		} catch( Exception e) {
			logger.error(e.getLocalizedMessage(),e);
		}
	}




	/**
	 * 사용자 유형별 context 체크
	 * 관리자는 관리자 context로만 접근가능
	 *
	 * @param request
	 * @param userId
	 * @param roleCd
	 * @return
	 */
	private boolean isValidContextByUser(HttpServletRequest request, String userId, String roleCd) {

	    // ex) @Value("${api.user.key}") 처럼 데이터 불러오기
		String adminContext = "";

		if( StringUtils.isNotEmpty(adminContext) ) {
			String contextPath = request.getContextPath();
			if( logger.isDebugEnabled()) {
				logger.debug("adminContext = " + adminContext);
				logger.debug("contextPath = " + contextPath);
			}
			/* admin 페이지와 일반페이지 분리 R9999 : 관리자 쪽 , 그외 로직 */
			/*
	    	if (roleList.contains("R9999")) {
	  			if( contextPath.equals("/" + adminContext) ) {
					return true;
				} else {
					return false;
				}

	    	} else {
	    		if( contextPath.equals("/" + adminContext) ) {
					return false;
				} else {
					return true;
				}

	    	}
	    	*/
		}
		return true;
	}

	/**
	 * 관리자 context가 아니면서 관리자 메뉴 호출시
	 *
	 * @param request
	 * @param topMenuNo
	 * @return
	 */
	private boolean isValidMenu(HttpServletRequest request, String topMenuNo) {

	    // ex) @Value("${api.user.key}") 처럼 데이터 불러오기
		String adminContext = "";

		if( StringUtils.isNotEmpty(adminContext) ) {
			String contextPath = request.getContextPath();
			if( logger.isDebugEnabled()) {
				logger.debug("adminContext = " + adminContext);
				logger.debug("contextPath = " + contextPath);
			}

  			if( ! contextPath.equals("/" + adminContext) ) {
  			    // ex) @Value("${api.user.key}") 처럼 데이터 불러오기
  				String sysMngTopMenuNo = "";

  				if( logger.isDebugEnabled()) {
  					logger.debug("sysMngTopMenuNo = " + sysMngTopMenuNo);
  					logger.debug("topMenuNo = " + topMenuNo);
  				}
  				if( sysMngTopMenuNo.equals(topMenuNo) ) {
  					return false;
  				}

			}
		}
		return true;
	}


	public String makeJsonErrorMsg( String errCd, String errMsg ) throws Exception {

		JsonData jsonData = new JsonData();
		jsonData.setMessage(errCd);
		jsonData.setErrMsg( errMsg );
		ObjectMapper mapper = new ObjectMapper();
		String retVal = mapper.writeValueAsString(jsonData);
		return retVal;

	}

}