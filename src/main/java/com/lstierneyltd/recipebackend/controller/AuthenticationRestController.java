package com.lstierneyltd.recipebackend.controller;

import com.lstierneyltd.recipebackend.entities.AuthenticationResponse;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

public interface AuthenticationRestController {
    @PostMapping
    AuthenticationResponse authenticate(@RequestBody UserDetails user);
}
