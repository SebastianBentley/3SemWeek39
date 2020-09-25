package facade;

import entities.Customer;
import entities.ItemType;
import entities.Orders;
import entities.OrderLine;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class AllFacade implements IAllFacade {

    private static AllFacade instance;
    private static EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu");

    //Private Constructor to ensure Singleton
    public AllFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static AllFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AllFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    @Override
    public Customer createCustomer(String name, String email) {
        EntityManager em = getEntityManager();
        Customer customer = new Customer(name, email);
        try {
            em.getTransaction().begin();
            em.persist(customer);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return customer;
    }

    @Override
    public Customer findCustomer(Long id) {
        EntityManager em = getEntityManager();
        try {
            Customer customer = em.find(Customer.class, id);
            return customer;
        } finally {
            em.close();
        }

    }

    @Override
    public List<Customer> findAllCustomers() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Customer> query = em.createNamedQuery("SELECT c FROM Customer c", Customer.class);
            List<Customer> personList = query.getResultList();
            return personList;
        } finally {
            em.close();
        }
    }

    @Override
    public ItemType createItemType(String name, String description, int price) {
        EntityManager em = getEntityManager();
        ItemType item = new ItemType(name, description, price);
        try {
            em.getTransaction().begin();
            em.persist(item);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return item;
    }

    @Override
    public ItemType findItemType(Long id) {
        EntityManager em = getEntityManager();
        try {
            ItemType item = em.find(ItemType.class, id);
            return item;
        } finally {
            em.close();
        }
    }

    @Override
    public Orders createOrder(Long customerId) {
        EntityManager em = getEntityManager();
        Orders order = new Orders();
        // customer.addOrders(order);
        try {
            em.getTransaction().begin();
            Customer c = em.find(Customer.class, customerId);
            c.addOrders(order);
            //em.persist(customer);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return order;
    }

    @Override
    public OrderLine createOrderLine(Long itemTypeId, int amount, Long orderId) {
        EntityManager em = getEntityManager();
        OrderLine orderline = new OrderLine(amount);
        try {
            em.getTransaction().begin();
            ItemType item = em.find(ItemType.class, itemTypeId);
            Orders order = em.find(Orders.class, orderId);
            orderline.setOrder(order);
            item.addOrderLines(orderline);

            em.getTransaction().commit();
        } finally {
            em.close();
        }
        return orderline;
    }

    @Override
    public List<Orders> findOrderForSpecificCustomer(Long customerId) {
        EntityManager em = getEntityManager();

        try {

            TypedQuery<Orders> query = em.createQuery("SELECT o FROM Orders o WHERE o.customer.id = :id", Orders.class);
            query.setParameter("id", customerId);
            List<Orders> orders = query.getResultList();

            return orders;
        } finally {
            em.close();
        }

    }

    @Override
    public int totalPriceOfOrder(Long orderId) {
        EntityManager em = getEntityManager();
        int price = 0;
        try {
            //Orders order = em.find(Orders.class, orderId);
            TypedQuery<OrderLine> query = em.createQuery("SELECT o FROM OrderLine o WHERE o.order.id = :id", OrderLine.class);
            query.setParameter("id", orderId);
            List<OrderLine> orderLines = query.getResultList();
            
            for (OrderLine orderLine : orderLines) {
                price += orderLine.getItemType().getPrice() * orderLine.getQuantity();
                System.out.println("price : " + orderLine.getItemType().getPrice());
                System.out.println("price : " + orderLine.getQuantity());
            }
            return price;
        } finally {
            em.close();
        }
    }

    public void populate() {
        EntityManager em = getEntityManager();
        Customer c1 = new Customer("lol", "lol@lol.dk");
        Orders order = new Orders();
        ItemType itemtype = new ItemType("hej", "hjesa", 12);
        OrderLine orderline = new OrderLine();

        try {
            em.getTransaction().begin();
            em.persist(c1);
            em.persist(order);
            em.persist(itemtype);
            em.persist(orderline);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

}
