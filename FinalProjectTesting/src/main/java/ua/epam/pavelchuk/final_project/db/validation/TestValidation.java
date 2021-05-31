package ua.epam.pavelchuk.final_project.db.validation;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.db.dao.TestDAO;
import ua.epam.pavelchuk.final_project.db.entity.Test;
import ua.epam.pavelchuk.final_project.db.exception.AppException;
import ua.epam.pavelchuk.final_project.db.exception.DBException;
import ua.epam.pavelchuk.final_project.db.exception.Messages;
import ua.epam.pavelchuk.final_project.web.command.AttributeNames;

public abstract class TestValidation extends LocalizationValidation {
	
	private TestValidation() {
	}

	private static final Logger LOG = Logger.getLogger(TestValidation.class);
	public static final String REGULAR_EXPRESSION_NUMBER = "\\d+";

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

	public static boolean isNumber(String number) {
		return !number.isEmpty() && number.matches(REGULAR_EXPRESSION_NUMBER);
	}

	public static boolean validate(HttpServletRequest request, String nameRu, String nameEn, String time, Test test)
			throws AppException {
		if (!validationNameRu(nameRu)) {
			request.getSession().setAttribute(AttributeNames.TEST_ERROR_MESSAGE, "test_validation.error.name_ru");
			return false;
		}
		if (!validationNameEn(nameEn)) {
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

		if (!isNumber(time)) {
			request.getSession().setAttribute(AttributeNames.TEST_ERROR_MESSAGE, "test_validation.error.time_not_number");
			return false;
		}
		if (Integer.parseInt(time) <= 0) {
			request.getSession().setAttribute(AttributeNames.TEST_ERROR_MESSAGE, "test_validation.error.time_invalid");
			return false;
		} else {
			return true;
		}
	}

}
