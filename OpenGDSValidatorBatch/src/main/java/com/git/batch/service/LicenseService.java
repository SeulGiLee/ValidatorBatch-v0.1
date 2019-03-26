package com.git.batch.service;

import java.io.File;

import com.git.batch.domain.LicenseInfo;
import com.git.batch.license.DateCheck;
import com.git.batch.license.EncryptCheck;
import com.git.batch.license.MacCheck;

public class LicenseService {
	MacCheck macc;
	DateCheck datec;
	EncryptCheck encc;

	public LicenseService() throws Throwable {
		super();
	}

	public boolean isValidLicense(String baseDir) throws Throwable {
		boolean flag = true;
		String keyFile = baseDir + File.separator + "gitlicense.key";
		this.datec = new DateCheck();
		this.macc = new MacCheck();
		this.encc = new EncryptCheck();
		
		try {
			encc.inputDecryptedInfo(keyFile, MacCheck.getCurrentMac(), DateCheck.getCurrentDate());	
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Request failed - Couldn't load license key.");
			System.exit(500);
		}

		if (!datec.isAllowed(LicenseInfo.getUntil(), LicenseInfo.getLast(), MacCheck.getCurrentMac())) {
			flag = false;
			System.out.println("Request failed - your license is expired.");
			System.exit(500);
		}

		if (!macc.isAllowed(LicenseInfo.getAllowed())) {
			flag = false;
			System.out.println("Request failed - you need license.");
			System.exit(500);
		}

		encc.outputDecryptedInfo(keyFile);
		return flag;
	}
}
