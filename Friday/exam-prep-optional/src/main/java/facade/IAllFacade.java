package facade;

import entities.Customer;
import entities.ItemType;
import entities.Orders;
import entities.OrderLine;
import java.util.List;


public interface IAllFacade {
    public Customer createCustomer(String name, String email);
    public Customer findCustomer(Long id);
    public List<Customer> findAllCustomers();
    public ItemType createItemType(String name, String description, int price);
    public ItemType findItemType(Long id);
    public Orders createOrder(Long customerId);
    public OrderLine createOrderLine(Long itemTypeId, int amount, Long orderId);
    public List<Orders> findOrderForSpecificCustomer(Long customerId);
    public int totalPriceOfOrder(Long orderId);
    
}
