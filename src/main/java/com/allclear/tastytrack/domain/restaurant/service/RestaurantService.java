package com.allclear.tastytrack.domain.restaurant.service;

import com.allclear.tastytrack.domain.restaurant.dto.RestaurantSearch;
import com.allclear.tastytrack.domain.restaurant.entity.Restaurant;
import com.allclear.tastytrack.domain.review.dto.ReviewRequest;

import java.util.List;

public interface RestaurantService {

    Restaurant getRestaurant(int id, int deletedYn);

    Restaurant updateRestaurantScore(ReviewRequest request);

	List<Restaurant> getRestaurantSearchByRegion(String dosi, String sgg, String type);

}
