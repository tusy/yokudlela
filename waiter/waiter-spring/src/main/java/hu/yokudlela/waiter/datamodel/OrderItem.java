/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.yokudlela.waiter.datamodel;

import lombok.Builder;
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
public class OrderItem {
    private String orderId;
    private String foodId;
    private String tableId;
    private Integer quantity;
    private String note;
    private boolean note4all = true;

    

    @Builder
    public OrderItem(String orderId, String foodId, String tableId, Integer quantity, String note, String groupName) {
        this.orderId = orderId;
        this.foodId = foodId;
        this.tableId = tableId;
        this.quantity = quantity;
        this.note = note;
    }
}
