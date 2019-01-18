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
public class UpdateAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 1L;
	DBAccess db = new DBAccess();
	private User userBean = new User();
	String uid;

	/**
	 * @return the uid
	 */
	public final String getUid() {
		return uid;
	}

	/**
	 * @param uid the uid to set
	 */
	public final void setUid(String uid) {
		this.uid = uid;
	}

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

	public boolean isAlphaNumeric(String s) {
		String pattern = "^[a-zA-Z0-9]*$";
		return s.matches(pattern);
	}

	public boolean isFullWidth(String s) {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int esw = UCharacter.getIntPropertyValue(c, UProperty.EAST_ASIAN_WIDTH);
			if (esw != 3) {
				return false;
			}
		}

		return true;
	}

	public boolean isHalfWidth(String s) {
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			int esw = UCharacter.getIntPropertyValue(c, UProperty.EAST_ASIAN_WIDTH);
			if (esw != 2) {
				return false;
			}
		}
		return true;

	}

	/**
	 * 
	 * This is the isThisDateValid method. This method will check if a date is
	 * valid.
	 * 
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

	@Override
	public String execute() throws Exception {
		db.connect();
		String sql = "SELECT user.ID,user.PASS,user.NAME,user.KANA,userdetail.BIRTH,userdetail.CLUB FROM userinfo.user INNER JOIN userinfo.userdetail ON user.ID=userdetail.ID WHERE user.ID=?";

		User userdata = db.selectUser(sql, uid);
		// fixing date format
		// String startTime = userBean.getBirth();
		String startDate = userdata.getBirth();
		SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
		java.util.Date utildate = sdf1.parse(startDate);
		SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy/MM/dd");
		String finaldate = sdf2.format(utildate);
		userdata.setBirth(finaldate.toString());
		
		this.setUserBean(userdata);
		return INPUT; // all well
	}

	public String UpdateUser() throws Exception {
		boolean errchq = false;
		if (userBean.getId() == null || userBean.getKana() == null || userBean.getUname() == null) {

			return INPUT;// First time page loads. We show page associated to INPUT result.

		} else {

			// adding field error
			if (userBean.getId().isEmpty()) {
				//addFieldError("userBean.id", "ID is needed");
				addActionError(MessagesConfig.MSE001);
				errchq = true;
			}
			if (userBean.getUname().isEmpty()) {
				//addFieldError("userBean.uname", MessagesConfig.MSE009);
				addActionError(MessagesConfig.MSE009);
				errchq = true;
			}
			if (userBean.getKana().isEmpty()) {
				//addFieldError("userBean.kana", MessagesConfig.MSE012);
				addActionError(MessagesConfig.MSE012);
				errchq = true;
			}
			if (userBean.getBirth().isEmpty()) {
				//addFieldError("userBean.birth", MessagesConfig.MSE016);
				addActionError(MessagesConfig.MSE016);
				errchq = true;
			}
			if (userBean.getClub().isEmpty()) {
				//addFieldError("userBean.club", MessagesConfig.MSE015);
				addActionError(MessagesConfig.MSE015);
				errchq = true;
			}

//			boolean isIdAlpha = isAlphaNumeric(userBean.getId());
//			boolean isNameFull = isHalfWidth(userBean.getUname());
//			boolean isKanaHalf = isHalfWidth(userBean.getKana());
//			boolean isBirthformatok = isThisDateValid(userBean.getBirth(), "yyyy/MM/dd");

//			if (!isIdAlpha) {
//				addActionError(MessagesConfig.MSE002);
//				return INPUT;
//			} else if (isNameFull) {
//				addActionError(MessagesConfig.MSE010);
//				return INPUT;
//			} else if (!isKanaHalf) {
//				addActionError(MessagesConfig.MSE013);
//				return INPUT;
//			} else if (!isBirthformatok) {
//				addActionError(MessagesConfig.MSE018);
//				return INPUT;
//			} else if (isFullWidth(userBean.getBirth())) {
//				addActionError(MessagesConfig.MSE017);
//				return INPUT;
//			} else if (isHalfWidth(userBean.getClub())) {
//				addActionError(MessagesConfig.MSE019);
//				return INPUT;
//			} else {
//				userSession.put("userData", userBean);
//				return SUCCESS; // all well
//
//			}

			
			if (userBean.getId().length() > 0) {
				boolean isIdAlpha = isAlphaNumeric(userBean.getId());
				if (!isIdAlpha) {
					addActionError(MessagesConfig.MSE002);
					errchq = true;
				} 
			}

			if (userBean.getBirth().length() > 0) {
				boolean isBirthformatok = isThisDateValid(userBean.getBirth(), "yyyy/MM/dd");
				if (!isBirthformatok) {
					addActionError(MessagesConfig.MSE018);
					errchq = true;
				}
				if (isFullWidth(userBean.getBirth())) {
					addActionError(MessagesConfig.MSE017);
					errchq = true;
				}
			}
		
			if (userBean.getUname().length() > 0) {
				boolean isNameFull = isHalfWidth(userBean.getUname());
				if (isNameFull) {
					addActionError(MessagesConfig.MSE010);
					errchq = true;
				}
			}
			if (userBean.getKana().length() > 0) {
				boolean isKanaHalf = isHalfWidth(userBean.getKana());
				if (!isKanaHalf) {
					addActionError(MessagesConfig.MSE013);
					errchq = true;
				}
			}

			if (userBean.getKana().length() > 0) {
				if (isHalfWidth(userBean.getClub())) {
					addActionError(MessagesConfig.MSE019);
					errchq = true;
				}
			}
			if (!errchq) {
				userSession.put("userData", userBean);
				return SUCCESS; // all well
			} else {
				return INPUT;// error is there
			}
			
			
		}

	}

	public User getUserBean() {
		return userBean;
	}

	public void setUserBean(User user) {
		userBean = user;
	}

	public String UserDBUpdate() {
		if (userBean != null) {
			userBean = (User) userSession.get("userData");
			db.connect();
			boolean flag = db.UpdateUser(userBean);
			if (flag) {
				addActionMessage("データを登録しました。");
				return SUCCESS;
			} else {
				addActionError("データの登録に失敗しました。");
				return ERROR;
			}
		} else {
			userBean = (User) userSession.get("userData");
			return INPUT;
		}

	}

}
