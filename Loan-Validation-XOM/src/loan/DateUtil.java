package loan;

import java.util.Calendar;
import java.util.Date;

public class DateUtil {

	public static Date now() {
		return Calendar.getInstance().getTime();
	}
	
	public static Date makeDate(int year, int month, int day) {
		
		Calendar cal=Calendar.getInstance();
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.YEAR, 0);
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, day);
		return cal.getTime();
	}
}
