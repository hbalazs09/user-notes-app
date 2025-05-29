package com.horvath.usernotesapp.repository;

import com.horvath.usernotesapp.domain.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}