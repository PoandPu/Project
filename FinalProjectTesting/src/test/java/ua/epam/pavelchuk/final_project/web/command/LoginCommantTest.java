package ua.epam.pavelchuk.final_project.web.command;

import static org.mockito.Mockito.mock; 
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.any;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.MockedStatic;

import ua.epam.pavelchuk.final_project.db.dao.UserDAO;
import ua.epam.pavelchuk.final_project.db.entity.User;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.captcha.VerifyUtils;
import ua.epam.pavelchuk.final_project.web.command.common.LoginCommand;
import ua.epam.pavelchuk.final_project.web.password_encryption.PasswordUtils;

public class LoginCommantTest {
	
	private static UserDAO userDAO;
	private static HttpServletRequest request;
	private static HttpServletResponse response;
	private static HttpSession session;
	
	@BeforeClass
	public static void init() {
		userDAO = mock(UserDAO.class);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		when(request.getSession()).thenReturn(session);
	}
	
	
	@Test
	public void doPost() throws IOException, ServletException, AppException, DBException {
		User user = mock(User.class);
		when(user.getPassword()).thenReturn("test");
		when(user.getPasswordKey()).thenReturn("test");
		try(MockedStatic<UserDAO> userDAOMockedStatic = mockStatic(UserDAO.class);
				MockedStatic<VerifyUtils> verifyUtilsMockedStatic = mockStatic(VerifyUtils.class);
				MockedStatic<PasswordUtils> passwordUtilsMockedStatic = mockStatic(PasswordUtils.class);){
			when(UserDAO.getInstance()).thenReturn(userDAO);
			when(userDAO.findByLogin(anyString())).thenReturn(user);
			when(request.getParameter(anyString())).thenReturn("test");	
			when(PasswordUtils.generateSecurePassword(anyString(), anyString())).thenReturn("test");
			when(user.getIsBlocked()).thenReturn(false);
			when(VerifyUtils.verify(anyString())).thenReturn(true);	
			
			LoginCommand testCommand = new LoginCommand();
			testCommand.execute(request, response, HttpMethod.POST);
			
			verify(userDAO, times(1)).findByLogin(anyString());
			verify(session, times(3)).setAttribute(anyString(), any());
		}	
	}
}
