package com.example.roomservice.repository;

import com.example.roomservice.model.RoomType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class RoomTypeRepositoryTest {

    @Autowired
    private RoomTypeRepository roomTypeRepository;

    @Test
    @DisplayName("Test findByMaxNumOfPeopleGreaterThanEqual returns matching RoomTypes")
    void testFindByMaxNumOfPeopleGreaterThanEqual() {
        RoomType type1 = new RoomType(null, "Single", 1, 1, 10.0, 50.0, "Single Room", null);
        RoomType type2 = new RoomType(null, "Double", 2, 2, 20.0, 100.0, "Double Room", null);
        RoomType type3 = new RoomType(null, "Family", 4, 4, 30.0, 150.0, "Family Room", null);
        roomTypeRepository.save(type1);
        roomTypeRepository.save(type2);
        roomTypeRepository.save(type3);

        List<RoomType> results = roomTypeRepository.findByMaxNumOfPeopleGreaterThanEqual(2);

        assertThat(results).hasSize(2);
        assertThat(results).extracting(RoomType::getName).containsExactlyInAnyOrder("Double", "Family");
    }

    @Test
    @DisplayName("Test findByMaxNumOfPeopleGreaterThanEqual returns empty list if no matches")
    void testFindByMaxNumOfPeopleGreaterThanEqualNoMatches() {
        RoomType type1 = new RoomType(null, "Single", 1, 1, 10.0, 50.0, "Single Room", null);
        roomTypeRepository.save(type1);

        List<RoomType> results = roomTypeRepository.findByMaxNumOfPeopleGreaterThanEqual(5);

        assertThat(results).isEmpty();
    }
}
