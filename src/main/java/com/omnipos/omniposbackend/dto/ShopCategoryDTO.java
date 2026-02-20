package com.omnipos.omniposbackend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Dusan
 * @date 2/20/2026
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopCategoryDTO {

    private String category_id;

    @NotBlank(message = "Category type is required")
    @Size(max = 50, message = "Category type must be under 50 characters")
    private String category_type;

    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String category_description;
}