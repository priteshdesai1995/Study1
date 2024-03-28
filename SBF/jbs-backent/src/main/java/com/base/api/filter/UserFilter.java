package com.base.api.filter;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserFilter extends FilterBase {
	String fullName;
	String email;
	String gender;
	String status;
	String cellPhone;
	String roleName;
}
