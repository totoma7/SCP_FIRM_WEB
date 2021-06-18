package com.ecbank.common.util;

import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

/**
 *
 * 전문 번호 생성 DAO
 * @version 1.0
 * </pre>
 */
@Repository("TlNoUtil")
@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=Exception.class)
public class TlNoUtil extends EgovAbstractMapper {


	//private final ReentrantLock lock = new ReentrantLock();

	@SuppressWarnings("unchecked")
	public String selectTlNo(Map<String, Object>  paramMap) throws Exception {

		String nextSeq = null;
		synchronized (this) {
	        try {
	        	Map<String, Object> retMap = (Map<String, Object>)selectOne("TlNoMapper.selectTlNo", paramMap);

	        	if( retMap != null ) {

	        		nextSeq  = (String)retMap.get("NEXT_SEQ");
	        		retMap.put("REG_ID", "BATCH");
	        		int nCnt = updateNextTlNo(retMap);
	        		if(nCnt <= 0) {
	        			logger.error("=======> updateNextTlNo is not update....");
	        		}

	        	}else {
	        		paramMap.put("REG_ID", "BATCH");
	        		int nCnt = insert("TlNoMapper.insertTlNo", paramMap);
	        		if(nCnt>0) {
	        			//다시 한번 호출
	        			nextSeq = this.selectTlNo(paramMap);
	        		}
	        	}

	        } finally {
	            //lock.unlock();
	        }
		}

        return nextSeq;
	}

	private int updateNextTlNo(Map<String, Object> paramMap) throws Exception {
		return update("TlNoMapper.updateNextTlNo", paramMap);
	}

}
