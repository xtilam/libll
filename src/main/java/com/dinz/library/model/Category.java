package com.dinz.library.model;

import com.dinz.library.common.updatemtm.ExcludeConditionMTM;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "category")
@Getter
@Setter
@ExcludeConditionMTM(condition = "deleteStatus = 0")
public class Category extends BaseEntity {

    @Column(name = "category_code", updatable = false)
    private String categoryCode;
    @Column(name = "category")
    private String category;

    @JsonIgnore
    @JsonIgnoreProperties("categories")
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "category_book", joinColumns = @JoinColumn(name = "category_code", referencedColumnName = "category_code"), inverseJoinColumns = @JoinColumn(name = "isbn", referencedColumnName = "isbn"))
    private Set<Book> books;

}
