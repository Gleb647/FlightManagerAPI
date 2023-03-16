package com.example.demo.Model;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
public class Role {
    @Id
    @SequenceGenerator(name = "role_sequence", sequenceName = "role_sequence", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "role_sequence")
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "roleName", nullable = false, columnDefinition = "TEXT")
    private String name;

    public Role(Long id, String name) {
        this.id = id;
        this.name = name;
    }

}
