package com.omnipos.omniposbackend.model;

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
@Document(collection = "shop_details")
@Builder
public class ShopDetails {
    @Id
    private String shop_id;

    private String shop_name;
    private String shop_sub_title;
    private String address;
    private String contact_number;
    private String logo_image_url;
    private String shop_category_id;
}
