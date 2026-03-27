package com.web.fitmaster.dto;

import com.web.fitmaster.model.enums.PaymentMethod;
import com.web.fitmaster.model.enums.RevenueType;
import jakarta.validation.constraints.*;
import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class RevenueDTOs {

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class statsResponse {
    private BigDecimal today;
    private BigDecimal thisMonth;
    private BigDecimal thisYear;
    private BigDecimal debt;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class monthlyResponse {
        private BigDecimal yearTotal;
        private Map<Integer, BigDecimal>months;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class periodResponse {
        private BigDecimal periodTotal;
        private List<RevenueRow>revenues;
    }
    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RevenueRow {
        private Long id;
        private String addedByName;
        private String memberName;
        private BigDecimal debt;
        private String description;
        private BigDecimal amount;
        private LocalDate createdAt;
    }

}
