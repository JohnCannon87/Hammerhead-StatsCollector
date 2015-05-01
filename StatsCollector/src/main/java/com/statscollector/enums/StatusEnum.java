package com.statscollector.enums;

public enum StatusEnum {

	OPEN("status:open"), MERGED("status:merged"), ABANDONED("status:abandoned");

	private String name;

	private StatusEnum(final String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return name;
	}

}
