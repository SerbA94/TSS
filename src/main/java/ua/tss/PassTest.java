package ua.tss;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PassTest {

	public static void main(String[] args) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		System.out.println(bCryptPasswordEncoder.matches("Deadmau5", bCryptPasswordEncoder.encode("Deadmau5")));

		
		
		
		
		
	}
}
