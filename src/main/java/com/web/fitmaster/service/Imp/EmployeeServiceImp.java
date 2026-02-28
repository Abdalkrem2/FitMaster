package com.web.fitmaster.service.Imp;

import com.web.fitmaster.dto.EmployeeDTOs;
import com.web.fitmaster.exceptions.APIException;
import com.web.fitmaster.model.Role;
import com.web.fitmaster.model.User;
import com.web.fitmaster.model.enums.AppRole;
import com.web.fitmaster.repository.RoleRepository;
import com.web.fitmaster.repository.UserRepository;
import com.web.fitmaster.service.EmployeeService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.internal.bytebuddy.implementation.bytecode.Throw;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImp implements EmployeeService {
 private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    @Override
    public EmployeeDTOs.EmployeeResponse getEmployees(Pageable pageable) {
        Page<User>content=userRepository.findAll(pageable);

        List<EmployeeDTOs.EmployeeDTO> dto = content.stream().map(this::mapToDTO).toList();

        return EmployeeDTOs.EmployeeResponse.builder()
                .content(dto)
                .pageNumber(content.getNumber())
                .pageSize(content.getSize())
                .totalPages(content.getTotalPages())
                .totalElements(content.getTotalElements())
                .LastPage(content.isLast())
                .build();
    }

    @Override
    @Transactional
    public EmployeeDTOs.EmployeeDTO createEmployee(EmployeeDTOs.EmployeeRequest req) {
       if(userRepository.existsByPhone(req.getPhone()))
           throw new APIException("Phone number already exists");

       User user = new User();
       user.setPhone(req.getPhone());
       user.setGender(req.getGender());
       user.setPassword(passwordEncoder.encode(req.getPassword()) );
       user.setFullName(req.getFullName());
       user.setIsActivated(req.getIsActivated());

        Role employeeRole = roleRepository.findByRoleName(AppRole.EMPLOYEE)
                .orElseThrow(() -> new APIException("EMPLOYEE not found"));

        Role adminRole = roleRepository.findByRoleName(AppRole.ADMIN)
                .orElseThrow(() -> new APIException("ADMIN not found"));


       if(req.getRole()==null||req.getRole()==AppRole.EMPLOYEE)
           user.setRoles(Set.of(employeeRole));
       else
           user.setRoles(Set.of(adminRole));


       userRepository.save(user);


        return mapToDTO(user);
    }

    @Override
    @Transactional
    public EmployeeDTOs.EmployeeDTO updateEmployee(Long id, EmployeeDTOs. EmployeeUpdateRequest req) {
        User user = userRepository.findById(id).orElseThrow(() -> new APIException("User not found"));
        if (req.getFullName() != null && !req.getFullName().isBlank()) {
            user.setFullName(req.getFullName());
        }

        if (req.getIsActivated() != null) {
            user.setIsActivated(req.getIsActivated());
        }

        if (req.getPhone() != null && !req.getPhone().isBlank()) {
            user.setPhone(req.getPhone());
        }

        if (req.getPassword() != null && !req.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(req.getPassword()));
        }


        if(req.getRole()!=null) {
            Role employeeRole = roleRepository.findByRoleName(AppRole.EMPLOYEE)
                    .orElseThrow(() -> new APIException("EMPLOYEE not found"));

            Role adminRole = roleRepository.findByRoleName(AppRole.ADMIN)
                    .orElseThrow(() -> new APIException("ADMIN not found"));

            user.getRoles().clear();

            if (req.getRole() == AppRole.ADMIN) {
                user.getRoles().add(adminRole);
            } else {
                user.getRoles().add(employeeRole);

            }
        }

        return mapToDTO(user);
    }

    @Override
    public String deleteEmployee(Long id) {

        if(!userRepository.existsById(id))
            throw new APIException("User not found");
        userRepository.deleteById(id);

        return "Employee with id= "+id+" was deleted successfully";
    }

    @Override
    public EmployeeDTOs.EmployeeDTO getEmployee(Long id) {
        User user = userRepository
                .findByUserIdAndRoles_RoleNameIn(
                        id,
                        Set.of(AppRole.EMPLOYEE, AppRole.ADMIN)
                )
                .orElseThrow(() -> new APIException("Staff Member not found"));

        return mapToDTO(user);
    }

    public  EmployeeDTOs.EmployeeDTO mapToDTO (User user) {
        return EmployeeDTOs.EmployeeDTO.builder()
                .phone(user.getPhone())
                .gender(user.getGender())
                .id(user.getId())
                .fullName(user.getFullName())
                .roles(user.getRoles())
                .isActivated(user.getIsActivated())
                .build();

    }

}
