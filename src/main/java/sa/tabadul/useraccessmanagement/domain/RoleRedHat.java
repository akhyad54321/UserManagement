package sa.tabadul.useraccessmanagement.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RoleRedHat {

	private String id;
	private String name;
	private String description;
	private Boolean composite;
	private Boolean clientRole;
	private String containerId;
	private Attributes attributes;
	
}
