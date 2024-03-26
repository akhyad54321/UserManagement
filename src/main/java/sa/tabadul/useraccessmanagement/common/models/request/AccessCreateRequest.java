package sa.tabadul.useraccessmanagement.common.models.request;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccessCreateRequest {
	
	private String type;
	private String logic;
	private String decisionStrategy;
	private String name;
	private List<String> resources= new ArrayList<>();
	private List<String> scopes= new ArrayList<>();
	private List<String> policies= new ArrayList<>();


}
