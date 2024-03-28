package com.example.demo.Entity;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@SuppressWarnings("serial")
public class CustomerDetail implements UserDetails{
	
	private Customer customer;
	
	

	public CustomerDetail(Customer customer) {
		super();
		this.customer = customer;
	}

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		HashSet<SimpleGrantedAuthority> set = new HashSet<SimpleGrantedAuthority>();
		set.add(new SimpleGrantedAuthority(this.customer.getRole()));
		return set;
	}

	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return this.customer.getPassword();
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return this.customer.getCustomerFirstName();
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}

}
