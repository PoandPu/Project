<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>
<%@ taglib prefix="ex" uri="/WEB-INF/custom/pagination.tld"%>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<html>
<c:set var = "title" value ="profile_jsp.title"></c:set>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
	<table id="main-container">
		<%@ include file="/WEB-INF/jspf/header.jspf"%>
		<tr>
			<td class="content">
				<table id="customers">
					<tr>
						<th colspan="2"></th>
					</tr>
					<tr>
						<td><fmt:message key="profile_jsp.login" /></td>
						<td>${user.login}</td>
					</tr>
					<tr>
						<td><fmt:message key="profile_jsp.first_name" /></td>
						<td>${user.firstName}</td>
					</tr>
					<tr>
						<td><fmt:message key="profile_jsp.last_name" /></td>
						<td>${user.lastName}</td>
					</tr>
					<tr>
						<td><fmt:message key="profile_jsp.email" /></td>
						<td>${user.email}</td>
					</tr>
					<tr>
						<td><fmt:message key="profile_jsp.language" /></td>
						<td>${user.language}</td>
					</tr>
					<tr>
						<th colspan="2"></th>
					</tr>
				</table> <c:if test="${Role.getRole(user) eq Role.CLIENT}">
					<br>
					<form action="reportCreator" method="post">
						<input type="hidden" name="userId" value="${user.id}" /> <input
							class="edit" type="submit" name="downloadReport"
							value="<fmt:message key="profile_jsp.create_report"/>">
					</form>
					<table id="customers">
						<tr>
							<th colspan="4" style="text-align: center">
								<h2>
									<fmt:message key="profile_jsp.results" />
								</h2>
							</th>
						</tr>
						<tr>
							<th><fmt:message key="profile_jsp.subject_name" /> <c:if test="${language == 'ru'}">
									<tag:sort command="userProfile" parameter="&userId=${user.id}" orderBy="subjects.name_ru" page="${page}" lines="${lines}"></tag:sort>
								</c:if> <c:if test="${language == 'en'}">
									<tag:sort command="userProfile" parameter="&userId=${user.id}" orderBy="subjects.name_en" page="${page}" lines="${lines}"></tag:sort>
								</c:if></th>
							<th><fmt:message key="profile_jsp.test_name" /> <c:if
									test="${language == 'ru'}">
									<tag:sort command="userProfile" parameter="&userId=${user.id}" orderBy="tests.name_ru" page="${page}" lines="${lines}"></tag:sort>	
								</c:if> <c:if test="${language == 'en'}">
									<tag:sort command="userProfile" parameter="&userId=${user.id}" orderBy="tests.name_en" page="${page}" lines="${lines}"></tag:sort>
								</c:if></th>
							<th><fmt:message key="profile_jsp.mark" />
								<tag:sort command="userProfile" parameter="&userId=${user.id}" orderBy="mark" page="${page}" lines="${lines}"></tag:sort>
							</th>
							<th><fmt:message key="profile_jsp.test_date" />
								<tag:sort command="userProfile" parameter="&userId=${user.id}" orderBy="test_date" page="${page}" lines="${lines}"></tag:sort>
							</th>
						</tr>
						<c:forEach var="result" items="${results}">
							<tr>
								<td><c:out
										value="${language eq 'ru' ? result.subjectNameRu : result.subjectNameEn}" />
								</td>

								<td><c:out
										value="${language eq 'ru' ? result.testNameRu :result.testNameEn}" />
								</td>

								<td><c:out value="${result.mark}%">
									</c:out></td>

								<td><c:out value="${result.testDate}">
									</c:out></td>
							</tr>
						</c:forEach>
						<tr>
							<th colspan="4"><ex:pagination
									command="userProfile&userId=${user.id}" page="${page}"
									lines="${lines}" orderBy="${orderBy}" direction="${direction}"></ex:pagination>
							</th>
						</tr>
					</table>
				</c:if>
			</td>
		</tr>
		<%@ include file="/WEB-INF/jspf/footer.jspf"%>
	</table>
</body>