package at.technikum.apps.mtcg.repository;

import at.technikum.apps.mtcg.entity.User;

import java.util.Optional;

public interface UserRepo extends CrudRepository<User, Integer>{
    Optional<User> findByUsername(String username);

}