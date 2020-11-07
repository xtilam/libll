package com.dinz.library.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "author")
@Getter
@Setter
public class Author extends BaseEntity {

	@Column(name = "name")
	private String name;
	@Column(name = "author_code", updatable = false)
	private String authorCode;
	@Column(name = "description")
	private String description;

	@JsonBackReference
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "author_book", joinColumns = @JoinColumn(name = "author_code", referencedColumnName = "author_code"), inverseJoinColumns = @JoinColumn(name = "isbn", referencedColumnName = "isbn"))
	private Set<Book> books;
}
