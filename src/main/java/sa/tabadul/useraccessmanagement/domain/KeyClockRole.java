package sa.tabadul.useraccessmanagement.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.Data;

@Data
@Entity
@Table(name = "KEYCLOAK_ROLE", schema = "dbo")
public class KeyClockRole {

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	@Column(name = "CLIENT_REALM_CONSTRAINT")
	private String clientRealmConstraint;

	@NotNull
	@Column(name = "CLIENT_ROLE")
	private Boolean clientRole;

	@Column(name = "DESCRIPTION")
	private String Description;

	@Column(name = "NAME")
	private String name;

	@Column(name = "REALM_ID")
	private String realmId;

	@Column(name = "CLIENT")
	private String client;

	@Column(name = "REALM")
	private String realm;
	
	@OneToMany(mappedBy = "keyClockRole", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JsonManagedReference
	private List<RoleAttribute> roleAttributes;
}
