//package rest;
//
//import dto.PersonDTO;
//import entities.Person;
//import utils.EMF_Creator;
//import io.restassured.RestAssured;
//import static io.restassured.RestAssured.given;
//import io.restassured.parsing.Parser;
//import java.net.URI;
//import java.util.List;
//import javax.persistence.EntityManager;
//import javax.persistence.EntityManagerFactory;
//import javax.ws.rs.core.UriBuilder;
//import org.glassfish.grizzly.http.server.HttpServer;
//import org.glassfish.grizzly.http.util.HttpStatus;
//import org.glassfish.jersey.grizzly2.httpserver.GrizzlyHttpServerFactory;
//import org.glassfish.jersey.server.ResourceConfig;
//import static org.hamcrest.MatcherAssert.assertThat;
//import org.hamcrest.Matchers;
//import static org.hamcrest.Matchers.equalTo;
//import static org.hamcrest.Matchers.notNullValue;
//import org.junit.jupiter.api.AfterAll;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Test;
////Uncomment the line below, to temporarily disable this test
////@Disabled
//
//public class PersonResourceTest {
//
//    private static final int SERVER_PORT = 7777;
//    private static final String SERVER_URL = "http://localhost/api";
//    private static Person p1, p2;
//
//    static final URI BASE_URI = UriBuilder.fromUri(SERVER_URL).port(SERVER_PORT).build();
//    private static HttpServer httpServer;
//    private static EntityManagerFactory emf;
//
//    static HttpServer startServer() {
//        ResourceConfig rc = ResourceConfig.forApplication(new ApplicationConfig());
//        return GrizzlyHttpServerFactory.createHttpServer(BASE_URI, rc);
//    }
//
//    @BeforeAll
//    public static void setUpClass() {
//        //This method must be called before you request the EntityManagerFactory
//        EMF_Creator.startREST_TestWithDB();
//        emf = EMF_Creator.createEntityManagerFactoryForTest();
//
//        httpServer = startServer();
//        //Setup RestAssured
//        RestAssured.baseURI = SERVER_URL;
//        RestAssured.port = SERVER_PORT;
//        RestAssured.defaultParser = Parser.JSON;
//    }
//
//    @AfterAll
//    public static void closeTestServer() {
//        //System.in.read();
//        //Don't forget this, if you called its counterpart in @BeforeAll
//        EMF_Creator.endREST_TestWithDB();
//        httpServer.shutdownNow();
//    }
//
//    // Setup the DataBase (used by the test-server and this test) in a known state BEFORE EACH TEST
//    //TODO -- Make sure to change the EntityClass used below to use YOUR OWN (renamed) Entity class
//    @BeforeEach
//    public void setUp() {
//        EntityManager em = emf.createEntityManager();
//        p1 = new Person("Test", "Tester", "123");
//        p2 = new Person("TestTwo", "TesterTwo", "456");
//        try {
//            em.getTransaction().begin();
//            em.createNamedQuery("Person.deleteAllRows").executeUpdate();
//            em.persist(p1);
//            em.persist(p2);
//            em.getTransaction().commit();
//        } finally {
//            em.close();
//        }
//    }
//
//    @Test
//    public void testServerIsUp() {
//        System.out.println("Testing is server UP");
//        given().when().get("/person").then().statusCode(200);
//    }
//
//    //This test assumes the database contains two rows
//    @Test
//    public void testDummyMsg() throws Exception {
//        given()
//                .contentType("application/json")
//                .get("/person/").then()
//                .assertThat()
//                .statusCode(HttpStatus.OK_200.getStatusCode())
//                .body("msg", equalTo("Hello World"));
//    }
//
//    @Test
//    public void testCount() throws Exception {
//        given()
//                .contentType("application/json")
//                .get("/person/count").then()
//                .assertThat()
//                .statusCode(HttpStatus.OK_200.getStatusCode())
//                .body("count", equalTo(2));
//    }
//
//    @Test
//    public void getAllPersonsTest() {
//        List<PersonDTO> personsDTO;
//        personsDTO = given()
//                .contentType("application/json")
//                .when()
//                .get("/person/all").then()
//                .extract().body().jsonPath().getList("all", PersonDTO.class);
//
//        PersonDTO p1DTO = new PersonDTO(p1);
//        PersonDTO p2DTO = new PersonDTO(p2);
//
//        assertThat(personsDTO, Matchers.containsInAnyOrder(p1DTO, p2DTO));
//
//    }
//
//    @Test
//    public void testFindPersonById() {
//        given()
//                .contentType("application/json")
//                .get("/person/" + p1.getId()).then()
//                .assertThat()
//                .statusCode(HttpStatus.OK_200.getStatusCode())
//                .body("fName", equalTo(p1.getFirstName()));
//    }
//
//    @Test
//    public void testAddPerson() {
//        given()
//                .contentType("application/json")
//                .body(new PersonDTO("Imposter", "FromAddPerson", "0001", "testGade", "1234", "testby"))
//                .when()
//                .post("person")
//                .then()
//                .body("fName", equalTo("Imposter"))
//                .body("lName", equalTo("FromAddPerson"))
//                .body("phone", equalTo("0001"))
//                .body("id", notNullValue());
//    }
//
//    @Test
//    public void testEditPerson() {
//        PersonDTO pDTO = new PersonDTO(p1);
//        pDTO.setfName("ChangedMyName");
//        given()
//                .contentType("application/json")
//                .body(pDTO)
//                .when()
//                .put("person/" + pDTO.getId())
//                .then()
//                .body("fName", equalTo("ChangedMyName"))
//                .body("lName", equalTo("Tester"))
//                .body("phone", equalTo("123"));
//    }
//
//    @Test
//    public void testDeletePerson() {
//        PersonDTO p1DTO = new PersonDTO(p1);
//        given()
//                .contentType("application/json")
//                .when()
//                .delete("/person/" + p1DTO.getId()).then()
//                .assertThat()
//                .statusCode(HttpStatus.OK_200.getStatusCode());
//
//        List<PersonDTO> personsDTO;
//        personsDTO = given()
//                .contentType("application/json")
//                .when()
//                .get("/person/all").then()
//                .extract().body().jsonPath().getList("all", PersonDTO.class);
//
//        PersonDTO p2DTO = new PersonDTO(p2);
//
//        assertThat(personsDTO, Matchers.containsInAnyOrder(p2DTO));
//    }
//
//}
