package com.aghs.mobilevaccination.data.repository;

import com.aghs.mobilevaccination.data.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public  interface AccountRepository extends JpaRepository<Account, Long> {
    Account findByMobileNumber(String mobileNumber);
}
