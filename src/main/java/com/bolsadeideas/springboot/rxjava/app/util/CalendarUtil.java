package com.bolsadeideas.springboot.rxjava.app.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

public class CalendarUtil {
	
	//PATTERN_DATE_DD_MMM_YYYY format=05 Feb 2020
	public static final String PATTERN_DATE_DD_MMM_YYYY = "dd MMM yyyy";
	
	public static Locale getLocalePeru() {
		return new Locale("es", "PE");
	}
	
	public static String getDateOperation() throws Exception {
		Date today = Calendar.getInstance().getTime();
		return convertDateToString(today, CalendarUtil.PATTERN_DATE_DD_MMM_YYYY);
	}
	
	public static String convertDateToString(Date date, String format) throws Exception {
		String strFecha = null;
		if(date!=null) {
			DateFormat df = new SimpleDateFormat(format, getLocalePeru());
			strFecha = df.format(date);
		}
		return StringUtils.capitalize(strFecha);
	}
	

}
