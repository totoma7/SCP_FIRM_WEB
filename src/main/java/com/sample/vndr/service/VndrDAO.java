package com.sample.vndr.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import egovframework.rte.psl.dataaccess.EgovAbstractMapper;

@Repository
public class VndrDAO extends EgovAbstractMapper {

    public List<Map<String, Object>> selectVndrList(Map<String, Object> paramMap) throws Exception{
        return selectList("VndrMapper.selectVndrList", paramMap);
    }


}
