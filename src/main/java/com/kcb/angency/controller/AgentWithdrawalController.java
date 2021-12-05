package com.kcb.angency.controller;

import java.io.IOException;
import java.util.LinkedHashMap;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.kcb.angency.configs.WithdrawalConfigurations;
import com.kcb.angency.models.AgentWithdrawalModel;
import com.kcb.angency.services.CustomHttpService;
import com.kcb.angency.utils.HelperUtility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import java.net.URL;

@Controller
@RequestMapping("agency-payments")
public class AgentWithdrawalController {

    public Logger LOGGER = LogManager.getLogger(AgentWithdrawalController.class);

    private String originatorConversationID;
    private String conversationID;
    private String thirdPartyID;
    private String resultURL;
    private String timestamp;
    private String customerMobile;
    private String channelCode;
    private double transactionAmount;
    private String requestUrl;
    private String keyOwner;
    private String requestorSystemCode;
    private String initiatorIdentifierType;
    private String initiatorIdentifier;
    private String initiatorSecurityCredential; 
    private String callerType;
    private String password; 
    private String receiverPartyIdentifier;
    private String redisRouteCode;
    private String redisAuthorization;
    private String redisMessageId;
    private String redisChannelCode;
    private String redisUrl;
    private String initiatorShortCode;
    private String businessKeyType;

    @Autowired
    WithdrawalConfigurations withdrawalConfigurations;

