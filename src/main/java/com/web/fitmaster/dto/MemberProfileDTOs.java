package com.web.fitmaster.dto;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

public class MemberProfileDTOs {


    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemberProfileRequest {

        @NotNull
        private Double weight;

        @NotNull
        private Double height;

        @NotNull
        private LocalDate dateOfBirth;

        @NotNull
        private Long userId;

        private String goalPriority;
        private int age;
        private double bmi;
    }
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemberProfileUpdateRequest {

        private Double weight;

        private Double height;

        private LocalDate dateOfBirth;

        private String goalPriority;
        private int age;
        private double bmi;
    }


    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemberProfileDTO {

        private Long id;
        private Double weight;
        private Double height;
        private LocalDate dateOfBirth;
        private Long userId;
        private String goalPriority;
        private int age;
        private double bmi;
    }


    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MemberProfileResponse {

        private List<MemberProfileDTO> content;
        private Integer pageNumber;
        private Integer pageSize;
        private Long totalElements;
        private Integer totalPages;
        private Boolean lastPage;
    }
}