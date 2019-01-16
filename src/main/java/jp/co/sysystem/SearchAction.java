/**
 * 
 */
package jp.co.sysystem;

import java.io.IOException;

import org.apache.struts2.interceptor.SessionAware;
import org.apache.struts2.interceptor.validation.SkipValidation;

import com.opensymphony.xwork2.ActionSupport;
import com.opensymphony.xwork2.validator.annotations.RequiredStringValidator;

/**
 * SearchAction.java class This class will perform search
 * 
 * @author SYSYSTEM/Mursalin
 * @update Nov 27, 2018
 */
public class SearchAction extends ActionSupport implements SessionAware {
	private static final long serialVersionUID = 1L;
	DBAccess db = new DBAccess();
	private String id;
	private String uname;
	private String kana;

	// For SessionAware
	private java.util.Map<String, Object> userSession;
	@Override
	public void setSession( java.util.Map<String, Object> session ) {
		userSession = session;
	}

	/**
	 * @return the id
	 */
	@RequiredStringValidator(message = MessagesConfig.MSE015)
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
	 * @return the uname
	 */
	@RequiredStringValidator(message = MessagesConfig.MSE015)
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
	@RequiredStringValidator(message = MessagesConfig.MSE015)
	public final String getKana() {
		return kana;
	}

	/**
	 * 
	 * This is the setKana method. This method set the value of kana
	 * 
	 * @param kana
	 * @return void
	 * @exception IOException on Input.
	 */
	public final void setKana(String kana) {
		this.kana = kana;
	}

	@SkipValidation
	public String execute() throws Exception {

		return SUCCESS; // all well
	}

	/**
	 * 
	 * This is the SearchMethod method. This method will authenticate the search
	 * 
	 * @return String
	 * @exception RuntimeException,IOException on Input.
	 */
	public String SearchMethod() {

		if (id == null || kana == null || uname == null) {
			return INPUT;// First time page loads. We show page associated to INPUT resulttes.

		} else {

			db.connect();
			boolean isIdAlpha = isAlphaNumeric(id);
			boolean isNameFull = isHalfWidth(uname);
			boolean isKanaHalf = isHalfWidth(kana);
			if (!isIdAlpha) {
				addActionError(MessagesConfig.MSE002);
				return INPUT;
			} else if (isNameFull) {
				addActionError(MessagesConfig.MSE010);
				return INPUT;
			} else if (!isKanaHalf) {
				addActionError(MessagesConfig.MSE013);
				return INPUT;
			} else {

				String SearchSql = "SELECT user.ID,NAME,KANA,BIRTH,CLUB FROM userinfo.user  INNER JOIN userinfo.userdetail ON user.ID= userdetail.ID WHERE user.ID=? AND NAME=? AND KANA=?";
				String[][] SearchRes = db.selectExec(SearchSql, id, uname, kana);
				// passing multidimensional array to jsp
				if (SearchRes.length > 0) {
					String retrievearrRow = Integer.toString(SearchRes.length);
					userSession.put("arrayRow", retrievearrRow);
					String retrievearrColumn = Integer.toString(SearchRes[0].length);
					userSession.put("arrayCol", retrievearrColumn);
					// sending alist to output
					userSession.put("datalist", SearchRes);
					
				} else {
					System.out.println("結果が見つかりません。/No rows fetched");
					userSession.put("msg", MessagesConfig.MSE022);

					//response.sendRedirect("B30_SELECT.jsp");
				}
				return SUCCESS; // all well
			}

		}
	}

	@Override
	public void validate() { //
		if (id == null || kana == null || uname == null) { // First time page loads, student is null.
			setFieldErrors(null); // We clear all the validation errors that strut stupidly found since there was
									// not form submission.
		}
	}

	public boolean isAlphaNumeric(String s) {
		String pattern = "^[a-zA-Z0-9]*$";
		return s.matches(pattern);
	}

	public boolean isFullWidth(String s) {
		String pattern = "^[Ａ-ｚ]*$";
		return s.matches(pattern);
	}

	public boolean isHalfWidth(String s) {
		String pattern = "^[ｧ-ﾝﾞﾟ]*$";
		return s.matches(pattern);
	}

}
