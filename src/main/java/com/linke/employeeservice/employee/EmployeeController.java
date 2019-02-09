package com.linke.employeeservice.employee;

import com.linke.employeeservice.employee.repository.EmployeeSpecifications;
import com.linke.employeeservice.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(path = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employee> getAll(@RequestParam(required = false) String firstName,
                                 @RequestParam(required = false) String lastName,
                                 @RequestParam(required = false) String charge,
                                 @RequestParam(required = false) Long salary) {
        Specification filters = EmployeeSpecifications.findByCriteria(firstName, lastName, charge, salary);
        return this.employeeService.getAll(filters);
    }

    @PostMapping(path = "/employees", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity save(@RequestBody Employee employee) {
        try {
            this.employeeService.save(employee);
            return new ResponseEntity<>(Boolean.TRUE, HttpStatus.OK);
        } catch (RuntimeException rex) {
            return new ResponseEntity<>(Boolean.TRUE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping(path = "/employees/{id}")
    public Boolean delete(@PathVariable Long id) {
        return this.employeeService.delete(id);
    }

}
