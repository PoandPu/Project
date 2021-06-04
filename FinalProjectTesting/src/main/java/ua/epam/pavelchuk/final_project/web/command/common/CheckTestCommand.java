package ua.epam.pavelchuk.final_project.web.command.common;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
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

	/**
	 * 
	 */
	private static final long serialVersionUID = -8541535961380321564L;
	private static final Logger LOG = Logger.getLogger(CheckTestCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws IOException, ServletException, AppException {

		LOG.debug("Command starts");
		String result = null;

		if (method == HttpMethod.POST) {
			result = doPost(request);
		} else {
			result = doGet();
		}
		LOG.debug("Command finished");
		return result;
	}

	private String doGet() {
		return Path.PAGE_LIST_ALL_SUBJECTS;
	}

	private String doPost(HttpServletRequest request) throws AppException {
		HttpSession session = request.getSession();
		int testId = Integer.parseInt(request.getParameter(ParameterNames.TEST_ID));
		long endTime = (long) session.getAttribute(AttributeNames.TEST_END_TIME);
		int currentUserId = (int) session.getAttribute(AttributeNames.ID);
		long currentTime = System.currentTimeMillis();

		ResultDAO resultDAO = null;
		try {
			resultDAO = ResultDAO.getInstance();
			if (currentTime > endTime) {
				BigDecimal mark = BigDecimal.valueOf(0);
				Result testResult = new Result(mark, currentUserId, testId);
				resultDAO.insert(testResult);
				throw new AppException("Time is over! Your result is 0.");
			}

			double mark = checkTest(request, testId);

			BigDecimal m = BigDecimal.valueOf(mark);
			LOG.debug("Mark : " + m);
			Result testResult = new Result(m, currentUserId, testId);
			resultDAO.insert(testResult);
		} catch (DBException ex) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION);
			throw new AppException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, ex);
		} finally {
			session.removeAttribute(AttributeNames.TEST_END_TIME);
		}

		return Path.COMMAND_VIEW_LIST_SUBJECTS;
	}

	// This method was created to get all Answers_id marked by user
	private List<Integer> getUserAnswersId(HttpServletRequest request, String parameter) {
		List<Integer> userAnswersId = new ArrayList<>();
		Enumeration<String> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String parameterName = enumeration.nextElement();
			if (parameterName.matches(parameter + "\\d+")) {
				parameterName = parameterName.replaceAll("\\D", "");
				userAnswersId.add(Integer.parseInt(parameterName));
			}
		}
		return userAnswersId;
	}

	private double checkTest(HttpServletRequest request, int testId) throws DBException {
		QuestionDAO questionDAO = QuestionDAO.getInstance();
		AnswerDAO answerDAO = AnswerDAO.getInstance();

		List<Question> questions = questionDAO.findQuestionsByTest(testId);
		List<Integer> userAnswersId = getUserAnswersId(request, "answerNumb");
		double amountOfCorrectAnswers = 0;

//		one: for (Question q : questions) {
//			List<Answer> answers = answerDAO.findAnswersByQuestion(q.getId());
//			for (Answer a : answers) {
//				// answer is correct, but the user didn't mark it OR answer isn't correct and
//				// user marked it
//				// go to the other question!
//				if (a.getIsCorrect() && userAnswersId.indexOf(a.getId()) == -1
//						|| !a.getIsCorrect() && userAnswersId.indexOf(a.getId()) != -1) {
//					continue one;
//				}
//			}
//			amountOfCorrectAnswers++;
//		}
		for (Question q : questions) {
			boolean isCorrect = true;
			List<Answer> answers = answerDAO.findAnswersByQuestion(q.getId());
			for (Answer a : answers) {
				// answer is correct, but the user didn't mark it OR answer isn't correct and
				// user marked it
				// go to the other question!
				if (a.getIsCorrect() && userAnswersId.indexOf(a.getId()) == -1
						|| !a.getIsCorrect() && userAnswersId.indexOf(a.getId()) != -1) {
					isCorrect = false;
				}
			}
			if (isCorrect) {
				amountOfCorrectAnswers++;
			}
		}
		LOG.debug("Questions amount : " + questions.size());
		LOG.debug("Amount of correct answers : " + amountOfCorrectAnswers);
		double mark = 0;
		if (!questions.isEmpty()) {
			mark = amountOfCorrectAnswers / (double) questions.size() * 100;
		}
		return mark;
	}
}
