package com.git.batch.license;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.List;

public class MacCheck {
	private static String currentMac;

	public MacCheck() throws Throwable {
		super();
		try {
			InetAddress addr = InetAddress.getLocalHost();
			/* NetworkInterface를 이용하여 현재 로컬 서버에 대한 하드웨어 어드레스를 가져오기 */
			NetworkInterface ni = NetworkInterface.getByInetAddress(addr);
			byte[] mac = ni.getHardwareAddress();
			String macAddr = "";
			for (int i = 0; i < mac.length; i++) {
				macAddr += String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : "");
			}
			MacCheck.setCurrentMac(macAddr);
		} catch (Exception e) {
			// e.printStackTrace();
			System.out.println("Couldn't find mac address.");
			throw new Throwable();
		}
	}

	public Boolean isAllowed(List<String> allows) throws Throwable {
		Boolean flag = false;
		String current = MacCheck.getCurrentMac();
		if (allows.size() > 0 && allows != null && !current.equals("") && current != null) {
			if (allows.indexOf(current) != -1) {
				flag = true;
			}
		}
		return flag;
	}

	public static String getCurrentMac() {
		return currentMac;
	}

	public static void setCurrentMac(String currentMac) {
		MacCheck.currentMac = currentMac;
	};

}
