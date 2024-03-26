package sa.tabadul.useraccessmanagement.common.models.response;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import sa.tabadul.useraccessmanagement.domain.CodeMaster;




@Data
public class CodeMasterResponse {
	private int statusCode;
	private String statusMessage;
	private List<CodeMaster> data = new ArrayList<>();
	

}