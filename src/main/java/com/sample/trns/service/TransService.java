package com.sample.trns.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecbank.common.util.StringUtil;



@Service
public class TransService {

    @Autowired
    private TransDao transDao;

    public List<Map<String, Object>> selectTrnBookList(Map<String, Object> paramMap) throws Exception{

    	if(!"".equals(StringUtil.isNullToString(paramMap.get("S_ACNT_NO")))) {
    		paramMap.put("S_ACNT_NO",(paramMap.get("S_ACNT_NO")).toString().replace("-", ""));
    	}
    	if(!"".equals(StringUtil.isNullToString(paramMap.get("S_PRTN_ACNT_NO")))) {
    		paramMap.put("S_PRTN_ACNT_NO",(paramMap.get("S_PRTN_ACNT_NO")).toString().replace("-", ""));
    	}
    	if(!"".equals(StringUtil.isNullToString(paramMap.get("S_FROM_DT")))) {
    		paramMap.put("S_FROM_DT",(paramMap.get("S_FROM_DT")).toString().replace("-", ""));
    	}
    	if(!"".equals(StringUtil.isNullToString(paramMap.get("S_TO_DT")))) {
    		paramMap.put("S_TO_DT",(paramMap.get("S_TO_DT")).toString().replace("-", ""));
    	}

        List<Map<String, Object>> result = transDao.selectTrnBookList(paramMap);

        return result;
    }

    public List<Map<String, Object>> selectTrnBookListSample(Map<String, Object> paramMap) throws Exception{


        List<Map<String, Object>> result = transDao.selectTrnBookListSample(paramMap);

        return result;
    }


}
