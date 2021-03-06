package ua.epam.pavelchuk.final_project.db.validation;

/**
 * Validator for multilingual input fields
 * 
 * @author O.Pavelchuk
 *
 */
public class LocalizationValidator {
	
	/**
	 * Protected utility class constructor
	 */
	protected LocalizationValidator() {}
	
	public static final String REGULAR_EXPRESSION_NAME_EN = "[A-Za-z0-9][A-Za-z0-9\\s,-\\.;?|=+()]+";
	public static final String REGULAR_EXPRESSION_NAME_RU = "[[\\p{IsCyrillic}]A-Za-z0-9][[\\p{IsCyrillic}]A-Za-z0-9\\s,-\\.;?|=+()]+";
	
	
	public static boolean validationNameRu(String nameRu) {
		return nameRu != null && !nameRu.isEmpty() && nameRu.matches(REGULAR_EXPRESSION_NAME_RU);
	}
	
	public static boolean validationNameEn(String nameEn) {
		return nameEn != null && !nameEn.isEmpty() && nameEn.matches(REGULAR_EXPRESSION_NAME_EN);
	}	
}
