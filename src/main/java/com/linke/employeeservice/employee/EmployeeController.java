package com.linke.employeeservice.employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Optional;

@RestController
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping(path = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employee> getAll(@RequestParam Optional<String> firstName,
                                 @RequestParam Optional<String> lastName,
                                 @RequestParam Optional<String> charge,
                                 @RequestParam Optional<Long> salary) {
        return this.employeeService.getAll();
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
