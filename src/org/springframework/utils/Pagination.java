/**
 * 
 */
package org.springframework.utils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

/**
 * 分页
 * 
 *
 */
public class Pagination<T> implements Serializable {

	// 页面序号
	private Integer pageIndex = 1;

	// 页面尺寸
	private Integer pageSize = 2;

	// 当前页的列表
	private List<T> currentPageList = null;

	// 总记录数
	private Integer totalRecord = 0;

	// 总计页数
	private Integer totalPage = 0;

	// 查询参数集合
	private Map<String, Object> queryParamMap = null;

	// 返回参数集合
	private Map<String, Object> returnParamMap = null;

	// url的参数
	private StringBuilder urlParamsBuilder = null;

	// 表格宽度
	private int tableWidth = 700;

	// 请求对象
	private HttpServletRequest request = null;

	// 查询的url
	private String actionUrl = null;

	/**
	 * 构造函数
	 * 
	 * @param request
	 * @param tableWidth
	 */
	public Pagination(HttpServletRequest request, int tableWidth) {
		this.request = request;
		actionUrl = request.getRequestURI();
		this.tableWidth = tableWidth;
	}

	/**
	 * 添加查询参数
	 * 
	 * @param paramName
	 * @param paramValue
	 * @param pattern
	 */
	public void addDateTimeQueryParam(String paramName, Date paramValue, String pattern) {

		if (paramValue != null) {

			if (urlParamsBuilder == null) {
				urlParamsBuilder = new StringBuilder();
			}

			// 日期时间类型
			urlParamsBuilder.append("&" + paramName + "=");
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			urlParamsBuilder.append(sdf.format(paramValue));
			// 返回参数集合
			if (returnParamMap == null) {
				returnParamMap = new HashMap<String, Object>();
			}
			returnParamMap.put(paramName, paramValue);

			// 封装查询条件
			if (queryParamMap == null) {
				queryParamMap = new HashMap<String, Object>();
			}
			queryParamMap.put(paramName, paramValue);

		}

	}

	/**
	 * 添加查询参数
	 * 
	 * @param paramName
	 * @param paramValue
	 */
	public void addQueryParam(String paramName, Object paramValue) {
		if (paramValue != null && !"".equals(paramValue) && !"%null%".equals(paramValue)) {
			if (urlParamsBuilder == null) {
				urlParamsBuilder = new StringBuilder();
			}
			if (paramValue instanceof String) {
				String paramValueStr = paramValue.toString();
				try {
					// 解决中文乱码
					//paramValueStr = new String(paramValueStr.getBytes("iso-8859-1"), "utf-8");
				} catch (Exception e) {
					e.printStackTrace();
				}
				// url的参数
				urlParamsBuilder.append("&" + paramName + "=");
				urlParamsBuilder.append(paramValueStr.replaceAll("[%]", ""));
				// 返回参数集合
				if (returnParamMap == null) {
					returnParamMap = new HashMap<String, Object>();
				}
				returnParamMap.put(paramName, paramValueStr.replaceAll("[%]", ""));
				// 查询参数
				paramValue = paramValueStr;
			} else if (paramValue instanceof Date) {
				// 日期类型
				urlParamsBuilder.append("&" + paramName + "=");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				urlParamsBuilder.append(sdf.format(paramValue));
				// 返回参数集合
				if (returnParamMap == null) {
					returnParamMap = new HashMap<String, Object>();
				}
				returnParamMap.put(paramName, paramValue);
			} else {
				// url的参数
				urlParamsBuilder.append("&" + paramName + "=");
				urlParamsBuilder.append(paramValue);
				// 返回参数集合
				if (returnParamMap == null) {
					returnParamMap = new HashMap<String, Object>();
				}
				returnParamMap.put(paramName, paramValue);
			}
			// 封装查询条件
			if (queryParamMap == null) {
				queryParamMap = new HashMap<String, Object>();
			}
			queryParamMap.put(paramName, paramValue);

		}
	}

	/**
	 * 读取查询参数
	 * 
	 * @param paramName
	 * @return
	 */
	public Object getQueryParam(String paramName) {
		if (queryParamMap == null) {
			return null;
		}
		return queryParamMap.get(paramName);
	}

