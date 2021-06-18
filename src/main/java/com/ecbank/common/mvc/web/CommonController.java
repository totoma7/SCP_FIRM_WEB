package com.ecbank.common.mvc.web;


import java.util.Locale;
import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.NoSuchMessageException;
import org.springframework.context.support.MessageSourceAccessor;




/**
 *
 */
public class CommonController {

    @Autowired
    MessageSourceAccessor messageSourceAccessor;



	/**
	 * Message_resource.properties 에서 키에 해당하는 값을 찾아서 리턴한다.
	 * @param messageKey 키
	 * @return 키에 해당하는 값
	 */
	public String getMessage(String messageKey) {
		String msg = null;
		try {
			msg = messageSourceAccessor.getMessage(messageKey);
		} catch( NoSuchMessageException nme) {
			msg = messageKey;
		}
		return msg;
	}

	/**
	 * Message_resource.properties 에서 키에 해당하는 값을 찾아서 파라미터 값이 들어갈 위치에 값을 세팅하여 Locale을 지정해서 결과를 리턴한다.
	 * @param messageKey 키
	 * @param params 결과의 변수에 들어갈 값의 배열
	 * @return 키에 해당하는 값
	 */
	public String getMessage(String messageKey, Object[] params) {
		String msg = null;
		try {
			msg = messageSourceAccessor.getMessage(messageKey, params);

		} catch( NoSuchMessageException nme) {
			msg = messageKey;
		}
		return msg;
	}


	/**
	 * Message_resource.properties 에서 키에 해당하는 값을 찾아서 파라미터 값이 들어갈 위치에 값을 세팅하여 Locale을 지정해서 결과를 리턴한다.
	 * @param messageKey 키
	 * @param params 결과의 변수에 들어갈 값의 배열
	 * @param locale 지역 지정
	 * @return 키에 해당하는 값
	 */
	public String getMessage(String messageKey, Object[] params, Locale locale) {
		String msg = null;
		try {
			msg = messageSourceAccessor.getMessage(messageKey, params, locale);
		} catch( NoSuchMessageException nme) {
			msg = messageKey;
		}
		return msg;
	}

	/**
	 * Message_resource.properties 에서 키에 해당하는 값을 찾아서 <br/>
	 * 파라미터 값이 들어갈 위치에 값을 세팅하고, appendMessage를 뒤에 붙인뒤 Locale을 지정해서 결과를 리턴한다.
	 * @param messageKey 키(예 : invalid.password.exception)
	 * @param paramas 결과의 변수에 들어갈 값의 배열
	 * @param appendMessage 결과의 뒤에 붙을 문자열
	 * @param locale 지역 지정
	 * @return 키에 해당하는 값
	 */
	public String getMessage(String messageKey, Object[] paramas, String appendMessage, Locale locale) {
		String msg = null;
		try {
			msg = messageSourceAccessor.getMessage(messageKey, paramas, appendMessage, locale);
		} catch( NoSuchMessageException nme) {
			msg = messageKey;
		}
		return msg;
	}


}
