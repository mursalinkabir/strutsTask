/**
 * 
 */
package jp.co.sysystem;

import java.io.IOException;

import org.apache.struts2.interceptor.validation.SkipValidation;
import org.apache.struts2.interceptor.SessionAware;
import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;
/**
 * 
 * LoginAction.java class
 * This class will authorize login
 * @author SYSYSTEM/Mursalin
 * @update Nov 27, 2018
 */
public class LoginAction extends ActionSupport implements SessionAware {

	private static final long serialVersionUID = 1L;
	private String id;
	private String password;
	//for token
	String token;
	public void setToken(String token) { this.token = token; }
	public String getToken() { return token; }
	DBAccess db = new DBAccess();
	// connecting to db
	// For SessionAware
	private java.util.Map<String, Object> userSession;

	@Override
	public void setSession(java.util.Map<String, Object> session) {
			userSession = session;
		}
	/**
	 * 
	 * This is the getId method.
	 * @return
	 * @return String
	 * @exception IOException on Input.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */

	@RequiredStringValidator(message = MessagesConfig.MSE001)
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the password
	 */
	@RequiredStringValidator(message = MessagesConfig.MSE005)
	public String getPassword() {
		return password;
	}

	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	public String login() throws Exception {
		return this.execute();
	}


	@SkipValidation
	

	public String insertOrUpdate() {
		

		if (password == null || id == null) {
			return INPUT;// First time page loads. We show page associated to INPUT result.
		} 
		else {
			db.connect();
			// checking if id and password exists in db ,if not show separate errors.
			String sql1 = "SELECT * FROM userinfo.user WHERE ID=?";
			String sql2 = "SELECT * FROM userinfo.user WHERE PASS=?";
			
			String[][] IdCount = db.selectExec(sql1,id);
			String[][] PassCount = db.selectExec(sql2,password);
			boolean isPassAlpha= isAlphaNumeric(password);
			boolean isIdAlpha= isAlphaNumeric(id);
			//checking row count
			if (!isPassAlpha) {
				addActionError(MessagesConfig.MSE006);
				return INPUT;
			}
			else if(!isIdAlpha) {
				addActionError(MessagesConfig.MSE002);
				return INPUT;
			}
			else if (IdCount.length < 1) {
				addActionError(MessagesConfig.MSE004);
				return INPUT;
			} else if (PassCount.length < 1) {
				addActionError(MessagesConfig.MSE007);
				return INPUT;
			} else {
				String sql3 = "SELECT * FROM userinfo.user WHERE ID="+id+" AND PASS=?";
				//checking if the password belong to the specified user.
				String[][] BothCount = db.selectExec(sql3,password);
				if (BothCount.length < 1) {
					addActionError("ユーザーIDとパスワードが一致しません。");
					return INPUT;
				}
				
				//for search page
				boolean errchq =true;
				//user session start
				userSession.put("ID", BothCount[0][0]);
				
				userSession.put("Errorchq", errchq);
				return SUCCESS; // all well
			}

		}
		
	}

	@Override
	public void validate() { //
		if (password == null || id == null) { // First time page loads, student is null.
			setFieldErrors(null); // We clear all the validation errors that strut stupidly found since there was
									// not form submission.
		}
	}
	
	public boolean isAlphaNumeric(String s){
	    String pattern= "^[a-zA-Z0-9]*$";
	    return s.matches(pattern);
	}

}
