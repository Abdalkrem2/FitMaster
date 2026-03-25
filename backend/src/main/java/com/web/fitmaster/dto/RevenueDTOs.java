package com.web.fitmaster.dto;

import com.web.fitmaster.model.enums.PaymentMethod;
import com.web.fitmaster.model.enums.RevenueType;
import jakarta.validation.constraints.*;
import lombok.*;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class RevenueDTOs {

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RevenueRequest {
        @NotNull(message = "memberId is required")
        private Long memberId;

        @NotNull(message = "Amount is required")
        @Positive(message = "Amount must be positive")
        private BigDecimal amount;
//
//        @NotNull(message = "Date is required")
//        private LocalDate date;

//        @NotNull(message = "Revenue type is required")
//        private RevenueType revenueType;

//        @NotNull(message = "Payment method is required")
//        private PaymentMethod paymentMethod;

        private String description;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RevenueUpdateRequest {
        private BigDecimal amount;
//        private LocalDate date;
//        private RevenueType revenueType;
//        private PaymentMethod paymentMethod;
        private String description;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RevenueDTO {
        private Long id;
        private Long memberId;
        private BigDecimal amount;
//        private LocalDate date;
//        private RevenueType revenueType;
//        private PaymentMethod paymentMethod;
        private String description;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class RevenueResponse {
        private List<RevenueDTO> content;
        private Integer pageNumber;
        private Integer pageSize;
        private Long totalElements;
        private Integer totalPages;
        private Boolean lastPage;
    }
}
