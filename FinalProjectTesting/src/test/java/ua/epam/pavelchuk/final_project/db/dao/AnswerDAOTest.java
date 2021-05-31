package ua.epam.pavelchuk.final_project.db.dao;


import static org.junit.jupiter.api.Assertions.assertFalse; 
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ua.epam.pavelchuk.final_project.db.entity.Answer;
import ua.epam.pavelchuk.final_project.db.exception.DBException;

public class AnswerDAOTest {
	
	private static AnswerDAO answerDAO;
	private static Answer answer;
	
	@BeforeClass
	public static void init() throws DBException {
		System.out.println("init");
		answerDAO = AnswerDAO.getInstance(false);
		answer = new Answer();
		answer.setId(1000);
		answer.setIsCorrect(false);
		answer.setNameEn("Test answer");
		answer.setNameRu("Тест ответ");
		answer.setQuestionId(1);
		answerDAO.insert(answer);
	}
	
	@AfterClass
	public static void tearDown() throws DBException {
		System.out.println("delete");
		answerDAO.delete(answer.getId());
	}
	
	@Test
	public void findAnswersByQuestion() throws DBException {
		assertFalse(answerDAO.findAnswersByQuestion(answer.getQuestionId()).isEmpty());
	}
	
	@Test
	public void findCorrectAnswersByQuestion() throws DBException {
		assertFalse(answerDAO.findCorrectAnswersByQuestion(answer.getQuestionId()).isEmpty());
	}
	
	@Test
	public void update() throws DBException {
		answer.setNameRu("Новое имя");
		answer.setNameEn("New name");
		answer.setIsCorrect(true);	
		assertTrue(answerDAO.update(answer));
	}
}
