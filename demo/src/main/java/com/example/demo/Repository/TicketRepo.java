package com.example.demo.Repository;

import com.example.demo.Model.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TicketRepo extends JpaRepository<Ticket, Long> {

}
