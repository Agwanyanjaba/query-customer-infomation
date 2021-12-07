package com.kcb.angency.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "vooma.bridge")
public class WithdrawalConfigurations {

    private String requestUrl;
    private String channelCode;
    private String keyOwner;
    private String requestorSystemCode;
    private String initiatorIdentifierType;
    private String initiatorIdentifier;
    private String initiatorSecurityCredential; 
    private String callerType;
    private String password; 
    private String receiverPartyIdentifier;
    private String receiverPartyIdentifierType;
    private String redisRouteCode;
    private String redisAuthorization;
    private String redisMessageId;
    private String redisChannelCode;
    private String redisUrl;
    private String initiatorShortCode;
    private String callerThirdPartyId;
    private String systemCallbackUrl;
    



    /**
     * @return String return the requestUrl
     */
    public String getRequestUrl() {
        return requestUrl;
    }

    /**
     * @param requestUrl the requestUrl to set
     */
    public void setRequestUrl(String requestUrl) {
        this.requestUrl = requestUrl;
    }

    /**
     * @return String return the channelCode
     */
    public String getChannelCode() {
        return channelCode;
    }

    /**
     * @param channelCode the channelCode to set
     */
    public void setChannelCode(String channelCode) {
        this.channelCode = channelCode;
    }

    /**
     * @return String return the keyOwner
     */
    public String getKeyOwner() {
        return keyOwner;
    }

    /**
     * @param keyOwner the keyOwner to set
     */
    public void setKeyOwner(String keyOwner) {
        this.keyOwner = keyOwner;
    }

    /**
     * @return String return the requestorSystemCode
     */
    public String getRequestorSystemCode() {
        return requestorSystemCode;
    }

    /**
     * @param requestorSystemCode the requestorSystemCode to set
     */
    public void setRequestorSystemCode(String requestorSystemCode) {
        this.requestorSystemCode = requestorSystemCode;
    }

    /**
     * @return String return the initiatorIdentifierType
     */
    public String getInitiatorIdentifierType() {
        return initiatorIdentifierType;
    }

    /**
     * @param initiatorIdentifierType the initiatorIdentifierType to set
     */
    public void setInitiatorIdentifierType(String initiatorIdentifierType) {
        this.initiatorIdentifierType = initiatorIdentifierType;
    }

    /**
     * @return String return the initiatorIdentifier
     */
    public String getInitiatorIdentifier() {
        return initiatorIdentifier;
    }

    /**
     * @param initiatorIdentifier the initiatorIdentifier to set
     */
    public void setInitiatorIdentifier(String initiatorIdentifier) {
        this.initiatorIdentifier = initiatorIdentifier;
    }

    /**
     * @return String return the initiatorSecurityCredential
     */
    public String getInitiatorSecurityCredential() {
        return initiatorSecurityCredential;
    }

    /**
     * @param initiatorSecurityCredential the initiatorSecurityCredential to set
     */
    public void setInitiatorSecurityCredential(String initiatorSecurityCredential) {
        this.initiatorSecurityCredential = initiatorSecurityCredential;
    }

    /**
     * @return String return the callerType
     */
    public String getCallerType() {
        return callerType;
    }

    /**
     * @param callerType the callerType to set
     */
    public void setCallerType(String callerType) {
        this.callerType = callerType;
    }

    /**
     * @return String return the password
     */
    public String getPassword() {
        return password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * @return String return the receiverPartyIdentifier
     */
    public String getReceiverPartyIdentifier() {
        return receiverPartyIdentifier;
    }

    /**
     * @param receiverPartyIdentifier the receiverPartyIdentifier to set
     */
    public void setReceiverPartyIdentifier(String receiverPartyIdentifier) {
        this.receiverPartyIdentifier = receiverPartyIdentifier;
    }

    /**
     * @return String return the redisRouteCode
     */
    public String getRedisRouteCode() {
        return redisRouteCode;
    }

    /**
     * @param redisRouteCode the redisRouteCode to set
     */
    public void setRedisRouteCode(String redisRouteCode) {
        this.redisRouteCode = redisRouteCode;
    }

    /**
     * @return String return the redisAuthorization
     */
    public String getRedisAuthorization() {
        return redisAuthorization;
    }

    /**
     * @param redisAuthorization the redisAuthorization to set
     */
    public void setRedisAuthorization(String redisAuthorization) {
        this.redisAuthorization = redisAuthorization;
    }

    /**
     * @return String return the redisMessageId
     */
    public String getRedisMessageId() {
        return redisMessageId;
    }

    /**
     * @param redisMessageId the redisMessageId to set
     */
    public void setRedisMessageId(String redisMessageId) {
        this.redisMessageId = redisMessageId;
    }

    /**
     * @return String return the redisChannelCode
     */
    public String getRedisChannelCode() {
        return redisChannelCode;
    }

    /**
     * @param redisChannelCode the redisChannelCode to set
     */
    public void setRedisChannelCode(String redisChannelCode) {
        this.redisChannelCode = redisChannelCode;
    }

    /**
     * @return String return the redisUrl
     */
    public String getRedisUrl() {
        return redisUrl;
    }

    /**
     * @param redisUrl the redisUrl to set
     */
    public void setRedisUrl(String redisUrl) {
        this.redisUrl = redisUrl;
    }

    /**
     * @return String return the initiatorShortCode
     */
    public String getInitiatorShortCode() {
        return initiatorShortCode;
    }

    /**
     * @param initiatorShortCode the initiatorShortCode to set
     */
    public void setInitiatorShortCode(String initiatorShortCode) {
        this.initiatorShortCode = initiatorShortCode;
    }


    /**
     * @return String return the callerThirdPartyId
     */
    public String getCallerThirdPartyId() {
        return callerThirdPartyId;
    }

    /**
     * @param callerThirdPartyId the callerThirdPartyId to set
     */
    public void setCallerThirdPartyId(String callerThirdPartyId) {
        this.callerThirdPartyId = callerThirdPartyId;
    }
    
    /**
     * @return String return the systemCallbackUrl
     */
    public String getSystemCallbackUrl() {
        return systemCallbackUrl;
    }

    /**
     * @param systemCallbackUrl the systemCallbackUrl to set
     */
    public void setSystemCallbackUrl(String systemCallbackUrl) {
        this.systemCallbackUrl = systemCallbackUrl;
    }


    /**
     * @return String return the receiverPartyIdentifierType
     */
    public String getReceiverPartyIdentifierType() {
        return receiverPartyIdentifierType;
    }

    /**
     * @param receiverPartyIdentifierType the receiverPartyIdentifierType to set
     */
    public void setReceiverPartyIdentifierType(String receiverPartyIdentifierType) {
        this.receiverPartyIdentifierType = receiverPartyIdentifierType;
    }

}
