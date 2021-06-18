package com.sample.login.controller;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ecbank.common.csrf.CSRFTokenManager;
import com.ecbank.common.json.JsonData;
import com.ecbank.common.mvc.web.BaseController;
import com.ecbank.common.session.UserSessionUtil;
import com.sample.login.service.LoginService;


@Controller
public class LoginController extends BaseController {

	final static Logger logger = LoggerFactory.getLogger(LoginController.class);

	@Autowired
    private LoginService loginService;

	@Value("${spring.profiles.active}")
    private String useMode;

	/**
     * 일반 로그인을 처리한다
     *
     * @param vo      - 아이디, 비밀번호가 담긴 LoginVO
     * @param request - 세션처리를 위한 HttpServletRequest
     * @return result - 로그인결과(세션정보)
     * @exception Exception
     */
	@RequestMapping(value="/login.do", method = {RequestMethod.POST})
	@ResponseBody
	public JsonData actionLogin(@RequestBody Map<String, Object> paramMap, HttpServletRequest request, ModelMap model) throws Exception {
		JsonData jsonData = new JsonData();


		String _userId = StringUtils.defaultIfBlank((String) paramMap.get("USER_ID"), "");

		try {

            if (logger.isDebugEnabled()) {

                logger.debug("paramMap = " + paramMap);

                logger.debug("getRequestedSessionId = " + request.getRequestedSessionId());
                logger.debug("isRequestedSessionIdFromCookie = " + request.isRequestedSessionIdFromCookie());
                logger.debug("isRequestedSessionIdValid = " + request.isRequestedSessionIdValid());
            }

            paramMap.put("LAST_LOGIN_IP", getUserIp());

            String getRunMode = useMode;

            if("local".equals( getRunMode)) {
                paramMap.put("USER_ID", "1012024");
                paramMap.put("PWD", "1");
            }

            boolean isValidUser = false;
            Map<String, Object> userInfo = null;

            Map<String, Object> retVal = loginService.actionLogin(paramMap);

            String retCode = StringUtils.defaultString((String) retVal.get("RET_CODE"));

            if ("000".equals(retCode)) {

                userInfo = (Map<String, Object>) retVal.get("USER_INFO");

                if (logger.isDebugEnabled()) {
                    logger.debug("userInfo = " + userInfo);
                }

                if (userInfo != null) {
                    String userId = (String) userInfo.get("USER_ID");

                    if (userId != null) {
                        isValidUser = true;
                    }
                }
            } else {
                isValidUser = false;
            }

            String forwardUrl = "loginView";

            if (isValidUser == true) {

//                String userRoleCd = loginService.selectUserRoleForLogin(paramMap);
//                userInfo.put("ROLE_CD", userRoleCd);
//
//                /*각각 권한 Setting*/
//                userInfo = setSessionAuth(userInfo, userRoleCd);
//
//                if (isValidContextByUser(request, _userId, userRoleCd) == false) {
//                    if (retVal == null) {
//                        retVal = new HashMap<String, Object>();
//                    }
//
//                    retVal.put("RET_CODE", "010");
//                    retVal.put("RET_MSG", getMessage("fail.common.login"));
//
//                } else {

//                    List<Map<String, Object>> userMenuList = loginService.selectUserMenuList(paramMap);
//
//                    String firstTopMenuNo = "";
//                    LinkedHashMap<String, Object> topMenuLinkedMap = new LinkedHashMap<String, Object>();
//
//                    LinkedHashMap<String, Object> menuLinkedMap = new LinkedHashMap<String, Object>();
//
//                    if (userMenuList != null) {
//                        String currTopMenuNo = "";
//
//                        String currDirMenuNo = "";
//
//                        String targetMenuNo = "";
//                        boolean isFindTopFirstMenu = false;
//                        boolean isFindFirstMenu = false;
//                        List<String> targetNotExistMenuList = new ArrayList<String>();
//
//                        List<Map<String, Object>> childMenuList = null;
//                        for (int i = 0, nSize = userMenuList.size(); i < nSize; i++) {
//                            Map<String, Object> menuInfo = userMenuList.get(i);
//                            String menuNo = StringUtils.defaultIfBlank((String) menuInfo.get("MENU_NO"), "");
//                            if (!"".equals(menuNo)) {
//                                menuLinkedMap.put(menuNo, menuInfo);
//
//                                BigDecimal bdMenuDepth = (BigDecimal) menuInfo.get("MENU_DEPTH");
//                                if (bdMenuDepth.compareTo(new BigDecimal(1)) == 0) {
//                                    if (!currTopMenuNo.equals(menuNo)) {
//                                        if ("".equals(currTopMenuNo)) {
//                                            firstTopMenuNo = menuNo;
//                                        }
//                                        currTopMenuNo = menuNo;
//                                        childMenuList = new ArrayList<Map<String, Object>>();
//
//                                        menuInfo.put("CHILD_MENU_LIST", childMenuList);
//                                        topMenuLinkedMap.put(menuNo, menuInfo);
//
//                                        isFindTopFirstMenu = false;
//                                        isFindFirstMenu = false;
//                                    }
//                                } else {
//                                    /*
//                                     * if( logger.isDebugEnabled()) { logger.debug( "menuNo =" + menuNo );
//                                     * logger.debug( "menuInfo =" + menuInfo ); logger.debug( "topMenuLinkedMap =" +
//                                     * topMenuLinkedMap ); logger.debug( "menuLinkedMap =" + menuLinkedMap );
//                                     * logger.debug( "targetNotExistMenuList =" + targetNotExistMenuList );
//                                     * logger.debug( "isFindTopFirstMenu =" + isFindTopFirstMenu ); logger.debug(
//                                     * "isFindFirstMenu =" + isFindFirstMenu ); }
//                                     */
//                                    if (childMenuList != null) {
//
//                                        String munuAddTf = "Y";
//                                        // 위수탁 세금계산서는 위수탁 업체일 경우만 메뉴 View처리
//                                        String strBrokerYn = StringUtils.defaultString((String)userInfo.get("BROKER_YN"));
//                                        if("MO100BRO".equals(menuNo) || "MO101BRO".equals(menuNo) || "MO200BRO".equals(menuNo) || "MO201BRO".equals(menuNo)){
//                                            if("N".equals(strBrokerYn)){//위수탁업체가 아닐 경우 Skip
//                                                munuAddTf = "N";
//                                            }
//                                        }
//
//                                        if("Y".equals(munuAddTf)){ // 메뉴 추가를 스킵 할지 여부 체크
//                                            childMenuList.add(menuInfo);
//
//                                            if ("DIR".equals((String) menuInfo.get("MENU_GUBUN"))) {
//                                                currDirMenuNo = menuNo;
//                                                targetNotExistMenuList.add(menuNo);
//                                                isFindFirstMenu = false;
//                                            } else if ("URL".equals((String) menuInfo.get("MENU_GUBUN"))) {
//
//                                                menuInfo.put("TARGET_MENU_NO", menuNo);
//                                                if (isFindFirstMenu == false) {
//                                                    if (isFindTopFirstMenu == false) {
//                                                        ((Map) menuLinkedMap.get(currTopMenuNo)).put("TARGET_MENU_NO",menuNo);
//                                                        isFindTopFirstMenu = true;
//                                                    }
//
//                                                    if (currDirMenuNo.equals((String) menuInfo.get("UPPER_MENU_NO"))) {
//                                                        for (int k = 0, notExistSize = targetNotExistMenuList.size(); k < notExistSize; k++) {
//                                                            ((Map) menuLinkedMap.get(targetNotExistMenuList.get(k))).put("TARGET_MENU_NO", menuNo);
//                                                        }
//                                                    }
//                                                    isFindFirstMenu = true;
//                                                    targetNotExistMenuList.clear();
//
//                                                }
//                                            }
//                                        }
//
//                                    }
//                                    /*
//                                     *
//                                     * if( logger.isDebugEnabled()) { logger.debug( "222 targetNotExistMenuList =" +
//                                     * targetNotExistMenuList ); logger.debug( "222 isFindTopFirstMenu =" +
//                                     * isFindTopFirstMenu ); logger.debug( "222 isFindFirstMenu =" + isFindFirstMenu
//                                     * ); }
//                                     *
//                                     */
//                                }
//
//                            }
//                        }

//                    }

//                    userInfo.put("TOP_MENU_MAP", topMenuLinkedMap);
//                    userInfo.put("FIRST_TOP_MENU_NO", firstTopMenuNo);
//                    userInfo.put("MENU_MAP", menuLinkedMap);
//                    userInfo.put("IS_SSO_YN", "N");

                    // session 생성
                    UserSessionUtil.setUserSession(request, userInfo);

                    // CSRF token 생성
                    CSRFTokenManager.getTokenForSession(request.getSession());

                    if (logger.isDebugEnabled()) {
                        logger.debug("LOGIN_INFO =" + UserSessionUtil.getUserSession(request));
                    }
                }

            jsonData.addFields("RET_CODE", retVal.get("RET_CODE"));
            jsonData.addFields("RET_MSG", retVal.get("RET_MSG"));

            try {
                paramMap.put("RET_CODE", retVal.get("RET_CODE"));
                paramMap.put("RET_MSG", retVal.get("RET_MSG"));
            } catch (Exception e) {
                logger.error(e.getLocalizedMessage(), e);
            }

        } catch (ServiceException se) {
            logger.error(se.getLocalizedMessage(), se);

            jsonData.addFields("RET_CODE", "999");
            jsonData.addFields("RET_MSG", se.getMessage());
            jsonData.setErrMsg(se.getMessage());
            jsonData.setCause(se);

            try {
                paramMap.put("RET_CODE", "999");
                paramMap.put("RET_MSG", se.getMessage());
            } catch (Exception _e) {
                logger.error(_e.getLocalizedMessage(), _e);
            }

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);

            jsonData.addFields("RET_CODE", "999");
            jsonData.addFields("RET_MSG", getMessage("fail.common.process"));
            jsonData.setErrMsg(getMessage("fail.common.process"));
            jsonData.setCause(e);

            try {
                paramMap.put("RET_CODE", "999");
                paramMap.put("RET_MSG", e.getMessage());
            } catch (Exception _e) {
                logger.error(_e.getLocalizedMessage(), _e);
            }

        }

