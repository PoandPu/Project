package ua.epam.pavelchuk.final_project.web.command;

import java.util.Map; 
import java.util.TreeMap;

import org.apache.log4j.Logger;

import ua.epam.pavelchuk.final_project.web.command.admin.subject.AddSubjectCommand;
import ua.epam.pavelchuk.final_project.web.command.admin.subject.EditSubjectCommand;
import ua.epam.pavelchuk.final_project.web.command.admin.test.AddAnswerCommand;
import ua.epam.pavelchuk.final_project.web.command.admin.test.AddQuestionCommand;
import ua.epam.pavelchuk.final_project.web.command.admin.test.AddTestCommand;
import ua.epam.pavelchuk.final_project.web.command.admin.test.DeleteAnswerCommand;
import ua.epam.pavelchuk.final_project.web.command.admin.test.DeleteQuestionCommand;
import ua.epam.pavelchuk.final_project.web.command.admin.test.EditTestCommand;
import ua.epam.pavelchuk.final_project.web.command.admin.test.EditTestContentCommand;
import ua.epam.pavelchuk.final_project.web.command.admin.user.EditUserCommand;
import ua.epam.pavelchuk.final_project.web.command.admin.user.FindUserCommand;
import ua.epam.pavelchuk.final_project.web.command.admin.user.ViewUsersListCommand;
import ua.epam.pavelchuk.final_project.web.command.common.CheckTestCommand;
import ua.epam.pavelchuk.final_project.web.command.common.PasswordRecoveryCommand;
import ua.epam.pavelchuk.final_project.web.command.common.GeneratePasswordCommand;
import ua.epam.pavelchuk.final_project.web.command.common.LoginCommand;
import ua.epam.pavelchuk.final_project.web.command.common.LogoutCommand;
import ua.epam.pavelchuk.final_project.web.command.common.RegistrationCommand;
import ua.epam.pavelchuk.final_project.web.command.common.SwitchLocaleCommand;
import ua.epam.pavelchuk.final_project.web.command.common.UserSettingsCommand;
import ua.epam.pavelchuk.final_project.web.command.common.ViewAllSubjectsCommand;
import ua.epam.pavelchuk.final_project.web.command.common.ViewProfileCommand;
import ua.epam.pavelchuk.final_project.web.command.common.ViewTestsListCommand;
import ua.epam.pavelchuk.final_project.web.command.common.ViewTestCommand;
import ua.epam.pavelchuk.final_project.web.command.error.ViewErrorCommand;


/**
 * Holder for all commands.
 * 
 * @author O.Pavelchuk
 * 
 */
public class CommandContainer {
	
	/**
	 * Private utility class constructor
	 */
	private CommandContainer(){}
	
	private static final Logger LOG = Logger.getLogger(CommandContainer.class);
	
	private static Map<String, Command> commands = new TreeMap<>();
	
	static {
		
		// admin commands 
		commands.put("editSubject", new EditSubjectCommand());
		commands.put("editTest", new EditTestCommand());
		commands.put("addSubject", new AddSubjectCommand());
		commands.put("addTest", new AddTestCommand());
		commands.put("editTestContent", new EditTestContentCommand());
		commands.put("viewUsersList", new ViewUsersListCommand());
		commands.put("editUser", new EditUserCommand());
		commands.put("addQuestion", new AddQuestionCommand());
		commands.put("addAnswer", new AddAnswerCommand());
		commands.put("deleteAnswer", new DeleteAnswerCommand());
		commands.put("deleteQuestion", new DeleteQuestionCommand());
		commands.put("findUser", new FindUserCommand());
		
//		// client commands
		commands.put("checkTest", new CheckTestCommand());
		
		// common commands
		commands.put("viewAllSubjects", new ViewAllSubjectsCommand());
		commands.put("viewTestsList", new ViewTestsListCommand());
		commands.put("viewTest", new ViewTestCommand());
		commands.put("logout", new LogoutCommand());
		commands.put("userProfile", new ViewProfileCommand());
		commands.put("switchLocale", new SwitchLocaleCommand());
		commands.put("userSettings", new UserSettingsCommand());
		
		//out of control commands
		commands.put("login", new LoginCommand());
		commands.put("registration", new RegistrationCommand());
		commands.put("viewErrorPage", new ViewErrorCommand());
		commands.put("passwordRecovery", new PasswordRecoveryCommand());
		commands.put("generatePassword", new GeneratePasswordCommand());
		
		LOG.debug("Command container was successfully initialized");
		LOG.trace("Number of commands --> " + commands.size());
	}

	/**
	 * Returns command object with the given name.
	 * 
	 * @param commandName
	 *            Name of the command.
	 * @return Command object.
	 */
	public static Command get(String commandName) {
		LOG.trace(commands);
		if (commandName == null || !commands.containsKey(commandName)) {
			LOG.trace("Command not found, name --> " + commandName);
			return commands.get("noCommand"); 
		}
		
		return commands.get(commandName);
	}
	
}