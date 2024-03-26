package sa.tabadul.useraccessmanagement.common.models.request;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CodeMasterRequest {

	public List<String> key1 = new ArrayList<>();
	public List<Integer> ids = new ArrayList<>();
	
}
