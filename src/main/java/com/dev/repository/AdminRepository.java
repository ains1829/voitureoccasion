package com.dev.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.dev.model.user.Admin;
import java.util.List;


public interface AdminRepository extends JpaRepository<Admin, Integer> {
    List<Admin> findByMailAndPwd(String mail, String pwd);
}
