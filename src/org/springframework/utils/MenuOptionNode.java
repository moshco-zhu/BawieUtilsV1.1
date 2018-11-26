/**
 * 
 */
package org.springframework.utils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单项的节点
 * 
 * 
 *
 */
public class MenuOptionNode implements Serializable {

	// 内容窗体名称
	public final static String ContentFrameName = "centerFrame";

	// 编号
	private String id = null;

	// 标题
	private String name = null;

	// 是否打开
	private Boolean open = null;

	// 下级节点
	private List<MenuOptionNode> children = null;

	// 链接
	private String url = null;

	// 链接目标
	private String target = null;

	/**
	 * 构造函数
	 */
	public MenuOptionNode() {
	}

	/**
	 * 构造函数
	 * 
	 * @param menuOption
	 */
	public MenuOptionNode(Object menuOption) {
		id = getStrVal(menuOption, "id");
		name = getStrVal(menuOption, "title");
		open = false;
		url = getStrVal(menuOption, "url");
		if (url != null) {
			target = ContentFrameName;
		}
	}

	/**
	 * 读取字段的值
	 * 
	 * @param menuOption
	 * @return
	 */
	private static String getStrVal(Object menuOption, String fieldName) {
		if (menuOption == null) {
			return null;
		}
		try {
			Field field = menuOption.getClass().getDeclaredField(fieldName);
			boolean b = field.isAccessible();
			if (!b) {
				field.setAccessible(true);
			}
			Object value = field.get(menuOption);
			if (!b) {
				field.setAccessible(false);
			}
			if (value != null) {
				return value.toString();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 列表转换为树
	 * 
	 * @param menuOptionList
	 * @return
	 */
	public static List<MenuOptionNode> toTree(List<?> menuOptionList) {

		if (menuOptionList == null || menuOptionList.size() <= 0) {
			return null;
		}

		// 找寻顶级节点列表
		List<MenuOptionNode> topMenuOptionNodeList = null;
		for (int i = 0; i < menuOptionList.size();) {
			// 菜单项的对象
			Object menuOption = menuOptionList.get(i);
			if (getStrVal(menuOption, "superId") == null) {
				// 构建节点对象
				MenuOptionNode topNode = new MenuOptionNode(menuOption);
				// 创建列表
				if (topMenuOptionNodeList == null) {
					topMenuOptionNodeList = new ArrayList<MenuOptionNode>();
				}
				// 加入列表
				topMenuOptionNodeList.add(topNode);
				// 删除记录
				menuOptionList.remove(i);
			} else {
				// 下一记录
				i++;
			}
		}

		if (topMenuOptionNodeList != null) {
			for (int i = 0; i < topMenuOptionNodeList.size();) {
				// 节点对象
				MenuOptionNode node = topMenuOptionNodeList.get(i);
				// 构建下级节点的列表树
				List<MenuOptionNode> childNodeList = buildChildNodeList(menuOptionList, node);
				if (childNodeList != null && childNodeList.size() > 0) {
					// 设置下级节点的列表树
					node.setChildren(childNodeList);
					// 下一节点
					i++;
				} else {
					// 顶级节点没有下级节点就不显示
					topMenuOptionNodeList.remove(i);
				}
			}
		}

		// 返回函数的值
		return topMenuOptionNodeList;
	}

	/**
	 * 构建下级节点的列表树
	 * 
	 * @param menuOptionList
	 * @param node
	 * @return
	 */
	private static List<MenuOptionNode> buildChildNodeList(List<?> menuOptionList, MenuOptionNode node) {

		if (menuOptionList == null || menuOptionList.size() <= 0) {
			return null;
		}

		// 构建下级节点列表
		List<MenuOptionNode> menuOptionNodeList = null;
		for (int i = 0; i < menuOptionList.size(); i++) {
			// 菜单项的对象
			Object menuOption = menuOptionList.get(i);
			if (node.getId().equals(getStrVal(menuOption, "superId"))) {
				// 构建节点对象
				MenuOptionNode childNode = new MenuOptionNode(menuOption);
				// 创建列表
				if (menuOptionNodeList == null) {
					menuOptionNodeList = new ArrayList<MenuOptionNode>();
				}
				// 加入列表
				menuOptionNodeList.add(childNode);
			}
		}

		if (menuOptionNodeList != null) {
			for (MenuOptionNode childNode : menuOptionNodeList) {
				// 构建下级节点的列表树
				List<MenuOptionNode> nextNodeList = buildChildNodeList(menuOptionList, childNode);
				if (nextNodeList != null && nextNodeList.size() > 0) {
					// 设置下级节点的列表树
					childNode.setChildren(nextNodeList);
				}
			}
		}

		// 返回函数的值
		return menuOptionNodeList;

	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean getOpen() {
		return open;
	}

	public void setOpen(Boolean open) {
		this.open = open;
	}

	public List<MenuOptionNode> getChildren() {
		return children;
	}

	public void setChildren(List<MenuOptionNode> children) {
		this.children = children;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getTarget() {
		return target;
	}

	public void setTarget(String target) {
		this.target = target;
	}

}
