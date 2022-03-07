package com.aghs.mobilevaccination.data.converter;

import org.springframework.core.convert.converter.Converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class StringToLocalDate implements Converter<String, LocalDate> {
    @Override
    public LocalDate convert(String source) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println(source);
        try {
            return LocalDate.parse(source, dateFormatter);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
