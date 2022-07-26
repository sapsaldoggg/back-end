package solobob.solobobmate.controller.restaurantDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RestaurantListDto {

    private Long id;

    private String name;

    private String category;

    private String address;

    private double longtitude;   //경도

    private double latitude;    //위도
}
