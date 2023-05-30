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
    protected static final String USER_NOT_FOUND_WITH_USERNAME = "User not found with username: ";
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException(USER_NOT_FOUND_WITH_USERNAME + username);
        }
        // TODO fix {noop}
        return new org.springframework.security.core.userdetails.User(user.getUsername(), "{noop}" + user.getPassword(),
                new ArrayList<>());
    }
}

