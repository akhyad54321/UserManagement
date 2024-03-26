package sa.tabadul.useraccessmanagement.common.models.request;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class UserByIdsRequest {
	
	public List<String> iAmUserIds = new ArrayList<>();

}
