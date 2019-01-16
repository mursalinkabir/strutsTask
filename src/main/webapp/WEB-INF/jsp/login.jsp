<%@taglib prefix="s" uri="/struts-tags"%>
<%@ page contentType="text/html; charset=UTF-8"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<style type="text/css">
</style>
<head>

<title>Login</title>
</head>
<body>

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

	<div id="loginFormDiv">
		<s:form action="login" method="post" namespace="/">
			<s:textfield label="ユーザーID" name="id" />
			<s:password label="パスワード" name="password" />
			<s:submit label="ログイン" />
			<s:token />
		</s:form>


	</div>


</body>
</html>