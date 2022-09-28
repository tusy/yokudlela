/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hu.yokudlela.waiter.rest;

import hu.yokudlela.waiter.datamodel.OrderItem;
import hu.yokudlela.waiter.store.OrderItemStore;
import hu.yokudlela.waiter.store.OrderItemsState;
import hu.yokudlela.waiter.store.OrderState;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
/**
 * Pincér rendeléseit kezelő REST kontroller
 * @author tusy
 */
@RestController()
@RequestMapping(path = "/waiter")
public class WaiterController {
     @Autowired
    private OrderItemStore service;
   
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Fizetés sikeres"),
        @ApiResponse(responseCode = "400", description = "Hibás adatok"),
        @ApiResponse(responseCode = "500", description = "Sikertelen fizetés")    
    })
    @Operation(summary = "Fizetés indítása")    
    @PostMapping(path = "/payment")
    public void payment(@Parameter(description = "Asztal azonosító", required = true) @RequestBody(required = true) String pTableId) throws Exception {
         List<OrderItemsState> table = service.getAll(pTableId);
         List<String> orderIds = new ArrayList<String>();
         
         for (OrderItemsState o : table) {
             for (OrderItem i: o.getOrders()) {
                 orderIds.add(i.getOrderId());
             }
         }
         service.modifyState(orderIds.toArray(String[]::new), OrderState.PAID);
         
    }    

    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "a rendelés sikeres"),
        @ApiResponse(responseCode = "500", description = "Sikertelen rendelés")    
    })
    @Operation(summary = "Rednedelés felvétele")    
    @PutMapping(path = "/order")
    public void order(@Parameter(description = "Asztal azonosító", required = true) @RequestBody(required = true) String pTableId,
            @Parameter(description = "Rednelt Tételek", required = true) @RequestBody(required = true) List<OrderItem> pOrders) throws Exception {
        OrderItemsState ois = new OrderItemsState(pOrders, pTableId, OrderState.ORDERED);
        List<OrderItemsState> oisList = new ArrayList<>();
        oisList.add(ois);
        service.add(pTableId, oisList);
         
    }    

    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Kiszállítás sikeres"),
        @ApiResponse(responseCode = "500", description = "Sikertelen Kiszállítás")    
    })
    @Operation(summary = "Rendelés kiszállítása")    
    @PostMapping(path = "/delivery")
    public void delivery(@Parameter(description = "Asztal azonosító", required = true) @RequestBody(required = true) String pTableId,
            @Parameter(description = "Rendelés azonosítók", required = true) @RequestBody(required = true) String[] pOrderIds) throws Exception {
         service.modifyState(pOrderIds, OrderState.DELIVERED);
    }    
    
    @ApiResponses(value = { 
        @ApiResponse(responseCode = "200", description = "Sikeres lekérdezés", 
	    content = { @Content(mediaType = "application/json",  
	    array = @ArraySchema(schema = @Schema(implementation = OrderItemsState.class))) }),
    })
    @Operation(summary = "Rendelések lekérdezése")    
    @GetMapping(path = "/get", produces = MediaType.APPLICATION_JSON_VALUE)    
    public List<OrderItemsState> orderStatuses(
                @Parameter(description = "Asztal azonosítója", required = true) @RequestParam(name = "tableId", required = true) String pTableId)  {
        return service.getAll(pTableId);
    }
}
