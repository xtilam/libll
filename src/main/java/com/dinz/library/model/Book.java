package com.dinz.library.model;

import java.io.Serializable;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "book")
@NoArgsConstructor
@Data
public class Book implements Serializable {
	public static final Path PATH_BOOK_COVER = Paths.get("uploads/book");
	@Id
	@Column(name = "id")
	private Long id;
	@Column(name = "title")
	private String title;
	@Column(name = "edition")
	private Integer edition;
	@Column(name = "price")
	private float price;
	@Column(name = "page_number")
	private Integer pageNumber;
	@Column(name = "description")
	private String description;

	@JsonIgnoreProperties("books")
	@ManyToOne
	@JoinColumn(name = "publisher_code", referencedColumnName = "publisher_code")
	private Publisher publisher;
	@Column(name = "rate")
	private Long rate;
	@Column(name = "rate_count")
	private Integer rateCount;
	@Column(name = "isbn")
	private Integer isbn;
	@Column(name = "quantity")
	private Integer quantity;
	@Column(name = "create_date")
	private Date createDate;
	@Column(name = "modified_date")
	private Date modifiedDate;
	@JsonIgnore
	@Column(name = "delete_status")
	private Integer deleteStatus;
	@Column(name = "create_by")
	private String createBy;
	@Column(name = "modified_by")
	private String modifiedBy;

	@JsonIgnoreProperties("books")
	@ManyToMany(mappedBy = "books", fetch = FetchType.LAZY)
	private List<Category> categories;
	@JsonIgnoreProperties("books")
	@ManyToMany(mappedBy = "books", fetch = FetchType.LAZY)
	private List<Author> authors;
	@JsonBackReference
	@OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
	private List<BookDetail> bookDetails;
}
