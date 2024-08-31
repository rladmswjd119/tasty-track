package com.allclear.tastytrack.domain.restaurant.controller;

import java.util.ArrayList;
import java.util.List;

import com.allclear.tastytrack.domain.restaurant.dto.RestaurantSearch;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.allclear.tastytrack.domain.auth.UserDetailsImpl;
import com.allclear.tastytrack.domain.restaurant.dto.RestaurantDetail;
import com.allclear.tastytrack.domain.restaurant.entity.Restaurant;
import com.allclear.tastytrack.domain.restaurant.service.RestaurantService;
import com.allclear.tastytrack.domain.review.dto.ReviewResponse;
import com.allclear.tastytrack.domain.review.entity.Review;
import com.allclear.tastytrack.domain.review.service.ReviewService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/restaurants")
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final ReviewService reviewService;

    @PostMapping("")
    public ResponseEntity<RestaurantDetail> getRestaurant(@AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody int id) {

        Restaurant restaurant = restaurantService.getRestaurant(id, 0);
        List<Review> reviews = reviewService.getAllReviewsByRestaurantId(id);

        List<ReviewResponse> reviewResponses = new ArrayList<>();
        if (!reviews.isEmpty()) {
            reviewResponses = reviewService.createListReviewResponse(restaurant, reviews, reviewResponses);
        }
        RestaurantDetail restaurantDetail = RestaurantDetail.builder()
                .name(restaurant.getName())
                .type(restaurant.getType())
                .status(restaurant.getStatus())
                .rateScore(restaurant.getRateScore())
                .oldAddress(restaurant.getOldAddress())
                .newAddress(restaurant.getNewAddress())
                .lon(restaurant.getLon())
                .lat(restaurant.getLat())
                .lastUpdateAt(restaurant.getLastUpdatedAt())
                .reviewResponses(reviewResponses)
                .build();

        return ResponseEntity.ok(restaurantDetail);
    }

    @GetMapping("/region")
    public List<Restaurant> getRestuarantSearchByRegion(@RequestParam String dosi,
                                                        @RequestParam String sgg,
                                                        @RequestParam String type){
        // 특정 지역의 맛집을 검색하여 반환
        return restaurantService.getRestaurantSearchByRegion(dosi, sgg, type);
    }


}
