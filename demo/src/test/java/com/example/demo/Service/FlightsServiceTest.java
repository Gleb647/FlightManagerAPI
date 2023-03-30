package com.example.demo.Service;

import com.example.demo.Model.Flight;
import com.example.demo.Repository.FlightInfoRepository;
import com.example.demo.Repository.FlightsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
class FlightsServiceTest {

    @Mock
    private FlightsRepository base_rep;
    @Mock
    private FlightInfoRepository info_rep;
    @InjectMocks
    private FlightsService service;

    private List<Flight> FlightsList() {
        return Arrays.asList(
                new Flight(1L, "minsk", "istanbul", null),
                new Flight(2L, "dublin", "paris", null),
                new Flight(3L, "london", "berlin", null)
        );
    }

    @BeforeEach
    void setUp() {
        List<Flight> lst = FlightsList();
        lenient().when(base_rep.getAllFlightsOrderedById()).thenReturn(lst);
        lenient().when(base_rep.findById(1L)).thenReturn(Optional.ofNullable(lst.get(0)));
        lenient().when(base_rep.findById(2L)).thenReturn(Optional.ofNullable(lst.get(1)));
        lenient().when(base_rep.findById(3L)).thenReturn(Optional.ofNullable(lst.get(2)));
        lenient().when(service.getFlights("", "")).thenReturn(lst);
    }

    @Test
    void updateExistingFlight() throws IOException {
        lenient().when(base_rep.checkIfNodeExist("oman", "brazilia", null)).thenReturn(Arrays.asList());
        Flight upd = new Flight(1L, "oman", "brazilia", null);
        boolean added = service.updateFlight(1L, upd.getDeparture(), upd.getDestination(), null);
        assertEquals(added, true);
    }

    @Test
    void updateNotExistingFlight() throws IOException {
        lenient().when(base_rep.checkIfNodeExist("omana", "brazilia", null)).thenReturn(Arrays.asList(
                new Flight("omana", "brazilia", null)
        ));
        Flight upd = new Flight(1L, "omana", "brazilia", null);
        boolean added = service.updateFlight(1L, upd.getDeparture(), upd.getDestination(), null);
        assertEquals(added, false);
    }

    @Test
    void getFlights() {
        lenient().when(base_rep.findByDepartureLike("minsk")).thenReturn(Arrays.asList(
                new Flight("minsk", "moscow", null),
                new Flight("minsk", "berlin", null)));

        lenient().when(base_rep.findByDestinationLike("moscow")).thenReturn(Arrays.asList(
                new Flight("krakow", "moscow", null),
                new Flight("minsk", "moscow", null)));

        List<Flight> ls = service.getFlights("minsk", null);
        assertEquals(ls.size(), 2);
        assertEquals(ls.get(0).getDestination(), "moscow");
        assertEquals(ls.get(1).getDestination(), "berlin");

        ls = service.getFlights(null, null);
        assertEquals(ls.size(), 3);
        assertEquals(ls.get(0).getDestination(), "istanbul");
    }

    @Test
    void canSaveFlight() throws IOException {
        Flight fl = new Flight("istanbul", "moscow", null);
        lenient().when(base_rep.findByDepartureLike("istanbul")).thenReturn(Arrays.asList(fl));
        lenient().when(base_rep.findByDepartureLike("moscow")).thenReturn(Arrays.asList());
        boolean added = service.saveFlight(fl.getDeparture(), fl.getDestination(), null);
        assertTrue(added);
    }

    @Test
    void canNotSaveFlight() throws IOException {
        Flight fl = new Flight("london", "berlin", null);
        lenient().when(base_rep.findByDepartureLike("london")).thenReturn(Arrays.asList(fl));
        lenient().when(base_rep.findByDestinationLike("berlin")).thenReturn(Arrays.asList(fl));
        boolean added = service.saveFlight(fl.getDeparture(), fl.getDestination(), null);
        assertFalse(added);
    }
}