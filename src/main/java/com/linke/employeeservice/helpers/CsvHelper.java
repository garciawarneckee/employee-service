package com.linke.employeeservice.helpers;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.InputStream;
import java.util.List;

public class CsvHelper {

	public static <T> List<T> read(Class<T> clazz, InputStream stream) {
		try {
			CsvSchema bootstrapSchema = CsvSchema.emptySchema().withHeader();
			bootstrapSchema.withColumnSeparator(',');
			CsvMapper mapper = new CsvMapper();
			MappingIterator<T> readValues = mapper.readerFor(clazz).with(bootstrapSchema).readValues(stream);
			return readValues.readAll();
		} catch (Exception e) {
			throw new RuntimeException("Cannot parse file");
		}
	}
}