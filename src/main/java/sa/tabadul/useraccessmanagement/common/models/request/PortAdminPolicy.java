package sa.tabadul.useraccessmanagement.common.models.request;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PortAdminPolicy {
	
	private String type;
    private String logic;
    private String decisionStrategy;
    private String name;
    private List<PolicyRole> roles;

}
