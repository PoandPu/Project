<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<html>
<title><fmt:message key="login_jsp.form.authorization" /></title>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
	<table id="main-container">
		<%@ include file="/WEB-INF/jspf/header.jspf"%>
		<tr>
			<td class="content">
				<div align="center">
					<div class="container" style = "width: 60%">
					
					<c:choose>
					<c:when test="${not empty message}"><p style = "font-family: Helvetica, Arial, sans-serif; font-size:  1.3em"> ${message} </p></c:when>
					
					<c:when test= "${empty message}"> 
						<form action="controller" method="post">
							<input type="hidden" name="command" value="passwordRecovery" required>
							<div align="center">
								<h3>
									<label style = "font-family: Helvetica, Arial, sans-serif; font-size:  1.3em">Please enter email or login</label>
								</h3>
							</div>
							<div class="row" style = "padding: 4px 15%">
								
									<input type="text" name="pattern"
										placeholder="<fmt:message key="login_jsp.form.login" />, email"
										required>
								
							</div>
							<div class="row" style = "content-align: center; padding: 4px 15%">
								<input class="form" type="submit" name="submit"
									value=<fmt:message key="login_jsp.form.login"/>>
							</div>

						</form>	
						<c:if test="${not empty loginErrorMessage}">
							<label class="validation"><fmt:message
									key="${loginErrorMessage }" /></label>
							<c:remove var="loginErrorMessage" />
						</c:if>
						</c:when>
						</c:choose>
					</div>
				</div>
			</td>
		</tr>
	</table>

</body>
</html>