package com.linke.employeeservice.employee.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.linke.employeeservice.amazon.AmazonClient;
import com.linke.employeeservice.employee.Employee;
import com.linke.employeeservice.employee.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Stream;

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

    public List<Employee> getAll(Specification filters) {
        return employeeRepository.findAll(filters);
    }

    public Boolean save(Employee employee) {
        try {
            employeeRepository.save(employee);
            return Boolean.TRUE;
        } catch(RuntimeException rex) {
            throw new RuntimeException("Error while saving");
        }
    }

    public String uploadPhoto(String fileName, MultipartFile file) {
        return this.amazonClient.uploadFile(fileName, file);
    }

    @Transactional
    public Boolean bulkSave(List<Employee> employees) {
        employees.forEach(this::save);
        return Boolean.TRUE;
    }

    public Boolean delete(Long id) {
        try {
            employeeRepository.deleteById(id);
            return Boolean.TRUE;
        } catch(RuntimeException rex) {
            throw new RuntimeException("Error while deleting");
        }
    }
}
