package ua.tss.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ua.tss.model.User;



public interface UserRepo extends JpaRepository<User, Long> {
    User findByUsername(String username);
}