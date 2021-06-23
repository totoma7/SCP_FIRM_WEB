package com.sample.trns.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ecbank.common.json.JsonData;
import com.sample.trns.service.TransService;

@Controller
public class TransferController {

	final static Logger logger = LoggerFactory.getLogger(TransferController.class);


	@Autowired
    private TransService transService;



    /**
	 * 거래원장 리스트
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/trnBookList.do", method = RequestMethod.POST)
    @ResponseBody
    public JsonData trnBookList(@RequestBody Map<String,Object> paramMap, HttpServletRequest request, ModelMap model) throws Exception {
		Map<String, Object> rtn = new HashMap<String,Object>();
    	JsonData jsonData = new JsonData();

        try {

    		if( logger.isDebugEnabled()) {
    			logger.debug("paramMap = " + paramMap);
    		}

        	List<Map<String,Object>> dataList = transService.selectTrnBookList(paramMap);

        	jsonData.setRows(dataList);

        } catch (Exception e) {
        	logger.error(e.getMessage());

            rtn.put("CODE_NM", e.getMessage());

            jsonData.setCause(e);
            jsonData.setStatus("FAIL");
            jsonData.addFieldsAll(rtn);
        }

        return jsonData;
    }


	/**
     * 거래원장 리스트
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/trnBookListSample.do", method = RequestMethod.POST)
    @ResponseBody
    public JsonData trnBookListSample(@RequestBody Map<String,Object> paramMap, HttpServletRequest request, ModelMap model) throws Exception {
        Map<String, Object> rtn = new HashMap<String,Object>();
        JsonData jsonData = new JsonData();

        try {

            if( logger.isDebugEnabled()) {
                logger.debug("paramMap = " + paramMap);
            }

            List<Map<String,Object>> dataList = transService.selectTrnBookListSample(paramMap);

            jsonData.setRows(dataList);

        } catch (Exception e) {
            logger.error(e.getMessage());

            rtn.put("CODE_NM", e.getMessage());

            jsonData.setCause(e);
            jsonData.setStatus("FAIL");
            jsonData.addFieldsAll(rtn);
        }

        return jsonData;
    }

}