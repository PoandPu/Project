<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>

<html>
<title><fmt:message key="admin.user_settings_jsp.title"/></title>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
	<table id="main-container">
		<%@ include file="/WEB-INF/jspf/header.jspf"%>
		<tr>
			<td class="content">
				<div align="center">
					<div class="container">
						<form action="controller" method="post">
							<input type="hidden" name="command" value="userSettings" />
							<div align="center">
								<label><fmt:message key="admin.user_settings_jsp.title" /></label>
							</div>
							<div class="row">
								<div class="col-25">
									<label><fmt:message
											key="admin.user_settings_jsp.first_name" /></label>
								</div>
								<div class="col-75">
									<input type="text" name="firstName"
										value="${sessionScope.user.firstName}" required>
								</div>
							</div>

							<div class="row">
								<div class="col-25">
									<label><fmt:message
											key="admin.user_settings_jsp.last_name" /></label>
								</div>
								<div class="col-75">
									<input type="text" name="lastName"
										value="${sessionScope.user.lastName}" required>
								</div>
							</div>

							<div class="row">
								<div class="col-25">
									<label><fmt:message key="admin.user_settings_jsp.email" /></label>
								</div>
								<div class="col-75">
									<input type="text" name="email"
										value="${sessionScope.user.email}" required>
								</div>
							</div>

							<div class="row">
								<div class="col-25">
									<label><fmt:message
											key="admin.user_settings_jsp.password" /></label>
								</div>
								<div class="col-75">
									<input type="password" name="new_password" autocomplete="off">
								</div>
							</div>

							<div class="row">
								<div class="col-25">
									<label><fmt:message
											key="admin.user_settings_jsp.confirm_password" /></label>
								</div>
								<div class="col-75">
									<input type="password" name="confirm_new_password"
										autocomplete="off">
								</div>
							</div>


							<div class="row">
								<input class="form" type="submit" name="submit"
									value=<fmt:message key="save" />>
							</div>

						</form>
						<c:if test="${not empty userSettingsErrorMessage}">
							<label class="validation"><fmt:message
									key="${userSettingsErrorMessage }" /></label>
							<c:remove var="userSettingsErrorMessage" />
						</c:if>
					</div>
				</div>
			</td>
		</tr>
		<%@ include file="/WEB-INF/jspf/footer.jspf"%>
	</table>
</body>
</html>