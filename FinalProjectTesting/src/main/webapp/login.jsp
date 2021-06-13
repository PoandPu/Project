<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<script src='https://www.google.com/recaptcha/api.js?hl=${language}'></script>
<script>
if (${not empty sessionScope.user}) {
	  window.location.href = 'controller?command=viewAllSubjects';
	}</script>
<html>
<c:set var = "title" value = "login_jsp.form.authorization"></c:set>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
	<table id="main-container">
		<%@ include file="/WEB-INF/jspf/header.jspf"%>
		<tr>
			<td class="content">
				<div align="center">
					<div class="container">
						<form action="controller" method="post">
							<input type="hidden" name="command" value="login" required>
							<div align="center">
								<h3>
									<label><fmt:message key="login_jsp.form.authorization" /></label>
								</h3>
							</div>
							<div class="row">
								<div class="col-25">
									<label><fmt:message key="login_jsp.form.login" /></label>
								</div>
								<div class="col-75">
									<input type="text" name="login"
										placeholder=<fmt:message key="login_jsp.form.login" />
										required>
								</div>
							</div>
							<div class="row">
								<div class="col-25">
									<label><fmt:message key="login_jsp.form.password" /></label>
								</div>
								<div class="col-75">
									<input type="password" name="password"
										placeholder=<fmt:message key="login_jsp.form.password" />
										required>
								</div>
							</div>
							<br>
							<div class="g-recaptcha"
								data-sitekey="6LfjBPgaAAAAAGIBKp2EaiqRU6aaH_kpGAhMBwIb"></div>
							<br>
							<div class="row" style = "content-align: center">
								<input class="form" type="submit" name="submit"
									value=<fmt:message key="login_jsp.form.login" />>
							</div>

						</form>
						<div class="row">
						<div class = "col-75">
							<label><fmt:message key="login_jsp.form.not_registred" /></label>
							<a href="controller?command=registration"><fmt:message
									key="login_jsp.form.registration" /></a>
						</div> 
						<div class = "col-25">
						<a href="controller?command=passwordRecovery"><fmt:message key="login_jsp.form.forgot_password"/></a>
						</div> 
						</div>
						<c:if test="${not empty loginErrorMessage}">
							<label class="validation"><fmt:message
									key="${loginErrorMessage }" /></label>
							<c:remove var="loginErrorMessage" />
						</c:if>
					</div>
				</div>
			</td>
		</tr>
		<%@ include file="/WEB-INF/jspf/footer.jspf"%>
	</table>
</body>
</html>