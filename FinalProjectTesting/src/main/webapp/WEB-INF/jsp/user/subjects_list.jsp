<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>
<%@ taglib prefix="ex" uri="/WEB-INF/custom/pagination.tld"%>
<%@ taglib prefix="tag" tagdir="/WEB-INF/tags" %>
<html>
<c:set var = "title" value ="subjects_list_jsp.subjects_list"></c:set>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
	<table id="main-container">
		<%@ include file="/WEB-INF/jspf/header.jspf"%>
		<tr>
			<td class="content">
				<table id="customers">
					<tr>
						<th><fmt:message key="subjects_list_jsp.subject_name" /> <c:if
								test="${language == 'ru'}">
								<tag:sort command="viewAllSubjects" orderBy="name_ru" page="${page}" lines="${lines}"></tag:sort>
							
							</c:if> <c:if test="${language == 'en'}">
								<tag:sort command="viewAllSubjects" orderBy="name_en" page="${page}" lines="${lines}"></tag:sort>
							</c:if></th>
					</tr>
					<c:forEach var="subject" items="${subjects}">
						<tr>
							<td><a href="controller?command=viewTestsList&subjectId=${subject.id}">
									<c:out value="${language eq 'ru' ? subject.nameRu : subject.nameEn}" />
							</a></td>
						</tr>
					</c:forEach>
					<tr>
						<th><ex:pagination command="viewAllSubjects" page="${page}"
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