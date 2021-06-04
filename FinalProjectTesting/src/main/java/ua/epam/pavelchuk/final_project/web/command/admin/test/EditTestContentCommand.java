package ua.epam.pavelchuk.final_project.web.command.admin.test;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.dao.AnswerDAO;
import ua.epam.pavelchuk.final_project.db.dao.QuestionDAO;
import ua.epam.pavelchuk.final_project.db.entity.Answer;
import ua.epam.pavelchuk.final_project.db.entity.Question;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.db.exception.Messages;
import ua.epam.pavelchuk.final_project.db.validation.LocalizationValidation;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;

public class EditTestContentCommand extends Command {

	/**
	 * standard constructor
	 * 
	 */
	public EditTestContentCommand(){
		try {
			questionDAO = QuestionDAO.getInstance();
			answerDAO = AnswerDAO.getInstance();
		} catch (DBException e) {
			LOG.error("Cannot obtain a connection!");
		}
	}
	
	/**
	 * Constructor for Mockito tests
	 * 
	 * @param QuestionDAO
	 * @param AnswerDAO
	 * 
	 */
	public EditTestContentCommand(QuestionDAO questionDAO, AnswerDAO answerDAO) {
		this.questionDAO = questionDAO;
		this.answerDAO = answerDAO;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 2684928586891220013L;
	private static final Logger LOG = Logger.getLogger(EditTestContentCommand.class);
	private QuestionDAO questionDAO;
	private AnswerDAO answerDAO;

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
		int testId = 0;
		int questionId = 0;
		int subjectId = 0;
		try {
			testId = Integer.parseInt(request.getParameter(ParameterNames.TEST_ID));
			questionId = Integer.parseInt(request.getParameter(ParameterNames.QUESTION_ID));
			subjectId = Integer.parseInt(request.getParameter(ParameterNames.SUBJECT_ID));
		} catch (NumberFormatException ex) {
			LOG.error(Messages.ERR_PARSING_PARAMETERS_LOG);
			throw new AppException(Messages.ERR_PARSING_PARAMETERS, ex);
		}
		try {
			Question question = questionDAO.findQuestionById(questionId);
			List<Answer> answers = answerDAO.findAnswersByQuestion(questionId);

			request.setAttribute(AttributeNames.TEST_ID, testId);
			request.setAttribute(AttributeNames.ANSWERS, answers);
			request.setAttribute(AttributeNames.QUESTION, question);
			request.setAttribute(AttributeNames.SUBJECT_ID, subjectId);
		} catch (DBException ex) {
			LOG.error(ex.getMessage());
			throw new AppException(Messages.ERR_EDIT_TEST_CONTENT_GET, ex);
		}
		return Path.ADMIN_EDIT_TEST_CONTENT;
	}

	private String doPost(HttpServletRequest request) throws AppException {
		int testId = 0;
		int subjectId = 0;
		int questionId = 0;

		try {
			testId = Integer.parseInt(request.getParameter(ParameterNames.TEST_ID));
			subjectId = Integer.parseInt(request.getParameter(ParameterNames.SUBJECT_ID));
			questionId = Integer.parseInt(request.getParameter(ParameterNames.QUESTION_ID));
		} catch (NumberFormatException ex) {
			LOG.error(Messages.ERR_PARSING_PARAMETERS_LOG);
			throw new AppException(Messages.ERR_PARSING_PARAMETERS, ex);
		}

		String pathEdit = Path.COMMAND_EDIT_TEST_CONTENT + "&subjectId=" + subjectId + "&testId=" + testId
				+ "&questionId=" + questionId;

		try {
			Question question = questionDAO.findQuestionById(questionId);
			String titleRu = request.getParameter(ParameterNames.TITLE_RU);
			String titleEn = request.getParameter(ParameterNames.TITLE_EN);

			// question validation
			if (!LocalizationValidation.validationNameRu(titleRu)) {
				request.getSession().setAttribute(AttributeNames.TEST_CONTENT_ERROR_MESSAGE,
						"admin.edit_test_content_jsp.error.question_title_ru");
				return pathEdit;
			}
			if (!LocalizationValidation.validationNameEn(titleEn)) {
				request.getSession().setAttribute(AttributeNames.TEST_CONTENT_ERROR_MESSAGE,
						"admin.edit_test_content_jsp.error.question_title_en");
				return pathEdit;
			}

			question.setNameRu(titleRu);
			question.setNameEn(titleEn);
			questionDAO.update(question);

			List<Answer> answers = answerDAO.findAnswersByQuestion(question.getId());
			LOG.trace("answers list size = " + answers.size());
			for (Answer a : answers) {
				String optionRu = request.getParameter(ParameterNames.OPTION_RU + a.getId());
				String optionEn = request.getParameter(ParameterNames.OPTION_EN + a.getId());

				// answer validation
				if (!LocalizationValidation.validationNameRu(optionRu)) {
					request.getSession().setAttribute(AttributeNames.TEST_CONTENT_ERROR_MESSAGE,
							"admin.edit_test_content_jsp.error.answer_option_ru");
					return pathEdit;
				}
				if (!LocalizationValidation.validationNameEn(optionEn)) {
					request.getSession().setAttribute(AttributeNames.TEST_CONTENT_ERROR_MESSAGE,
							"admin.edit_test_content_jsp.error.answer_option_en");
					return pathEdit;
				}

				a.setNameRu(optionRu);
				a.setNameEn(optionEn);

				if (request.getParameter(ParameterNames.IS_CORRECT + a.getId()) != null) {
					a.setIsCorrect(true);
					LOG.trace("Answer :" + a.getNameEn() + " was marked as correct");
				} else {
					a.setIsCorrect(false);
				}
				answerDAO.update(a);
			}
		} catch (DBException e) {
			LOG.error(e.getMessage());
			throw new AppException(Messages.ERR_EDIT_TEST_CONTENT_POST, e);
		}
		return pathEdit;
	}
}
