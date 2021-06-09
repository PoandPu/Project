package ua.epam.pavelchuk.final_project.web.command.admin.test;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.dao.AnswerDAO;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.db.exception.Messages;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;

/**
 * Deletes an answer
 * 
 * @author O.Pavelchuk
 */
public class DeleteAnswerCommand extends Command{

	private static final long serialVersionUID = 2684928586891220013L;
	private static final Logger LOG = Logger.getLogger(DeleteAnswerCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws AppException {
		LOG.debug("Command starts");

		String result = doPost(request);

		LOG.debug("Command finished");
		return result;
	}

	private String doPost(HttpServletRequest request) throws AppException {
		int testId = 0;
		int subjectId = 0;
		int questionId = 0;
		int answerId = 0;
		try {
		testId = Integer.parseInt(request.getParameter(ParameterNames.TEST_ID));
		subjectId = Integer.parseInt(request.getParameter(ParameterNames.SUBJECT_ID));
		questionId = Integer.parseInt(request.getParameter(ParameterNames.QUESTION_ID));
		answerId = getIdByParameter(request, ParameterNames.DELETE);
		}catch (NumberFormatException ex) {
			LOG.error(Messages.ERR_PARSING_PARAMETERS_LOG);
			throw new AppException(Messages.ERR_PARSING_PARAMETERS, ex);
		}
		
		try {
			AnswerDAO answerDAO = AnswerDAO.getInstance();
			answerDAO.delete(answerId);
		} catch (DBException e) {
			LOG.error(e.getMessage());
			throw new AppException("delete_answer_command.error.post", e);
		}
		return Path.COMMAND_EDIT_TEST_CONTENT + "&subjectId=" + subjectId + "&testId=" + testId + "&questionId="
				+ questionId;
	}
	
	private int getIdByParameter(HttpServletRequest request, String parameter) {
		int userAnswersId = -1;
		Enumeration<String> enumeration = request.getParameterNames();
		while (enumeration.hasMoreElements()) {
			String parameterName = enumeration.nextElement();
			if (parameterName.matches(parameter + "\\d+")) {
				parameterName = parameterName.replaceAll("\\D", "");
				userAnswersId = Integer.parseInt(parameterName);
			}
		}
		return userAnswersId;
	}
}
