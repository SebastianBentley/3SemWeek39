package facades;

import dto.PersonDTO;
import dto.PersonsDTO;
import entities.Address;
import entities.Person;
import exceptions.MissingInputException;
import exceptions.PersonNotFoundException;
import java.util.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * Rename Class to a relevant name Add add relevant facade methods
 */
public class PersonFacade implements IPersonFacade {

    private static PersonFacade instance;
    private static EntityManagerFactory emf;

    //Private Constructor to ensure Singleton
    private PersonFacade() {
    }

    /**
     *
     * @param _emf
     * @return an instance of this facade class.
     */
    public static PersonFacade getFacadeExample(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PersonFacade();
        }
        return instance;
    }

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    //TODO Remove/Change this before use
    public long getPersonCount() {
        EntityManager em = getEntityManager();
        try {
            long renameMeCount = (long) em.createQuery("SELECT COUNT(p) FROM Person p").getSingleResult();
            return renameMeCount;
        } finally {
            em.close();
        }

    }

    @Override
    public PersonDTO addPerson(String fName, String lName, String phone, String street, String zip, String city) throws MissingInputException {
        EntityManager em = getEntityManager();
        if (fName == null || lName == null) {
            throw new MissingInputException("First Name and/or Last Name is missing");
        }
        Person person = new Person(fName, lName, phone);
        try {
            em.getTransaction().begin();
            TypedQuery query = em.createQuery("SELECT a FROM Address a WHERE a.street = :street AND a.zip = :zip AND a.city = :city", Address.class);
            query.setParameter("street", street);
            query.setParameter("zip", zip);
            query.setParameter("city", city);
            List<Address> addresses = query.getResultList();
            if(addresses.size() > 0){
                person.setAddress(addresses.get(0));
            } else {
                person.setAddress(new Address(street, zip, city));
            }
            
            em.persist(person);
            em.getTransaction().commit();
            return new PersonDTO(person);
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO deletePerson(Long id) throws PersonNotFoundException {
        EntityManager em = getEntityManager();
        try {
            Person person = em.find(Person.class, id);
            if (person == null) {
                throw new PersonNotFoundException("Could not delete, provided id does not exist");
            }

            TypedQuery<Person> q1 = em.createQuery("SELECT e FROM Person e WHERE e.address.id = :id", Person.class);
            q1.setParameter("id", person.getAddress().getId());
            List<Person> pList = q1.getResultList();
            if (pList.size() > 1) {
                em.getTransaction().begin();
                em.remove(person);
                em.getTransaction().commit();
                return new PersonDTO(person);
            } else {
                em.getTransaction().begin();
                em.remove(person);
                em.remove(person.getAddress());
                em.getTransaction().commit();
                return new PersonDTO(person);
            }

        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO getPerson(Long id) throws PersonNotFoundException {
        EntityManager em = getEntityManager();
        try {
            Person person = em.find(Person.class, id);
            if (person == null) {
                throw new PersonNotFoundException("No person with provided id found");
            }
            return new PersonDTO(person);
        } finally {
            em.close();
        }
    }

    @Override
    public PersonsDTO getAllPersons() {
        EntityManager em = getEntityManager();
        try {
            TypedQuery<Person> query = em.createNamedQuery("Person.getAll", Person.class);
            List<Person> personList = query.getResultList();
            return new PersonsDTO(personList);
        } finally {
            em.close();
        }
    }

    @Override
    public PersonDTO editPerson(PersonDTO p) throws MissingInputException, PersonNotFoundException {
        if (p.getfName() == null || p.getlName() == null || p.getfName().length() == 0 || p.getlName().length() == 0) {
            throw new MissingInputException("First Name and/or Last Name is missing");
        }
        EntityManager em = getEntityManager();
        try {
            Person person = em.find(Person.class, p.getId());
            if (person == null) {
                throw new PersonNotFoundException(String.format("Person with id: (%d) not found", p.getId()));
            }
            em.getTransaction().begin();
            person.setFirstName(p.getfName());
            person.setLastName(p.getlName());
            person.setPhone(p.getPhone());
            person.setLastEdited(new Date());
            em.getTransaction().commit();
            return new PersonDTO(person);
        } finally {
            em.close();
        }
    }
    public void populate() {
        EntityManager em = getEntityManager();
        try {
            em.getTransaction().begin();
            Address a1 = new Address("lolleren 1", "2123", "Frederiksberg");
            Address a2 = new Address("lolvej 2", "3300", "Ballerup");
            Address a3 = new Address("Hej 12", "3230", "Svenborg");
            Person p1 = new Person("Las", "McGee", "12121212");
            Person p2 = new Person("Sven", "Johnen", "13131313");
            Person p3 = new Person("derp", "mcgee", "13541121");
            p1.setAddress(a1);
            p2.setAddress(a2);
            p3.setAddress(a3);
            em.persist(p1);
            em.persist(p2);
            em.persist(p3);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

}
