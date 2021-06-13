<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>
<html>
<c:set var = "title" value ="admin.edit_test_content_jsp.title"></c:set>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
	<table id="main-container">
		<%@ include file="/WEB-INF/jspf/header.jspf"%>
		<tr>
			<td class="content">

				<div align="center">
					<div class="container" style="width: 800px">
						<form id="addAnswer" action="controller" method="post">
							<input type="hidden" name="command" value="addAnswer" /><input
								type="hidden" name="testId" value="${testId}" /> <input
								type="hidden" name="questionId" value="${question.id}" /> <input
								type="hidden" name="subjectId" value="${subjectId}" />
						</form>
						<form id="deleteAnswer" action="controller" method="post">
							<input type="hidden" name="command" value="deleteAnswer" /><input
								type="hidden" name="testId" value="${testId}" /> <input
								type="hidden" name="questionId" value="${question.id}" /> <input
								type="hidden" name="subjectId" value="${subjectId}" />
						</form>
						<form id="deleteQuestion" action="controller" method="post">
							<input type="hidden" name="command" value="deleteQuestion" /> <input
								type="hidden" name="testId" value="${testId}" /> <input
								type="hidden" name="questionId" value="${question.id}" /> <input
								type="hidden" name="subjectId" value="${subjectId}" />
						</form>
						<form id="update" action="controller" method="post">
							<input type="hidden" name="command" value="editTestContent" /> <input
								type="hidden" name="testId" value="${testId}" /> <input
								type="hidden" name="questionId" value="${question.id}" /> <input
								type="hidden" name="subjectId" value="${subjectId}" />
						</form>

						<div align="center">
							<h3>
								<label><fmt:message
										key="admin.edit_test_content_jsp.edit_question" /></label>
							</h3>
						</div>
						<div class="row">
							<div class="col-25">
								<label><fmt:message
										key="admin.edit_test_content_jsp.title_ru" /></label>
							</div>
							<div class="col-75">
								<input type="text" name="titleRu" value="${question.nameRu}"
									form="update">
							</div>
						</div>
						<div class="row">
							<div class="col-25">
								<label><fmt:message
										key="admin.edit_test_content_jsp.title_en" /></label>
							</div>
							<div class="col-75">
								<input type="text" name="titleEn" value="${question.nameEn}"
									form="update">
							</div>
						</div>
						<div class="row">
							<div class="col-25">
								<fmt:message key="admin.edit_test_content_jsp.answers_options" />
							</div>
							<div class="col-75">
								<input class="form" type="submit" id="addAnswer"
									value="<fmt:message
											key="admin.edit_test_content_jsp.add_answer" />"
									form="addAnswer">
							</div>
						</div>
						<ol>
							<c:forEach var="answer" items="${answers}">
								<hr>
								<li>
									<div class="row">
										<div class="col-25">
											<label><fmt:message
													key="admin.edit_test_content_jsp.option_ru" /></label>
										</div>
										<div class="col-75">
											<input type="text" name="optionRu${answer.id}"
												value="${answer.nameRu}" form="update">
										</div>
									</div>
								</li>
								<div class="row">
									<div class="col-25">
										<label><fmt:message
												key="admin.edit_test_content_jsp.option_en" /></label>
									</div>
									<div class="col-75">
										<input type="text" name="optionEn${answer.id}"
											value="${answer.nameEn}" form="update">
									</div>
								</div>
								<div class="row">
									<div class="col-75">
										<label><fmt:message
												key="admin.edit_test_content_jsp.correct" /></label> <input
											type="checkbox" name="isCorrect${answer.id}"
											${answer.isCorrect ? 'Checked' : ''} form="update">
									</div>


									<input class="delete" type="submit" name="delete${answer.id}"
										value=<fmt:message key="admin.edit_test_content_jsp.delete" />
										form="deleteAnswer">


								</div>
							</c:forEach>
						</ol>
						<hr>
						<br>
						<div class="row">
							<input class="form" type="submit" name="submit"
								value=<fmt:message key="admin.edit_test_content_jsp.save_changes" />
								form="update">
							<c:remove var="command" />
							<input class="delete" type="submit" name="delete"
								value=<fmt:message key="admin.edit_test_content_jsp.delete" />
								form="deleteQuestion">
						</div>

						<div class="row">
							<div align="center">
								<a
									href="controller?command=viewTest&subjectId=${subjectId}&testId=${testId}"
									class="bot1"><fmt:message
										key="admin.edit_test_content_jsp.go_back" /></a>
							</div>
						</div>
						<c:if test="${not empty testContentErrorMessage}">
							<label class="validation"><fmt:message
									key="${testContentErrorMessage }" /></label>
							<c:remove var="testContentErrorMessage" />
						</c:if>
					</div>
				</div>
			</td>
		</tr>
		<%@ include file="/WEB-INF/jspf/footer.jspf"%>
	</table>
</body>
</html>