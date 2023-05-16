package com.lstierneyltd.recipebackend.repository;

import com.lstierneyltd.recipebackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

}
