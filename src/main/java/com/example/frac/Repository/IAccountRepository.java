package com.example.frac.Repository;

import com.example.frac.Entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Integer>
{
    public boolean existsByUsername(String Username);
    public Account findByUsername(String Username);
}
