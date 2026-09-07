package com.example.demo.repository;

import java.util.Optional;

import com.example.demo.model.entity.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MyUserRepository extends JpaRepository<MyUser, Long>{

    Optional<MyUser> findByUsername (String Username);

}