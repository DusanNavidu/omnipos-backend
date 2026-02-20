package com.omnipos.omniposbackend.controller;

import com.omnipos.omniposbackend.dto.ShopCategoryDTO;
import com.omnipos.omniposbackend.service.ShopCategoryService;
import com.omnipos.omniposbackend.util.APIResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Dusan
 * @date 2/20/2026
 */

@RestController
@RequestMapping("/api/v1/shop-category")
@RequiredArgsConstructor
@CrossOrigin
@Slf4j
public class ShopCategoryController {

    private final ShopCategoryService shopCategoryService;

    @PostMapping("/save")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<APIResponse<ShopCategoryDTO>> saveCategory(@Valid @RequestBody ShopCategoryDTO dto) {
        log.info("Saving new shop category: {}", dto.getCategory_type());
        ShopCategoryDTO saved = shopCategoryService.saveCategory(dto);
        return ResponseEntity.ok(new APIResponse<>(200, "Category Saved", saved));
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('SUPER_ADMIN') or hasRole('SHOP_ADMIN')")
    public ResponseEntity<APIResponse<List<ShopCategoryDTO>>> getAllCategories() {
        return ResponseEntity.ok(new APIResponse<>(200, "Success", shopCategoryService.getAllCategories()));
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<APIResponse<ShopCategoryDTO>> updateCategory(
            @PathVariable String id,
            @Valid @RequestBody ShopCategoryDTO dto) {

        ShopCategoryDTO updated = shopCategoryService.updateCategory(id, dto);
        return ResponseEntity.ok(new APIResponse<>(200, "Category Updated Successfully", updated));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public ResponseEntity<APIResponse<Void>> deleteCategory(@PathVariable String id) {
        shopCategoryService.deleteCategory(id);
        return ResponseEntity.ok(new APIResponse<>(200, "Category Deleted Successfully", null));
    }
}
