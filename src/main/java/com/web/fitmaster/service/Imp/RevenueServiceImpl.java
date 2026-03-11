package com.web.fitmaster.service.Imp;

import com.web.fitmaster.model.Revenue;
import com.web.fitmaster.model.User;
import com.web.fitmaster.repository.RevenueRepository;
import com.web.fitmaster.repository.UserRepository;
import com.web.fitmaster.dto.RevenueDTOs;
import com.web.fitmaster.exceptions.APIException;
import com.web.fitmaster.service.RevenueService;import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RevenueServiceImpl implements RevenueService {

    private final RevenueRepository revenueRepository;
    private final UserRepository userRepository;

    // CREATE
    @Override
    public RevenueDTOs.RevenueDTO createRevenue(RevenueDTOs.RevenueRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new APIException("User not found"));

        Revenue revenue = new Revenue();
        revenue.setUser(user);
        revenue.setAmount(request.getAmount());
        revenue.setDate(request.getDate());
        revenue.setRevenueType(request.getRevenueType());
        revenue.setPaymentMethod(request.getPaymentMethod());
        revenue.setDescription(request.getDescription());

        Revenue saved = revenueRepository.save(revenue);
        return mapToDTO(saved);
    }

    // GET ALL WITH PAGINATION
    @Override
    public RevenueDTOs.RevenueResponse getRevenues(Pageable pageable) {
        Page<Revenue> page = revenueRepository.findAll(pageable);
        List<RevenueDTOs.RevenueDTO> content = page.getContent()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());

        return RevenueDTOs.RevenueResponse.builder()
                .content(content)
                .pageNumber(page.getNumber())
                .pageSize(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .lastPage(page.isLast())
                .build();
    }

    // GET BY ID
    @Override
    public RevenueDTOs.RevenueDTO getRevenue(Long id) {
        Revenue revenue = revenueRepository.findById(id)
                .orElseThrow(() -> new APIException("Revenue not found with id: " + id));
        return mapToDTO(revenue);
    }

    // UPDATE
    @Override
    public RevenueDTOs.RevenueDTO updateRevenue(Long id, RevenueDTOs.RevenueUpdateRequest request) {
        Revenue revenue = revenueRepository.findById(id)
                .orElseThrow(() -> new APIException("Revenue not found with id: " + id));

        if(request.getAmount() != null) revenue.setAmount(request.getAmount());
        if(request.getDate() != null) revenue.setDate(request.getDate());
        if(request.getRevenueType() != null) revenue.setRevenueType(request.getRevenueType());
        if(request.getPaymentMethod() != null) revenue.setPaymentMethod(request.getPaymentMethod());
        if(request.getDescription() != null) revenue.setDescription(request.getDescription());

        Revenue updated = revenueRepository.save(revenue);
        return mapToDTO(updated);
    }

    // DELETE
    @Override
    public String deleteRevenue(Long id) {
        Revenue revenue = revenueRepository.findById(id)
                .orElseThrow(() -> new APIException("Revenue not found with id: " + id));
        revenueRepository.delete(revenue);
        return "Revenue deleted successfully";
    }

    // GET BY DATE RANGE
    @Override
    public List<RevenueDTOs.RevenueDTO> getRevenueByDateRange(LocalDate start, LocalDate end) {
        List<Revenue> revenues = revenueRepository.findByDateBetween(start, end);
        return revenues.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    // Mapping Entity -> DTO
    private RevenueDTOs.RevenueDTO mapToDTO(Revenue revenue) {
        return RevenueDTOs.RevenueDTO.builder()
                .id(revenue.getId())
                .userId(revenue.getUser().getId())
                .amount(revenue.getAmount())
                .date(revenue.getDate())
                .revenueType(revenue.getRevenueType())
                .paymentMethod(revenue.getPaymentMethod())
                .description(revenue.getDescription())
                .build();
    }
}
