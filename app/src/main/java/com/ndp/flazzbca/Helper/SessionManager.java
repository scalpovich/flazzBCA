package com.ndp.flazzbca.Helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by safeMode on 10/02/2016.
 */
public class SessionManager {

    private SharedPreferences.Editor editor;
    private SharedPreferences sharePref;

    Context context;

    private int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "midtrans";

    public SessionManager(Context context) {
        this.context = context;
        sharePref = context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = sharePref.edit();
        editor.apply();
    }

    public void setTid(String TID){
        editor.putString("TerminalID", TID);
        editor.commit();
    }

    public String getTid(){
        return sharePref.getString("TerminalID", "");
    }

    public void setMid(String Mid){
        editor.putString("MerchantID", Mid);
        editor.commit();
    }

    public String getMid(){
        return sharePref.getString("MerchantID", "");
    }

    public void setMerchantName(String merchantName){
        editor.putString("MerchantName", merchantName);
        editor.commit();
    }

    public String getMerchantName(){
        return sharePref.getString("MerchantName", "");
    }

    public void setAddress(String merchantName){
        editor.putString("Address", merchantName);
        editor.commit();
    }

    public String getAddress(){
        return sharePref.getString("Address", "");
    }

    public void setFTPPORT(String Ip_Port){
        editor.putString("FTP Port", Ip_Port);
        editor.commit();
    }

    public String getFTPPORT(){
        return sharePref.getString("FTP Port", "");
    }

    public void setFTPHost(String ServerPort){
        editor.putString("FTP Host", ServerPort);
        editor.commit();
    }

    public String getFTPHost(){
        return sharePref.getString("FTP Host", "");
    }

    public void setFTPUser(String ServerPort){
        editor.putString("FTP User", ServerPort);
        editor.commit();
    }

    public String getFTPUser(){
        return sharePref.getString("FTP User", "");
    }

    public void setFTPPass(String ServerPort){
        editor.putString("FTP Pass", ServerPort);
        editor.commit();
    }

    public String getFTPPass(){
        return sharePref.getString("FTP Pass", "");
    }

    public void setFTPPath(String ServerPort){
        editor.putString("FTP Path", ServerPort);
        editor.commit();
    }

    public String getFTPPath(){
        return sharePref.getString("FTP Path", "");
    }

    public void setSams(String Sams){
        editor.putString("SAMS", Sams);
        editor.commit();
    }

    public String getSams(){
        return sharePref.getString("SAMS", "");
    }

//    public void setTraceNo(int traceNo)
//    {
//        editor.putInt(Constant.TraceNo, traceNo);
//        editor.commit();
//    }
//    public int getTraceNo(){return sharePref.getInt(Constant.TraceNo,0);}
//
//    public void setTraceRefund(int traceRefund)
//    {
//        editor.putInt(Constant.TraceRefund, traceRefund);
//        editor.commit();
//    }
//    public int getTraceRefund(){return sharePref.getInt(Constant.TraceRefund,0);}
//
//    public void setPaymentType(String paymentType)
//    {
//        editor.putString(Constant.PaymentType, paymentType);
//        editor.commit();
//    }
//    public String getPaymentType(){return sharePref.getString(Constant.PaymentType,"");}
//
//    public void setOrderId(String orderId)
//    {
//        editor.putString(Constant.OrderId, orderId);
//        editor.commit();
//    }
//    public String getOrderId(){return sharePref.getString(Constant.OrderId,"");}
//
//    public void setGrossAmount(String grossAmount) {
//        editor.putString(Constant.GrossAmount, grossAmount);
//        editor.commit();
//    }
//    public String getGrossAmount(){return sharePref.getString(Constant.GrossAmount,"");}
//
//    public void setStatusCode(String statusCode)
//    {
//        editor.putString(Constant.StatusCode, statusCode);
//        editor.commit();
//    }
//    public String getStatusCode(){return sharePref.getString(Constant.StatusCode,"");}
//
//    public void setCurrency(String currency)
//    {
//        editor.putString(Constant.Currency, currency);
//        editor.commit();
//    }
//    public String getCurrency(){return sharePref.getString(Constant.Currency,"");}

