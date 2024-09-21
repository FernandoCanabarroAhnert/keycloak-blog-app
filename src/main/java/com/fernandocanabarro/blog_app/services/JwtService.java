package com.fernandocanabarro.blog_app.services;

import org.springframework.stereotype.Service;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class JwtService {

    public static String USERID = "sub";
    public static String EMAIL = "email";
    public static String USERNAME = "preferred_username";
    public static String FULL_NAME = "name";

    private String extractJwt(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        return authorizationHeader.substring(7);
    }

   public String extractClaimFromRequest(HttpServletRequest request,String claim) {
        String token = this.extractJwt(request);
        try {
            SignedJWT signedJWT = SignedJWT.parse(token);
            JWTClaimsSet claimsSet = signedJWT.getJWTClaimsSet();

            String response = claimsSet.getStringClaim(claim);
            return response;

        } catch (Exception e) {
            throw new RuntimeException("Erro ao decodificar o token JWT", e);
        }
    }

}
