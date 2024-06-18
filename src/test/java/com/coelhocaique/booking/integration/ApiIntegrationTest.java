package com.coelhocaique.booking.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static com.coelhocaique.booking.exception.ValidationException.BOOKING_WITH_SPECIFIED_ID_NOT_FOUND;
import static com.coelhocaique.booking.exception.ValidationException.PROPERTY_IS_ALREADY_BOOKED_FOR_THE_PERIOD_REQUESTED;
import static com.coelhocaique.booking.exception.ValidationException.PROPERTY_IS_BLOCKED_FOR_THIS_PERIOD;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void bookingIntegrationTest() throws Exception {
        String bookingJson = "{" +
                "  \"end_date\": \"2024-06-18\"," +
                "  \"guest_info\": \"Caique Coelho\"," +
                "  \"property_id\": 1," +
                "  \"start_date\": \"2024-06-17\"" +
                "}";

        String updateBookingJson = "{" +
                "  \"end_date\": \"2024-06-19\"," +
                "  \"guest_info\": \"Coelho Caique\"," +
                "  \"property_id\": 1," +
                "  \"start_date\": \"2024-06-17\"" +
                "}";

        String propertyBlockJson = "{" +
                "  \"end_date\": \"2024-06-19\"," +
                "  \"reason\": \"Painting\"," +
                "  \"start_date\": \"2024-06-15\"" +
                "}";

        // create booking
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.property_id", is(1)))
                .andExpect(jsonPath("$.start_date", equalTo("2024-06-17")))
                .andExpect(jsonPath("$.end_date", equalTo("2024-06-18")))
                .andExpect(jsonPath("$.guest_info", equalTo("Caique Coelho")));


        // find booking to make sure it was created
        mockMvc.perform(get("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.property_id", is(1)))
                .andExpect(jsonPath("$.start_date", equalTo("2024-06-17")))
                .andExpect(jsonPath("$.end_date", equalTo("2024-06-18")))
                .andExpect(jsonPath("$.guest_info", equalTo("Caique Coelho")));

        // try to create booking again, but should get 409
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson))
                .andExpect(status().is(409))
                .andExpect(jsonPath("$.errors.[0]", equalTo(PROPERTY_IS_ALREADY_BOOKED_FOR_THE_PERIOD_REQUESTED)));

        // update booking guest info
        mockMvc.perform(put("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateBookingJson))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.property_id", is(1)))
                .andExpect(jsonPath("$.start_date", equalTo("2024-06-17")))
                .andExpect(jsonPath("$.end_date", equalTo("2024-06-19")))
                .andExpect(jsonPath("$.guest_info", equalTo("Coelho Caique")));

        // try to cancel nonexistent booking
        mockMvc.perform(patch("/bookings/3/cancel")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.errors.[0]", equalTo(BOOKING_WITH_SPECIFIED_ID_NOT_FOUND)));

        // try to delete nonexistent booking
        mockMvc.perform(delete("/bookings/3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.errors.[0]", equalTo(BOOKING_WITH_SPECIFIED_ID_NOT_FOUND)));

        // cancel booking
        mockMvc.perform(patch("/bookings/1/cancel")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.status", is("CANCELED")))
                .andExpect(jsonPath("$.id", is(1)));

        // rebook booking
        mockMvc.perform(patch("/bookings/1/rebook")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.status", is("REBOOKED")))
                .andExpect(jsonPath("$.id", is(1)));

        // delete booking
        mockMvc.perform(delete("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(204));

        // try to get booking but it doesn't exist anymore
        mockMvc.perform(get("/bookings/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(404))
                .andExpect(jsonPath("$.errors.[0]", equalTo(BOOKING_WITH_SPECIFIED_ID_NOT_FOUND)));

        // block a property
        mockMvc.perform(post("/properties/1/blocks")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(propertyBlockJson))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.property_id", is(1)))
                .andExpect(jsonPath("$.start_date", equalTo("2024-06-15")))
                .andExpect(jsonPath("$.end_date", equalTo("2024-06-19")))
                .andExpect(jsonPath("$.reason", equalTo("Painting")));

        // check if block exists
        mockMvc.perform(get("/properties/1/blocks")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].property_id", is(1)))
                .andExpect(jsonPath("$.[0].start_date", equalTo("2024-06-15")))
                .andExpect(jsonPath("$.[0].end_date", equalTo("2024-06-19")))
                .andExpect(jsonPath("$.[0].reason", equalTo("Painting")));

        // try to recreate booking but should fail
        mockMvc.perform(post("/bookings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookingJson))
                .andExpect(status().is(409))
                .andExpect(jsonPath("$.errors.[0]", equalTo(PROPERTY_IS_BLOCKED_FOR_THIS_PERIOD)));

        // delete block
        mockMvc.perform(delete("/properties/1/blocks/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(204));
    }

    @Test
    void propertiesIntegrationTests() throws Exception {
        mockMvc.perform(get("/properties/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.[0].id", is(1)))
                .andExpect(jsonPath("$.[0].name", equalTo("Burj Khalifa")))
                .andExpect(jsonPath("$.[0].address", equalTo("1 Sheikh Mohammed bin Rashid Blvd - Downtown Dubai - Dubai - United Arab Emirates")))
                .andExpect(jsonPath("$.[1].id", is(2)))
                .andExpect(jsonPath("$.[1].name", equalTo("Empire State Building")))
                .andExpect(jsonPath("$.[1].address", equalTo("20 W 34th St., New York, NY 10001, United States")))
                .andExpect(jsonPath("$.[2].id", is(3)))
                .andExpect(jsonPath("$.[2].name", equalTo("CN Tower")))
                .andExpect(jsonPath("$.[2].address", equalTo("290 Bremner Blvd, Toronto, ON M5V 3L9, Canada")));
    }
}
