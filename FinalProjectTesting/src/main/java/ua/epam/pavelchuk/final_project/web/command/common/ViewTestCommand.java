package ua.epam.pavelchuk.final_project.web.command.common;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.Role;
import ua.epam.pavelchuk.final_project.db.dao.AnswerDAO;
import ua.epam.pavelchuk.final_project.db.dao.QuestionDAO;
import ua.epam.pavelchuk.final_project.db.dao.TestDAO;
import ua.epam.pavelchuk.final_project.db.entity.Answer;
import ua.epam.pavelchuk.final_project.db.entity.Question;
import ua.epam.pavelchuk.final_project.db.entity.Test;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.db.exception.Messages;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;

public class ViewTestCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7484275001450441997L;
	private static final Logger LOG = Logger.getLogger(ViewTestCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws IOException, ServletException, AppException {
		LOG.debug("Command starts");
		String result = null;

		if (method == HttpMethod.POST) {
			result = doPost(request);
		} else {
			result = doGet(request);
		}
		LOG.debug("Command finished");
		return result;
	}

	private String doGet(HttpServletRequest request) throws AppException {
		HttpSession session = request.getSession();
		int testId = 0;
		try {
			testId = Integer.parseInt(request.getParameter(ParameterNames.TEST_ID));
		} catch (NumberFormatException ex) {
			LOG.error(Messages.ERR_PARSING_PARAMETERS_LOG);
			throw new AppException(Messages.ERR_PARSING_PARAMETERS, ex);
		}
		
		Test test = null;
		QuestionDAO questionDAO = null;
		AnswerDAO answerDAO = null;
		TestDAO testDAO = null;
		try {
			questionDAO = QuestionDAO.getInstance();
			answerDAO = AnswerDAO.getInstance();
			testDAO = TestDAO.getInstance();
			test = testDAO.findTestById(testId);
			List<Question> questions = questionDAO.findQuestionsByTest(testId);
			List<List<Answer>> answers = new ArrayList<>();
			for (Question q : questions) {
				answers.add(answerDAO.findAnswersByQuestion(q.getId()));
			}
			request.setAttribute(AttributeNames.ANSWERS_LIST, answers);
			request.setAttribute(AttributeNames.QUESTIONS, questions);
		} catch (DBException e) {
			LOG.error(e.getMessage());
			throw new AppException("view_test_command.error.get", e);
		}

		request.setAttribute(AttributeNames.TEST, test);

		// to return to the subject page
		request.setAttribute(AttributeNames.SUBJECT_ID, request.getParameter(ParameterNames.SUBJECT_ID));

		// Admin page
		if (session.getAttribute(AttributeNames.USER_ROLE) == Role.ADMIN) {
			return Path.ADMIN_PAGE_TEST;
		}
		// Error page
		if (session.getAttribute(AttributeNames.TEST_END_TIME) == null) {
			throw new AppException("view_test_command.error.get.incorrect_way");
		}

		long currentTime = System.currentTimeMillis();
		long endTime = (long) session.getAttribute(AttributeNames.TEST_END_TIME);
		int minutes = (int) ((endTime - currentTime) / 60000);
		int seconds = (int) ((endTime - currentTime) / 1000) % 60;
		LOG.debug("Time left : " + minutes + ":" + seconds);

		request.setAttribute(AttributeNames.MINUTES, minutes);
		request.setAttribute(AttributeNames.SECONDS, seconds);
		// User page
		return Path.PAGE_TEST;
	}
	
	private String doPost(HttpServletRequest request) throws AppException {
		int testId = 0;
		try {
			testId = Integer.parseInt(request.getParameter(ParameterNames.TEST_ID));
		} catch (NumberFormatException ex) {
			LOG.error(Messages.ERR_PARSING_PARAMETERS_LOG);
			throw new AppException(Messages.ERR_PARSING_PARAMETERS, ex);
		}
		
		HttpSession session = request.getSession();
		// increase the number of requests of the current test
		TestDAO testDAO = null;
		try {
			testDAO = TestDAO.getInstance();
			
			testDAO.increaseRequests(testId);
			
			int timeForTest = testDAO.findTimeByTestId(testId);
			long startTime = System.currentTimeMillis();
			long endTime = startTime + timeForTest * 60000;
			
			session.setAttribute(AttributeNames.TEST_END_TIME, endTime);
			LOG.debug("Test start time: " +  DateFormat.getDateTimeInstance().format(startTime));
		} catch (DBException e) {
			LOG.error(e.getMessage());
			throw new AppException("view_test_command.error.post", e);
		}
		return Path.COMMAND_VIEW_TEST + "&testId=" + testId;
	}
}
