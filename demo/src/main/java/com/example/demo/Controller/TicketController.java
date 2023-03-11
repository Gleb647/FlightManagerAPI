package com.example.demo.Controller;

import com.example.demo.Model.Ticket;
import com.example.demo.Model.User;
import com.example.demo.Service.TicketService;
import com.example.demo.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("*")
@RestController
public class TicketController {

    @Autowired
    public TicketService ticket_service;

    @GetMapping("/buy-ticket/{id}")
    public void buyTicket(@PathVariable("id") Long id, @RequestParam String username){
        ticket_service.buyTicket(id, username);
    }
}
