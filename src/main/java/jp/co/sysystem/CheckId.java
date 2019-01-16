/**
 * jp.co.sysystem package.
 */
package jp.co.sysystem;

import com.opensymphony.xwork2.ActionSupport;

/**
 * CheckId.java class This class will......
 * 
 * @author SYSYSTEM/work
 * @update Nov 30, 2018
 */
public class CheckId extends ActionSupport {

	private static final long serialVersionUID = 1L;
	private String id;
	private String msg;

	DBAccess db = new DBAccess();

	@Override
	public String execute() throws Exception {
		System.out.println("ID in Checkid class is " + id);
		boolean exists = false;
		try {
			db.connect();
			exists = db.isExist(id);
			if (exists) {
				setMsg("ID Number =" + id + " Exists in the Database.");
			} else {
				setMsg("ID Number =" + id + " is free to use.");
			}

			System.out.println(msg);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "SUCCESS";

	}

	/**
	 * @return the id
	 */
	public final String getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public final void setId(String id) {
		this.id = id;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
