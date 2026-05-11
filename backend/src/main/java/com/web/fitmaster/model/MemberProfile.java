package com.web.fitmaster.model;

import com.web.fitmaster.model.enums.*;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "member_profiles")
public class MemberProfile {

    @Id
    @Column(name = "member_id")
    private Long memberId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "member_id")
    private User member;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FitnessGoal goal;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FitnessLevel fitnessLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SplitType splitType;


    @ElementCollection//منستخدمها مشان ننشئ تيبل منفصل
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "member_injuries", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "injury_type")
    private List< InjuryType> injuries;

    @Enumerated(EnumType.STRING)
    private TrainingStyle trainingStyle;

//التغدية
    private Double weight;
    private Double height;
    private Integer age;
    private boolean hasDiabetes;
    private boolean hasHeartConditions;
    private boolean hasHypertension;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "member_allergies", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "allergy_type")
    private List<AllergyType> allergies;



}
