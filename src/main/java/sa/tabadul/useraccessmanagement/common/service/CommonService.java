package sa.tabadul.useraccessmanagement.common.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.LogManager;
import java.util.stream.Collectors;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;

import sa.tabadul.useraccessmanagement.common.constants.CommonCodes;
import sa.tabadul.useraccessmanagement.common.models.request.PortPermitListRequest;
import sa.tabadul.useraccessmanagement.common.models.response.PortPermitListResponse;
import sa.tabadul.useraccessmanagement.domain.EmailRequest;
import sa.tabadul.useraccessmanagement.domain.EmailTemplate;
import sa.tabadul.useraccessmanagement.domain.NotificationResponse;
import sa.tabadul.useraccessmanagement.domain.SmsRequest;
import sa.tabadul.useraccessmanagement.domain.SmsTemplate;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class CommonService {
	@Value("${SmsUrl}")
	private String SmsUrl;
	
	@Value("${emailUrl}")
	private String emailUrl;

	@Autowired
	private ObjectMapper objectMapper;

	public List<PortPermitListResponse> paginatePortPermit(List<PortPermitListResponse> lstPortPermit,
			PortPermitListRequest objPortPermitListRequest) {
		if (!"".equals(objPortPermitListRequest.getSearch()) && objPortPermitListRequest.getSearch() != null) {

			lstPortPermit = lstPortPermit.stream()
					.filter(obj -> (Long.toString(obj.getRequestNo())).contains(objPortPermitListRequest.getSearch())

							|| (obj.getStatus() != null && obj.getStatus().toLowerCase()
									.contains(objPortPermitListRequest.getSearch().toLowerCase()))

							|| (obj.getOrgName() != null && obj.getOrgName().toLowerCase()
									.contains(objPortPermitListRequest.getSearch().toLowerCase()))

							|| (obj.getLicenceNo() != null && obj.getLicenceNo().toLowerCase()
									.contains(objPortPermitListRequest.getSearch().toLowerCase()))

							|| (obj.getRequestTypeEnglish() != null && obj.getRequestTypeEnglish().toLowerCase()
									.contains(objPortPermitListRequest.getSearch().toLowerCase()))

							|| (obj.getRequestTypeArabic() != null && obj.getRequestTypeArabic().toLowerCase()
									.contains(objPortPermitListRequest.getSearch().toLowerCase()))

							|| (obj.getRequestStatusEnglish() != null && obj.getRequestStatusEnglish().toLowerCase()
									.contains(objPortPermitListRequest.getSearch().toLowerCase()))

							|| (obj.getRequestStatusArabic() != null && obj.getRequestStatusArabic().toLowerCase()
									.contains(objPortPermitListRequest.getSearch().toLowerCase()))

							|| (Long.toString(obj.getEunn())).contains(objPortPermitListRequest.getSearch())

							|| (obj.getPortOfSubmission() != null && obj.getPortOfSubmission().toLowerCase()
									.contains(objPortPermitListRequest.getSearch().toLowerCase()))

					)

					.collect(Collectors.toList());
		}
		String sort = objPortPermitListRequest.getSort();

		if (objPortPermitListRequest.getSortDir().equalsIgnoreCase(CommonCodes.ASCENDING)) {

			if (sort.equals(CommonCodes.REQUEST_NO)) {
				lstPortPermit = lstPortPermit.stream()
						.sorted(Comparator.comparingLong(PortPermitListResponse::getRequestNo))
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.ORG_NAME)) {
				lstPortPermit = lstPortPermit.stream().sorted(Comparator.comparing(PortPermitListResponse::getOrgName))
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.PORT_OF_SUBMISSION)) {
				lstPortPermit = lstPortPermit.stream()
						.sorted(Comparator.comparing(PortPermitListResponse::getPortOfSubmission))
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.STATUS)) {
				lstPortPermit = lstPortPermit.stream().sorted(Comparator.comparing(PortPermitListResponse::getStatus))
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.USER_ID)) {
				lstPortPermit = lstPortPermit.stream().sorted(Comparator.comparing(PortPermitListResponse::getUserId))
						.collect(Collectors.toList());

			} else if (sort.equals(CommonCodes.REQUEST_DATE)) {
				lstPortPermit = lstPortPermit.stream()
						.sorted(Comparator.comparing(PortPermitListResponse::getRequestDate))
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.EUNN)) {
				lstPortPermit = lstPortPermit.stream().sorted(Comparator.comparingLong(PortPermitListResponse::getEunn))
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.REQUEST_TYPE_ENGLISH)) {
				lstPortPermit = lstPortPermit.stream()
						.sorted(Comparator.comparing(PortPermitListResponse::getRequestTypeEnglish))
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.REQUEST_TYPE_ARABIC)) {
				lstPortPermit = lstPortPermit.stream()
						.sorted(Comparator.comparing(PortPermitListResponse::getRequestTypeArabic))
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.REQUEST_STATUS_ENGLISH)) {
				lstPortPermit = lstPortPermit.stream()
						.sorted(Comparator.comparing(PortPermitListResponse::getRequestStatusEnglish))
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.REQUEST_STSTUS_ARABIC)) {
				lstPortPermit = lstPortPermit.stream()
						.sorted(Comparator.comparing(PortPermitListResponse::getRequestStatusArabic))
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.UPDATED_DATE)) {
				lstPortPermit = lstPortPermit.stream()
						.sorted(Comparator.comparing(PortPermitListResponse::getUpdatedDate))
						.collect(Collectors.toList());
			}

		} else {
			if (sort.equals(CommonCodes.REQUEST_NO)) {
				lstPortPermit = lstPortPermit.stream()
						.sorted(Comparator.comparingLong(PortPermitListResponse::getRequestNo).reversed())
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.ORG_NAME)) {
				lstPortPermit = lstPortPermit.stream()
						.sorted(Comparator.comparing(PortPermitListResponse::getOrgName).reversed())
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.PORT_OF_SUBMISSION)) {
				lstPortPermit = lstPortPermit.stream()
						.sorted(Comparator.comparing(PortPermitListResponse::getPortOfSubmission).reversed())
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.STATUS)) {
				lstPortPermit = lstPortPermit.stream()
						.sorted(Comparator.comparing(PortPermitListResponse::getStatus).reversed())
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.USER_ID)) {
				lstPortPermit = lstPortPermit.stream()
						.sorted(Comparator.comparing(PortPermitListResponse::getUserId).reversed())
						.collect(Collectors.toList());

			} else if (sort.equals(CommonCodes.REQUEST_DATE)) {
				lstPortPermit = lstPortPermit.stream()
						.sorted(Comparator.comparing(PortPermitListResponse::getRequestDate).reversed())
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.EUNN)) {
				lstPortPermit = lstPortPermit.stream()
						.sorted(Comparator.comparingLong(PortPermitListResponse::getEunn).reversed())
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.REQUEST_TYPE_ENGLISH)) {
				lstPortPermit = lstPortPermit.stream()
						.sorted(Comparator.comparing(PortPermitListResponse::getRequestTypeEnglish).reversed())
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.REQUEST_TYPE_ARABIC)) {
				lstPortPermit = lstPortPermit.stream()
						.sorted(Comparator.comparing(PortPermitListResponse::getRequestTypeArabic).reversed())
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.REQUEST_STATUS_ENGLISH)) {
				lstPortPermit = lstPortPermit.stream()
						.sorted(Comparator.comparing(PortPermitListResponse::getRequestStatusEnglish).reversed())
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.REQUEST_STSTUS_ARABIC)) {
				lstPortPermit = lstPortPermit.stream()
						.sorted(Comparator.comparing(PortPermitListResponse::getRequestStatusArabic).reversed())
						.collect(Collectors.toList());
			} else if (sort.equals(CommonCodes.UPDATED_DATE)) {
				lstPortPermit = lstPortPermit.stream()
						.sorted(Comparator.comparing(PortPermitListResponse::getUpdatedDate).reversed())
						.collect(Collectors.toList());
			}

		}

		return lstPortPermit;
	}

	public String sortKeys(String key) {
		switch (key) {
		case CommonCodes.REQUEST_NO:

			return CommonCodes.CRN;

		case CommonCodes.ORG_NAME:

			return CommonCodes.USER_ORGID;

		case CommonCodes.UPDATED_DATE:

			return CommonCodes.UPDATED_DATE;

		case CommonCodes.USER_ID:

			return CommonCodes.CRN;

		case CommonCodes.STATUS:

			return CommonCodes.APPROVAL_STATUS;

		default:
			return CommonCodes.ID;
		}
	}

	public String SendSms(SmsTemplate smsTemplate) {
		SmsRequest request = new SmsRequest();
		request.setConsumerId("test-test-001");
		List<String> mobileNumbers = new ArrayList<>();
		mobileNumbers.add(smsTemplate.getMobile());
		request.setRecepientNumbers(mobileNumbers);
		request.setSenderTag("Fasah-Ops");

		String body = smsTemplate.getSmsEng() + smsTemplate.getSmsArb();
		request.setMessage(body);

		Map<String, Object> map = new HashMap<>();
		map.put("consumerId", request.getConsumerId());
		map.put("senderTag", request.getSenderTag());
		map.put("recepientNumbers", request.getRecepientNumbers());
		map.put("message", request.getMessage());

		String requestBody = null;
		try {
			objectMapper.setSerializationInclusion(Include.NON_EMPTY);
			requestBody = objectMapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			// log.error(e.getMessage());
		}
		String responseString = notifySms(requestBody);
		return responseString;
		// log.info("Sms Details For: " + smsTemplate.getMobile() + " " +
		// responseString);
	}

	private String notifySms(String requestBody) {

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Tabadul-Client-Id", "630668f5e10691788a93b0be037b8dc2");
		headers.set("X-Tabadul-Client-Secret", "39a3f40010704cd05294bb3d83ae1f64");
		headers.set("Authorization",
				"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjMDY5NyIsIkdST1VQUyAiOiJBQSxISElBR1UsUExBQkdVU0VSLFdCQkxRUlksV0JJQUdTLFdCSUFHVSxXQlBDR1UsTEFCT1JBVE9SWSxTSElQSU5HX0FHRU5ULFBPUlRfQ09OVFJBQ1RPUiIsIkdST1VQUyI6IkFBLEhISUFHVSxQTEFCR1VTRVIsV0JCTFFSWSxXQklBR1MsV0JJQUdVLFdCUENHVSxMQUJPUkFUT1JZLFNISVBJTkdfQUdFTlQsUE9SVF9DT05UUkFDVE9SIiwiSVNfU1NPICI6ZmFsc2UsIlNTT19UT0tFTiAiOiIiLCJJU19TU08iOmZhbHNlLCJTU09fVE9LRU4iOiIiLCJDTElFTlRfTkFNRSI6IkZBU0FIIiwiaXNzIjoiRkFTQUgiLCJhdWQiOiJGQVNBSCBBcHBsaWNhdGlvbiIsImV4cCI6MTY5ODQxODQ0OX0.hjwpxumXq75aiLFeewTMuEeH-e9F7NJIsVKOcQ8GvyWhuxpgwNm8ZP2yLMK_MBEE5mwZJrx8eQzA7DgHnRd5tA");
		headers.set("Accept-Language", "en");
		headers.set("transactionId", "1132398");
		headers.set("Cookie", "dbe37801645c0242003c4c9c46f80586=1e8d844889d450f11e11d5736cc6b562");
		headers.set("Accept", "*/*");

		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

		StringBuilder response = new StringBuilder();
		int maxRetries = 2;
		int retryCount = 0;
		// log.debug("----------");

		while (retryCount < maxRetries) {
			try {
				ResponseEntity<NotificationResponse> responseEntity = restTemplate.exchange(SmsUrl, HttpMethod.POST,
						requestEntity, NotificationResponse.class);

				response.append(responseEntity.getBody());

//				log.info(
//						"{}\r\n Request Method: POST\r\n, Request URL: {}\r\n, Request Headers: {}\r\n, Response Code: {}\r\n, Response: {}\r\n{}\r\n",
//						"--------", SmsUrl, responseEntity.getHeaders(), responseEntity.getStatusCodeValue(),
//						response, "--------");

				break;

			} catch (Exception e) {
				retryCount++;

				if (retryCount == maxRetries) {
					// log.debug("retries ended");
					return null;
				}
			}
		}

		return "OTP sent successfully";
	}
	
	public String sendEmail(EmailTemplate emailTemplate) {

		EmailRequest request = new EmailRequest();
		request.setConsumerId("a49c22bf-cf28-44a9-aab5-773a8eaeb505");
		List<String> mailsList = new ArrayList<>();
		mailsList.add(emailTemplate.getEmail());
		request.setTo(mailsList);
		request.setFrom("PC-CARE@fasah.sa");
		request.setSubject(emailTemplate.getEmailHeaderEng() + " | " + emailTemplate.getEmailHeaderArb());
		request.setReplyTo("NA");
		String body = "<p>" + emailTemplate.getEmailBodyEng() + "</p><p dir='rtl' lang='ar'>"
				+ emailTemplate.getEmailBodyArb() + "</p>";
		request.setBody(body);
		request.setHTML(true);
		request.setCharset("NA");
		List<String> attachments = new ArrayList<>();
		request.setAttachments(attachments);

		Map<String, Object> map = new HashMap<>();
		map.put("consumerId", request.getConsumerId());
		map.put("To", request.getTo());
		map.put("from", request.getFrom());
		map.put("subject", request.getSubject());
		map.put("replyTo", request.getReplyTo());
		map.put("body", request.getBody());
		map.put("isHTML", request.isHTML());
		map.put("charset", request.getCharset());
		map.put("attachments", request.getAttachments());

		String requestBody = null;
		try {
			objectMapper.setSerializationInclusion(Include.NON_EMPTY);
			requestBody = objectMapper.writeValueAsString(map);
		} catch (JsonProcessingException e) {
			//log.error(e.getMessage());
		}
		String responseString = notifyEmail(requestBody);
		//log.info("Mail Details For: " + emailTemplate.getEmail() + " " + responseString);
		
		return responseString;
	}

	private String notifyEmail(String requestBody) {

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-Tabadul-Client-Id", "630668f5e10691788a93b0be037b8dc2");
		headers.set("X-Tabadul-Client-Secret", "39a3f40010704cd05294bb3d83ae1f64");
		headers.set("Authorization",
				"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJjMDY5NyIsIkdST1VQUyAiOiJBQSxISElBR1UsUExBQkdVU0VSLFdCQkxRUlksV0JJQUdTLFdCSUFHVSxXQlBDR1UsTEFCT1JBVE9SWSxTSElQSU5HX0FHRU5ULFBPUlRfQ09OVFJBQ1RPUiIsIkdST1VQUyI6IkFBLEhISUFHVSxQTEFCR1VTRVIsV0JCTFFSWSxXQklBR1MsV0JJQUdVLFdCUENHVSxMQUJPUkFUT1JZLFNISVBJTkdfQUdFTlQsUE9SVF9DT05UUkFDVE9SIiwiSVNfU1NPICI6ZmFsc2UsIlNTT19UT0tFTiAiOiIiLCJJU19TU08iOmZhbHNlLCJTU09fVE9LRU4iOiIiLCJDTElFTlRfTkFNRSI6IkZBU0FIIiwiaXNzIjoiRkFTQUgiLCJhdWQiOiJGQVNBSCBBcHBsaWNhdGlvbiIsImV4cCI6MTY5ODQxODQ0OX0.hjwpxumXq75aiLFeewTMuEeH-e9F7NJIsVKOcQ8GvyWhuxpgwNm8ZP2yLMK_MBEE5mwZJrx8eQzA7DgHnRd5tA");
		headers.set("Accept-Language", "en");
		headers.set("transactionId", "11323");
		headers.set("Cookie", "dbe37801645c0242003c4c9c46f80586=1e8d844889d450f11e11d5736cc6b562");
		headers.set("Accept", "*/*");

		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

		StringBuilder response = new StringBuilder();
		int maxRetries = 2;
		int retryCount = 0;
		//log.debug("----------");

		while (retryCount < maxRetries) {
			try {
				ResponseEntity<NotificationResponse> responseEntity = restTemplate.exchange(emailUrl, HttpMethod.POST,
						requestEntity, NotificationResponse.class);

				response.append(responseEntity.getBody());

				/*
				 * log.info(
				 * "{}\r\n Request Method: POST\r\n, Request URL: {}\r\n, Request Headers: {}\r\n, Response Code: {}\r\n, Response: {}\r\n{}\r\n"
				 * , "--------", EmailUrl, responseEntity.getHeaders(),
				 * responseEntity.getStatusCodeValue(), response, "--------");
				 */

				break;

			} catch (Exception e) {
//			log.debug(String.format(Constant.LOG_MESSAGE_RETRY_URL, EmailUrl, retryCount + 1));
				retryCount++;

				if (retryCount == maxRetries) {
//				log.debug(String.format(Constant.LOG_MESSAGE_MAX_TRY, retryCount));
					//log.debug("retries ended");
					return null;
				}
			}
		}

		return "Email sent successfully";
	}


}
