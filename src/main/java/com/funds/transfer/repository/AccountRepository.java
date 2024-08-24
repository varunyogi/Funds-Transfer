package com.funds.transfer.repository;

import com.funds.transfer.entity.Account;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.repository.query.Param;


import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {

    @Query("select acc from Account acc where acc.accountId in :accountId ")
    public List<Account> findByIdIn(@Param("accountId") List<Integer> accountIds);
}
