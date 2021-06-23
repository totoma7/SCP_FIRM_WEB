package com.sample.trns.service;

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
public class TransDao extends EgovAbstractMapper {


	/**
	 * 거래원장 내역 조회
	 *
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> selectTrnBookList(Map<String, Object> paramMap) throws Exception{
		return selectList("TransMapper.selectTrnBookList", paramMap);
	}

	/**
     * 거래원장 내역 조회
     *
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> selectTrnBookListSample(Map<String, Object> paramMap) throws Exception{
        return selectList("TransMapper.selectTrnBookListSample", paramMap);
    }


	/*
	 * 자금이체 등록
	 */
	public int insertAcctTrans(Map<String, Object> map) throws Exception{
        return insert("TransMapper.insertAcctTrans", map);
    }

	/*
	 * 업체코드 조회
	 */
	public Map<String, Object> searchCpnCd(Map<String, Object> paramMap) throws Exception{
        return selectOne("TransMapper.searchCpnCd",paramMap);
    }

	/**
     * 자금이체 내역 조회
     *
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> selectTransList(Map<String, Object> paramMap) throws Exception{
        return selectList("TransMapper.selectTransList", paramMap);
    }

    /**
     * 자금이체 상세내역 조회
     *
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> selectTransDetailList(Map<String, Object> paramMap) throws Exception{
        return selectList("TransMapper.selectTransDetailList", paramMap);
    }

    /**
     * 가상계좌 리스트 조회
     *
     * @return
     * @throws Exception
     */
    public List<Map<String, Object>> selectVrtList(Map<String, Object> paramMap) throws Exception{
        return selectList("TransMapper.selectVrtList", paramMap);
    }


    /*
     * 가상계좌 상세 조회
     */
    public Map<String, Object> selectVrtDetail(Map<String, Object> paramMap) throws Exception{
        return selectOne("TransMapper.selectVrtDetail",paramMap);
    }

}
