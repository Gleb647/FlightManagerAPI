package com.example.demo.Repository;

import com.example.demo.Model.Flight;
import com.example.demo.Model.FlightInfoEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.time.LocalDateTime;
import java.util.List;


public interface FlightInfoRepository extends JpaRepository<FlightInfoEntity, Long> {

    @Query(
        value = "SELECT * FROM flight_info u WHERE u.flight_id = :id",
        nativeQuery = true)
    List<FlightInfoEntity> findAllExpNotes(@Param("id") Long id);

    @Modifying
    @Transactional
    @Query(
            value = "DELETE FROM flight_info u WHERE u.flight_id = :id",
            nativeQuery = true)
    void deleteAllFlightInfoByFlightId(@Param("id") Long id);

    @Query(
            value = "SELECT * FROM flight_info u WHERE u.flight_id = :id AND u.cost >= :min AND u.cost <= :max",
            nativeQuery = true)
    List<FlightInfoEntity> findAllFlightsBetween(@PathVariable("id") Long id, @Param("min") Integer min, @Param("max") Integer max);

    @Query(
            value = "SELECT * FROM flight_info u WHERE u.flight_id = :id AND u.cost >= :min",
            nativeQuery = true)
    List<FlightInfoEntity> findAllFlightsAbove(@PathVariable("id") Long id, @Param("min") Integer min);

    @Query(
            value = "SELECT * FROM flight_info u WHERE u.flight_id = :id AND u.cost <= :max",
            nativeQuery = true)
    List<FlightInfoEntity> findAllFlightsBelow(@PathVariable("id") Long id, @Param("max") Integer max);

    @Query(
            value = "SELECT * FROM flight_info f WHERE f.carrier = :carrier AND f.flight_duration = :flightDuration " +
                    "AND f.cost = :cost AND f.date = :date",
            nativeQuery = true)
    List<FlightInfoEntity> checkIfNodeExist(
            @Param("carrier") String carrier, @Param("flightDuration") Integer flightDuration,
            @Param("cost") Integer cost, @Param("date") LocalDateTime date);
}
