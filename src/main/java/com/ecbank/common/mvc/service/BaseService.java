package com.ecbank.common.mvc.service;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ecbank.common.session.UserSessionUtil;
import com.ecbank.common.util.WebUtil;


public class BaseService extends CommonService {



   /**
    * request정보를 구한다.
    *
    * @param String  attribute key name
    * @return Object attribute obj
    */
   public static HttpServletRequest getRequest() {

	   return ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
   }

	/**
	 * Servlet Request 정보를 구한다.
	 *
	 * @return
	 */
	public ServletRequestAttributes getServletRequestAttributes() throws Exception{

		return (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
	}



	/**
	 * @return UserSession 이 특정값 리턴
	 */
	public Object getSessionAttribute(String key) throws Exception {
		Map<String,Object> userinfo  = UserSessionUtil.getUserSession(this.getRequest());
		return userinfo.get(key);
	}

	/**
	 * @return UserSession 이 특정값 리턴
	 */
	public String getUserAttr(String key) throws Exception {
		Map<String,Object> userinfo  = UserSessionUtil.getUserSession(this.getRequest());
		return (userinfo.get(key) == null) ? "" : String.valueOf(userinfo.get(key));
	}

	/**
	 * @return the userId
	 */
	public String getUserId() throws Exception {

		Map<String,Object> userinfo  = null;
		try {
			userinfo  = UserSessionUtil.getUserSession(this.getRequest());
			return (String)userinfo.get("USER_ID");
		} catch (Exception e) {
			return null;
		}
	}

	/**
	 * @return the userNm
	 */
	public String getUserNm() throws Exception {

		Map<String,Object> userinfo  = UserSessionUtil.getUserSession(this.getRequest());

		return (String)userinfo.get("USER_NM");
	}


	/**
	 * @return the userDept
	 */
	public String getDeptCd() throws Exception  {
		Map<String,Object> userinfo  = UserSessionUtil.getUserSession(this.getRequest());
		return (String)userinfo.get("DEPT");
	}

	/**
	 * @return the userDeptNm
	 */
	public String getDeptNm() throws Exception  {
		Map<String,Object> userinfo  = UserSessionUtil.getUserSession(this.getRequest());
		return (String)userinfo.get("DEPT_NM");
	}



	/**
	 * @return the SAPID
	 */
	public String getSapId() throws Exception  {
		Map<String,Object> userinfo  = UserSessionUtil.getUserSession(this.getRequest());
		return (String)userinfo.get("SAPID");
	}

	/**
	 * @return the ZUONR
	 */
	public String getZuonr() throws Exception  {
		Map<String,Object> userinfo  = UserSessionUtil.getUserSession(this.getRequest());
		return (String)userinfo.get("ZUONR");
	}



	/**
	 * @return the ZUONR_NM
	 */
	public String getZuonrNm() throws Exception  {
		Map<String,Object> userinfo  = UserSessionUtil.getUserSession(this.getRequest());
		return (String)userinfo.get("ZUONR_NM");
	}




	/**
	 * @return the userIp
	 */
	public String getUserIp() throws Exception  {

		return WebUtil.getClientIP(this.getRequest());
	}



	/**
	 * @return 공급업체 코드( 공급사 사이트에서만 사용)
	 */
	public String getVndCd() throws Exception  {
		Map<String,Object> userinfo  = UserSessionUtil.getUserSession(this.getRequest());
		return (String)userinfo.get("VND_CD");
	}




	/**
	 * 관리자여부 체크
	 * @return
	 * @throws Exception
	 */
	public boolean chkAdminAuth()  throws Exception {

		String roleCd = (String)getSessionAttribute("ROLE_CD");

    	if ("MNG_AUTH".equals(roleCd)) {
    		return true;
    	}  else {
    		return false;
    	}
	}

}
