package com.dinz.library.dto;

import com.dinz.library.model.Group;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class GroupDTO {

	private Long id;
	private String groupCode;
	private String description;
	private String name;

	public Group toGroup() {
		Group group = new Group();
		group.setId(id);
		group.setGroupCode(groupCode);
		group.setDescription(description);
		group.setName(name);
		return group;
	}
}
