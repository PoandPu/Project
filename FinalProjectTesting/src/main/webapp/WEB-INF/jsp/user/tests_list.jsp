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
					</tr>


					<c:forEach var="test" items="${tests}">
						<tr>
							<td><c:out
									value="${language eq 'ru' ? test.nameRu : test.nameEn}" /></td>
							<td><c:out value="${test.time}" /></td>
							<td><c:out value="${test.requests}" /></td>
							<td><c:out
									value="${language eq 'ru' ? test.difficultyNameRu : test.difficultyNameEn}" /></td>

							<td>

								<form action="controller" method="post">
									<input type="hidden" name="command" value="viewTest" /> <input
										type="hidden" name="testId" value="${test.id}" /><input
										type="hidden" name="subjectId" value="${subjectId}" /> <input
										class="edit" type="submit" name="submit"
										value="<fmt:message key="tests_list_jsp.take_test"/>">
								</form>

							</td>
					</c:forEach>
					<tr>
						<th colspan="5"><ex:pagination
								command="viewTestsList&subjectId=${subjectId}" page="${page}"
								lines="${lines}" orderBy="${orderBy}" direction="${direction}"></ex:pagination>
						</th>
					</tr>
					<c:if test="${not empty testErrorMessage}">
						<tr>
							<td colspan="2" style="border: none"><label
								class="validation" style = "width:100%"><fmt:message
										key="${testErrorMessage}" /> ${language eq 'ru' ? sessionScope.subject.nameRu : sessionScope.subject.nameEn}
									--> ${language eq 'ru' ? sessionScope.test.nameRu : sessionScope.test.nameEn}
							</label> <c:remove var="testErrorMessage" /></td>
							<td colspan="1" style="border: none">
							
								<form action="controller" method="post">
									<input type="hidden" name="command" value="viewTest" /> <input
										type="hidden" name="testId" value="${test.id}" /><input
										type="hidden" name="subjectId" value="${subjectId}" /> <input
										class="form" style = "float: left" type="submit" name="submit"
										value="<fmt:message key="tests_list_jsp.take_test"/>">
								</form>
							</td>
							<td colspan="2" style="border: none">
								<form action="controller" method="post">
									<input type="hidden" name="command" value="failTest" /> <input
										type="hidden" name="testId" value="${test.id}" /><input
										type="hidden" name="subjectId" value="${subjectId}" /> <input
										class="delete" style = "float: left" type="submit" name="submit"
										value="<fmt:message key="tests_list_jsp.fail_test"/>">
								</form>
							</td>
						</tr>
					</c:if>
				</table>
			</td>
		</tr>
		<%@ include file="/WEB-INF/jspf/footer.jspf"%>
	</table>
</body>
</html>