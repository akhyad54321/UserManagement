package sa.tabadul.useraccessmanagement.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="RESOURCE_SERVER_SCOPE",schema="dbo")
public class ResourceServerScope {
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
	@Column(name = "NAME")
	private String name;
	
	@Column(name = "ICON_URI")
	private String iconUri;
	
	@Column(name = "RESOURCE_SERVER_ID")
	private String resourceServerId;
	
	@Column(name = "DISPLAY_NAME")
	private String displayName;

}
