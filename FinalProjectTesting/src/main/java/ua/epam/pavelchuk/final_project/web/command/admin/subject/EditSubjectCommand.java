package ua.epam.pavelchuk.final_project.web.command.admin.subject;

import java.io.IOException; 

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.dao.SubjectDAO;
import ua.epam.pavelchuk.final_project.db.entity.Subject;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.db.exception.Messages;
import ua.epam.pavelchuk.final_project.db.validation.SubjectValidation;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;

public class EditSubjectCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9120726057195419542L;
	private static final Logger LOG = Logger.getLogger(EditSubjectCommand.class);

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
		try {
			int subjectId = Integer.parseInt(request.getParameter(ParameterNames.SUBJECT_ID));
			
			SubjectDAO subjectDAO = SubjectDAO.getInstance();
			Subject subject = subjectDAO.findSubjectById(subjectId);
			LOG.trace(subject);
			request.setAttribute(AttributeNames.SUBJECT, subject);
			
		} catch (DBException e) {
			LOG.error(e.getMessage());
			throw new AppException(Messages.ERR_EDIT_SUBJECT_GET, e);
		} catch (NumberFormatException ex) {
			LOG.error(Messages.ERR_PARSING_PARAMETERS_LOG);
			throw new AppException(Messages.ERR_PARSING_PARAMETERS, ex);
		}
		return Path.ADMIN_EDIT_SUBJECT;
	}

	private String doPost(HttpServletRequest request) throws AppException {
		int subjectId = 0;
		try {
		subjectId = Integer.parseInt(request.getParameter(ParameterNames.SUBJECT_ID));
		} catch (NumberFormatException ex) {
			LOG.error(Messages.ERR_PARSING_PARAMETERS_LOG);
			throw new AppException(Messages.ERR_PARSING_PARAMETERS, ex);
		}

		SubjectDAO subjectDAO = null;
		try {
			subjectDAO = SubjectDAO.getInstance();

			Subject subject = subjectDAO.findSubjectById(subjectId);

			if (request.getParameter(ParameterNames.DELETE) != null) {
				subjectDAO.delete(subjectId);
			} else {
				String nameRu = request.getParameter(ParameterNames.NAME_RU);
				String nameEn = request.getParameter(ParameterNames.NAME_EN);

				if (!SubjectValidation.validate(request, nameRu, nameEn, subject)) {
					return Path.COMMAND_EDIT_SUBJECT + "&subjectId=" + subjectId;
				}

				subject.setNameRu(nameRu);
				subject.setNameEn(nameEn);
				subjectDAO.update(subject);
			}
		} catch (DBException e) {
			LOG.error(e.getMessage());
			throw new AppException(Messages.ERR_EDIT_SUBJECT_POST, e);
		}
		return Path.COMMAND_VIEW_LIST_SUBJECTS;
	}
}
