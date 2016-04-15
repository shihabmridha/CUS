package kpur.model;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ShareHolderDataClass {
	private final StringProperty date;
	private final IntegerProperty shareDepositValue;
	private final IntegerProperty profitValue;
	private final IntegerProperty shareWithdrolValue;
	private final IntegerProperty shareBalancelValue;
	private final IntegerProperty shareTransValue;

	public ShareHolderDataClass(){
		this(null,0,0,0,0,0);
	}

	public ShareHolderDataClass(String date,int shareDepositValue, int profitValue, int shareWithdrolValue, int shareBalancelValue, int shareTransValue){
		this.date = new SimpleStringProperty(date);
		this.shareDepositValue = new SimpleIntegerProperty(shareDepositValue);
		this.profitValue = new SimpleIntegerProperty(profitValue);
		this.shareWithdrolValue = new SimpleIntegerProperty(shareWithdrolValue);
		this.shareBalancelValue = new SimpleIntegerProperty(shareBalancelValue);
		this.shareTransValue = new SimpleIntegerProperty(shareTransValue);
	}

	public void setdate(String date) {
		this.date.set(date);
	}
	public String getdate() {
		return date.get();
	}


	public void setshareDepositValue(int shareDepositValue) {
		this.shareDepositValue.set(shareDepositValue);
	}
	public int getshareDepositValue() {
		return shareDepositValue.get();
	}


	public void setprofitValue(int profitValue) {
		this.profitValue.set(profitValue);
	}
	public int getprofitValue() {
		return profitValue.get();
	}


	public void setshareWithdrolValue(int shareWithdrolValue) {
		this.shareWithdrolValue.set(shareWithdrolValue);
	}
	public int getshareWithdrolValue() {
		return shareWithdrolValue.get();
	}


	public void setshareBalancelValue(int shareBalancelValue) {
		this.shareBalancelValue.set(shareBalancelValue);
	}
	public int getshareBalancelValue() {
		return shareBalancelValue.get();
	}

	public void setshareTransValue(int shareTransValue) {
		this.shareBalancelValue.set(shareTransValue);
	}
	public int getshareTransValue() {
		return shareTransValue.get();
	}

}
