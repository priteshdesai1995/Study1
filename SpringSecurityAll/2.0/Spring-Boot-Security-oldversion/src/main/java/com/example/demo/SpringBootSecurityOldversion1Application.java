package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.example.demo.Entity.Customer;
import com.example.demo.Repository.CustomerRepository;

@SpringBootApplication
public class SpringBootSecurityOldversion1Application {
//implements CommandLineRunner{

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	public static void main(String[] args) {
		SpringApplication.run(SpringBootSecurityOldversion1Application.class, args);
	}

//	@Override
//	public void run(String... args) throws Exception {
//		
//		Customer customer = new Customer();
//		customer.setCustomerFirstName("Pritesh");
//		customer.setCustomerLastName("Desai");
//		customer.setAddress("Ahmedabad");
//		customer.setPhoneNumber("8888888888");
//		customer.setCustomerEmail("pritesh.desai@gmail.com");
//		customer.setPassword(this.bCryptPasswordEncoder.encode("pritesh@123"));
//		customer.setRole("ROLE_NORMAL");
//		this.customerRepository.save(customer);
//		
//		Customer customer1 = new Customer();
//		customer1.setCustomerFirstName("Swapnil");
//		customer1.setCustomerLastName("Limgude");
//		customer1.setAddress("Pune");
//		customer1.setPhoneNumber("9999999999");
//		customer1.setCustomerEmail("swapnil.limgude@gmail.com");
//		customer1.setPassword(this.bCryptPasswordEncoder.encode("swap@123"));
//		customer1.setRole("ROLE_ADMIN");
//		this.customerRepository.save(customer1);
//	}

}
