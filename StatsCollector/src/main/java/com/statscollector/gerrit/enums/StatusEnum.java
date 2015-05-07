package com.statscollector.gerrit.enums;

public enum StatusEnum {

	OPEN("open"), MERGED("merged"), ABANDONED("abandoned");

	private String name;

	private StatusEnum(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}
