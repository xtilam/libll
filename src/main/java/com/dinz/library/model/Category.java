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
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.Table;

import org.hibernate.annotations.NaturalId;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.base.Objects;

import lombok.Data;

@Entity
@Table(name = "category")
@Data
@NamedEntityGraph(name = "Boook.categories", attributeNodes = @NamedAttributeNode("books"))
public class Category implements Serializable {

	private static final long serialVersionUID = 3408945552412725731L;
	@Id
	@Column(name = "id")
	private Long id;
	@JsonIgnore
	@Column(name = "delete_status")
	private Integer deleteStatus;
	@NaturalId
	@Column(name = "category_code")
	private String categoryCode;
	@Column(name = "category")
	private String category;
	@Column(name = "create_date")
	private Date createDate;
	@Column(name = "modified_Date")
	private Date modifiedDate;

	@Column(name = "create_by")
	private String createBy;
	@Column(name = "modified_by")
	private String modifiedBy;

	@JsonIgnore
	@JsonIgnoreProperties("categories")
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "category_book", joinColumns = @JoinColumn(name = "category_code", referencedColumnName = "category_code"), inverseJoinColumns = @JoinColumn(name = "isbn", referencedColumnName = "isbn"))
	private Set<Book> books;

	@Override
	public boolean equals(Object obj) {
		return obj instanceof Category ? Objects.equal(this.id, ((Category) obj).id) : false;
	}

	@Override
	public int hashCode() {
		return this.id instanceof Long ? this.id.hashCode() : 0;
	}
}
