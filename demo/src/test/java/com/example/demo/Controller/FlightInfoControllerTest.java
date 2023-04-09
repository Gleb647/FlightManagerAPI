package com.example.demo.Controller;

import com.example.demo.Model.FlightInfoEntity;
import com.example.demo.Service.FlightInfoServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import java.time.LocalDateTime;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ContextConfiguration(classes = {FlightInfoController.class, FlightInfoServiceImpl.class})
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
public class FlightInfoControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FlightInfoServiceImpl base_serv;

    @Test
    public void addNewFlightInfoTest() throws Exception {
        LocalDateTime time = LocalDateTime.of(2023, 10, 10, 10, 00);
        FlightInfoEntity info = new FlightInfoEntity(
                "belavia", 1, 100, time, null);
        when(base_serv.addFlightInfo(info, 1L)).thenReturn(info);
        mvc.perform(MockMvcRequestBuilders
                .post("/flightinfo/1")
                .content("""
                        {
                            "carrier":"belavia",
                            "flightDuration":"1",
                            "cost":"100",
                            "date": "2023-10-10 10:00"
                        }
                        """)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void addExistingFlightInfoTest() throws Exception {
        LocalDateTime time = LocalDateTime.of(2023, 10, 10, 10, 00);
        FlightInfoEntity info = new FlightInfoEntity("belavia", 1, 100, time, null);
        when(base_serv.addFlightInfo(info, 1L)).thenReturn(null);
        mvc.perform(MockMvcRequestBuilders
                        .post("/flightinfo/1")
                        .content("""
                        {
                            "carrier":"belavia",
                            "cost":"100",                          
                            "date": "2023-10-10 10:00",                           
                            "flightDuration":"1"
                        }
                        """)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void updateFlightInfoIfNotExistTest() throws Exception {
        LocalDateTime time = LocalDateTime.of(2023, 10, 10, 10, 00);
        FlightInfoEntity info = new FlightInfoEntity("belavia", 1, 100, time, null);
        when(base_serv.updateFlight(1L, info)).thenReturn(true);
        mvc.perform(MockMvcRequestBuilders
                .put("/flightinfo/change/1")
                        .content("""
                        {
                            "carrier":"belavia",
                            "cost":"100",                          
                            "date": "2023-10-10 10:00",                           
                            "flightDuration":"1"
                        }
                        """)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateFlightInfoIfExistTest() throws Exception {
        LocalDateTime time = LocalDateTime.of(2023, 10, 10, 10, 00);
        FlightInfoEntity info = new FlightInfoEntity("belavia", 1, 100, time, null);
        when(base_serv.updateFlight(1L, info)).thenReturn(false);
        mvc.perform(MockMvcRequestBuilders
                        .put("/flightinfo/change/1")
                        .content("""
                        {
                            "carrier":"belavia",
                            "cost":"100",                          
                            "date": "2023-10-10 10:00",                           
                            "flightDuration":"1"
                        }
                        """)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    public void getFlightInfoTest() throws Exception {
        LocalDateTime time = LocalDateTime.of(2023, 03, 18, 12, 00);
        Pageable paging = PageRequest.of(0, 3);
        when(base_serv.findAllExpNotes(1L, paging)).thenReturn(Arrays.asList(
                new FlightInfoEntity(1L,"belavia", 1, 100, time, null),
                new FlightInfoEntity(2L,"lufthansa", 3, 300, time, null)
        ));
        mvc.perform(MockMvcRequestBuilders
                .get("/flightinfo/get/1")
                        .param("pageNum", "0")
                        .param("pageSize", "3")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].carrier", is("belavia")))
                .andExpect(jsonPath("$.[0].flightDuration", is(1)))
                .andExpect(jsonPath("$.[0].cost", is(100)))
                .andExpect(jsonPath("$.[0].date", is("2023-03-18 12:00")))
                .andExpect(jsonPath("$.[1].carrier", is("lufthansa")))
                .andExpect(jsonPath("$.[1].flightDuration", is(3)))
                .andExpect(jsonPath("$.[1].cost", is(300)))
                .andExpect(jsonPath("$.[1].date", is("2023-03-18 12:00")))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void getFlightBetween() throws Exception {
        LocalDateTime time = LocalDateTime.of(2023, 03, 18, 12, 00);
        Pageable paging = PageRequest.of(0, 3);
        when(base_serv.findFlightInfoBetween(1L, "1", "9", paging)).thenReturn(
                Arrays.asList(
                        new FlightInfoEntity(1L,"belavia", 1, 100, time, null),
                        new FlightInfoEntity(2L,"lufthansa", 3, 300, time, null),
                        new FlightInfoEntity(3L,"turkish airlines", 9, 500, time, null)
                )
        );
        mvc.perform(MockMvcRequestBuilders
                        .get("/flightinfo/get-flight-info-between/1")
                        .param("min", "1")
                        .param("max", "9")
                        .param("pageNum", "0")
                        .param("pageSize", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$.[0].carrier", is("belavia")))
                .andExpect(jsonPath("$.[0].flightDuration", is(1)))
                .andExpect(jsonPath("$.[0].cost", is(100)))
                .andExpect(jsonPath("$.[0].date", is("2023-03-18 12:00")))
                .andExpect(jsonPath("$.[1].carrier", is("lufthansa")))
                .andExpect(jsonPath("$.[1].flightDuration", is(3)))
                .andExpect(jsonPath("$.[1].cost", is(300)))
                .andExpect(jsonPath("$.[1].date", is("2023-03-18 12:00")))
                .andExpect(jsonPath("$.[2].carrier", is("turkish airlines")))
                .andExpect(jsonPath("$.[2].flightDuration", is(9)))
                .andExpect(jsonPath("$.[2].cost", is(500)))
                .andExpect(jsonPath("$.[2].date", is("2023-03-18 12:00")))
                .andExpect(status().is2xxSuccessful());
    }
}
