/**
 * jp.co.sysystem package.
 */
package jp.co.sysystem;

import org.apache.struts2.interceptor.SessionAware;

import com.ibm.icu.lang.UCharacter;
import com.ibm.icu.lang.UProperty;
import com.opensymphony.xwork2.ActionSupport;

/**
 * RegisterAction.java class This class will......
 * 
 * @author SYSYSTEM/work
 * @update Nov 28, 2018
 */
public class DeleteAction extends ActionSupport implements SessionAware {
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

	public String execute() throws Exception {
		db.connect();
		String sql="SELECT user.ID,user.PASS,user.NAME,user.KANA,userdetail.BIRTH,userdetail.CLUB FROM userinfo.user INNER JOIN userinfo.userdetail ON user.ID=userdetail.ID WHERE user.ID=?";	
		User userdata = db.selectUser(sql, uid);
		this.setUserBean(userdata);
		userSession.put("userData", userBean);
		return INPUT; // all well
	}

	public String UpdateUser() throws Exception {
	
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
			
			boolean isIdAlpha = isAlphaNumeric(userBean.getId());
			boolean isPwAlpha = isAlphaNumeric(userBean.getPword());
			boolean isNameFull = isHalfWidth(userBean.getUname());
			boolean isKanaHalf = isHalfWidth(userBean.getKana());
//			boolean isBirthokay = true;
			
//			try {
//				SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
//				java.util.Date date  = formatter1.parse(userBean.getBirth());
//				java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());  
//				
//
//			} catch (Exception e) {
//
//				isBirthokay = false;
//			}

			if (!isIdAlpha) {
				addActionError(MessagesConfig.MSE002);
				return INPUT;
			} else if (!isPwAlpha) {
				addActionError(MessagesConfig.MSE006);
				return INPUT;
			} else if (isNameFull) {
				addActionError(MessagesConfig.MSE010);
				return INPUT;
			} else if (!isKanaHalf) {
				addActionError(MessagesConfig.MSE013);
				return INPUT;
			} 
			else if (isHalfWidth(userBean.getClub())) {
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

	public String UserDBUpdate() {
		if(userBean!=null) {
			userBean = (User) userSession.get("userData");
			db.connect();
			boolean flag = db.DeleteUser(userBean);
			if (flag) {
				addActionMessage("データを登録しました。");
				return SUCCESS;
			}else {
				addActionError("データの登録に失敗しました。");
				return ERROR;
			}
		}else {
			userBean = (User) userSession.get("userData");	
			return INPUT;
		}
		

		
	}

}
