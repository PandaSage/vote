package com.aeexe.vote.example.step01;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * 주문 생성 프로세스 sample
 * by panda
 *
 * todo 1. order, orderitem 객체를 생성하여 주문 프로세스를 생성 한다.
 * todo 2. 주문을 만들고 금액별 할인, 추출 등 비즈니스를 구현한다
 */
public class OrderProcess {
    public static void main(String[] args) {
        Order order1 = new Order(
                1111l, "or-001", BigDecimal.ZERO, Arrays.asList(
                        new OrderItem(
                                1L, "oi-001", new BigDecimal("1000"), 1
                        ),
                        new OrderItem(
                                2L, "oi-001", new BigDecimal("2000"), 2
                        ),
                        new OrderItem(
                                3L, "oi-001", new BigDecimal("3000"), 3
                        ),
                        new OrderItem(
                                4L, "oi-001", new BigDecimal("4000"), 4
                        ),
                        new OrderItem(
                                5L, "oi-001", new BigDecimal("5000"), 5
                        )
                )
        );
        Order order2 = new Order(
                2222L, "or-002", BigDecimal.ZERO, Arrays.asList(
                        new OrderItem(
                                1L, "oi-001", new BigDecimal("1500"), 1
                        ),
                        new OrderItem(
                                2L, "oi-001", new BigDecimal("2500"), 2
                        ),
                        new OrderItem(
                                3L, "oi-001", new BigDecimal("3500"), 3
                        ),
                        new OrderItem(
                                4L, "oi-001", new BigDecimal("4500"), 4
                        ),
                        new OrderItem(
                                5L, "oi-001", new BigDecimal("5500"), 5
                        )
            )
        );

        final Predicate<BigDecimal> bigDecimalPredicate = p -> p.compareTo(new BigDecimal("5000")) > 0;
        final Function<OrderItem, BigDecimal> getOrderItemPrice = OrderItem::getOrderItemPrice;

        final BigDecimal discount_price = order1.getOrderItems().stream()
                .map(getOrderItemPrice)
                .filter(bigDecimalPredicate)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        System.out.println("discount_price value is : " + discount_price);

        Function<Order, BigDecimal> mapper = order -> order.getOrderItems().get(2).getOrderItemPrice();
        Predicate<BigDecimal> filter = bigDecimal -> bigDecimal.compareTo(new BigDecimal("5000")) > 0;

        BigDecimal result = reduce(order1, filter);

        System.out.println("reduce price is : " + result);

        System.out.println(
            "mapper value is : " + mapper.apply(order1) + "\n" +
            "filter value is : " + filter.test(mapper.apply(order1))
        );
    }

    private static<T extends Order> BigDecimal reduce(T t, Predicate<BigDecimal> filter) {
        BigDecimal result = BigDecimal.ZERO;

        for (OrderItem item : t.getOrderItems()) {
            final BigDecimal target = item.getOrderItemPrice();
            if (filter.test(target)) {
                result = result.add(target);
            }
        }
        return result;
    }

    private static <T extends Order> BigDecimal mapper(List<T> items, Function<T, BigDecimal> mapper) {
        BigDecimal result = BigDecimal.ZERO;

        for (T item : items) {
            result = mapper.apply(item);
        }

        return result;
    }


    /**
     * 주문키, 주문 번호, 주문 총 금액, 주문 아이템 리스트, 등록/수정 로그 데이터 구현
     * 총 금액은 아이템의 상품 단가의 섬
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class Order {
        private Long orderId;
        private String orderNo;
        private BigDecimal totPrice;
        private List<OrderItem> orderItems;

        public BigDecimal getTotPrice() {
           BigDecimal result = BigDecimal.ZERO;
           for(int i = 0; i<orderItems.size(); i++){
               result.add(orderItems.get(i).getOrderItemPrice());
           }
           return result;
        }

        public BigDecimal discountTotPrice50Percent(){
            BigDecimal result = BigDecimal.ZERO;
            for(int i = 0; i<orderItems.size(); i++){
                result.add(orderItems.get(i).getOrderItemPrice());
            }
            return result.multiply(new BigDecimal("0.5"));
        }

        public BigDecimal getTotalPrice() {
            return orderItems.stream()
                    .map(OrderItem::getOrderItemPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }

    /**
     * 주문 아이템 키, 주문 아이템 번호, 주문 아이템 금액, 주문 아이템 수량, 등록/수정 로그 데이터,
     * 주문 아이템 금액 메서드 구현값
     *
     * 개별 아이탬 금액은 원 판매가 * 구입 수량
     */
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class OrderItem {
        private Long orderItemId;
        private String orderItemNo;
        private BigDecimal orderItemPrice;
        private int quantity;

        public BigDecimal getOrderItemPrice() {
            return orderItemPrice.multiply(new BigDecimal(quantity));
        }
    }
}
