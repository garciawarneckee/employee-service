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

import java.io.IOException;
import java.util.List;

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

    /**
     * Saves an employee data with the optional photo.
     * @param employee
     * @param photo
     * @return
     */
    @PostMapping(path = "/employees")
    public ResponseEntity save(@RequestPart("employee") Employee employee,
                               @RequestPart(value = "photo", required = false) MultipartFile photo) throws IOException {
        this.service.save(employee);
        if(photo != null) {
            String fileName = employee.getFirstName() + "_" + employee.getLastName() + "_" + employee.getCharge();
            this.service.uploadPhoto(fileName, photo);
        }
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.CREATED);
    }

    /**
     * Makes a bulk save of Employees with basic data provided by a CSV file.
     * @param file employees data to be saved.
     * @return
     */
    @PostMapping(path = "/employees/csv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity csvSave(@RequestParam("file") MultipartFile file) throws IOException {
        service.bulkSave(CsvHelper.read(Employee.class, file.getInputStream()));
        return new ResponseEntity<>(Boolean.TRUE, HttpStatus.CREATED);
    }

    @DeleteMapping(path = "/employees/{id}")
    public Boolean delete(@PathVariable Long id) {
        return this.service.delete(id);
    }

}

