package solobob.solobobmate.service;

import solobob.solobobmate.controller.restaurantDto.RestaurantListDto;
import solobob.solobobmate.domain.Restaurant;
import solobob.solobobmate.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public List<RestaurantListDto> restaurantList() {

        return restaurantRepository.findAll().stream().map(r -> new RestaurantListDto(r)).collect(Collectors.toList());

    }
}
