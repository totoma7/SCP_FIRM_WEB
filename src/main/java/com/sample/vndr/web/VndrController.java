package com.sample.vndr.web;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.sample.vndr.service.VndrService;

@RestController
public class VndrController {

    final static Logger logger = LoggerFactory.getLogger(VndrController.class);

    @Autowired
    private VndrService vndrService;


    @RequestMapping(value = "/vendor", method = {RequestMethod.POST, RequestMethod.GET})
    public List<Map<String, Object>> getVendors(@RequestBody Map<String, Object> paramMap){

        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        Map<String, Object> dataMap = new HashMap();

        try {

            dataList = vndrService.selectVndrList(dataMap);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            logger.error(e.getLocalizedMessage(), e);
        }

        return dataList;
    }


}
