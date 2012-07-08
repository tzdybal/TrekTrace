package org.trektrace.entities;

import com.ianywhere.ultralitej12.UUIDValue;

public abstract class Entity {
	private UUIDValue id;

	public UUIDValue getId() {
		return id;
	}

	public void setId(UUIDValue id) {
		this.id = id;
	}
}
