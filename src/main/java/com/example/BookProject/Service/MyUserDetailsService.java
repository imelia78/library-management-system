package com.example.BookProject.Service;

import com.example.BookProject.Config.MyUserDetails;
import com.example.BookProject.Model.MyUser;
import com.example.BookProject.Repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service

public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<MyUser> user = userRepository.findMyUserByName(username);
        return user.map(MyUserDetails::new).orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
