package com.board.api.security;

import com.board.api.entity.AppUser;
import com.board.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser appUser = userRepository.findByUserId(username);

        if(!ObjectUtils.isEmpty(appUser)) {
            // UserDetails에 담아서 return하면 AutneticationManager가 검증
            return new CustomUserDetails(appUser);
        }

        return null;
    }
}
