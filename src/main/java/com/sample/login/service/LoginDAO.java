package com.sample.login.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;


/**
 *
 * 공통코드에 대한 데이터 접근 클래스를 정의한다
 *
 * @version 1.0
 *          </pre>
 */
@Repository
public class LoginDAO extends EgovAbstractMapper {

    /**
     * 사용자 정보
     *
     * @return
     * @throws Exception
     */
    public Map<String, Object> selectUserLoginInfo(Map<String, Object> paramMap) throws Exception {

        return selectOne("LoginMapper.selectUserLoginInfo", paramMap);
    }

    /**
     * 로그인 성공
     *
     * @return
     * @throws Exception
     */
    public int updateUserLoginDt(Map<String, Object> paramMap) throws Exception {

        return update("LoginMapper.updateUserLoginDt", paramMap);
    }

    /**
     * 로그인 실패
     *
     * @return
     * @throws Exception
     */
    public int updateUserLoginFail(Map<String, Object> paramMap) throws Exception {

        return update("LoginMapper.updateUserLoginFail", paramMap);
    }

    /**
     * 사용자 Role 목록 조회
     *
     * @param paramMap
     * @return
     * @throws Exception
     */
    public String selectUserRoleForLogin(Map<String, Object> paramMap) throws Exception {
        return selectOne("LoginMapper.selectUserRoleForLogin", paramMap);
    }

    /**
     * 사용자 메뉴 목록 조회
     *
     * @param paramMap
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> selectUserMenuList(Map<String, Object> paramMap) throws Exception {
        return selectList("LoginMapper.selectUserMenuList", paramMap);
    }

    /**
     * 사용자 로그인 이력 등록
     *
     * @param paramMap
     * @return
     * @throws Exception
     */
    public int insertLogLogin(Map<String, Object> paramMap) throws Exception {
        return insert("LoginMapper.insertLogLogin", paramMap);
    }

    public String selectAuthDeptList(Map<String, Object> paramMap) {
        return selectOne("LoginMapper.selectAuthDeptList", paramMap);
    }

    /**
     * 관리자 IP 확인
     *
     * @param paramMap
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> selectMngAllowIpList(Map<String, Object> paramMap) throws Exception {
        return selectList("LoginMapper.selectMngAllowIpList", paramMap);
    }

}
