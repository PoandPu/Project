<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>
<%@ taglib prefix="ex" uri="/WEB-INF/tag/pagination.tld"%>
<html>
<title><fmt:message key="admin.view_users_jsp.title" /></title>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
	<table id="main-container">
		<%@ include file="/WEB-INF/jspf/header.jspf"%>
		<tr>
			<td class="content">
				<table id="customers">
					<tr>
						<th colspan="9">
							<div style="float: right; width: 19%;">
								<form action="controller">
									<input type="hidden" name="command" value="findUser" /> <input
										type="text" name="pattern"
										placeholder="<fmt:message key="admin.view_users_jsp.login"/> / <fmt:message key="admin.view_users_jsp.first_name"/> / <fmt:message key="admin.view_users_jsp.last_name"/> / <fmt:message key="admin.view_users_jsp.email"/> / <fmt:message key="admin.view_users_jsp.role"/> / Id">
									<input class="form" type="submit"
										value="<fmt:message key="admin.view_users_jsp.search"/>" />
								</form>
							</div>
						</th>
					</tr>


					<tr>
						<th>Id &nbsp; <a
							href="controller?command=viewUsersList&orderBy=id&direction=ASC&page=${page}&lines=${lines}"
							class="bot1g">\/</a> <a
							href="controller?command=viewUsersList&orderBy=id&direction=DESC&page=${page}&lines=${lines}"
							class="bot1g">/\</a>

						</th>
						<th><fmt:message key="admin.view_users_jsp.login" />&nbsp;<a
							href="controller?command=viewUsersList&orderBy=login&direction=ASC&page=${page}&lines=${lines}"
							class="bot1g">\/</a> <a
							href="controller?command=viewUsersList&orderBy=login&direction=DESC&page=${page}&lines=${lines}"
							class="bot1g">/\</a></th>
						<th><fmt:message key="admin.view_users_jsp.first_name" />&nbsp;<a
							href="controller?command=viewUsersList&orderBy=first_name&direction=ASC&page=${page}&lines=${lines}"
							class="bot1g">\/</a> <a
							href="controller?command=viewUsersList&orderBy=first_name&direction=DESC&page=${page}&lines=${lines}"
							class="bot1g">/\</a></th>
						<th><fmt:message key="admin.view_users_jsp.last_name" />&nbsp;<a
							href="controller?command=viewUsersList&orderBy=last_name&direction=ASC&page=${page}&lines=${lines}"
							class="bot1g">\/</a> <a
							href="controller?command=viewUsersList&orderBy=last_name&direction=DESC&page=${page}&lines=${lines}"
							class="bot1g">/\</a></th>
						<th><fmt:message key="admin.view_users_jsp.email" />&nbsp;<a
							href="controller?command=viewUsersList&orderBy=email&direction=ASC&page=${page}&lines=${lines}"
							class="bot1g">\/</a> <a
							href="controller?command=viewUsersList&orderBy=email&direction=DESC&page=${page}&lines=${lines}"
							class="bot1g">/\</a></th>
						<th><fmt:message key="admin.view_users_jsp.role" /><a
							href="controller?command=viewUsersList&orderBy=role_id&direction=ASC&page=${page}&lines=${lines}"
							class="bot1g">\/</a> <a
							href="controller?command=viewUsersList&orderBy=role_id&direction=DESC&page=${page}&lines=${lines}"
							class="bot1g">/\</a></th>
						<th><fmt:message key="admin.view_users_jsp.status" />&nbsp;<a
							href="controller?command=viewUsersList&orderBy=isBlocked&direction=ASC&page=${page}&lines=${lines}"
							class="bot1g">\/</a> <a
							href="controller?command=viewUsersList&orderBy=isBlocked&direction=DESC&page=${page}&lines=${lines}"
							class="bot1g">/\</a></th>
						<th></th>
						<th></th>
					</tr>
					<c:forEach var="user" items="${users}">
						<tr>
							<td><c:out value="${user.id}" /></td>
							<td><c:out value="${user.login}" /></td>
							<td><c:out value="${user.firstName}" /></td>
							<td><c:out value="${user.lastName}" /></td>
							<td><c:out value="${user.email}" /></td>
							<td><c:out value="${Role.getRole(user)}" /></td>
							<td><c:out value="${user.isBlocked ? 'Blocked' : 'Free'}" />
							</td>
							<td><a
								href="controller?command=userProfile&userId=${user.id}"
								class="bot1"><fmt:message
										key="admin.view_users_jsp.view_profile" /></a></td>
							<td><c:if test="${Role.getRole(user) eq Role.CLIENT}">
									<a href="controller?command=editUser&userId=${user.id}"
										class="bot1"><fmt:message key="admin.view_users_jsp.edit" /></a>
								</c:if></td>
						</tr>
					</c:forEach>
					<tr>
						<th colspan="9"><ex:pagination command="viewUsersList"
								page="${page}" lines="${lines}" orderBy="${orderBy}"
								direction="${direction}"></ex:pagination></th>
					</tr>
				</table>
			</td>
		</tr>

	</table>
</body>
</html>