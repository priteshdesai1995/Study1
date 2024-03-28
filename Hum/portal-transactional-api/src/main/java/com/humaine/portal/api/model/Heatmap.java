package com.humaine.portal.api.model;

import java.time.OffsetDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "account_heatmap")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Heatmap {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", columnDefinition = "bigserial")
	Long id;

	@JsonIgnore
	@Column(name = "accountid")
	Long account;

	@Column(name = "category")
	String category;

	@Column(name = "device")
	String device;

	@Column(name = "pageurl")
	String pageUrl;

	@JsonIgnore
	@Column(name = "file")
	String file;

	@JsonIgnore
	@Column(name = "localpath")
	String localPath;

	@JsonIgnore
	@Column(name = "awspath")
	String awsPath;

	@JsonIgnore
	@Column(name = "aws_upload_fail")
	Boolean awsUploadFailed;

	@JsonIgnore
	@Column(name = "upload_error")
	String uploadError;

	@JsonIgnore
	@Column(name = "createdon")
	OffsetDateTime createdOn;
}
