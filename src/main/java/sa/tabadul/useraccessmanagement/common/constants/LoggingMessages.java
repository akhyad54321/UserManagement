package sa.tabadul.useraccessmanagement.common.constants;


import lombok.Data;

@Data
public class LoggingMessages {

    public static final String LOG_MESSAGE_MAX_TRY = "Max retries(%d) reached. Could not complete the request.";
    public static final String LOG_MESSAGE_RETRY_URL = "Failed to connect..!\"%s\", Retrying(%d)...";
    public static final String LOG_MESSAGE_RESPONSE_CODE = "ResponseCode: %d";
    public static final String LOG_MESSAGE_API_CALL = "-----------------EXTERNAL API CALL-----------------";
    public static final String LOG_SEPERATOR = "---------------------------------------------------";
    public static final String LOG_URL = "URL";
    public static final String LOG_REQUEST_TYPE = "Request Type : ";
    public static final String LOG_RESPONSE_STATUS = "Response status : ";
    public static final String LOG_REQUEST_BODY = "Request Body: ";
    public static final String LOG_RESPONSE_BODY = "Response Body: ";
    public static final String REQUEST_PARAMS = "Request Params: ";
    
    
   
    
    

}
