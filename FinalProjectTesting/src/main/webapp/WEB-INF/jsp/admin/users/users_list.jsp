<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>
<%@ taglib prefix="ex" uri="/WEB-INF/custom/pagination.tld"%>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<html>
<c:set var = "title" value ="admin.view_users_jsp.title"></c:set>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
	<table id="main-container">
		<%@ include file="/WEB-INF/jspf/header.jspf"%>
		<tr>
			<td class="content">
				<table id="customers">
					<tr>
						<th colspan="10">
						
							<div style="float: right; width: 19%;">
								<form action="controller">
									<input type="hidden" name="command" value="findUser" /> <input
										type="text" name="pattern"
										placeholder="<fmt:message key="admin.view_users_jsp.login"/> / <fmt:message key="admin.view_users_jsp.first_name"/> / <fmt:message key="admin.view_users_jsp.last_name"/> / <fmt:message key="admin.view_users_jsp.email"/> / <fmt:message key="admin.view_users_jsp.role"/> / Id">
									<input class="form" type="submit"
										value="<fmt:message key="admin.view_users_jsp.search"/>" />
								</form>
							</div>
							<div style="float: right; width: 5%;">
								<form action="controller">
									<input type="hidden" name="command" value="findBestUsers" /> 
									<select name="period">
										<option value="DAY">1 day</option>
										<option value="WEEK">week</option>
										<option value="MONTH" >month</option>
									</select>
									<input class="form" type="submit"
										value="<fmt:message key="admin.view_users_jsp.search"/>" />
								</form>
							</div>
						</th>
					</tr>

					<tr>
						<th>Id &nbsp; 
							<tag:sort command="viewUsersList" orderBy="id" page="${page}" lines="${lines}"></tag:sort>
						</th>
						<th><fmt:message key="admin.view_users_jsp.login"/>&nbsp;
							<tag:sort command="viewUsersList" orderBy="login" page="${page}" lines="${lines}"></tag:sort>
						</th>
						<th><fmt:message key="admin.view_users_jsp.first_name"/>&nbsp;
							<tag:sort command="viewUsersList" orderBy="first_name" page="${page}" lines="${lines}"></tag:sort>
						</th>
						<th><fmt:message key="admin.view_users_jsp.last_name"/>&nbsp;
							<tag:sort command="viewUsersList" orderBy="last_name" page="${page}" lines="${lines}"></tag:sort>
						</th>
						<th><fmt:message key="admin.view_users_jsp.email"/>&nbsp;
							<tag:sort command="viewUsersList" orderBy="email" page="${page}" lines="${lines}"></tag:sort>
						</th>
						<th><fmt:message key="admin.view_users_jsp.role"/>&nbsp;
							<tag:sort command="viewUsersList" orderBy="role_id" page="${page}" lines="${lines}"></tag:sort>
						</th>
						<th><fmt:message key="admin.view_users_jsp.status"/>&nbsp;
							<tag:sort command="viewUsersList" orderBy="isBlocked" page="${page}" lines="${lines}"></tag:sort>
						</th>
						<c:if test="${user.averageMark ne null}">
						<th></th>
						</c:if>
						<th></th>
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
							<c:if test="${user.averageMark ne null}">
							<td><c:out value="${user.averageMark}" /></td>
							</c:if>
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
						<th colspan="10"><ex:pagination command="viewUsersList"
								page="${page}" lines="${lines}" orderBy="${orderBy}"
								direction="${direction}"></ex:pagination></th>
					</tr>
				</table>
			</td>
		</tr>
		<%@ include file="/WEB-INF/jspf/footer.jspf"%>
	</table>
</body>
</html>