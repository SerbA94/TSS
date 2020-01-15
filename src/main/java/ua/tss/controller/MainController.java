package ua.tss.controller;

import java.io.BufferedWriter;

import java.io.FileWriter;
import java.io.IOException;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MainController extends SuperController {

	@GetMapping("/main")
	public String indexMain() {

		return "redirect:/";
	}

	@GetMapping("/")
	public String index() {
		String fileName = "log.txt";
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		WebAuthenticationDetails wad;

		if (auth.getDetails() instanceof WebAuthenticationDetails) {
			 wad = (WebAuthenticationDetails) auth.getDetails();



		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true));
		    writer.append(wad.getRemoteAddress()+", ");
		    writer.append(wad.getSessionId());

		    writer.append('\n');


		    writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		}

		return "index";
	}

	@GetMapping("/login")
	public String login() {
		if (isCurrentAuthenticationAnonymous()) {
			return "login";
		}
		return "redirect:/";
	}

	@GetMapping("/logout")
	public String signout(HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			new SecurityContextLogoutHandler().logout(request, response, auth);
			SecurityContextHolder.getContext().setAuthentication(null);
		}
		return "redirect:/";
	}

}
