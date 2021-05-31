<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>
<html>
<title><fmt:message key="test_jsp.title"/></title>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
	<table id="main-container">
		<%@ include file="/WEB-INF/jspf/header.jspf"%>
		<tr>
			<td class="content">
				<div align="center">
					<div class="container" style="width: 800px">
						<div align="center">
							<h2>
								<label><fmt:message key="admin.test_jsp.editor" /></label>
							</h2>
							<p>${language eq 'ru' ? test.nameRu : test.nameEn}
							<h3></h3>
						</div>

						<div align="left">
							<ol>
								<c:forEach var="question" items="${questions}" varStatus="ctr">
									<li>
										<div class="row">
											<div class="col-75">
												<c:out
													value="${language eq 'ru' ? question.nameRu : question.nameEn}" />
											</div>
											<div class="col-25">
												<a class="bot2"
													href="controller?command=editTestContent&subjectId=${subjectId}&testId=${test.id}&questionId=${question.id}">
													<fmt:message key="admin.test_jsp.edit_content" />
												</a>
											</div>

										</div>
										<ol class="latin">
											<c:forEach var="answer" items="${answersList.get(ctr.index)}">
												<li><div class="row">
														<label for="coding${answer.id}">${language eq 'ru' ? answer.nameRu : answer.nameEn}</label>
													</div></li>
											</c:forEach>
										</ol>
									</li>
								</c:forEach>
							</ol>

						</div>
						<div class="row">
							<div class="col-25">
								<form action="controller" method="post">
									<input type="hidden" name="command" value="addQuestion" /> <input
										type="hidden" name="subjectId" value="${subjectId}" /><input
										type="hidden" name="testId" value="${test.id}" /> <input
										class="edit" type="submit"
										value="<fmt:message key="admin.test_jsp.add_question"/>">
								</form>
							</div>

							<div class="col-75">
								<form action="controller">
									<input type="hidden" name="command" value="viewTestsList" /> <input
										type="hidden" name="subjectId" value="${subjectId}" /> <input
										class="form" type="submit" name="submit"
										value=<fmt:message key="admin.test_jsp.save_changes" />>
								</form>
							</div>
						</div>
					</div>
				</div>

			</td>
		</tr>
	</table>
</body>
</html>