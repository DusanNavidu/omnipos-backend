package com.omnipos.omniposbackend.repository;

import com.omnipos.omniposbackend.model.ShopCategories;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Dusan
 * @date 2/20/2026
 */

@Repository
public interface ShopCategoryRepository extends MongoRepository<ShopCategories, String> {
}