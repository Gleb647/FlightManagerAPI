package com.example.demo.Service;

import com.example.demo.Model.Flight;
import com.example.demo.Model.FlightInfoEntity;
import com.example.demo.Repository.FlightInfoRepository;
import com.example.demo.Repository.FlightsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.internal.stubbing.answers.CallsRealMethods;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FlightsServiceTest {

    @Mock
    private FlightsRepository base_rep;
    @Mock
    private FlightInfoRepository info_rep;
    @Mock
    private FlightConverterUtilsImpl fileConverter;
    @InjectMocks
    private FlightsServiceImpl service;

    private Page<Flight> FlightsList() {
        return new PageImpl<>(Arrays.asList(
                new Flight(1L, "minsk", "istanbul", null),
                new Flight(2L, "dublin", "paris", null),
                new Flight(3L, "london", "berlin", null)
        ));
    }

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        Page<Flight> lst = FlightsList();
        lenient().when(base_rep.getAllFlightsOrderedById(null)).thenReturn(lst);
        lenient().when(base_rep.findById(1L)).thenReturn(Optional.ofNullable(lst.getContent().get(0)));
        lenient().when(base_rep.findById(2L)).thenReturn(Optional.ofNullable(lst.getContent().get(1)));
        lenient().when(base_rep.findById(3L)).thenReturn(Optional.ofNullable(lst.getContent().get(2)));
    }

    @Test
    void updateExistingFlight(){
        lenient().when(base_rep.checkIfNodeExist("oman", "brazilia", null)).thenReturn(Arrays.asList());
        Flight upd = new Flight(1L, "oman", "brazilia", null);
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Multipart file test".getBytes()
        );
        Mockito.doNothing().when(fileConverter).createFileLocal(file);
        boolean added = service.updateFlight(1L, upd.getDeparture(), upd.getDestination(), file);
        assertEquals(added, true);
    }

    @Test
    void updateNotExistingFlight(){
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Multipart file test".getBytes()
        );
        lenient().when(base_rep.checkIfNodeExist("oman", "brazil", file.getOriginalFilename())).
                thenReturn(List.of(new Flight("oman", "brazil", file.getOriginalFilename())));
        Flight upd = new Flight(1L, "oman", "brazil", file.getOriginalFilename());
        boolean added = service.updateFlight(1L, upd.getDeparture(), upd.getDestination(), file);
        assertEquals(added, false);
    }

    @Test
    void getFlights() {
        Flight fl = new Flight(1L, "minsk", "moscow",null);
        Flight fl2 = new Flight(2L, "minsk", "berlin",null);
        Flight fl3 = new Flight(3L, "istanbul", "moscow",null);
        lenient().when(info_rep.findAllExpNotes(1L, null)).
                thenReturn(new PageImpl<>(Arrays.asList(
                        new FlightInfoEntity("ad", 1, 1, LocalDateTime.now(), fl))));
        lenient().when(info_rep.findAllExpNotes(2L, null)).
                thenReturn(new PageImpl<>(Arrays.asList(
                        new FlightInfoEntity("ad", 1, 1, LocalDateTime.now(), fl2))));
        lenient().when(info_rep.findAllExpNotes(3L, null)).
                thenReturn(new PageImpl<>(Arrays.asList(
                        new FlightInfoEntity("ad", 1, 1, LocalDateTime.now(), fl3))));

        lenient().when(base_rep.findByDepartureLike("minsk", null)).
                thenReturn(new PageImpl<>(Arrays.asList(fl, fl2)));
        lenient().when(base_rep.findByDestinationLike("moscow", null)).
                thenReturn(new PageImpl<>(Arrays.asList(fl, fl2, fl3)));

        Page<Flight> ls = service.getFlights("minsk", null, null);
        assertEquals(ls.getContent().size(), 2);
        assertEquals(ls.getContent().get(0).getDestination(), "moscow");
        assertEquals(ls.getContent().get(1).getDestination(), "berlin");

        ls = service.getFlights(null, null, null);
        assertEquals(ls.getContent().size(), 3);
        assertEquals(ls.getContent().get(0).getDestination(), "istanbul");
    }

    @Test
    void canSaveFlight(){
        Flight fl = new Flight(1L, "istanbul", "moscow",null);
        lenient().when(base_rep.findByDepartureLike("istanbul", null)).
                thenReturn(new PageImpl<>(Arrays.asList(fl)));
        lenient().when(base_rep.findByDestinationLike("moscow", null)).
                thenReturn(new PageImpl<>(Arrays.asList()));
        lenient().when(info_rep.findAllExpNotes(1L, null)).
                thenReturn(new PageImpl<>(Arrays.asList(
                        new FlightInfoEntity("ad", 1, 1, LocalDateTime.now(), fl))));
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Multipart file test".getBytes()
        );
        Mockito.doNothing().when(fileConverter).createFileLocal(file);
        boolean added = service.saveFlight(fl.getDeparture(), fl.getDestination(), file);
        assertTrue(added);
    }

    @Test
    void canNotSaveFlight(){
        Flight fl = new Flight(1L, "istanbul", "moscow",null);
        lenient().when(base_rep.findByDepartureLike("istanbul", null)).
                thenReturn(new PageImpl<>(Arrays.asList(fl)));
        lenient().when(base_rep.findByDestinationLike("moscow", null)).
                thenReturn(new PageImpl<>(Arrays.asList(fl)));
        lenient().when(info_rep.findAllExpNotes(1L, null)).
                thenReturn(new PageImpl<>(Arrays.asList(
                        new FlightInfoEntity("ad", 1, 1, LocalDateTime.now(), fl))));
        MockMultipartFile file
                = new MockMultipartFile(
                "file",
                "test.txt",
                MediaType.TEXT_PLAIN_VALUE,
                "Multipart file test".getBytes()
        );
        Mockito.doNothing().when(fileConverter).createFileLocal(file);
        boolean added = service.saveFlight(fl.getDeparture(), fl.getDestination(), file);
        assertFalse(added);
    }
}