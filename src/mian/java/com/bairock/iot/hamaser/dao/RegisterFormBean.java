package com.bairock.iot.hamaser.dao;

import java.util.HashMap;
import java.util.Map;

public class RegisterFormBean {

	//<input type="text" name="userName"/>
	private String name;
	//<input type="password" name="password"/>
	private String psd;
	//<input type="password" name="confirmPwd"/>
	private String confirmPsd;
	//<input type="text" name="email"/>
	private String groupName;
	private String petName;
	//<input type="password" name="confirmPwd"/>
	private String groupPsd;
	private String groupConfirmPsd;
	private String email;

	
	private Map<String, String> errors = new HashMap<String, String>();

	public Map<String, String> getErrors() {
		return errors;
	}

	public void setErrors(Map<String, String> errors) {
		this.errors = errors;
	}

	public boolean validate() {
		if(validateManagerName(this.name)
				&& validateManagerPsd(this.psd, this.confirmPsd)){
			return true;
		}else{
			return false;
		}
	}
	
	private boolean validateManagerName(String name){
		boolean isOk = true;
		if (name == null || name.trim().equals("")) {
			isOk = false;
			putError("name", "用户名不能为空");
		} else {
			if (!name.matches("^[a-zA-Z0-9_]{3,16}$")) {
				isOk = false;
				putError("name", "用户名必须为英文字母或数字，长度不能小于6");
			}
		}
		return isOk;
	}
	
	private boolean validateManagerPsd(String psd, String confirmPsd){
		boolean isOk = true;
		if (psd == null || psd.trim().equals("")) {
			isOk = false;
			putError("psd", "密码不能为空");
		} else {
			if (!this.psd.matches("^[a-zA-Z_][a-zA-Z0-9_]{6,16}$")) {
				isOk = false;
				putError("psd", "密码必须以英文字母开头，并且只能是英文字母和数字，长度不能小于6");
			}
		}
		if (confirmPsd == null || confirmPsd.trim().equals("")) {
			isOk = false;
			putError("confirmPsd", "重复密码不能为空");
		} else {
			if (!confirmPsd.equals(psd)) {
				isOk = false;
				errors.put("confirmPsd", "两次输入得密码不一致");
			}
		}
		return isOk;
	}	
	
	private void putError(String key, String value){
		errors.put(key, value);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPsd() {
		return psd;
	}

	public void setPsd(String psd) {
		this.psd = psd;
	}

	public String getConfirmPsd() {
		return confirmPsd;
	}

	public void setConfirmPsd(String confirmPsd) {
		this.confirmPsd = confirmPsd;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getPetName() {
		return petName;
	}

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
