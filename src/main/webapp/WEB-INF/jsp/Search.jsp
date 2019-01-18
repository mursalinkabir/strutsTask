<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.Arrays"%>
<%@ page import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Search Page</title>

<style type="text/css">
.welcome li {
	list-style: none;
}
</style>
<!-- <link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css"> -->
</head>
<body>

	<h1>検索画面</h1>


	<div id="SearchFormDiv">
		<s:if test="hasActionMessages()">
			<div class="welcome">
				<s:actionmessage />
			</div>
		</s:if>
		<s:if test="hasActionErrors()">
			<div class="errors">
				<s:actionerror />
			</div>
		</s:if>
		<s:form action="Search" method="GET" namespace="/">
			<table>
				<tr>
					<s:textfield label="ID" name="ID"  />
				</tr>
				<tr>
					<s:textfield label="名前" name="uname" />
				</tr>
				<tr>
					<s:textfield label="カナ" name="kana" />
				</tr>





			</table>
			<span class="w3-container" style="display: inline;"> <input
				type="submit" value="検索" id="Search_0" style="float: left;">


			</span>

		</s:form>
		<!-- 			New Input Button -->
		<a href="<s:url action="Register"/>">
			<button class="button -blue -bordered" style="margin-left: 5px;">
				<span class="button--inner">新規</span>
			</button>
		</a>
		<!--Logout Button -->
		<a href="<s:url action="login"/>">
			<button class="button -blue -bordered" style="margin-left: 5px;">
				<span class="button--inner">ログアウト</span>
			</button>
		</a>
	</div>


	<!-- Checking if error exists -->
	<%  
	boolean errchq= true;
	errchq = (boolean) session.getAttribute("Errorchq");
	if(!errchq){
	%>
		<!-- Showing search results -->
	<div id="SearchResultDiv">
		<table id="conTable">
			<tr>


				<% 
					// retrieve your list from the request, with casting 
					String flag = (String) session.getAttribute("arrayRow");
					if (flag != null) {
						// Form was submitted.

						String row = (String) session.getAttribute("arrayRow");
						int rowSize = Integer.parseInt(row.trim());
						String col = (String) session.getAttribute("arrayCol");
						int colSize = Integer.parseInt(col.trim());
						String[][] rowsAll = new String[rowSize][colSize];
						rowsAll = (String[][]) session.getAttribute("datalist");

						out.println("<tr>");
						out.println("<th>ID</th><th>名前</th><th>カナ</th><th>生年月日</th><th>委員会</th><th colspan=\"2\">操作</th>");
						out.println("</tr>");

						for (int i = 0; i < rowsAll.length; i++) {
							if (rowsAll[i][0] != null) {
								out.println("<tr>");
								for (int j = 0; j < colSize; j++)

									out.println("<td>" + rowsAll[i][j] + "</td>");

							}
							out.println("<td>");
				%>
				<!--Update Button -->
				<a
					href="<s:url action="Update"><s:param name="uid"><%out.println(rowsAll[i][0]);%></s:param></s:url>">
					<button class="button -blue -bordered" style="margin-left: 5px;">
						<span class="button--inner"> 更新</span>
					</button>
				</a>
				<%
					out.println("</td>");
							out.println("<td>");
				%>
				<!--Delete Button -->
				<a
					href="<s:url action="Delete"><s:param name="uid"><%out.println(rowsAll[i][0]);%></s:param></s:url>">
					<button class="button -blue -bordered" style="margin-left: 5px;">
						<span class="button--inner"> 削除</span>
					</button>
				</a>
				<%
					out.println("</td>");
							out.println("</tr>");
						}

					}
					String msg = (String) session.getAttribute("msg");
					if (msg != null) {
						out.println("<tr><h2>" + msg + "</h2></tr>");
					}
				%>
			
		</table>

	</div>

<% } %>


</body>
</html>