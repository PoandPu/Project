<%@ include file="/WEB-INF/jspf/directive/page.jspf"%>
<%@ include file="/WEB-INF/jspf/directive/taglib.jspf"%>

<html>
<c:set var = "title" value ="admin.edit_subject_jsp.title"></c:set>
<%@ include file="/WEB-INF/jspf/head.jspf"%>
<body>
	<table id="main-container">
		<%@ include file="/WEB-INF/jspf/header.jspf"%>
		<tr>
			<td class="content">

				<div align="center">
					<div class="container">
						<form action="controller" method="post">
							<input type="hidden" name="command" value="editSubject" />
							<input type="hidden" name="subjectId" value="${subject.id}" />
							<div align="center">
								<label><fmt:message
										key="admin.edit_subject_jsp.title" /></label>
							</div>
							<div class="row">
								<div class="col-25">
									<label><fmt:message
											key="admin.add_subject_jsp.name_ru" /></label>
								</div>
								<div class="col-75">
									<input type="text" name="nameRu" value ="${subject.nameRu}">
								</div>
							</div>

							<div class="row">
								<div class="col-25">
									<label><fmt:message
											key="admin.add_subject_jsp.name_en" /></label>
								</div>
								<div class="col-75">
									<input type="text" name="nameEn" value = "${subject.nameEn}" >
								</div>
							</div>
							<div class="row">
								<input class="form" type="submit" name="submit"
									value=<fmt:message key="save" />>
									<input class="delete" type="submit" name="delete"
									value=<fmt:message key="delete" />>			
							</div>								
						</form>
						<c:if test="${not empty subjectErrorMessage}">
							<label class="validation"><fmt:message
									key="${subjectErrorMessage }" /></label>
							<c:remove var="subjectErrorMessage" />
						</c:if>
					</div>
				</div>
			</td>
		</tr>
	<%@ include file="/WEB-INF/jspf/footer.jspf" %>
	</table>
</body>
</html>