    @PostMapping(path = "/agent-withdrawal", produces = "application/json")
    @org.springframework.web.bind.annotation.ResponseBody
    public String agentDeposit(@RequestBody AgentWithdrawalModel agentWithdrawalModel,
            @RequestHeader(value = "System-Credentials") String basicAuth) throws JsonProcessingException {
        // validate user credentials
        // get specific value of auth string and match with in memory basic auth
        //String authValue = basicAuth.substring(6, basicAuth.length());
        String authValue = basicAuth;

        if (authValue.equals(HelperUtility.encodeUserCredentials())) {
            LOGGER.info(agentWithdrawalModel.getIdentifier());
            //Get values from request Body
              // Get values from request body
              originatorConversationID = agentWithdrawalModel.getOriginatorConversationID();
              channelCode = withdrawalConfigurations.getChannelCode();
              customerMobile = agentWithdrawalModel.getIdentifier();
              transactionAmount = agentWithdrawalModel.getTransactionAmount();
              receiverPartyIdentifier = agentWithdrawalModel.getReceiverPartyIdentifierType()
              //Values from config file
            callerType = withdrawalConfigurations.getCallerType();
            password = withdrawalConfigurations.getPassword(); 
            redisRouteCode = withdrawalConfigurations.getRedisRouteCode();
            redisAuthorization = withdrawalConfigurations.getRedisAuthorization();
            redisMessageId = withdrawalConfigurations.getRedisMessageId();
            redisChannelCode = withdrawalConfigurations.getRedisChannelCode();
            redisUrl = withdrawalConfigurations.getRedisUrl();
            initiatorShortCode = withdrawalConfigurations.getInitiatorShortCode();
        
            //Insert call back details;
            // Get and Build URL
			URL redisURL = new URL(redisUrl);
			// Save the callback details to redis
			String businessKey = agentWithdrawalModel.getOriginatorConversationID();
			String businessKeyType = agentWithdrawalModel.getBusinessKeyType();
			String url = agentWithdrawalModel.getCallBackUrl();
			String agencyBusinessName = agentWithdrawalModel.getBusinessName();

			URL URL = new URL(url); //URL Builder

			// Client Builder
			OkHttpClient client = new OkHttpClient().newBuilder()
					.build();
			MediaType mediaType = MediaType.parse("application/json");
			okhttp3.RequestBody body = okhttp3.RequestBody.create(mediaType,
					"{\r\n\"keyType\": {\r\n\"businessKey\":\"" + businessKey + "\",\r\n\"businessKeyType\": "
							+ "\"" + businessKeyType + "\",\r\n\"businessKeyName\": \"" + agencyBusinessName
							+ "\"\r\n},\r\n\"urlType\": {\r\n\"businessKey\":"
							+ "\"" + URL + "\",\r\n\"businessKeyType\": \"" + businessKeyType + "\"\r\n}\r\n}");
			Request request = new Request.Builder()
					.url(redisURL)
					.method("POST", body)
					.addHeader("routeCode", withdrawalConfigurations.getRedisRouteCode())
					.addHeader("Authorization", withdrawalConfigurations.getRedisAuthorization())
					.addHeader("messageID", withdrawalConfigurations.getRedisMessageId())
					.addHeader("channelCode", withdrawalConfigurations.getRedisChannelCode())
					.build();
			ResponseBody responseBody = client.newCall(request).execute().body();
			ObjectMapper responseMapper = new ObjectMapper();
			PostUrlResponse entity = responseMapper.readValue(responseBody.string(), PostUrlResponse.class);
			// entity.keyType
			LOGGER.info("====================================[REDIS Response Status Code:] " + entity.statusCode);
			LOGGER.info("====================================[REDIS Response Service ID:] " + entity.statusDescription);


          
            // Get the XML Request String
            String agentWithdrawalXMLReq = AgentWithdrawalController.getAgentWithdrawalReqXML(originatorConversationID,
                    conversationID, channelCode, customerMobile);

            LOGGER.info("====================================[Start Request]\n"
                    + agentWithdrawalXMLReq);
            // LinkedHashMap<String, Object> agentDepositList = new LinkedHashMap<>();

            String withdrawalResponse = CustomHttpService.callWithdrawalService(withdrawalConfigurations.getEndPoint(),
                    agentWithdrawalXMLReq);
            LOGGER.info("====================================[Account Validation XML Response] :\n "+ withdrawalResponse);

            // Initialize JSON String
            String withdrawalJsonResponse = null;
            try {
                // Create a new XmlMapper to read XML tags
                XmlMapper xmlMapper = new XmlMapper();

                // Reading the XML
                JsonNode jsonNode = xmlMapper.readTree(withdrawalResponse.getBytes());

                // Create a new ObjectMapper
                ObjectMapper objectMapper = new ObjectMapper();

                // Get JSON as a string
                // agentDepositJsonAckResponse =
                // objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonNode);
                withdrawalJsonResponse = objectMapper.writeValueAsString(jsonNode);

               LOGGER.info("====================================[KYC JSON Response] :\n "+ withdrawalJsonResponse);

            } catch (JsonParseException e) {
                e.printStackTrace();
            } catch (JsonMappingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            // agentDepositList.put("Response", accountValidationJsonResponse);
            return withdrawalJsonResponse;

        } else {
            LinkedHashMap<String, Object> errorMessage = new LinkedHashMap<>();
            errorMessage.put("StausCode", "4001");
            errorMessage.put("MessageDesc", "Invalid Username or Password");
            errorMessage.put("httpCode", HttpStatus.BAD_REQUEST);

            ObjectMapper objectMapper = new ObjectMapper();
            String withdrawalErrorResponse = objectMapper.writeValueAsString(errorMessage);
            LOGGER.info("====================================[KYC JSON Response] :\n");
            LOGGER.info("====================================["+withdrawalErrorResponse+"]:\n");
            return withdrawalErrorResponse;
        }

    }

    // VOOMA Request Builder
    public static String getAgentWithdrawalReqXML(String originatorRequestID, String initiatorShortCode,
            String responseUrl, String mobilePhoneNumber) {

        String reqXML = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:api=\"http://cps.huawei.com/cpsinterface/api_requestmgr\" xmlns:req=\"http://cps.huawei.com/cpsinterface/request\" xmlns:com=\"http://cps.huawei.com/cpsinterface/common\">\n"
                + "    <soapenv:Header/>\n" + "    <soapenv:Body>\n" + "        <api:Request>\n"
                + "            <req:Header>\n" + "                <req:Version>1.0</req:Version>\n"
                + "                <req:CommandID>InitTrans_CustomerCashOutatATM</req:CommandID>\n"
                + "                <req:OriginatorConversationID>" + originatorRequestID
                + "</req:OriginatorConversationID>\n" + "                <req:Caller>\n"
                + "                    <req:CallerType>2</req:CallerType>\n"
                + "                    <req:ThirdPartyID>POS_Broker</req:ThirdPartyID>\n"
                + "                    <req:Password>NykEQz7IBFjYtaEzG08MmScbxSv4rPfhxZtSq10Fv5HtX4Wj8mImawjoWAspaROkgT5Wb57i8A45FEvZ5nJ4ges9ioza83eCB3WjEwuOztEc/C0ntD4YWWj/ONrfSNCDiQOspv1xjc6/wiSu73kF1d7LEPP9aDCmsxkM3tXAZDdCRp93L0aaSPjS1EWFF6tJe8eOtj2iFoO69zz3p3LVl/DU6oE2inTKnKVgTmutrmow3F/FL5CR5uE7ZFXMRX3QNu0fI4e0PD6YuCi842EKFGdYzy+k/qzcrjqINWjb9p9zLa3EgLR+1yhm6odFAo2Ko7prWnBXZvy2KDYoYZmH6hPwZV7KBTylVjAteRlX1rrS6a5WcL6Ie9HQen3dUt8CmeqfVKNkO2eGk2q0Imoku5wh8JCjy+TN3A3AdfGhrsueYOEWReTYl3W9sUotpfT9KeuxJQzR0mC17MKIatrdR0J/RqFcCshkmEVz3Ja7o/wm6LHk8+1zJvxnFY3/K+hBuJEpf1Ho512JGRdMPOdniCyicSomaZhIAKYkYhA7TA30l5oXMRdc6E1Vh91nDpLjSUQZ2fdy5o6ljFNfwfcq8ynS95E0uWnCAwbDVEs2m3onhBHmHKoyQpivfk/EvMvTdRTUu6Xn/pJ0q6AVjUB9ldBqgARjwOWPXEdXs5PA0Qo=</req:Password>\n"
                + "                    <req:ResultURL>http://agency-services-callback-kcb-wso2-gateway.apps.dev.aro.kcbgroup.com/agency-callback/v1/transaction-result</req:ResultURL>\n"
                + "                </req:Caller>\n" + "                <req:KeyOwner>1</req:KeyOwner>\n"
                + "                <req:Timestamp>{{$timestamp}}</req:Timestamp>\n"
                + "                <req:ChannelCode>1014</req:ChannelCode>\n" + "            </req:Header>\n"
                + "            <req:Body>\n" + "                <req:Identity>\n"
                + "                    <req:Initiator>\n"
                + "                        <req:IdentifierType>11</req:IdentifierType>\n"
                + "                        <req:Identifier>orgapi</req:Identifier>\n"
                + "                        <req:SecurityCredential>jH/U+kcMAYybyz76d3MNBHpTOhNreCtMf8uHFpgEklN6Axc90tWE50rytt2IVs5u3cLwOdq9PGuhTbHtPcguf1gDB+tN8MytGxpJziEYB2uSY1x149zSCTXItKCKJACU3qeK5qohcmHqDccA/CBMx6aGU+X7yEMRmV+kebvc7ZW+q4teYoDqzL6PaZWLLm/hMAU42/7j2Xu9oUC0uj14SlQzv1hOQoqnIileV2tipCejj63Kr6P8ZFtPNvJfz9uZUmbpBaaETwsUpCniMTnCur0FMkghdR6WDL1cJhyS8y4qs+0RG1/H6y5sUbih1/mt3+X02VqrGpa1ahxwVHV0OC/38ae6lFQIeh4xg9X6hgR/7ZnIrOvpNjEVq2wfyDDJZ+Zp28843Telch2Adl1h2+3Rwvf4bo/urdXXFZ2F+Ug6+BvXmsDN3QJjubgQ9Dd48g/Hs0L8Ieg8tjoLtwEpc//DO8beV4OmYB5cLSh4bPOuP7+8XCNV6BmGbZ31vDEAamfCHGB7xsqwrLhOL8A6PByqfdI5La6ZYztwutVfkuWhpmCdwHdWkPDIAT/GMCf2qQUQBCrUGqq/0iL0cioCU4mND3A5jT8l3B/oqW3qAYevW/NMOSEIgbIN1zVBK0f3ob1IjssvX2VQjQPoQTd0tvqcbx8yiNjEFBXghOXYo6U=</req:SecurityCredential>\n"
                + "                        <req:ShortCode>987654</req:ShortCode>\n"
                + "                    </req:Initiator>\n" + "                    <req:ReceiverParty>\n"
                + "                        <req:IdentifierType>13</req:IdentifierType>\n"
                + "                        <req:Identifier>110589</req:Identifier>\n"
                + "                    </req:ReceiverParty>\n" + "                </req:Identity>\n"
                + "                <req:TransactionRequest>\n" + "                    <req:Parameters>\n"
                + "                        <req:Parameter>\n"
                + "                            <com:Key>VoucherValidationValue</com:Key>\n"
                + "                            <com:Value>9136</com:Value>\n"
                + "                        </req:Parameter>\n"
                + "                        <req:Amount>10005.00</req:Amount>\n"
                + "                        <req:Currency>KES</req:Currency>\n"
                + "                    </req:Parameters>\n" + "                </req:TransactionRequest>\n"
                + "                <req:ReferenceData>\n" + "                    <req:ReferenceItem>\n"
                + "                        <com:Key>ExternalTransactionID</com:Key>\n"
                + "                        <com:Value>OLL606RCF0</com:Value>\n"
                + "                    </req:ReferenceItem>\n" + "                    <req:ReferenceItem>\n"
                + "                        <com:Key>POSDeviceID</com:Key>\n"
                + "                        <com:Value>POS23579</com:Value>\n"
                + "                    </req:ReferenceItem>\n" + "                </req:ReferenceData>\n"
                + "            </req:Body>\n" + "        </api:Request>\n" + "    </soapenv:Body>\n"
                + "</soapenv:Envelope>";

        return reqXML;
    }
}