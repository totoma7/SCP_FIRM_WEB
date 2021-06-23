package com.sample.code.service;

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
public class CodeDao extends EgovAbstractMapper {


	/**
	 * 코드 Master 목록를 조회한다.
	 *
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectGrpCodeList(Map<String, Object> paramMap) throws Exception{
		return selectList("CodeMapper.selectGrpCodeList", paramMap);
	}


	/**
     * 특정 codeId에 의 코드 목록을 조회한다.
     *
     * @param codeId
     * @return
     */
    public List<Map<String, Object>> selectCodeList(Map<String, Object> paramMap) throws Exception{
    	return selectList("CodeMapper.selectCodeList", paramMap);
    }


//	/**
//	 * 코드 Masger정보를 조회한다.
//	 *
//	 * @param codeId
//	 * @return
//	 */
//	public Map<String, Object> selectGrpCode(Map<String, Object> paramMap) throws Exception {
//		return (Map<String, Object>) selectOne("etax.CodeDAO.selectGrpCode", paramMap);
//	}
//
	/**
	 * 코드 마스터 정보를 등록한다.
	 *
	 * @param codeId
	 * @return
	 */
	public int insertGrpCode(Map<String, Object> paramMap) throws Exception{
		return insert("CodeMapper.insertGrpCode", paramMap);
	}

	/**
	 * 코드 마스터 수정
	 *
	 * @param codeId
	 * @return
	 */
	public int updateGrpCode(Map<String, Object> paramMap) throws Exception{
		return update("CodeMapper.updateGrpCode", paramMap);
	}

	/**
	 * 코드 마스터 삭제
	 *
	 * @param codeId
	 * @return
	 */
	public int deleteGrpCode(Map<String, Object> paramMap) throws Exception{
		return delete("CodeMapper.deleteGrpCode", paramMap);
	}

	/**
     * 코드 Detail 등록
     *
     * @param codeId
     * @return
     */
    public int insertCode(Map<String, Object> paramMap) throws Exception{
    	return insert("CodeMapper.insertCode", paramMap);
    }

    /**
     * 코드 Detail 수정
     *
     * @param codeId
     * @return
     */
    public int updateCode(Map<String, Object> paramMap) throws Exception{
    	return update("CodeMapper.updateCode", paramMap);
    }

    /**
     * 코드 Detail 삭제
     *
     * @param paramMap
     * @return
     */
    public int deleteCode(Map<String, Object> paramMap) throws Exception{
    	return update("CodeMapper.deleteCode", paramMap);
    }
//
//
//	/**
//     * 특정 codeId에 의 코드 목록을 조회한다.
//     * 매입전용 사업장 조회
//     *
//     * @param codeId
//     * @return
//     */
//    public List<Map<String, Object>> selectBuyCompCodeList(Map<String, Object> paramMap) throws Exception {
//        return selectList("etax.CodeDAO.selectBuyCompCodeList", paramMap);
//    }
//
//	/**
//     * 사업영역(AREA)의 부서 목록을 조회한다.
//     *
//     * @param codeId
//     * @return
//     */
//    public List<Map<String, Object>> selectCodeDeptList(Map<String, Object> paramMap) throws Exception {
//        return selectList("etax.CodeDAO.selectCodeDeptList", paramMap);
//    }
//
//	/**
//     * 사업영역의 목록을 조회한다.
//     *
//     * @param codeId
//     * @return
//     */
//    public List<Map<String, Object>> selectCodeAreaList(Map<String, Object> paramMap) throws Exception {
//        return selectList("etax.CodeDAO.selectCodeAreaList", paramMap);
//    }
//
//	/**
//	 * 코드 Detail정보를 조회한다.
//	 *
//	 * @param codeId
//	 * @return
//	 */
//	public Map<String, Object> selectCode(Map<String, Object> paramMap) throws Exception {
//		return (Map<String, Object>) selectOne("etax.CodeDAO.selectCode", paramMap);
//	}
//


//
//	/**
//	 * 코드 Detail 삭제
//	 *
//	 * @param codeId
//	 * @return
//	 */
//	public int deleteCodeByGrpCodeId(Map<String, Object> paramMap) throws Exception {
//		return delete("etax.CodeDAO.deleteCodeByGrpCodeId", paramMap);
//	}


    /**
     * 공통 지급계좌 조회
     *
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> selectCmmAcntNoList(Map<String, Object> paramMap) throws Exception{
    	return selectList("CodeMapper.selectCmmAcntNoList", paramMap);
    }


	public Map<String, Object> selectCmmAcntNo(Map<String, Object> paramMap) throws Exception{
		return selectOne("CodeMapper.selectCmmAcntNo",paramMap);
	}

	public Map<String, Object> selectCodeList(String[] arrCodes) {
		// TODO Auto-generated method stub
		return null;
	}

}