	/**
	 * 读取参数参数
	 * 
	 * @return
	 */
	public Map<String, Object> getQueryParamMap() {
		if (queryParamMap == null) {
			queryParamMap = new HashMap<String, Object>();
		} else {
			queryParamMap = new HashMap<String, Object>(queryParamMap);
		}
		queryParamMap.put("beginIndex", getBeginIndex());
		queryParamMap.put("pageSize", getPageSize());
		return queryParamMap;
	}

	public Integer getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(Integer pageIndex) {
		if (pageIndex == null || pageIndex <= 0) {
			this.pageIndex = 1;
			return;
		}
		this.pageIndex = pageIndex;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if (pageSize == null || pageSize <= 0) {
			this.pageSize = 2;
			return;
		}
		this.pageSize = pageSize;
	}

	public List<T> getCurrentPageList() {
		return currentPageList;
	}

	public void setCurrentPageList(List<T> currentPageList) {
		this.currentPageList = currentPageList;
	}

	public Integer getTotalRecord() {
		return totalRecord;
	}

	public int getTableWidth() {
		return tableWidth;
	}

	public void setTableWidth(int tableWidth) {
		this.tableWidth = tableWidth;
	}

	public HttpServletRequest getRequest() {
		return request;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}

	public String getActionUrl() {
		return actionUrl;
	}

	public void setActionUrl(String actionUrl) {
		this.actionUrl = actionUrl;
	}

