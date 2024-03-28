package com.base.api.entities;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "logos")
@AttributeOverride(name = "id", column = @Column(name = "logo_id"))
public class Logo extends BaseEntity implements Serializable {
	
	private static final long serialVersionUID = 6226823332731150830L;

	@Column(name = "logo",columnDefinition="text", length=10485760)
	private byte[] logo;

	@Column(name = "favicon", columnDefinition="text", length=10485760)
	private byte[] favicon;

	@Column(name = "site_name")
	private String siteName;
}
