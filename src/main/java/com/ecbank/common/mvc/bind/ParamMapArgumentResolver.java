package com.ecbank.common.mvc.bind;


import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;


/*
 * Sping MVC는 Controller의 argument를 분석하여 argument값을 customizing 할 수 있는 WebArgumentResolver라는 interface를 제공한다.
 *
 * WebArgumentResolver의 구현 클래스이다.
 * Controller 메소드의 argument중에 paramMap이라는 Map 객체가 있다면
 * HTTP request 객체에 있는 파라미터이름과 값을 paramMap에 담는다
 */
public class ParamMapArgumentResolver implements HandlerMethodArgumentResolver  {

	protected final Log logger = LogFactory.getLog(getClass());

	public boolean supportsParameter(MethodParameter parameter) {

		Class<?> clazz = parameter.getParameterType();
		String paramName = parameter.getParameterName();

		/*if( logger.isDebugEnabled() ) {
			logger.debug("####################### paramName = " + paramName );
		}*/

		if(clazz.equals(Map.class) && paramName.equals("paramMap")) {
			return true;
		}

		return false;

    }

	public Object resolveArgument(MethodParameter parameter,
			   ModelAndViewContainer mavContainer,
			   NativeWebRequest webRequest,
			   WebDataBinderFactory binderFactory) throws Exception
	{



		Map<String, Object> paramMap = new HashMap<String, Object>();
		HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
		Enumeration<?> enumeration = request.getParameterNames();

		/*if( logger.isDebugEnabled() ) {
			logger.debug( "####################### enumeration = " + enumeration);
		}*/

		while(enumeration.hasMoreElements()){
			String key = (String) enumeration.nextElement();
			String[] values = request.getParameterValues(key);

			if(values!=null){
				paramMap.put(key, (values.length > 1) ? Arrays.asList(values):values[0] );
			}
		}

		if( logger.isDebugEnabled() ) {
			logger.debug("paramMap = " + paramMap );
		}

		return paramMap;

	}


}
