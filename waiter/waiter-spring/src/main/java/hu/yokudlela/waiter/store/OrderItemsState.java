/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.yokudlela.waiter.store;

import hu.yokudlela.waiter.datamodel.OrderItem;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 *
 * @author tusy
 */
@Data
@EqualsAndHashCode()
@NoArgsConstructor
public class OrderItemsState {
    private List<OrderItem> orders = new ArrayList<>();
    private OrderState state = OrderState.ORDERED;
    private String tableId;

    public OrderItemsState(List<OrderItem> orders) {
        this.orders = orders;
    }
    public OrderItemsState(List<OrderItem> orders, String tableId, OrderState orderState) {
        this.orders = orders;
        this.tableId = tableId;
        this.state = orderState;
    }
    
    public OrderItemsState(OrderItemsState orderItemsState) {
        this.orders = orderItemsState.orders;
        this.tableId = orderItemsState.tableId;
        this.state = orderItemsState.state;
    }
    public OrderItemsState(String tableId, OrderState orderState) {
        this.orders = new ArrayList<>();
        this.tableId = tableId;
        this.state = orderState;
    }

}
