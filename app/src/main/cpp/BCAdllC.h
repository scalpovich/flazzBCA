/*
 * BCAdllC.h
 *
 *  Created on: Feb 23, 2017
 *      Author: ALB
 */

#ifndef BCADLLC_H_
#define BCADLLC_H_

#ifdef __cplusplus
extern "C" {
#endif


#define TRUE							1
#define FALSE							0

#define LENGTH_LOG_RSP					190		// MAX.189 + 1,	Length of LOG Response: XXXX (Response Code) + 185 (Log/Message Response)	lenLogRsp
#define LENGTH_STR_CONFIG				31		// Fix.30 + 1	// Length of 'strConfig'
#define LENGTH_STR_CARDNO				17		// Fix.16 + 1	// Length of 'CardNo'
#define LENGTH_STR_DATETIME				15		// Fix.14 + 1	// Length of 'strCurrDateTime' / yyyyMMddHHmmss


// -------------------------------------------------------------------------------------


/*
 * ********************************** SET BCA CONFIG *******************************************
 * 	Input:
 * 			strConfig		:	SET Configuration of DLL as string
 * 								Length: LENGTH_STR_CONFIG
 * 	Output:
 * 			strLogRsp		:	Log/Data Response
 * 								Max.Length: LENGTH_LOG_RSP
 * 			return function	:	TRUE as Successful
 * 								FALSE as Failed
 *
 * *********************************************************************************************
 */
unsigned char BCASetConfig(unsigned char *strConfig, unsigned char *strLogRsp);

/*
 * ********************************** GET BCA CONFIG *******************************************
 * 	Output:
 * 			strConfig		:	GET Configuration of DLL as string
 * 								Length: LENGTH_STR_CONFIG
 * 			strLogRsp		:	Log/Data Response
 * 								Max.Length: LENGTH_LOG_RSP
 * 			return function	:	TRUE as Successful
 * 								FALSE as Failed
 *
 * *********************************************************************************************
 */
unsigned char BCAGetconfig(unsigned char *strConfig, unsigned char *strLogRsp);

/*
 * ********************************** VERSION BCA DLL ******************************************
 * 	Output:
 * 			strLogRsp		:	Log/Data Response
 * 								Max.Length: LENGTH_LOG_RSP
 * 			return function	:	TRUE as Successful
 * 								FALSE as Failed
 *
 * *********************************************************************************************
 */
unsigned char BCAVersionDll(unsigned char *strLogRsp);

/*
 * ***************************************** IS MY CARD ****************************************
 * 	Output:
 * 			strLogRsp		:	Log/Data Response
 * 								Max.Length: LENGTH_LOG_RSP
 * 			return function	:	TRUE as Successful
 * 								FALSE as Failed
 *
 * *********************************************************************************************
 */
unsigned char BCAIsMyCard(unsigned char *strLogRsp);

/*
 * *************************************** CHECK BALANCE ***************************************
 * 	Output:
 * 			Balance  		: 	Balance before transaction, size of variable at least 4 BYTE_BCAs
 * 			CardNo			:	Card Number
 * 								Length: LENGTH_STR_CARDNO
 * 			strLogRsp		:	Log/Data Response
 * 								Max.Length: LENGTH_LOG_RSP
 * 			return function	:	TRUE as Successful
 * 								FALSE as Failed
 *
 * *********************************************************************************************
 */
unsigned char BCACheckBalance(long *Balance, unsigned char *CardNo, unsigned char *strLogRsp);

/*
 * ***************************************** INITial SAM ***************************************
 * Input:
 * 			SAMSlotNo		:	Slot number of PSAM
 *
 * 	Output:
 * 			strLogRsp		:	Log/Data Response
 * 								Max.Length: LENGTH_LOG_RSP
 * 			return function	:	TRUE as Successful
 * 								FALSE as Failed
 *
 * *********************************************************************************************
 */
unsigned char BCAInitialSAM(int SAMSlotNo, unsigned char *strLogRsp);

/*
 * *************************************** DEBIT BALANCE ***************************************
 * Input:
 * 			SAMSlotNo		:	Slot number of PSAM
 * 			strCurrDateTime	:	Date Time of Current Transaction
 * 								Format: yyyyMMddHHmmss
 * 								Length: 14 of chars (LENGTH_STR_DATETIME)
 * 			DebitAmount		: 	Amount of Debit, size of variable at least 4 BYTE_BCAs
 *
 * 	Output:
 * 			BalBefore		: 	Balance before transaction, size of variable at least 4 BYTE_BCAs
 * 			BalAfter		:	Balance after transaction, size of variable at least 4 BYTE_BCAs
 * 			strLogRsp		:	Log/Data Response
 * 								Max.Length: LENGTH_LOG_RSP
 * 			return function	:	TRUE as Successful
 * 								FALSE as Failed
 *
 * *********************************************************************************************
 */
unsigned char BCADebitBalance(int SAMSlotNo, unsigned char *strCurrDateTime, long DebitAmount, long *BalBefore, long *BalAfter, unsigned char *strLogRsp);



/*
 * ////////////////////////////////////////////////////// Defined in Third Party's Object \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
 *
 *
 *
 */


	/*
	 * ******************************** SENDCONTACTLESSAPDU ****************************************
	 * Function to Send APDU's command to Contactless Reader
	 *
	 * Input:
	 * 			cmdBYTE_BCA			:	Command APDU, BCD format
	 * 			lenC			:	Length of Command APDU
	 *
	 * 	Output:
	 * 			RAPDU			:	Response of Command APDU
	 * 			lenR			:	Length of RAPDU
	 *
	 * 			return function	:	TRUE as Successful
	 * 								FALSE as Failed
	 *
	 * *********************************************************************************************
	 */
	unsigned char BCAsendContactlessAPDU(unsigned char* cmdBYTE_BCA, unsigned short lenC, unsigned char* RAPDU, unsigned short* lenR);

	/*
	 * *********************************** SENDCONTACTAPDU *****************************************
	 * Function to Send APDU's command to Contact Reader
	 * Input:
	 * 			SAMSlotNo		:	Slot Number of SAM
	 * 			cmdBYTE_BCA			:	Command APDU, BCD format
	 * 			lenC			:	Length of Command APDU
	 *
	 * 	Output:
	 * 			RAPDU			:	Response of Command APDU
	 * 			lenR			:	Length of RAPDU
	 *
	 * 			return function	:	TRUE as Successful
	 * 								FALSE as Failed
	 *
	 * *********************************************************************************************
	 */
	unsigned char BCAsendContactAPDU(int SAMSlotNo, unsigned char* cmdBYTE_BCA, unsigned short lenC, unsigned char* RAPDU, unsigned short* lenR);
	// -----------------------------------------------------------------------------------------------------------------------------------

/*
 * \\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\//////////////////////////////////////////////////////////////
 */


#ifdef __cplusplus
}
#endif
#endif /* BCADLLC_H_ */
