package com.cn.xa.qyw.utils;

import android.text.TextUtils;
/**
 * Created by 409176 on 2016/7/23.
 * 文件操作类
 */
public class IFileUtils extends FileUtils {

	/**
	 * 文件存储类型，即各个存储路径存储的文件是干什么的
	 */
	public interface FileStoreType {
		/**
		 * 数据库存储路径
		 */
		public final int TEST = -1;

		/**
		 * 数据库存储路径
		 */
		public final int DB = 1;

		/**
		 * 我的头像存储路径
		 */
		public final int AVATAR = 2;

		/**
		 * 欢迎海报
		 */
		public final int WEL_POSTER = 3;

		/**
		 * 缓存图片信息
		 */
		public final int CACHE_IMGS = 4;

		/**
		 * 缓存图片信息
		 */
		public final int DOWNLOAD_FILE = 5;
	}

	/**
	 * 根据类型获取存储路径
	 */
	public static String getStorePath(int type) {
		//
		String storePath = "";

		// 数据库存储路径
		if (type == FileStoreType.DB) {
			storePath = mRootPath + "/db";

			// 我的头像保存地址
		} else if (type == FileStoreType.AVATAR) {
			storePath = mRootPath + "/avatar";

			// 欢迎页面海报图片存储路径
		} else if (type == FileStoreType.WEL_POSTER) {
			storePath = mRootPath + "/welPoster";

			// 测试路径
		} else if (type == FileStoreType.TEST) {
			storePath = mRootPath + "/test";

			// 缓存图片位置
		} else if (type == FileStoreType.CACHE_IMGS) {
			storePath = mRootPath + "/cacheImages";

			// 缓存下载文件位置
		} else if (type == FileStoreType.DOWNLOAD_FILE) {
			storePath = mRootPath + "/download";
		}

		// 创建路径
		createPath(storePath);

		return storePath;
	}

	/**
	 * 我的头像文件路径
	 */
	public static String getAvatarFilePath() {
		return getStorePath(FileStoreType.AVATAR) + "/avatar.jpg";
	}

	public static String getAvatarFilePathCut() {
		return getStorePath(FileStoreType.AVATAR) + "/avatarcut.jpg";
	}

	/**
	 * 缓存图片路径
	 */
	public static String getCacheImgsPath() {
		return getStorePath(FileStoreType.CACHE_IMGS);
	}

	/**
	 * Cache download files
	 */
	public static String getDownloadPath(String saveFileName) {

		if (TextUtils.isEmpty(saveFileName) || TextUtils.isEmpty(saveFileName.trim())) {
			return getStorePath(FileStoreType.DOWNLOAD_FILE);
		}

		return getStorePath(FileStoreType.DOWNLOAD_FILE) + "/" + saveFileName;
	}
}
