/**
 * jp.co.sysystem package.
 */
package jp.co.sysystem;

import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

/**
 * User.java class This class is user bean
 * 
 * @author SYSYSTEM/work
 * @update Dec 4, 2018
 */
public class User {
	private String id;
	private String pword;
	private String conpword;
	private String uname;
	private String kana;
	private String birth;
	private String club;

	/**
	 * @return the id
	 */
	@RequiredStringValidator(message = MessagesConfig.MSE001)
	public final String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public final void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the pword
	 */
	@RequiredStringValidator(message = MessagesConfig.MSE005)
	public final String getPword() {
		return pword;
	}

	/**
	 * @param pword the pword to set
	 */
	public final void setPword(String pword) {
		this.pword = pword;
	}

	/**
	 * @return the conpword
	 */
	@RequiredStringValidator(message = MessagesConfig.MSE005)
	public final String getConpword() {
		return conpword;
	}

	/**
	 * @param conpword the conpword to set
	 */
	public final void setConpword(String conpword) {
		this.conpword = conpword;
	}

	/**
	 * @return the uname
	 */
	@RequiredStringValidator(message = MessagesConfig.MSE009)
	public final String getUname() {
		return uname;
	}

	/**
	 * @param uname the uname to set
	 */
	public final void setUname(String uname) {
		this.uname = uname;
	}

	/**
	 * @return the kana
	 */
	@RequiredStringValidator(message = MessagesConfig.MSE012)
	public final String getKana() {
		return kana;
	}

	/**
	 * @param kana the kana to set
	 */
	public final void setKana(String kana) {
		this.kana = kana;
	}

	/**
	 * @return the birth
	 */
	@RequiredStringValidator(message = MessagesConfig.MSE016)
	public final String getBirth() {
		return birth;
	}

	/**
	 * @param birth the birth to set
	 */
	public final void setBirth(String birth) {
		this.birth = birth;
	}

	/**
	 * @return the club
	 */
	@RequiredStringValidator(message = MessagesConfig.MSE015)
	public final String getClub() {
		return club;
	}

	/**
	 * @param club the club to set
	 */
	public final void setClub(String club) {
		this.club = club;
	}
	
    public String toString() {
        return "ID: " + getId() + " Passowrds:  " + getPword() + 
        " UserName:      " + getUname() + " Kana:      " + getKana()+ 
        " Birthday:      " + getBirth() + 
        " Club:      " + getClub() ;
    }
   
	/**
	 * This is the setBirth method.
	 * This method will ......
	 * @param finalBirth
	 * @return void
	 * @exception IOException on Input.
	 */
	
}
