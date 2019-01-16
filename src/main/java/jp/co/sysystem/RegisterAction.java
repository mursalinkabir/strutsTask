/**
 * jp.co.sysystem package.
 */
package jp.co.sysystem;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UProperty;
import com.opensymphony.xwork2.ActionSupport;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.struts2.interceptor.SessionAware;
import org.springframework.expression.ParseException;

/**
 * RegisterAction.java class This class will......
 * 
 * @author SYSYSTEM/work
 * @update Nov 28, 2018
 */
public class RegisterAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 1L;
	DBAccess db = new DBAccess();
	private User userBean = new User();
	// For SessionAware
	private java.util.Map<String, Object> userSession;

	@Override
	public void setSession(java.util.Map<String, Object> session) {
		userSession = session;
	}

	/**
	 * @return the userSession
	 */
	public final java.util.Map<String, Object> getUserSession() {
		return userSession;
	}

	@Override
	public void validate() { //
		if (userBean.getId() == null || userBean.getKana() == null || userBean.getUname() == null) { // First time page
																										// loads,
																										// student is
																										// null.
			setFieldErrors(null); // We clear all the validation errors that strut stupidly found since there was
									// not form submission.
		}
	}
	/**
	 * 
	 * This is the isThisDateValid method.
	 * This method will check if a date is valid.
	 * @param dateToValidate
	 * @param dateFromat
	 * @return boolean
	 * @exception ParseException on Input.
	 */
	public boolean isThisDateValid(String dateToValidate, String dateFromat) {

		if (dateToValidate == null) {
			return false;
		}

		SimpleDateFormat sdf = new SimpleDateFormat(dateFromat);
		sdf.setLenient(false);

		try {

			// if not valid, it will throw ParseException
			Date date = sdf.parse(dateToValidate);
			System.out.println(date);

		} catch (java.text.ParseException e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean isAlphaNumeric(String s) {
		String pattern = "^[a-zA-Z0-9]*$";
		return s.matches(pattern);
	}

	public boolean isFullWidth(String s) {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int esw = UCharacter.getIntPropertyValue(c, UProperty.EAST_ASIAN_WIDTH);
			if (esw!=3) {
				return false;
			}
		}

		return true;
	}

	public boolean isHalfWidth(String s) {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int esw = UCharacter.getIntPropertyValue(c, UProperty.EAST_ASIAN_WIDTH);
			if (esw!=2) {
				return false;
			}
		}
		return true;

		
	}

	@Override
	public String execute() throws Exception {
		return INPUT; // all well
	}

	public String RegisterUser() throws Exception {

		if (userBean.getId() == null || userBean.getKana() == null || userBean.getUname() == null) {

			return INPUT;// First time page loads. We show page associated to INPUT result.

		} else {

			// adding field error
			if (userBean.getId().isEmpty()) {
				addFieldError("userBean.id", "ID is needed");
			}
			if (userBean.getPword().isEmpty()) {
				addFieldError("userBean.pword", MessagesConfig.MSE005);
			}
			if (userBean.getConpword().isEmpty()) {
				addFieldError("userBean.conpword", MessagesConfig.MSE005);
			}
			if (userBean.getUname().isEmpty()) {
				addFieldError("userBean.uname", MessagesConfig.MSE009);
			}
			if (userBean.getKana().isEmpty()) {
				addFieldError("userBean.kana", MessagesConfig.MSE012);
			}
			if (userBean.getBirth().isEmpty()) {
				addFieldError("userBean.birth", MessagesConfig.MSE016);
			}
			if (userBean.getClub().isEmpty()) {
				addFieldError("userBean.club", MessagesConfig.MSE015);
			}
			db.connect();
			boolean isIdAlpha = isAlphaNumeric(userBean.getId());
			boolean isPwAlpha = isAlphaNumeric(userBean.getPword());
			boolean isNameFull = isHalfWidth(userBean.getUname());
			boolean isKanaHalf = isHalfWidth(userBean.getKana());
			boolean isBirthformatok = isThisDateValid(userBean.getBirth(), "yyyy/MM/dd");

//			try {
//				SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
//				java.util.Date date = formatter1.parse(userBean.getBirth());
//				java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());
//
//
//
//			} catch (Exception e) {
//
//				isBirthokay = false;
//			}

			if (!isIdAlpha) {
				addActionError(MessagesConfig.MSE002);
				return INPUT;
			} else if (db.isExist(userBean.getId())) {
				addActionError(MessagesConfig.MSE003);
				return INPUT;
			} else if (!isBirthformatok) {
				addActionError(MessagesConfig.MSE018);
				return INPUT;
			} else if (!isPwAlpha) {
				addActionError(MessagesConfig.MSE006);
				return INPUT;
			} else if (!isAlphaNumeric(userBean.getConpword())) {
				addActionError(MessagesConfig.MSE008);
				return INPUT;
			} else if (!userBean.getPword().equals(userBean.getConpword())) {
				addActionError(MessagesConfig.MSE025);
				return INPUT;
			} else if (isNameFull) {
				addActionError(MessagesConfig.MSE010);
				return INPUT;
			} else if (!isKanaHalf) {
				addActionError(MessagesConfig.MSE013);
				return INPUT;
			}
//			else if (!isAlphaNumeric(userBean.getBirth().toString())) {
//				addActionError(MessagesConfig.MSE017);
//				return INPUT;
//			} 
			else if (isFullWidth(userBean.getBirth())) {
				addActionError(MessagesConfig.MSE017);
				return INPUT;
			} else if (isHalfWidth(userBean.getClub())) {
				addActionError(MessagesConfig.MSE019);
				return INPUT;
			} else {
				userSession.put("userData", userBean);
				return SUCCESS; // all well
			}

		}

	}

	public User getUserBean() {
		return userBean;
	}

	public void setUserBean(User user) {
		userBean = user;
	}

	public String UserInsert() {
		userBean = (User) userSession.get("userData");
		boolean flag = db.RegisterUser(userBean);
		if (flag) {
			addActionMessage("データを登録しました。");
			return SUCCESS;
		} else {
			addActionError("データの登録に失敗しました。");
			return ERROR;
		}

	}

}
