package solobob.solobobmate.controller;

import solobob.solobobmate.controller.restaurantDto.RestaurantListDto;
import solobob.solobobmate.service.RestaurantService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/user")
public class RestaurantController {

    private final RestaurantService restaurantService;

    //식당 조회 (메인페이지 예상 - 지도 화면)
    @GetMapping("/restaurants")
    public List<RestaurantListDto> restaurantList() {
        return restaurantService.restaurantList();
    }

}
