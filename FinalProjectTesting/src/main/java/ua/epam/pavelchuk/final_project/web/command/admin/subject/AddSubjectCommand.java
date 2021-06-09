package ua.epam.pavelchuk.final_project.web.command.admin.subject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.Path;
import ua.epam.pavelchuk.final_project.db.dao.SubjectDAO;
import ua.epam.pavelchuk.final_project.db.entity.Subject;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.db.validation.SubjectValidator;
import ua.epam.pavelchuk.final_project.web.HttpMethod;
import ua.epam.pavelchuk.final_project.web.command.Command;
import ua.epam.pavelchuk.final_project.web.command.ParameterNames;

public class AddSubjectCommand extends Command {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5625321633039361639L;

	private static final Logger LOG = Logger.getLogger(AddSubjectCommand.class);

	@Override
	public String execute(HttpServletRequest request, HttpServletResponse response, HttpMethod method)
			throws AppException {
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
		return Path.ADMIN_ADD_SUBJECT;
	}

	private String doPost(HttpServletRequest request) throws AppException {
		String nameRu = request.getParameter(ParameterNames.NAME_RU);
		String nameEn = request.getParameter(ParameterNames.NAME_EN);
		Subject subject = new Subject();

		if (!SubjectValidator.validate(request, nameRu, nameEn, subject)) {
			return Path.COMMAND_ADD_SUBJECT;
		}
	
		SubjectDAO subjectDAO = null;
		try {
			subjectDAO = SubjectDAO.getInstance();
			subject.setNameRu(nameRu);
			subject.setNameEn(nameEn);
			subjectDAO.insert(subject);
		} catch (DBException e) {
			LOG.error(e.getMessage());
			throw new AppException("add_subject_command.error", e);
		}
		return Path.COMMAND_VIEW_LIST_SUBJECTS;
	}
}
