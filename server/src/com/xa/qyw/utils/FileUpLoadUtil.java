package com.xa.qyw.utils;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传帮助类
 * 
 * @author 张奎
 */
public class FileUpLoadUtil {

	private FileUpLoadUtil() {
	}

	//
//	 private static String DOWNLOAD = "D:" + File.separator + "download" +
//	 File.separator + "yht";

	public static String DOWNLOAD = File.separator + "opt" + File.separator
			+ "upload";
	private static String APP = "app";
	private static String USER_PHOTO = "userphoto";
	private static String RELEASE_PIC = "release";
	private static String VIDEO = "video";
	private static String VIDEO_IMAGE = "videoimage";

	/**
	 * 设置默认上传路径
	 * 
	 * @param path
	 */
	public static void setBaseDownloadPath(String path) {
		DOWNLOAD = path;
	}

	/**
	 * 上传图片文件(重载[返回全路径])
	 * 
	 * @param path
	 *            上传路径
	 * @param file
	 *            图片文件
	 * @return
	 * @throws java.io.IOException
	 */
	public static String fileupload(String path, MultipartFile file)
			throws IOException {
		String uploadFileName = file.getOriginalFilename();
		String id = UUID.randomUUID().toString().replace("-", "");
		String saveName = null;
		if (uploadFileName == null) {
			saveName = id;
		} else {
			saveName = id
					+ uploadFileName.substring(uploadFileName.lastIndexOf("."));
		}

		File saveFile = new File(path + saveName);
		if (!saveFile.getParentFile().exists()) {
			saveFile.getParentFile().mkdirs();
		}

		// 写入文件
		FileUtils.writeByteArrayToFile(saveFile, file.getBytes());
		return saveName;
	}

	/**
	 * 上传图片文件(重载[返回全路径])
	 * 
	 * @param path
	 *            上传路径
	 * @param file
	 *            图片文件
	 * @return
	 * @throws java.io.IOException
	 */
	public static String fileAppUpload(String path, MultipartFile file)
			throws IOException {
		String uploadFileName = file.getOriginalFilename();
		String id = UUID.randomUUID().toString().replace("-", "");;
		String saveName = null;
		if (uploadFileName == null) {
			saveName = id;
		} else {
			saveName = id
					+ uploadFileName.substring(uploadFileName.lastIndexOf("."));
		}

		File saveFile = new File(path + saveName);
		if (!saveFile.getParentFile().exists()) {
			saveFile.getParentFile().mkdirs();
		}

		File newFile = new File(path + "a_qiuyiwangyht.apk");

		if (newFile.exists()) {
			newFile.delete();
		}

		// 写入文件
		FileUtils.writeByteArrayToFile(saveFile, file.getBytes());
		FileUtils.writeByteArrayToFile(newFile, file.getBytes());
		return saveName;
	}

	/**
	 * 上传图片文件(重载[返回全路径])
	 * 
	 * @param path
	 *            上传路径
	 * @param file
	 *            图片文件
	 * @return
	 * @throws java.io.IOException
	 */
	public static String fileupload(String path, FileItem file)
			throws IOException {

		String uploadFileName = file.getName();
		String id = UUID.randomUUID().toString().replace("-", "");;
		String saveName = null;
		if (uploadFileName == null) {
			saveName = id;
		} else {
			saveName = id
					+ uploadFileName.substring(uploadFileName.lastIndexOf("."));
		}

		File saveFile = new File(path + saveName);
		if (!saveFile.getParentFile().exists()) {
			saveFile.getParentFile().mkdirs();
		}
		// 写入文件
		FileUtils.writeByteArrayToFile(saveFile, file.get());
		file.delete();
		return saveName;
	}

	/**
	 * 上传图片文件(重载[返回全路径])
	 * 
	 * @param path
	 *            上传路径
	 * @param file
	 *            图片文件
	 * @return
	 * @throws java.io.IOException
	 */
	public static String fileuploadApk(String path, FileItem file)
			throws IOException {

		String uploadFileName = file.getName();
		String id = "app-debug";
		String saveName = null;
		if (uploadFileName == null) {
			saveName = id;
		} else {
			saveName = id
					+ uploadFileName.substring(uploadFileName.lastIndexOf("."));
		}

		File saveFile = new File(path + saveName);
		if (!saveFile.getParentFile().exists()) {
			saveFile.getParentFile().mkdirs();
		}
		// 写入文件
		FileUtils.writeByteArrayToFile(saveFile, file.get());
		file.delete();
		return saveName;
	}
	
	public static String fileVideoUpload(String path, MultipartFile file)
			throws IOException {
		String uploadFileName = file.getOriginalFilename();
		String id = UUID.randomUUID().toString().replace("-", "");;
		String saveName = null;
		if (uploadFileName == null) {
			saveName = id;
		} else {
			saveName = id
					+ uploadFileName.substring(uploadFileName.lastIndexOf("."));
		}

		File saveFile = new File(path + saveName);
		if (!saveFile.getParentFile().exists()) {
			saveFile.getParentFile().mkdirs();
		}

		// 写入文件
		FileUtils.writeByteArrayToFile(saveFile, file.getBytes());
		return saveName;
	}
	
	public static String fileVideoImageUpload(String path, MultipartFile file)
			throws IOException {
		String uploadFileName = file.getOriginalFilename();
		String id = UUID.randomUUID().toString().replace("-", "");;
		String saveName = null;
		if (uploadFileName == null) {
			saveName = id;
		} else {
			saveName = id
					+ uploadFileName.substring(uploadFileName.lastIndexOf("."));
		}

		File saveFile = new File(path + saveName);
		if (!saveFile.getParentFile().exists()) {
			saveFile.getParentFile().mkdirs();
		}

		// 写入文件
		FileUtils.writeByteArrayToFile(saveFile, file.getBytes());
		return saveName;
	}
	

	/**
	 * 获取顶级上传路径
	 * 
	 * @param request
	 * @return
	 */
	public static String getUserPhotoBaseUpLoadRoot(HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("");
		return DOWNLOAD + File.separator + USER_PHOTO + File.separator;
	}
	
	public static String getUserPhotoBaseUpLoadRoot() {
		return DOWNLOAD + File.separator + USER_PHOTO + File.separator;
	}

	/**
	 * 获取顶级上传路径
	 * 
	 * @param request
	 * @return
	 */
	public static String getUserReleaseBaseUpLoadRoot(HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("");
		return DOWNLOAD + File.separator + RELEASE_PIC + File.separator;
	}

	/**
	 * 获取顶级上传路径
	 * 
	 * @param request
	 * @return
	 */
	public static String getAppBaseUpLoadRoot(HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("");
		return DOWNLOAD + File.separator + APP + File.separator;
	}
	
	public static String getVideoBaseUpLoadRoot(HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("");
		return DOWNLOAD + File.separator + VIDEO + File.separator;
	}
	
	public static String getVideoImageBaseUpLoadRoot(HttpServletRequest request) {
		String path = request.getSession().getServletContext().getRealPath("");
		return DOWNLOAD + File.separator + VIDEO_IMAGE + File.separator;
	}
	
}
