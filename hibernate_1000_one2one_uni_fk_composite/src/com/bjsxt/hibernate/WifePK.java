package com.bjsxt.hibernate;

import java.io.Serializable;



public class WifePK implements Serializable {
	private int id;
	private String name;
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	//需要重写equals 和 hashCode 方法
	/*@Override
	public boolean equals(Object o){
		return false;
		
	}*/
	
	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return this.name.hashCode();
	}
	
}
