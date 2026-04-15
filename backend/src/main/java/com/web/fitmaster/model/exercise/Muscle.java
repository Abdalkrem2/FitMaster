package com.web.fitmaster.model.exercise;

import com.web.fitmaster.model.enums.BodyRegion;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "muscles")
@Getter
@Setter
@NoArgsConstructor
public class Muscle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "body_region", nullable = false, length = 50)
    private BodyRegion bodyRegion;
}
