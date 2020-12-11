package com.ggx.files.service.config.spring.converter;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.core.convert.converter.Converter;
import org.springframework.util.StringUtils;

public class StringToDateConverter implements Converter<String, Date> {
	
	 private static final String dateFormat      = "yyyy-MM-dd HH:mm:ss";
	 private static final String shortDateFormat = "yyyy-MM-dd";

	    @Override
	    public Date convert(String source) {
	        if (!StringUtils.hasText(source)) {
	            return null;
	        }
	        source = source.trim();
	        try {
	            if (source.contains("-")) {
	                SimpleDateFormat formatter;
	                if (source.contains(":")) {
	                    formatter = new SimpleDateFormat(dateFormat);
	                } else {
	                    formatter = new SimpleDateFormat(shortDateFormat);
	                }
	                Date dtDate = formatter.parse(source);
	                return dtDate;
	            } else if (source.matches("^\\d+$")) {
	                Long lDate = Long.valueOf(source);
	                return new Date(lDate);
	            }
	        } catch (Exception e) {
	            throw new RuntimeException(String.format("parser %s to Date fail", source));
	        }
	        throw new RuntimeException(String.format("parser %s to Date fail", source));
	    }



}
