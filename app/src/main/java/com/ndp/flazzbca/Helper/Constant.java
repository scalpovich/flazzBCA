package com.ndp.flazzbca.Helper;

import com.ndp.flazzbca.Paxstore.DownloadManager;

import java.io.File;

/**
 * Created by safeMode on 3/21/2017.
 */

public class Constant {

    static final String TraceNo          = "TraceNo";
    static final String PaymentType      = "PaymentType";
    static final String OrderId          = "OrderId";
    static final String GrossAmount      = "GrossAmount";
    static final String StatusCode       = "StatusCode";
    static final String Currency         = "Currency";
    static final String TrxTime          = "TrxTime";
    static final String TrxId            = "TrxId";
    static final String TrxStatus        = "TrxStatus";
    static final String FraudStatus      = "FraudStatus";
    static final String SettleTime       = "SettleTime";
    static final String SignatureKey     = "SignatureKey";
    static final String QR_URL           = "QR_URL";
    static final String DeepLink_URL     = "Deeplink_URL";
    static final String Status_URL       = "Status_URL";
    static final String Cancel_URL       = "Cancel_URL";
    static final String Trx              = "Trx";

    //nobu dina
    static final String ResponseCode         = "ResponseCode";
    static final String ResponseDescription  = "ResponseDescription";
    static final String TransactionNo        = "TransactionNo";
    static final String ReferenceNo          = "ReferenceNo";
    static final String QRISData             = "QRISData";
    static final String Amount               = "Amount";
    static final String PaymentStatus        = "PaymentStatus";
    static final String PaymentReferenceNo   = "PaymentReferenceNo";
    static final String PaymentDate          = "PaymentDate";
    static final String IssuerID             = "IssuerID";
    static final String RetrievalReferenceNo = "RetrievalReferenceNo";
    static final String TraceRefund          = "TraceRefund";
    static final String IssuerName           = "IssuerName";
    static final String Token                = "Token";
    static final String TokenType            = "TokenType";
    static final String TokenExp             = "TokenExp";
    static final String SecretKey            = "SecretKey";
    static final String ClientId             = "ClientId";
    static final String ClientSecret         = "ClientSecret";

    static final String PURCHASE_AMOUNT  = "PURCHASE_AMOUNT";

    public static final String CHARGE    = "CHARGE";
    public static final String INQUIRY   = "INQUIRY";   //nobu dina
    public static final String CANCEL    = "CANCEL";    //nobu dina
    public static final String REFUND    = "REFUND";
    public static final String TOKEN     = "TOKEN";
    static final String STATUS_MSG       = "STATUS_MSG";
    public static final String XML_PATH  = "flazz.xml";
    //    =================== CONFIG ===========================
    static final int timer  = 300;
    static final String merchant_name    = "Smartweb Indonesia";
    static final String address1         = "Jl. Daksinapati Timur 3";
    static final String address2         = "Pulogadung";
    static final String address3         = "Jakarta Timur";
    static final String mid              = "936005030000048806";
    static final String tid              = "A1026229";
    static final String curr             = "IDR";
    static final String client_id        = "4decd7840e4653b723c27fffee3abee1";//"Mid-server-Noph9d4zIHJ3rF7NKM2SUvq1";// "SB-Mid-server-370zEzJgFs1eH8ZbpBfZpg_M";
    static final String client_secret    = "ee7aa6bae143446fe75b6d44f65975c6d738a94f1e8228da0c72299c3c1fde66";
    static final String cust_name        = "George";
    static final String cust_email       = "george.f@smartwebindonesia.com";
    static final String cust_phone       = "62816272627";
    static final String server_url       = "https://qris-dev.novapay.id";//"https://api.midtrans.com/v2/"; // https://api.sandbox.midtrans.com/v2/
  /*
    static final int timer  = 0;
    static final String merchant_name    = "";
    static final String address1         = "";
    static final String address2         = "";
    static final String address3         = "";
    static final String mid              = "";
    static final String auth_user        = "";
    static final String auth_pass        = "";
    static final String cust_firstName   = "";
    static final String cust_lastName    = "";
    static final String cust_email       = "";
    static final String cust_phone       = "";
    static final String server_url       = "";
    */

    public static final String CERT_PATH = DownloadManager.getInstance().getFilePath() + File.separator + "intermediate.crt";
}

