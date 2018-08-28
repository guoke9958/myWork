package com.cn.xa.qyw.utils;

import android.content.Context;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;

/**
 * Created by 409176 on 2016/7/23.
 * 文件操作类
 */
public class FileUtils {

	/**
	 * Context
	 */
	protected static Context mContext;

	/**
	 * APP 文件存储根路径
	 */
	protected static String mRootPath = "";

	/**
	 * 创建 APP 文件存储根路径
	 */
	public static void initAppFolder(Context cxt, String packageName) {
		//
		mContext = cxt;

		try {
			// 当前存在外部存储器 且 可读可写
			if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
				mRootPath = mContext.getExternalFilesDir(null).getParentFile().getAbsolutePath();

				// Data
			} else {
				mRootPath = Environment.getDataDirectory().getPath() + "/data/" + cxt.getPackageName();
			}

			// 创建根目录
			createPath(mRootPath);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建存储路径
	 */
	protected static void createPath(String path) {
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
	}

	/**
	 * Get path end with "/"
	 */
	protected static String getPath(String path) {
		if (path.endsWith(File.separator)) {
			return path;
		}

		return path + "/";
	}

	/**
	 * 获取文件存储根路径
	 */
	public static String getRootPath() {
		return mRootPath;
	}

	/**
	 * New create Folder
	 * 
	 * @return 0 :创建成功
	 *         <p>
	 *         -1 非文件夹
	 *         <p>
	 *         -2 文件夹已存在
	 * 
	 */
	public static int createFolder(String folderPath) {
		return createFolder(new File(folderPath));
	}

	/**
	 * New create Folder
	 * 
	 * @return 0 :创建成功
	 *         <p>
	 *         -1 非文件夹
	 *         <p>
	 *         -2 文件夹已存在
	 * 
	 */
	public static int createFolder(File f) {
		if (f.isFile()) {
			return -1;
		}

		if (f.exists()) {
			return -2;
		}

		createPath(f.getPath());

		return 0;
	}

	/**
	 * 获取目标文件
	 */
	public static File getTargetFile(String targetPath, String fileName) {
		//
		File targetFile = new File(targetPath + "/" + fileName);
		if (targetFile.exists()) {
			String startName = "";
			String endName = "";
			if (targetFile.isFile()) {
				int idxSeperate = fileName.lastIndexOf(".");
				if (idxSeperate == -1) {
					startName = fileName;
					endName = "";
				} else {
					startName = fileName.substring(0, idxSeperate);
					endName = fileName.substring(idxSeperate);
				}
			} else {
				startName = fileName;
				endName = "";
			}

			//
			int loopNum = 1;
			boolean isLoop = true;
			while (isLoop) {
				for (int idx = loopNum; idx <= loopNum * 100; idx++) {
					fileName = startName + "(" + idx + ")" + endName;
					targetFile = new File(targetPath + "/" + fileName);
					if (!targetFile.exists()) {
						isLoop = false;
						break;
					}
				}
			}
		}

		return targetFile;
	}

	/**
	 * Delete File
	 * <p>
	 * if file is folder,delete it and its children
	 */
	public static void deleteFiles(File file) {
		//
		if (file == null || !file.exists()) {
			return;
		}

		//
		if (file.isFile()) {
			file.delete();
			return;
		}

		//
		if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0) {
				file.delete();
				return;
			}
			for (File f : childFile) {
				deleteFiles(f);
			}
			file.delete();
		}
	}

	/**
	 * @param path
	 *            文件目录
	 * @param oldname
	 *            原来的文件名
	 * @param newname
	 *            新文件名
	 * 
	 * @return 0 重命名成功
	 *         <p>
	 *         -1 重命名文件不存在
	 *         <p>
	 *         -2 新文件名和原文件名相同
	 *         <p>
	 *         -3 已经存在同名文件
	 *         <p>
	 *         -4 重命名信息输入非法
	 */
	public static int renameFile(String path, String oldname, String newname) {
		//
		if (StringUtils.isEmpty(oldname) || StringUtils.isEmpty(newname)) {
			return -4;
		}

		//
		if (oldname.equals(newname)) {
			return -2;
		}

		//
		File oldfile = new File(path + "/" + oldname);
		if (!oldfile.exists()) {
			return -1;
		}

		//
		File newfile = new File(path + "/" + newname);
		if (newfile.exists()) {
			return -3;
		}

		//
		oldfile.renameTo(newfile);
		return 0;
	}

	/**
	 * 
	 * 使用文件通道的方式复制文件,执行效率比较高
	 * 
	 * @param srcFile
	 *            源文件
	 * @param targetFile
	 *            复制到的新文件
	 */

	public static void copyByChannel(File srcFile, File targetFile) {

		FileInputStream fis = null;
		FileChannel fcIn = null;

		FileOutputStream fos = null;
		FileChannel fcOut = null;

		try {

			//
			fis = new FileInputStream(srcFile);
			fcIn = fis.getChannel();// 得到对应的文件通道

			//
			fos = new FileOutputStream(targetFile);
			fcOut = fos.getChannel();// 得到对应的文件通道

			// 连接两个通道，并且从"fcIn"通道读取，然后写入"fcOut"通道
			fcIn.transferTo(0, fcIn.size(), fcOut);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				fcIn.close();
				fos.close();
				fcOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 拷贝文件 , 普通的缓冲输入输出流，执行效率比较低
	 * 
	 * @param srcFile
	 *            源文件
	 * @param targetFile
	 *            复制到的新文件
	 */
	public static void copyByStream(File srcFile, File targetFile) {
		InputStream fis = null;
		OutputStream fos = null;

		try {
			fis = new BufferedInputStream(new FileInputStream(srcFile));
			fos = new BufferedOutputStream(new FileOutputStream(targetFile));

			byte[] buf = new byte[1024 * 2];
			int i;
			while ((i = fis.read(buf)) != -1) {
				fos.write(buf, 0, i);
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				fis.close();
				fos.close();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	/**
	 * 拷贝文件夹
	 * 
	 * @param srcPath
	 *            : 源文件夹路径
	 * @param targetPath
	 *            : 目标路径
	 */
	public static void copyFolder(String srcPath, String targetPath) {
		try {
			//
			createPath(targetPath);

			//
			File srcFolder = new File(srcPath);
			String[] strFNames = srcFolder.list();

			//
			File temp = null;
			for (int idx = 0; idx < strFNames.length; idx++) {
				temp = new File(getPath(srcPath) + strFNames[idx]);

				// Copy child files
				if (temp.isFile()) {
					copyByChannel(temp, new File(getPath(targetPath) + strFNames[idx]));

					// Loop copy child folders
				} else if (temp.isDirectory()) {
					copyFolder(getPath(srcPath) + strFNames[idx], getPath(targetPath) + strFNames[idx]);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 移动文件夹
	 * 
	 * @param srcPath
	 *            : 源文件夹路径
	 * @param targetPath
	 *            : 目标路径
	 */
	public static void moveFolder(String srcPath, String targetPath) {
		copyFolder(srcPath, targetPath);
		deleteFiles(new File(srcPath));
	}
}
