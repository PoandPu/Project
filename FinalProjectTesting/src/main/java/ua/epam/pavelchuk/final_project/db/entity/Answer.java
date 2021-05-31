package ua.epam.pavelchuk.final_project.db.entity;

public class Answer extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -319232265967113628L;

	private String nameRu;
	private String nameEn;
	private boolean isCorrect;
	private int questionId;
	
	public Answer() {}
	
	public Answer(String nameRu, String nameEn) {
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
	
	public boolean getIsCorrect() {
		return isCorrect;
	}

	public void setIsCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}
	
	public int getQuestionId() {
		return questionId;
	}
	public void setQuestionId(int questionId) {
		this.questionId = questionId;
	}
	
	@Override
	public String toString() {
		return "Answer [id:" + getId() +", name en:" + nameEn + ", name ru:" + nameRu + "]";	
	}
	
}
