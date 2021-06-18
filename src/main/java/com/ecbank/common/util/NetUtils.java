package com.ecbank.common.util;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NetUtils {

    final static Logger logger = LoggerFactory.getLogger(NetUtils.class);

	public static String getIp(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");

		logger.info(">>>> X-FORWARDED-FOR : " + ip);

		if (ip == null) {
			ip = request.getHeader("Proxy-Client-IP");
			logger.info(">>>> Proxy-Client-IP : " + ip);
		}
		if (ip == null) {
			ip = request.getHeader("WL-Proxy-Client-IP");
			logger.info(">>>> WL-Proxy-Client-IP : " + ip);
		}
		if (ip == null) {
			ip = request.getHeader("HTTP_CLIENT_IP");
			logger.info(">>>> HTTP_CLIENT_IP : " + ip);
		}
		if (ip == null) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
			logger.info(">>>> HTTP_X_FORWARDED_FOR : " + ip);
		}
		if (ip == null) {
			ip = request.getRemoteAddr();
		}
		logger.info(">>>> Result : IP Address : "+ip);

		return ip;
	}
	public static String lpad(long l, int length, String prefix) {
		try {
			StringBuilder sb = new StringBuilder();
			String castValue  = l +"";

			for (int i = castValue.length(); i < length; i++) {
				sb.append(prefix);
			}
			sb.append(castValue);
			return sb.toString();
		}catch(Exception e ) {
			return "";
		}
	}
	public static String jsonEnterConvert(String json) {

        if( json == null || json.length() < 2 )
            return json;


        final int len = json.length();
        final StringBuilder sb = new StringBuilder();
        char c;
        String tab = "";
        final char newLine = '\n';
        boolean beginEnd = true;
        for( int i=0 ; i<len ; i++ ){
            c = json.charAt(i);
            switch( c ){
            case '{': case '[':{
                sb.append( c );
                if( beginEnd ){
                    tab += "\t";
                    sb.append( newLine );
                    sb.append( tab );
                }
                break;
            }
            case '}': case ']':{
                if( beginEnd ){
                    tab = tab.substring(0, tab.length()-1);
                    sb.append( newLine );
                    sb.append( tab );
                }
                sb.append( c );
                break;
            }
            case '"':{
                if( json.charAt(i-1)!='\\' )
                    beginEnd = ! beginEnd;
                sb.append( c );
                break;
            }
            case ',':{
                sb.append( c );
                if( beginEnd ){
                    sb.append( newLine );
                    sb.append( tab );
                }
                break;
            }
            case '\r': case '\n':{
                if( ! beginEnd ){
                    sb.append( c );
                }
                break;
            }
            default :{
                sb.append( c );
            }
            }// switch end

        }
        if( sb.length() > 0 )
            sb.insert(0, '\n');
        return sb.toString();
    }
}
