package com.dinz.library.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "book_detail")
public class BookDetail {

    @Id
    @Column(name = "id")
    private String id;
    @Column(name = "description")
    private String description;

    @ManyToOne
    @JoinColumn(name = "status_book_code", referencedColumnName = "status_book_code")
    private StatusBookDetail statusBookDetail;

    @ManyToOne
    @JoinColumn(name = "storage_code", referencedColumnName = "storage_code")
    private Storage storage;
    @ManyToOne
    @JoinColumn(name = "isbn", referencedColumnName = "isbn")
    private Book book;

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
}
