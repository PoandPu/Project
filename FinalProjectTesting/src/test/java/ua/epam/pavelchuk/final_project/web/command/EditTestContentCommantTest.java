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
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import ua.epam.pavelchuk.final_project.db.dao.AnswerDAO;
import ua.epam.pavelchuk.final_project.db.dao.QuestionDAO;
import ua.epam.pavelchuk.final_project.db.entity.Answer;
import ua.epam.pavelchuk.final_project.db.entity.Question;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.validation.LocalizationValidation;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.admin.test.EditTestContentCommand;

public class EditTestContentCommantTest {

	private AnswerDAO answerDAO;
	private QuestionDAO questionDAO;
	private HttpServletRequest request;
	private HttpServletResponse response;
	private HttpSession session;
	private EditTestContentCommand testCommand;

	@BeforeEach
	public void init() {
		answerDAO = mock(AnswerDAO.class);
		questionDAO = mock(QuestionDAO.class);
		request = Mockito.mock(HttpServletRequest.class);
		response = Mockito.mock(HttpServletResponse.class);
		session = Mockito.mock(HttpSession.class);
		when(request.getSession()).thenReturn(session);
		testCommand = new EditTestContentCommand();
	}

	@Test
	void doGet() throws IOException, ServletException, AppException {
		try (MockedStatic<AnswerDAO> answerDAOMockedStatic = mockStatic(AnswerDAO.class);
				MockedStatic<QuestionDAO> questionDAOMockedStatic = mockStatic(QuestionDAO.class)) {
			when(AnswerDAO.getInstance()).thenReturn(answerDAO);
			when(QuestionDAO.getInstance()).thenReturn(questionDAO);
			when(request.getParameter(anyString())).thenReturn("1");

			//testCommand.execute(request, response, HttpMethod.GET);
			testCommand.execute(request, response, HttpMethod.GET);

			verify(questionDAO, times(1)).findQuestionById(anyInt());
			verify(answerDAO, times(1)).findAnswersByQuestion(anyInt());
			verify(request, times(4)).setAttribute(anyString(), any());
		}
	}

	@Test
	void doPost() throws IOException, ServletException, AppException {
		Question question = mock(Question.class);
		Answer answer = mock(Answer.class);
		try (MockedStatic<AnswerDAO> answerDAOMockedStatic = mockStatic(AnswerDAO.class);
				MockedStatic<QuestionDAO> questionDAOMockedStatic = mockStatic(QuestionDAO.class);
				MockedStatic<LocalizationValidation> LocalizationValidationMockedStatic = mockStatic(LocalizationValidation.class)) {
			when(AnswerDAO.getInstance()).thenReturn(answerDAO);
			when(QuestionDAO.getInstance()).thenReturn(questionDAO);
			when(questionDAO.findQuestionById(anyInt())).thenReturn(question);
			List<Answer> answers = new ArrayList<>();
			answers.add(answer);
			answers.add(answer);
			when(answerDAO.findAnswersByQuestion(anyInt())).thenReturn(answers);
			when(LocalizationValidation.validationNameEn(anyString())).thenReturn(true);
			when(LocalizationValidation.validationNameRu(anyString())).thenReturn(true);
			when(request.getParameter(anyString())).thenReturn("1");
			
			testCommand.execute(request, response, HttpMethod.POST);	
			
			verify(questionDAO, times(1)).findQuestionById(anyInt());
			verify(answerDAO, times(1)).findAnswersByQuestion(anyInt());
			verify(answerDAO, times(answers.size())).update(answer);
		}
	}
}