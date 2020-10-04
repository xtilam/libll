package com.dinz.library.model;

import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "status_book_detail")
@Data
public class StatusBookDetail {

	@Id
	@Column(name = "id")
	private Long id;
	@Column(name = "status_book_code")
	private String statusBookCode;
	@Column(name = "status_book_message")
	private String statusBookMessage;
	@Column(name = "description")
	private String description;

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

	@OneToMany(mappedBy = "statusBookDetail", fetch = FetchType.LAZY)
	private List<BookDetail> bookDetail;
}
