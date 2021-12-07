package com.kcb.angency.controller;

import java.io.IOException;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.kcb.angency.configs.WithdrawalConfigurations;
import com.kcb.angency.models.AgentWithdrawalModel;
import com.kcb.angency.models.PostUrlResponse;
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

    String originatorConversationID;
    String conversationID;
    String thirdPartyID;
    String customerMobile;
    String channelCode;
    String transactionAmount;
    String requestUrl;
    String keyOwner;
    String requestorSystemCode;
    String initiatorIdentifierType;
    String initiatorIdentifier;
    String initiatorSecurityCredential;
    String callerType;
    String password;
    String receiverPartyIdentifier;
    String receiverPartyIdentifierType;
    String redisUrl;
    String initiatorShortCode;
    String callerThirdPartyId;
    String currency;
    String externalTransactionID;
    String strSystemCallbackUrl;
    String voucheValidationValue;
    String posDeviceID;
    static URL systemCallbackUrl;
    String callbackUrl;

    @Autowired
    WithdrawalConfigurations withdrawalConfigurations;

    @PostMapping(path = "/agent-withdrawal", produces = "application/json")
    @org.springframework.web.bind.annotation.ResponseBody
    public String agentDeposit(@RequestBody AgentWithdrawalModel agentWithdrawalModel,
            @RequestHeader(value = "System-Credentials") String basicAuth) throws IOException {
        // validate user credentials
        // get specific value of auth string and match with in memory basic auth
        // String authValue = basicAuth.substring(6, basicAuth.length());
        String authValue = basicAuth;

      

        if (authValue.equals(HelperUtility.encodeUserCredentials())) {
            LOGGER.info("====================================[TEST OUTPUT:]"+transactionAmount);
            LOGGER.info("====================================[USER CALLBACK:]"+agentWithdrawalModel.getCallBackUrl());
            // Get values from request body
            originatorConversationID = agentWithdrawalModel.getOriginatorConversationID();
            channelCode = withdrawalConfigurations.getChannelCode();
            customerMobile = agentWithdrawalModel.getIdentifier();
            transactionAmount = agentWithdrawalModel.getTransactionAmount();
            receiverPartyIdentifier = agentWithdrawalModel.getReciverPartyIdentifier();
            receiverPartyIdentifierType = agentWithdrawalModel.getReceiverPartyIdentifierType();
            currency = agentWithdrawalModel.getCurrency();
            voucheValidationValue = agentWithdrawalModel.getVoucheValidationValue();
            posDeviceID = agentWithdrawalModel.getPosDeviceID();
            externalTransactionID = agentWithdrawalModel.getExternalTransactionID();
            

            // Values from config file
            initiatorIdentifier = withdrawalConfigurations.getInitiatorIdentifier();
            initiatorIdentifierType = withdrawalConfigurations.getInitiatorIdentifierType();
            callerType = withdrawalConfigurations.getCallerType();
            password = withdrawalConfigurations.getPassword();
            redisUrl = withdrawalConfigurations.getRedisUrl();
            initiatorShortCode = withdrawalConfigurations.getInitiatorShortCode();
            callerThirdPartyId = withdrawalConfigurations.getCallerThirdPartyId();
            initiatorSecurityCredential = withdrawalConfigurations.getInitiatorSecurityCredential();
            strSystemCallbackUrl = withdrawalConfigurations.getSystemCallbackUrl();
            channelCode = withdrawalConfigurations.getChannelCode();
            
            //LOGGER.info("====================================[TEST OUTPUT:]" + strSystemCallbackUrl);

            // Build call back URL
            systemCallbackUrl = new URL(strSystemCallbackUrl);

            // Insert call back details;
            // Get and Build URLs
            URL redisURL = new URL(redisUrl);
            // Save the callback details to redis
            String businessKey = agentWithdrawalModel.getOriginatorConversationID();
            String businessKeyType = agentWithdrawalModel.getBusinessKeyType();
            String userUrl = agentWithdrawalModel.getCallBackUrl();
            String agencyBusinessName = agentWithdrawalModel.getBusinessName();

            //LOGGER.info("====================================[TEST OUTPUT:]" + userUrl);
            URL URL = new URL(userUrl); // URL Builder

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
            String agentWithdrawalXMLReq = AgentWithdrawalController.getAgentWithdrawalReqXML(posDeviceID,
                    voucheValidationValue, receiverPartyIdentifier,
                    receiverPartyIdentifierType, initiatorIdentifier, initiatorIdentifierType, callerType,
                    callerThirdPartyId, externalTransactionID,
                    currency, transactionAmount, initiatorShortCode,
                    password, initiatorSecurityCredential, originatorConversationID,
                    conversationID, channelCode, customerMobile);

            LOGGER.info("====================================[Start Request]\n"
                    + agentWithdrawalXMLReq);
            // LinkedHashMap<String, Object> agentDepositList = new LinkedHashMap<>();

            String withdrawalResponse = CustomHttpService.callWithdrawalService(
                    withdrawalConfigurations.getRequestUrl(),
                    agentWithdrawalXMLReq);
            LOGGER.info(
                    "====================================[Account Validation XML Response] :\n " + withdrawalResponse);

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

                LOGGER.info("====================================[KYC JSON Response] :\n " + withdrawalJsonResponse);

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
            LOGGER.info("====================================[" + withdrawalErrorResponse + "]:\n");
            return withdrawalErrorResponse;
        }

    }

    // VOOMA Request Builder
    public static String getAgentWithdrawalReqXML(String posDeviceID, String voucheValidationValue,
            String receiverPartyIdentifier, String receiverPartyIdentifierType,
            String initiatorIdentifier, String initiatorIdentifierType, String callerType, String callerThirdPartyId,
            String externalTransactionID, String currency,
            String transactionAmount, String initiatorShortCode, String password,
            String initiatorSecurityCredential, String originatorConversationID, String conversationID,
            String channelCode, String customerMobile) {

        String reqXML = "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:api=\"http://cps.huawei.com/cpsinterface/api_requestmgr\" xmlns:req=\"http://cps.huawei.com/cpsinterface/request\" xmlns:com=\"http://cps.huawei.com/cpsinterface/common\">\n"
                + "    <soapenv:Header/>\n" + "    <soapenv:Body>\n" + "        <api:Request>\n"
                + "            <req:Header>\n" + "                <req:Version>1.0</req:Version>\n"
                + "                <req:CommandID>InitTrans_CustomerCashOutatATM</req:CommandID>\n"
                + "                <req:OriginatorConversationID>" + originatorConversationID
                + "</req:OriginatorConversationID>\n" + "                <req:Caller>\n"
                + "                    <req:CallerType>" + callerType + "</req:CallerType>\n"
                + "                    <req:ThirdPartyID>" + callerThirdPartyId + "</req:ThirdPartyID>\n"
                + "                    <req:Password>" + password + "</req:Password>\n"
                + "                    <req:ResultURL>" + systemCallbackUrl + "</req:ResultURL>\n"
                + "                </req:Caller>\n" + "                <req:KeyOwner>1</req:KeyOwner>\n"
                + "                <req:Timestamp>" + HelperUtility.getTransactionTimestamp() + "</req:Timestamp>\n"
                + "                <req:ChannelCode>" + channelCode + "</req:ChannelCode>\n"
                + "            </req:Header>\n"
                + "            <req:Body>\n" + "                <req:Identity>\n"
                + "                    <req:Initiator>\n"
                + "                        <req:IdentifierType>" + initiatorIdentifierType + "</req:IdentifierType>\n"
                + "                        <req:Identifier>" + initiatorIdentifier + "</req:Identifier>\n"
                + "                        <req:SecurityCredential>" + initiatorSecurityCredential
                + "</req:SecurityCredential>\n"
                + "                        <req:ShortCode>" + initiatorShortCode + "</req:ShortCode>\n"
                + "                    </req:Initiator>\n" + "                    <req:ReceiverParty>\n"
                + "                        <req:IdentifierType>" + receiverPartyIdentifierType
                + "</req:IdentifierType>\n"
                + "                        <req:Identifier>" + receiverPartyIdentifier + "</req:Identifier>\n"
                + "                    </req:ReceiverParty>\n" + "                </req:Identity>\n"
                + "                <req:TransactionRequest>\n" + "                    <req:Parameters>\n"
                + "                        <req:Parameter>\n"
                + "                            <com:Key>VoucherValidationValue</com:Key>\n"
                + "                            <com:Value>" + voucheValidationValue + "</com:Value>\n"
                + "                        </req:Parameter>\n"
                + "                        <req:Amount>" + transactionAmount + "</req:Amount>\n"
                + "                        <req:Currency>" + currency + "</req:Currency>\n"
                + "                    </req:Parameters>\n" + "                </req:TransactionRequest>\n"
                + "                <req:ReferenceData>\n" + "                    <req:ReferenceItem>\n"
                + "                        <com:Key>ExternalTransactionID</com:Key>\n"
                + "                        <com:Value>" + externalTransactionID + "</com:Value>\n"
                + "                    </req:ReferenceItem>\n" + "                    <req:ReferenceItem>\n"
                + "                        <com:Key>POSDeviceID</com:Key>\n"
                + "                        <com:Value>" + posDeviceID + "</com:Value>\n"
                + "                    </req:ReferenceItem>\n" + "                </req:ReferenceData>\n"
                + "            </req:Body>\n" + "        </api:Request>\n" + "    </soapenv:Body>\n"
                + "</soapenv:Envelope>";

        return reqXML;
    }
}