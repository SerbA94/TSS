package ua.tss.repository;


import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import ua.tss.model.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    
    //List<User> findByUsername(String name);
    User findByUsername(String username);
    
}