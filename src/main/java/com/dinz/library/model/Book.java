package com.dinz.library.model;

import com.dinz.library.common.updatemtm.ExcludeConditionMTM;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.web.multipart.MultipartFile;

import com.dinz.library.exception.UploadImageException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "book")
@NoArgsConstructor
@Getter
@Setter
@ExcludeConditionMTM
public class Book extends BaseEntity {

    public static final Path PATH_BOOK_COVER = Paths.get("uploads/book");

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
    @JsonIgnore
    @Column(name = "rate", updatable = false)
    private Long rate = 0L;
    @Column(name = "rate_count", updatable = false)
    private Integer rateCount = 0;
    @PropertyName(name = "ISBN")
    @Column(name = "isbn", updatable = false)
    private Long isbn;
    @Column(name = "quantity")
    private Integer quantity = 0;

    @JsonIgnoreProperties("books")
    @ManyToMany(mappedBy = "books", fetch = FetchType.LAZY)
    private Set<Category> categories;
    @JsonIgnoreProperties("books")
    @ManyToMany(mappedBy = "books", fetch = FetchType.LAZY)
    private Set<Author> authors;
    @JsonIgnore
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY)
    private List<BookDetail> bookDetails;

    public void uploadImage(MultipartFile image) {
        try {
            if (ImageIO.read(image.getInputStream()) != null) {
                Files.copy(image.getInputStream(), PATH_BOOK_COVER.resolve(this.id.toString() + ".jpg"),
                        StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new UploadImageException();
        }
    }

    @JsonProperty("rate")
    public double getRateAVG() {
        if (this.rateCount == 0) return 0;
        return this.rate / this.rateCount;
    }
}
