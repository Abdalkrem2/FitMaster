package com.web.fitmaster.dto;

import com.web.fitmaster.model.Membership;
import com.web.fitmaster.model.Role;
import com.web.fitmaster.model.enums.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public class MemberDTOs {


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class  MemberRequest {
        @NotBlank
        @NotNull
        @Size(max = 15)
        private String phone;
        @NotBlank @NotNull @Size(max = 50)
        private String fullName;
        @NotBlank @NotNull
        private String gender;

        private String profilePicture;


    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class  MemberUpdateRequest {
        @Size(max = 15)
        private String phone;

        @Size(max = 50)
        private String fullName;

        private String profilePicture;

    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemberDTO {
        private Long id;
        private String phone;
        private String fullName;
        private String gender;
        private LocalDate endDate;
        private BigDecimal debt;
//        private MemberStatus status;
        private String addedByName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemberDetailsDTO {
        private Long id;
        private String phone;
        private String fullName;
        private String gender;
        private LocalDate startDate;
        private LocalDate endDate;
        private BigDecimal debt;
        private String profilePicture;
        private List<MembershipDTOs.MembershipHistory> memberships;
        private String addedByName;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemberResponse {
        private List<MemberDTOs.MemberDTO> content;
        private Integer pageNumber;
        private Integer pageSize;
        private Long totalElements;
        private Integer totalPages;
        private Boolean LastPage;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemberProfileDTO {
        private FitnessGoal goal;
        private FitnessLevel fitnessLevel;
        private SplitType splitType;
        private List<InjuryType> injuries;
        private Double weight;
        private Double height;
        private Integer age;
        private TrainingStyle trainingStyle;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemberProfileRequest {
        @NotNull
        private FitnessGoal goal;

        @NotNull
        private FitnessLevel fitnessLevel;

        @NotNull
        private SplitType splitType;

        private List<InjuryType> injuries;

        private Double weight;
        private Double height;
        private Integer age;

        private TrainingStyle trainingStyle;
    }
}
