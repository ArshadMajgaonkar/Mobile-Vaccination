package com.aghs.mobilevaccination.data.repository;

import com.aghs.mobilevaccination.data.model.Account;
import com.aghs.mobilevaccination.data.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    List<Member> findByLinkedAccount(Account account);
}
