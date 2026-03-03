package com.web.fitmaster.dto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


public class MemberDTOs {

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberRequest {
        private Long id;
        private String name;
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
}

