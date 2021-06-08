package ua.epam.pavelchuk.final_project.web.command;


import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.anyInt;
import static org.mockito.Mockito.any;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.mockito.MockedStatic;

import ua.epam.pavelchuk.final_project.db.Role;
import ua.epam.pavelchuk.final_project.db.dao.SubjectDAO;
import ua.epam.pavelchuk.final_project.db.entity.Subject;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.common.ViewAllSubjectsCommand;

public class ViewAllSubjectsCommandTest {

	@Test
	public void doPost() throws IOException, ServletException, AppException, DBException {
		ViewAllSubjectsCommand testCommand = new ViewAllSubjectsCommand();
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		SubjectDAO subjectDAO = mock(SubjectDAO.class);
		HttpSession mockSession = mock(HttpSession.class);
		when(request.getSession()).thenReturn(mockSession);
		when(request.getParameter(anyString())).thenReturn("1");
		try (MockedStatic<SubjectDAO> subjectDAOMockedStatic = mockStatic(SubjectDAO.class)) {
			when(SubjectDAO.getInstance()).thenReturn(subjectDAO);
			when(subjectDAO.findAllOrderBy(anyString(), anyString(), anyInt(), anyInt())).thenReturn(new ArrayList<Subject>());
			
			when(mockSession.getAttribute(AttributeNames.USER_ROLE)).thenReturn(Role.CLIENT);
			
			testCommand.execute(request, response, HttpMethod.GET);
			
			verify(subjectDAO, times(1)).findAllOrderBy(anyString(), anyString(), anyInt(), anyInt());
			verify(request, times(5)).setAttribute(anyString(), any());
		}	
	}
}
