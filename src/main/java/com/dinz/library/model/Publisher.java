package com.dinz.library.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "publisher")
@Getter
@Setter
public class Publisher extends BaseEntity {

    @Column(name = "publisher_code", updatable = false)
    private String publisherCode;
    @Column(name = "name")
    private String name;

    @JsonBackReference
    @OneToMany(mappedBy = "publisher", fetch = javax.persistence.FetchType.LAZY)
    private List<Book> books;
}
