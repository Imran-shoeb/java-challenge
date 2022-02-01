package jp.co.axa.apidemo.services;

import jp.co.axa.apidemo.entities.Employee;
import jp.co.axa.apidemo.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService{

    @Autowired
    private EmployeeRepository employeeRepository;

    //initialize EmployeeRepository to use the built-in methods
    public void setEmployeeRepository(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    //all the employee data are cacheable
    //retrieving list of employee data
    @Cacheable(value="employees")  
    public List<Employee> retrieveEmployees() {
        List<Employee> employees = employeeRepository.findAll();
        return employees;
    }

    //retrieving employee data with a specific ID
    public Employee getEmployee(Long employeeId) {
        Optional<Employee> optEmp = employeeRepository.findById(employeeId);
        return optEmp.get();
    }

    //saving emloyee data
    public void saveEmployee(Employee employee){
        employeeRepository.save(employee);
    }

    //since the employee get deleted, using CacheEvict to remove all cached data. 
    //deleting employee data with a specific ID
    @CacheEvict(value="employees", allEntries=true)  
    public void deleteEmployee(Long employeeId){
        employeeRepository.deleteById(employeeId);
    }

    //since the employee data is getting updated, updating cache with the specific ID
    //updating employee with the ID
    @CachePut(cacheNames="employees", key="#employee.id")
    public void updateEmployee(Employee employee) {
        employeeRepository.save(employee);
    }
}