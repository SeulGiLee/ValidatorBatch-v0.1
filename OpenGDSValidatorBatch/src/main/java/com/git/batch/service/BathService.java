package com.git.batch.service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;

import org.geotools.feature.SchemaException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.operation.TransformException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.git.batch.step.Progress;
import com.git.gdsbuilder.file.FileMeta;
import com.git.gdsbuilder.file.FileMetaList;
import com.git.gdsbuilder.file.writer.SHPFileWriter;
import com.git.gdsbuilder.parser.file.QAFileParser;
import com.git.gdsbuilder.parser.qa.QATypeParser;
import com.git.gdsbuilder.type.dt.collection.DTLayerCollection;
import com.git.gdsbuilder.type.dt.collection.DTLayerCollectionList;
import com.git.gdsbuilder.type.validate.error.ErrorLayer;
import com.git.gdsbuilder.type.validate.layer.QALayerTypeList;
import com.git.gdsbuilder.validator.collection.CollectionValidator;
import com.git.gdsbuilder.validator.fileReader.UnZipFile;

public class BathService {

	private static final String QA1DEFDIR;
	private static final String QA2DEFDIR;
	private static final String NM1LAYERDEF;
	private static final String NM5LAYERDEF;
	private static final String NM25LAYERDEF;
	private static final String UG1LAYERDEF;
	private static final String UG5LAYERDEF;
	private static final String UG25LAYERDEF;
	private static final String FR1LAYERDEF;
	private static final String FR5LAYERDEF;
	private static final String FR25LAYERDEF;
	private static final String NM1OPTIONDEF;
	private static final String NM5OPTIONDEF;
	private static final String NM25OPTIONDEF;
	private static final String UG1OPTIONDEF;
	private static final String UG5OPTIONDEF;
	private static final String UG25OPTIONDEF;
	private static final String FR1OPTIONDEF;
	private static final String FR5OPTIONDEF;
	private static final String FR25OPTIONDEF;

	public static int totalValidSize = 0;
	public static Progress pb;

