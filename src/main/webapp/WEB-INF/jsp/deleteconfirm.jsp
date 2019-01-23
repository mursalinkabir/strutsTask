<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Delete Confirm</title>
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
	<h1>削除</h1>

	<div id="conDiv">
		<table id="conTable">
			<tr>
				<th>Column</th>
				<th>Data</th>
			</tr>
			<tr>
				<td>ユーザーID</td>
				<td><s:property value="userBean.getId()" /></td>
			</tr>
			<tr>
				<td>名前</td>
				<td><s:property value="userBean.getUname()" /></td>
			</tr>
			<tr>
				<td>カナ</td>
				<td><s:property value="userBean.getKana()" /></td>
			</tr>
			<tr>
				<td>生年月日</td>
				<td><s:property value="userBean.getBirth()" /></td>
			</tr>
			<tr>
				<td>委員会</td>
				<td><s:property value="userBean.getClub()" /></td>
			</tr>


		</table>

		<a id="confirm">
			<button class="button -blue -bordered" style="margin-left: 5px;">
				<span class="button--inner">削除</span>
			</button>
		</a>
		<button type="button" name="back" onclick="history.back()">戻る</button>
	</div>




	<script type="text/javascript">
		$("#confirm")
				.click(
						function() {
							if (confirm("登録しますか？")) {
								$("#confirm")
										.attr("href",
												"/struts2-archetype-starter/DeleteConfirm.action");
							} else {
								return false;
							}
						});
	</script>
</body>
</html>