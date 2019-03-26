package com.git.batch.license;

import java.util.Map;

import org.apache.commons.net.ntp.NTPUDPClient;

public class DateCheck {
	// 타임서버 주소
	private static final String TIME_SERVER = "pool.ntp.org";
	// 현재 실행 시간
	private static long currentDate = 0;
	private static boolean isOnline = false;

	public DateCheck() {
		super();
		// NTPUDPClient timeClient = new NTPUDPClient();
		// timeClient.setDefaultTimeout(1000);
		// try {
		// timeClient.open();
		// InetAddress address = InetAddress.getByName(TIME_SERVER);
		// TimeInfo timeInfo = timeClient.getTime(address);
		// long returnTime =
		// timeInfo.getMessage().getTransmitTimeStamp().getTime();
		// flag = true;
		// this.setCurrentDate(returnTime);
		// } catch (SocketException e) {
		// TODO Auto-generated catch block
		DateCheck.setCurrentDate(System.currentTimeMillis());
		// e.printStackTrace();
		// } catch (UnknownHostException e) {
		// // TODO Auto-generated catch block
		// this.setCurrentDate(System.currentTimeMillis());
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// this.setCurrentDate(System.currentTimeMillis());
		// e.printStackTrace();
		// }

	}

	public boolean checkYourLastDate(Map<String, Long> last, String mac) {
		boolean success = false;
		long lastDate = last.get(mac);
		long latest = DateCheck.getCurrentDate();
		if (latest != 0 && lastDate != 0) {
			if (lastDate <= latest) {
				success = true;
			}
		}
		return success;
	}

	public boolean setYourCurrentDate() {
		boolean flag = false;
		NTPUDPClient timeClient = new NTPUDPClient();
		timeClient.setDefaultTimeout(1000);
		// try {
		// timeClient.open();
		// InetAddress address = InetAddress.getByName(TIME_SERVER);
		// TimeInfo timeInfo = timeClient.getTime(address);
		// long returnTime =
		// timeInfo.getMessage().getTransmitTimeStamp().getTime();
		// flag = true;
		// this.setCurrentDate(returnTime);
		// } catch (SocketException e) {
		// TODO Auto-generated catch block
		DateCheck.setCurrentDate(System.currentTimeMillis());
		// e.printStackTrace();
		// } catch (UnknownHostException e) {
		// // TODO Auto-generated catch block
		// this.setCurrentDate(System.currentTimeMillis());
		// e.printStackTrace();
		// } catch (IOException e) {
		// // TODO Auto-generated catch block
		// this.setCurrentDate(System.currentTimeMillis());
		// e.printStackTrace();
		// }
		return flag;
	}

	public Boolean isAllowed(long until, Map<String, Long> last, String mac) {
		Boolean flag = false;
		long current = DateCheck.getCurrentDate();
		if (current == 0) {
			this.setYourCurrentDate();
			current = DateCheck.getCurrentDate();
		}
		if (until > 0 && current > 0) {
			if (current <= until) {
				boolean isHonest = this.checkYourLastDate(last, mac);
				if (isHonest) {
					flag = true;
				}
			}
		}
		return flag;
	}

	public static long getCurrentDate() {
		return currentDate;
	}

	public static void setCurrentDate(long currentDate) {
		DateCheck.currentDate = currentDate;
	}

	public static boolean isOnline() {
		return isOnline;
	}

	public static void setOnline(boolean isOnline) {
		DateCheck.isOnline = isOnline;
	}

}
