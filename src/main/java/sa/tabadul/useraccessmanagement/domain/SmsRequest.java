package sa.tabadul.useraccessmanagement.domain;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmsRequest {
	private String consumerId;
	private String senderTag;
	private List<String> recepientNumbers;
	private String message;
}