	static {

		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		Properties properties = new Properties();
		try {
			properties.load(classLoader.getResourceAsStream("batch.properties"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		QA1DEFDIR = properties.getProperty("qa1DefDir");
		QA2DEFDIR = properties.getProperty("qa2DefDir");

		NM1LAYERDEF = properties.getProperty("nm1LayerDef");
		NM5LAYERDEF = properties.getProperty("nm5LayerDef");
		NM25LAYERDEF = properties.getProperty("nm25LayerDef");
		UG1LAYERDEF = properties.getProperty("ug1LayerDef");
		UG5LAYERDEF = properties.getProperty("ug5LayerDef");
		UG25LAYERDEF = properties.getProperty("ug25LayerDef");
		FR1LAYERDEF = properties.getProperty("fr1LayerDef");
		FR5LAYERDEF = properties.getProperty("fr5LayerDef");
		FR25LAYERDEF = properties.getProperty("fr25LayerDef");

		NM1OPTIONDEF = properties.getProperty("nm1OptionDef");
		NM5OPTIONDEF = properties.getProperty("nm5OptionDef");
		NM25OPTIONDEF = properties.getProperty("nm25OptionDef");
		UG1OPTIONDEF = properties.getProperty("ug1OptionDef");
		UG5OPTIONDEF = properties.getProperty("ug5OptionDef");
		UG25OPTIONDEF = properties.getProperty("ug25OptionDef");
		FR1OPTIONDEF = properties.getProperty("fr1OptionDef");
		FR5OPTIONDEF = properties.getProperty("fr5OptionDef");
		FR25OPTIONDEF = properties.getProperty("fr25OptionDef");
	}

	// file dir
	protected static String ERR_OUTPUT_DIR;
	protected static String ERR_FILE_DIR;
	protected static String ERR_OUTPUT_NAME;
	protected static String ERR_ZIP_DIR;

	// qa progress
	protected static int fileUpload = 1;
	protected static int validateProgresing = 2;
	protected static int validateSuccess = 3;
	protected static int validateFail = 4;

	Logger logger = LoggerFactory.getLogger(BathService.class);

	@SuppressWarnings("unchecked")
	public boolean validate(String baseDir, String valType, String pFlag, String valDType, String fileType,
			int category, String layerDefPath, String valOptPath, String objFilePath, String crs) throws Throwable {
		java.util.logging.Logger.getLogger("com.git.batch.service.BathService").setLevel(Level.OFF);
		boolean isSuccess = false;

		String qaVer = valDType;// 검수세부타입
		String qaType = valType;
		String prid = pFlag;
		String fileformat = fileType;

		int cIdx = category;
		String epsg = crs;

		String support = fileType;

		// preset
		if (prid.equals("nonset")) {
			if (qaVer.equals("qa1")) {
				switch (qaType) {
				case "nm1":
					layerDefPath = QA1DEFDIR + File.separator + NM1LAYERDEF;
					valOptPath = QA1DEFDIR + File.separator + NM1OPTIONDEF;
					break;
				case "nm5":
					layerDefPath = QA1DEFDIR + File.separator + NM5LAYERDEF;
					valOptPath = QA1DEFDIR + File.separator + NM5OPTIONDEF;
					break;
				case "nm25":
					layerDefPath = QA1DEFDIR + File.separator + NM25LAYERDEF;
					valOptPath = QA1DEFDIR + File.separator + NM25OPTIONDEF;
					break;
				case "ug1":
					layerDefPath = QA1DEFDIR + File.separator + UG1LAYERDEF;
					valOptPath = QA1DEFDIR + File.separator + UG1OPTIONDEF;
					break;
				case "ug5":
					layerDefPath = QA1DEFDIR + File.separator + UG5LAYERDEF;
					valOptPath = QA1DEFDIR + File.separator + UG5OPTIONDEF;
					break;
				case "ug25":
					layerDefPath = QA1DEFDIR + File.separator + UG25LAYERDEF;
					valOptPath = QA1DEFDIR + File.separator + UG25OPTIONDEF;
					break;
				case "fr1":
					layerDefPath = QA1DEFDIR + File.separator + FR1LAYERDEF;
					valOptPath = QA1DEFDIR + File.separator + FR1OPTIONDEF;
					break;
				case "fr5":
					layerDefPath = QA1DEFDIR + File.separator + FR5LAYERDEF;
					valOptPath = QA1DEFDIR + File.separator + FR5OPTIONDEF;
					break;
				case "fr25":
					layerDefPath = QA1DEFDIR + File.separator + FR25LAYERDEF;
					valOptPath = QA1DEFDIR + File.separator + FR25OPTIONDEF;
					break;
				default:
					break;
				}
			} else if (qaVer.equals("qa2")) {
				switch (qaType) {
				case "nm1":
					layerDefPath = QA2DEFDIR + File.separator + NM1LAYERDEF;
					valOptPath = QA2DEFDIR + File.separator + NM1OPTIONDEF;
					break;
				case "nm5":
					layerDefPath = QA2DEFDIR + File.separator + NM5LAYERDEF;
					valOptPath = QA2DEFDIR + File.separator + NM5OPTIONDEF;
					break;
				case "nm25":
					layerDefPath = QA2DEFDIR + File.separator + NM25LAYERDEF;
					valOptPath = QA2DEFDIR + File.separator + NM25OPTIONDEF;
					break;
				case "ug1":
					layerDefPath = QA2DEFDIR + File.separator + UG1LAYERDEF;
					valOptPath = QA2DEFDIR + File.separator + UG1OPTIONDEF;
					break;
				case "ug5":
					layerDefPath = QA2DEFDIR + File.separator + UG5LAYERDEF;
					valOptPath = QA2DEFDIR + File.separator + UG5OPTIONDEF;
					break;
				case "ug25":
					layerDefPath = QA2DEFDIR + File.separator + UG25LAYERDEF;
					valOptPath = QA2DEFDIR + File.separator + UG25OPTIONDEF;
					break;
				case "fr1":
					layerDefPath = QA2DEFDIR + File.separator + FR1LAYERDEF;
					valOptPath = QA2DEFDIR + File.separator + FR1OPTIONDEF;
					break;
				case "fr5":
					layerDefPath = QA2DEFDIR + File.separator + FR5LAYERDEF;
					valOptPath = QA2DEFDIR + File.separator + FR5OPTIONDEF;
					break;
				case "fr25":
					layerDefPath = QA2DEFDIR + File.separator + FR25LAYERDEF;
					valOptPath = QA2DEFDIR + File.separator + FR25OPTIONDEF;
					break;
				default:
					break;
				}
			}
		}

		// 옵션또는 파일이 제대로 넘어오지 않았을때 강제로 예외발생
		if (qaVer == null || qaType == null || prid == null) {
//			logger.info("다시 요청해주세요.");
			System.out.println("다시 요청해주세요.");
			return isSuccess;
		} else if (fileformat == null) {
			System.out.println("파일포맷을 설정해주세요.");
//			logger.info("파일포맷을 설정해주세요.");
			return isSuccess;
		} else {
			long time = System.currentTimeMillis();

			SimpleDateFormat dayTime = new SimpleDateFormat("yyMMdd_HHmmss");
			String cTimeStr = dayTime.format(new Date(time));

			// temp file 적용 시작
			// Windows Temp기본 경로 : C:\Users\GIT\AppData\Local\Temp\
			String defaultTempPath = System.getProperty("java.io.tmpdir") + "GeoDT";
			if (!new File(defaultTempPath).exists()) {
				new File(defaultTempPath).mkdirs();
			}

			// C:\Users\GIT\AppData\Local\Temp\GeoDT\...
			Path tmpBasedir = Files.createTempDirectory(Paths.get(defaultTempPath), "Validate_temp_");
			Path tmpFileunzipPath = Files.createTempDirectory(tmpBasedir, "unzipfiles");
			UnZipFile unZipFile = new UnZipFile(tmpFileunzipPath.toString());
			unZipFile.decompress(new File(objFilePath), cIdx);
			totalValidSize = unZipFile.getTotalSize();
			String comment = unZipFile.getFileState();
			
			/*  #####################################
			 *  yhg
			 *  적어도 해당 파일 형식이 한 개는 있는지 검사
			 */
			boolean checkExt = false;
			if(unZipFile.isFiles()) {			
				FileMetaList fList = unZipFile.getFileMetaList();
				for (FileMeta fMeta : fList) {
					if(fMeta.getFileName().endsWith(fileType)) {
						checkExt = true;
						break;
					}
				}
			} else if(unZipFile.isDir()){
				Map<String, FileMetaList> dirMetaList = unZipFile.getDirMetaList();
				Iterator<?> dirIterator = dirMetaList.keySet().iterator();
				while (dirIterator.hasNext()) {
					String dirPath = (String) dirIterator.next();
					FileMetaList metaList = dirMetaList.get(dirPath);
					for (FileMeta fileMeta : metaList) {
						if (fileMeta.getFileName().endsWith(fileType)) {
							checkExt = true;
							break;
						}
					}
				}
			}
			if(!checkExt) {
				System.out.println("검수 대상 파일에 " + fileType + "가 존재하지 않습니다.");
				throw new Throwable();
			}
			// #####################################

			

			// option parsing
			JSONParser jsonP = new JSONParser();
			JSONObject option = null;
			JSONArray layers = null;
			try {
				option = (JSONObject) ((Object) jsonP.parse(new FileReader(valOptPath)));
			} catch (ClassCastException e) {
				System.out.println("잘못된 옵션 파일입니다.");
				throw new Throwable();
			}
			try {
				layers = (JSONArray) ((Object) jsonP.parse(new FileReader(layerDefPath)));				
			} catch (ClassCastException e) {
				System.out.println("잘못된 레이어 정의 파일입니다.");
				throw new Throwable();
			}
			

			Object neatLine = option.get("border");
			String neatLineCode = null;
			if (neatLine != null) {
				JSONObject neatLineObj = (JSONObject) neatLine;
				neatLineCode = (String) neatLineObj.get("code");
			}
			

			// files
			QAFileParser parser = new QAFileParser(epsg, cIdx, support, unZipFile, neatLineCode);
			boolean parseTrue = parser.isTrue();
			if (!parseTrue) {
				comment += parser.getFileState();
				if (!comment.equals("")) {
//					logger.info(comment);
					System.out.println(comment);
				}
				deleteDirectory(tmpBasedir.toFile());
				return isSuccess;
			}

			DTLayerCollectionList collectionList = parser.getCollectionList();
			if (collectionList == null) {
				// 파일 다 에러
				comment += parser.getFileState();
				if (!comment.equals("")) {
//					logger.info(comment);
					System.out.println(comment);
				}
				deleteDirectory(tmpBasedir.toFile());
				return isSuccess;
			} else {
				// 몇개만 에러
				comment += parser.getFileState();
				if (!comment.equals("")) {
//					logger.info(comment);
					System.out.println(comment);
				}
			}
			JSONArray typeValidate = (JSONArray) option.get("definition");
			for (int j = 0; j < layers.size(); j++) {
				JSONObject lyrItem = (JSONObject) layers.get(j);
				Boolean isExist = false;
				for (int i = 0; i < typeValidate.size(); i++) {
					JSONObject optItem = (JSONObject) typeValidate.get(i);
					String typeName = (String) optItem.get("name");
					if (typeName.equals((String) lyrItem.get("name"))) {
						optItem.put("layers", (JSONArray) lyrItem.get("layers"));
						isExist = true;
					}
				}
				if (!isExist) {
					JSONObject obj = new JSONObject();
					obj.put("name", (String) lyrItem.get("name"));
					obj.put("layers", (JSONArray) lyrItem.get("layers"));
					typeValidate.add(obj);
				}
			}

			// options
			QATypeParser validateTypeParser = new QATypeParser(typeValidate);
			QALayerTypeList validateLayerTypeList = validateTypeParser.getValidateLayerTypeList();
			if (validateLayerTypeList == null) {
				comment += validateTypeParser.getComment();
				if (!comment.equals("")) {
//					logger.info(comment);
					System.out.println(comment);
				}
				deleteDirectory(tmpBasedir.toFile());
				return isSuccess;
			}
			validateLayerTypeList.setCategory(cIdx);

			// set err directory
			ERR_OUTPUT_DIR = baseDir + File.separator + "error";

			String entryName = unZipFile.getEntryName();
			ERR_OUTPUT_NAME = entryName + "_" + cTimeStr;

			ERR_FILE_DIR = ERR_OUTPUT_DIR + File.separator + ERR_OUTPUT_NAME;
			createFileDirectory(ERR_FILE_DIR);

			// excute validation
			isSuccess = executorValidate(collectionList, validateLayerTypeList, epsg);
			if (isSuccess) {
//				logger.info("검수 요청이 성공적으로 완료되었습니다.");
				System.out.println("검수 요청이 성공적으로 완료되었습니다.");
				// zip err shp directory
				/*
				 * zipFileDirectory(); InputStream inputStream = new
				 * FileInputStream(ERR_FILE_DIR + ".zip");
				 */
			} else {
				// insert validate state
//				logger.info("검수 요청이 실패했습니다.");
				System.out.println("검수 요청이 실패했습니다.");
			}
			validateTypeParser = null;
			validateLayerTypeList = null;
			unZipFile = null;
			parser = null;
			collectionList = null;
			deleteDirectory(tmpBasedir.toFile());
			return isSuccess;
		}
	}

	public static void showProgress() {

	}

	/**
	 * @author DY.Oh
	 * @Date 2018. 2. 6. 오전 10:12:22
	 * @param collectionList
	 * @param validateLayerTypeList
	 *            void
	 * @throws IOException
	 * @throws TransformException
	 * @throws FactoryException
	 * @throws SchemaException
	 * @throws NoSuchAuthorityCodeException
	 * @description
	 */
	private boolean executorValidate(DTLayerCollectionList collectionList, QALayerTypeList validateLayerTypeList,
			String epsg) {
		// 콘솔창에 로그 안찍히게 하기
		org.geotools.util.logging.Logging.getLogger("org").setLevel(Level.OFF);

		// 도엽별 검수 쓰레드 생성
		List<Future<?>> futures = new ArrayList<>();
		ExecutorService execService = Executors.newFixedThreadPool(3);
//		ExecutorService execService = Executors.newCachedThreadPool();

		pb = new Progress();
		for (final DTLayerCollection collection : collectionList) {
			pb.countTotalTask(validateLayerTypeList, collection,
					collectionList.getCloseLayerCollections(collection.getMapRule()));
		}
		pb.startProgress();

		for (final DTLayerCollection collection : collectionList) {
			Runnable runnable = new Runnable() {
				@Override
				public void run() {
					CollectionValidator validator = null;
					try {
						DTLayerCollectionList closeCollections = collectionList
								.getCloseLayerCollections(collection.getMapRule());
						validator = new CollectionValidator(collection, closeCollections, validateLayerTypeList);
					} catch (Exception e) {
						e.printStackTrace();
					}
					writeErrShp(epsg, validator);
				}
			};

			Future<?> future = execService.submit(runnable);
			futures.add(future);
		}
		// final long totalAmount = collectionList.getAllLayerSize();
		int futureCount = 0;

		for (int i = 0; i < futures.size(); i++) {
			Future<?> tmp = futures.get(i);
			try {
				tmp.get();
				futureCount++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		execService.shutdown();
		pb.terminate();
		// System.out.println("검수가 완료되었습니다.");

		return futureCount == collectionList.size();
	}

	/*
	 * private void zipFileDirectory() {
	 * 
	 * File directory = new File(ERR_FILE_DIR); List<String> fileList =
	 * getFileList(directory); try { ERR_ZIP_DIR = ERR_FILE_DIR + ".zip";
	 * FileOutputStream fos = new FileOutputStream(ERR_ZIP_DIR); ZipOutputStream zos
	 * = new ZipOutputStream(fos);
	 * 
	 * for (String filePath : fileList) { String name =
	 * filePath.substring(directory.getAbsolutePath().length() + 1,
	 * filePath.length()); ZipEntry zipEntry = new ZipEntry(name);
	 * zos.putNextEntry(zipEntry); FileInputStream fis = new
	 * FileInputStream(filePath); byte[] buffer = new byte[1024]; int length; while
	 * ((length = fis.read(buffer)) > 0) { zos.write(buffer, 0, length); }
	 * zos.closeEntry(); fis.close();
	 * 
	 * // 압축 후 삭제 File file = new File(filePath); file.delete(); } zos.close();
	 * fos.close(); directory.delete(); } catch (IOException e) {
	 * e.printStackTrace(); } }
	 */

	/*
	 * private List<String> getFileList(File directory) {
	 * 
	 * List<String> fileList = new ArrayList<>();
	 * 
	 * File[] files = directory.listFiles(); if (files != null && files.length > 0)
	 * { for (File file : files) { if (file.isFile()) {
	 * fileList.add(file.getAbsolutePath()); } else { getFileList(file); } } }
	 * return fileList; }
	 */

	private boolean writeErrShp(String epsg, CollectionValidator validator) {
		try {
			// 오류레이어 발행
			ErrorLayer errLayer = validator.getErrLayer();
			int errSize = errLayer.getErrFeatureList().size();
			// System.out.println(errSize);
			if (errSize > 0) {
				SHPFileWriter.writeSHP(epsg, errLayer, ERR_FILE_DIR + "\\" + errLayer.getCollectionName() + "_err.shp");
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	private void createFileDirectory(String directory) {
		File file = new File(directory);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	private void deleteDirectory(File dir) {

		if (dir.exists()) {
			File[] files = dir.listFiles();
			for (File file : files) {
				if (file.isDirectory()) {
					deleteDirectory(file);
				} else {
					file.delete();
				}
			}
		}
		dir.delete();
	}

	public File[] sortFileList(File[] files, final int compareType) {
		int COMPARETYPE_NAME = 0;
		int COMPARETYPE_DATE = 1;

		Arrays.sort(files, new Comparator<Object>() {
			@Override
			public int compare(Object object1, Object object2) {

				String s1 = "";
				String s2 = "";

				if (compareType == COMPARETYPE_NAME) {
					s1 = ((File) object1).getName();
					s2 = ((File) object2).getName();
				} else if (compareType == COMPARETYPE_DATE) {
					s1 = ((File) object1).lastModified() + "";
					s2 = ((File) object2).lastModified() + "";
				}
				return s1.compareTo(s2);
			}
		});

		return files;
	}

	/**
	 * 폴더 내에 폴더가 있을시 하위 폴더 탐색
	 * 
	 * @author SG.Lee
	 * @Date 2018. 4. 18. 오전 9:09:33
	 * @param source
	 *            void
	 */
	@SuppressWarnings("unused")
	private static void subDirList(String source) {
		File dir = new File(source);

		File[] fileList = dir.listFiles();
		List<File> indexFiles = new ArrayList<File>();

		for (int i = 0; i < fileList.length; i++) {
			File file = fileList[i];

			if (file.isFile()) {
				String filePath = file.getPath();
				String fFullName = file.getName();

				int Idx = fFullName.lastIndexOf(".");
				String _fileName = fFullName.substring(0, Idx);

				String parentPath = file.getParent(); // 상위 폴더 경로

				if (_fileName.endsWith("index")) {
					indexFiles.add(fileList[i]);// 도곽파일 리스트 add(shp,shx...)
				} else {
					if (_fileName.contains(".")) {
						moveDirectory(_fileName.substring(0, _fileName.lastIndexOf(".")), fFullName, filePath,
								parentPath);
					} else {
						moveDirectory(_fileName, fFullName, filePath, parentPath);
					}
				}
			}
		}

		fileList = dir.listFiles();
		// 도엽별 폴더 생성후 도곽파일 이동복사
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isDirectory()) {
				String message = "[디렉토리] ";
				message = fileList[i].getName();
				System.out.println(message);
				for (File iFile : indexFiles) {
					try {
						FileNio2Copy(iFile.getPath(), fileList[i].getPath() + File.separator + iFile.getName());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.getMessage();
					}
				}
			}
		}

		// index파일 삭제
		for (File iFile : indexFiles) {
			iFile.delete();
		}

		// 파일 사용후 객체초기화
		fileList = null;
		indexFiles = null;
	}

	/**
	 * 임상도 폴더 재생성
	 * 
	 * @author SG.Lee
	 * @Date 2018. 4. 18. 오후 1:24:16
	 * @param unzipFolder
	 *            void
	 */
	// private static File[] createCollectionFolders(File unzipFolder) {
	// boolean equalFlag = false; // 파일명이랑 압축파일명이랑 같을시 대비 flag값
	// String unzipName = unzipFolder.getName();
	//
	// if (unzipFolder.exists() == false) {
	// System.out.println("경로가 존재하지 않습니다");
	// }
	//
	// File[] fileList = unzipFolder.listFiles();
	// List<File> indexFiles = new ArrayList<File>();
	// String parentPath = unzipFolder.getParent(); // 상위 폴더 경로
	//
	// for (int i = 0; i < fileList.length; i++) {
	// if (fileList[i].isDirectory()) {
	// /*
	// * String message = "[디렉토리] "; message = fileList[ i
	// * ].getName(); System.out.println( message );
	// *
	// * subDirList( fileList[ i ].getPath());//하위 폴더 탐색
	// */ } else {
	// String filePath = fileList[i].getPath();
	// String fFullName = fileList[i].getName();
	//
	// int Idx = fFullName.lastIndexOf(".");
	// String _fileName = fFullName.substring(0, Idx);
	//
	// if (_fileName.equals(unzipName)) {
	// equalFlag = true;
	// }
	//
	// if (_fileName.endsWith("index")) {
	// indexFiles.add(fileList[i]);// 도곽파일 리스트 add(shp,shx...)
	// } else {
	// if (_fileName.contains(".")) {
	// moveDirectory(_fileName.substring(0, _fileName.lastIndexOf(".")), fFullName,
	// filePath,
	// parentPath);
	// } else {
	// moveDirectory(_fileName, fFullName, filePath, parentPath);
	// }
	// }
	// }
	// }
	//
	// fileList = unzipFolder.listFiles();
	//
	// // 도엽별 폴더 생성후 도곽파일 이동복사
	// for (int i = 0; i < fileList.length; i++) {
	// if (fileList[i].isDirectory()) {
	// for (File iFile : indexFiles) {
	// try {
	// FileNio2Copy(iFile.getPath(), fileList[i].getPath() + File.separator +
	// iFile.getName());
	// } catch (IOException e) {
	// // TODO Auto-generated catch block
	// System.out.println(e.getMessage());
	// }
	// }
	// }
	// }
	//
	// // index파일 삭제
	// for (File iFile : indexFiles) {
	// iFile.delete();
	// }
	//
	// // 원래 폴더 삭제
	// if (!equalFlag) {
	// unzipFolder.delete();
	// }
	//
	// // 파일 사용후 객체초기화
	// fileList = null;
	// indexFiles = null;
	//
	// return new File(parentPath).listFiles();
	// }

	/**
	 * 파일이동
	 * 
	 * @author SG.Lee
	 * @Date 2018. 4. 18. 오전 9:46:27
	 * @param folderName
	 * @param fileName
	 * @param beforeFilePath
	 * @param afterFilePath
	 * @return String
	 */
	private static String moveDirectory(String folderName, String fileName, String beforeFilePath,
			String afterFilePath) {
		String path = afterFilePath + "/" + folderName;
		String filePath = path + "/" + fileName;

		File dir = new File(path);

		if (!dir.exists()) { // 폴더 없으면 폴더 생성
			dir.mkdirs();
		}

		try {
			File file = new File(beforeFilePath);

			if (file.renameTo(new File(filePath))) { // 파일 이동
				return filePath; // 성공시 성공 파일 경로 return
			} else {
				return null;
			}
		} catch (Exception e) {
			e.getMessage();
			return null;
		}
	}

	/**
	 * 파일복사
	 * 
	 * @author SG.Lee
	 * @Date 2018. 4. 18. 오전 9:45:55
	 * @param source
	 * @param dest
	 * @throws IOException
	 *             void
	 */
	private static void FileNio2Copy(String source, String dest) throws IOException {
		Files.copy(new File(source).toPath(), new File(dest).toPath());
	}

}
