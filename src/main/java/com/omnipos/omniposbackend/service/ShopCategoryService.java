package com.omnipos.omniposbackend.service;

import com.omnipos.omniposbackend.dto.ShopCategoryDTO;

import java.util.List;

/**
 * @author Dusan
 * @date 2/20/2026
 */

public interface ShopCategoryService {
    ShopCategoryDTO saveCategory(ShopCategoryDTO dto);
    List<ShopCategoryDTO> getAllCategories();
    ShopCategoryDTO updateCategory(String id, ShopCategoryDTO dto);
    void deleteCategory(String id);
}