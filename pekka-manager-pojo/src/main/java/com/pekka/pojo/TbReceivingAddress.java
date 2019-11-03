package com.pekka.pojo;

import java.io.Serializable;
import java.util.Date;

public class TbReceivingAddress implements Serializable {
	private Integer addressId;

	private String username;

	private String receiverName;

	private String receiverPhone;

	private String receiverMobile;

	private String receiverState;

	private String receiverCity;

	private String receiverDistrict;

	private String receiverAddress;

	private String receiverZip;

	private Date created;

	private Date updated;

	public TbReceivingAddress() {
	}

	public Integer getAddressId() {
		return addressId;
	}

	public void setAddressId(Integer addressId) {
		this.addressId = addressId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username == null ? null : username.trim();
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName == null ? null : receiverName.trim();
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone == null ? null : receiverPhone.trim();
	}

	public String getReceiverMobile() {
		return receiverMobile;
	}

	public void setReceiverMobile(String receiverMobile) {
		this.receiverMobile = receiverMobile == null ? null : receiverMobile.trim();
	}

	public String getReceiverState() {
		return receiverState;
	}

	public void setReceiverState(String receiverState) {
		this.receiverState = receiverState == null ? null : receiverState.trim();
	}

	public String getReceiverCity() {
		return receiverCity;
	}

	public void setReceiverCity(String receiverCity) {
		this.receiverCity = receiverCity == null ? null : receiverCity.trim();
	}

	public String getReceiverDistrict() {
		return receiverDistrict;
	}

	public void setReceiverDistrict(String receiverDistrict) {
		this.receiverDistrict = receiverDistrict == null ? null : receiverDistrict.trim();
	}

	public String getReceiverAddress() {
		return receiverAddress;
	}

	public void setReceiverAddress(String receiverAddress) {
		this.receiverAddress = receiverAddress == null ? null : receiverAddress.trim();
	}

	public String getReceiverZip() {
		return receiverZip;
	}

	public void setReceiverZip(String receiverZip) {
		this.receiverZip = receiverZip == null ? null : receiverZip.trim();
	}

	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	@Override
	public String toString() {
		return "TbReceivingAddress [addressId=" + addressId + ", username=" + username + ", receiverName="
				+ receiverName + ", receiverPhone=" + receiverPhone + ", receiverMobile=" + receiverMobile
				+ ", receiverState=" + receiverState + ", receiverCity=" + receiverCity + ", receiverDistrict="
				+ receiverDistrict + ", receiverAddress=" + receiverAddress + ", receiverZip=" + receiverZip
				+ ", created=" + created + ", updated=" + updated + "]";
	}
}