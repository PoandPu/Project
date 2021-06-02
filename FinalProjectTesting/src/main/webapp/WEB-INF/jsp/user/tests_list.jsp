<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>
<%@ taglib prefix="ex" uri="/WEB-INF/tag/pagination.tld"%>
<html>
<title><fmt:message key="tests_list_jsp.title" /></title>
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
								<a
									href="controller?command=viewTestsList&subjectId=${subjectId}&orderBy=name_ru&direction=ASC&page=${page}&lines=${lines}"
									class="bot1g">\/</a>
								<a
									href="controller?command=viewTestsList&subjectId=${subjectId}&orderBy=name_ru&direction=DESC&page=${page}&lines=${lines}"
									class="bot1g">/\</a>
							</c:if> <c:if test="${language == 'en'}">
								<a
									href="controller?command=viewTestsList&subjectId=${subjectId}&orderBy=name_en&direction=ASC&page=${page}&lines=${lines}"
									class="bot1g">\/</a>
								<a
									href="controller?command=viewTestsList&subjectId=${subjectId}&orderBy=name_en&direction=DESC&page=${page}&lines=${lines}"
									class="bot1g">/\</a>
							</c:if></th>
						<th><fmt:message key="tests_list_jsp.duration" /> <a
							href="controller?command=viewTestsList&subjectId=${subjectId}&orderBy=time_minutes&direction=ASC&page=${page}&lines=${lines}"
							class="bot1g">\/</a> <a
							href="controller?command=viewTestsList&subjectId=${subjectId}&orderBy=time_minutes&direction=DESC&page=${page}&lines=${lines}"
							class="bot1g">/\</a></th>
						<th><fmt:message key="tests_list_jsp.number_of_requests" />
							<a
							href="controller?command=viewTestsList&subjectId=${subjectId}&orderBy=numb_of_requests&direction=ASC&page=${page}&lines=${lines}"
							class="bot1g">\/</a> <a
							href="controller?command=viewTestsList&subjectId=${subjectId}&orderBy=numb_of_requests&direction=DESC&page=${page}&lines=${lines}"
							class="bot1g">/\</a></th>
						<th><fmt:message key="tests_list_jsp.difficulty_level" /> <a
							href="controller?command=viewTestsList&subjectId=${subjectId}&orderBy=difficulty_level_id&direction=ASC&page=${page}&lines=${lines}"
							class="bot1g">\/</a> <a
							href="controller?command=viewTestsList&subjectId=${subjectId}&orderBy=difficulty_level_id&direction=DESC&page=${page}&lines=${lines}"
							class="bot1g">/\</a></th>
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
				</table>
			</td>
		</tr>
		<%@ include file="/WEB-INF/jspf/footer.jspf"%>
	</table>
</body>
</html>