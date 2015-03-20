package safe;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.sun.mail.util.BASE64EncoderStream;

public class Encrypter {
	public static String md5Encrypte(String s) throws NoSuchAlgorithmException, UnsupportedEncodingException{
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		sun.misc.BASE64Encoder base64Encoder = new sun.misc.BASE64Encoder();
		return base64Encoder.encode(md5.digest(s.getBytes("utf-8")));
	}
}
