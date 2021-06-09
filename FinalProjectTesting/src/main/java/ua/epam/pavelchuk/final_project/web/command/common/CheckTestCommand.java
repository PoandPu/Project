package ua.epam.pavelchuk.final_project.web.command.common;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.dao.AnswerDAO;
import ua.epam.pavelchuk.final_project.db.dao.QuestionDAO;
import ua.epam.pavelchuk.final_project.db.dao.ResultDAO;
import ua.epam.pavelchuk.final_project.db.entity.Answer;
import ua.epam.pavelchuk.final_project.db.entity.Question;
import ua.epam.pavelchuk.final_project.db.entity.Result;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.db.exception.Messages;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;

public class CheckTestCommand extends Command {

	private static final long serialVersionUID = -8541535961380321564L;
	private static final Logger LOG = Logger.getLogger(CheckTestCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws AppException {
		LOG.debug("Command starts");
		String result = null;

		result = doPost(request);

		LOG.debug("Command finished");
		return result;
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
		long endTime = session.getAttribute(AttributeNames.TEST_END_TIME) != null
				? (long) session.getAttribute(AttributeNames.TEST_END_TIME)
				: 0;
		int currentUserId = (int) session.getAttribute(AttributeNames.ID);
		long currentTime = System.currentTimeMillis();

		ResultDAO resultDAO = null;
		try {
			resultDAO = ResultDAO.getInstance();
			BigDecimal mark = null;
			if (currentTime > endTime) {
				mark = BigDecimal.valueOf(0);
				Result testResult = new Result(mark, currentUserId, testId);
				resultDAO.insert(testResult);
				LOG.warn("Time for test [id: " + testId + " ] was over!");
				throw new AppException("check_test_command.error.time_is_over");
			}

			mark = BigDecimal.valueOf(checkTest(request, testId));
			LOG.debug("Mark : " + mark);
			Result testResult = new Result(mark, currentUserId, testId);
			resultDAO.insert(testResult);
		} catch (DBException ex) {
			LOG.error(ex.getMessage());
			throw new AppException("check_test_error", ex);
		} finally {
			session.removeAttribute(AttributeNames.TEST_END_TIME);
			session.removeAttribute(AttributeNames.TEST);
			session.removeAttribute(AttributeNames.SUBJECT);
		}
		return Path.COMMAND_VIEW_PROFILE + "&userId=" + currentUserId;
	}

	/**
	 * Receives all IDs of the user's replies
	 * 
	 * @param request   HttpServletRequest
	 * @param parameter pattern of search
	 * 
	 * @return List of IDs
	 */
	private List<Integer> getUserAnswers(HttpServletRequest request, String parameter) {
		List<Integer> userAnswers = new ArrayList<>();
		Enumeration<String> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String parameterName = enumeration.nextElement();
			if (parameterName.matches(parameter + "\\d+")) {
				parameterName = parameterName.replaceAll("\\D", "");
				userAnswers.add(Integer.parseInt(parameterName));
			}
		}
		return userAnswers;
	}

	/**
	 * Finds the number of correct answers in %
	 * 
	 * @param request HttpServletRequest
	 * @param testId  ID of the test
	 * 
	 * @return mark in %
	 */
	private double checkTest(HttpServletRequest request, int testId) throws DBException {
		QuestionDAO questionDAO = QuestionDAO.getInstance();
		AnswerDAO answerDAO = AnswerDAO.getInstance();

		List<Question> questions = questionDAO.findQuestionsByTest(testId);
		List<Integer> userAnswers = getUserAnswers(request, "answerNumb");
		double amountOfCorrectAnswers = 0;

		for (Question q : questions) {
			List<Answer> answers = answerDAO.findAnswersByQuestion(q.getId());
			if (checkIsCorrect(answers, userAnswers)) {
				amountOfCorrectAnswers++;
			}
		}
		LOG.debug("Questions: " + questions.size() + " / Correct answers : " + amountOfCorrectAnswers);
		double mark = 0;
		if (!questions.isEmpty()) {
			mark = amountOfCorrectAnswers / (double) questions.size() * 100;
		}
		return mark;
	}

	/**
	 * Checking the discrepancy between user responses and responses from the
	 * database
	 * 
	 * @param answers     list of answers from DB
	 * @param userAnswers list of users answers
	 * 
	 * @return true if the answer to the question is correct
	 */
	private boolean checkIsCorrect(List<Answer> answers, List<Integer> userAnswers) {
		for (Answer a : answers) {
			if (a.getIsCorrect() && userAnswers.indexOf(a.getId()) == -1
					|| !a.getIsCorrect() && userAnswers.indexOf(a.getId()) != -1) {
				return false;
			}
		}
		return true;
	}
}
