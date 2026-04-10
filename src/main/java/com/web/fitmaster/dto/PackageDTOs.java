package com.web.fitmaster.dto;

import com.web.fitmaster.model.enums.PackageStatus;
import lombok.*;

import java.math.BigDecimal;

public class PackageDTOs {


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
    public static class PackageDTO {
        private Long id;
        private String name;
        private String description;
        private BigDecimal price;
        private Integer durationDays;
        private PackageStatus status;

    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class CreatePackageRequest {
        private String name;
        private String description;
        private BigDecimal price;
        private Integer durationDays;
        private PackageStatus status;

    }


}
