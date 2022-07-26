package solobob.solobobmate.service;

import solobob.solobobmate.controller.restaurantDto.RestaurantListDto;
import solobob.solobobmate.domain.Restaurant;
import solobob.solobobmate.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public List<RestaurantListDto> restaurantList() {
        List<RestaurantListDto> restaurantList = new ArrayList<>();
        for (Restaurant restaurant : restaurantRepository.findAll()) {
            restaurantList.add(new RestaurantListDto(restaurant.getId(), restaurant.getName(), restaurant.getCategory(),
                    restaurant.getAddress(), restaurant.getLongtitude(), restaurant.getLatitude()));
        }
        return restaurantList;
    }
}
