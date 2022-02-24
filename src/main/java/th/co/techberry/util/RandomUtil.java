package th.co.techberry.util;
import java.util.Random;
public class RandomUtil {
	public static char[] generatePassword(int length) {
	      String capitalCaseLetters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	      String lowerCaseLetters = "abcdefghijklmnopqrstuvwxyz";
	      String numbers = "1234567890";
	      String combinedChars = capitalCaseLetters + lowerCaseLetters + numbers;
	      Random random = new Random();
	      char[] password = new char[length]; 
	      for(int i = 0; i< length ; i++) {
	         password[i] = combinedChars.charAt(random.nextInt(combinedChars.length()));
	      }
	      return password;
	   }
	
	
	}

