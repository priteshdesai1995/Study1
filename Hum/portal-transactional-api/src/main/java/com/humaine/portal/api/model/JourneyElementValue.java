package com.humaine.portal.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "journey_element_values")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class JourneyElementValue {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigserial")
	Long id;

	@ManyToOne
	@JoinColumn(name = "element_id")
	@JsonIgnore
	JourneyElementMaster element;

	@Column(name = "value", length = 255)
	String value;

	public JourneyElementValue(Long id, String value) {
		this.id = id;	
		this.value = value;
	}
}
