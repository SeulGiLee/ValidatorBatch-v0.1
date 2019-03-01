package com.git.batch.license;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Properties;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;

public class DateCheck {
	private static final String TIME_SERVER = "pool.ntp.org";
	long untilDate = 0;
	long currentDate = 0;

	public DateCheck() {
		super();
	}

	public boolean setYourCurrentDate() {
		boolean flag = false;
		NTPUDPClient timeClient = new NTPUDPClient();
		timeClient.setDefaultTimeout(1000);
		try {
			timeClient.open();
			InetAddress address = InetAddress.getByName(TIME_SERVER);
			TimeInfo timeInfo = timeClient.getTime(address);
			long returnTime = timeInfo.getMessage().getTransmitTimeStamp().getTime();
			flag = true;
			this.setCurrentDate(returnTime);
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			this.setCurrentDate(System.currentTimeMillis());
			e.printStackTrace();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			this.setCurrentDate(System.currentTimeMillis());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			this.setCurrentDate(System.currentTimeMillis());
			e.printStackTrace();
		}
		return flag;
	}

	public void setYourLimitDate() {
		try {
			ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
			Properties properties = new Properties();

			try {
				InputStream inputStream = classLoader.getResourceAsStream("license.properties");
				properties.load(inputStream);
				inputStream.close();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			long until = Long.parseLong(properties.getProperty("until"));
			this.setUntilDate(until);
		} catch (Exception e2) {
			// TODO: handle exception
			e2.printStackTrace();
		}
	}

	public Boolean isAllowed() {
		Boolean flag = false;
		long until = this.getUntilDate();
		long current = this.getCurrentDate();
		if (until == 0) {
			this.setYourLimitDate();
		}
		if (current == 0) {
			this.setYourCurrentDate();
		}
		until = this.getUntilDate();
		current = this.getCurrentDate();

		if (until > 0 && current > 0) {
			if (current <= until) {
				flag = true;
			}
		}
		return flag;
	};

	public long getUntilDate() {
		return untilDate;
	}

	public void setUntilDate(long untilDate) {
		this.untilDate = untilDate;
	}

	public long getCurrentDate() {
		return currentDate;
	}

	public void setCurrentDate(long currentDate) {
		this.currentDate = currentDate;
	}

}
