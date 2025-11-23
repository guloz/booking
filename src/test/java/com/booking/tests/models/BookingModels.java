package com.booking.tests.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BookingModels {

    public static class Booking {
        @JsonProperty("firstname")
        public String firstName;

        @JsonProperty("lastname")
        public String lastName;

        @JsonProperty("totalprice")
        public int totalPrice;

        @JsonProperty("depositpaid")
        public boolean depositPaid;

        public String additionalNeeds;

        @JsonProperty("bookingdates")
        public BookingDates bookingDates;
    }

    public static class BookingDates {
        public String checkin;
        public String checkout;
    }

    public static class BookingResponse {
        public int bookingId;
        public Booking booking;
    }

    public static class AuthRequest {
        public String username;
        public String password;
    }

    public static class AuthResponse {
        public String token;
        public String reason;
    }

}
