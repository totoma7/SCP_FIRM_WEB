package com.ecbank.common.crypt;

import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component("DigestUtil")
public class DigestUtil {


	private static final Log logger = LogFactory.getLog(DigestUtil.class);

	private static String algorithm = "SHA-256";

    /**
     * 비밀번호를 암호화하는 기능(복호화가 되면 안되므로 SHA-256 인코딩 방식 적용)
     *
     * @param password 암호화될 패스워드
     * @return
     * @throws Exception
     */
    public static String digest(String data) throws Exception {

    	if (data == null) {
		    return "";
		}

		byte[] hashValue = null; // 해쉬값

		MessageDigest md = MessageDigest.getInstance(algorithm);

		md.reset();
		md.update(data.getBytes());

		hashValue = md.digest(data.getBytes());

		if( logger.isDebugEnabled() ) {
			logger.debug("Digested DATA = [" + hashValue + "]");
		}

		return new String(Base64.encodeBase64(hashValue));
    }


    /**
     * 비밀번호를 암호화된 패스워드 검증(salt가 사용된 경우만 적용).
     *
     * @param data 원 패스워드
     * @param encoded 해쉬처리된 패스워드(Base64 인코딩)
     * @return
     * @throws Exception
     */
    public static boolean matches(String data, String encoded) throws Exception {
    	byte[] hashValue = null; // 해쉬값

    	MessageDigest md = MessageDigest.getInstance(algorithm);

    	md.reset();
    	md.update(data.getBytes());
    	hashValue = md.digest(data.getBytes());

		if( logger.isDebugEnabled() ) {
			logger.debug("hashValue Byte DATA = [" + hashValue + "]" );
			logger.debug("hashValue String DATA = [" + new String(Base64.encodeBase64(hashValue)) + "]" );
		}

    	return MessageDigest.isEqual(hashValue, Base64.decodeBase64(encoded.getBytes()));
    }
}
