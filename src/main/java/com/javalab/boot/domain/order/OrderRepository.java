package com.javalab.boot.domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    Order findByUserId(int userId);

    /**
     * JPQL에서 시간 부분을 무시하고 날짜만으로 GROUP BY 하려면,
     * CAST 함수를 사용하여 시간 부분을 제거할 수 있습니다.
     * CAST 함수를 사용하여 LocalDateTime을 LocalDate로 변환하고,
     * 이를 기준으로 GROUP BY 하면 됩니다.
     * 또한 CAST 함수를 이용하여 String type의 startDate와 endDate를
     * LocalDateTime type인 createDate 와 비교할 수 있게 됩니다.
     */
    @Query("SELECT CAST(o.createDate AS java.time.LocalDate) as dateOnly, SUM(oi.price * oi.count) as totalAmount " +
            "FROM Order o " +
            "JOIN o.order_items oi " +
            "WHERE o.createDate >= CAST(:startDate AS java.time.LocalDateTime) " +
            "  AND o.createDate <= CAST(:endDate AS java.time.LocalDateTime) " +
            "GROUP BY dateOnly " +
            "ORDER BY dateOnly")
    List<Object[]> getTotalAmountByDateRange( @Param("startDate") String startDate,
                                              @Param("endDate") String endDate);

    @Query("SELECT SUM(o.price) FROM Order o " +
            "WHERE o.status = '환불 완료' " +
            "AND o.createDate >= CAST(:startDate AS java.time.LocalDateTime) " +
            "AND o.createDate < CAST(:endDate AS java.time.LocalDateTime)")
    Integer getRefundedTotalAmountByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 아래 Query문은 group by를 하지 않았기 때문에
     * 따로 CAST 함수를 사용하여 데이터 비교를 하지 않고 합계 count가 정상적으로 반환 됩니다.
     */
    @Query("SELECT SUM(oi.count) " +
            "FROM Order o " +
            "JOIN o.order_items oi " +
            "WHERE o.createDate >= :startDate AND o.createDate <= :endDate")
    Integer getTotalItemCountByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // 환불 완료 처리된 데이터 계산
    @Query("SELECT SUM(oi.count) " +
            "FROM Order o " +
            "JOIN o.order_items oi " +
            "WHERE o.status = '환불 완료' " +
            "  AND o.createDate >= CAST(:startDate AS java.time.LocalDateTime) " +
            "  AND o.createDate < CAST(:endDate AS java.time.LocalDateTime)")
    Integer getRefundedItemCountByDateRange(@Param("startDate") String startDate, @Param("endDate") String endDate);

}
