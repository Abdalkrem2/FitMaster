package com.web.fitmaster.service.Imp;

import com.web.fitmaster.dto.MemberDTOs;
import com.web.fitmaster.exceptions.BadRequestException;
import com.web.fitmaster.exceptions.NotFoundException;
import com.web.fitmaster.model.Role;
import com.web.fitmaster.model.User;
import com.web.fitmaster.model.enums.AppRole;
import com.web.fitmaster.repository.RoleRepository;
import com.web.fitmaster.repository.UserRepository;
import com.web.fitmaster.service.MemberService;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MemberServiceImp implements MemberService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    @Override
    public MemberDTOs.MemberResponse getAllMembers(Pageable pageable) {
        Page<User> members =userRepository.findByRoles_roleNameIn(Set.of(AppRole.MEMBER), pageable);

        List<MemberDTOs.MemberDTO> content= members.stream().map(this::mapToDTO).toList();

        MemberDTOs.MemberResponse memberResponse = new MemberDTOs.MemberResponse();
        memberResponse.setLastPage(members.isLast());
        memberResponse.setPageNumber(members.getNumber());
        memberResponse.setPageSize(members.getSize());
        memberResponse.setTotalPages(members.getTotalPages());
        memberResponse.setTotalElements(members.getTotalElements());
        memberResponse.setContent(content);

        return memberResponse;

    }

    @Override
    public MemberDTOs.MemberDTO getMember(Long id) {
    User member = userRepository.findByIdAndRoles_RoleNameIn(id,Set.of(AppRole.MEMBER)).orElseThrow(()->new NotFoundException(String.format( "Member with id '%s' not found",id)));
        return mapToDTO(member);

    }

    @Override
    @Transactional
    public MemberDTOs.MemberDTO createMember(MemberDTOs.MemberRequest memberDTO) {
        if(userRepository.existsByPhone(memberDTO.getPhone())) {
            throw new BadRequestException(String.format( "Phone '%s' is already registered",memberDTO.getPhone()));
        }
        User user = new User();
        user.setPhone(memberDTO.getPhone());
        user.setPassword(passwordEncoder.encode(memberDTO.getPassword()));
        user.setGender(memberDTO.getGender());
        user.setFullName(memberDTO.getFullName());
        user.setIsActivated(memberDTO.getIsActivated());
        user.setProfilePicture(memberDTO.getProfilePicture());
        Role role = roleRepository.findByRoleName(AppRole.MEMBER)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().add(role);

        return mapToDTO(userRepository.save(user));
    }

    @Override
    @Transactional
    public MemberDTOs.MemberDTO updateMember(MemberDTOs.@Valid MemberUpdateRequest memberDTO, Long id) {
        User user= userRepository.findByIdAndRoles_RoleNameIn(id,Set.of(AppRole.MEMBER)).orElseThrow(()->new NotFoundException(String.format( "Member with id '%s' not found",id)));


            if (memberDTO.getPhone() != null&& !memberDTO.getPhone().isBlank()) {
                user.setPhone(memberDTO.getPhone());
            }
            if (memberDTO.getFullName() != null && !memberDTO.getFullName().isBlank()) {
                user.setFullName(memberDTO.getFullName());
            }
            if (memberDTO.getProfilePicture() != null) {
                user.setProfilePicture(memberDTO.getProfilePicture());
            }
            if (memberDTO.getIsActivated() != null) {
                user.setIsActivated(memberDTO.getIsActivated());
            }
            return mapToDTO(user);




    }

    @Override
    @Transactional
    public String deleteMember(Long id) {
        User member =userRepository.findByIdAndRoles_RoleNameIn(id,Set.of(AppRole.MEMBER)).orElseThrow(()->new NotFoundException(String.format( "Member with id '%s' not found",id)));
        userRepository.delete(member);
        return "Member with id '"+id+"' deleted";
    }

    public MemberDTOs.MemberDTO mapToDTO(User member) {
        MemberDTOs.MemberDTO memberDTO = new MemberDTOs.MemberDTO();
        memberDTO.setId(member.getId());
        memberDTO.setPhone(member.getPhone());
        memberDTO.setRoles(member.getRoles());
        memberDTO.setFullName(member.getFullName());
        memberDTO.setProfilePicture(member.getProfilePicture());
        memberDTO.setGender(member.getGender());
        memberDTO.setIsActivated(member.getIsActivated());
        return memberDTO;
    }
}
