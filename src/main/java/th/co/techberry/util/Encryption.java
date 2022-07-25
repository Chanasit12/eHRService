package th.co.techberry.util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import javax.crypto.spec.PBEKeySpec;
import th.co.techberry.constants.ConfigConstants;

public class Encryption {
	public static String encryptPassword(String username, String password) {
		TextEncryptor encrytor = Encryptors.text(password,ConfigConstants.ENCRYPT_KEY);
		String cipherText = encrytor.encrypt(password);
		System.out.println("cipherText : "+cipherText);
		return cipherText;
	}

	public static boolean verifyPassword(String encode, String username, String password) {
		try {
			TextEncryptor encrytor = Encryptors.text(password,ConfigConstants.ENCRYPT_KEY);
			String decrypText = encrytor.decrypt(encode);
			if (password.equals(decrypText.replace(username,""))) {
				return true ;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false ;
		}
		return false ;
	}
	
	public static String GetDecryText(String encode, String username, String password) {
		try {
			TextEncryptor encrytor = Encryptors.text(username + password,ConfigConstants.ENCRYPT_KEY);
			String decrypText = encrytor.decrypt(encode);
			return decrypText.replace(username,"") ;
		} catch (Exception e) {
			e.printStackTrace();
			return "" ;
		}
	}

}
