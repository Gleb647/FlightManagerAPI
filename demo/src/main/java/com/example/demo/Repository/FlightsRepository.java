package com.example.demo.Repository;

import com.example.demo.Model.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface FlightsRepository extends JpaRepository<Flight, Long> {
    @Query(
            value = "SELECT * FROM flight f WHERE f.departure LIKE :departure% ORDER BY f.id",
            nativeQuery = true
    )
    List<Flight> findByDepartureLike(@Param("departure") String departure);
    @Query(
            value = "SELECT * FROM flight f WHERE f.destination LIKE :destination% ORDER BY f.id",
            nativeQuery = true
    )
    List<Flight> findByDestinationLike(String destination);

    @Query(
            value = "SELECT * FROM flight f ORDER BY f.id",
            nativeQuery = true
    )
    List<Flight> getAllFlightsOrderedById();

    @Query(
            value = "SELECT * FROM flight f WHERE f.departure = :departure AND " +
                    "f.destination = :destination AND f.image = :image",
            nativeQuery = true)
    List<Flight> checkIfNodeExist(
            @Param("departure") String departure,
            @Param("destination") String destination,
            @Param("image") String image);
}
