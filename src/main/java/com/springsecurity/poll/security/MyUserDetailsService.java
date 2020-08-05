package com.springsecurity.poll.security;

import com.springsecurity.poll.domain.User;
import com.springsecurity.poll.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class MyUserDetailsService implements UserDetailsService {

    UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String userNameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUserNameOrEmail(userNameOrEmail, userNameOrEmail).orElseThrow();
        return MyUserPrincipal.create(user);
    }

    public MyUserPrincipal loadUserById(Long id) {
        User user = userRepository.findById(id).orElseThrow();
        return MyUserPrincipal.create(user);
    }
}
