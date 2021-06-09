package ua.epam.pavelchuk.final_project.db.validation;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.db.dao.TestDAO;
import ua.epam.pavelchuk.final_project.db.entity.Test;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.db.exception.Messages;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;

/**
 * Validator for test-related  fields 
 * 
 * @author O.Pavelchuk
 *
 */
public class TestValidator extends LocalizationValidator {
	
	/**
	 * Private utility class constructor
	 */
	private TestValidator() {}

	private static final Logger LOG = Logger.getLogger(TestValidator.class);

	public static boolean checkUniquenessName(String name) throws AppException {
		boolean result = false;
		try {
			TestDAO testDAO = TestDAO.getInstance();
			result = !testDAO.hasName(name);
		} catch (DBException e) {
			LOG.error(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
			throw new AppException(Messages.ERR_CANNOT_OBTAIN_CONNECTION, e);
		}
		return result;
	}

	public static boolean validate(HttpServletRequest request, String nameRu, String nameEn, int time, Test test)
			throws AppException {
		if (!validationNameRu(nameRu) || nameRu.length() > 128) {
			request.getSession().setAttribute(AttributeNames.TEST_ERROR_MESSAGE, "test_validation.error.name_ru");
			return false;
		}
		if (!validationNameEn(nameEn) || nameEn.length() > 128) {
			request.getSession().setAttribute(AttributeNames.TEST_ERROR_MESSAGE, "test_validation.error.name_en");
			return false;
		}
		if (test.getNameRu() != null && test.getNameEn() != null) {
			if (!test.getNameRu().equals(nameRu) && !checkUniquenessName(nameRu)) {
				request.getSession().setAttribute(AttributeNames.TEST_ERROR_MESSAGE, "test_validation.error.name_ru_not_unique");
				return false;
			}
			if (!test.getNameEn().equals(nameEn) && !checkUniquenessName(nameEn)) {
				request.getSession().setAttribute(AttributeNames.TEST_ERROR_MESSAGE, "test_validation.error.name_en_not_unique");
				return false;
			}
		} else {
			if (!checkUniquenessName(nameRu)) {
				request.getSession().setAttribute(AttributeNames.TEST_ERROR_MESSAGE, "test_validation.error.name_ru_not_unique");
				return false;
			}
			if (!checkUniquenessName(nameEn)) {
				request.getSession().setAttribute(AttributeNames.TEST_ERROR_MESSAGE, "test_validation.error.name_en_not_unique");
				return false;
			}
		}
		if (time <= 0) {
			request.getSession().setAttribute(AttributeNames.TEST_ERROR_MESSAGE, "test_validation.error.time_invalid");
			return false;
		} else {
			return true;
		}
	}
}
