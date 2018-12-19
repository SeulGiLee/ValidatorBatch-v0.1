package com.git.batch.service;

import static org.junit.Assert.*;

import org.junit.Test;

public class BathServiceTest {

	@Test
	public void test() {

		BathService service = new BathService();
		boolean res = false;
		try {
			res = service.validate("C:\\val", "", "", "", "shp", 2, "C:\\val\\digitalmap20_layer.json",
					"C:\\val\\digitalmap20_option.json", "C:\\val\\digitalmap20.zip", "EPSG:5186");
		} catch (Throwable e) {
			e.printStackTrace();
		}
		
		assertEquals(true, res);
		
	}
}
