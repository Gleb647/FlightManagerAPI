package com.example.demo.Jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

import static java.util.Arrays.stream;

@Component
public class JwtServiceImpl implements JwtService{
    @Override
    public  Map<String, String > generateToken(String username, HttpServletRequest request, List<String> lst) {
        Date access_toke_exp = new Date(System.currentTimeMillis() + 60 * 60 * 1000);
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        String access_token = JWT.create()
                .withSubject(username)
                .withExpiresAt(access_toke_exp)
                .withIssuer(request.getRequestURI().toString())
                .withClaim("roles", lst)
                .sign(algorithm);

        long mili = access_toke_exp.getTime();
        String mil = String.valueOf(mili);

        String refresh_token = JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 1000))
                .withIssuer(request.getRequestURI().toString())
                .sign(algorithm);

        Map<String, String > tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        tokens.put("exp", mil);
        return tokens;
    }

    @Override
    public DecodedJWT verifyToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(token);
        return decodedJWT;
    }

    @Override
    public void refreshToken() {

    }
}
