package ua.epam.pavelchuk.final_project.db.entity;

import java.io.Serializable;

/**
 * Root of all entities which have identifier field.
 * 
 * @author O.Pavelchuk
 * 
 */
public abstract class Entity implements Serializable {

	private static final long serialVersionUID = 2373219931075931675L;

	protected int id;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
