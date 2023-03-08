package com.example.demo.Repository;

import com.example.demo.Model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface FlightsRepository extends JpaRepository<Flight, Long> {
    List<Flight> findByDepartureLike(String departure);
    List<Flight> findByDestinationLike(String destination);

    @Query(
            value = "SELECT * FROM flight f ORDER BY f.id",
            nativeQuery = true
    )
    List<Flight> getAllFlightsOrderedById();

    @Query(
            value = "SELECT * FROM flight f WHERE f.departure = :departure AND f.destination = :destination",
            nativeQuery = true)
    List<Flight> checkIfNodeExist(@Param("departure") String departure, @Param("destination") String destination);
}
