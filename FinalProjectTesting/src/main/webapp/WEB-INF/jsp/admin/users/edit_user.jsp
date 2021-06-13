<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>

<html>
<c:set var = "title" value ="admin.edit_user_jsp.edit_user"></c:set>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
	<table id="main-container">
		<%@ include file="/WEB-INF/jspf/header.jspf"%>
		<tr>
			<td class="content">

				<div align="center">
					<div class="container">
						<form action="controller" method="post">
							<input type="hidden" name="command" value="editUser" /> <input
								type="hidden" name="userId" value="${user.id}" />
							<div align="center">
								<label><fmt:message key="admin.edit_user_jsp.edit_user"/></label>
							</div>
							<div class="row">
								<div class="col-25">
									<label><fmt:message key="profile_jsp.first_name"/></label>
								</div>
								<div class="col-75">
									<input type="text" name="firstName"
										value="${user.firstName}">
								</div>
							</div>

							<div class="row">
								<div class="col-25">
									<label><fmt:message key="profile_jsp.last_name"/></label>
								</div>
								<div class="col-75">
									<input type="text" name="lastName" value="${user.lastName}">
								</div>
							</div>
							
							<div class="row">
								<div class="col-25">
									<label><fmt:message key="profile_jsp.email"/></label>
								</div>
								<div class="col-75">
									<input type="text" name="email" value="${user.email}">
								</div>
							</div>



							<div class="row">
								<div class="col-25">
									<label><fmt:message key="admin.edit_user_jsp.status"/></label>
								</div>
								<div class="col-75">
								
									<input type="radio" id="contactChoice1" name="radioButton"
										value="block" ${user.isBlocked eq 'true' ? 'checked' : ''}>
										<label for="contactChoice1">Blocked</label>
										<input type="radio" id="contactChoice2" name="radioButton"
										value="unblock" ${user.isBlocked eq 'false' ? 'checked' : ''}>
										<label for="contactChoice2">Unblocked</label>
										
								</div>
							</div>
							<div class="row">
								<input class="form" type="submit" name="submit"
									value=<fmt:message key="save" />>
							</div>
						</form>
						<c:if test="${not empty userSettingsErrorMessage}">
							<label class="validation"><fmt:message key="${userSettingsErrorMessage}"/></label>
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