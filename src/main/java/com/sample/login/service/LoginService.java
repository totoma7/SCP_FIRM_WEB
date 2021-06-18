package com.sample.login.service;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.ecbank.common.crypt.DigestUtil;
import com.ecbank.common.mvc.service.BaseService;
import com.ecbank.common.util.StringUtil;
import com.ibm.icu.math.BigDecimal;




@Service
public class LoginService extends BaseService {

    protected final Log logger = LogFactory.getLog(LoginService.class);

    @Autowired
    private LoginDAO loginDAO;

    @Autowired
    private DigestUtil digestUtil;

    @Value("${spring.profiles.active}")
    private String useMode;

    /**
     * 사용자 로그인 처리한다.
     *
     *
     * 010 : 로그인 정보가 올바르지 않습니다.
     * 020 : 계정이 잠겨 있습니다. 관리자에게 문의하여 주십시요.
     * 030 : 비밀번호 5회 이상 불일치로 계정이 잠겼습니다. 관리자에게 문의하여 주십시요.
     * 040 : 임시 비밀번호 입니다. 비밀번호를 변경 후 다시 로그인하여 주십시요.
     * 050 : 장기 미사용으로 계정이 잠겨 있습니다.
     * 060 : 비밀번호 유효기간이 지났습니다. 비밀번호를 변경 후 다시 로그인하여 주십시요.
     * 070 : 사업자정보가 등록되어 있지 않습니다.\nSK텔레콤 담당자에게 거래처 등록을 요청해 주시기 바랍니다.
     *
     * @return
     * @throws Exception
     */
    public Map<String, Object> actionLogin(Map<String, Object> paramMap) throws ServiceException, Exception {

        Map<String, Object> retVal = new HashMap();

        // local, dev, prod
        String getRunMode = useMode;

        Map<String, Object> userInfo = loginDAO.selectUserLoginInfo(paramMap);

        String userId = null;


        if (userInfo != null) {

            // 사용자 아이디 체크
            userId = StringUtils.defaultIfBlank((String) userInfo.get("USER_ID"), "");
            if ("".equals(userId)) {
                logger.error("User Not Found Error : USER_ID is NULL");
                // throw new ServiceException( getMessage("fail.common.login"));
                retVal.put("RET_CODE", "010");
                retVal.put("RET_MSG", getMessage("fail.common.login"));
                return retVal;
            }

        } else {
            logger.error("User Not Found Error : USER_INFO is NULL");
            retVal.put("RET_CODE", "010");
            retVal.put("RET_MSG", getMessage("fail.common.login"));
            return retVal;
        }

        String isSsoYn = StringUtils.defaultString((String) paramMap.get("IS_SSO_YN"), "N");

        if ("N".equals(isSsoYn)) {
            // 계정 잠김 체크
            String lockYn = StringUtils.defaultString((String) userInfo.get("LOCK_YN"), "N");
            if ("Y".equals(lockYn)) {
                retVal.put("RET_CODE", "020");
                retVal.put("RET_MSG", getMessage("fail.common.login.lock"));
                return retVal;
            }

            // 임시비밀번호 체크
            String tepPwYn = StringUtils.defaultString((String) userInfo.get("TMP_PW_YN"), "N");
            if ("Y".equals(tepPwYn)) {
                retVal.put("RET_CODE", "040");
                retVal.put("RET_MSG", getMessage("fail.common.login.temp"));
                return retVal;
            }

            // 비밀번호 변경 유효기간 180일
//            int MAX_DIFF_DAY = 180;
//            String strDiffDay = String.valueOf(userInfo.get("LAST_LOGIN_DT"));
//            Integer diffDay = ObjectUtils.defaultIfNull(Integer.parseInt(strDiffDay), new Integer(0));
//            if (diffDay.compareTo(new Integer(MAX_DIFF_DAY)) > 0) {
//                retVal.put("RET_CODE", "060");
//                retVal.put("RET_MSG", getMessage("fail.common.login.pwd.period", null, "no search", Locale.KOREA));
//                return retVal;
//            }

        }

        retVal.put("RET_CODE", "000");
        retVal.put("USER_INFO", userInfo);

        loginDAO.updateUserLoginDt(paramMap);

        return retVal;
    }

    /**
     * 사용자 Role 목록을 조회한다.
     *
     * @return
     * @throws Exception
     */
    public String selectUserRoleForLogin(Map<String, Object> paramMap) throws ServiceException, Exception {

        return loginDAO.selectUserRoleForLogin(paramMap);

    }

    /**
     * 사용자 메뉴 목록을 조회한다.
     *
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> selectUserMenuList(Map<String, Object> paramMap) throws ServiceException, Exception {

        return loginDAO.selectUserMenuList(paramMap);
    }

    /**
     * 사용자 로그인 이력 등록
     *
     * @param paramMap
     * @return
     * @throws Exception
     */
    public void insertLogLogin(Map<String, Object> paramMap) throws ServiceException, Exception {

        loginDAO.insertLogLogin(paramMap);

    }

    public String selectAuthDeptList(Map<String, Object> paramMap) throws Exception {
        return loginDAO.selectAuthDeptList(paramMap);
    }

    /**
     * 관리자 IP 확인
     *
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> selectMngAllowIpList(Map<String, Object> paramMap) throws ServiceException, Exception {
        return loginDAO.selectMngAllowIpList(paramMap);
    }

}
