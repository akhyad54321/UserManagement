package sa.tabadul.useraccessmanagement.common.service;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import lombok.extern.log4j.Log4j2;
import sa.tabadul.useraccessmanagement.common.configs.PropertiesConfig;
import sa.tabadul.useraccessmanagement.common.constants.CommonCodes;
import sa.tabadul.useraccessmanagement.common.models.request.AccessCreateRequest;
import sa.tabadul.useraccessmanagement.common.models.request.Body;
import sa.tabadul.useraccessmanagement.common.models.request.CodeMasterRequest;
import sa.tabadul.useraccessmanagement.common.models.request.CreateInvoiceRequest;
import sa.tabadul.useraccessmanagement.common.models.request.Envelope;
import sa.tabadul.useraccessmanagement.common.models.request.GetStatusBody;
import sa.tabadul.useraccessmanagement.common.models.request.GetStatusEnvelope;
import sa.tabadul.useraccessmanagement.common.models.request.GetStatusRequest;
import sa.tabadul.useraccessmanagement.common.models.request.InvoiceCreateRequest;
import sa.tabadul.useraccessmanagement.common.models.request.PortAdminPolicy;
import sa.tabadul.useraccessmanagement.common.models.request.RedHatKeycloakRole;
import sa.tabadul.useraccessmanagement.common.models.request.SoapEnvelope;
import sa.tabadul.useraccessmanagement.common.models.response.CodeMasterResponse;
import sa.tabadul.useraccessmanagement.common.models.response.KeyCloakUserDetails;
import sa.tabadul.useraccessmanagement.common.models.response.MCIServiceGetByEUNResposne;
import sa.tabadul.useraccessmanagement.common.models.response.OwnersListResponse;
import sa.tabadul.useraccessmanagement.common.models.response.PortMasterResponse;
import sa.tabadul.useraccessmanagement.domain.CodeMaster;
import sa.tabadul.useraccessmanagement.domain.PortMaster;
import sa.tabadul.useraccessmanagement.domain.RoleRedHat;
import sa.tabadul.useraccessmanagement.exception.BusinessException;

@Log4j2
@Service
public class UserManagementExternalService {

	ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
			.registerModule(new JavaTimeModule());

	@Autowired
	PropertiesConfig propertiesConfig;

