package ua.epam.pavelchuk.final_project.web.command;


import static org.mockito.Mockito.mock;   
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.Test;

import ua.epam.pavelchuk.final_project.db.dao.AnswerDAO;
import ua.epam.pavelchuk.final_project.db.dao.QuestionDAO;
import ua.epam.pavelchuk.final_project.db.entity.Answer;
import ua.epam.pavelchuk.final_project.db.entity.Question;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.admin.test.EditTestContentCommand;

public class EditTestContentCommantTest {
	
	private static AnswerDAO answerDAO;
	private static QuestionDAO questionDAO;
	private static HttpServletRequest request;
	private static HttpServletResponse response;
	private static HttpSession session;
	private static EditTestContentCommand testCommand;
	
//	@BeforeAll
//	static void init() {
//		answerDAO = mock(AnswerDAO.class);
//		questionDAO = mock(QuestionDAO.class);
//		request = Mockito.mock(HttpServletRequest.class);
//		response = Mockito.mock(HttpServletResponse.class);
//		session = Mockito.mock(HttpSession.class);
//		when(request.getSession()).thenReturn(session);
//	}
	
	
	@Test
	public void doGet() throws IOException, ServletException, AppException {
		
		answerDAO = mock(AnswerDAO.class);
		questionDAO = mock(QuestionDAO.class);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		
		testCommand = new EditTestContentCommand(questionDAO, answerDAO);
		
		when(request.getSession()).thenReturn(session);
		
		when(request.getParameter(ParameterNames.TEST_ID)).thenReturn("1");
		when(request.getParameter(ParameterNames.QUESTION_ID)).thenReturn("1");
		when(request.getParameter(ParameterNames.SUBJECT_ID)).thenReturn("1");
		when(questionDAO.findQuestionById(1)).thenReturn(new Question());
		when(answerDAO.findAnswersByQuestion(1)).thenReturn(new ArrayList<>());
		
		testCommand.execute(request, response, HttpMethod.GET);
	}
	
	@Test
	public void doPost() throws IOException, ServletException, AppException {
		answerDAO = mock(AnswerDAO.class);
		questionDAO = mock(QuestionDAO.class);
		request = mock(HttpServletRequest.class);
		response = mock(HttpServletResponse.class);
		session = mock(HttpSession.class);
		testCommand = new EditTestContentCommand(questionDAO, answerDAO);
		when(request.getSession()).thenReturn(session);
		
		when(request.getParameter(ParameterNames.TEST_ID)).thenReturn("1");
		when(request.getParameter(ParameterNames.QUESTION_ID)).thenReturn("1");
		when(request.getParameter(ParameterNames.SUBJECT_ID)).thenReturn("1");
		
		when(questionDAO.findQuestionById(1)).thenReturn(new Question());
		
		when(request.getParameter(ParameterNames.TITLE_RU)).thenReturn("что-то");
		when(request.getParameter(ParameterNames.TITLE_EN)).thenReturn("something");
		List<Answer> answers = new ArrayList<>();
		Answer a = new Answer();
		a.setId(1);
		a.setNameEn("name");
		a.setNameRu("имя");
		a.setQuestionId(1);
		answers.add(a);
		answers.add(a);
		when(answerDAO.findAnswersByQuestion(0)).thenReturn(answers);
		
		when(request.getParameter(ParameterNames.OPTION_RU + 1)).thenReturn("что-то");
		when(request.getParameter(ParameterNames.OPTION_EN + 1)).thenReturn("something");
		when(request.getParameter(ParameterNames.IS_CORRECT + 1)).thenReturn("correct1");
		
		testCommand.execute(request, response, HttpMethod.POST);
		
	}
	
	
}