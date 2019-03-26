package com.git.batch.license;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import com.git.batch.domain.LicenseInfo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public class EncryptCheck {
	public static String path;
	private String iv;
	private Key keySpec;

	/**
	 * 16자리의 키값을 입력하여 객체를 생성한다.
	 * 
	 * @param key
	 *            암/복호화를 위한 키값
	 * @throws UnsupportedEncodingException
	 *             키값의 길이가 16이하일 경우 발생
	 */
	static String key;

	public EncryptCheck() throws IOException, UnsupportedEncodingException {
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		InputStream inputStream = classLoader.getResourceAsStream("license.properties");
		properties.load(inputStream);
		inputStream.close();
		String yourKey = properties.getProperty("key");
		key = yourKey;

		this.iv = key.substring(0, 16);

		byte[] keyBytes = new byte[16];
		byte[] b = key.getBytes("UTF-8");
		int len = b.length;
		if (len > keyBytes.length) {
			len = keyBytes.length;
		}
		System.arraycopy(b, 0, keyBytes, 0, len);
		SecretKeySpec skeySpec = new SecretKeySpec(keyBytes, "AES");

		this.keySpec = skeySpec;
	}

	public void inputDecryptedInfo(String filePath, String mac, long currentDate)
			throws IOException, NoSuchAlgorithmException, GeneralSecurityException, ParseException {

		// String oristr =
		// "{\"allowed\":[\"D0-50-99-38-B1-C3\",\"D0-50-99-67-EB-DE\"],\"until\":1577804340000,\"last\":{}}";
		// 인증받지 않은 맥주소를 가진 경우
		// String oristr =
		// "{\"allowed\":[\"D0-50-99-38-B1-C1\",\"D0-50-99-67-EB-DE\"],\"until\":1577804340000,\"last\":{}}";
		// 만료기한이 지난 경우
		// String oristr =
		// "{\"allowed\":[\"D0-50-99-38-B1-C3\",\"D0-50-99-67-EB-DE\"],\"until\":1522050532000,\"last\":{}}";
		// 만료기한이 지난 후 날짜를 만료기한 전으로 돌린 경우
		// String oristr =
		// "{\"allowed\":[\"D0-50-99-38-B1-C3\",\"D0-50-99-67-EB-DE\"],\"until\":1553069077000,\"last\":{\"D0-50-99-38-B1-C3\":1552982677000}}";
		// String encstr = this.encrypt(oristr);

		this.setPath(filePath);
		String linfo = new String(Files.readAllBytes(Paths.get(filePath)));
		String dinfo = this.decrypt(linfo);
		JSONParser parser = new JSONParser();
		JSONObject jsonObj = (JSONObject) parser.parse(dinfo);
		JSONArray tempMacs = (JSONArray) jsonObj.get("allowed");
		Object[] tempMacs2 = tempMacs.toArray();
		String[] tempMacs3 = Arrays.copyOf(tempMacs2, tempMacs2.length, String[].class);

		ArrayList<String> allowedMac = new ArrayList<String>(Arrays.asList(tempMacs3));
		LicenseInfo.setAllowed(allowedMac);

		long until = (long) jsonObj.get("until");
		LicenseInfo.setUntil(until);

		JSONObject lastObj = (JSONObject) jsonObj.get("last");
		// String lastString = jsonObj.get("last").toString();
		// JsonObject lastObj2 = new Gson().fromJson(lastString,
		// JsonObject.class);
		Map<String, Long> macLast;

		if (!lastObj.containsKey(mac)) {
			lastObj.put(mac, currentDate);
		}
		macLast = EncryptCheck.getMapFromJsonObject(lastObj);
		LicenseInfo.setLast(macLast);
	}

	public void outputDecryptedInfo(String filePath) {
		JsonObject outJson = new JsonObject();

		List<String> allowed = LicenseInfo.getAllowed();
		String alstr = new Gson().toJson(allowed);
		JsonArray aljson = new Gson().fromJson(alstr, JsonArray.class);
		outJson.add("allowed", aljson);

		long until = LicenseInfo.getUntil();
		String unstr = new Gson().toJson(until);
		JsonPrimitive unjson = new Gson().fromJson(unstr, JsonPrimitive.class);
		outJson.add("until", unjson);

		Map<String, Long> last = LicenseInfo.getLast();
		String lastr = new Gson().toJson(last);
		JsonObject lajson = new Gson().fromJson(lastr, JsonObject.class);
		outJson.add("last", lajson);
		BufferedWriter out;
		String outs = outJson.toString();
		String eouts = null;
		try {
			eouts = this.encrypt(outs);
		} catch (UnsupportedEncodingException | GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (eouts != null) {
			try {
				out = new BufferedWriter(new FileWriter(filePath));
				out.write(eouts);
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * AES256 으로 암호화 한다.
	 * 
	 * @param str
	 *            암호화할 문자열
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws GeneralSecurityException
	 * @throws UnsupportedEncodingException
	 */
	public String encrypt(String str)
			throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException {
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.ENCRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
		byte[] encrypted = c.doFinal(str.getBytes("UTF-8"));
		String enStr = new String(Base64.encodeBase64(encrypted));
		return enStr;
	}

	public static Map<String, Long> getMapFromJsonObject(JSONObject jsonObj) {
		Map<String, Long> map = null;
		try {
			map = new ObjectMapper().readValue(jsonObj.toJSONString(), Map.class);
		} catch (JsonParseException e) {
			e.printStackTrace();
		} catch (JsonMappingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}

	/**
	 * AES256으로 암호화된 txt 를 복호화한다.
	 * 
	 * @param str
	 *            복호화할 문자열
	 * @return
	 * @throws NoSuchAlgorithmException
	 * @throws GeneralSecurityException
	 * @throws UnsupportedEncodingException
	 */
	public String decrypt(String str)
			throws NoSuchAlgorithmException, GeneralSecurityException, UnsupportedEncodingException {
		Cipher c = Cipher.getInstance("AES/CBC/PKCS5Padding");
		c.init(Cipher.DECRYPT_MODE, keySpec, new IvParameterSpec(iv.getBytes()));
		byte[] byteStr = Base64.decodeBase64(str.getBytes());
		return new String(c.doFinal(byteStr), "UTF-8");
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
