package ua.epam.pavelchuk.final_project.db;
/**
 * Fields holder (stores the name of the columns from the database).
 * 
 * @author O.Pavelchuk
 *
 */
public final class Fields {
	
	/**
	 * Private utility class constructor
	 */
	private Fields() {}
	
	public static final String ID ="id";
	
	public static final String USER_LOGIN ="login";
	public static final String USER_PASSWORD ="password";
	public static final String USER_PASSWORD_KEY ="password_key";
	public static final String USER_FIRST_NAME ="first_name";
	public static final String USER_LAST_NAME ="last_name";
	public static final String USER_EMAIL ="email";
	public static final String USER_LANGUAGE ="language";
	public static final String USER_ROLE ="role_id";
	
	public static final String USER_CONFIRM_PASSWORD ="confirm_password";
	
	public static final String USER_IS_BLOCKED ="isBlocked";
	
	public static final String SUBJECTS_NAME_RU ="subjects.name_ru";
	public static final String SUBJECTS_NAME_EN ="subjects.name_en";
	public static final String SUBJECT_ID="subject_id";
	
	public static final String QUESTION_TITLE_RU ="title_ru";
	public static final String QUESTION_TITLE_EN ="title_en";
	public static final String QUESTION_ID="question_id";
	public static final String QUESTION_TEST_ID="test_id";
	
	public static final String ANSWER_OPTION_RU ="option_ru";
	public static final String ANSWER_OPTION_EN ="option_en";
	public static final String ANSWER_CORRECT ="isCorrect";
	
	public static final String RESULT_MARK ="mark";
	public static final String RESULT_TEST_DATE ="test_date";
	
	public static final String TEST_NAME_RU="tests.name_ru";
	public static final String TEST_NAME_EN="tests.name_en";
	public static final String TEST_REQ_NUMB="numb_of_requests";
	public static final String TEST_TIME_MINUTES="time_minutes";
	
	public static final String DIFFICULTY_LEVEL_ID="difficulty_level_id";
	public static final String DIFFICULTY_NAME_EN="name_en";
	public static final String DIFFICULTY_NAME_RU="name_ru";
	
	public static final String RU = "ru";
	public static final String EN = "en";
}
