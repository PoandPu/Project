package ua.epam.pavelchuk.final_project.db.entity;

import java.math.BigDecimal;

public class Result extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 2345675637639324893L;

	private BigDecimal mark;
	private int entrantId;
	private int testId;
	
	//The parameters given below are required to form a table of results.
	private String subjectNameRu;
	private String subjectNameEn;
	private String testNameRu;
	private String testNameEn;
	private String testDate;
	
	public Result() {}
	
	public Result(BigDecimal mark, int entrantId, int testId) {
		this.mark = mark;
		this.entrantId = entrantId;
		this.testId = testId;
	}
	
	public BigDecimal getMark() {
		return mark;
	}
	public void setMark(BigDecimal mark) {
		this.mark = mark;
	}
	public int getEntrantId() {
		return entrantId;
	}
	public void setEntrantId(int entrantId) {
		this.entrantId = entrantId;
	}
	public int getTestId() {
		return testId;
	}
	public void setTestId(int testId) {
		this.testId = testId;
	}
	
	public void setSubjectNameRu(String nameRu) {
		subjectNameRu = nameRu;
	}
	public String getSubjectNameRu() {
		return subjectNameRu;
	}
	public void setSubjectNameEn(String nameEn) {
		subjectNameEn = nameEn;
	}
	public String getSubjectNameEn() {
		return subjectNameEn;
	}
	
	public void setTestNameRu(String nameRu) {
		testNameRu = nameRu;
	}
	public String getTestNameRu() {
		return testNameRu;
	}
	public void setTestNameEn(String nameEn) {
		testNameEn = nameEn;
	}
	public String getTestNameEn() {
		return testNameEn;
	}
	
	public void setTestDate(String time) {
		testDate = time;
	}
	public String getTestDate() {
		return testDate;
	}
	
	@Override
	public String toString() {
		return "Result [id:" + getId() +", name en:" + testNameEn + ", name ru:" + testNameRu + "]";	
	}
	
}
