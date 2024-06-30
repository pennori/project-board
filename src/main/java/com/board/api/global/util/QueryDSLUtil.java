package com.board.api.global.util;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.Expressions;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class QueryDSLUtil {
    public List<OrderSpecifier<?>> getOrderSpecifier(Sort sort, Path<?> qClass) {
        List<OrderSpecifier<?>> orders = new ArrayList<>();
        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            orders.add(getSortedColumn(direction, qClass, prop));
        });
        return orders;
    }

    public OrderSpecifier<?> getSortedColumn(Order order, Path<?> parent, String fieldName) {
        Path<Object> fieldPath = Expressions.path(Object.class, parent, fieldName);
        return new OrderSpecifier(order, fieldPath);
    }
}
