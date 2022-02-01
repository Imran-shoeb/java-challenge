package jp.co.axa.apidemo.controllers;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Api(value="employeeInfo", description="Operations for the employee information")
public class EmployeeController {


    //setting employee service
    @Autowired
    private EmployeeService employeeService;

    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    //ApiOperation annotation makes the documentation more meaningful in SwaggerUI

    //for fetching all employee data
    @ApiOperation(value = "View a list of employees", response = Iterable.class)
    @GetMapping("/employees")
    public List<Employee> getEmployees() {
        List<Employee> employees = employeeService.retrieveEmployees();
        return employees;
    }

    //for fetching a specific employee data with ID
    @ApiOperation(value = "Search an employee with an ID",response = Employee.class)
    @GetMapping("/employees/{employeeId}")
    public Employee getEmployee(@PathVariable(name="employeeId")Long employeeId) {
        return employeeService.getEmployee(employeeId);
    }

    //for creating employee data
    @ApiOperation(value = "Add an employee")
    @PostMapping("/employees")
    public void saveEmployee(Employee employee){
        employeeService.saveEmployee(employee);
        System.out.println("Employee Saved Successfully");
    }

    //for deleting a specific employee data using ID
    @ApiOperation(value = "Delete an employee")
    @DeleteMapping("/employees/{employeeId}")
    public void deleteEmployee(@PathVariable(name="employeeId")Long employeeId){
        employeeService.deleteEmployee(employeeId);
        System.out.println("Employee Deleted Successfully");
    }

    //for updating a specific employee data with ID
    @ApiOperation(value = "Update an employee with ID")
    @PutMapping("/employees/{employeeId}")
    public void updateEmployee(@RequestBody Employee employee,
                               @PathVariable(name="employeeId")Long employeeId){
        Employee emp = employeeService.getEmployee(employeeId);

        //if employee exists with the ID then update
        if(emp != null){
            employeeService.updateEmployee(employee);
        }

    }

}
