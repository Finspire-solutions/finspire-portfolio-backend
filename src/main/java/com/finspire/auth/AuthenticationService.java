package com.finspire.auth;

import com.finspire.constants.ApplicationConstants;
import com.finspire.email.EmailService;
import com.finspire.entity.PasswordResetToken;
import com.finspire.entity.RefreshToken;
import com.finspire.entity.Token;
import com.finspire.entity.User;
import com.finspire.exception.ServiceException;
import com.finspire.repository.*;
import com.finspire.security.JwtService;
import com.finspire.service.RefreshTokenService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.finspire.email.EmailTemplateName.ACTIVATE_ACCOUNT;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final EmailService emailService;
//    @Value("${application.mailing.frontend.activation-url}")
//    private String activationUrl;
    public void register(RegistrationRequest request) throws MessagingException {
        var userRole = roleRepository.findByName("USER").orElseThrow(()-> new IllegalStateException("Role USER was not initilized"));

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();

        userRepository.save(user);
        sendValidationEmail(user);
    }

    private void sendValidationEmail(User user) throws MessagingException {
        var newToken = generatedAndSaveActivationToken(user);
        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                ACTIVATE_ACCOUNT,
                newToken,
                "Account activation"


        );
    }

    private String generatedAndSaveActivationToken(User user) {
        String generateToken = generateActivationCode(6);

        var token = Token.builder()
                .token(generateToken)
                .createAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);
        return generateToken;
    }

    private String generateActivationCode(int length) {
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();
        SecureRandom secureRandom = new SecureRandom();
        for (int i=0;i<length;i++){
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }
        return codeBuilder.toString();
    }

    public AuthendicateResponse authendicate(AuthendicationRequest request) {
        var auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        var claims = new HashMap<String,Object>();
        var user = ((User)auth.getPrincipal());
        claims.put("fullname",user.fullName());

        String jwtToken = jwtService.generateToken(claims, user);
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return AuthendicateResponse.builder()
                .token(jwtToken)
                .refreshToken(refreshToken.getToken())
                .build();
    }

    public String activateAccount(String token) throws MessagingException {

        Token savedToken = tokenRepository.findByToken(token).orElseThrow(() -> new RuntimeException("Invalid Token"));

        if (LocalDateTime.now().isAfter(savedToken.getExpiresAt())){
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("Activation has expired. A new token has sent");
        }

        var user = userRepository.findById(savedToken.getUser().getId()).orElseThrow(() ->new RuntimeException("User not found"));
        user.setEnabled(true);
        userRepository.save(user);
        savedToken.setValidatedAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
        return "Successfully Validated ";

    }

    public void registerAsAdmin(RegistrationRequest request) throws MessagingException {
        var userRole = roleRepository.findByName("ADMIN").orElseThrow(()-> new IllegalStateException("Role ADMIN was not initilized"));

        var user = User.builder()
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .accountLocked(false)
                .enabled(false)
                .roles(List.of(userRole))
                .build();

        userRepository.save(user);
        sendValidationEmail(user);
    }

    public ResponseEntity<AuthendicateResponse> generateRefreshToken(Map<String, String> request) {
        String refreshTokenStr = request.get("refreshToken");
        return refreshTokenRepository.findByToken(refreshTokenStr)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String newAccessToken = jwtService.generateToken(new HashMap<>(), user);
                    return ResponseEntity.ok(AuthendicateResponse.builder()
                            .token(newAccessToken)
                            .refreshToken(refreshTokenStr) // reuse same refresh token
                            .build());
                })
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));
    }

    public void validateEmailToChangePassword(String email) throws MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceException(
                        ApplicationConstants.EMAIL_NOT_FOUND,
                        ApplicationConstants.BAD_REQUEST,
                        HttpStatus.BAD_REQUEST
                ));
        String token = UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .resetToken(token)
                .user(user)
                .expiryDate(LocalDateTime.now().plusMinutes(30))
                .build();

        passwordResetTokenRepository.save(resetToken);
        String resetUrl = "https://your-frontend.com/reset-password?token=" + token;
        emailService.sendPasswordResetEmail(
                user,resetUrl
        );
    }
    @Transactional
    public void resetPassword(String token, String newPassword) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByResetToken(token)
                .orElseThrow(() -> new ServiceException("Invalid token", "BAD_REQUEST", HttpStatus.BAD_REQUEST));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new ServiceException("Token expired", "BAD_REQUEST", HttpStatus.BAD_REQUEST);
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
        passwordResetTokenRepository.deleteByResetToken(token);
    }

    @Transactional
    public void resendActivationCode(String email) throws MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String newToken = generateActivationCode(6);;

        Token token = tokenRepository.findByUser(user)
                .map(existingToken -> {
                    existingToken.setToken(newToken);
                    existingToken.setCreateAt(LocalDateTime.now());
                    existingToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));
                    return existingToken;
                })
                .orElseGet(() -> {
                    Token newT = new Token();
                    newT.setUser(user);
                    newT.setToken(newToken);
                    newT.setCreateAt(LocalDateTime.now());
                    newT.setExpiresAt(LocalDateTime.now().plusMinutes(15));
                    return newT;
                });

        tokenRepository.save(token);

        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                ACTIVATE_ACCOUNT,
                newToken,
                "Account activation"
        );
    }


    //    this is usd to
    @Transactional
    public void resendPasswordResetActivationCode(String email) throws MessagingException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Generate a new token
        String newToken = generateActivationCode(6);;

        PasswordResetToken token = passwordResetTokenRepository.findByUserEmail(email)
                .map(existingToken -> {
                    existingToken.setResetToken(newToken);
                    existingToken.setExpiryDate(LocalDateTime.now().plusMinutes(15));
                    return existingToken;
                })
                .orElseGet(() -> {
                    PasswordResetToken prt = new PasswordResetToken();
                    prt.setUser(user);
                    prt.setResetToken(newToken);
                    prt.setExpiryDate(LocalDateTime.now().plusMinutes(15));
                    return prt;
                });

        passwordResetTokenRepository.save(token);

        emailService.sendEmail(
                user.getEmail(),
                user.fullName(),
                ACTIVATE_ACCOUNT,
                newToken,
                "Account activation"


        );
    }
}
