package sa.tabadul.useraccessmanagement.common.service;



import org.springframework.stereotype.Service;

import sa.tabadul.useraccessmanagement.common.constants.CommonCodes;
import sa.tabadul.useraccessmanagement.common.enums.ExceptionMessage;


@Service
public class GetStatusNameService {
	
	public String getStatusName(Integer id) {

		if (id == 1) {
			return CommonCodes.SUBMITTED;
		} else if (id == 3) {
			return CommonCodes.APPROVED;
		} else if (id == 4) {
			return CommonCodes.REJECTED;
		} else {
			return ExceptionMessage.UNKOWN.getValue();
		}
	}
	
}
