package sa.tabadul.useraccessmanagement.common.models.response;

import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KeyCloakUserDetails {

	private String id;
	private Long createdTimestamp;
	private String username;
	private Boolean enabled;
	private Boolean totp;
	private Boolean emailVerified;
	private String firstName;
	private String lastName;
	private String email;
	private Integer notBefore;
	private List<Object> disableableCredentialTypes;
	private List<Object> requiredActions;
	private Object access;
	private Object attributes;

}
