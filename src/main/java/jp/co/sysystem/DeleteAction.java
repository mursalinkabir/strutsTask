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



	public String execute() throws Exception {
		db.connect();
		String sql="SELECT user.ID,user.PASS,user.NAME,user.KANA,userdetail.BIRTH,userdetail.CLUB FROM userinfo.user INNER JOIN userinfo.userdetail ON user.ID=userdetail.ID WHERE user.ID=?";	
		User userdata = db.selectUser(sql, uid);
		this.setUserBean(userdata);
		userSession.put("userData", userBean);
		return INPUT; // all well
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
