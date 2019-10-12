package ua.tss.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class MainController {
	
	   @GetMapping("/")
	    public String mainPage() {
	        return "mainPage";
	    }
	   
	   @GetMapping("/login")
	    public String loginPage() {
	        return "login";
	    }
	   
	   @GetMapping("/mainPage")
	    public String mPage() {
	        return "mainPage";
	    }
	   
	   @GetMapping("/closed")
	    public String closrdPage() {
	        return "closed";
	    }
	   

}
