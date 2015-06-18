package com.statscollector.gerrit.serialization;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

public class TimestampDeserializer implements JsonDeserializer<Timestamp> {

	private static final String DATE_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";

	@Override
	public Timestamp deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context)
			throws JsonParseException {
		String date = json.getAsString();
		date = date.split("\\.")[0];

		SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT_PATTERN);

		try {
			return new Timestamp(formatter.parse(date).getTime());
		} catch (ParseException e) {
			return null;
		}
	}

}
