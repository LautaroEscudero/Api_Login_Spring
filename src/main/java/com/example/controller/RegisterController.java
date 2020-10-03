package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.entity.ConfirmationToken;
import com.example.entity.User;
import com.example.mapper.UserMapper;
import com.example.repository.ConfirmationTokenRepository;
import com.example.representation.AuthorizationRequest;
import com.example.service.EmailSenderService;
import com.example.service.UserService;

@RestController
@RequestMapping("/register")
@CrossOrigin
public class RegisterController {

	@Autowired
	private ConfirmationTokenRepository confirmationTokenRepository;

	@Autowired
	private EmailSenderService emailSenderService;

	private BCryptPasswordEncoder bCryptPasswordEncoder;

	private UserService userService;

	public RegisterController(UserService userService, BCryptPasswordEncoder bCryptPasswordEncoder) {
		this.userService = userService;
		this.bCryptPasswordEncoder = bCryptPasswordEncoder;
	}

	@PostMapping
	public ResponseEntity<?> saveUser(@RequestBody AuthorizationRequest userRequest) {
		
		User u = userService.getUserName(userRequest.getUserName());
		if (u == null) {
			userRequest.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));

			User userToSave = UserMapper.toDomain(userRequest);

			userToSave.setEnabled(false);

			userToSave = userService.save(userToSave);

			ConfirmationToken confirmationToken = new ConfirmationToken(userToSave);

			confirmationTokenRepository.save(confirmationToken);

			SimpleMailMessage mailMessage = new SimpleMailMessage();
			mailMessage.setTo(userToSave.getName());
			mailMessage.setSubject("Complete Registration!");
			// mailMessage.setFrom("LoutaCompany@escudero.com");
			mailMessage.setText("To confirm your account, please click here : "
					+ "http://localhost:8080/register/confirm-account?token="
					+ confirmationToken.getConfirmationToken());

			emailSenderService.sendEmail(mailMessage);

			return new ResponseEntity<>(userToSave, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("Something went wrong", HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping("/confirm-account")
	public ResponseEntity<?> confirmUserAccount(@RequestParam("token") String confirmationToken) {
		
		ConfirmationToken token = confirmationTokenRepository.findByConfirmationToken(confirmationToken);

		if (token != null) {
			User user = userService.getUser(token.getUser().getId());
			user.setEnabled(true);
			userService.save(user);
			confirmationTokenRepository.delete(token);
			return new ResponseEntity<>("accountVerified", HttpStatus.OK);
		} else {
			return new ResponseEntity<>("The link is invalid or broken!", HttpStatus.OK);
		}

	}

}
