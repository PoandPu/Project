package ua.epam.pavelchuk.final_project.db.dao;


import static org.junit.Assert.assertFalse;

import java.math.BigDecimal;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ua.epam.pavelchuk.final_project.db.entity.Result;
import ua.epam.pavelchuk.final_project.db.exception.DBException;

public class ResultDAOTest {
	
	private static ResultDAO resultDAO;
	private static Result result;
	
	@BeforeClass
	public static void init() throws DBException {
		System.out.println("init");
		resultDAO = ResultDAO.getInstance(false);
		result = new Result();
		result.setId(1000);
		result.setMark(new BigDecimal(60));
		result.setEntrantId(1);
		result.setTestId(1);
		resultDAO.insert(result);
	}
	
	@AfterClass
	public static void tearDown() throws DBException {
		System.out.println("delete");
		resultDAO.delete(result.getId());
	}
	
	@Test
	public void getResultsByUserIdWithPagination() throws DBException {
		assertFalse(resultDAO.getResultsByUserId(result.getEntrantId(), "tests.name_Ru", "ASC", 0, 10).isEmpty());
	}
	
	@Test
	public void getResultsByUserId() throws DBException {
		assertFalse(resultDAO.getResultsByUserId(result.getEntrantId()).isEmpty());
	}
	
}
