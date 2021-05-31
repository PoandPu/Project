package ua.epam.pavelchuk.final_project.db.dao;


import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import ua.epam.pavelchuk.final_project.db.entity.User;
import ua.epam.pavelchuk.final_project.db.exception.DBException;

public class UserDAOTest {
	
	private static User testUser;
	private static UserDAO userDAO;
	
	@BeforeClass
	public static void init() throws DBException {
		System.out.println("init");
		userDAO = UserDAO.getInstance(false);
		testUser = new User();
		testUser.setLogin("test123");
		testUser.setPassword("test123");
		testUser.setPasswordKey("someKey");
		testUser.setFirstName("TestFirstName");
		testUser.setLastName("TestLastName");
		testUser.setEmail("alex80395@gmail.com");
		testUser.setLanguage("en");
		testUser.setId(10000);
		testUser.setRoleId(1);
		userDAO.insert(testUser);
	}
	
	@AfterClass
	public static void tearDown() throws DBException {
		System.out.println("delete");
		userDAO.deleteById(testUser.getId());
	}
	
	@Test
	public void hasLogin() throws DBException {
		System.out.println("hasLogin");
		assertTrue(userDAO.hasLogin(testUser.getLogin()));
	}
	
	@Test
	public void findByLogin() throws DBException {
		System.out.println("findByLogin");
		assertTrue(testUser.equals(userDAO.findByLogin(testUser.getLogin())));
	}
	
	@Test
	public void getUserIdByLogin() throws DBException {
		System.out.println("getUserIdByLogin");
		assertTrue(testUser.getId() == userDAO.getIdByLogin(testUser.getLogin()));
	}
	
	@Test
	public void insertNullUser() throws DBException {
		System.out.println("insertNullUser");
		User user = null;
		assertFalse(userDAO.insert(user));
	}
	
	@Test
	public void findById() throws DBException {
		System.out.println("findById");
		assertTrue(testUser.equals(userDAO.findById(testUser.getId())));
	}
	
	@Test
	public void getUsers() throws DBException {
		System.out.println("getUsers");
		assertTrue(!userDAO.findAllUsersOrderBy("id", "ASC", 1, 10).isEmpty());
//		assertThrows(DBException.class, ()->{
//			userDAO.findAllUsersOrderBy(null, null,0, 0);
//		});
	}
	
	@Test
	public void searchUsers() throws DBException {
		System.out.println("searchUsers");
		assertFalse(userDAO.findUsersOrderBy(testUser.getFirstName()).isEmpty());
		assertFalse(userDAO.findUsersOrderBy(testUser.getLastName()).isEmpty());
		assertFalse(userDAO.findUsersOrderBy(testUser.getEmail()).isEmpty());
		assertFalse(userDAO.findUsersOrderBy(testUser.getLogin()).isEmpty());
	}
	
	@Test
	public void hasEmail() throws DBException {
		System.out.println("hasEmail");
		assertTrue(userDAO.hasEmail(testUser.getEmail()));
		assertFalse(userDAO.hasEmail(null));
	}
	
	@Test
	public void unblockById() throws DBException {
		System.out.println("unblockById " + testUser.getIsBlocked());
		assertTrue(testUser.getIsBlocked());
		userDAO.unblockById(testUser.getId());
		testUser = userDAO.findById(testUser.getId());
		assertFalse(testUser.getIsBlocked());
	}
	
	@Test
	public void blockById() throws DBException {
		System.out.println("blockById " + testUser.getIsBlocked());
		assertFalse(testUser.getIsBlocked());
		userDAO.blockById(testUser.getId());
		testUser = userDAO.findById(testUser.getId());
		assertTrue(testUser.getIsBlocked());
	}
	
	@Test
	public void update() throws DBException {
		System.out.println("update");
		testUser.setFirstName(testUser.getFirstName() + "TestUpdate");
		testUser.setLastName(testUser.getLastName() + "TestUpdate");
		testUser.setEmail(testUser.getEmail() + "TestUpdate");
		assertTrue(userDAO.update(testUser));
		testUser = userDAO.findById(testUser.getId());
	}
		
}