        return jsonData;

    }


	/**
     * 로그인 화면으로 들어간다
     *
     * @param vo - 로그인후 이동할
     * @return 로그인 페이지
     * @exception Exception
     */
    @RequestMapping(value = "/loginView.do")
    public String loginView(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, ModelMap model)
            throws Exception {

        Map<String, Object> loginInfo = UserSessionUtil.getUserSession(request);

        if (loginInfo != null && loginInfo.get("USER_ID") != null) {

            // 세션 재사용 방지
            if (UserSessionUtil.matchSessionKey(request, (String) loginInfo.get("USER_ID"),
                    (String) loginInfo.get("SESSION_KEY")) == false) {
                if (logger.isDebugEnabled()) {
                    logger.debug("======= NO AUTH USER  =======");
                }

//                return "redirect:/common/noAuthError.jsp";
            }

            return "forward:/main.do";
        }
        return "loginView";
    }


    /**
     * TODO 상황에 맞게 변경 필요
     *  ★★★ 특수 권한 추가시 하기 소스에도 추가 처리 필요~
     */
    private Map<String, Object> setSessionAuth(Map<String, Object> userInfo, String userRoleCd) throws Exception {
        //세무팀 권한
        if("R0003".equals(userRoleCd) ){
            userInfo.put("ROLE_AUTH", "TAX_AUTH");
        //회계 권한 , 구매팀 ( 구매팀도 매출,매입 내역 전체적으로 봐야 한다고 함 )
        }else if( "R0006".equals(userRoleCd) || "R0005".equals(userRoleCd) ){
            userInfo.put("ROLE_AUTH", "ACCT_AUTH");
        //부서관리 권한
        }else if( "R0002".equals(userRoleCd) ){
            userInfo.put("ROLE_AUTH", "DEPT_AUTH");
            String deptList = loginService.selectAuthDeptList(userInfo);
            userInfo.put("DEPT_LIST", deptList);
        //지역 회계 권한
        }else if( "R0004".equals(userRoleCd) ){
            userInfo.put("ROLE_AUTH", "AREA_AUTH");
        //관리자 권한
        }else if( "R9999".equals(userRoleCd) ){
            userInfo.put("ROLE_AUTH", "MNG_AUTH");
        // VIEW 전용 권한, 고객지원, 감사
        }else if("R0008".equals(userRoleCd) || "R9998".equals(userRoleCd) ){
            userInfo.put("ROLE_AUTH", "VIEW_AUTH");
        // 기타권한들
        }else{
            userInfo.put("ROLE_AUTH", "USER_AUTH");
        }

        return userInfo;
    }


    /**
     * 사용자 유형별 context 체크 관리자는 관리자 context로만 접근가능
     *
     * @param request
     * @param userId
     * @param roleList
     * @return
     */
    private boolean isValidContextByUser(HttpServletRequest request, String userId, String roleList) {

        String adminContext = useMode;

        if ("local".equals(useMode)) {
            String contextPath = request.getContextPath();
            if (logger.isDebugEnabled()) {
                logger.debug("adminContext = " + adminContext);
                logger.debug("contextPath = " + contextPath);
            }
            /* admin 페이지와 일반페이지 분리 R9999 : 관리자 쪽 , 그외 로직 */
            /*
            if (roleList.contains("R9999")) {
                if (contextPath.equals("/" + adminContext)) {
                    return true;
                } else {
                    return false;
                }

            } else {
                if (contextPath.equals("/" + adminContext)) {
                    return false;
                } else {
                    return true;
                }

            }
            */
        }
        return true;
    }

}