package com.web.fitmaster.dto;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;


public class MemberDTOs {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberRequest {
        @NotNull(message = "UserId is required")
        private Long userId;
        @NotNull(message = "name is required")
        private String name;
        @NotNull(message = "phone is required")
        private String phone;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberUpdate {
        private Long id;
        private String name;
        private String phone;
    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberDTO {
        private Long id;
        private String name;
        private String phone;
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
        private Boolean lastPage;
    }
}

