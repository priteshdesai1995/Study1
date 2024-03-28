package com.base.api.response.dto;

import java.util.List;

import com.base.api.entities.User;

import lombok.Data;

@Data
public class SearchInfo {
	public List<User> users;
	public int count;
}
