package com.web.fitmaster.dto;

import com.web.fitmaster.model.Role;
import com.web.fitmaster.model.User;
import com.web.fitmaster.model.enums.AppRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;
import java.util.Set;

public class EmployeeDTOs {


    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class  EmployeeRequest {
        @NotBlank @NotNull @Size(max = 15)
        private String phone;
        @NotBlank @NotNull @Size(max = 50)
        private String fullName;
        @NotBlank @NotNull
        private String gender;
        @NotBlank @NotNull @Size(max = 50)
        private String password;

        private AppRole role;

        private Boolean isActivated = true;
    }

    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class  EmployeeUpdateRequest {
        @Size(max = 15)
        private String phone;

       @Size(max = 50)
        private String fullName;

        @Size(max = 50)
        private String password;

        private AppRole role;

        private Boolean isActivated = true;
    }




    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class EmployeeDTO {
        private Long id;
        private String phone;
        private String fullName;
        private String gender;
        private Set<Role> roles;
        private Boolean isActivated = false;
    }




    @Setter
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
   public static class EmployeeResponse {
       private List<EmployeeDTO> content;
       private Integer pageNumber;
       private Integer pageSize;
       private Long totalElements;
       private Integer totalPages;
       private Boolean LastPage;
   }

}