	public String getPort(Integer id) {

		String apiUrl = propertiesConfig.getBaseUrl() + propertiesConfig.getPortById() + id;

		// Create a RestTemplate with SSL support
		RestTemplate restTemplate = new RestTemplate();

		// Set headers, including the Authorization header with the token
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// Create a HttpEntity with headers and request body
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		try {
			ResponseEntity<String> response;

			response = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, String.class);
			if (response.getStatusCode().equals(HttpStatus.OK)) {
				return response.getBody();
			} else {
				return null;
			}

		} catch (HttpClientErrorException e) {
			if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
				log.error(e.getMessage());
				return null;
			} else {
				log.error(e.getMessage());
				return null;
			}
		} catch (HttpServerErrorException e) {
			log.error(e.getMessage());
			return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}

	}

	public List<CodeMaster> getByKeysCodeMaster(CodeMasterRequest codeMasterRequest) {

		String apiUrl = propertiesConfig.getBaseUrl() + propertiesConfig.getCodeMasterByKeys();

		// Create a RestTemplate with SSL support
		RestTemplate restTemplate = new RestTemplate();

		// Set headers, including the Authorization header with the token
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		String requestBody = null;
		try {
			objectMapper.setSerializationInclusion(Include.NON_EMPTY);
			requestBody = objectMapper.writeValueAsString(codeMasterRequest);
		} catch (JsonProcessingException e) {
		}
		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

		try {
			ResponseEntity<CodeMasterResponse> response;

			response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, CodeMasterResponse.class);
			if (response.getStatusCode().equals(HttpStatus.OK)) {
				return response.getBody().getData();
			} else {
				return null;
			}

		} catch (HttpClientErrorException e) {
			if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
				log.error(e.getMessage());
				return null;
			} else {
				log.error(e.getMessage());
				return null;
			}
		} catch (HttpServerErrorException e) {
			log.error(e.getMessage());
			return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}

	}

	public List<PortMaster> getByKeysPortMaster() {

		String apiUrl = propertiesConfig.getBaseUrl() + propertiesConfig.getListOfPorts();

		// Create a RestTemplate with SSL support
		RestTemplate restTemplate = new RestTemplate();

		// Set headers, including the Authorization header with the token
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// Create a HttpEntity with headers and request body
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		try {
			ResponseEntity<PortMasterResponse> response;

			response = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, PortMasterResponse.class);
			if (response.getStatusCode().equals(HttpStatus.OK)) {
				return response.getBody().getData();
			} else {
				return null;
			}

		} catch (HttpClientErrorException e) {
			if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
				log.error(e.getMessage());
				return null;
			} else {
				log.error(e.getMessage());
				return null;
			}
		} catch (HttpServerErrorException e) {
			log.error(e.getMessage());
			return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}

	}

	public String getLocation(Integer id) {

		String apiUrl = propertiesConfig.getBaseUrl() + propertiesConfig.getLocationById() + id;

		// Create a RestTemplate with SSL support
		RestTemplate restTemplate = new RestTemplate();

		// Set headers, including the Authorization header with the token
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// Create a HttpEntity with headers and request body
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		try {
			ResponseEntity<String> response;

			response = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, String.class);
			if (response.getStatusCode().equals(HttpStatus.OK)) {

				return response.getBody();
			} else {
				return null;
			}

		} catch (HttpClientErrorException e) {
			if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
				log.error(e.getMessage());
				return null;
			} else {
				log.error(e.getMessage());
				return null;
			}
		} catch (HttpServerErrorException e) {
			log.error(e.getMessage());
			return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}

	}

	public String getAllLocation(String key) {

		String apiUrl = propertiesConfig.getBaseUrl() + propertiesConfig.getListofLocations() + key;

		// Create a RestTemplate with SSL support
		RestTemplate restTemplate = new RestTemplate();

		// Set headers, including the Authorization header with the token
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		// Create a HttpEntity with headers and request body
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		try {
			ResponseEntity<String> response;

			response = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, String.class);
			if (response.getStatusCode().equals(HttpStatus.OK)) {
				return response.getBody();
			} else {
				return null;
			}

		} catch (HttpClientErrorException e) {
			if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
				log.error(e.getMessage());
				return null;
			} else {
				log.error(e.getMessage());
				return null;
			}
		} catch (HttpServerErrorException e) {
			log.error(e.getMessage());
			return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}

	}

	public String userCreation(String requestBody, String token) {
		String apiUrl = propertiesConfig.getKeyCloakUrl();

		// Set your authorization token
		String[] bearerString = token.split(" ");
		String authToken = bearerString[1];

		// Create a RestTemplate with SSL support
		RestTemplate restTemplate = new RestTemplate();

		// Set headers, including the Authorization header with the token
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(authToken);

		// Create a HttpEntity with headers and request body
		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

		ResponseEntity<String> responseEntity = null;

		// Make the POST request
		try {
			responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
			if (responseEntity.getStatusCode().equals(HttpStatus.CREATED)) {
				return "OK";
			} else {
				return null;
			}

		} catch (HttpClientErrorException e) {
			if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
				return e.getStatusText();
			} else {
				return e.getResponseBodyAsString();
			}

		} catch (HttpServerErrorException e) {
			log.error(e.getMessage());
			return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}

	}

	public String userCreationPMIS(String requestBody, String token) {
		String apiUrl = propertiesConfig.getKeyCloakBaseUrl() + propertiesConfig.getKeyCloakUserCreation();

		// Set your authorization token
		String[] bearerString = token.split(" ");
		String authToken = bearerString[1];

		// Create a RestTemplate with SSL support
		RestTemplate restTemplate = new RestTemplate();

		// Set headers, including the Authorization header with the token
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(authToken);

		// Create a HttpEntity with headers and request body
		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

		ResponseEntity<String> responseEntity = null;

		// Make the POST request
		try {
			responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, String.class);
			if (responseEntity.getStatusCode().equals(HttpStatus.CREATED)) {
				return "OK";
			} else {
				return null;
			}

		} catch (HttpClientErrorException e) {
			if (e.getRawStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
				throw new BusinessException(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());

			} else {
				Map<String, String> errorMessage = new HashMap<String, String>();
				try {
					errorMessage = objectMapper.readValue(e.getResponseBodyAsString(),
							new TypeReference<Map<String, String>>() {
							});
				} catch (JsonProcessingException ex) {

				}
				throw new BusinessException(e.getRawStatusCode(), errorMessage.get(CommonCodes.ERR_STR));
			}
		} catch (HttpServerErrorException e) {
			log.error(e.getMessage());
			return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}

	}

	public KeyCloakUserDetails validateUserPMIS(String email, String token) {
		String apiUrl = propertiesConfig.getKeyCloakBaseUrl() + propertiesConfig.getKeyCloakUserVerify() + email;

		// Set your authorization token
		String[] bearerString = token.split(" ");
		String authToken = bearerString[1];

		// Create a RestTemplate with SSL support
		RestTemplate restTemplate = new RestTemplate();

		// Set headers, including the Authorization header with the token
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(authToken);

		// Create a HttpEntity with headers and request body
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		// Make the GET request
		ResponseEntity<KeyCloakUserDetails[]> responseEntity = null;
		try {
			responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, KeyCloakUserDetails[].class);
			if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
				return responseEntity.getBody()[0];
			} else {
				return null;
			}

		} catch (HttpClientErrorException e) {
			if (e.getRawStatusCode() == HttpStatus.UNAUTHORIZED.value()) {
				throw new BusinessException(HttpStatus.UNAUTHORIZED.value(), HttpStatus.UNAUTHORIZED.getReasonPhrase());

			} else {
				Map<String, String> errorMessage = new HashMap<String, String>();
				try {
					errorMessage = objectMapper.readValue(e.getResponseBodyAsString(),
							new TypeReference<Map<String, String>>() {
							});
				} catch (JsonProcessingException ex) {

				}
				throw new BusinessException(e.getRawStatusCode(), errorMessage.get(CommonCodes.ERR_STR));
			}

		} catch (HttpServerErrorException e) {
			log.error(e.getMessage());
			return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}

	}

	public String validateUser(String email, String token) {
		String apiUrl = propertiesConfig.getKeyCloakVerify() + email;

		// Set your authorization token
		String[] bearerString = token.split(" ");
		String authToken = bearerString[1];

		// Create a RestTemplate with SSL support
		RestTemplate restTemplate = new RestTemplate();

		// Set headers, including the Authorization header with the token
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(authToken);

		// Create a HttpEntity with headers and request body
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		// Make the GET request
		ResponseEntity<String> responseEntity = null;
		try {
			responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, String.class);
			if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
				return responseEntity.getBody();
			} else {
				return null;
			}

		} catch (HttpClientErrorException e) {
			if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
				return e.getStatusText();
			} else {
				return e.getResponseBodyAsString();
			}
		} catch (HttpServerErrorException e) {
			log.error(e.getMessage());
			return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}

	}

	public MCIServiceGetByEUNResposne eunDetails(Long euNumber) {
		String apiUrl = String.format(propertiesConfig.getEunInfo(), euNumber);

		// Set your authorization token

		// Create a RestTemplate with SSL support
		RestTemplate restTemplate = new RestTemplate();

		// Set headers, including the Authorization header with the token
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-IBM-Client-Secret", "9435ebdc6eced52f348b8f18544a33e6");
		headers.set("X-IBM-Client-Id", "6f733b22207f835a461c8cdd7ca5e870");

		// Create a HttpEntity with headers and request body
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		try {
			ResponseEntity<MCIServiceGetByEUNResposne> objByEUNResposne;

			objByEUNResposne = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity,
					MCIServiceGetByEUNResposne.class);
			if (objByEUNResposne.getStatusCode().equals(HttpStatus.OK)) {
				return objByEUNResposne.getBody();
			} else {
				return null;
			}

		} catch (HttpClientErrorException e) {
			throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

		} catch (HttpServerErrorException e) {
			throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}

	}

	public OwnersListResponse ownerDetails(Long euNumber) {
		String apiUrl = String.format(propertiesConfig.getEunOwner(), euNumber);

		// Set your authorization token

		// Create a RestTemplate with SSL support
		RestTemplate restTemplate = new RestTemplate();

		// Set headers, including the Authorization header with the token
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.set("X-IBM-Client-Secret", "9435ebdc6eced52f348b8f18544a33e6");
		headers.set("X-IBM-Client-Id", "6f733b22207f835a461c8cdd7ca5e870");

		// Create a HttpEntity with headers and request body
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		try {
			ResponseEntity<OwnersListResponse> objByEUNResposne;

			objByEUNResposne = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, OwnersListResponse.class);
			if (objByEUNResposne.getStatusCode().equals(HttpStatus.OK)) {
				return objByEUNResposne.getBody();
			} else {
				return null;
			}

		} catch (HttpClientErrorException e) {
			throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());

		} catch (HttpServerErrorException e) {
			throw new BusinessException(HttpStatus.INTERNAL_SERVER_ERROR.value(),
					HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}

	}

	public SoapEnvelope sadadPayment(InvoiceCreateRequest createInvoiceRequest) {
		RestTemplate restTemplate = new RestTemplate();

		String SOAP_ENDPOINT_URL = propertiesConfig.getMciSerivce();
		String username = propertiesConfig.getUsername();
		String password = propertiesConfig.getPassword();

		CreateInvoiceRequest objCreateInvoiceRequest = new CreateInvoiceRequest();
		objCreateInvoiceRequest.setCRN(createInvoiceRequest.getCrn());
		objCreateInvoiceRequest.setAGENT_TYPE(createInvoiceRequest.getAgentType());
		objCreateInvoiceRequest.setLIC_TYPE(createInvoiceRequest.getLicType());
		objCreateInvoiceRequest.setORDER_DATE(createInvoiceRequest.getOrderDate());
		objCreateInvoiceRequest.setINVOICE_AMOUNT(createInvoiceRequest.getInvoiceAmount());
		objCreateInvoiceRequest.setORDER_NUMBER(createInvoiceRequest.getOrderNumber());

		Envelope objEnvelope = new Envelope();
		Body objBody = new Body();

		objBody.setCreateInvoice(objCreateInvoiceRequest);
		objEnvelope.setBody(objBody);

		// Set up the request headers with basic authentication
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(username, password);
		headers.setContentType(MediaType.TEXT_XML);

		String soapRequest = convertToXmlString(objEnvelope);

		// Create the HTTP request entity with the SOAP request and headers
		HttpEntity<String> requestEntity = new HttpEntity<>(soapRequest, headers);

		// Send the SOAP request and get the response
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(SOAP_ENDPOINT_URL, requestEntity,
				String.class);

		// Extract and print the response body
		String responseBody = responseEntity.getBody();
		SoapEnvelope objSoapEnvelope = new SoapEnvelope();
		try {
			objSoapEnvelope = convertXmlToSoapEnvelope(responseBody);
		} catch (JAXBException e) {
			log.error(e.getMessage());
			return null;

		}

		return objSoapEnvelope;
	}

	private static String convertToXmlString(Envelope request) {
		try {
			// Create a JAXB context for the CreateInvoiceRequest class
			JAXBContext context = JAXBContext.newInstance(Envelope.class);

			// Create a marshaller
			Marshaller marshaller = context.createMarshaller();

			// Configure the marshaller to format the output
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// Marshal the CreateInvoiceRequest to a StringWriter
			StringWriter writer = new StringWriter();
			marshaller.marshal(request, writer);

			// Get the XML string from the StringWriter
			return writer.toString();
		} catch (JAXBException e) {

			log.error(e.getMessage());
			return null;
		}
	}

	private static SoapEnvelope convertXmlToSoapEnvelope(String xmlString) throws JAXBException {
		JAXBContext jaxbContext = JAXBContext.newInstance(SoapEnvelope.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

		StringReader reader = new StringReader(xmlString);
		return (SoapEnvelope) unmarshaller.unmarshal(reader);

	}

	public SoapEnvelope sadadPaymentCheckStatus(String sadadNo) {
		RestTemplate restTemplate = new RestTemplate();

		String SOAP_ENDPOINT_URL = propertiesConfig.getMciSerivce();
		String username = propertiesConfig.getUsername();
		String password = propertiesConfig.getPassword();

		GetStatusEnvelope objEnvelope = new GetStatusEnvelope();
		GetStatusBody objBody = new GetStatusBody();
		GetStatusRequest objRequest = new GetStatusRequest();

		objRequest.setSadad_no(sadadNo);
		objBody.setGetStatus(objRequest);
		objEnvelope.setBody(objBody);

		// Set up the request headers with basic authentication
		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth(username, password);
		headers.setContentType(MediaType.TEXT_XML);

		String soapRequest = convertToXml(objEnvelope);

		// Create the HTTP request entity with the SOAP request and headers
		HttpEntity<String> requestEntity = new HttpEntity<>(soapRequest, headers);

		// Send the SOAP request and get the response
		ResponseEntity<String> responseEntity = restTemplate.postForEntity(SOAP_ENDPOINT_URL, requestEntity,
				String.class);

		// Extract and print the response body
		String responseBody = responseEntity.getBody();
		SoapEnvelope objSoapEnvelope = new SoapEnvelope();
		try {
			objSoapEnvelope = convertXmlToSoapEnvelope(responseBody);
		} catch (JAXBException e) {
			log.error(e.getMessage());
			return null;

		}

		return objSoapEnvelope;
	}

	private static String convertToXml(GetStatusEnvelope request) {
		try {
			// Create a JAXB context for the CreateInvoiceRequest class
			JAXBContext context = JAXBContext.newInstance(GetStatusEnvelope.class);

			// Create a marshaller
			Marshaller marshaller = context.createMarshaller();

			// Configure the marshaller to format the output
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

			// Marshal the CreateInvoiceRequest to a StringWriter
			StringWriter writer = new StringWriter();
			marshaller.marshal(request, writer);

			// Get the XML string from the StringWriter
			return writer.toString();
		} catch (JAXBException e) {

			log.error(e.getMessage());
			return null;
		}
	}
	
	
	public Integer restTemplatePut(String apiUrl, String requestBody, String token) {

		// Set your authorization token
		String[] bearerString = token.split(" ");
		String authToken = bearerString[1];

		// Create a RestTemplate with SSL support
		RestTemplate restTemplate = new RestTemplate();

		// Set headers, including the Authorization header with the token
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(authToken);
		
		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

		try {
			ResponseEntity<String> response;

			response = restTemplate.exchange(apiUrl, HttpMethod.PUT, requestEntity, String.class);
			return response.getStatusCodeValue();
		} catch (HttpClientErrorException e) {
			return e.getRawStatusCode();
		} catch (HttpServerErrorException e) {
			return e.getRawStatusCode();
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}

	}
	
	public Object policyCreate(PortAdminPolicy requestBody, String token) {
		
		
		String apiUrl = propertiesConfig.getPolicyCreate() + propertiesConfig.getPmisClientId() + "/authz/resource-server/policy/role";
		
		// Set your authorization token
		String[] bearerString = token.split(" ");
		String authToken = bearerString[1];

		// Create a RestTemplate with SSL support
		RestTemplate restTemplate = new RestTemplate();

		// Set headers, including the Authorization header with the token
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(authToken);

		// Create a HttpEntity with headers and request body
		HttpEntity<PortAdminPolicy> requestEntity = new HttpEntity<>(requestBody, headers);

		ResponseEntity<Object> responseEntity = null;

		// Make the POST request
		try {
			responseEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntity, Object.class);
			if (responseEntity.getStatusCode().equals(HttpStatus.CREATED)) {
				return responseEntity.getBody();
			} else {
				return null;
			}

		} catch (HttpClientErrorException e) {
			if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
				return e.getStatusText();
			} else {
				return e.getResponseBodyAsString();
			}

		} catch (HttpServerErrorException e) {
			log.error(e.getMessage());
			return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}

	}
	
	public String roleCreateRedHat(RedHatKeycloakRole roles,String token) {
		if (roles != null) {
			ResponseEntity<String> responseEntity = null;
			try {
				String apiUrl = propertiesConfig.getRedhatpmisurl()+"/roles";
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("Authorization", token);

				HttpEntity<RedHatKeycloakRole> requestEntity = new HttpEntity<>(roles, headers);

				responseEntity = new RestTemplate().exchange(apiUrl, HttpMethod.POST,
						requestEntity, String.class);

				if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
					String jsonObject = "created";
					return jsonObject;
				} else {
					return null;
				}
			} catch (HttpClientErrorException e) {

				throw new BusinessException(e.getStatusCode().value() ,e.getMessage());
			}
		}
		return null;
	}

	public JSONObject roleGetRedHat(String search,String token) {
	    if (search != null) {
	        try {
	            String apiUrl = propertiesConfig.getRedhatpmisurl()+"/roles?first=0&max=20&search=" + search;
	            HttpHeaders headers = new HttpHeaders();
	            headers.set("Authorization", token);
	            HttpEntity<String> requestEntity = new HttpEntity<>(headers);
	            ResponseEntity<String> responseEntity = new RestTemplate().exchange(apiUrl, HttpMethod.GET, requestEntity, String.class);

	            if (responseEntity.getStatusCode() == HttpStatus.OK) {
	                String jsonObject = responseEntity.getBody();
	                JSONArray jsonArray = new JSONArray(jsonObject);

	                for (int i = 0; i < jsonArray.length(); i++) {
	                    JSONObject jsonOb = jsonArray.getJSONObject(i);
	                    return jsonOb;
	                }
	            } else {
	                return null;
	            }
	        } catch (HttpClientErrorException e) {

				throw new BusinessException(e.getStatusCode().value() ,e.getMessage());
			}
	    }
	    return null;
	}

	public String attributeCreateRedhat(RoleRedHat search, String id,String token) {
		if (search != null) {
			try {
				String apiUrl = propertiesConfig.getRedhatpmisurl()+"/roles-by-id/" + id;
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("Authorization", token);
				HttpEntity<RoleRedHat> requestEntity = new HttpEntity<>(search, headers);

				ResponseEntity<String> responseEntity = new RestTemplate().exchange(apiUrl, HttpMethod.PUT,
						requestEntity, String.class);	

				if (responseEntity.getStatusCode() == HttpStatus.NO_CONTENT) {
					String jsonObject = "save";
					return jsonObject;
				} else {
					return null;
				}
			} catch (HttpClientErrorException e) {

				throw new BusinessException(e.getStatusCode().value() ,e.getMessage());
			}
		}
		return null;
	}
	
	public String getAttributes(String id, String token) {
		String apiUrl = propertiesConfig.getGetAttributesUrl() + id;

		// Set your authorization token
		String[] bearerString = token.split(" ");
		String authToken = bearerString[1];

		// Create a RestTemplate with SSL support
		RestTemplate restTemplate = new RestTemplate();

		// Set headers, including the Authorization header with the token
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(authToken);

		// Create a HttpEntity with headers and request body
		HttpEntity<String> requestEntity = new HttpEntity<>(headers);

		// Make the GET request
		ResponseEntity<String> responseEntity = null;
		try {
			responseEntity = restTemplate.exchange(apiUrl, HttpMethod.GET, requestEntity, String.class);
			if (responseEntity.getStatusCode().equals(HttpStatus.OK)) {
				return responseEntity.getBody();
			} else {
				return null;
			}

		} catch (HttpClientErrorException e) {
			if (e.getStatusCode().equals(HttpStatus.UNAUTHORIZED)) {
				return e.getStatusText();
			} else {
				return e.getResponseBodyAsString();
			}

		} catch (HttpServerErrorException e) {
			log.error(e.getMessage());
			return null;
		} catch (Exception e) {
			log.error(e.getMessage());
			return null;
		}

	}
	
	public String createUpdateAccess(AccessCreateRequest accRequest,String permissionId,String token) {
		if (accRequest != null) {
			ResponseEntity<String> responseEntity = null;
			try {
				String apiUrl = propertiesConfig.getPolicyCreate() + propertiesConfig.getResourceServerId() +"/authz/resource-server/permission/scope/"+ permissionId;
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("Authorization", token);

				HttpEntity<AccessCreateRequest> requestEntity = new HttpEntity<>(accRequest, headers);

				responseEntity = new RestTemplate().exchange(apiUrl, HttpMethod.PUT,
						requestEntity, String.class);

				if (responseEntity.getStatusCode() == HttpStatus.CREATED) {
					String jsonObject = "created";
					return jsonObject;
				} else {
					return null;
				}
			} catch (HttpClientErrorException e) {

				throw new BusinessException(e.getStatusCode().value() ,e.getMessage());
			}
		}
		return null;
	}

	
	public String userAddPmis(KeyCloakUserDetails userRequest,String uuid,String token) {
		if (userRequest != null) {
			ResponseEntity<String> responseEntity = null;
			try {
				String apiUrl = propertiesConfig.getKeyCloakBaseUrl() + propertiesConfig.getKeyCloakUserCreation() + "/" + uuid ;
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("Authorization", token);

				HttpEntity<KeyCloakUserDetails> requestEntity = new HttpEntity<>(userRequest, headers);

				responseEntity = new RestTemplate().exchange(apiUrl, HttpMethod.PUT,
						requestEntity, String.class);

				if (responseEntity.getStatusCode() == HttpStatus.NO_CONTENT) {
					String jsonObject = "created";
					return jsonObject;
				} else {
					return null;
				}
			} catch (HttpClientErrorException e) {

				throw new BusinessException(e.getStatusCode().value() ,e.getMessage());
			}
		}
		return null;
	}
	
	public KeyCloakUserDetails userGetPmis(String uuid, String token) {
		try {
			String apiUrl = propertiesConfig.getKeyCloakBaseUrl() + propertiesConfig.getKeyCloakUserCreation() + "/"
					+ uuid;
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", token);

			HttpEntity<String> requestEntity = new HttpEntity<>(headers);

			ResponseEntity<KeyCloakUserDetails> responseEntity = new RestTemplate().exchange(apiUrl, HttpMethod.GET,
					requestEntity, KeyCloakUserDetails.class);

			if (responseEntity.getStatusCode() == HttpStatus.OK) {
				return responseEntity.getBody();
			} else {
				return null;
			}
		} catch (HttpClientErrorException e) {
			throw new BusinessException(e.getStatusCode().value(), e.getMessage());
		}
	}
	
	public String userAddPcs(KeyCloakUserDetails userRequest,String uuid,String token) {
		if (userRequest != null) {
			ResponseEntity<String> responseEntity = null;
			try {
				String apiUrl = propertiesConfig.getKeyCloakUrl() + "/" + uuid ;
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				headers.set("Authorization", token);

				HttpEntity<KeyCloakUserDetails> requestEntity = new HttpEntity<>(userRequest, headers);

				responseEntity = new RestTemplate().exchange(apiUrl, HttpMethod.PUT,
						requestEntity, String.class);

				if (responseEntity.getStatusCode() == HttpStatus.NO_CONTENT) {
					String jsonObject = "created";
					return jsonObject;
				} else {
					return null;
				}
			} catch (HttpClientErrorException e) {

				throw new BusinessException(e.getStatusCode().value() ,e.getMessage());
			}
		}
		return null;
	}
	
	public KeyCloakUserDetails userGetPcs(String uuid, String token) {
		try {
			String apiUrl = propertiesConfig.getKeyCloakUrl() + "/" + uuid ;
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			headers.set("Authorization", token);

			HttpEntity<String> requestEntity = new HttpEntity<>(headers);

			ResponseEntity<KeyCloakUserDetails> responseEntity = new RestTemplate().exchange(apiUrl, HttpMethod.GET,
					requestEntity, KeyCloakUserDetails.class);

			if (responseEntity.getStatusCode() == HttpStatus.OK) {
				return responseEntity.getBody();
			} else {
				return null;
			}
		} catch (HttpClientErrorException e) {
			throw new BusinessException(e.getStatusCode().value(), e.getMessage());
		}
	}

}
