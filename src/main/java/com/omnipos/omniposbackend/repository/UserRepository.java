package com.omnipos.omniposbackend.repository;

import com.omnipos.omniposbackend.model.Role;
import com.omnipos.omniposbackend.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * @author Dusan
 * @date 2/19/2026
 */

@Repository
public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByShopIdAndRole(String shopId, Role role);
}