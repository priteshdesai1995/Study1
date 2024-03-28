package com.example.demo.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.demo.Entity.Customer;
import com.example.demo.Entity.CustomerDetail;
import com.example.demo.Repository.CustomerRepository;

@Service
public class CustomerDetailService implements UserDetailsService{
	
	@Autowired
	private CustomerRepository customerRepository;

	@Override
	public UserDetails loadUserByUsername(String customerFirstName) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		//by this username user data will be loaded
		Customer customer = this.customerRepository.findByCustomerFirstName(customerFirstName);
		if(customer==null) {
			throw new UsernameNotFoundException("No user Found");
		}
		return new CustomerDetail(customer);
	}

}
