package sa.tabadul.useraccessmanagement.common.models.request;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCreationKeyCloak {
	

	
	public String username;
	public String firstName;
	public String lastName;
	public String email;
	public Boolean emailVerified;
	public Boolean enabled;
	public List<String> groups = new ArrayList<>();
	public List<UserCredential> credentials = new ArrayList<>();
	
	

}
