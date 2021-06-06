package ua.epam.pavelchuk.final_project.db.entity;

public class Test extends Entity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8627134466051125032L;
	private String nameRu;
	private String nameEn;
	private int time;
	private int requests;
	private int subjectId;
	private int difficultyLevel;
	private String difficultyNameEn;
	private String difficultyNameRu;

	public Test() {
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

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public int getRequests() {
		return requests;
	}

	public void setRequests(int request) {
		this.requests = request;
	}

	public int getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(int id) {
		subjectId = id;
	}

	public int getDifficultyLevel() {
		return difficultyLevel;
	}

	public void setDifficultyLevel(int id) {
		difficultyLevel = id;
	}

	public String getDifficultyNameEn() {
		return difficultyNameEn;
	}

	public void setDifficultyNameEn(String name) {
		difficultyNameEn = name;
	}

	public String getDifficultyNameRu() {
		return difficultyNameRu;
	}

	public void setDifficultyNameRu(String name) {
		difficultyNameRu = name;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Test test = (Test) obj;
		if (id != test.getId())
			return false;
		if (!nameRu.equals(test.getNameRu()))
			return false;
		if (!nameEn.equals(test.getNameEn()))
			return false;
		if (difficultyLevel != test.getDifficultyLevel())
			return false;
		if (subjectId != test.getSubjectId())
			return false;
		if (time != test.getTime())
			return false;
		else {
			return true;
		}
	}

}
