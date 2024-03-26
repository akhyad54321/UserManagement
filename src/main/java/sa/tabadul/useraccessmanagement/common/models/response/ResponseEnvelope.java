package sa.tabadul.useraccessmanagement.common.models.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseEnvelope<T> {

	private Integer responseCode;
	private String responseMessage;
	private T data;
}
