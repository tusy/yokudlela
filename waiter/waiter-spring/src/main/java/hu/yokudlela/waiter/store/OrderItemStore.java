/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.yokudlela.waiter.store;

import hu.yokudlela.waiter.datamodel.OrderItem;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

/**
 *
 * @author tusy
 */
@Service
public class OrderItemStore {
    
    private static final Map<String, List<OrderItemsState>> orders = new HashMap<>();
    
    /**
     * Hozzáad egy rendelési tételt az adott asztalhoz
     * @param tableId
     * @param orders
     */
    public void add(String tableId, List<OrderItemsState> orders) {
        orders.forEach(o -> o.setTableId(tableId));
        OrderItemStore.orders.getOrDefault(tableId, new ArrayList<>()).addAll(orders);
 
    }

    /**
     * Módosítja egy rendelési tétel állapotát
     * @param orderIds
     * @param state
     */
    public void modifyState(String[] orderIds, OrderState state) {
        
        OrderItemStore.orders.forEach((s,o) -> {
            List<OrderItemsState> nOIS = new ArrayList<>();
            List<OrderItemsState> dOIS = new ArrayList<>();
            
            o.forEach(ois -> {
                OrderItemsState oois = this.splitOrderItemsStateByOrderId(ois, orderIds, state);
                if (oois != null) {
                    nOIS.add(oois);
                }
                if (ois.getOrders().isEmpty()) {
                    dOIS.add(ois);
                }
            });
            if (!dOIS.isEmpty()) {
                o.removeAll(dOIS);
            }
            if (!nOIS.isEmpty()) {
                o.addAll(nOIS);
            }
        }
        );
    }
    
    /**
     * Töröl egy rendelést az asztalról ha lehet
     * @param orderId
     */
    public void delete(String[] orderIds) {
        
        OrderItemStore.orders.forEach((s,o) -> {
            List<OrderItemsState> dOIS = new ArrayList<>();
            
            o.forEach(ois -> {
                deleteOrderItemFromOrderItemsState(ois, orderIds);
                if (ois.getOrders().isEmpty())
                    dOIS.add(ois);
            });
            if (!dOIS.isEmpty()) {
                o.removeAll(dOIS);
            }
        }
        );
    }

    
    /**
     * Törli az asztal összes rendelését
     * @param tableId
     */
    public void clear(String tableId) {
        OrderItemStore.orders.remove(tableId);
    }
    
    public List<OrderItemsState> getAll(String tableId) {
        return OrderItemStore.orders.get(tableId);
    }
    
    private OrderItemsState splitOrderItemsStateByOrderId(OrderItemsState orderItemsState, String[] orderIds, OrderState orderState) {
        OrderItemsState newOIS = new OrderItemsState(orderItemsState.getTableId(),  orderState);
        orderItemsState.getOrders().forEach(e -> {
            if (Arrays.stream(orderIds).anyMatch(e.getOrderId()::equals)) {
                newOIS.getOrders().add(e);
            } 
        }
        
        );
        if (!newOIS.getOrders().isEmpty()) {
            orderItemsState.getOrders().removeAll(newOIS.getOrders());
            return newOIS;
        } 
        return null;
    }
    private void deleteOrderItemFromOrderItemsState(OrderItemsState orderItemsState, String[] orderIds) {
        List<OrderItem> tempOrderList = orderItemsState.getOrders().stream()
                .filter(x -> Arrays.stream(orderIds).anyMatch(x.getOrderId()::equals))
                .collect(Collectors.toList());
        orderItemsState.setOrders(tempOrderList);
    }
}
