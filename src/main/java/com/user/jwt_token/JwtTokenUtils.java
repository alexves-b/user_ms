package com.user.jwt_token;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;

@Component
public class JwtTokenUtils{
    public String decodeJWTToken(String token) {
        Base64.Decoder decoder = Base64.getUrlDecoder();
        String[] chunks = token.split("\\.");
        String header = new String(decoder.decode(chunks[0]));
        return new String(decoder.decode(chunks[1]));
    }
}