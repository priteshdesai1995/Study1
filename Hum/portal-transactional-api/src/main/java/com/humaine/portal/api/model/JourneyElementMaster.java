package com.humaine.portal.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "journey_element_master")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class JourneyElementMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigserial")
	Long id;

	@OneToMany(mappedBy = "element")
	List<JourneyElementValue> values;

	@Column(name = "name", length = 255)
	String name;

	public JourneyElementMaster(Long id, String name, List<String> values) {
		this.id = id;
		this.name = name;
		if (values == null) {
			values = new ArrayList<>();
		}

		AtomicInteger index = new AtomicInteger();
		this.values = values.stream()
				.map(e -> new JourneyElementValue(Long.valueOf(String.valueOf(index.getAndIncrement())), e))
				.collect(Collectors.toList());
	}
}
