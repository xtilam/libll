/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dinz.library.api.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.dinz.library.common.APIResult;
import com.dinz.library.common.APIResultMessage;
import com.dinz.library.dto.PageDTO;
import com.dinz.library.exception.NotFoundEntityException;
import com.dinz.library.model.Book;
import com.dinz.library.service.BookService;

/**
 *
 * @author DinzeniLL
 */
@RestController
@RequestMapping(value = "/api/admin")
public class BookAPI {

	@Autowired
	BookService bookService;

	@GetMapping(value = "/books")
	public APIResult findBooks(PageDTO dto) {
		if (!dto.isUnlimited()) {
			Page<Book> result;
			if (dto.getPage() >= 0) {
				result = this.bookService.findBooks(PageRequest.of(dto.getPage(), dto.getLimit()));
			} else {
				result = this.bookService.findBooksLastPage(dto.getLimit());
			}
			return APIUtils.getAPIResultPage(result);
		} else {
			return new APIResult(APIResultMessage.SUCCESS, bookService.findBooks());
		}

	}

	@PostMapping(value = "/book")
	public APIResult insertBook(@RequestBody Book book) {
		this.bookService.insert(book);
		return new APIResult(APIResultMessage.SUCCESS, book);
	}

	@PostMapping(value = "/upload-image")
	public APIResult uploadImageBook(@RequestParam("id") Long id, @RequestPart MultipartFile image) {
		Book book = bookService.findById(id);
		if (null != book) {
			book.uploadImage(image);
			return new APIResult(APIResultMessage.UPDATE_SUCCESS, null);
		} else {
			throw new NotFoundEntityException();
		}
	}

}
