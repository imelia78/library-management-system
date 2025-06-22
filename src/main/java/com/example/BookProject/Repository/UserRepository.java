package com.example.BookProject.Repository;

import com.example.BookProject.Model.MyUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<MyUser, Long> {

    Optional<MyUser> findMyUserByName(String name);
}
