package com.dinz.library.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;

@Entity
@Table(name = "publisher")
@Data
public class Publisher implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1381572681226979477L;
	@Id
	@Column(name = "id")
	private Long id;
	@Column(name = "publisher_code")
	private String publisherCode;
	@Column(name = "name")
	private String name;
	@Column(name = "create_date")
	private Date createDate;
	@Column(name = "modified_date")
	private Date modifiedDate;
	@Column(name = "create_by")
	private String createBy;
	@Column(name = "modified_by")
	private String modifiedBy;

	@Column(name = "delete_status")
	private Integer deleteStatus;

	@JsonBackReference
	@OneToMany(mappedBy = "publisher", fetch = javax.persistence.FetchType.LAZY)
	private List<Book> books;
}
