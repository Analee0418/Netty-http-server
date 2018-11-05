package com.lee.arphoto.action.cache;

import java.util.HashMap;
import java.util.Map;

import com.lee.arphoto.action.AbstractAction;

/**
 * 网络请求映射关系
 * 
 * @author xiaofei.lee(13651027213@163.com)
 *
 */
public final class ActionMappingCache {

	/**
	 * Action name - Action instance
	 */
	private final static Map<String, AbstractAction> actionInstanceMap = new HashMap<>();

	public static void _pushActionInstanceWithName(String actionName, AbstractAction action) {
		actionInstanceMap.put(actionName, action);
	}

	public static AbstractAction getActionHandler(String actionName) {
		return actionInstanceMap.get(actionName);
	}

}
