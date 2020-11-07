package com.dinz.library.api.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.dinz.library.common.APIResult;
import com.dinz.library.common.APIResultMessage;
import com.dinz.library.dto.IdsDTO;
import com.dinz.library.dto.PageDTO;
import com.dinz.library.model.Author;
import com.dinz.library.service.AuthorService;

@RestController
@RequestMapping(value = "api/admin/book")
public class AuthorAPI {
	@Autowired
	AuthorService authorService;

	@GetMapping(value = "/authors")
	public APIResult getAllAuthor(PageDTO dto) {
		if (!dto.isUnlimited()) {
			Page<Author> result;
			if (dto.getPage() >= 0) {
				result = this.authorService.findAll(PageRequest.of(dto.getPage(), dto.getLimit()));
			} else {
				result = this.authorService.findLastPage(dto.getLimit());
			}
			return APIUtils.getAPIResultPage(result);
		} else {
			return new APIResult(APIResultMessage.SUCCESS, this.authorService.findAll());
		}
	}

	@PostMapping(value = "/author")
	public APIResult insertCategory(@RequestBody Author author) {
		this.authorService.insert(author);
		return new APIResult(APIResultMessage.INSERT_SUCCESS);
	}

	@GetMapping(value = "/author")
	public APIResult findAuthor(@RequestParam("id") Long id) {
		return new APIResult(APIResultMessage.SUCCESS, authorService.findById(id));
	}

	@PutMapping(value = "/author")
	public APIResult updateCategory(@RequestBody Author author) {
		this.authorService.update(author);
		return new APIResult(APIResultMessage.UPDATE_SUCCESS);
	}

	@DeleteMapping(value = "/authors")
	public APIResult deleteCategories(@RequestBody IdsDTO idsDTO) {
		return APIUtils.deleteIds((id) -> {
			try {
				this.authorService.delete(id);
				return 1;
			} catch (Exception e) {
				return 0;
			}
		}, idsDTO.getIds());
	}
}
