package com.lstierneyltd.recipebackend.service;

import com.lstierneyltd.recipebackend.entities.User;
import com.lstierneyltd.recipebackend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found with username: " + username);
        }
        // TODO fix {noop}
        return new org.springframework.security.core.userdetails.User(user.getUsername(), "{noop}" + user.getPassword(),
                new ArrayList<>());
    }
}

