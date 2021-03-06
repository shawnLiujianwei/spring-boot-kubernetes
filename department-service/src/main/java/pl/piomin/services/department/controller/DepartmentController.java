package pl.piomin.services.department.controller;

import java.util.List;

import client.Department;
import client.Employee;
import client.EmployeeClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import pl.piomin.services.department.repository.DepartmentRepository;

@RestController
public class DepartmentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);
	
	@Autowired
	DepartmentRepository repository;
	@Autowired
	EmployeeClient employeeClient;

	@GetMapping("/feign")
	public List<Employee> listRest() {
		return employeeClient.findByDepartment("1");
	}
	
	@PostMapping("/")
	public Department add(@RequestBody Department department) {
		LOGGER.info("Department add: {}", department);
		return repository.save(department);
	}

	@GetMapping("/hello")
	public String hello() {
		return "hello from department";
	}

	@GetMapping("/hello/employee")
	public String helloE() {
		return employeeClient.hello();
	}
	
	@GetMapping("/{id}")
	public Department findById(@PathVariable("id") String id) {
		LOGGER.info("Department find: id={}", id);
		return repository.findById(id).get();
	}
	

	@GetMapping("/organization/{organizationId}")
	public List<Department> findByOrganization(@PathVariable("organizationId") Long organizationId) {
		LOGGER.info("Department find: organizationId={}", organizationId);
		return repository.findByOrganizationId(organizationId);
	}
	
	@GetMapping("/organization/{organizationId}/with-employees")
	public List<Department> findByOrganizationWithEmployees(@PathVariable("organizationId") Long organizationId) {
		LOGGER.info("Department find: organizationId={}", organizationId);
		List<Department> departments = repository.findByOrganizationId(organizationId);
		departments.forEach(d -> d.setEmployees(employeeClient.findByDepartment(d.getId())));
		return departments;
	}


	@RequestMapping(value = "/", method = RequestMethod.GET)
	public Iterable<Department> findAll() {
		LOGGER.info("Department find");
		return repository.findAll();
	}

}
