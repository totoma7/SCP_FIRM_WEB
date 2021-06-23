package com.sample.code.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ecbank.common.json.JsonData;
import com.ecbank.common.mvc.web.BaseController;
import com.sample.code.service.CodeService;


@Controller
public class CodeController extends BaseController {

    final static Logger logger = LoggerFactory.getLogger(CodeController.class);

	@Autowired
    private CodeService codeService;


	/**
	 * 임시적으로
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/codeList.do", method = RequestMethod.POST)
    public String ntsTransView(@RequestParam Map<String, Object> paramMap, HttpServletRequest request, ModelMap model) throws Exception {

        if( logger.isDebugEnabled()) {
            logger.debug("paramMap = " + paramMap);
        }

        return "code/codeList";
    }


	@RequestMapping(value = "/code/grpCodeList.do", method = RequestMethod.POST)
    @ResponseBody
    public JsonData getGrpCodeList(@RequestBody Map<String, Object> paramMap, HttpServletRequest request, ModelMap model) throws Exception {

        JsonData jsonData = new JsonData();

        try {
            List<Map<String, Object>> dataList = codeService.selectGrpCodeList(paramMap);
            jsonData.setRows(dataList);

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            jsonData.setErrMsg("조회 실패 하였습니다.");
            jsonData.setCause(e);
        }

        return jsonData;
    }


	@RequestMapping(value = "/code/codeDtlList.do", method = RequestMethod.POST)
    @ResponseBody
    public JsonData getCodeList(@RequestBody Map<String, Object> paramMap, HttpServletRequest request, ModelMap model) throws Exception {

        JsonData jsonData = new JsonData();

        try {
            List<Map<String, Object>> dataList = codeService.selectCodeList(paramMap);
            jsonData.setRows(dataList);

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            jsonData.setErrMsg("조회 실패 하였습니다.");
            jsonData.setCause(e);
        }

        return jsonData;
    }


	@RequestMapping(value = "/code/saveGrpCode.do", method = RequestMethod.POST)
    @ResponseBody
    public JsonData saveGrpCode(@RequestBody Map<String, Object> paramMap, HttpServletRequest request, ModelMap model) throws Exception {
        JsonData jsonData = new JsonData();

        try {
            if (logger.isDebugEnabled()) {
                logger.debug("paramMap = " + paramMap);
            }

            codeService.saveGrpCode(paramMap);

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            jsonData.setErrMsg("저장에 실패 하였습니다.");
            jsonData.setCause(e);
        }

        return jsonData;
    }


	@RequestMapping(value = "/code/saveCode.do", method = RequestMethod.POST)
    @ResponseBody
    public JsonData saveCode(@RequestBody Map<String, Object> paramMap, HttpServletRequest request, ModelMap model) throws Exception {
        JsonData jsonData = new JsonData();

        try {
            if (logger.isDebugEnabled()) {
                logger.debug("paramMap = " + paramMap);
            }

            codeService.saveCode(paramMap);

            jsonData.addFields("CODE_GRP_ID", paramMap.get("CODE_GRP_ID"));

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            jsonData.setErrMsg("저장에 실패 하였습니다.");
            jsonData.setCause(e);
        }

        return jsonData;
    }


	@RequestMapping(value = "/code/cmmAcntNo.do", method = RequestMethod.POST)
    @ResponseBody
    public JsonData cmmAcntNo(@RequestBody Map<String, Object> paramMap, HttpServletRequest request, ModelMap model) throws Exception {

        JsonData jsonData = new JsonData();

        try {
            List<Map<String, Object>> dataList = codeService.selectCmmAcntNoList(paramMap);
            jsonData.setRows(dataList);

        } catch (Exception e) {
            logger.error(e.getLocalizedMessage(), e);
            jsonData.setErrMsg("조회 실패 하였습니다.");
            jsonData.setCause(e);
        }

        return jsonData;
    }

}