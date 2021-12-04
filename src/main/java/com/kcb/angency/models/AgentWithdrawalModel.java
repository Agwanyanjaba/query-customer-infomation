package com.kcb.angency.models;

public class AgentWithdrawalModel {
    private String originatorConversationID;
    private String thirdPartyID;
    private String callBackUrl;
    private String identifier;
    private String voucheValidationValue;
    private String externalID;
    private String posDeviceValue;
    private double transactionAmount;

    //Set getters and setters    

    /**
     * @return String return the originatorConversationID
     */
    public String getOriginatorConversationID() {
        return originatorConversationID;
    }

    /**
     * @param originatorConversationID the originatorConversationID to set
     */
    public void setOriginatorConversationID(String originatorConversationID) {
        this.originatorConversationID = originatorConversationID;
    }

    /**
     * @return String return the thirdPartyID
     */
    public String getThirdPartyID() {
        return thirdPartyID;
    }

    /**
     * @param thirdPartyID the thirdPartyID to set
     */
    public void setThirdPartyID(String thirdPartyID) {
        this.thirdPartyID = thirdPartyID;
    }

    /**
     * @return String return the callBackUrl
     */
    public String getCallBackUrl() {
        return callBackUrl;
    }

    /**
     * @param callBackUrl the callBackUrl to set
     */
    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }

    /**
     * @return String return the identifier
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @param identifier the identifier to set
     */
    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * @return String return the voucheValidationValue
     */
    public String getVoucheValidationValue() {
        return voucheValidationValue;
    }

    /**
     * @param voucheValidationValue the voucheValidationValue to set
     */
    public void setVoucheValidationValue(String voucheValidationValue) {
        this.voucheValidationValue = voucheValidationValue;
    }

    /**
     * @return String return the externalID
     */
    public String getExternalID() {
        return externalID;
    }

    /**
     * @param externalID the externalID to set
     */
    public void setExternalID(String externalID) {
        this.externalID = externalID;
    }

    /**
     * @return String return the posDeviceValue
     */
    public String getPosDeviceValue() {
        return posDeviceValue;
    }

    /**
     * @param posDeviceValue the posDeviceValue to set
     */
    public void setPosDeviceValue(String posDeviceValue) {
        this.posDeviceValue = posDeviceValue;
    }

    /**
     * @return double return the transactionAmount
     */
    public double getTransactionAmount() {
        return transactionAmount;
    }

    /**
     * @param transactionAmount the transactionAmount to set
     */
    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

}
