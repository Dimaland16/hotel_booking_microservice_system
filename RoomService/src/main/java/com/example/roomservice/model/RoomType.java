package com.example.roomservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.List;

@Entity
public class RoomType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name cannot be blank")
    @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
    private String name;
    @Min(value = 1, message = "Maximum number of people must be at least 1")
    @Max(value = 10, message = "Maximum number of people cannot exceed 10")
    private int maxNumOfPeople;
    @Min(value = 1, message = "Number of beds must be at least 1")
    private int numOfBeds;
    @DecimalMin(value = "10.0", message = "Area must be at least 10 square meters")
    private double area;
    @DecimalMin(value = "0.0", inclusive = false, message = "Price per night must be greater than 0")
    private double pricePerNight;
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;

    @OneToMany(mappedBy = "roomType", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Room> rooms;

    public RoomType() {}

    public RoomType(Long id, String name, int maxNumOfPeople, int numOfBeds, double area, double pricePerNight, String description, List<Room> rooms) {
        this.id = id;
        this.name = name;
        this.maxNumOfPeople = maxNumOfPeople;
        this.numOfBeds = numOfBeds;
        this.area = area;
        this.pricePerNight = pricePerNight;
        this.description = description;
        this.rooms = rooms;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMaxNumOfPeople() {
        return maxNumOfPeople;
    }

    public void setMaxNumOfPeople(int maxNumOfPeople) {
        this.maxNumOfPeople = maxNumOfPeople;
    }

    public int getNumOfBeds() {
        return numOfBeds;
    }

    public void setNumOfBeds(int numOfBeds) {
        this.numOfBeds = numOfBeds;
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        this.area = area;
    }

    public double getPricePerNight() {
        return pricePerNight;
    }

    public void setPricePerNight(double pricePerNight) {
        this.pricePerNight = pricePerNight;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }
}
