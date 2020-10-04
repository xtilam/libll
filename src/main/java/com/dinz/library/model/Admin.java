package com.dinz.library.model;

import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "admin")
public class Admin implements Serializable {

	public static final Path PATH_AVARTAR_ADMIN_USER = Paths.get("uploadAdmin");
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "admin_code", unique = true, nullable = false)
	@PropertyName(name = "Mã người dùng")
	private String adminCode;

	@PropertyName(name = "Tên đầy đủ")
	@Column(name = "fullname", nullable = false)
	private String fullname;

	@PropertyName(name = "Số chứng minh thư")
	@Column(name = "identity_document", unique = true, nullable = false)
	private String identityDocument;

	@PropertyName(name = "Email")
	@Column(name = "email", unique = true, nullable = false)
	private String email;

	@PropertyName(name = "Mật khẩu")
	@Column(name = "password", nullable = false)
	private String password;

	@PropertyName(name = "Giới tính")
	@Column(name = "gender")
	private Integer gender;

	@PropertyName(name = "SĐT")
	@Column(name = "phone", nullable = false)
	private String phone;

	@PropertyName(name = "Địa chỉ")
	@Column(name = "address", nullable = false)
	private String address;

	@PropertyName(name = "Ngày sinh")
	@Column(name = "date_of_birth", nullable = false)
	private Date dateOfBirth;

	@Column(name = "create_by")
	private String createBy;

	@Column(name = "modified_by")
	private String modifiedBy;

	@Column(name = "delete_status")
	private Integer deleteStatus;

	@Column(name = "create_date")
	private Date createDate;

	@Column(name = "modified_date")
	private Date modifiedDate;

	@OneToMany(mappedBy = "admin")
	private List<PermissionAdmin> permissionAdmins;
	@OneToMany(mappedBy = "admin")
	private Set<GroupAdmin> groupAdmin;

	// Reader
	@OneToMany(mappedBy = "createBy")
	private List<Reader> readerCreateBy;
	@OneToMany(mappedBy = "modifiedBy")
	private List<Reader> readerModifiedBy;

	// Storage
	@OneToMany(mappedBy = "createBy")
	private List<Storage> storageCreateBy;
	@OneToMany(mappedBy = "modifiedBy")
	private List<Reader> storageModifiedBy;

	// StorageCategory
	@OneToMany(mappedBy = "createBy")
	private List<StorageCategory> storageCategoryCreateBy;
	@OneToMany(mappedBy = "modifiedBy")
	private List<StorageCategory> storageCategoryModifiedBy;

	// Role
	@OneToMany(mappedBy = "createBy")
	private List<Role> roleCreateBy;
	@OneToMany(mappedBy = "modifiedBy")
	private List<Role> roleModifiedBy;

	// Group
	@OneToMany(mappedBy = "createBy", fetch = FetchType.LAZY)
	private List<Group> groupCreateBy;
	@OneToMany(mappedBy = "modifiedBy", fetch = FetchType.LAZY)
	private List<Group> groupModifiedBy;

	// Permission
	@OneToMany(mappedBy = "createBy")
	private List<Permission> permissionCreateBy;
	@OneToMany(mappedBy = "modifiedBy")
	private List<Permission> permissionModifiedBy;
	// #endregion

	public void setAdminCode(String adminCode) {
		if (adminCode != null && IRegexPatternCheckData.CODE.matcher(adminCode).find()) {
			this.adminCode = adminCode;
		}
	}

	public void setGender(Integer gender) {
		if (null == gender || gender < 0 || gender > 2) {
			this.gender = 2;
		} else {
			this.gender = gender;
		}
	}

	public void setFullname(String fullname) {
		this.fullname = fullname == null ? null : (this.fullname = fullname.trim()).isEmpty() ? null : this.fullname;
	}

	public void setEmail(String email) {
		if (email != null && IRegexPatternCheckData.EMAIL.matcher(email).find()) {
			this.email = email;
		}
	}

	public void setPhone(String phone) {
		if (phone != null && IRegexPatternCheckData.NUMBER_STRING.matcher(phone).find()) {
			this.phone = phone;
		}
	}

	public void setIdentityDocument(String identityDocument) {
		if (identityDocument != null && IRegexPatternCheckData.NUMBER_STRING.matcher(identityDocument).find()) {
			this.identityDocument = identityDocument;
		}
	}

	public void setAddress(String address) {
		this.address = address == null ? null : (this.address = address.trim()).isEmpty() ? null : this.address;
	}

	public Admin(Long id, String adminCode) {
		this.adminCode = adminCode;
		this.id = id;
	}

	public void uploadImage(MultipartFile image) throws IOException {
		if (null != image && null != this.id) {
			// Kiểm tra file gửi lên có phải là file ảnh
			if (ImageIO.read(image.getInputStream()) != null) {
				Files.copy(image.getInputStream(), PATH_AVARTAR_ADMIN_USER.resolve(this.id.toString() + ".jpg"),
						StandardCopyOption.REPLACE_EXISTING);
			} else {
				throw new IOException("File not an image");
			}
		}
	}

}
