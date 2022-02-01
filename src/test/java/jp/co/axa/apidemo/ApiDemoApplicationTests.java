package jp.co.axa.apidemo;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.HttpClientErrorException;

import jp.co.axa.apidemo.entities.Employee;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ApiDemoApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApiDemoApplicationTests {

    // initializing restTemplate to test the api
    @Autowired
    private TestRestTemplate restTemplate;

    // since testing environment port gets changed randomly, getting the local
    // server port address
    @LocalServerPort
    private int port;

    // getting the root URL for further use in the API testing
    private String getRootUrl() {
        return "http://localhost:" + port + "/api/v1";
    }

    @Test
    public void contextLoads() {

    }

    // testing GET method for retrieving all employee data
    @Test
    public void testGetAllEmployees() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<String> entity = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = restTemplate.exchange(getRootUrl() + "/employees",
                HttpMethod.GET, entity, String.class);
        //checking if the response body is null or not
        assertNotNull(response.getBody());
        //if request gets completed then the test is OK
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }


    //testing GET method for retrieving a specific employee data using ID
    @Test
    public void testGetEmployeeById() {
        Employee employee = restTemplate.getForObject(getRootUrl() + "/employees/1", Employee.class);
        //checking the data if it is null or not. if it has some value then test is OK
        assertNotNull(employee);
    }

    @Test
    public void testCreateEmployee() {
        Employee employee = new Employee();

        //setting employee data for POST
        employee.setName("imranul");
        employee.setSalary(24000);
        employee.setDepartment("SE");
        //POST request using the data
        ResponseEntity<Employee> postResponse = restTemplate.postForEntity(getRootUrl() + "/employees", employee,
                Employee.class);
        
        //checking if the response is not null.
        assertNotNull(postResponse);
        //if we get a response and status code is 200 then the test is OK
        assertEquals(HttpStatus.OK, postResponse.getStatusCode());
    }

    @Test
    public void testUpdateEmployee() {

        // let an ID is 1 for updating employee data
        int id = 1;
        Employee employee = restTemplate.getForObject(getRootUrl() + "/employees/" + id, Employee.class);

        //setting employee data for PUT
        employee.setName("imranul1");
        employee.setSalary(23000);
        restTemplate.put(getRootUrl() + "/employees/" + id, employee);
        Employee updatedEmployee = restTemplate.getForObject(getRootUrl() + "/employees/" + id, Employee.class);

        //checking if the value is not null.
        assertNotNull(updatedEmployee);
    }

    @Test
    public void testDeleteEmployee() {

        // let an ID is 1 for deleting employee data
        int id = 1;
        Employee employee = restTemplate.getForObject(getRootUrl() + "/employees/" + id, Employee.class);

        //checking if the value is not null.
        assertNotNull(employee);
        restTemplate.delete(getRootUrl() + "/employees/" + id);

        //wrapping around exception handler because the specific employee gets deleted and it will throw an error
        try {
            employee = restTemplate.getForObject(getRootUrl() + "/employees/" + id, Employee.class);
        } catch (final HttpClientErrorException e) {

            //checking if the response code returns NOT FOUND
            assertEquals(e.getStatusCode(), HttpStatus.NOT_FOUND);
        }
    }

}
