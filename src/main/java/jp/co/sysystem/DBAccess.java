/**
 * studyB package.
 */
package jp.co.sysystem;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * B_14.java class This class will handle db ransaction
 * 
 * @author SYSYSTEM/work
 * @update Oct 24, 2018
 */
public class DBAccess {

	public static Connection conn = null;

	/**
	 * This is the connect method. This method will create a connection instance
	 * 
	 * @return void
	 * @exception IOException on Input.
	 */
	public void connect() {

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException ex) {
			System.err.println("Unable to load MySQL Driver");
			
		}

		String jdbcUrl = "jdbc:mysql://localhost:3306/userinfo?useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
		try {
			// DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			Connection con = DriverManager.getConnection(jdbcUrl, "root", "smmk143143");
			conn = con;
			// Set auto commit as false.
			conn.setAutoCommit(false);
			System.out.println("接続済み/Connected");

		} catch (SQLException e) {
			System.out.println("接続エラーが発生しました/Connection Error Occured");
			System.out.println(e.getMessage());

		}
	}

	public Connection Getconnect() {

		try {

			Class.forName("com.mysql.cj.jdbc.Driver");
		} catch (ClassNotFoundException ex) {
			System.err.println("Unable to load MySQL Driver");
		}

		String jdbcUrl = "jdbc:mysql://localhost:3306/userinfo?useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false";
		try {
			// DriverManager.registerDriver(new com.mysql.jdbc.Driver());
			Connection con = DriverManager.getConnection(jdbcUrl, "root", "smmk143143");
			conn = con;
			// Set auto commit as false.
			conn.setAutoCommit(false);
			System.out.println("接続済み/Connected");
			return conn;

		} catch (SQLException e) {
			System.out.println("接続エラーが発生しました/Connection Error Occured");
			System.out.println(e.getMessage());

		}
		return conn;
	}

	/**
	 * This is the disconnect method. This method will disconnect from database.
	 * 
	 * @return void
	 * @exception SQLServerException on Input.
	 */
	public void disconnect() {
		try {
			conn.close();
			System.out.println("切断された/Disconnected");
		} catch (SQLException e) {
			System.out.println("接続エラーが発生しました/Connection Close Error Occured");
			e.printStackTrace();
		}
	}

	/**
	 * This is the commit method. This method will commit changes in database.
	 * 
	 * @return void
	 * @exception SQLServerException .
	 */
	public void commit() {
		try {
			conn.commit();
			System.out.println("完了したコミット/Commit Done");

		} catch (SQLException e) {
			System.out.println("接続エラーが発生しました/Connection Commit Error Occured");
			e.printStackTrace();
		}
	}

	/**
	 * This is the rollback method. This method will rollback to previous state.
	 * 
	 * @return void
	 * @exception SQLServerException on Input.
	 */
	public void rollback() {
		try {
			if (conn != null) {
				conn.rollback();
			}

		} catch (SQLException e) {
			System.out.println("接続エラーが発生しました/Connection Rollback Error Occured");
			e.printStackTrace();
		}
	}

	/**
	 * This is the undateExec method. This method executes insert/update/delete
	 * 
	 * @param sql
	 * @return returns row count as int.
	 * @exception SQLServerException .
	 */
	public int undateExec(String sql) {
		int rows = 0;
		try {
			java.sql.Statement st = conn.createStatement();
			rows = st.executeUpdate(sql);
			if (st != null) {
				st.close();

			}

		} catch (SQLException e) {
			System.out.println("ステートメント実行エラーが発生しました/Statement execution Error Occured");
			e.printStackTrace();
		}

		return rows;

	}

	public boolean RegisterUser(User user) {
		int rows2 = 0;
		boolean flag1 = false;
		boolean flag2 = false;
		PreparedStatement prpstmt = null;
		PreparedStatement prpstmt2 = null;
		String insertUserTable = "INSERT INTO userinfo.user" + "(ID, PASS, NAME, KANA) VALUES" + "(?,?,?,?)";
		String insertUserDetailTable = "INSERT INTO userinfo.userdetail" + "(ID, BIRTH, CLUB) VALUES" + "(?,?,?)";
		try {
			prpstmt = conn.prepareStatement(insertUserTable);
			prpstmt.setString(1, user.getId());
			prpstmt.setString(2, user.getPword());
			prpstmt.setString(3, user.getUname());
			prpstmt.setString(4, user.getKana());
//			rows = prpstmt.executeUpdate();
			if (prpstmt.executeUpdate() > 0) {
				flag1 = true;

			}
			if (flag1) {

				prpstmt2 = conn.prepareStatement(insertUserDetailTable);
				prpstmt2.setString(1, user.getId());
//				SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
//				Date parsed;
//				java.sql.Date dbdate;
				try {
					SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy/MM/dd");
					java.util.Date date = formatter1.parse(user.getBirth());
					java.sql.Date sqlStartDate = new java.sql.Date(date.getTime());
					prpstmt2.setDate(2, sqlStartDate);
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				// Date dbdate= user.getBirth()

				prpstmt2.setString(3, user.getClub());

				rows2 = prpstmt2.executeUpdate();

				if (rows2 > 0) {
					flag2 = true;

				}
			}

		} catch (SQLException e) {
			System.out.println("ステートメント実行エラーが発生しました/Statement execution Error Occured");
			e.printStackTrace();
		}
		if (flag1 && flag2) {
			this.commit();
			return true;
		}
		return false;
	}

	/**
	 * 
	 * This is the isExist method. This method will check if id exists in db.
	 * 
	 * @param ID
	 * @return boolean
	 * @exception SQLException on Input.
	 */
	public boolean isExist(String ID) {
		boolean verdict = false;
		PreparedStatement prpstmt = null;
		ResultSet rset;
		try {
			String sql = "SELECT * FROM userinfo.user WHERE ID=?";

			if (ID != "" && ID != null) {

				prpstmt = conn.prepareStatement(sql);
				prpstmt.setString(1, ID);
				rset = prpstmt.executeQuery();
				if (rset.first()) {
					verdict = true;
					return verdict;
				}
			}
		} catch (SQLException e) {
			System.out.println("ステートメント実行エラーが発生しました/Statement execution Error Occured");
			e.printStackTrace();
		}

		return verdict;

	}

	/**
	 * 
	 * This is the selectUser method. This method will find given field data
	 * 
	 * @param sql
	 * @param fieldname
	 * @return User
	 * @exception SQLException on Input.
	 */
	public User selectUser(String sql, String fieldname) {
		User UserData = new User();
		try {
			PreparedStatement prpstmt = null;
			ResultSet rset;
//			User UserData;
			prpstmt = conn.prepareStatement(sql);
			if (fieldname != "") {
				prpstmt.setString(1, fieldname);
				rset = prpstmt.executeQuery();

				if (rset.first()) {

					// there's stuff to do
					rset.beforeFirst();
					// counting row
					if (rset.last()) {
						rset.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the
											// first
											// element
					}
					while (rset.next()) {

						UserData.setId(rset.getString(1));
						UserData.setPword(rset.getString(2));
						UserData.setUname(rset.getString(3));
						UserData.setKana(rset.getString(4));
						UserData.setBirth(rset.getString(5));
						UserData.setClub(rset.getString(6));
					}

					rset.close();
					prpstmt.close();

					return UserData;
				} else {
					// rs was empty
//				String[][] rows = new String[0][0];
					return UserData;
				}
			}

		} catch (Exception e) {
			System.out.println("選択処理時に例外が発生しました。/Exception occured when processing select." + e);
		}
		// User UserDataNull;
		return UserData;

	}

	public boolean UpdateUser(User user) {
		boolean verdict = false;
		PreparedStatement prpstmt = null;
		PreparedStatement prpstmt2 = null;
		try {

			// create the java mysql update preparedstatement
			String query = "UPDATE userinfo.user SET ID = ?,  NAME = ?, KANA = ? WHERE ID = ?;";
			prpstmt = conn.prepareStatement(query);
			prpstmt.setString(1, user.getId());
			prpstmt.setString(2, user.getUname());
			prpstmt.setString(3, user.getKana());
			prpstmt.setString(4, user.getId());
			// execute the java preparedstatement
			// rows= prpstmt.executeUpdate();
			if (prpstmt.executeUpdate() > 0) {
				String query2 = "UPDATE userinfo.userdetail SET ID = ?, BIRTH = ?, CLUB = ? WHERE ID = ?;";
				prpstmt2 = conn.prepareStatement(query2);
				prpstmt2.setString(1, user.getId());
				prpstmt2.setString(2, user.getBirth());
				prpstmt2.setString(3, user.getClub());
				prpstmt2.setString(4, user.getId());
				if (prpstmt2.executeUpdate() > 0) {

					this.commit();
					verdict = true;
				}

			}
			conn.close();
		} catch (Exception e) {
			System.err.println("Got an exception! ");
			System.err.println(e.getMessage());
		}

		return verdict;
	}

	/**
	 * This is the selectExec method. This method will execute select and return
	 * value.
	 * 
	 * @param sql
	 * @return
	 * @return String[][]
	 * @exception SQLServerException .
	 */
	public String[][] selectExec(String sql, String searchTerm) {
		try {
			PreparedStatement prpstmt = null;
			ResultSet rset;
			prpstmt = conn.prepareStatement(sql);
			if (searchTerm != "") {
				prpstmt.setString(1, searchTerm);
				rset = prpstmt.executeQuery();
			} else {
				// is search term is null
				java.sql.Statement st = conn.createStatement();
				rset = st.executeQuery(sql);

			}

			if (rset.first()) {
				// there's stuff to do
				ResultSetMetaData rsmd = rset.getMetaData();
				int rowcount = 0;
				// getting column count
				int colcount = rsmd.getColumnCount();
				// counting row
				if (rset.last()) {
					rowcount = rset.getRow();

					rset.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first
										// element
				}
				String[][] rows = new String[rowcount][colcount];
				int i = 0;
				while (rset.next()) {
					for (int j = 0; j < colcount; j++) {
						rows[i][j] = rset.getString(j + 1);
					}

					i++;
				}

				rset.close();
				prpstmt.close();

				return rows;
			} else {
				// rs was empty
				String[][] rows = new String[0][0];
				return rows;
			}

		} catch (Exception e) {
			System.out.println("選択処理時に例外が発生しました。/Exception occured when processing select." + e);
		}
		String[][] rows = new String[0][0];
		return rows;

	}

	public String[][] selectExec(String sql, String ID, String uname, String kana) {

		try {
			PreparedStatement prpstmt = null;
			prpstmt = conn.prepareStatement(sql);
			if (ID != "" || uname != "" || kana != "") {
				//fixing search problem and making it search for input item only.
				ID = (ID.length() == 0) ? " ":ID;
				uname = (uname.length() == 0) ? " ":uname;
				kana = (kana.length() == 0) ? " ":kana;
				
				prpstmt.setString(1, "%"+ID+"%");
				prpstmt.setString(2, ""+uname+"%");
				prpstmt.setString(3, ""+kana+"%");
			} else {
				System.out.println(prpstmt);
				// is search term is null
				prpstmt.close();
				String[][] rows = new String[0][0];
				return rows;
			}
			System.out.println(prpstmt);
			ResultSet rset = prpstmt.executeQuery();
			if (rset.first()) {
				// result not empty
				ResultSetMetaData rsmd = rset.getMetaData();
				int rowcount = 0;
				// getting column count
				int colcount = rsmd.getColumnCount();
				// counting row
				if (rset.last()) {
					rowcount = rset.getRow();

					rset.beforeFirst(); // not rs.first() because the rs.next() below will move on, missing the first
										// element
				}
				String[][] rows = new String[rowcount][colcount];
				int i = 0;
				while (rset.next()) {
					for (int j = 0; j < colcount; j++) {
						rows[i][j] = rset.getString(j + 1);
					}

					i++;
				}

				rset.close();
				prpstmt.close();

				return rows;

			} else {
				// rs was empty
				// nothing found return empty array
				String[][] rows = new String[0][0];
				return rows;
			}

		} catch (Exception e) {
			System.out.println("選択処理時に例外が発生しました。/Exception occured when processing select." + e);
		}
		// nothing found return empty array
		String[][] rows = new String[0][0];
		return rows;
	}

	public String[][] selectExec(String sql) {
		return selectExec(sql, "");
	}

	/**
	 * This is the DeleteUser method. This method will delete user based on input.
	 * 
	 * @param userBean
	 * @return
	 * @return boolean
	 * @exception IOException on Input.
	 */
	public boolean DeleteUser(User userBean) {
		String sqlUser = "DELETE FROM userinfo.user WHERE ID = " + userBean.getId();
		String sqlUserDetail = "DELETE FROM userinfo.userdetail WHERE ID = " + userBean.getId();
		boolean flag = false;
		if (undateExec(sqlUser) > 0) {
			if (undateExec(sqlUserDetail) > 0) {
				this.commit();
				flag = true;
			}
		}
		return flag;
	}

}
