package ua.epam.pavelchuk.final_project;

/**
 * Path holder (jsp pages, controller commands).
 * 
 * @author 
 * 
 */
public final class Path {
	
	/**
	 * Private utility class constructor
	 */
	private Path(){}
	
	// pages
	public static final String PAGE_LOGIN = "/login.jsp";
	public static final String PAGE_ERROR = "/WEB-INF/jsp/error_page.jsp";
	public static final String PAGE_FORGOT_PASSWORD = "/WEB-INF/jsp/forgot_password.jsp";
	public static final String PAGE_CLIENT_REGISRTATION = "/registration.jsp";
	public static final String PAGE_ADMIN = "/WEB-INF/jsp/admin/profile/view.jsp";
	public static final String PAGE_CLIENT = "/WEB-INF/jsp/client/profile/view.jsp";
	public static final String PAGE_LIST_ALL_SUBJECTS ="/WEB-INF/jsp/user/subjects_list.jsp";
	public static final String PAGE_LIST_TESTS ="/WEB-INF/jsp/user/tests_list.jsp";
	public static final String PAGE_TEST ="/WEB-INF/jsp/user/test.jsp";
	public static final String PAGE_PROFILE ="/WEB-INF/jsp/user/profile.jsp";
	public static final String PAGE_USER_SETTINGS ="/WEB-INF/jsp/userSettings.jsp";
	
	public static final String ADMIN_PAGE_LIST_ALL_SUBJECTS ="/WEB-INF/jsp/admin/subjects/subjects_list.jsp";
	public static final String ADMIN_EDIT_SUBJECT ="/WEB-INF/jsp/admin/subjects/edit_subject.jsp";
	public static final String ADMIN_ADD_SUBJECT ="/WEB-INF/jsp/admin/subjects/add_subject.jsp";
	public static final String ADMIN_PAGE_LIST_TESTS ="/WEB-INF/jsp/admin/tests/tests_list.jsp";
	public static final String ADMIN_EDIT_TEST ="/WEB-INF/jsp/admin/tests/edit_test.jsp";
	public static final String ADMIN_EDIT_TEST_CONTENT ="/WEB-INF/jsp/admin/tests/edit_test_content.jsp";
	public static final String ADMIN_ADD_TEST ="/WEB-INF/jsp/admin/tests/add_test.jsp";
	public static final String ADMIN_PAGE_TEST ="/WEB-INF/jsp/admin/tests/view_test.jsp";
	public static final String ADMIN_PAGE_LIST_USERS ="/WEB-INF/jsp/admin/users/users_list.jsp";
	public static final String ADMIN_EDIT_USER ="/WEB-INF/jsp/admin/users/edit_user.jsp";
	
	// commands
	
	public static final String COMMAND_VIEW_LOGIN_PAGE = "controller?command=login";
	public static final String COMMAND_VIEW_REGISTRATION_PAGE = "controller?command=registration";
	public static final String COMMAND_LOGOUT = "controller?command=logout";
	public static final String COMMAND_VIEW_ERROR_PAGE ="controller?command=viewErrorPage";
	
	public static final String COMMAND_VIEW_USERS_LIST = "controller?command=viewUsersList";
	public static final String COMMAND_EDIT_USER = "controller?command=editUser";
	public static final String COMMAND_SETTINGS_USER = "controller?command=userSettings";
	
	public static final String COMMAND_VIEW_LIST_SUBJECTS = "controller?command=viewAllSubjects";
	public static final String COMMAND_VIEW_TESTS_LIST = "controller?command=viewTestsList";
	public static final String COMMAND_ADD_SUBJECT = "controller?command=addSubject";
	public static final String COMMAND_EDIT_SUBJECT = "controller?command=editSubject";
	
	public static final String COMMAND_VIEW_TEST = "controller?command=viewTest";
	public static final String COMMAND_ADD_TEST = "controller?command=addTest";
	public static final String COMMAND_EDIT_TEST = "controller?command=editTest";

	public static final String COMMAND_EDIT_TEST_CONTENT = "controller?command=editTestContent";
	
	public static final String COMMAND_FORGOT_PASSWORD = "controller?command=forgotPassword";
}