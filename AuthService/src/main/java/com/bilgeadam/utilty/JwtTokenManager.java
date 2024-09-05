package com.bilgeadam.utilty;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bilgeadam.exception.AuthServiceException;
import com.bilgeadam.exception.ErrorType;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class JwtTokenManager {
    private final String SECRETKEY ="secretkey";
    private final String ISSUER ="workforce";
    private final Long EXDATE = 1000L * 60 * 60 ; // 5 minutes

    public Optional<String> createToken (Long authId){
        String token;
        try{
            token = JWT.create().withAudience()
                    .withClaim("id", authId)
                    .withIssuer(ISSUER)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXDATE))
                    .sign(Algorithm.HMAC512(SECRETKEY));
            return Optional.of(token);
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public Optional<Long> validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC512(SECRETKEY);
            JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
            DecodedJWT decodedJWT = verifier.verify(token);
            if(decodedJWT == null)
                return Optional.empty();
            Long authId = decodedJWT.getClaim("id").asLong();
            return Optional.of(authId);
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public Optional<Long> getIdFromToken(String token){
        try {
            Algorithm algorithm=Algorithm.HMAC512(SECRETKEY);
            JWTVerifier verifier=JWT.require(algorithm).withIssuer(ISSUER).build();
            DecodedJWT decodedJWT= verifier.verify(token);

            if (decodedJWT==null){
                throw new AuthServiceException(ErrorType.INVALID_TOKEN);
            }

            Long id=decodedJWT.getClaim("id").asLong();
            return Optional.of(id);

        }catch (Exception e){
            System.out.println(e.getMessage());
            throw new AuthServiceException(ErrorType.INVALID_TOKEN);
        }
    }

//    public ERole getRoleFromToken(String token){
//        try {
//            Algorithm algorithm = Algorithm.HMAC512(SECRETKEY);
//            JWTVerifier verifier = JWT.require(algorithm).withIssuer(ISSUER).build();
//            DecodedJWT decodedJWT = verifier.verify(token);
//
//            if (decodedJWT == null) {
//                System.out.println("token null mıı?????");
//                throw new AuthServiceException(ErrorType.INVALID_TOKEN);
//            }
//
//            String role = decodedJWT.getClaim("role").asString();
//            return ERole.valueOf(role.toUpperCase());
//        }catch (Exception e){
//            System.out.println(e.getMessage());
//            System.out.println("yoksa bura mıı????");
//            throw new AuthServiceException(ErrorType.INVALID_TOKEN);
//        }
//    }


}