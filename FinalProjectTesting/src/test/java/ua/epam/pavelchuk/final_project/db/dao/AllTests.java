package ua.epam.pavelchuk.final_project.db.dao;


import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import ua.epam.pavelchuk.final_project.web.command.EditTestContentCommantTest;
import ua.epam.pavelchuk.final_project.web.command.ViewAllSubjectsCommandTest;

@RunWith(Suite.class)
@SuiteClasses({SubjectDAOTest.class , TestDAOTest.class, UserDAOTest.class, AnswerDAOTest.class, QuestionDAOTest.class, ResultDAOTest.class, ViewAllSubjectsCommandTest.class, EditTestContentCommantTest.class})
public class AllTests {

}
