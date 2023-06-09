/*
 * This is the HomeController class which handles various requests related to user authentication and registration.
 * It also includes methods for loading different views and processing user registration, password reset, and password change.
 */

package com.example.demo.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.demo.model.UserDtls;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	/*
	 * This method adds the user details to the model attribute.
	 * The user details are fetched from the repository using the email obtained
	 * from the Principal object.
	 * It is executed before each request mapping method in this controller.
	 */
	@ModelAttribute
	private void userDetails(Model model, Principal principal) {
		if (principal != null) {
			String email = principal.getName();
			UserDtls user = userRepo.findByEmail(email);
			model.addAttribute("user", user);
		}
	}

	/*
	 * This method handles the GET request for the home page.
	 * It returns the "index" view to display the home page.
	 */
	@GetMapping("/")
	public String index() {
		return "index";
	}

	/*
	 * This method handles the GET request for the sign-in page.
	 * It returns the "login" view to display the sign-in page.
	 */
	@GetMapping("/signin")
	public String login() {
		return "login";
	}

	/*
	 * This method handles the GET request for the registration page.
	 * It returns the "registration" view to display the registration page.
	 */
	@GetMapping("/registration")
	public String registration() {
		return "registration";
	}

	/*
	 * This method handles the GET request for the dashboard page.
	 * It returns the "dashboard" view to display the dashboard page.
	 */
	@GetMapping("/dashboard")
	public String dashboard() {
		return "dashboard";
	}

	/*
	 * This method handles the POST request for creating a new user.
	 * It takes the UserDtls object, HttpSession, and role as parameters.
	 * It checks if the provided role is either "ROLE_USER" or "ROLE_TEACHER".
	 * If the email is already registered, it sets a message in the session
	 * attribute.
	 * Otherwise, it creates the user with the specified role and saves it to the
	 * repository.
	 * If the user creation is successful, it sets a success message in the session
	 * attribute and redirects to the sign-in page.
	 * If there is an error during user creation, it sets an error message in the
	 * session attribute.
	 */
	@PostMapping("/createUser")
	public String createuser(@ModelAttribute UserDtls user, HttpSession session, @RequestParam String role) {
		if (!(role.equals("ROLE_USER") || role.equals("ROLE_TEACHER"))) {
			session.setAttribute("msg", "Something went wrong!!");
		}
		boolean isEmailRegistered = userService.checkEmail(user.getEmail());
		if (isEmailRegistered) {
			session.setAttribute("msg", "Email is already registered.");
		} else {
			UserDtls createdUser = userService.createUser(user, role);
			if (createdUser != null) {
				session.setAttribute("msg", "Registered successfully");
				return "redirect:/signin";
			} else {
				session.setAttribute("msg", "Something went wrong!!");
			}
		}
		return "redirect:/registration";
	}

	/*
	 * This method handles the GET request for loading the forgot password page.
	 * It returns the "forgot_password" view to display the forgot password page.
	 */
	@GetMapping("/loadForgotPassword")
	public String loadForgotPassword() {
		return "forgot_password";
	}

	/*
	 * This method handles the GET request for loading the reset password page.
	 * It takes the user's ID and Model as parameters.
	 * It adds the user's ID to the model attribute and returns the "reset_password"
	 * view to display the reset password page.
	 */
	@GetMapping("/loadResetPassword/{id}")
	public String loadResetPassword(@PathVariable int id, Model model) {
		model.addAttribute("id", id);
		return "reset_password";
	}

	/*
	 * This method handles the POST request for the forgot password functionality.
	 * It takes the email and mobileNum as parameters.
	 * It checks if the provided email and mobile number match any user in the
	 * repository.
	 * If a user is found, it redirects to the reset password page with the user's
	 * ID as a path variable.
	 * If no user is found, it returns to the forgot password page.
	 */
	@PostMapping("/forgotPassword")
	public String forgotPassword(@RequestParam String email, @RequestParam String mobileNum) {
		UserDtls user = userRepo.findByEmailAndMobileNumber(email, mobileNum);
		if (user != null) {
			return "redirect:/loadResetPassword/" + user.getId();
		} else {
			System.out.println("Invalid email or mobile number");
			return "forgot_password";
		}
	}

	/*
	 * This method handles the POST request for resetting the user's password.
	 * It takes the new password (psw) and the user's ID (id) as parameters.
	 * It retrieves the user from the repository based on the ID, encrypts the new
	 * password, and updates the user's password.
	 * If the password update is successful, it prints "updated" to the console.
	 * If there is an error during the password update, it prints "not updated" to
	 * the console.
	 * Finally, it redirects to the sign-in page.
	 */
	@PostMapping("/changePassword")
	public String resetPassword(@RequestParam String psw, @RequestParam Integer id) {
		UserDtls user = userRepo.findById(id).get();
		String encryptedPassword = passwordEncoder.encode(psw);
		user.setPassword(encryptedPassword);
		UserDtls updatedUser = userRepo.save(user);
		if (updatedUser != null) {
			System.out.println("Password updated successfully.");
		} else {
			System.out.println("Failed to update the password.");
		}
		return "redirect:/signin";
	}
}
