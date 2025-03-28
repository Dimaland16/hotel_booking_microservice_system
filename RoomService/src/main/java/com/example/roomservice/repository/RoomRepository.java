package com.example.roomservice.repository;

import com.example.roomservice.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoomRepository extends JpaRepository<Room, Long> {
    List<Room> findAllByRoomTypeId(Long roomTypeId);
    @Query("SELECT r FROM Room r JOIN FETCH r.roomType rt WHERE rt.id IN :roomTypesIds")
    List<Room> findAllWithRoomType(@Param("roomTypesIds") List<Long> roomTypesIds);
}
