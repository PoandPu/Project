package ua.epam.pavelchuk.final_project.db.validation;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.db.dao.SubjectDAO;
import ua.epam.pavelchuk.final_project.db.entity.Subject;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.db.exception.Messages;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;

/**
 * Validator for the fields associated with the Subject
 * 
 * @author O.Pavelchuk
 *
 */
public class SubjectValidator extends LocalizationValidator {

	/**
	 * Private utility class constructor
	 */
	private SubjectValidator() {}

	private static final Logger LOG = Logger.getLogger(SubjectValidator.class);

	public static boolean checkUniquenessName(String name) throws AppException {
		boolean result = false;
		try {
			SubjectDAO subjectDAO = SubjectDAO.getInstance();
			result = !subjectDAO.hasName(name);
		} catch (DBException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new AppException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
		}
		return result;
	}

	public static boolean validate(HttpServletRequest request, String nameRu, String nameEn, Subject subject)
			throws AppException {
		if (!validationNameRu(nameRu) || nameRu.length() > 45) {
			request.getSession().setAttribute(AttributeNames.SUBJECT_ERROR_MESSAGE, "subject_validation.error.name_ru");
			return false;
		}
		if (!validationNameEn(nameEn) || nameEn.length() > 45) {
			request.getSession().setAttribute(AttributeNames.SUBJECT_ERROR_MESSAGE, "subject_validation.error.name_en");
			return false;
		}
		if (subject.getNameRu() != null && subject.getNameEn() != null) {
			if (!subject.getNameRu().equals(nameRu) && !checkUniquenessName(nameRu)) {
				request.getSession().setAttribute(AttributeNames.SUBJECT_ERROR_MESSAGE,
						"subject_validation.error.name_ru_not_unique");
				return false;
			}
			if (!subject.getNameEn().equals(nameEn) && !checkUniquenessName(nameEn)) {
				request.getSession().setAttribute(AttributeNames.SUBJECT_ERROR_MESSAGE,
						"subject_validation.error.name_en_not_unique");
				return false;
			}
		} else {
			if (!checkUniquenessName(nameRu)) {
				request.getSession().setAttribute(AttributeNames.SUBJECT_ERROR_MESSAGE,
						"subject_validation.error.name_ru_not_unique");
				return false;
			}
			if (!checkUniquenessName(nameEn)) {
				request.getSession().setAttribute(AttributeNames.SUBJECT_ERROR_MESSAGE,
						"subject_validation.error.name_en_not_unique");
				return false;
			}
		}
		return true;
	}
}
