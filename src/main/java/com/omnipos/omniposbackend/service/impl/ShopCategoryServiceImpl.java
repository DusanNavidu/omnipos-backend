package com.omnipos.omniposbackend.service.impl;

import com.omnipos.omniposbackend.dto.ShopCategoryDTO;
import com.omnipos.omniposbackend.exception.ResourceNotFoundException;
import com.omnipos.omniposbackend.model.ShopCategories;
import com.omnipos.omniposbackend.repository.ShopCategoryRepository;
import com.omnipos.omniposbackend.service.ShopCategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Dusan
 * @date 2/20/2026
 */

@Service
@RequiredArgsConstructor
@Slf4j
public class ShopCategoryServiceImpl implements ShopCategoryService {

    private final ShopCategoryRepository repository;
    private final ModelMapper modelMapper;

    @Override
    public ShopCategoryDTO saveCategory(ShopCategoryDTO dto) {
        log.info("Request to save new shop category: {}", dto.getCategory_type());

        ShopCategories category = modelMapper.map(dto, ShopCategories.class);
        ShopCategories saved = repository.save(category);

        log.info("Successfully saved category with ID: {}", saved.getCategory_id());
        return modelMapper.map(saved, ShopCategoryDTO.class);
    }

    @Override
    public List<ShopCategoryDTO> getAllCategories() {
        log.info("Fetching all shop categories from database");
        return repository.findAll().stream()
                .map(cat -> modelMapper.map(cat, ShopCategoryDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ShopCategoryDTO updateCategory(String id, ShopCategoryDTO dto) {
        log.info("Request to update category ID: {}", id);
        ShopCategories existingCategory = repository.findById(id)
                .orElseThrow(() ->
                {
                    log.error("Update failed! Category ID {} not found", id);
                    return new ResourceNotFoundException("Shop Category not found with ID: " + id);
                });

        existingCategory.setCategory_type(dto.getCategory_type());
        existingCategory.setCategory_description(dto.getCategory_description());

        ShopCategories updated = repository.save(existingCategory);
        log.info("Successfully updated category ID: {}", id);

        return modelMapper.map(updated, ShopCategoryDTO.class);
    }

    @Override
    public void deleteCategory(String id) {
        log.warn("Attempting to delete category ID: {}", id);

        if (!repository.existsById(id)) {
            log.error("Delete failed! Category ID {} does not exist", id);
            throw new ResourceNotFoundException("Cannot delete! Category ID " + id + " does not exist.");
        }

        repository.deleteById(id);
        log.info("Successfully deleted category ID: {}", id);
    }
}
