<%@ tag pageEncoding="UTF-8"%>
<%@ attribute name="parameter" required="false" rtexprvalue="true"%>
<%@ attribute name="command" required="true" rtexprvalue="true"%>
<%@ attribute name="orderBy" required="true" rtexprvalue="true"%>
<%@ attribute name="page" required="true" rtexprvalue="true"%>
<%@ attribute name="lines" required="true" rtexprvalue="true"%>
<a href="controller?command=${command}${parameter}&orderBy=${orderBy}&direction=ASC&page=${page}&lines=${lines}"
	class="bot1g">\/</a>
<a href="controller?command=${command}${parameter}&orderBy=${orderBy}&direction=DESC&page=${page}&lines=${lines}"
	class="bot1g">/\</a>