	public void setTotalRecord(Integer totalRecord) {

		if (totalRecord == null) {
			throw new RuntimeException("总记录数不能为空。");
		}

		// 总记录数
		this.totalRecord = totalRecord;
		// 计算总计页数
		totalPage = totalRecord / pageSize;
		if (totalRecord % pageSize > 0) {
			totalPage++;
		}
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	/**
	 * 起始点的序号
	 * 
	 * @return
	 */
	public Integer getBeginIndex() {
		if (pageIndex == null || pageIndex <= 0 || pageSize == 0 || pageSize <= 0) {
			return 0;
		}
		return (pageIndex - 1) * pageSize;
	}

	/**
	 * 返回函数
	 * 
	 * @return
	 */
	public Map<String, Object> getParams() {
		if (returnParamMap == null) {
			returnParamMap = new HashMap<String, Object>();
		} else {
			returnParamMap = new HashMap<String, Object>(returnParamMap);
		}
		return returnParamMap;
	}

	/**
	 * 输出统计信息
	 * 
	 * @return
	 */
	public String getInfo() {
		return outTotalInfo();
	}

	/**
	 * 输出统计信息
	 * 
	 * @param tableWidth
	 * @return
	 */
	public String outTotalInfo() {
		StringBuilder totalInfoBuilder = new StringBuilder();
		totalInfoBuilder.append("<table width=\"" + tableWidth + "\" border=\"1\" cellpadding=\"5\"><tr><td>");
		totalInfoBuilder.append("找到" + getTotalRecord() + "条记录，");
		totalInfoBuilder.append("每页显示" + getPageSize() + "条，");
		totalInfoBuilder.append("共计" + getTotalPage() + "页，");
		totalInfoBuilder.append("当前是第" + getPageIndex() + "页，");
		totalInfoBuilder.append("当前页的列表如下：");
		totalInfoBuilder.append("</td></tr></table>");
		String totalInfo = totalInfoBuilder.toString();
		return totalInfo;
	}

	/**
	 * 输出分页的条
	 * 
	 * @return
	 */
	public String getBar() {
		return outPaginationBar();
	}

	/**
	 * 输出分页的条
	 * 
	 * @param request
	 * @param actionUrl
	 * @param tableWidth
	 * @return
	 */
	public String outPaginationBar() {
		StringBuilder paginationBarBuilder = new StringBuilder();
		if (getTotalRecord() > 0) {
			// url的参数
			String urlParams = "";
			if (urlParamsBuilder != null) {
				urlParams = urlParamsBuilder.toString();
			}
			paginationBarBuilder.append("<table width=\"" + tableWidth + "\" border=\"1\" cellpadding=\"5\"><tr>");
			paginationBarBuilder.append("<td>");
			// 页面导航
			if (getPageIndex() > 1) {
				paginationBarBuilder.append("<a href=\"");
				// paginationBarBuilder.append(request.getContextPath());
				// paginationBarBuilder.append("/");
				paginationBarBuilder.append(actionUrl);
				paginationBarBuilder.append("?pageIndex=1&pageSize=");
				paginationBarBuilder.append(getPageSize());
				paginationBarBuilder.append(urlParams);
				paginationBarBuilder.append("\">首页</a>&nbsp;&nbsp;");
				paginationBarBuilder.append("<a href=\"");
				// paginationBarBuilder.append(request.getContextPath());
				// paginationBarBuilder.append("/");
				paginationBarBuilder.append(actionUrl);
				paginationBarBuilder.append("?pageIndex=" + (getPageIndex() - 1) + "&pageSize=");
				paginationBarBuilder.append(getPageSize());
				paginationBarBuilder.append(urlParams);
				paginationBarBuilder.append("\">上一页</a>");
			}
			if (getPageIndex() < getTotalPage()) {
				paginationBarBuilder.append("<a href=\"");
				// paginationBarBuilder.append(request.getContextPath());
				// paginationBarBuilder.append("/");
				paginationBarBuilder.append(actionUrl);
				paginationBarBuilder.append("?pageIndex=" + (getPageIndex() + 1) + "&pageSize=");
				paginationBarBuilder.append(getPageSize());
				paginationBarBuilder.append(urlParams);
				paginationBarBuilder.append("\">下一页</a>&nbsp;&nbsp;");
				paginationBarBuilder.append("<a href=\"");
				// paginationBarBuilder.append(request.getContextPath());
				// paginationBarBuilder.append("/");
				paginationBarBuilder.append(actionUrl);
				paginationBarBuilder.append("?pageIndex=" + (getTotalPage()) + "&pageSize=");
				paginationBarBuilder.append(getPageSize());
				paginationBarBuilder.append(urlParams);
				paginationBarBuilder.append("\">尾页</a>");
			}
			paginationBarBuilder.append("</td><td>");
			// 去第几页
			paginationBarBuilder.append("<form name=\"goPageForm\" method=\"post\" action=\"");
			// paginationBarBuilder.append(request.getContextPath());
			// paginationBarBuilder.append("/");
			paginationBarBuilder.append(actionUrl);
			paginationBarBuilder.append("?pageSize=");
			paginationBarBuilder.append(getPageSize());
			paginationBarBuilder.append(urlParams);
			paginationBarBuilder.append("\">");
			paginationBarBuilder.append("去第<input type=\"text\" name=\"pageIndex\" value=\"" + getPageIndex() + "\" ");
			paginationBarBuilder.append("style=\"width: 30px\" />页<button type=\"submit\">Go</button>");
			paginationBarBuilder.append("</form>");
			paginationBarBuilder.append("</td><td>");
			// 每页显示几条
			paginationBarBuilder.append("<form name=\"changePageSize\" method=\"post\" action=\"");
			// paginationBarBuilder.append(request.getContextPath());
			// paginationBarBuilder.append("/");
			paginationBarBuilder.append(actionUrl);
			paginationBarBuilder.append("?pageIndex=");
			paginationBarBuilder.append(getPageIndex());
			paginationBarBuilder.append(urlParams);
			paginationBarBuilder.append("\">");
			paginationBarBuilder.append("每页显示<select name=\"pageSize\" onchange=\"submit();\" width=\"80\">");
			paginationBarBuilder.append("<option value=\"2\"");
			if (pageSize == 2) {
				paginationBarBuilder.append(" selected");
			}
			paginationBarBuilder.append(">2</option>");
			paginationBarBuilder.append("<option value=\"5\"");
			if (pageSize == 5) {
				paginationBarBuilder.append(" selected");
			}
			paginationBarBuilder.append(">5</option>");
			paginationBarBuilder.append("<option value=\"10\"");
			if (pageSize == 10) {
				paginationBarBuilder.append(" selected");
			}
			paginationBarBuilder.append(">10</option>");
			paginationBarBuilder.append("<option value=\"20\"");
			if (pageSize == 20) {
				paginationBarBuilder.append(" selected");
			}
			paginationBarBuilder.append(">20</option>");
			paginationBarBuilder.append("<option value=\"50\"");
			if (pageSize == 50) {
				paginationBarBuilder.append(" selected");
			}
			paginationBarBuilder.append(">50</option>");
			paginationBarBuilder.append("</select>条");
			paginationBarBuilder.append("</form>");
			paginationBarBuilder.append("</td></tr></table>");
		}
		String paginationBar = paginationBarBuilder.toString();
		return paginationBar;
	}

}
