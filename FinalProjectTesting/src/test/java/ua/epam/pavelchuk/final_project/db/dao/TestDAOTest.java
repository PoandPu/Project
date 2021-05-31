package ua.epam.pavelchuk.final_project.db.dao;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import ua.epam.pavelchuk.final_project.db.entity.Test;
import ua.epam.pavelchuk.final_project.db.exception.DBException;

public class TestDAOTest {
	private static TestDAO testDAO;
	private static Test test;
	
	@BeforeClass
	public static void init() throws DBException {
		System.out.println("init");
		testDAO = TestDAO.getInstance(false);
		test = new Test();
		test.setId(10000);
		test.setNameRu("Предмет тест");
		test.setNameEn("Subject test");
		test.setDifficultyLevel(3);
		test.setTime(10);
		test.setSubjectId(3);
		testDAO.insert(test);
	}
	
	@AfterClass
	public static void tearDown() throws DBException {
		System.out.println("delete");
		testDAO.delete(test.getId());
	}
	
	@org.junit.Test
	public void getTestById() throws DBException {
		assertEquals(test, testDAO.getTestById(test.getId()));
	}
	
	@org.junit.Test
	public void findTestsBySubject() throws DBException {
		assertFalse(testDAO.findTestsBySubject(test.getSubjectId()).isEmpty());
	}
	
	@org.junit.Test
	public void findAllOrderBy() throws DBException {
		assertFalse(testDAO.findAllOrderBy("id", "ASC", test.getSubjectId(), 1, 10).isEmpty());
	}
	
	@org.junit.Test
	public void findTimeByTestId() throws DBException {
		assertEquals(test.getTime(), testDAO.findTimeByTestId(test.getId()));
	}
	
	@org.junit.Test
	public void hasName() throws DBException {
		assertTrue(testDAO.hasName(test.getNameRu()));
		assertTrue(testDAO.hasName(test.getNameEn()));
	}
	
	@org.junit.Test
	public void increaseRequests() throws DBException {
		assertTrue(testDAO.increaseRequests(test.getId()));
	}
	
	@org.junit.Test
	public void update() throws DBException {
		int difLev = 1;
		int time = 50;
		String nameRu = "Тестирование теста";
		String nameEn = "Testig message";
		test.setNameRu(nameRu);
		test.setNameEn(nameEn);
		test.setDifficultyLevel(difLev);
		test.setTime(time);
		testDAO.update(test);
		test = testDAO.getTestById(test.getId());
		assertEquals(difLev, test.getDifficultyLevel());
		assertEquals(time, test.getTime());
		assertEquals(nameRu, test.getNameRu());
		assertEquals(nameEn, test.getNameEn());
	}		
}
