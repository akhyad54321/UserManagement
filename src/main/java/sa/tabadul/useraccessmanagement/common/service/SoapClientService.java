package sa.tabadul.useraccessmanagement.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.client.core.SoapActionCallback;

import sa.tabadul.useraccessmanagement.soap.GetZakatCertificateByCrNumber;
import sa.tabadul.useraccessmanagement.soap.GetZakatCertificateByCrNumberResponse;

@Service
public class SoapClientService {

	private final WebServiceTemplate webServiceTemplate;

	@Autowired
	public SoapClientService(WebServiceTemplate webServiceTemplate) {
		this.webServiceTemplate = webServiceTemplate;
	}

	public GetZakatCertificateByCrNumberResponse callSoapService(
			GetZakatCertificateByCrNumber request) {

		String soapAction = "http://tempuri.org/IDZITService/GetZakatCertificateByCommercialRegistrationNumberForMainBranchOrSubBranches";

		return (GetZakatCertificateByCrNumberResponse) webServiceTemplate
				.marshalSendAndReceive(
						"http://192.168.115.235/GSBExpressFasah/Other/ZakatCertificate/3.0/CertificateService.svc",
						request, new SoapActionCallback(soapAction));
	}
}
