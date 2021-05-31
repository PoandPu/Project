package ua.epam.pavelchuk.final_project.db.entity;

/**
 * Subject
 * @author 
 *
 */
public class Subject extends Entity{
	private static final long serialVersionUID = 7478712165594836356L;
	private String nameRu;
	private String nameEn;
	
	public Subject() {}
	
	public Subject(String nameRu, String nameEn) {
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
	
	public String toString() {
		return "Subject [id:" + getId() +", name en:" + nameEn + ", name ru:" + nameRu + "]";	
	}
	
	@Override
		public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Subject subj = (Subject) obj;
		if(id != subj.getId()) 
			return false;
		if (!nameRu.equals(subj.getNameRu()))
			return false;
		if (!nameEn.equals(subj.getNameEn()))
			return false;
		else {
			return true;
		}
	}
}
