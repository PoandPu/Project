<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>
<html>
<title><fmt:message key="registration_jsp.form.registration" /></title>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
	<table id="main-container">
		<%@ include file="/WEB-INF/jspf/header.jspf"%>
		<tr>
			<td class="content">
				<div align="center">
					<div class="container">
						<form id="login_form" action="controller" method="post">
							<input type="hidden" name="command" value="registration" />
							<div align="center">
								<h3><label><fmt:message
										key="registration_jsp.form.registration" /></label></h3>
							</div>
							<div class="row">
								<div class="col-25">
									<label><fmt:message key="registration_jsp.form.login" /></label>
								</div>
								<div class="col-75">
									<input type="text" name="login"
										placeholder="<fmt:message key="registration_jsp.form.login" />"
										required>
								</div>
							</div>
							<div class="row">
								<div class="col-25">
									<label><fmt:message
											key="registration_jsp.form.password" /></label>
								</div>
								<div class="col-75">
									<input type="password" name="password"
										placeholder="<fmt:message key="registration_jsp.form.password" />"
										required>
								</div>
							</div>

							<div class="row">
								<div class="col-25">
									<label><fmt:message
											key="registration_jsp.form.confirm_password" /></label>
								</div>
								<div class="col-75">
									<input type="password" name="confirm_password"
										placeholder="<fmt:message key="registration_jsp.form.confirm_password" />"
										required>
								</div>
							</div>


							<div class="row">
								<div class="col-25">
									<label><fmt:message key="registration_jsp.form.email" /></label>
								</div>
								<div class="col-75">
									<input type="email" name="email"
										placeholder="<fmt:message key="registration_jsp.form.email" />"
										required>
								</div>
							</div>
							<div class="row">
								<div class="col-25">
									<label><fmt:message
											key="registration_jsp.form.first_name" /></label>
								</div>
								<div class="col-75">
									<input type="text" name="first_name"
										placeholder="<fmt:message key="registration_jsp.form.first_name" />"
										required>
								</div>
							</div>
							<div class="row">
								<div class="col-25">
									<label><fmt:message
											key="registration_jsp.form.last_name" /></label>
								</div>
								<div class="col-75">
									<input type="text" name="last_name"
										placeholder="<fmt:message key="registration_jsp.form.last_name" />"
										required>
								</div>
							</div>
							<div class="row">
								<fmt:message key="registration_jsp.form.russian" />
								<input type="radio" name="language" id="ru" checked="checked"
									value="ru">
								<fmt:message key="registration_jsp.form.english" />
								<input type="radio" name="language" id="en" value="en">
							</div>
							<div class="row">
								<input class="form" type="submit" name="submit"
									value=<fmt:message key="registration_jsp.form.submit" />>
							</div>
							<div class="row">
								<label><fmt:message
										key="registration_jsp.form.have_an_account" /> <a
									href="login.jsp"><fmt:message
											key="registration_jsp.form.click_here" /></a></label>
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