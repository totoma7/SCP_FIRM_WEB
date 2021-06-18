package com.ecbank.common.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Component
public class JsonData{

	private static final String SUCC = "SUCC";
    private static final String FAIL = "FAIL";

	/** 상태 코드  SUCC, FAIL */
    private String status;

    /** 일반 메시지 */
    private String message;

    /** 에러 메시지 */
    private String errMsg;

    /** 현재 페이지 */
    private int page;

    /** 페이지 사이즈 */
    private int pageSize;


    /** 전체 레코드 수 */
    private int records;

    /** 전체 페이지 수 */
    private int total;

    /** 조회된 레코드 */
    private List<Map<String, Object>> rows = new ArrayList<Map<String, Object>>();

    /** 기타 데이터 */
    @SuppressWarnings("rawtypes")
	private Map fields;

    @JsonIgnore
    private Throwable cause;

    public JsonData() {
    	this.status = SUCC;
    	this.page = 1;
    	this.pageSize = 0;
    	this.records = 0;
    	this.total = 0;
    	this.rows = null;
    	this.fields = null;
    }


    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

	public String getErrMsg() {
		return this.errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.status = FAIL;
		this.errMsg = errMsg;
	}


	public int getPage() {
        return this.page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public void setPage(String page) {
    	String strPage = StringUtils.defaultIfBlank(page, "1");
    	this.page = Integer.parseInt(strPage);
    }


	public int getPageSize() {
        return this.pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setPageSize(String pageSize) {
    	String strPageSize = StringUtils.defaultIfBlank(pageSize, "0");
    	this.pageSize = Integer.parseInt(strPageSize);
    }

    public int getRecords() {
        return this.records;
    }

    public void setRecords(int records) {
        this.records = records;
    }

    public int getTotal() {
        return this.total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Map<String, Object>> getRows() {
        return this.rows;
    }

    public void setRows(List<Map<String, Object>> rows) {
        this.rows = rows;
    }

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addFields(String key, Object value) {

		if( this.fields == null ) {
			this.fields = new HashMap();
		}

		this.fields.put(key, value);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void addFieldsAll( Map<String,Object> value) {

		if( this.fields == null ) {
			this.fields = new HashMap();
		}

		this.fields.putAll(value);
	}

	@SuppressWarnings("unchecked")
	public Map<String,Object> getFields() {
		return this.fields;
	}


    public void setPageInfo(Map<String, Object> paramMap) {

    	String strPage = StringUtils.defaultIfBlank((String)paramMap.get("page"), "1");
    	this.page = Integer.parseInt(strPage);


    	String strPageSize = StringUtils.defaultIfBlank((String)paramMap.get("pageSize"), "0");
    	this.pageSize = Integer.parseInt(strPageSize);

    }


    @SuppressWarnings({ "unchecked", "rawtypes" })
	public void setPageRows(List<Map<String, Object>> rows, Integer totalRecords ) {


    	if( rows == null ) {
    		this.rows = new ArrayList();
    		this.records = 0;
    		this.total = 0;
    	} else {
	    	this.rows = rows;

	    	int nRowSize = rows.size();

	    	/*
	    	String strRowTotal = StringUtils.defaultIfBlank(totalRecords, "0");

	    	if( "0".equals(strRowTotal) ) {
	    		this.records = nRowSize;
	    	} else {
	    		this.records = Integer.parseInt(strRowTotal);
	    	}
	    	*/

	    	if( totalRecords == null || totalRecords.intValue() == 0  ) {
	    		this.records = nRowSize;
	    	} else {
	    		this.records = totalRecords.intValue();
	    	}

	    	if( this.pageSize == 0 ) {
	    		this.total = 1;
	    	} else {
	    		this.total = (int)Math.ceil(this.records / (double)this.pageSize);
	    	}
    	}
    }


    public void setPageRows(Map<String, Object> paramMap, List<Map<String, Object>> rows, Integer totalRecords ) {

    	setPageInfo( paramMap);

    	setPageRows(rows, totalRecords);
    }


    public Throwable getCause() {
        return this.cause;
    }

    public void setCause(Throwable cause) {
        this.cause = cause;
    }
}
