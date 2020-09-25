package main;

import entities.Orders;
import facade.AllFacade;
import java.util.List;

public class Tester {

    public static void main(String[] args) {
        AllFacade facade = new AllFacade();

        facade.createCustomer("Jens", "lol@lol.dk");
        facade.createOrder(1L);
        facade.createOrder(1L);
        facade.createOrder(1L);
     
        facade.createItemType("Bacon", "Med Ekstra ost", 50);
        
        facade.createOrderLine(1L, 5, 1L);
        facade.createOrderLine(1L, 10, 1L);
        facade.createOrderLine(1L, 5, 1L);
        
        List<Orders> orders = facade.findOrderForSpecificCustomer(1L);
        
        for (Orders order : orders) {
            System.out.println("All orders for " + order.getCustomer().getName() + " - OrderId:" + order.getId());
        }
        
        int price = facade.totalPriceOfOrder(1L);
        System.out.println("Price of order 1: " + price);
       
    }

}
