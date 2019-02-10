package com.linke.employeeservice.employee;

import com.linke.employeeservice.employee.repository.EmployeeSpecifications;
import com.linke.employeeservice.employee.service.EmployeeService;
import com.linke.employeeservice.helpers.CsvHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@RestController
public class EmployeeController {

    private EmployeeService service;
    private EmployeeSpecifications specifications;

    @Autowired
    public EmployeeController(EmployeeService service, EmployeeSpecifications specifications) {
        this.service = service;
        this.specifications = specifications;
    }

    @GetMapping(path = "/employees", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Employee> getAll(@RequestParam(required = false) String firstName,
                                 @RequestParam(required = false) String lastName,
                                 @RequestParam(required = false) String charge,
                                 @RequestParam(required = false) Long salary) {
        Specification filters = specifications.buildCriteria(firstName, lastName, charge, salary);
        return this.service.getAll(filters);
    }

    @PostMapping(path = "/employees")
    public ResponseEntity save(@RequestPart("employee") Employee employee,
                               @RequestPart("photo") MultipartFile photo) {
        try {
            String fileName = employee.getFirstName() + "_" + employee.getLastName() + "_" + employee.getCharge();
            this.service.uploadPhoto(fileName, photo);
            this.service.save(employee);
            return new ResponseEntity<>(Boolean.TRUE, HttpStatus.CREATED);
        } catch (RuntimeException rex) {
            return new ResponseEntity<>(Boolean.TRUE, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping(path = "/employees/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity csvSave(@RequestParam("file") MultipartFile file) {
        try {
            service.bulkSave(CsvHelper.read(Employee.class, file.getInputStream()));
            return new ResponseEntity<>(
                    Boolean.TRUE,
                    HttpStatus.CREATED);
        } catch (IOException ioe) {
            return new ResponseEntity<>(
                    "There was an error during the processing of the file please send it again ",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping(path = "/employees/{id}")
    public Boolean delete(@PathVariable Long id) {
        return this.service.delete(id);
    }

}

