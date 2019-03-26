package com.git.batch.domain;

import java.util.List;
import java.util.Map;

public class LicenseInfo {
	// 검수 허용된 맥 주소 배열
	public static List<String> allowed;
	// 검수 사용가능 기한일시의 long타입
	public static long until;
	// 마지막 검수 수행일시 맥주소 - 검수일시
	public static Map<String, Long> last;

	public static List<String> getAllowed() {
		return allowed;
	}

	public static void setAllowed(List<String> allowed) {
		LicenseInfo.allowed = allowed;
	}

	public static long getUntil() {
		return until;
	}

	public static void setUntil(long until) {
		LicenseInfo.until = until;
	}

	public static Map<String, Long> getLast() {
		return last;
	}

	public static void setLast(Map<String, Long> last) {
		LicenseInfo.last = last;
	}

}
