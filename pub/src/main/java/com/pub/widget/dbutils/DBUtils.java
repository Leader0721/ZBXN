package com.pub.widget.dbutils;

import com.pub.widget.dbutils.base.AbsBaseDbUtils;

public class DBUtils<T> extends AbsBaseDbUtils<T> {

	private Class<T> t;
	
	public DBUtils(Class<T> t) {
		this.t = t;
	}
	
	@Override
	public Class getClazz() {
		return t;
	}

}
