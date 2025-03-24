package com.app.pruebaApi.model;

import java.math.BigInteger;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_person")
public class Person {
    
    @Id
    @Column(unique = true, nullable = false, columnDefinition = "BIGINT")
    private BigInteger dni;
    
    @Column(name = "first_name", nullable = false, length = 45)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 45)
    private String lastName;

    @Column(nullable = false, columnDefinition = "DATE")
    private LocalDate birthdate;

    @Column(nullable = false, length = 20)
    private String phone;

    @Column(length = 45)
    private String address;

}
