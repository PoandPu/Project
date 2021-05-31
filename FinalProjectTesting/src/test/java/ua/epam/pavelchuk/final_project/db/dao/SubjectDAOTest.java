package ua.epam.pavelchuk.final_project.db.dao;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ua.epam.pavelchuk.final_project.db.entity.Subject;
import ua.epam.pavelchuk.final_project.db.exception.DBException;

public class SubjectDAOTest {

	private static SubjectDAO subjectDAO;
	private static Subject subject;
	
	@BeforeClass
	public static void init() throws DBException {
		System.out.println("init");
		subjectDAO = SubjectDAO.getInstance(false);
		subject = new Subject();
		subject.setId(10000);
		subject.setNameRu("Предмет тест");
		subject.setNameEn("Subject test");
		subjectDAO.insert(subject);
	}
	
	@AfterClass
	public static void tearDown() throws DBException {
		System.out.println("delete");
		subjectDAO.delete(subject.getId());
	}
	
	@Test
	public void findAll() throws DBException {
		assertFalse(subjectDAO.findAllOrderBy("id", "ASC", 1, 10).isEmpty());
	}
	
	@Test
	public void hasName() throws DBException {
		assertTrue(subjectDAO.hasName(subject.getNameRu()));
		assertTrue(subjectDAO.hasName(subject.getNameEn()));
	}
	
	@Test
	public void getSubjectById() throws DBException {
		assertEquals(subject, subjectDAO.getSubjectById(subject.getId()));
	}
	
	@Test
	public void update() throws DBException {
		subject.setNameRu(subject.getNameRu() + "test");
		assertTrue(subjectDAO.update(subject));
		subject = subjectDAO.getSubjectById(subject.getId());
	}
	
	
}
