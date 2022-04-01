package login.jwtlogin.domain;

import lombok.Getter;

import javax.persistence.*;

@Getter
@Entity
public class Restaurant {

    @Id @GeneratedValue
    @Column(name = "restaurant_id")
    private Long id;

    private String name;

    private String category;

    private String address;

    private double longtitude;   //경도

    private double latitude;    //위도

}