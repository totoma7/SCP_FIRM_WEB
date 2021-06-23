package com.sample.code.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecbank.common.mvc.service.BaseService;






@Service
public class CodeService extends BaseService {

    @Autowired
    private CodeDao codeDao;

    public List<Map<String, Object>> selectGrpCodeList(Map<String, Object> paramMap) throws Exception {

        List<Map<String, Object>> result = codeDao.selectGrpCodeList(paramMap);

        return result;
    }


    public List<Map<String, Object>> selectCodeList(Map<String, Object> paramMap) throws Exception {

        List<Map<String, Object>> result = codeDao.selectCodeList(paramMap);

        return result;
    }

    public Map<String, Object> selectCodeList(String[] arrCodes) throws Exception {
        Map<String, Object> mapResult = new HashMap<>();

        for (String strCode : arrCodes) {
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("USE_YN", "Y");
            paramMap.put("CODE_GRP_ID", strCode);

            mapResult.put("CODELIST_".concat(strCode), selectCodeList(paramMap));
        }
        return mapResult;
    }


    /**
     * 코드 Master 정보 저장한다.
     *
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public void saveGrpCode(Map<String, Object> paramMap) throws Exception {
        Map<String, Object> dataList = (Map<String, Object>) paramMap.get("DATA_LIST");

        if (dataList != null) {
            List<Map<String, Object>> insertList = (List<Map<String, Object>>) dataList.get("CREATED");
            List<Map<String, Object>> updateList = (List<Map<String, Object>>) dataList.get("UPDATED");
            List<Map<String, Object>> deleteList = (List<Map<String, Object>>) dataList.get("DELETED");

            if (deleteList != null) {
                for (int i = 0, nSize = deleteList.size(); i < nSize; i++) {
                    Map<String, Object> dataMap = deleteList.get(i);

                    codeDao.deleteGrpCode(dataMap);
                }
            }

            if (updateList != null) {
                for (int i = 0, nSize = updateList.size(); i < nSize; i++) {
                    Map<String, Object> dataMap = updateList.get(i);
                    dataMap.put("UPD_ID", "");
                    codeDao.updateGrpCode(dataMap);
                }
            }

            if (insertList != null) {
                for (int i = 0, nSize = insertList.size(); i < nSize; i++) {
                    Map<String, Object> dataMap = insertList.get(i);
                    dataMap.put("REG_ID", "");
                    codeDao.insertGrpCode(dataMap);
                }
            }
        }
    }

    /**
     * 코드정보 저장한다.
     *
     * @return
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
	public void saveCode(Map<String, Object> paramMap) throws Exception {

        Map<String, Object> dataList = (Map<String, Object>) paramMap.get("DATA_LIST");

        if (dataList != null) {
            List<Map<String, Object>> insertList = (List<Map<String, Object>>) dataList.get("CREATED");
            List<Map<String, Object>> updateList = (List<Map<String, Object>>) dataList.get("UPDATED");
            List<Map<String, Object>> deleteList = (List<Map<String, Object>>) dataList.get("DELETED");

            if (deleteList != null) {
                for (int i = 0, nSize = deleteList.size(); i < nSize; i++) {
                    Map<String, Object> dataMap = deleteList.get(i);
                    codeDao.deleteCode(dataMap);
                }
            }

            if (updateList != null) {
                for (int i = 0, nSize = updateList.size(); i < nSize; i++) {
                    Map<String, Object> dataMap = updateList.get(i);
                    dataMap.put("UPD_ID", "");
                    codeDao.updateCode(dataMap);
                }
            }



            if (insertList != null) {
                for (int i = 0, nSize = insertList.size(); i < nSize; i++) {
                    Map<String, Object> dataMap = insertList.get(i);
                    dataMap.put("CODE_GRP_ID", paramMap.get("CODE_GRP_ID"));
                    dataMap.put("REG_ID", "");
                    codeDao.insertCode(dataMap);
                }
            }
        }
    }









    /*
     * 공통 지급계좌 조회
     * Key MNG_ACN_NO = USER_ID 및 USER PK Key
     */
    public List<Map<String, Object>> selectCmmAcntNoList(Map<String, Object> paramMap) throws Exception {

        List<Map<String, Object>> result = codeDao.selectCmmAcntNoList(paramMap);
        return result;
    }

    /*
     * 공통 지급계좌 상세조회
     */
    public Map<String, Object> selectCmmAcntNo(Map<String, Object> paramMap) throws Exception {
    	return codeDao.selectCmmAcntNo(paramMap);
    }

}
