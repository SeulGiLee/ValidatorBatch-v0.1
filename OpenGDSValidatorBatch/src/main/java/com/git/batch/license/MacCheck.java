package com.git.batch.license;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class MacCheck {
	ArrayList<String> allowedMacs;
	String currentMac;

	public MacCheck() {
		super();
	}

	public void setYourMacAddress() {
		try {
			InetAddress addr = InetAddress.getLocalHost();
			/* NetworkInterface를 이용하여 현재 로컬 서버에 대한 하드웨어 어드레스를 가져오기 */
			NetworkInterface ni = NetworkInterface.getByInetAddress(addr);
			byte[] mac = ni.getHardwareAddress();
			String macAddr = "";
			for (int i = 0; i < mac.length; i++) {
				macAddr += String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "");
			}
			this.setCurrentMac(macAddr);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setAllowedMacAddresses() {
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

			String amacs = properties.getProperty("allowed");
			amacs = amacs.replaceAll("\\s+", "");
			allowedMacs = new ArrayList<String>(Arrays.asList(amacs.split(",")));
			this.setAllowedMacs(allowedMacs);
		} catch (Exception e2) {
			// TODO: handle exception
			e2.printStackTrace();
		}
	}

	public Boolean isAllowed() {
		Boolean flag = false;
		ArrayList<String> allows = this.getAllowedMacs();
		String current = this.getCurrentMac();
		if (allows == null) {
			this.setAllowedMacAddresses();
		}
		if (current == null) {
			this.setYourMacAddress();
		}
		allows = this.getAllowedMacs();
		current = this.getCurrentMac();

		if (allows.size() > 0 && allows != null && !current.equals("") && current != null) {
			if (allows.indexOf(current) != -1) {
				flag = true;
			}
		}
		return flag;
	};

	public String getCurrentMac() {
		return currentMac;
	}

	public void setCurrentMac(String currentMac) {
		this.currentMac = currentMac;
	}

	public ArrayList<String> getAllowedMacs() {
		return allowedMacs;
	}

	public void setAllowedMacs(ArrayList<String> allowedMacs) {
		this.allowedMacs = allowedMacs;
	}

	@Override
	public String toString() {
		return "MacCheck [allowedMacs=" + allowedMacs + ", currentMac=" + currentMac + "]";
	}

}
