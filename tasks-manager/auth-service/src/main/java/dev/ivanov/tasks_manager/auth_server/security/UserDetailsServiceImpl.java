package dev.ivanov.tasks_manager.auth_server.security;

import dev.ivanov.tasks_manager.auth_server.repositories.postgres.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var accountOptional = accountRepository.findByUsername(username);
        var account = accountOptional.orElseThrow(
                () -> new UsernameNotFoundException("username " + username + " not found"));
        return new UserDetailsImpl(account);
    }
}
