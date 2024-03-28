package com.base.api.request.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import com.base.api.response.dto.StateTranslableDto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
//@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class StateDTO implements Serializable {

	private static final long serialVersionUID = -8954681213196859937L;

	public String stateCode;
	public String status;
	public List<StateTranslableDto> stateDtos;
	public UUID countryId;
	public LocalDateTime createDate;
	public LocalDateTime updatedAt;
	public String uuid;
	public String countryName;
}
