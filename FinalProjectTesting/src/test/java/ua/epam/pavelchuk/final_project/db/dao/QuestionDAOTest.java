package ua.epam.pavelchuk.final_project.db.dao;




import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ua.epam.pavelchuk.final_project.db.entity.Question;
import ua.epam.pavelchuk.final_project.db.exception.DBException;

public class QuestionDAOTest {
	
	private static QuestionDAO questionDAO;
	private static Question question;
	
	@BeforeClass
	public static void init() throws DBException {
		System.out.println("init");
		questionDAO = QuestionDAO.getInstance(false);
		question = new Question();
		question.setId(1000);
		question.setNameRu("Имя вопроса (русский)");
		question.setNameEn("Name of a question (english)");
		question.setTestId(1);
		questionDAO.insert(question);
	}
	
	@AfterClass
	public static void tearDown() throws DBException {
		System.out.println("delete");
		questionDAO.delete(question.getId());
	}
	
	@Test
	public void findQuestionsByTest() throws DBException {
		assertFalse(questionDAO.findQuestionsByTest(question.getTestId()).isEmpty());
	}
	
	@Test
	public void findQuestionsById() throws DBException {
		assertNotEquals(question, questionDAO.findQuestionById(question.getId()));
	}
	
	@Test
	public void update() throws DBException {
		String nameRu = "Новое имя вопроса (русский)";
		String nameEn = "New name of a question (english)";
		int testId = 2;
		question.setNameRu(nameRu);
		question.setNameEn(nameEn);
		question.setTestId(testId);
		assertTrue(questionDAO.update(question));
		questionDAO.findQuestionById(question.getId());
		assertEquals(nameRu, question.getNameRu());
		assertEquals(nameEn, question.getNameEn());
		assertEquals(testId, question.getTestId());
	}
	
	
}
