package com.dinz.library.model;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.common.base.Objects;

import lombok.Data;

@Entity
@Table(name = "author")
@Data
public class Author implements Serializable {

	private static final long serialVersionUID = 539524071631557569L;
	@Id
	@Column(name = "id")
	private Long id;
	@Column(name = "name")
	private String name;
	@Column(name = "author_code")
	private String authorCode;
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

	@JsonBackReference
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "author_book", joinColumns = @JoinColumn(name = "author_code", referencedColumnName = "author_code"), inverseJoinColumns = @JoinColumn(name = "isbn", referencedColumnName = "isbn"))
	private Set<Book> books;

	@Column(name = "delete_status")
	private Integer deleteStatus;

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Author ? Objects.equal(this.id, ((Author) obj).id) : false;
	}

	@Override
	public int hashCode() {
		return this.id instanceof Long ? this.id.hashCode() : 0;
	}
}
