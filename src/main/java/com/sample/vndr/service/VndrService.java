package com.sample.vndr.service;

import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VndrService {

    final static Logger logger = LoggerFactory.getLogger(VndrService.class);

    @Autowired
    VndrDAO vndDao;

    public List<Map<String, Object>> selectVndrList(Map<String, Object> paramMap) throws Exception{
        return vndDao.selectVndrList(paramMap);
    }

}
