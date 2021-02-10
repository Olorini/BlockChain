package utils;

public class StringTools {

	public static boolean equals(String str1, String str2) {
		return str1 == null && str2 == null || str1 != null && str1.equals(str2);
	}

	public static boolean notEquals(String str1, String str2) {
		return !equals(str1, str2);
	}
}