    public void setTimeout(String Timeout) {
        editor.putString("timeout", Timeout);
        editor.commit();
    }

    public String getTimeout() {
        return sharePref.getString("timeout", "");
    }

//    public void setTrxTime(String trxTime)
//    {
//        editor.putString(Constant.TrxTime, trxTime);
//        editor.commit();
//    }
//    public String getTrxTime(){return sharePref.getString(Constant.TrxTime,"");}
//
//    public void setTrxId(String trxId)
//    {
//        editor.putString(Constant.TrxId, trxId);
//        editor.commit();
//    }
//    public String getTrxId(){return sharePref.getString(Constant.TrxId,"");}
//
//    public void setTrxStatus(String trxStatus)
//    {
//        editor.putString(Constant.TrxStatus, trxStatus);
//        editor.commit();
//    }
//    public String getTrxStatus(){return sharePref.getString(Constant.TrxStatus,"");}
//
//    public void setFraudStatus(String fraudStatus)
//    {
//        editor.putString(Constant.FraudStatus, fraudStatus);
//        editor.commit();
//    }
//    public String getFraudStatus(){return sharePref.getString(Constant.FraudStatus,"");}
//
//    public void setSettleTime(String settleTime)
//    {
//        editor.putString(Constant.SettleTime, settleTime);
//        editor.commit();
//    }
//    public String getSettleTime(){return sharePref.getString(Constant.SettleTime,"");}
//
//    public void setSignatureKey(String signatureKey)
//    {
//        editor.putString(Constant.SignatureKey, signatureKey);
//        editor.commit();
//    }
//    public String getSignatureKey(){return sharePref.getString(Constant.SignatureKey,"");}
//
//    public void setQRUrl(String qrUrl)
//    {
//        editor.putString(Constant.QR_URL, qrUrl);
//        editor.commit();
//    }
//    public String getQRUrl(){return sharePref.getString(Constant.QR_URL,"");}
//
//    public void setDeeplinkUrl(String deeplinkUrlUrl)
//    {
//        editor.putString(Constant.DeepLink_URL, deeplinkUrlUrl);
//        editor.commit();
//    }
//    public String getDeeplinkUrl(){return sharePref.getString(Constant.DeepLink_URL,"");}
//
//    public void setStatusUrl(String statusUrl)
//    {
//        editor.putString(Constant.Status_URL, statusUrl);
//        editor.commit();
//    }
//    public String getStatusUrl(){return sharePref.getString(Constant.Status_URL,"");}
//
//    public void setCancelUrl(String cancelUrl)
//    {
//        editor.putString(Constant.Cancel_URL, cancelUrl);
//        editor.commit();
//    }
//    public String getCancelUrl(){return sharePref.getString(Constant.Cancel_URL,"");}
//
//    public void setStatusMsg(String statusMsg){
//        editor.putString(Constant.STATUS_MSG, statusMsg);
//        editor.commit();
//    }
//
//    public String getStatusMsg(){
//        return sharePref.getString(Constant.STATUS_MSG, "");
//    }
//
//    public void setPrinterRet(int ret){
//        editor.putInt("printerRet", ret);
//        editor.commit();
//    }
//
//    public int getPrinterRet(){
//        return sharePref.getInt("printerRet", 0);
//    }
//
//    public void setPurchaseAmount(Long amount) {
//        editor.putLong(Constant.PURCHASE_AMOUNT, amount);
//        editor.commit();
//    }
//    public long getPurchaseAmount(){return sharePref.getLong(Constant.PURCHASE_AMOUNT,0);}
//
//    public void setTipAmount(Long amount) {
//        editor.putLong("Tip", amount);
//        editor.commit();
//    }
//    public long getTipAmount(){return sharePref.getLong("Tip",0);}
//
//    public void setTrx(String trx)
//    {
//        editor.putString(Constant.Trx, trx);
//        editor.commit();
//    }
//    public String getTrx(){return sharePref.getString(Constant.Trx,"");}

    //    =================================PAXSTORE=================================

    public String getAddress1(){
        return sharePref.getString("address1", "");
    }

