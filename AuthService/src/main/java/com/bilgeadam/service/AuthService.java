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
        checkEmailExist(dto.email());
        checkPasswordMatch(dto.password(), dto.rePassword());
        String encodedPassword = passwordEncoder.bCryptPasswordEncoder().encode(dto.password());

        Auth auth = Auth.builder()
                .email(dto.email())
                .password(encodedPassword)
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

        Auth auth = authRepository.findOptionalByEmail(dto.email())
                .orElseThrow(() -> new AuthServiceException(USER_NOT_FOUND));

        if (auth.getStatus().equals(EStatus.PENDING))  {

            throw new AuthServiceException(USER_IS_NOT_ACTIVE);


        }
        if (!passwordEncoder.bCryptPasswordEncoder().matches(dto.password(), auth.getPassword())) {
            throw new AuthServiceException(INVALID_LOGIN_PARAMETER);
        }


        String token = jwtTokenManager.createToken(auth.getId()).orElseThrow(() -> new AuthServiceException(TOKEN_CREATION_FAILED));
        return token;

    }



    /**
     Verifies a user's account by updating the status to ACTIVE.
     * If the user is not found or the account is already active, an exception is thrown.
     *
     * @param email The email of the user whose account is being verified.
     * @return Returns true if the account verification is successful.
     * @throws AuthServiceException if the user is not found or the account is already active.
     */
    public Boolean verifyAccount(String email) {
        // TODO: Implement email verification logic here
        // Find user by email
        // Return true if email verification is successful, false otherwi

        Auth auth = authRepository.findOptionalByEmail(email).orElseThrow(() -> new AuthServiceException(USER_NOT_FOUND));
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

    /**
     * Deletes (soft delete) an authentication entity by setting its status to DELETED.
     * If the user is not found or already deleted, an exception is thrown.
     *
     * @param authId The ID of the authentication entity to be deleted.
     * @return Returns true if the deletion (status update) is successful.
     * @throws AuthServiceException if the user is not found or already deleted.
     */
    public Boolean deleteAuth(Long authId) {

        Auth auth = authRepository.findById(authId).orElseThrow(() -> new AuthServiceException(USER_NOT_FOUND));
        if (auth.getStatus().equals(EStatus.DELETED)) {
            throw new AuthServiceException(USER_ALREADY_DELETED);
        }
        auth.setStatus(EStatus.DELETED);
        authRepository.save(auth);
        return true;

    }


//    // TODO: kuyruk ve model gelecek

/**
 * Listens to the email update message from the RabbitMQ queue and performs the update operation.
 * @param updateEmailRequest DTO containing email update information from the User Service.
 */

//    @RabbitListener(queues = "{update-email-queue}")
//    public void updateEmail() {
//
//
//        Auth auth = authRepository.findById(updateEmailRequest.getUserId())
//                .orElseThrow(() -> new AuthServiceException(USER_NOT_FOUND));
//
//        // Yeni email adresinin eşsiz olup olmadığını kontrol et
//        if (authRepository.existsByEmail(updateEmailRequest.getNewEmail())) {
//            throw new AuthServiceException(EMAIL_ALREADY_TAKEN);
//        }
//
//        // Email'i güncelle
//        auth.setEmail(updateEmailRequest.getNewEmail());
//        authRepository.save(auth);
//    }


}
