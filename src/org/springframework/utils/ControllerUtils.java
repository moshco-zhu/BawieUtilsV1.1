/**
 * 
 */
package org.springframework.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller工具类
 * 
 *
 */
public class ControllerUtils implements Serializable {

	/**
	 * 删除指定的cookie
	 * 
	 * @param cookieName
	 */
	public static void removeCookie(String cookieName) {

		// 读取request和response的对象
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getResponse();

		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			String cookieName1 = cookie.getName();
			if (cookieName.equals(cookieName1)) {
				// 有效时长
				cookie.setMaxAge(0);// 单位是秒
				// 设置存储路径
				cookie.setPath(request.getContextPath());
				// 设值
				cookie.setValue("");
				// 保存到客户端
				response.addCookie(cookie);
			}
		}

	}

	/**
	 * cookie转换到session中
	 * 
	 * @param request
	 * @param cookieName
	 * @param sessionValueType
	 */
	public static void cookieToSession(HttpServletRequest request, String cookieName, Class<?> sessionValueType) {

		// 读取Session的对象
		HttpSession session = request.getSession(true);

		// cookie的名称
		Cookie[] cookies = request.getCookies();
		if (cookies == null || cookies.length <= 0) {
			return;
		}
		for (Cookie cookie : cookies) {
			// Cookie的名称
			String cookieName1 = cookie.getName();
			if (!cookieName.equals(cookieName1)) {
				continue;
			}
			// Cookie值
			String cookieValue1 = cookie.getValue();
			if (cookieValue1 == null || "".equals(cookieValue1)) {
				continue;
			}

			if (Integer.class.isAssignableFrom(sessionValueType)) {
				// 转换为整数型
				Integer intValue = Integer.parseInt(cookieValue1);
				session.setAttribute(cookieName, intValue);
			} else {
				// 字符串的类型
				session.setAttribute(cookieName, cookieValue1);
			}

		}

	}

	/**
	 * cookie转换到session中
	 * 
	 * @param cookieName
	 * @param sessionValueType
	 */
	public static void cookieToSession(String cookieName, Class<?> sessionValueType) {

		// 读取request和response的对象
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		// HttpServletResponse response = ((ServletRequestAttributes)
		// RequestContextHolder.getRequestAttributes())
		// .getResponse();
		// cookie转换到session中
		cookieToSession(request, cookieName, sessionValueType);
	}

	/**
	 * 保存到Cookie
	 * 
	 * @param cookieName
	 * @param cookieValue
	 */
	public static void saveCookie(String cookieName, String cookieValue) {

		// 读取request和response的对象
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getResponse();
		// 记住
		Cookie cookie = new Cookie(cookieName, cookieValue);
		// 有效时长
		cookie.setMaxAge(2 * 24 * 60 * 60);// 单位是秒
		// 设置存储路径
		cookie.setPath(request.getContextPath());
		// 保存到客户端
		response.addCookie(cookie);

	}

	/**
	 * 文件上传
	 * 
	 * @param application
	 * @param fileData
	 * @param filePath
	 * @throws Exception
	 */
	public static void uploadFile(ServletContext application, MultipartFile fileData, String filePath)
			throws Exception {

		// 没有文件数据
		if (fileData.isEmpty()) {
			return;
		}

		// 读取真实路径
		filePath = application.getRealPath(filePath);

		// 文件对象
		File headPhotoFile = new File(filePath);
		// 目录没有构建
		if (!headPhotoFile.getParentFile().exists()) {
			headPhotoFile.getParentFile().mkdirs();
		}

		// 文件存在删除
		if (!headPhotoFile.exists()) {
			headPhotoFile.delete();
		}

		// 保存文件
		fileData.transferTo(headPhotoFile);

	}

	/**
	 * 下载文件
	 * 
	 * @param application
	 * @param filePath
	 * @param response
	 * @throws Exception
	 */
	public static void downloadFile(ServletContext application, String filePath, HttpServletResponse response)
			throws Exception {

		// 读取真实路径
		filePath = application.getRealPath(filePath);
		// 文件对象
		File downloadFile = new File(filePath);

		// 设置头的信息
		response.setHeader("Content-Disposition", "attachment; filename=" + downloadFile.getName());
		// 设置内容类型
		response.setContentType("imge/jpeg;charset=utf-8");

		// 获取输出流的对象
		OutputStream os = response.getOutputStream();
		// 输入流的对象
		FileInputStream fis = new FileInputStream(downloadFile);

		// 缓存
		byte[] buffer = new byte[1024];

		while (true) {

			// 读取数据
			int dataLength = fis.read(buffer);
			if (dataLength <= 0) {
				break;
			}
			os.write(buffer, 0, dataLength);
			os.flush();

		}

		// 关闭输入输出
		fis.close();
		os.close();

	}

	/**
	 * 输出文件
	 * 
	 * @param application
	 * @param filePath
	 * @param response
	 * @throws Exception
	 */
	public static void outFile(ServletContext application, String filePath, HttpServletResponse response)
			throws Exception {

		// 读取真实路径
		filePath = application.getRealPath(filePath);
		// 文件对象
		File downloadFile = new File(filePath);

		// 设置内容类型
		response.setContentType("imge/jpeg;charset=utf-8");

		// 获取输出流的对象
		OutputStream os = response.getOutputStream();
		// 输入流的对象
		FileInputStream fis = new FileInputStream(downloadFile);

		// 缓存
		byte[] buffer = new byte[1024];

		while (true) {

			// 读取数据
			int dataLength = fis.read(buffer);
			if (dataLength <= 0) {
				break;
			}
			os.write(buffer, 0, dataLength);
			os.flush();

		}

		// 关闭输入输出
		fis.close();
		os.close();

	}

	/**
	 * 复制文件
	 * 
	 * @param application
	 * @param filePath1
	 * @param filePath2
	 * @throws Exception
	 */
	public static void copyFile(ServletContext application, String filePath1, String filePath2) {

		// 输入文件
		filePath1 = application.getRealPath(filePath1);
		File file1 = new File(filePath1);

		// 输入源文件对象必须存在
		if (!file1.exists()) {
			return;
		}

		// 输出文件
		filePath2 = application.getRealPath(filePath2);
		File file2 = new File(filePath2);

		// 构建输出目录
		if (!file2.getParentFile().exists()) {
			file2.getParentFile().mkdirs();
		}

		// 删除已经存在文件
		if (file2.exists()) {
			file2.delete();
		}

		// 文件输入流输出流
		FileInputStream fis = null;
		FileOutputStream fos = null;

		try {

			// 构建文件输出流输入流
			fis = new FileInputStream(file1);
			fos = new FileOutputStream(file2);

			// 缓存
			byte[] buffer = new byte[1024];

			while (true) {
				// 读取数据
				int length = fis.read(buffer);
				// 中断循环
				if (length <= 0) {
					break;
				}
				// 输出数据
				fos.write(buffer, 0, length);
				// 刷新
				fos.flush();
			}

			// 关闭资源
			fis.close();
			fis = null;
			fos.close();
			fos = null;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			// 关闭资源
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}

}
