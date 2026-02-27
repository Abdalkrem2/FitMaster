package com.web.fitmaster.model;

import com.web.fitmaster.model.enums.PackageStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "packages")
public class Package {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long packageId;

    private String name;

    private int durationInDays;

    private double price;

    private String description;

    @Enumerated(EnumType.STRING)
    private PackageStatus status;



}