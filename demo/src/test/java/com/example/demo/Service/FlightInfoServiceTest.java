package com.example.demo.Service;

import com.example.demo.Model.Flight;
import com.example.demo.Model.FlightInfoEntity;
import com.example.demo.Repository.FlightInfoRepository;
import com.example.demo.Repository.FlightsRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class FlightInfoServiceTest {

    @Mock
    private FlightInfoRepository info_repo;

    @Mock
    private FlightsRepository flight_repo;

    @InjectMocks
    private FlightInfoService info_service;

    public List<Flight> setup(){
        return Arrays.asList(new Flight(1L, "minsk", "brest", null));
    }

    @Test
    public void addFlightInfo(){
        lenient().when(flight_repo.findById(setup().get(0).getId())).thenReturn(Optional.of(setup().get(0)));
        FlightInfoEntity info = new FlightInfoEntity(
                "belavia", 1, 100, LocalDateTime.now(), setup().get(0));
        lenient().when(info_repo.save(info)).thenReturn(info);
        assertEquals(info_service.addFlightInfo(info, 1L), info);
    }

    @Test
    public void updateFlightInfoIfNotExist(){
        FlightInfoEntity info = new FlightInfoEntity(
                "belavia", 1, 100, LocalDateTime.now(), setup().get(0));
        lenient().when(info_repo.findById(1L)).thenReturn(Optional.of(info));
        lenient().when(info_repo.checkIfNodeExist(
                info.getCarrier(), info.getFlightDuration(), info.getCost(), info.getDate())
        ).thenReturn(Arrays.asList());
        assertTrue(info_service.updateFlight(1L, info));
    }

    @Test
    public void updateFlightInfoIfExist(){
        FlightInfoEntity info = new FlightInfoEntity(
                "belavia", 1, 100, LocalDateTime.now(), setup().get(0));
        lenient().when(info_repo.findById(1L)).thenReturn(Optional.of(info));
        lenient().when(info_repo.checkIfNodeExist(
                info.getCarrier(), info.getFlightDuration(), info.getCost(), info.getDate())
        ).thenReturn(Arrays.asList(info));
        assertFalse(info_service.updateFlight(1L, info));
    }

    @Test
    public void checkIfNotExpiredIfNotExpired(){
        FlightInfoEntity info = new FlightInfoEntity(1L,
                "belavia", 1, 100, LocalDateTime.of(
                        2023, 10, 10, 10, 10), setup().get(0));
        lenient().when(info_repo.findById(1L)).thenReturn(Optional.of(info));
        info_service.checkIfFlightNotExpired(1L);
        assertEquals(info_repo.findById(1L).get(), info);
    }

    @Test
    public void checkIfNotExpiredIfExpired() throws NullPointerException{
        Flight flight = new Flight(2L, "minsk", "brest", null);
        FlightInfoEntity info = new FlightInfoEntity(2L,
                "belavia", 1, 100, LocalDateTime.of(
                2023, 03, 10, 10, 10), flight);
        info.setFlight(flight);
        lenient().when(info_repo.findById(2L)).thenReturn(Optional.of(info));
        lenient().when(flight_repo.findById(2L)).thenReturn(Optional.of(flight));
        info_service.checkIfFlightNotExpired(2L);
        verify(info_repo, times(1)).deleteById(2L);
    }

    @Test
    public void findFlightInfoBetweenTest(){
        FlightInfoEntity info = new FlightInfoEntity(
                "belavia", 1, 100, LocalDateTime.now(), setup().get(0));
        lenient().when(info_repo.findAllFlightsBetween(1L, 1, 9)).thenReturn(
                Arrays.asList(info)
        );
        info_service.findFlightInfoBetween(1L, "1", "9");
        verify(info_repo, times(1)).findAllFlightsBetween(
                1L, Integer.valueOf(1), Integer.valueOf(9)
        );
    }

    @Test
    public void findFlightInfoBelowTest() {
        FlightInfoEntity info = new FlightInfoEntity(
                "belavia", 1, 100, LocalDateTime.now(), setup().get(0));
        lenient().when(info_repo.findAllFlightsBelow(1L, 9)).thenReturn(
                Arrays.asList(info)
        );
        info_service.findFlightInfoBetween(1L, null, "9");
        verify(info_repo, times(1)).findAllFlightsBelow(
                1L, Integer.valueOf(9)
        );
    }
    @Test
    public void findFlightInfoAboveTest() {
        FlightInfoEntity info = new FlightInfoEntity(
                "belavia", 1, 100, LocalDateTime.now(), setup().get(0));
        lenient().when(info_repo.findAllFlightsAbove(1L, 1)).thenReturn(
                Arrays.asList(info)
        );
        info_service.findFlightInfoBetween(1L, "1", null);
        verify(info_repo, times(1)).findAllFlightsAbove(
                1L, Integer.valueOf(1)
        );
    }
}
