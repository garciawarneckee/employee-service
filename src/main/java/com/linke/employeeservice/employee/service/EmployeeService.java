package com.linke.employeeservice.employee.service;

import com.linke.employeeservice.amazon.AmazonClient;
import com.linke.employeeservice.employee.Employee;
import com.linke.employeeservice.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.List;

@Service
public class EmployeeService {

    private EmployeeRepository employeeRepository;
    private AmazonClient amazonClient;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository,
                           AmazonClient amazonClient) {
        this.employeeRepository = employeeRepository;
        this.amazonClient = amazonClient;
    }

    /**
     * Gets a list of employees by some or non filters.
     * @param filters filters to apply: firstName, lastName, charge or salary.
     * @return list of employees.
     */
    public List<Employee> getAll(Specification filters) {
        return employeeRepository.findAll(filters);
    }

    /**
     * Save in DB a new Employee.
     * @param employee employee data to be saved
     * @return true if the process it's ok.
     */
    public Boolean save(Employee employee) {
        employeeRepository.save(employee);
        return Boolean.TRUE;
    }

    /**
     * Delegates to Amazon client the reposibility to upload the incoming file.
     * @param fileName the name of the file
     * @param file the file.
     * @return return the Amazon URL of the file.
     * @throws IOException happens if there are any problem with the file transformation.
     */
    public String uploadPhoto(String fileName, MultipartFile file) throws IOException {
        return this.amazonClient.uploadFile(fileName, file);
    }

    /**
     * Make a save operation over a list of employees. If one of them has a problem during the process
     * it gonna take back the whole process and save no one.
     * @param employees list of employees to be saved.
     * @return true if the process finish right.
     */
    @Transactional
    public Boolean bulkSave(List<Employee> employees) {
        employees.forEach(this::save);
        return Boolean.TRUE;
    }

    /**
     * Deletes an employee by his id.
     * @param id unique identification of an employee
     * @return true if the deletion was ok.
     */
    public Boolean delete(Long id) {
        employeeRepository.deleteById(id);
        return Boolean.TRUE;
    }
}
