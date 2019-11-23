package ua.tss.controller;

import java.util.Collections;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import ua.tss.model.Role;
import ua.tss.model.User;
import ua.tss.repository.UserRepository;

@Controller
public class UserController {

	private final UserRepository userRepository;

	@Autowired
	public UserController(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@GetMapping("/signup")
	public String showSignUpForm(Model model) {
		if (isCurrentAuthenticationAnonymous()) {
			model.addAttribute("user", new User());
			return "signup";
		}
		return "redirect:/";
	}

	@PreAuthorize("isAnonymous()")
	@PostMapping("/signup")
	public String addUser(@Valid User user, BindingResult result, Model model) {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();	
		if (result.hasErrors()) {
			return "signup";
		}
		user.setUsername(user.getPhoneNumber());
		user.setActive(true);
		user.setRoles(Collections.singleton(Role.CUSTOMER));

		if (userRepository.findByUsername(user.getUsername()) != null) {
			FieldError existError = new FieldError("user", "username", "User Exists!");
			result.addError(existError);
			return "signup";
		}
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		userRepository.save(user);
		return "redirect:/login";
	}

	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	@GetMapping("/edit/{id}")
	public String showUpdateForm(@PathVariable("id") long id, Model model) {
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		model.addAttribute("user", user);
		return "update-user";
	}

	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	@PostMapping("/update/{id}")
	public String updateUser(@PathVariable("id") long id, @Valid User user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			user.setId(id);
			return "update-user";
		}
		userRepository.save(user);
		model.addAttribute("users", userRepository.findAll());
		return "redirect:/userList";
	}

	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	@GetMapping("/userList")
	public String userList(Model model) {
		model.addAttribute("users", userRepository.findAll());
		return "list-user";
	}

	@PreAuthorize("hasAuthority('ADMIN') or hasAuthority('SUPERVISOR')")
	@GetMapping("/delete/{id}")
	public String deleteUser(@PathVariable("id") long id, Model model) {
		User user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
		userRepository.delete(user);
		return "redirect:/userList";
	}

	
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@GetMapping("/profile")
	public String profile(Model model) {
		Object principal = getCurrentAuthentication().getPrincipal();
		Long id;
		User user;
		if (principal instanceof User) {
			id = ((User) principal).getId();
			user = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
			model.addAttribute("user", user);
			return "profile-user";
		}
			return "redirect:/";
	}
	
	@PreAuthorize("hasAuthority('CUSTOMER')")
	@PostMapping("/update-profile")
	public String updateProfile(@Valid User user, BindingResult result, Model model) {
		if (result.hasErrors()) {
			return "profile-user";
		}
		userRepository.save(user);
		return "profile-user";
	}

	private Authentication getCurrentAuthentication() {
		return SecurityContextHolder.getContext().getAuthentication();
	}

	private boolean isCurrentAuthenticationAnonymous() {
		return getCurrentAuthentication().getPrincipal().equals("anonymousUser");
	}
}