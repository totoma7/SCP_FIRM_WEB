package com.ecbank.common.csrf;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.support.RequestDataValueProcessor;

public class CSRFRequestDataValueProcessor implements RequestDataValueProcessor{

	@Override
	public Map<String, String> getExtraHiddenFields(HttpServletRequest request) {
		Map<String,String> hiddenFields = new HashMap<String,String>();
		hiddenFields.put(CSRFTokenManager.CSRF_PARAM_NAME, CSRFTokenManager.getTokenForSession(request.getSession()));
		//logger.info("CSRFRequestDataValueProcessor");
		return hiddenFields;
	}

	@Override
	public String processAction(HttpServletRequest arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		return arg1;
	}

	@Override
	public String processFormFieldValue(HttpServletRequest arg0, String arg1, String arg2, String arg3) {
		// TODO Auto-generated method stub
		return arg2;
	}

	@Override
	public String processUrl(HttpServletRequest arg0, String arg1) {
		// TODO Auto-generated method stub
		return arg1;
	}

}
