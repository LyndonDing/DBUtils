package com.dingfch.dbutils;

import com.dingfch.dbutils.db.annotation.Column;
import com.dingfch.dbutils.db.annotation.Id;
import com.dingfch.dbutils.db.annotation.Table;

@Table(name = "User")
public class User {
	@Id(column = "Id")
	public String mId;
	@Column(column = "Name")
	public String mName;
}
