package com.aghs.mobilevaccination.service;

import com.aghs.mobilevaccination.auth.GeneralUserDetails;
import com.aghs.mobilevaccination.data.model.Account;
import com.aghs.mobilevaccination.data.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class GeneralUserDetailService implements UserDetailsService {
    @Autowired
    private AccountRepository accountRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByMobileNumber(username);
        if(account == null) {
            throw new UsernameNotFoundException("User not found.");
        }
        return new GeneralUserDetails(account);
    }
}
