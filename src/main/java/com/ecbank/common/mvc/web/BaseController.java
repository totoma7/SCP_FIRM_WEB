package com.ecbank.common.mvc.web;


import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.service.spi.ServiceException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.ecbank.common.session.UserSessionUtil;
import com.ecbank.common.util.WebUtil;




public class BaseController extends CommonController {


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
	public String getLoginType() throws Exception {
		String returnStr = null;
		Map<String,Object> userinfo  = UserSessionUtil.getUserSession(this.getRequest());
		if(userinfo != null){
			returnStr = StringUtils.defaultIfBlank((String)userinfo.get("LOGIN_TYPE"), null);
		}
		return returnStr;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() throws Exception {
		String returnStr = null;
		Map<String,Object> userinfo  = UserSessionUtil.getUserSession(this.getRequest());
		if(userinfo != null){
			returnStr = StringUtils.defaultIfBlank((String)userinfo.get("USER_ID"), null);
		}
		return returnStr;
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
	 * @return the bizArea
	 */
	public String getBizArea() throws Exception  {
		Map<String,Object> userinfo  = UserSessionUtil.getUserSession(this.getRequest());
		return (String)userinfo.get("BIZ_AREA");
	}

	/**
	 * @return the RoleAuth
	 */
	public String getRoleAuth() throws Exception  {
		Map<String,Object> userinfo  = UserSessionUtil.getUserSession(this.getRequest());
		return (String)userinfo.get("ROLE_AUTH");
	}

	/**
     * @return the UserEmail
     */
    public String getEmail() throws Exception  {
        Map<String,Object> userinfo  = UserSessionUtil.getUserSession(this.getRequest());
        return (String)userinfo.get("EMAIL");
    }

	/**
	 * @return the BROKER_YN
	 */
	public String getBrokerYn() throws Exception  {
		Map<String,Object> userinfo  = UserSessionUtil.getUserSession(this.getRequest());
		return (String)userinfo.get("BROKER_YN");
	}

	/**
	 * @return the userIp
	 */
	public String getUserIp() throws Exception  {

		return WebUtil.getClientIP(this.getRequest());
	}

	public String getServerIp() throws Exception {
		try
		{
		    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();)
		    {
		        NetworkInterface intf = en.nextElement();
		        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();)
		        {
		            InetAddress inetAddress = enumIpAddr.nextElement();
		            if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress() && inetAddress.isSiteLocalAddress())
		            {
		            	return inetAddress.getHostAddress().toString();
		            }
		        }
		    }
		}
		catch (SocketException ex) {}
		return null;
	}



	/**
	 * 저장 권한 체크
	 * @param request
	 * @return
	 * @throws ServiceException
	 */
	public void checkAuthSave(Map<String,Object>paramMap) throws ServiceException, Exception {

		try {
			String gMenuNo = StringUtils.defaultIfBlank((String)paramMap.get("G_MENU_NO"), "");

			Boolean existSaveAuth = false;
			if( ! "".equals(gMenuNo) ) {
				Map<String,Object> menuMap = (Map<String,Object>)this.getSessionAttribute("MENU_MAP");
				if( menuMap != null && menuMap.containsKey(gMenuNo)) {
					Map<String,Object> menuInfo = (Map<String,Object>)menuMap.get(gMenuNo);

					if( menuInfo != null ) {
						String authSave = StringUtils.defaultIfBlank((String)menuInfo.get("AUTH_SAVE") , "N");

						if( "Y".equals( authSave)) {
							existSaveAuth = true;
						}
					}
				}
			}

			if( existSaveAuth == false) {
				throw new ServiceException( getMessage( "NOT_EXIST_AUTH_SAVE") );
			}
		} catch( Exception e) {
			throw e;
		}

	}

	/**
	 * @return 롤코드 체크시
	 */
	public String getRoleCd() throws Exception  {
		Map<String,Object> userinfo  = UserSessionUtil.getUserSession(this.getRequest());
		return (String)userinfo.get("ROLE_CD");
	}


	/**
	 * 관리자여부 체크
	 * @return
	 * @throws Exception
	 */
	public boolean chkAdminAuth()  throws Exception {

		String roleAuth = (String)getSessionAttribute("ROLE_AUTH");

    	if ("MNG_AUTH".equals(roleAuth)) {
    		return true;
    	}  else {
    		return false;
    	}
	}
}
