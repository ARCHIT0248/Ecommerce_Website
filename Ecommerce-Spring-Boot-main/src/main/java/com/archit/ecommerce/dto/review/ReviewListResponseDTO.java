package com.archit.ecommerce.dto.review;

import com.archit.ecommerce.model.Review;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewListResponseDTO {
    private List<Review> reviewList;
}
