<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>
<%@ taglib prefix="ex" uri="/WEB-INF/custom/pagination.tld"%>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<html>
<c:set var = "title" value ="tests_list_jsp.title"></c:set>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
	<table id="main-container">
		<%@ include file="/WEB-INF/jspf/header.jspf"%>
		<tr>
			<td class="content">
				<table id="customers">

					<tr>
						<th><fmt:message key="tests_list_jsp.test_name" /> <c:if
								test="${language == 'ru'}">
								<tag:sort command="viewTestsList" parameter="&subjectId=${subjectId}" orderBy="name_ru" page="${page}" lines="${lines}"></tag:sort>
							</c:if> <c:if test="${language == 'en'}">
								<tag:sort command="viewTestsList" parameter="&subjectId=${subjectId}" orderBy="name_en" page="${page}" lines="${lines}"></tag:sort>
							</c:if></th>
						<th><fmt:message key="tests_list_jsp.duration" />
							<tag:sort command="viewTestsList" parameter="&subjectId=${subjectId}" orderBy="time_minutes" page="${page}" lines="${lines}"></tag:sort>
						</th>
						<th><fmt:message key="tests_list_jsp.number_of_requests" />
							<tag:sort command="viewTestsList" parameter="&subjectId=${subjectId}" orderBy="numb_of_requests" page="${page}" lines="${lines}"></tag:sort>
						</th>
						<th><fmt:message key="tests_list_jsp.difficulty_level" /> 
							<tag:sort command="viewTestsList" parameter="&subjectId=${subjectId}" orderBy="difficulty_level_id" page="${page}" lines="${lines}"></tag:sort>
						</th>
						<th></th>
						<th></th>
					</tr>


					<c:forEach var="test" items="${tests}">
						<tr>
							<td><c:out
									value="${language eq 'ru' ? test.nameRu : test.nameEn}" /></td>
							<td><c:out value="${test.time}" /></td>
							<td><c:out value="${test.requests}" /></td>
							<td><c:out
									value="${language eq 'ru' ? test.difficultyNameRu : test.difficultyNameEn}" /></td>

							<td><form action="controller">
									<input type="hidden" name="command" value="viewTest" /><input
										type="hidden" name="subjectId" value="${subjectId}" /> <input
										type="hidden" name="testId" value="${test.id}" /> <input
										class="edit" type="submit" name="submit"
										value="<fmt:message key="admin.tests_list_jsp.edit_test_content"/>">

								</form></td>

							<td><form action="controller">
									<input type="hidden" name="command" value="editTest" /><input
										type="hidden" name="subjectId" value="${subjectId}" /> <input
										type="hidden" name="testId" value="${test.id}" /> <input
										class="edit" type="submit" name="submit"
										value="<fmt:message key="admin.tests_list_jsp.edit_test"/>">

								</form></td>
					</c:forEach>
					<tr>
						<td><form action="controller">
								<input type="hidden" name="command" value="addTest" /><input
									type="hidden" name="subjectId" value="${subjectId}" /> <input
									class="edit" type="submit" name="submit"
									value="<fmt:message key="admin.tests_list_jsp.add_test"/>">

							</form></td>
					</tr>
					<tr>
						<th colspan="6"><ex:pagination
								command="viewTestsList&subjectId=${subjectId}" page="${page}"
								lines="${lines}" orderBy="${orderBy}" direction="${direction}"></ex:pagination></th>
					</tr>
				</table>
			</td>
		</tr>
		<%@ include file="/WEB-INF/jspf/footer.jspf"%>
	</table>
</body>
</html>