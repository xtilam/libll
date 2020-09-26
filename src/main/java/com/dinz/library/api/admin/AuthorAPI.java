package com.dinz.library.api.admin;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dinz.library.common.APIResult;
import com.dinz.library.common.APIResultMessage;
import com.dinz.library.dto.PageDTO;
import com.dinz.library.model.Author;
import com.dinz.library.service.AuthorService;

@RestController
@RequestMapping(value = "api/admin")
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
}
