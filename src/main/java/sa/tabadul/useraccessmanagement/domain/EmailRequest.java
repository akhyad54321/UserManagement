package sa.tabadul.useraccessmanagement.domain;


import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailRequest {

    private String consumerId;
    private List<String> To;
    private String from;
    private String subject;
    private String replyTo;
    private String body;
    private boolean isHTML;
    private String charset;
    private List<String> attachments;
	@Override
	public String toString() {
		return "EmailRequest [consumerId=" + consumerId + ", To=" + To + ", from=" + from + ", subject=" + subject
				+ ", replyTo=" + replyTo + ", body=" + body + ", isHTML=" + isHTML + ", charset=" + charset
				+ ", attachments=" + attachments + "]";
	}
}
