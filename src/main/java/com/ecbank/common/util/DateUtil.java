package com.ecbank.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class DateUtil {

	/**
	 * 현재(한국기준) 날짜정보를 얻는다. 표기법은 yyyymmddHHMISS
	 *
	 * @param dateType
	 *            날짜타입
	 * @return String yyyymmdd형태의 현재 한국시간.
	 */
	public static String getCurrentFullDate() {
		String strDate = getCurrentDate();
		String strTime = getCurrentTime();

		return (strDate+strTime);
	}

	/**
     * 현재(한국기준) 날짜정보를 얻는다. 표기법은 yyyymmdd
     *
     * @param dateType
     *            날짜타입
     * @return String yyyymmdd형태의 현재 한국시간.
     */
    public static String getCurrentDate() {
        Calendar aCalendar = Calendar.getInstance();

        int year = aCalendar.get(Calendar.YEAR);
        int month = aCalendar.get(Calendar.MONTH) + 1;
        int date = aCalendar.get(Calendar.DATE);
        String strDate = Integer.toString(year) + ((month < 10) ? "0" + Integer.toString(month) : Integer.toString(month)) + ((date < 10) ? "0" + Integer.toString(date) : Integer.toString(date));

        return strDate;
    }

	/**
     * 현재(한국기준) 날짜정보를 얻는다. 표기법은 HH24MMSS
     *
     * @param dateType
     *            날짜타입
     * @return String HHMMSS 형태의 현재 한국시간.
     */
    public static String getCurrentTime() {
        Calendar aCalendar = Calendar.getInstance();

        int hour = aCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = aCalendar.get(Calendar.MINUTE);
        int second = aCalendar.get(Calendar.SECOND);
        String strTime = ((hour < 10) ? "0" + Integer.toString(hour) : Integer.toString(hour)) + ((minute < 10) ? "0" + Integer.toString(minute) : Integer.toString(minute)) + ((second < 10) ? "0" + Integer.toString(second) : Integer.toString(second));

        return strTime;
    }


    public static String getToday() {
        return getCurrentDate();
    }


    /**
     * 날짜형태의 String의 날짜 포맷 및 TimeZone을 변경해 주는 메서드
     *
     * @param strSource
     *            바꿀 날짜 String
     * @param fromDateFormat
     *            기존의 날짜 형태
     * @param toDateFormat
     *            원하는 날짜 형태
     * @param strTimeZone
     *            변경할 TimeZone(""이면 변경 안함)
     * @return 소스 String의 날짜 포맷을 변경한 String
     */
    public static String convertDate(String sDate, String sTime, String sFormatStr) {
        String dateStr = validChkDate(sDate);
        String timeStr = validChkTime(sTime);

        Calendar cal = null;
        cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, Integer.parseInt(dateStr.substring(0, 4)));
        cal.set(Calendar.MONTH, Integer.parseInt(dateStr.substring(4, 6)) - 1);
        cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateStr.substring(6, 8)));
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeStr.substring(0, 2)));
        cal.set(Calendar.MINUTE, Integer.parseInt(timeStr.substring(2, 4)));

        SimpleDateFormat sdf = new SimpleDateFormat(sFormatStr, Locale.ENGLISH);

        return sdf.format(cal.getTime());
    }

    /**
     * 입력된 일자 문자열을 확인하고 8자리로 리턴
     *
     * @param dateStr
     *            문자열
     * @return 문자열
     */
    public static String validChkDate(String dateStr) {

    	if (dateStr.trim().equals("")) {
    		return dateStr;
    	}

        if (dateStr == null || !(dateStr.trim().length() == 8 || dateStr.trim().length() == 10)) {
            throw new IllegalArgumentException("Invalid date format: " + dateStr);
        }

        if (dateStr.length() == 10) {
        	return StringUtils.remove(dateStr, "-");
        }

        return dateStr;
    }

    /**
     * 입력된 일자 문자열을 확인하고 8자리로 리턴
     *
     * @param timeStr
     *            시간문자열
     * @return 시간문자열
     */
    public static String validChkTime(String timeStr) {
        if (timeStr == null || !(timeStr.trim().length() == 6)) {
            throw new IllegalArgumentException("Invalid time format: " + timeStr);
        }

        if (timeStr.length() == 6) {
            timeStr = StringUtils.remove(timeStr, ':');
        }

        return timeStr;
    }

}