    public void setAddress1(String address1){
        editor.putString("address1", address1);
        editor.commit();
    }

    public String getAddress2(){
        return sharePref.getString("Address2", "");
    }

    public void setAddress2(String address2){
        editor.putString("Address2", address2);
        editor.commit();
    }

    public String getAddress3(){
        return sharePref.getString("Address3", "");
    }

    public void setAddress3(String address3){
        editor.putString("Address3", address3);
        editor.commit();
    }

    public String getAddress4(){
        return sharePref.getString("Address4", "");
    }

    public void setAddress4(String address3){
        editor.putString("Address4", address3);
        editor.commit();
    }


//    public void setTid(String tid){
//        editor.putString("terminalId", tid);
//        editor.commit();
//    }

    public String getAuthUser(){
        return sharePref.getString("AuthUser", "");
    }

    public void setAuthUser(String authUser){
        editor.putString("AuthUser", authUser);
        editor.commit();
    }

    public String getAuthPass(){
        return sharePref.getString("AuthPass", "");
    }

    public void setAuthPass(String authPass){
        editor.putString("AuthPass", authPass);
        editor.commit();
    }

    public String getCustFirstName(){
        return sharePref.getString("FirstName", "");
    }

    public void setCustFirstName(String firstName){
        editor.putString("FirstName", firstName);
        editor.commit();
    }

    public String getCustLastName(){
        return sharePref.getString("LastName", "");
    }

    public void setCustLastName(String lastName){
        editor.putString("LastName", lastName);
        editor.commit();
    }

    public String getCustEmail(){
        return sharePref.getString("Email", "");
    }

    public void setCustEmail(String custEmail){
        editor.putString("Email", custEmail);
        editor.commit();
    }

    public String getCustPhone(){
        return sharePref.getString("Phone", "");
    }

    public void setCustPhone(String custPhone){
        editor.putString("Phone", custPhone);
        editor.commit();
    }

    public String getServerUrl(){
        return sharePref.getString("serverUrl", "");
    }

    public void setServerUrl(String serverUrl){
        editor.putString("serverUrl", serverUrl);
        editor.commit();
    }

    public int getTimer(){
        return sharePref.getInt("Timer", 60);
    }

    public void setTimer(int timer){
        editor.putInt("Timer", timer);
        editor.commit();
    }

    public boolean getDisablePrint(){
        return sharePref.getBoolean("disablePrint", false);
    }

    public void setDisablePrint(boolean disablePrint){
        editor.putBoolean("disablePrint", disablePrint);
        editor.commit();
    }

    public void setSecretKey(String SecretKey)
    {
        editor.putString(Constant.SecretKey, SecretKey);
        editor.commit();
    }
    public String getSecretKey(){return sharePref.getString(Constant.SecretKey,"");}

    public void setClientId(String ClientId)
    {
        editor.putString(Constant.ClientId, ClientId);
        editor.commit();
    }
    public String getClientId(){return sharePref.getString(Constant.ClientId,"");}

    public void setClientSecret(String ClientSecret)
    {
        editor.putString(Constant.ClientSecret, ClientSecret);
        editor.commit();
    }
    public String getClientSecret(){return sharePref.getString(Constant.ClientSecret,"");}

    public String getWalletID(){
        return sharePref.getString("WalletID", "");
    }

    public void setWalletID(String WalletID){
        editor.putString("WalletID", WalletID);
        editor.commit();
    }

    //bca dina

    public void setResponseCode(String responseCode)
    {
        editor.putString(Constant.ResponseCode, responseCode);
        editor.commit();
    }
    public String getResponseCode(){return sharePref.getString(Constant.ResponseCode,"");}

    public void setResponseDescription(String responseDescription)
    {
        editor.putString(Constant.ResponseDescription, responseDescription);
        editor.commit();
    }
    public String getResponseDescription(){return sharePref.getString(Constant.ResponseDescription,"");}

    public void setTransactionNo(String transactionNo)
    {
        editor.putString(Constant.TransactionNo, transactionNo);
        editor.commit();
    }
    public String getTransactionNo(){return sharePref.getString(Constant.TransactionNo,"");}

