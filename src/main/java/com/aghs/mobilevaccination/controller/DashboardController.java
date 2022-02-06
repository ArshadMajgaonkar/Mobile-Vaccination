package com.aghs.mobilevaccination.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    @GetMapping("/staff/dashboard")
    public String staffDashboard() {
        return "staff-dashboard";
    }

    @GetMapping("/user/dashboard")
    public String userDashboard() {
        return "user-dashboard1";
    }

    @GetMapping("/staff/trail")
    public String trail() {
        return "staff-dashboard";
    }


}
