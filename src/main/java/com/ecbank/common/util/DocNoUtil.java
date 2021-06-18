package com.ecbank.common.util;

import java.util.Map;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

/**
 *
 * 문서 번호 생성 DAO
 * @version 1.0
 * </pre>
 */
@Repository("DocNoUtil")
@Transactional(propagation=Propagation.REQUIRES_NEW, rollbackFor=Exception.class)
public class DocNoUtil extends EgovAbstractMapper {


	//private final ReentrantLock lock = new ReentrantLock();

	@SuppressWarnings("unchecked")
	public String selectDocNo(Map<String, Object>  paramMap) throws Exception {

		//lock.lock();
		String nextSeq = null;
		synchronized (this) {
	        try {
	        	Map<String, Object> retMap = (Map<String, Object>)selectOne("DocNoMapper.selectDocNo", paramMap);

	        	//logger.info( "retMap = " + retMap);
	        	//Thread.sleep(1000 * 10);
	        	if( retMap != null ) {

	        		nextSeq  = (String)retMap.get("NEXT_SEQ");

	        		retMap.put("REG_ID", "BATCH");
	        		int nCnt = updateNextDocNo(retMap);

	        		if(nCnt <= 0) {
	        			logger.error("=======> UpdateNextDocNo is not update....");
	        		}
	        	}

	        } finally {
	            //lock.unlock();
	        }
		}

        return nextSeq;
	}

	private int updateNextDocNo(Map<String, Object> paramMap) throws Exception {
		return update("DocNoMapper.updateNextDocNo", paramMap);
	}

}
