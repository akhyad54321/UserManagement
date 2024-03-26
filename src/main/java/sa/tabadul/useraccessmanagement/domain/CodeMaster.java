package sa.tabadul.useraccessmanagement.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "[MastersLookup].[MASTER].[MST_CODE]")
public class CodeMaster {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@NotNull
	@Column(name = "KEY1")
	private String key1;

	@Column(name = "KEY2")
	private String key2;

	@NotNull
	@Column(name = "CODE", unique = true)
	private String code;

	@NotNull
	@Column(name = "CODE_DESCRIPTION_ENGLISH", unique = true)
	private String codeDescriptionEnglish = "-";

	@NotNull
	@Column(name = "CODE_DESCRIPTION_ARABIC", unique = true)
	private String codeDescriptionArabic = "-";

	@Column(name = "PARENT_ID")
	private Integer parentId;

	@NotNull
	@Column(name = "SEQUENCE")
	private Integer sequence;

	@Column(name = "APP_ID")
	private Integer appId;

	@Column(name = "IS_ACTIVE")
	private Boolean is_active;

	@Column(name = "REMARK")
	private String remark;

	@Column(name = "CREATED_BY")
	private String createdBy;

	@Column(name = "UPDATED_BY")
	private String updatedBy;
}