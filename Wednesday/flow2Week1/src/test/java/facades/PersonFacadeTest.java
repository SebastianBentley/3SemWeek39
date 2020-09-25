package facades;

import dto.PersonDTO;
import entities.Person;
import exceptions.MissingInputException;
import exceptions.PersonNotFoundException;
import utils.EMF_Creator;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import static org.hamcrest.MatcherAssert.assertThat;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

//Uncomment the line below, to temporarily disable this test
//@Disabled
public class PersonFacadeTest {

    private static EntityManagerFactory emf;
    private static PersonFacade facade;
    private static Person p1, p2;

    public PersonFacadeTest() {
    }

    @BeforeAll
    public static void setUpClass() {
        emf = EMF_Creator.createEntityManagerFactoryForTest();
        facade = PersonFacade.getFacadeExample(emf);
    }

    @AfterAll
    public static void tearDownClass() {
//        Clean up database after test is done or use a persistence unit with drop-and-create to start up clean on every test
    }

    // Setup the DataBase in a known state BEFORE EACH TEST
    //TODO -- Make sure to change the script below to use YOUR OWN entity class
    @BeforeEach
    public void setUp() {
        EntityManager em = emf.createEntityManager();
        p1 = new Person("Test", "Tester", "123");
        p2 = new Person("TestTwo", "TesterTwo", "456");
        try {
            em.getTransaction().begin();
            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
            em.persist(p1);
            em.persist(p2);
            em.getTransaction().commit();
        } finally {
            em.close();
        }
    }

    @AfterEach
    public void tearDown() {
//        Remove any data after each test was run
    }

    // TODO: Delete or change this method 
    @Test
    public void testAFacadeMethod() {
        assertEquals(2, facade.getPersonCount(), "Expects two rows in the database");
    }

    @Test
    public void testGetAllPersons() {
        assertEquals(2, facade.getPersonCount());
    }

    @Test
    public void testGetPersonById() throws PersonNotFoundException {
        PersonDTO person = facade.getPerson(p1.getId());
        assertEquals("Test", person.getfName());
    }

    @Test
    public void testAddPerson() throws MissingInputException {
        facade.addPerson("Imposter", "FromAddPerson", "0001");
        assertEquals(3, facade.getPersonCount());
    }

    @Test
    public void testEditPerson() throws MissingInputException, PersonNotFoundException {
        p2.setLastName("ChangedMyName");
        PersonDTO p2New = facade.editPerson(new PersonDTO(p2));
        assertEquals(p2New.getlName(), p2.getLastName());
    }

    @Test
    public void testDeletePerson() throws PersonNotFoundException {
        EntityManager em = emf.createEntityManager();
        try {
            Person p = em.find(Person.class, p1.getId());
            assertNotNull(p);
            facade.deletePerson(p1.getId());
            assertEquals(1, facade.getPersonCount());

        } finally {
            em.close();
        }
    }

}
