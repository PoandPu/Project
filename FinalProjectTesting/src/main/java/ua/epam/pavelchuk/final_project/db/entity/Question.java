package ua.epam.pavelchuk.final_project.db.entity;

public class Question extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3585931730186945841L;
	
	private String nameRu;
	private String nameEn;
	private int testId;
	
	public Question() {}
	
	public Question(String nameRu, String nameEn) {
		this.nameEn = nameEn;
		this.nameRu = nameRu;
	}
	
	public String getNameRu() {
		return nameRu;
	}
	public void setNameRu(String nameRu) {
		this.nameRu = nameRu;
	}
	public String getNameEn() {
		return nameEn;
	}
	public void setNameEn(String nameEn) {
		this.nameEn = nameEn;
	}
	
	public int getTestId() {
		return testId;
	}
	public void setTestId(int testId) {
		this.testId = testId;
	}
	
	@Override
	public String toString() {
		return "Question [id:" + getId() +", name en:" + nameEn + ", name ru:" + nameRu + "]";	
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((nameRu == null) ? 0 : nameRu.hashCode());
		result = prime * result + ((nameEn == null) ? 0 : nameEn.hashCode());
		result = prime * result + testId;
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Question question = (Question) obj;
		if(id != question.getId()) 
			return false;
		if (!nameRu.equals(question.getNameRu()))
			return false;
		if (!nameEn.equals(question.getNameEn()))
			return false;
		if (testId != question.getTestId()) 
			return false;
		else 
			return true;
		
	}
}
