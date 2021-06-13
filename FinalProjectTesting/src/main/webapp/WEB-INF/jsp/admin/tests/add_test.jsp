<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>
<html>
<c:set var = "title" value ="admin.add_test_jsp.title"></c:set>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
	<table id="main-container">
		<%@ include file="/WEB-INF/jspf/header.jspf"%>
		<tr>
			<td class="content">

				<div align="center">
					<div class="container">
						<form action="controller" method="post">
							<input type="hidden" name="command" value="addTest" /> <input
								type="hidden" name="subjectId" value="${subjectId}" />
							<div align="center">
								<label><fmt:message key="admin.add_test_jsp.title" /></label>
							</div>
							<div class="row">
								<div class="col-25">
									<label><fmt:message key="admin.edit_test_jsp.name_ru" /></label>
								</div>
								<div class="col-75">
									<input type="text" name="nameRu" value="${test.nameRu}"
										required>
								</div>
							</div>
							<div class="row">
								<div class="col-25">
									<label><fmt:message key="admin.edit_test_jsp.name_en" /></label>
								</div>
								<div class="col-75">
									<input type="text" name="nameEn" value="${test.nameEn}"
										required>
								</div>
							</div>

							<div class="row">
								<div class="col-25">
									<label><fmt:message key="admin.edit_test_jsp.duration" /></label>
								</div>
								<div class="col-75">
									<input type="text" name="time" value="${test.time}" required>
								</div>
							</div>
							<div class="row">
								<div class="col-25">
									<label><fmt:message
											key="admin.edit_test_jsp.difficulty_level" /></label>
								</div>
								<div class="col-75">
									<select name="difficultyLevel">
										<option value="1"><fmt:message
											key="admin.edit_test_jsp.difficulty_level.very_easy" /></option>
										<option value="2"><fmt:message
											key="admin.edit_test_jsp.difficulty_level.easy" /></option>
										<option value="3"><fmt:message
											key="admin.edit_test_jsp.difficulty_level.medium" /></option>
										<option value="4"><fmt:message
											key="admin.edit_test_jsp.difficulty_level.hard" /></option>
										<option value="5"><fmt:message
											key="admin.edit_test_jsp.difficulty_level.very_hard" /></option>
									</select>
								</div>
							</div>
							<div class="row">
							<a href ="controller?command=viewTestsList&subjectId=${subjectId}" class = "bot1"><fmt:message
									key="test_jsp.behind" /></a>
								<input class="form" type="submit" name="submit"
									value=<fmt:message key="save" />>
							</div>
						</form>
						<c:if test="${not empty testErrorMessage}">
							<label class="validation"><fmt:message
									key="${testErrorMessage }" /></label>
							<c:remove var="testErrorMessage" />
						</c:if>
					</div>
				</div>
			</td>
		</tr>
		<%@ include file="/WEB-INF/jspf/footer.jspf"%>
	</table>
</body>
</html>