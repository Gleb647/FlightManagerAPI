package com.example.demo.Service;

import com.example.demo.Logger.CustomLogger;
import com.example.demo.Model.Flight;
import com.example.demo.Model.FlightInfoEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface FlightInfoService {
    FlightInfoEntity addFlightInfo(FlightInfoEntity flightInfo, Long id);

    void checkIfFlightNotExpired(Long id);

    List<FlightInfoEntity> findFlightInfoBetween(Long id, String min, String max, Pageable paging);

    boolean updateFlight(Long id, FlightInfoEntity info);

    List<FlightInfoEntity> findAllExpNotes(Long id, Pageable paging);

    void deleteFlightInfo(Long id);
}
