package com.finspire.auth;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
@Tag(name="Authentication")
@CrossOrigin("*")
public class AuthenticationController {
    private final AuthenticationService service;
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> register(@RequestBody @Valid RegistrationRequest request) throws MessagingException {
        service.register(request);
        return ResponseEntity.accepted().build();
    }
    @PostMapping("/resend-otp/{email}")
    public ResponseEntity<String> resendAuthenticationOtp(@PathVariable(name = "email") String email) throws MessagingException {
        service.resendActivationCode(email);
        return ResponseEntity.ok("Otp has sent to your email.");
    }
    @PostMapping("/admin/register")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ResponseEntity<?> registerAsAdmin(@RequestBody @Valid RegistrationRequest request) throws MessagingException {
        service.registerAsAdmin(request);
        return ResponseEntity.accepted().build();
    }

    @PostMapping("/authendicate")
    public ResponseEntity<AuthendicateResponse> authendicate(@RequestBody @Valid AuthendicationRequest request){
        return ResponseEntity.ok(service.authendicate(request));
    }

    @GetMapping("/activate-account")
    public String confirm(@RequestParam String token) throws MessagingException {
        return service.activateAccount(token);
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthendicateResponse> refreshToken(@RequestBody Map<String, String> request) {
        return service.generateRefreshToken(request);

    }
    @PostMapping("/forgot-password/{email}")
    public ResponseEntity<String> forgotPassword(@PathVariable(name = "email") String email) throws MessagingException {
        service.validateEmailToChangePassword(email);
        return ResponseEntity.ok("Password reset link sent to your email.");
    }
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(
            @RequestParam String token,
            @RequestParam String newPassword) {
        service.resetPassword(token, newPassword);
        return ResponseEntity.ok("Password reset successful.");
    }

    @PostMapping("/resend-forgotPassword/otp")
    public ResponseEntity<String> resendOtp(@PathVariable(name = "email") String email) throws MessagingException {
        service.resendActivationCode(email);
        return ResponseEntity.ok("Password reset link sent to your email.");
    }


}
