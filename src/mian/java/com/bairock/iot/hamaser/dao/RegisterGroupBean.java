package com.bairock.iot.hamaser.dao;

import java.util.HashMap;
import java.util.Map;

public class RegisterGroupBean {
	private String groupName;
	//昵称
	private String petName;
	//<input type="password" name="confirmPwd"/>
	private String groupPsd;
	private String groupConfirmPsd;
	
	private Map<String, String> errors = new HashMap<String, String>();
	
	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	/**
	 * 昵称
	 * @return
	 */
	public String getPetName() {
		return petName;
	}

	/**
	 * 昵称
	 * @param petName
	 */
	public void setPetName(String petName) {
		this.petName = petName;
	}

	public String getGroupPsd() {
		return groupPsd;
	}

	public void setGroupPsd(String groupPsd) {
		this.groupPsd = groupPsd;
	}

	public String getGroupConfirmPsd() {
		return groupConfirmPsd;
	}

	public void setGroupConfirmPsd(String groupConfirmPsd) {
		this.groupConfirmPsd = groupConfirmPsd;
	}
	
	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}

	public boolean validate() {
		if(validateGroupName(this.groupName)
				&& validateGroupPsd(this.groupPsd, this.groupConfirmPsd)){
			return true;
		}else{
			return false;
		}
	}
	
	private boolean validateGroupName(String name){
		boolean isOk = true;
		if (name == null || name.trim().equals("")) {
			isOk = false;
			putError("groupName", "组名不能为空");
		} else {
			if (!name.matches("^[a-zA-Z0-9_]{0,16}$")) {
				isOk = false;
				putError("groupName", "组名必须1-16个字符");
			}
		}
		return isOk;
	}
	
	private boolean validateGroupPsd(String psd, String confirmPsd){
		boolean isOk = true;
		if (psd == null || psd.trim().equals("")) {
			isOk = false;
			putError("groupPsd", "组密码不能为空");
		} else {
			if (!psd.matches("^[a-zA-Z_][a-zA-Z0-9_]{3,16}$")) {
				isOk = false;
				putError("groupPsd", "组密码必须为3-16个字符");
			}
		}
		if (confirmPsd == null || confirmPsd.trim().equals("")) {
			isOk = false;
			putError("groupConfirmPsd", "重复密码不能为空");
		} else {
			if (!confirmPsd.equals(psd)) {
				isOk = false;
				putError("groupConfirmPsd", "两次输入的密码不一致");
			}
		}
		return isOk;
	}
	
	private void putError(String key, String value){
		errors.put(key, value);
	}
}
