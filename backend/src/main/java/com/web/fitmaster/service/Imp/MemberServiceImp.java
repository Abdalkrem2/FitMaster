package com.web.fitmaster.service.Imp;

import com.web.fitmaster.dto.MemberDTOs;
import com.web.fitmaster.dto.MembershipDTOs;
import com.web.fitmaster.event.ActivityEvent;
import com.web.fitmaster.exceptions.BadRequestException;
import com.web.fitmaster.exceptions.NotFoundException;
import com.web.fitmaster.model.*;
import com.web.fitmaster.model.Package;
import com.web.fitmaster.model.enums.*;
import com.web.fitmaster.repository.*;
import com.web.fitmaster.service.MemberService;
import com.web.fitmaster.util.AuthUtil;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MemberServiceImp implements MemberService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final MembershipRepository membershipRepository;
    private final RevenueRepository revenueRepository;
    private final AuthUtil authUtil;
    private final PackageRepository packageRepository;
    private final ApplicationEventPublisher eventPublisher;


    @Override
    public MemberDTOs.MemberResponse getAllMembers(Pageable pageable,String search) {
        Page<User> members;
        if(search !=null && !search.isBlank()){
           members=userRepository.searchMembers(Set.of(AppRole.MEMBER),search,pageable);
        }
        else{
         members =userRepository.findByRoles_roleNameInAndDeletedFalse(Set.of(AppRole.MEMBER), pageable);
        }
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
    public MemberDTOs.MemberDetailsDTO getMemberDetails(Long id) {
    User member = userRepository.findByIdAndRoles_RoleNameInAndDeletedFalse(id,Set.of(AppRole.MEMBER)).orElseThrow(()->new NotFoundException(String.format( "Member with id '%s' not found",id)));

    List<Membership> memberships=membershipRepository.findMembershipByMemberIdOrderByEndDateDesc(id);





        new MemberDTOs.MemberDetailsDTO();
        return MemberDTOs.MemberDetailsDTO.builder()
                .id(member.getId())
                .phone(member.getPhone())
                .gender(member.getGender())
                .fullName(member.getFullName())
                .addedByName(authUtil.loggedInUser().getFullName())
                .debt(calculateDebt(id))
                .endDate(calculateEndDate(id))
                .startDate(calculateStartDate(id))
                .profilePicture(member.getProfilePicture())
                .memberships(memberships.stream().map(this::mapMembershipToDTO).collect(Collectors.toList()))
                .build();

    }

    @Override
    @Transactional
    public MemberDTOs.MemberDTO createMember(MemberDTOs.MemberRequest memberDTO) {
        if(userRepository.existsByPhoneAndDeletedFalse(memberDTO.getPhone())) {
            throw new BadRequestException(String.format( "Phone '%s' is already registered",memberDTO.getPhone()));
        }
        User user = new User();
        user.setPhone(memberDTO.getPhone());
        user.setCreatedBy(authUtil.loggedInUser());
//        user.setPassword(passwordEncoder.encode("abdalkremn@123"));
        //the member will use his phone number as password until the first login
        user.setPassword(passwordEncoder.encode(memberDTO.getPhone()));
        user.setGender(memberDTO.getGender());
        user.setFullName(memberDTO.getFullName());
        user.setProfilePicture(memberDTO.getProfilePicture());
        Role role = roleRepository.findByRoleName(AppRole.MEMBER)
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.getRoles().add(role);
            User savedUser = userRepository.save(user);


        eventPublisher.publishEvent(ActivityEvent.builder()
                .action(ActionType.CREATE)
                .performedBy(authUtil.loggedInUser())
                .entityType(EntityType.MEMBER)
                .details("Created member: " + savedUser.getFullName())
                .entityId(savedUser.getId())
                .build()
        );

        return mapToDTO(savedUser);
    }

    @Override
    @Transactional
    public MemberDTOs.MemberDTO updateMember(MemberDTOs.@Valid MemberUpdateRequest memberDTO, Long id) {
        User user= userRepository.findByIdAndRoles_RoleNameInAndDeletedFalse(id,Set.of(AppRole.MEMBER)).orElseThrow(()->new NotFoundException(String.format( "Member with id '%s' not found",id)));


            if (memberDTO.getPhone() != null&& !memberDTO.getPhone().isBlank()) {
                user.setPhone(memberDTO.getPhone());
            }
            if (memberDTO.getFullName() != null && !memberDTO.getFullName().isBlank()) {
                user.setFullName(memberDTO.getFullName());
            }
            if (memberDTO.getProfilePicture() != null) {
                user.setProfilePicture(memberDTO.getProfilePicture());
            }

        eventPublisher.publishEvent(ActivityEvent.builder()
                .action(ActionType.UPDATE)
                .performedBy(authUtil.loggedInUser())
                .entityType(EntityType.MEMBER)
                .details("Updated member: " + user.getFullName())
                .entityId(user.getId())
                .build()
        );



            return mapToDTO(user);




    }

    @Override
    @Transactional
    public String deleteMember(Long id) {
        User member =userRepository.findByIdAndRoles_RoleNameInAndDeletedFalse(id,Set.of(AppRole.MEMBER))
                .orElseThrow(()->new NotFoundException(String.format( "Member with id '%s' not found",id)));
        member.setDeleted(true);
        userRepository.save(member);
        eventPublisher.publishEvent(ActivityEvent.builder()
                .action(ActionType.DELETE)
                .performedBy(authUtil.loggedInUser())
                .entityType(EntityType.MEMBER)
                .details("Deleted member: " + member.getFullName())
                .entityId(member.getId())
                .build()
        );

        return "Member with id '"+id+"' deleted";
    }

    @Override
    @Transactional
    public void addMembership(Long id, MembershipDTOs.MembershipRequest request) {
        User member= userRepository.findByIdAndRoles_RoleNameInAndDeletedFalse(id,Set.of( AppRole.MEMBER)).orElseThrow(()->new NotFoundException(String.format( "Member with id '%s' not found",id)));

        Package pkg=packageRepository.findById(request.getPackageId()).orElseThrow(()->new NotFoundException(String.format( "Package with id '%s' not found",request.getPackageId())));

        Optional<Membership> activeMembership = membershipRepository
                .findTopByMemberIdOrderByEndDateDesc(id)
                .filter(m -> m.getEndDate().isAfter(LocalDate.now()));

        LocalDate startDate;
        if(request.getStartDate()!=null){
            startDate=(request.getStartDate());
        }else if(activeMembership.isPresent()){
            startDate = activeMembership.get().getEndDate().plusDays(1);
        }else {
            startDate=LocalDate.now();
        }
        BigDecimal debt=pkg.getPrice().subtract(request.getPrice());
        Membership membership = new Membership().builder()
      .startDate(startDate)
           .price(request.getPrice())
           .pkg(pkg)
                .endDate(startDate.plusDays(pkg.getDurationInDays()))
                        .description(request.getDescription())
                                .debt(debt.compareTo(BigDecimal.ZERO)<0?BigDecimal.ZERO:debt)
                                        .member(member)
                .status(MembershipStatus.ACTIVE)
                .build();

        Revenue revenue=new Revenue().builder()
                .membership(membership)
                .member(member)
                .createdBy(authUtil.loggedInUser())
                .amount(request.getPrice())
                .description(request.getDescription())
                .build();




        membershipRepository.save(membership);
        revenueRepository.save(revenue);

        member.setIsActivated(true);
        userRepository.save(member);

        eventPublisher.publishEvent(ActivityEvent.builder()
                .action(ActionType.ADD)
                .performedBy(authUtil.loggedInUser())
                .entityType(EntityType.MEMBERSHIP)
                .details("Added membership to : " + member.getFullName())
                .entityId(member.getId())
                .build()
        );

    }

    @Override
    public List<MembershipDTOs.MembershipHistory> getMemberships(Long id) {
        User member= userRepository.findByIdAndRoles_RoleNameInAndDeletedFalse(id,Set.of( AppRole.MEMBER)).orElseThrow(()->new NotFoundException(String.format( "Member with id '%s' not found",id)));
        List<Membership> memberships= membershipRepository.findAllByMemberId(id);
      List<  MembershipDTOs.MembershipHistory> dto =memberships.stream().map(this::mapMembershipToDTO).toList();
        return dto;
    }

    @Override
    public long countActiveMembers() {
        return membershipRepository
                .countDistinctMemberByEndDateAfter(LocalDate.now());
    }

    @Override
    public long countExpiringSoon() {
        LocalDate today = LocalDate.now();
        LocalDate threeDaysLater = today.plusDays(3);
        return membershipRepository
                .countDistinctMemberByEndDateBetween(today, threeDaysLater);
    }

    public MemberDTOs.MemberDTO mapToDTO(User member) {
        MemberDTOs.MemberDTO memberDTO = new MemberDTOs.MemberDTO();
        memberDTO.setId(member.getId());
        memberDTO.setPhone(member.getPhone());
        memberDTO.setFullName(member.getFullName());
        memberDTO.setGender(member.getGender());
        memberDTO.setDebt(calculateDebt(member.getId()));
        memberDTO.setEndDate(calculateEndDate(member.getId()));
        memberDTO.setAddedByName(member.getCreatedBy().getFullName());
        return memberDTO;
    }


    public MembershipDTOs.MembershipHistory mapMembershipToDTO(Membership data) {
        MembershipDTOs.MembershipHistory dto = new MembershipDTOs.MembershipHistory().builder()
                .id(data.getMembershipId())
                .packageName(data.getPkg().getName())
                .price(data.getPrice())
                .timestamp(data.getCreatedAt())
                .debt(data.getDebt())
                .description(data.getDescription()).build();
        return dto;
    }
    public LocalDate calculateEndDate(Long memberId) {
        List<Membership> memberships=membershipRepository.findMembershipByMemberIdOrderByEndDateDesc(memberId);
        if(memberships.isEmpty()){
            return null;
        }
        long totalDays = memberships.stream()
                .map(Membership::getEndDate)
                .filter(endDate -> endDate.isAfter(LocalDate.now()))
                .mapToLong(endDate -> ChronoUnit.DAYS.between(LocalDate.now(), endDate))
                .sum();
        return LocalDate.now().plusDays(totalDays);
    }


    public BigDecimal calculateDebt(Long memberId) {
        BigDecimal cost= membershipRepository.sumPackagePriceByMemberId(memberId);
        if(cost==null) {
            cost=BigDecimal.ZERO;
        }


        BigDecimal totalPaid= revenueRepository.sumAmountByMemberId(memberId);
        if(totalPaid==null) {
            totalPaid=BigDecimal.ZERO;
        }
            BigDecimal debt= cost.subtract(totalPaid);
        return debt.compareTo(BigDecimal.ZERO)<0?BigDecimal.ZERO:debt;

    }

    public LocalDate calculateStartDate(Long memberId) {
        Optional<Membership> lastMembership=membershipRepository.findTopByMemberIdOrderByStartDateDesc(memberId);
        LocalDate startDate=null;
        if(lastMembership.isPresent()) {
            startDate= lastMembership.get().getStartDate();
        }
        return startDate;
    }

    @Override
    @Transactional
    public void changePassword(Long id, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Member not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
    }
}
