package com.dinz.library.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnoreType;
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
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.NaturalId;

@Entity
@Table(name = "category")
@Data
public class Category implements Serializable {

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
    
    @JsonBackReference
    @JsonIgnoreProperties("categories")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "category_book",
            joinColumns = @JoinColumn(name = "category_code", referencedColumnName = "category_code"),
            inverseJoinColumns = @JoinColumn(name = "isbn", referencedColumnName = "isbn"))
    private List<Book> books;
}
