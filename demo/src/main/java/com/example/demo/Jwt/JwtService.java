package com.example.demo.Jwt;


import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

public interface JwtService {
    Map<String, String > generateToken(String username, HttpServletRequest request, List<String> lst);
    DecodedJWT verifyToken(String token);

    void refreshToken();
}
