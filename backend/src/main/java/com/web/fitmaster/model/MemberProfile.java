package com.web.fitmaster.model;

import com.web.fitmaster.model.enums.FitnessGoal;
import com.web.fitmaster.model.enums.FitnessLevel;
import com.web.fitmaster.model.enums.InjuryType;
import com.web.fitmaster.model.enums.TrainingStyle;
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

    @Column(name = "days_per_week", nullable = false)
    private Integer daysPerWeek;

    @ElementCollection//منستخدمها مشان ننشئ تيبل منفصل
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "member_injuries", joinColumns = @JoinColumn(name = "member_id"))
    @Column(name = "injury_type")
    private List< InjuryType> injuries;

    private Double weight;
    private Double height;
    private Integer age;

    @Enumerated(EnumType.STRING)
    private TrainingStyle trainingStyle;








}
