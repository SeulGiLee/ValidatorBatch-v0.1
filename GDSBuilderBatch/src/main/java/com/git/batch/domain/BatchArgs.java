package com.git.batch.domain;

import com.beust.jcommander.Parameter;

/**
 *
 * @author SG.Lee
 * @Date 2018. 5. 4. 오전 10:10:42
 * */
public class BatchArgs {
	
	public static final String BASEDIR = "--basedir";
	@Parameter(names = BASEDIR, description = "최상위 폴더", required = false)
	private String baseDir="";
	
	public static final String VALTYPE = "--valtype";
	@Parameter(names = VALTYPE, description = "검수타입(수치지도, 지하시설물, 임상도)", required = false)
	private String valType="";

	public static final String PFLAG = "--pflag";
	@Parameter(names = PFLAG, description = "기존옵션여부", required = false)
	private String pFlag=""; 
	
	public static final String VALDTYPE = "--valdtype";
	@Parameter(names = VALDTYPE, description = "검수 세부타입(정위치, 구조화)", required = false)
	private String valDType=""; 
	
	public static final String FILETYPE = "--filetype";
	@Parameter(names = FILETYPE, description = "파일타입(dxf, ngi, shp)", required = true)
	private String fileType=""; 
	

	public static final String CIDX = "--cidx";
	@Parameter(names = CIDX, description = "옵션타입(1 - 수치지도1.0, 2 - 수치지도 2.0, 3 - 지하시설물 1.0, 4 - 지하시설물 2.0, 5 - 임상도)", required = true)
	private String cIdx=""; 

	public static final String LAYERDEFPATH = "--layerdefpath";
	@Parameter(names = LAYERDEFPATH, description = "레이어 정의 옵션 경로", required = true)
	private String layerDefPath="";
	
	public static final String VALOPTPATH = "--valoptpath";
	@Parameter(names = VALOPTPATH, description = "검수 옵션경로", required = true)
	private String valOptPath="";
	
	public static final String OBJFILEPATH = "--objfilepath";
	@Parameter(names = OBJFILEPATH, description = "검수 대상파일 경로", required = true)
	private String objFilePath="";
	
	public static final String CRS = "--crs";
	@Parameter(names = CRS, description = "좌표계", required = true)
	private String crs="";
	
	
	
	/**
	 * GET, SET 함수
	 * @author SG.Lee
	 * @Date 2018. 5. 4. 오전 10:10:43
	 * */
	public String getBaseDir() {
		return baseDir;
	}
	public void setBaseDir(String baseDir) {
		this.baseDir = baseDir;
	}
	public String getValType() {
		return valType;
	}
	public void setValType(String valType) {
		this.valType = valType;
	}
	public String getValDType() {
		return valDType;
	}
	public void setValDType(String valDType) {
		this.valDType = valDType;
	}
	public String getFileType() {
		return fileType;
	}
	public void setFileType(String fileType) {
		this.fileType = fileType;
	}
	public String getLayerDefPath() {
		return layerDefPath;
	}
	public void setLayerDefPath(String layerDefPath) {
		this.layerDefPath = layerDefPath;
	}
	public String getValOptPath() {
		return valOptPath;
	}
	public void setValOptPath(String valOptPath) {
		this.valOptPath = valOptPath;
	}
	public String getObjFilePath() {
		return objFilePath;
	}
	public void setObjFilePath(String objFilePath) {
		this.objFilePath = objFilePath;
	}
	public String getpFlag() {
		return pFlag;
	}
	public void setpFlag(String pFlag) {
		this.pFlag = pFlag;
	}
	public String getcIdx() {
		return cIdx;
	}
	public void setcIdx(String cIdx) {
		this.cIdx = cIdx;
	}
	public static String getValtype() {
		return VALTYPE;
	}
	public static String getValdtype() {
		return VALDTYPE;
	}
	public static String getLayerdefpath() {
		return LAYERDEFPATH;
	}
	public static String getValoptpath() {
		return VALOPTPATH;
	}
	public static String getObjfilepath() {
		return OBJFILEPATH;
	}
	
	public String getCrs() {
		return crs;
	}
	public void setCrs(String crs) {
		this.crs = crs;
	}
	
}
