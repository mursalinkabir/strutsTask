<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<title>Update</title>
<script type="text/javascript">
	function sendMyValue() {

		//var data1 = JSON.stringify("id="+id);
		$.ajax({
			url : "checkid.action",
			data : "id=" + id,
			dataType : 'json',
			//			contentType : 'application/json',
			type : 'GET',
			async : true,
			success : function(data) {
				var html = "<br>" + data.msg;
				$("#info").html(html);
			},
			error : function(data) {
				alert("Some error occured.");
			}
		});
	}

	function ajaxStruts() {
		var id = $("#Register_userBean_id").val();
		console.log("form id =" + id);
		$.ajax({
			type : "GET",
			contentType : "application/x-www-form-urlencoded",
			url : "checkid.action",
			dataType : "json",
			data : "id=" + id,
			success : function(data) {
				// 	            alert(data.msg);
				var html = "<br>" + data.msg;
				$("#info").html(html);
			}
		});
	}
</script>
</head>
<body>


	<h1>変更入力/Update</h1>
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



	<div id="UpdateFormDiv">

		<s:form action="Update" method="GET" namespace="/">
			<table>
				<tr>

					<s:textfield label="ID" name="userBean.id" readonly="true" />


				</tr>
	
<!-- 				<tr> -->
<%-- 					<s:textfield label="パスワード" name="userBean.pword" /> --%>
<!-- 				</tr> -->
				<!-- 				<tr> -->
				<%-- 					<s:textfield label="パスワード再入力" name="userBean.conpword" /> --%>
				<!-- 				</tr> -->
				<tr>
					<s:textfield label="名前" name="userBean.uname" />
				</tr>
				<tr>
					<s:textfield label="カナ" name="userBean.kana" />
				</tr>
				<tr>
					<s:textfield label="生年月日(yyyy/MM/dd)" name="userBean.birth" />
				</tr>
				<tr>
					<s:textfield label="委員会" name="userBean.club" />
				</tr>


			</table>
			<span class="w3-container" style="display: inline;"> <s:submit
					value="確認" style="float: left;" action="UpdateSubmit" />

			</span>

		</s:form>
		

		<button type="button" name="back" onclick="history.back()">戻る</button>
	

	</div>


</body>
</html>