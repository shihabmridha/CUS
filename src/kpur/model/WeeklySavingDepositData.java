package kpur.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class WeeklySavingDepositData {
	private final StringProperty Date;
	private final DoubleProperty BFbalance;
	private final DoubleProperty WeeklyDeposit;
	private final DoubleProperty Rebet;
	private final DoubleProperty MonthlyCollection;
	private final DoubleProperty SavingReturn;
	private final DoubleProperty TotalBalance;
	private final IntegerProperty transID;

	public WeeklySavingDepositData(String date,double bfbalance,double weeklydeposit,double rebet, double monthlycollection, double savingreturn, double TotalBalance,int transid){
		this.Date = new SimpleStringProperty(date);
		this.BFbalance = new SimpleDoubleProperty(bfbalance);
		this.WeeklyDeposit = new SimpleDoubleProperty(weeklydeposit);
		this.Rebet = new SimpleDoubleProperty(rebet);
		this.MonthlyCollection = new SimpleDoubleProperty(monthlycollection);
		this.SavingReturn = new SimpleDoubleProperty(savingreturn);
		this.TotalBalance = new SimpleDoubleProperty(TotalBalance);
		this.transID = new SimpleIntegerProperty(transid);
	}

	public void setDate(String date){
		this.Date.set(date);
	}
	public String getDate(){
		return this.Date.get();
	}

	public void setBfBalance(String bfbalance){
		this.Date.set(bfbalance);
	}
	public double getBfBalance(){
		return this.BFbalance.get();
	}

	public void setWeeklyDeposit(String weeklydeposit){
		this.Date.set(weeklydeposit);
	}
	public double getWeeklyDeposit(){
		return this.WeeklyDeposit.get();
	}

	public void setRebet(String rebet){
		this.Date.set(rebet);
	}
	public double getRebet(){
		return this.Rebet.get();
	}

	public void setMonthlyCollection(String monthlycollection){
		this.Date.set(monthlycollection);
	}
	public double getMonthlyCollection(){
		return this.MonthlyCollection.get();
	}

	public void setSavingReturn(String savingreturn){
		this.Date.set(savingreturn);
	}
	public double getSavingReturn(){
		return this.SavingReturn.get();
	}

	public void setTotalBalance(String TotalBalance){
		this.Date.set(TotalBalance);
	}
	public double getTotalBalance(){
		return this.TotalBalance.get();
	}

	public void setTransId(String transid){
		this.Date.set(transid);
	}
	public int getTransId(){
		return this.transID.get();
	}


}
