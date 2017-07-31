package edu.lu.uni.serval.diffentry;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegExp {
	private static final String REGULAR_EXPRESSION = "^@@\\s\\-\\d+,*\\d*\\s\\+\\d+,*\\d*\\s@@$"; //@@ -21,0 +22,2 @@
	private static Pattern pattern = Pattern.compile(REGULAR_EXPRESSION);
	
	public static boolean filterSignal(String string) {
		boolean flag = false;
		
		Matcher res = pattern.matcher(string);
		if (res.matches()) {
			flag = true;
		}
		
		return flag;
	}
}
