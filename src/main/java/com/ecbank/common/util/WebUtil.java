package com.ecbank.common.util;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/**
 *
 * EgovWebUtil을 대체함
 */
public abstract class WebUtil {

	protected final static Log logger = LogFactory.getLog(WebUtil.class);

    public static String clearXSSMinimum(String value) {
        if (value == null || value.trim().equals("")) {
            return "";
        }

        String returnValue = value;

        returnValue = returnValue.replaceAll("&", "&amp;");
        returnValue = returnValue.replaceAll("<", "&lt;");
        returnValue = returnValue.replaceAll(">", "&gt;");
        returnValue = returnValue.replaceAll("\"", "&#34;");
        returnValue = returnValue.replaceAll("\'", "&#39;");
        return returnValue;
    }

    public static String clearXSSMaximum(String value) {
        String returnValue = value;
        returnValue = clearXSSMinimum(returnValue);

        returnValue = returnValue.replaceAll("%00", null);

        returnValue = returnValue.replaceAll("%", "&#37;");

        // \\. => .

        returnValue = returnValue.replaceAll("\\.\\./", ""); // ../
        returnValue = returnValue.replaceAll("\\.\\.\\\\", ""); // ..\
        returnValue = returnValue.replaceAll("\\./", ""); // ./
        returnValue = returnValue.replaceAll("%2F", "");

        return returnValue;
    }

    public static String clearScriptPatternXSS(String value) {

    	String retVal = "";

    	Pattern[] patterns = new Pattern[]{
    	        // Script fragments
    	        Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
    	        // src='...'
    	        //Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    	        //Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    	        // lonely script tags
    	        Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
    	        Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    	        // eval(...)
    	        Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    	        // expression(...)
    	        Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    	        // javascript:...
    	        Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
    	        // vbscript:...
    	        Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
    	        // onload(...)=...
    	        Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
    	    };


    	Set<String> removable = new HashSet<>(
    		Arrays.asList(
    				  "onblur"
    				, "onchange"
    				, "onclick"
    				, "ondblclick"
    				, "onfocus"
    				, "onkeydown"
    				, "onkeypress"
    				, "onkeyup"
    				, "onmousedown"
    				, "onmousemove"
    				, "onmouseout"
    				, "onmouseover"
    				, "onmouseup"
    				, "onload"
    				, "onerror"
    				, "onscroll"
    				, "onsubmit"
    				, "onunload"
    			)
    	);


    	if( value != null) {
    		//retVal = Jsoup.clean(value, Whitelist.basic());
    		retVal = value;
    		for( Pattern scriptPattern : patterns) {
    			retVal = scriptPattern.matcher(retVal).replaceAll("");
    		}

 			Document doc = Jsoup.parse(retVal);
			Elements el = doc.getAllElements();
			for (Element e : el) {
				Attributes at = e.attributes();
				for (Attribute a : at) {
					if( StringUtils.isNotEmpty(a.getKey()) ) {
						logger.debug(" attribue = " + a.getKey() );

						if( removable.contains(a.getKey().toLowerCase()) ) {
							e.removeAttr(a.getKey());
						}
					}
				}
			}

			retVal = doc.html().trim();
			retVal  = retVal.replace("<html>", "");
			retVal  = retVal.replace("</html>", "");
			retVal  = retVal.replace("<head>", "");
			retVal  = retVal.replace("</head>", "");
			retVal  = retVal.replace("<body>", "");
			retVal  = retVal.replace("</body>", "");
			retVal  = retVal.replace("alert", "");
			retVal  = retVal.replace("iframe", "");
			retVal = retVal.trim();
    	}
/*
    	if( logger.isDebugEnabled() ) {
    		logger.debug("unSafe value = " + value);
    		logger.debug("safe value = " + retVal);
    	}

    */
        return retVal;
    }


