package com.omnipos.omniposbackend.model;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * @author Dusan
 * @date 2/20/2026
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "shop_categories")
@Builder
public class ShopCategories {
    @Id
    private String category_id;

    @Size(max = 50, message = "Category type must be under 50 characters")
    private String category_type;
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String category_description;
}