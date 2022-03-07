package com.aghs.mobilevaccination.config;

import com.aghs.mobilevaccination.data.converter.StringToDriveStatusConvertor;
import com.aghs.mobilevaccination.data.converter.StringToLocalDate;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new StringToDriveStatusConvertor());
        registry.addConverter(new StringToLocalDate());
    }
}
