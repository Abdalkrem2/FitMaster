package com.web.fitmaster.dto;

import com.web.fitmaster.model.Role;
import com.web.fitmaster.model.enums.AppRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
        @NotBlank @NotNull @Size(max = 50)
        private String password;

        private String profilePicture;
        private AppRole role;
        private Boolean isActivated = true;
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

        @Size(max = 50)
        private String password;

        private String profilePicture;

        private AppRole role;

        private Boolean isActivated = true;
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
        private Set<Role> roles;
        private Boolean isActivated = false;
        private String profilePicture;
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
}
