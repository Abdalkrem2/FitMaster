package com.web.fitmaster.dto;

import com.web.fitmaster.model.Package;
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

public class MembershipDTOs {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MembershipRequest{
        @NotNull
        private Long packageId;

//        @NotNull
        private LocalDate startDate;

        @NotNull
        private BigDecimal price;

        private BigDecimal debt;

        private String description;
    }
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class MembershipHistory{
        private Long id;
        @NotNull
        private String packageName;

        @NotNull
        private LocalDateTime timestamp;

        @NotNull
        private BigDecimal price;

        private BigDecimal debt;

        private String description;
    }
}
