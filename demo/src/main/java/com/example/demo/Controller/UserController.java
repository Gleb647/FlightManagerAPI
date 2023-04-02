package com.example.demo.Controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.Logger.CustomLogger;
import com.example.demo.Model.Role;
import com.example.demo.RoleToUser.RoleToUserForm;
import com.example.demo.Service.UserService;
import com.example.demo.Model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;


import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@CrossOrigin("*")
@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/users/get")
    public ResponseEntity<List<User>> getUsers(){
        return ResponseEntity.ok(userService.getUsers());
    }

    @PostMapping("/signup")
    public ResponseEntity signUp(@RequestBody User user){
        if (userService.saveUser(user) == null){
            CustomLogger.error(
                    "{}: user with username {} is already exist", this.getClass().getName(),
                    user.getUsername());
            return new ResponseEntity("User with provided username is already exist", HttpStatus.CONFLICT);
        }
        CustomLogger.info("{}: new user created", this.getClass().getName());
        return new ResponseEntity("User created", HttpStatus.OK);
    }


    @PostMapping("/role/add")
    public ResponseEntity<Role> addRole(@RequestBody Role role){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/role/add").toUriString());
        return ResponseEntity.created(uri).body(userService.saveRole(role));
    }

    @PostMapping("/role/addtouser")
    public ResponseEntity<?> addUser(@RequestBody RoleToUserForm form){
        URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path("/role/add").toUriString());
        userService.addRoleToUser(form.getUsername(), form.getRoleName());
        return ResponseEntity.created(uri).build();
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            try{
                String refresh_token = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
                JWTVerifier verifier = JWT.require(algorithm).build();
                DecodedJWT decodedJWT = verifier.verify(refresh_token);
                String username = decodedJWT.getSubject();
                User user = userService.getUser(username);
                Date access_toke_exp = new Date(System.currentTimeMillis() + 15 * 60 * 1000);
                long mili = access_toke_exp.getTime();
                String mil = String.valueOf(mili);
                String access_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(access_toke_exp)
                        .withIssuer(request.getRequestURI().toString())
                        .withClaim("roles", user.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);
                String new_refresh_token = JWT.create()
                        .withSubject(user.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 60 * 60 * 1000))
                        .withIssuer(request.getRequestURI().toString())
                        .sign(algorithm);
                Map<String, String > tokens = new HashMap<>();
                tokens.put("access_token", access_token);
                tokens.put("refresh_token", new_refresh_token);
                tokens.put("exp", mil);
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }catch (Exception exception){
                response.setHeader("error", exception.getMessage());
                response.setStatus(FORBIDDEN.value());
                Map<String, String > error = new HashMap<>();
                error.put("error_message", exception.getMessage());
                response.setContentType(APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        }else{
            throw new RuntimeException("Refresh token is missing");
        }
    }

}