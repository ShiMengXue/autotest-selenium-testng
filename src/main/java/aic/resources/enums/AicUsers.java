package aic.resources.enums;

import java.util.HashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import shared.user.User;

/**
 * existing users that test need
 * 
 * @author mengxue.shi
 * 
 */
public enum AicUsers
{
	/**
	 * members: email, password
	 */

	//@formatter:off
	
	//free user
	US_FREE_USER("dil42666+us+frauto@adobetest.com","3edc$RFV",0),
	GB_FREE_USER("dil42666+gb+frauto@adobetest.com","3edc$RFV",0),
	FR_FREE_USER("dil42666+fr+frauto@adobetest.com","3edc$RFV",0),
	DE_FREE_USER("dil42666+de+frauto@adobetest.com","3edc$RFV",0),
	DK_FREE_USER("dil42666+dk+frauto@adobetest.com","3edc$RFV",0),
	SE_FREE_USER("dil42666+se+frauto@adobetest.com","3edc$RFV",0),
	BR_FREE_USER("dil42666+br+frauto@adobetest.com","3edc$RFV",0),
	JP_FREE_USER("dil42666+jp+frauto@adobetest.com","3edc$RFV",0),
	ES_FREE_USER("dil42666+es+frauto@adobetest.com","3edc$RFV",0),
	NO_FREE_USER("dil42666+no+frauto@adobetest.com","3edc$RFV",0),
	FI_FREE_USER("dil42666+fi+frauto@adobetest.com","3edc$RFV",0),
	NL_FREE_USER("dil42666+nl+frauto@adobetest.com","3edc$RFV",0),
	IT_FREE_USER("dil42666+it+frauto@adobetest.com","3edc$RFV",0),
	
	
	//epdf user
	US_EPDF_USER("dil42666+us+epdfauto@adobetest.com","3edc$RFV",1),
	GB_EPDF_USER("dil42666+gb+epdfauto@adobetest.com","3edc$RFV",1),
	FR_EPDF_USER("dil42666+fr+epdfauto@adobetest.com","3edc$RFV",1),
	DE_EPDF_USER("dil42666+de+epdfauto@adobetest.com","3edc$RFV",1),
	DK_EPDF_USER("dil42666+dk+epdfauto@adobetest.com","3edc$RFV",1),
	SE_EPDF_USER("dil42666+se+epdfauto@adobetest.com","3edc$RFV",1),
	BR_EPDF_USER("dil42666+br+epdfauto@adobetest.com","3edc$RFV",1),
	JP_EPDF_USER("dil42666+jp+epdfauto@adobetest.com","3edc$RFV",1),
	ES_EPDF_USER("dil42666+es+epdfauto@adobetest.com","3edc$RFV",1),
	NO_EPDF_USER("dil42666+no+epdfauto@adobetest.com","3edc$RFV",1),
	FI_EPDF_USER("dil42666+fi+epdfauto@adobetest.com","3edc$RFV",1),
	NL_EPDF_USER("dil42666+nl+epdfauto@adobetest.com","3edc$RFV",1),
	IT_EPDF_USER("dil42666+it+epdfauto@adobetest.com","3edc$RFV",1),
	
	//pdf pack user
	US_PDF_PACK_USER("dil42666+us+cpdfauto@adobetest.com","3edc$RFV",2),
	GB_PDF_PACK_USER("dil42666+gb+cpdfauto@adobetest.com","3edc$RFV",2),
	FR_PDF_PACK_USER("dil42666+fr+cpdfauto@adobetest.com","3edc$RFV",2),
	DE_PDF_PACK_USER("dil42666+de+cpdfauto@adobetest.com","3edc$RFV",2),
	DK_PDF_PACK_USER("dil42666+dk+cpdfauto@adobetest.com","3edc$RFV",2),
	SE_PDF_PACK_USER("dil42666+se+cpdfauto@adobetest.com","3edc$RFV",2),
	BR_PDF_PACK_USER("dil42666+br+cpdfauto@adobetest.com","3edc$RFV",2),
	JP_PDF_PACK_USER("dil42666+jp+cpdfauto@adobetest.com","3edc$RFV",2),
	ES_PDF_PACK_USER("dil42666+es+cpdfauto@adobetest.com","3edc$RFV",2),
	NO_PDF_PACK_USER("dil42666+no+cpdfauto@adobetest.com","3edc$RFV",2),
	FI_PDF_PACK_USER("dil42666+fi+cpdfauto@adobetest.com","3edc$RFV",2),
	NL_PDF_PACK_USER("dil42666+nl+cpdfauto@adobetest.com","3edc$RFV",2),
	IT_PDF_PACK_USER("dil42666+it+cpdfauto@adobetest.com","3edc$RFV",2);
	
	
	//@formatter:on

	private String email;
	private String pswd;
	private int type;
	private static final Logger LOG = LogManager.getLogger(AicUsers.class);

	public static final int TYPE_FREE = 0;
	public static final int TYPE_EPDF = 1;
	public static final int TYPE_PDF_PACK = 2;

	AicUsers(String email, String pswd, int type) {
		this.email = email;
		this.pswd = pswd;
		this.type = type;
	}

	public String getEmail() {
		return email;
	}

	public String getPswd() {
		return pswd;
	}

	public int getType() {
		return type;
	}

	/**
	 * Returns a User generated from this getUser
	 * 
	 * @return user created from this getUser
	 */
	public User getUser() {
		HashMap<String, Object> catMap = new HashMap<String, Object>();
		catMap.put("email", this.getEmail());
		catMap.put("password", this.getPswd());
		catMap.put("createinrenga", false);
		User user = new User(catMap);

		LOG.info("Retrieving user: " + this.getEmail());

		return user;
	}

	/**
	 * 
	 * @param countryCode
	 * @return one Free type AicUsers on countryCode region
	 */

	public static AicUsers getAicFreeUsers(String countryCode) {

		if ("us" == countryCode) {
			return US_FREE_USER;
		} else {
			for (AicUsers user : AicUsers.values()) {
				if (user.getType() == AicUsers.TYPE_FREE && user.getEmail().contains("+" + countryCode + "+")) {
					return user;
				}
			}
			LOG.info("Didn't find a Free type AicUsers that matches the user's country");
			return null;
		}
	}

	/**
	 * 
	 * @param countryCode
	 * @return one Export PDF type AicUsers on countryCode region
	 */

	public static AicUsers getAicEpdfUsers(String countryCode) {

		if ("us" == countryCode) {
			return US_EPDF_USER;
		} else {
			for (AicUsers user : AicUsers.values()) {
				if (user.getType() == AicUsers.TYPE_EPDF && user.getEmail().contains("+" + countryCode + "+")) {
					return user;
				}
			}
			LOG.info("Didn't find a Export PDF type AicUsers that matches the user's country");
			return null;
		}
	}

	/**
	 * 
	 * @param countryCode
	 * @return one PDF Pack type AicUsers on countryCode region
	 */

	public static AicUsers getAicPdfpackUsers(String countryCode) {

		if ("us" == countryCode) {
			return US_PDF_PACK_USER;
		} else {
			for (AicUsers user : AicUsers.values()) {
				if (user.getType() == AicUsers.TYPE_PDF_PACK && user.getEmail().contains("+" + countryCode + "+")) {
					return user;
				}
			}
			LOG.info("Didn't find a PDF Pack type AicUsers that matches the user's country");
			return null;
		}
	}

}
