package ua.epam.pavelchuk.final_project.web.command;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import static org.mockito.Mockito.any;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;
import org.mockito.MockedStatic;

import ua.epam.pavelchuk.final_project.db.dao.UserDAO;
import ua.epam.pavelchuk.final_project.db.entity.User;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.validation.UserValidation;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.common.RegistrationCommand;

public class RegistrationCommandTest {

	@Test
	public void doPost() throws AppException, IOException, ServletException {
		UserDAO userDAO = mock(UserDAO.class);
		User user = mock(User.class);
		HttpServletRequest request = mock(HttpServletRequest.class);
		HttpServletResponse response = mock(HttpServletResponse.class);
		HttpSession mockSession = mock(HttpSession.class);
		when(request.getSession()).thenReturn(mockSession);
		when(request.getParameter(anyString())).thenReturn("1");
		try (MockedStatic<UserDAO> userDAOMockedStatic = mockStatic(UserDAO.class);
				MockedStatic<UserValidation> userValidationMockedStatic = mockStatic(UserValidation.class)) {
			when(UserDAO.getInstance()).thenReturn(userDAO);
			when(UserValidation.validationLogin(anyString())).thenReturn(true);
			when(UserValidation.validate(any(HttpServletRequest.class), anyString(), anyString(), anyString(), any(User.class),anyString(),anyString())).thenReturn(true);
			when(userDAO.insert(user)).thenReturn(true);
			
			new RegistrationCommand().execute(request, response, HttpMethod.POST);

			verify(userDAO, times(1)).insert(any(User.class));
		}
	}
}
