package com.bilgeadam.service;



import com.bilgeadam.dto.request.LoginRequestDTO;
import com.bilgeadam.dto.request.RegisterRequestDTO;
import com.bilgeadam.entity.Auth;


import com.bilgeadam.utilty.enums.EStatus;
import com.bilgeadam.exception.AuthServiceException;
import static com.bilgeadam.exception.ErrorType.*;
import com.bilgeadam.repository.AuthRepository;
import com.bilgeadam.utilty.JwtTokenManager;
import com.bilgeadam.utilty.PasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthRepository authRepository;
    private final JwtTokenManager jwtTokenManager;
    private final PasswordEncoder passwordEncoder;



    public Boolean register(RegisterRequestDTO dto) {
        // TODO: Implement register logic here
        // Check if email already exists
        // Encrypt password
        // Save user to database
        // Return true if registration is successful, false otherwise
        checkEmailExist(dto.getEmail());
        checkPasswordMatch(dto.getPassword(), dto.getRePassword());
        String encodedPassword = passwordEncoder.bCryptPasswordEncoder().encode(dto.getPassword());

        Auth auth = Auth.builder()
                .email(dto.getEmail())
                .password(encodedPassword)
                .firstName(dto.getFirstName())
                .lastName(dto.getLastName())
                .build();
        authRepository.save(auth);
        return true;




    }

    /**
     * Email must be unique
     * @param email
     */
    private void checkEmailExist(String email) {
        if (authRepository.existsByEmail(email)) {
            throw new AuthServiceException(EMAIL_ALREADY_TAKEN);
        }
    }

    private void checkPasswordMatch(String password, String rePassword) {
        if (!password.equals(rePassword)) {
            throw new AuthServiceException(PASSWORD_MISMATCH);
        }
    }

    public String login(LoginRequestDTO dto) {
        // TODO: Implement login logic here
        // Find user by email
        // Check if user exists
        // Create JWT token
        // Return token if login is successful, throw exception otherwise

        Optional<Auth> optionalAuth = authRepository.findOptionalByEmail(dto.getEmail());



        if (optionalAuth.isEmpty()) {
            throw new AuthServiceException(USER_NOT_FOUND);
        }

        Auth auth = optionalAuth.get();

        if (auth.getStatus().equals(EStatus.PENDING))  {

            throw new AuthServiceException(USER_IS_NOT_ACTIVE);


        }
        if (!passwordEncoder.bCryptPasswordEncoder().matches(dto.getPassword(), auth.getPassword())) {
            throw new AuthServiceException(INVALID_LOGIN_PARAMETER);
        }


        String token = jwtTokenManager.createToken(auth.getId()).orElseThrow(() -> new AuthServiceException(TOKEN_CREATION_FAILED));
        return token;

    }

    public Boolean verifyAccount(String email) {
        // TODO: Implement email verification logic here
        // Find user by email
        // Update emailVerify status to EEmailVerify.VERIFIED
        // Return true if email verification is successful, false otherwise

        Optional<Auth> optionalAuth = authRepository.findOptionalByEmail(email);

        if (optionalAuth.isEmpty()) {
            throw new AuthServiceException(USER_NOT_FOUND);
        }


        Auth auth = optionalAuth.get();
        if (auth.getStatus().equals(EStatus.ACTIVE)) {

            throw new AuthServiceException(USER_IS_ACTIVE);


        }
        auth.setStatus(EStatus.ACTIVE);
        authRepository.save(auth);
        return true;
    }

    public Auth findById(Long authid) {
        // TODO: Implement finding user by id logic here
        // Find user by id
        // Return user if found, throw exception otherwise

        Optional<Auth> optionalAuth = authRepository.findById(authid);

        if (optionalAuth.isEmpty()) {
            throw new AuthServiceException(USER_NOT_FOUND);
        }

        return optionalAuth.get();
    }


}
