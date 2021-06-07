<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>
<html>
<title><fmt:message key="test_jsp.title" /></title>
<%@ include file="/WEB-INF/jspf/head.jspf"%>

<script type="text/javascript">
	function startTimer(duration, display) {
		var timer = duration, minutes, seconds;
		setInterval(function() {
			minutes = parseInt(timer / 60, 10)
			seconds = parseInt(timer % 60, 10);

			minutes = minutes < 10 ? "0" + minutes : minutes;
			seconds = seconds < 10 ? "0" + seconds : seconds;

			display.textContent = minutes + ":" + seconds;
			
			if (--timer < 0) {
				timer = 0;
			}
		}, 1000);
	}

	window.onload = function() {
		var time = 60 * ${minutes} + ${seconds}, display = document
				.querySelector('#time');
		startTimer(time < 0 ? 0 : time, display);
	};
</script>

<body>
	<table id="main-container">
		<%@ include file="/WEB-INF/jspf/header.jspf"%>
		<tr>
			<td class="content">
				<div class="timer">
					Timer : <span id="time"> <c:choose><c:when test="${minutes < 0}">00</c:when><c:when test="${minutes < 10}">0${minutes}</c:when><c:when test="${minutes > 10}">${minutes}</c:when></c:choose>:<c:choose><c:when test="${seconds < 0}">00</c:when><c:when test="${seconds < 10}">0${seconds}</c:when><c:when test="${seconds > 10}">${seconds}</c:when>
						</c:choose>

					</span>
				</div>
				<div align="center">
					<div class="container" style="width: 1000px">
						<form action="controller" method="post">
							<input type="hidden" name="command" value="checkTest" /> <input
								type="hidden" name="questions" value="${questions}" /> <input
								type="hidden" name="testId" value="${test.id}" /> <input
								type="hidden" name="answersList" value="${answersList}" />

							<div align="center">
								<h3>
									<label>${language eq 'ru' ? test.nameRu : test.nameEn}</label>
								</h3>

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
											</div>
											<ol class="latin">
												<c:forEach var="answer"
													items="${answersList.get(ctr.index)}">
													<li>
														<div class="row">

															<input type="checkbox" id="coding${answer.id}"
																name="answerNumb${answer.id}"> <label
																for="coding${answer.id}">${language eq 'ru' ? answer.nameRu : answer.nameEn}</label>
														</div>
													</li>
												</c:forEach>
											</ol>
										</li>
									</c:forEach>
								</ol>
							</div>
							<div class="row">
								<input class="form" type="submit" name="submit"
									value=<fmt:message key="test_jsp.finish" />>
							</div>
						</form>
					</div>
				</div>
			</td>
		</tr>
		<%@ include file="/WEB-INF/jspf/footer.jspf"%>
	</table>
</body>
</html>