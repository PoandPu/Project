package ua.epam.pavelchuk.final_project.web.command;


import static org.mockito.Mockito.mock; 
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;

import ua.epam.pavelchuk.final_project.db.Role;
import ua.epam.pavelchuk.final_project.db.dao.SubjectDAO;
import ua.epam.pavelchuk.final_project.db.entity.Subject;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.common.ViewAllSubjectsCommand;

public class ViewAllSubjectsCommandTest {

	@Test
	public void testDoPost() throws IOException, ServletException, AppException {
		final HttpServletRequest request = mock(HttpServletRequest.class);
		final HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession mockSession = mock(HttpSession.class);
		
		SubjectDAO subjectDAO = mock(SubjectDAO.class);
		ViewAllSubjectsCommand testCommand = new ViewAllSubjectsCommand(subjectDAO);
		
		
		
		when(request.getParameter(ParameterNames.PAGINATION_PAGE)).thenReturn("0");
		when(request.getParameter(ParameterNames.PAGINATION_LINES)).thenReturn("0");
		when(request.getParameter(ParameterNames.ORDER_BY)).thenReturn("id");
		when(request.getParameter(ParameterNames.DIRECTION)).thenReturn("ASC");
		when(subjectDAO.findAllOrderBy("id", "ASC", 0, 10)).thenReturn(new ArrayList<Subject>());
		when(request.getSession()).thenReturn(mockSession);
		when(mockSession.getAttribute(AttributeNames.USER_ROLE)).thenReturn(Role.CLIENT);
		
		testCommand.execute(request, response, HttpMethod.GET);
		
		verify(subjectDAO, times(1)).findAllOrderBy("id", "ASC", 0, 10);
		
	}
}