    public static String clearXSS(String value) {

      	if (value == null || value.trim().equals("")) {
            return "";
        }


    	String retVal = "";

    	Pattern[] patterns = new Pattern[]{
    	        // Script fragments
    	        Pattern.compile("<script>(.*?)</script>", Pattern.CASE_INSENSITIVE),
    	        // src='...'
    	        Pattern.compile("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    	        Pattern.compile("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    	        // lonely script tags
    	        Pattern.compile("</script>", Pattern.CASE_INSENSITIVE),
    	        Pattern.compile("<script(.*?)>", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    	        // eval(...)
    	        Pattern.compile("eval\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    	        // expression(...)
    	        Pattern.compile("expression\\((.*?)\\)", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL),
    	        // javascript:...
    	        Pattern.compile("javascript:", Pattern.CASE_INSENSITIVE),
    	        // vbscript:...
    	        Pattern.compile("vbscript:", Pattern.CASE_INSENSITIVE),
    	        // onload(...)=...
    	        Pattern.compile("onload(.*?)=", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL)
    	    };


    	Set<String> removable = new HashSet<>(
    		Arrays.asList(
    				  "onblur"
    				, "onchange"
    				, "onclick"
    				, "ondblclick"
    				, "onfocus"
    				, "onkeydown"
    				, "onkeypress"
    				, "onkeyup"
    				, "onmousedown"
    				, "onmousemove"
    				, "onmouseout"
    				, "onmouseover"
    				, "onmouseup"
    				, "onload"
    				, "onerror"
    				, "onscroll"
    				, "onsubmit"
    				, "onunload"

    				, "cookie"
    				, "alert"
    			)
    	);


		//retVal = Jsoup.clean(value, Whitelist.basic());
		retVal = value;
		for( Pattern scriptPattern : patterns) {
			retVal = scriptPattern.matcher(retVal).replaceAll("");
    	}


		for(Iterator<String> it = removable.iterator(); it.hasNext();){
			String key  =  (String)it.next();
			retVal = retVal.replace(key, "");
		}

		retVal  = retVal.replace("<", "〈");
		retVal  = retVal.replace(">", "〉");
		retVal  = retVal.replace("/", "／");  // 전표제목(subject에 / 있는 경우 문제가 생김)

        return retVal;
    }


    public static String filePathBlackList(String value) {
        String returnValue = value;
        if (returnValue == null || returnValue.trim().equals("")) {
            return "";
        }

        returnValue = returnValue.replaceAll("\\.\\./", ""); // ../
        returnValue = returnValue.replaceAll("\\.\\.\\\\", ""); // ..\

        return returnValue;
    }

    /**
     * 행안부 보안취약점 점검 조치 방안.
     *
     * @param value
     * @return
     */
    public static String filePathReplaceAll(String value) {
        String returnValue = value;
        if (returnValue == null || returnValue.trim().equals("")) {
            return "";
        }

        returnValue = returnValue.replaceAll("/", "");
        returnValue = returnValue.replaceAll("\\", "");
        returnValue = returnValue.replaceAll("\\.\\.", ""); // ..
        returnValue = returnValue.replaceAll("&", "");

        return returnValue;
    }

    public static String filePathWhiteList(String value) {
        return value; // TODO
    }

    public static boolean isIPAddress(String str) {
        Pattern ipPattern = Pattern.compile("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}");

        return ipPattern.matcher(str).matches();
    }

    public static String removeCRLF(String parameter) {
        return parameter.replaceAll("\r", "").replaceAll("\n", "");
    }

    public static String removeSQLInjectionRisk(String parameter) {
        return parameter.replaceAll("\\p{Space}", "").replaceAll("\\*", "").replaceAll("%", "").replaceAll(";", "").replaceAll("-", "").replaceAll("\\+", "").replaceAll(",", "");
    }

    public static String removeOSCmdRisk(String parameter) {
        return parameter.replaceAll("\\p{Space}", "").replaceAll("\\*", "").replaceAll("|", "").replaceAll(";", "");
    }



    /**
     * 서버 URL 구하기
     *
     * @param request
     * @return
     */
	public static String getServerUrl( HttpServletRequest request){

		StringBuilder sb = new StringBuilder();
		sb.append( request.getScheme() ).append("://")
			.append( request.getServerName() );

		if( request.getServerPort() != 80 ) {
			sb.append(":").append( request.getServerPort() );
		}



        return sb.toString();
    }


    /**
     * 서버 path 구하기
     *
     * @param request
     * @return
     */
	public static String getServerContextPath( HttpServletRequest request){

        return getServerUrl(request) + request.getContextPath();
    }



    /**
     * 클라이언트(Client)의 IP주소를 구한다.
     * @param request
     * @return
     */
	public static String getClientIP( HttpServletRequest request){

        String ip = request.getHeader("X-Forwarded-For");

        if( StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }

        if(StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }

        if(StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }

        if(StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }

        if(StringUtils.isEmpty(ip) || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }





    /**
     * QueryString을 Map올 return한다.
     *
     * @param request
     * @return
     * @throws Exception
     */
	public static Map<String,Object> getRequestParams(HttpServletRequest request) throws Exception
	{
		LinkedHashMap<String, Object> paramMap = new LinkedHashMap<String, Object>();

		Enumeration<String> enumeration = request.getParameterNames();
		/*
		if( logger.isDebugEnabled() ) {
			logger.debug( "####################### enumeration = " + enumeration);
		}*/

		while(enumeration.hasMoreElements()){
			String key = (String) enumeration.nextElement();
			String[] values = request.getParameterValues(key);
			//logger.debug("key = " + key + ", value=" + values);

			if(values!=null){
				paramMap.put(key, (values.length > 1) ? Arrays.asList(values):values[0] );
			}
		}

		if( logger.isDebugEnabled() ) {
			logger.debug("paramMap = " + paramMap );
		}

		return paramMap;
	}

    /**
     * QueryString을 Map올 return한다.
     *
     * @param request
     * @return
     * @throws Exception
     */
	public static String getQueryString(Map<String,Object> paramMap)throws Exception {
		StringBuilder sb = new StringBuilder();

		if (null != paramMap) {

            for (Map.Entry<String, Object> entry : paramMap.entrySet()) {
				String strKey = entry.getKey();
				String strValue = URLEncoder.encode(StringUtils.defaultString((String)entry.getValue()), "UTF-8");

				//logger.debug("key = " + strKey + ", value=" + strValue);
				if (null != strKey && ! "".equals(strKey)) {
					if( sb.length() > 0 ) {
						sb.append("&");
					}

					sb.append( strKey).append("=").append( strValue );

				}
			}
		}

		/*
		if( logger.isDebugEnabled() ) {
			logger.debug("QueryString = " + sb.toString() );
		}*/

		return sb.toString();
	}
}