    public void setReferenceNo(String referenceNo)
    {
        editor.putString(Constant.ReferenceNo, referenceNo);
        editor.commit();
    }
    public String getReferenceNo(){return sharePref.getString(Constant.ReferenceNo,"");}

    public void setQRISData(String qrisData)
    {
        editor.putString(Constant.QRISData, qrisData);
        editor.commit();
    }
    public String getQRISData(){return sharePref.getString(Constant.QRISData,"");}

    public void setAmount(String amount)
    {
        editor.putString(Constant.Amount, amount);
        editor.commit();
    }
    public String getAmount(){return sharePref.getString(Constant.Amount,"");}

    public void setPaymentStatus(String paymentStatus)
    {
        editor.putString(Constant.PaymentStatus, paymentStatus);
        editor.commit();
    }
    public String getPaymentStatus(){return sharePref.getString(Constant.PaymentStatus,"");}

    public void setPaymentReferenceNo(String paymentReferenceNo)
    {
        editor.putString(Constant.PaymentReferenceNo, paymentReferenceNo);
        editor.commit();
    }
    public String getPaymentReferenceNo(){return sharePref.getString(Constant.PaymentReferenceNo,"");}

    public void setPaymentDate(String paymentDate)
    {
        editor.putString(Constant.PaymentDate, paymentDate);
        editor.commit();
    }
    public String getPaymentDate(){return sharePref.getString(Constant.PaymentDate,"");}

    public void setIssuerID(String issuerID)
    {
        editor.putString(Constant.IssuerID, issuerID);
        editor.commit();
    }
    public String getIssuerID(){return sharePref.getString(Constant.IssuerID,"");}

    public void setIssuerName(String issuerName)
    {
        editor.putString(Constant.IssuerName, issuerName);
        editor.commit();
    }
    public String getIssuerName(){return sharePref.getString(Constant.IssuerName,"");}

    public void setRetrievalReferenceNo(String retrievalReferenceNo)
    {
        editor.putString(Constant.RetrievalReferenceNo, retrievalReferenceNo);
        editor.commit();
    }
    public String getRetrievalReferenceNo(){return sharePref.getString(Constant.RetrievalReferenceNo,"");}

    public void setToken(String Token)
    {
        editor.putString(Constant.Token, Token);
        editor.commit();
    }
    public String getToken(){return sharePref.getString(Constant.Token,"");}

    public void setTokenType(String TokenType)
    {
        editor.putString(Constant.TokenType, TokenType);
        editor.commit();
    }
    public String getTokenType(){return sharePref.getString(Constant.TokenType,"");}

    public void setTokenExp(String TokenExp)
    {
        editor.putString(Constant.TokenExp, TokenExp);
        editor.commit();
    }
    public String getTokenExp(){return sharePref.getString(Constant.TokenExp,"");}

    public String getCustName(){
        return sharePref.getString("CustName", "");
    }

    public void setCustName(String CustName){
        editor.putString("CustName", CustName);
        editor.commit();
    }

    public void setTrxFee(Long TrxFee) {
        editor.putLong("TrxFee", TrxFee);
        editor.commit();
    }
    public long getTrxFee(){return sharePref.getLong("TrxFee",0);}

    public String getTrxCurr(){
        return sharePref.getString("TrxCurr", "");
    }

    public void setTrxCurr(String TrxCurr){
        editor.putString("TrxCurr", TrxCurr);
        editor.commit();
    }

    //  add abdul 19 mei 2022

    public void setUrlAuth(String UrlAuth) {
        editor.putString("urlAuth", UrlAuth);
        editor.commit();
    }

    public String getUrlAuth() {
        return sharePref.getString("urlAuth", "");
    }

    public void setUrlGenQr(String UrlAuth) {
        editor.putString("genQr", UrlAuth);
        editor.commit();
    }

    public String getUrlGenQr() {
        return sharePref.getString("genQr", "");
    }

    public void setUrlInq(String UrlInq) {
        editor.putString("urlInq", UrlInq);
        editor.commit();
    }

    public String getUrlInq() {
        return sharePref.getString("urlInq", "");
    }

}
