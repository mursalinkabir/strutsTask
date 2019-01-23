<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">

<title>Register</title>
<script>
/* 	function sendMyValue() {
		
		 //var data1 = JSON.stringify("id="+id);
		$.ajax({
			url : "checkid.action",
			data : "id="+id,
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
	} */
	
	function ajaxStruts() {
		var id = $("#Register_userBean_id").val();
		if(id.length >0){
			 console.log("form id ="+id);
			    $.ajax({
			          type: "GET",
			          contentType: "application/x-www-form-urlencoded",
			          url: "checkid.action",
			          dataType: "json",
			          data: "id="+id,
			          success: function(data){
//		 	            alert(data.msg);
			        	  var html = "<br>" + data.msg;
							$("#info").html(html);
			          }
			    });
		}else{
			$("#info").html("ユーザーIDを入力してください。");
		}
	
	}
</script>
</head>
<body>


	<h1>新規登録</h1>
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



	<div id="RegisterFormDiv">

		<s:form action="Register" method="GET" namespace="/">
			<table>
				<tr>

					<s:textfield label="ID" name="userBean.id" />
					<!--Checker  Button -->
					<%-- 				<a href="<s:url action="login"><s:param name="id"></s:param></s:url>"> --%>
					<!-- 					<button class="button -blue -bordered" style="margin-left: 5px;"> -->
					<%-- 						<span class="button--inner"> 削除</span> --%>
					<!-- 					</button> -->
					<!-- 				</a> -->

				</tr>
				<tr>
					<td><div id="info" style="color: green; font-size: 18px;"></div></td>
					<td><input type="button" value="使用できるか確認" onclick="ajaxStruts()"
						style="background-color: #008CBA;"></td>
				</tr>
				<tr>
					<s:textfield label="パスワード" name="userBean.pword" />
				</tr>
				<tr>
					<s:textfield label="パスワード再入力" name="userBean.conpword" />
				</tr>
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
					value="登録します" style="float: left;" />

			</span>

		</s:form>
		<a
					href="<s:url action="Search">
					<s:param name="ID">
					<s:property value = "userBean.id"/></s:param>
					<s:param name="uname"></s:param>
					<s:param name="kana"></s:param>
					</s:url>">
					<button class="button -blue -bordered" style="margin-left: 5px;">
						<span class="button--inner"> 戻る</span>
					</button>
				</a>
		<!-- <button type="button" name="back" onclick="history.back()">戻る</button> -->
	
	</div>


</body>
</html>