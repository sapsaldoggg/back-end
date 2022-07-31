package solobob.solobobmate.controller.restaurantDto;

import lombok.AllArgsConstructor;
import lombok.Data;
import solobob.solobobmate.domain.Restaurant;

@Data
public class RestaurantListDto {

    private Long id;

    private String name;

    private String category;

    private String address;

    private double longtitude;   //경도

    private double latitude;    //위도

    public RestaurantListDto(Restaurant restaurant) {
        this.id = restaurant.getId();;
        this.name = restaurant.getName();
        this.category = restaurant.getCategory();
        this.address = restaurant.getAddress();
        this.longtitude = restaurant.getLongtitude();
        this.latitude = restaurant.getLatitude();
    }
}
