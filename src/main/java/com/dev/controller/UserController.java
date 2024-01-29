package com.dev.controller;

import java.util.Hashtable;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dev.config.JwtService;
import com.dev.model.Note;
import com.dev.model.user.User;
import com.dev.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/signinlogin")
@RequiredArgsConstructor
public class UserController {

    private final JwtService jwtService;
    private final AuthenticationService authenticationService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> regist(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authenticationService.register(request));
        
    }

    @PostMapping("/authentication")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request){
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping("/findUserById")
    public Hashtable <String,Object> findUserById(@RequestParam int idUser) {
        Hashtable <String,Object> response=new Hashtable<>();
        try {
            response.put("data", userService.findById(idUser).get());
            response.put("status",200);
            response.put("message","ok");
        } catch (Exception e) {
            e.printStackTrace();
            response.put("status",500);
            response.put("message",e.getMessage());
        }
        return response;
    }
}
