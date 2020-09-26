package com.dinz.library.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "author")
@Data
public class Author implements Serializable{

    @Id
    @Column(name = "id")
    private Integer id;
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
    @JoinTable(name = "author_book",
            joinColumns = @JoinColumn(name = "author_code", referencedColumnName = "author_code"),
            inverseJoinColumns = @JoinColumn(name = "isbn", referencedColumnName = "isbn"))
    private List<Book> books;

    @Column(name = "delete_status")
    private Integer deleteStatus;
}
