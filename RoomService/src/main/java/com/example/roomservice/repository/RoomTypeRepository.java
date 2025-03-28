package com.example.roomservice.repository;

import com.example.roomservice.model.RoomType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomTypeRepository extends JpaRepository<RoomType, Long> {
    List<RoomType> findByMaxNumOfPeopleGreaterThanEqual(int maxNumberOfPeople);
}
