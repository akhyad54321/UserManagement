package sa.tabadul.useraccessmanagement.common.models.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginationResponse {
	
	private Long recordsTotal;
    private Long recordsFiltered;
    private Object data;
	
   

}
