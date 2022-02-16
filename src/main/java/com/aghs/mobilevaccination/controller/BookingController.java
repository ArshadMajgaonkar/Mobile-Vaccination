package com.aghs.mobilevaccination.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookingController {


    @GetMapping("/user/book-slot")
    public String getSlotBookingPage() {
        return "book-slot";
    }

}
