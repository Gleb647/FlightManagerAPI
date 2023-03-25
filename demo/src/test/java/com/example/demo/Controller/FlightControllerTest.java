package com.example.demo.Controller;

import com.example.demo.Model.Flight;
import com.example.demo.Service.FlightsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;



@ContextConfiguration(classes = { FlightController.class, FlightsService.class})
@WebMvcTest
@AutoConfigureMockMvc(addFilters = false)
class FlightControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private FlightsService base_serv;

    @Test
    void addNewFlight() throws Exception {
        Flight fl = new Flight("minsk", "krakow");
        when(base_serv.saveFlight(fl)).thenReturn(true);
        mvc.perform(MockMvcRequestBuilders
                        .post("/flights/add")
                        .content(("""
                        {
                            "departure": "minsk",
                            "destination": "krakow"
                        }
                        """))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    void addExistingFlight() throws Exception {
        Flight fl = new Flight("minsk", "krakow");
        when(base_serv.saveFlight(fl)).thenReturn(false);
        mvc.perform(MockMvcRequestBuilders
                        .post("/flights/add")
                        .content(("""
                        {
                            "departure": "minsk",
                            "destination": "krakow"
                        }
                        """))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isConflict());
    }

    @Test
    void getFlightsWithoutParams() throws Exception {
        Flight fl1 = new Flight("minsk", "istanbul");
        Flight fl2 = new Flight("dublin", "paris");
        when(base_serv.getFlights(null, null)).thenReturn(Arrays.asList(fl1, fl2));

        mvc.perform(MockMvcRequestBuilders
                        .get("/flights/get")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].departure", is(fl1.getDeparture())))
                .andExpect(jsonPath("$.[0].destination", is(fl1.getDestination())))
                .andExpect(jsonPath("$.[1].departure", is(fl2.getDeparture())))
                .andExpect(jsonPath("$.[1].destination", is(fl2.getDestination())))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void getFlightsWithParams() throws Exception{
        Flight fl1 = new Flight("minsk", "istanbul");
        when(base_serv.getFlights("minsk", null)).thenReturn(Arrays.asList(fl1));

        mvc.perform(MockMvcRequestBuilders
                        .get("/flights/get")
                        .param("departure", "minsk")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].departure", is(fl1.getDeparture())))
                .andExpect(jsonPath("$.[0].destination", is(fl1.getDestination())))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    void updateFlight() throws Exception {
        Flight fl1 = new Flight(1L,"minsk", "istanbul");
        when(base_serv.updateFlight(1L, fl1)).thenReturn(true);
        mvc.perform(MockMvcRequestBuilders
                        .put("/flights/change/1")
                        .content(asJsonString(fl1))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}