package com.web.fitmaster.service.Imp;

import com.web.fitmaster.dto.MonthlyRevenue;
import com.web.fitmaster.exceptions.NotFoundException;
import com.web.fitmaster.model.Membership;
import com.web.fitmaster.model.Revenue;
import com.web.fitmaster.model.User;
import com.web.fitmaster.repository.MembershipRepository;
import com.web.fitmaster.repository.PackageRepository;
import com.web.fitmaster.repository.RevenueRepository;
import com.web.fitmaster.repository.UserRepository;
import com.web.fitmaster.dto.RevenueDTOs;
import com.web.fitmaster.exceptions.APIException;
import com.web.fitmaster.service.RevenueService;import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RevenueServiceImpl implements RevenueService {

    private final RevenueRepository revenueRepository;
    private final UserRepository userRepository;
    private final MembershipRepository membershipRepository;
    private final PackageRepository packageRepository;


    @Override
    public RevenueDTOs.statsResponse getRevenueStats() {
    LocalDate today = LocalDate.now();
    LocalDate month=today.withDayOfMonth(1);//اول يوم بالشهر
    LocalDate year=today.withDayOfYear(1);//اول يوم بالسنة


        return RevenueDTOs.statsResponse.builder()
                .today(revenueRepository.sumByDate(today))
                .thisMonth(revenueRepository.sumBetweenDates(month,today))
                .thisYear(revenueRepository.sumBetweenDates(year,today))
                .debt(calculateTotalDebt())
                .build();
    }


    @Override
    public RevenueDTOs.monthlyResponse getRevenueMonthly(int year) {
        List<MonthlyRevenue>row=revenueRepository.monthlyBreakdown(year);

        Map<Integer,BigDecimal>months=new HashMap<>();
        for(int i=1;i<=12;i++){
           months.put(i,BigDecimal.ZERO);
        }

        BigDecimal yearTotal=BigDecimal.ZERO;
        for(MonthlyRevenue m:row){
            months.put(m.getMonth(),m.getTotal());
            yearTotal=yearTotal.add(m.getTotal());
        }


        return RevenueDTOs.monthlyResponse.builder().months(months).yearTotal(yearTotal).build();
    }

    @Override
    public RevenueDTOs.periodResponse getRevenueByPeriod(LocalDate start, LocalDate end, String gender) {
        List<Revenue> revenues;
        if (gender == null || "All".equals(gender)) {
            revenues = revenueRepository.findByCreatedAtBetweenOrderByCreatedAtDesc(start, end);
        } else {
            revenues = revenueRepository.findByCreatedAtBetweenAndMemberGenderOrderByCreatedAtDesc(start, end, gender);
        }

        BigDecimal periodTotal=revenues.stream().map(Revenue::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal periodDebt=revenues.stream().map(revenue -> revenue.getMembership().getDebt()).reduce(BigDecimal.ZERO, BigDecimal::add);

        List<RevenueDTOs.RevenueRow>rows=revenues.stream().map(



                r -> RevenueDTOs.RevenueRow.builder()
                        .id(r.getId())
                        .memberName(r.getMember().getFullName())
                        .addedByName(r.getCreatedBy() != null ? r.getCreatedBy().getFullName() : "Unknown")
                        .amount(r.getAmount())
                        .debt(r.getMembership().getDebt())
                        .pkg(r.getMembership().getPkg().getName())
                        .createdAt(r.getCreatedAt())
                        .description(r.getDescription())
                        .build()


        ).toList();


        return RevenueDTOs.periodResponse.builder().periodTotal(periodTotal)
                .periodDebt(periodDebt)
                .revenues(rows).build();
    }

    private BigDecimal calculateTotalDebt() {
        // Sum all membership prices - sum all revenue amounts
        BigDecimal totalCost = membershipRepository.sumAllPrices();
        BigDecimal totalPaid = revenueRepository.sumAllAmounts();
        if (totalCost == null) totalCost = BigDecimal.ZERO;
        if (totalPaid == null) totalPaid = BigDecimal.ZERO;
        return totalCost.subtract(totalPaid);
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

        return  cost.subtract(totalPaid);

    }

}
