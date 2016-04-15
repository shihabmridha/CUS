package kpur.model;

import javafx.beans.property.*;

public class WeeklyInvestData {
	private final StringProperty Date;
	private final DoubleProperty DisbudsAmount;
	private final DoubleProperty InstalmentAmount;
	private final DoubleProperty ProjectNo;
	private final DoubleProperty ODLastMonth;
	private final DoubleProperty Investment;
	private final DoubleProperty LastMonthOutStanding;
	private final DoubleProperty DisbudsCurrentMonth;
	private final DoubleProperty TotalBalance;
	private final DoubleProperty WeeklyInstolment;
	private final DoubleProperty TotalCollection;
	private final DoubleProperty TotalOutStanding;
	private final IntegerProperty transID;

	public WeeklyInvestData(String date, double disbudsamount, double instalmentamount, double projectno, double odlastmonth, double investment, double lastmonthos, double disbudscurrentmonth, double totalbalance, double weeklyinstolment, double totalcollection, double totaloutstanding, int transid){
		this.Date = new SimpleStringProperty(date);
		this.DisbudsAmount = new SimpleDoubleProperty(disbudsamount);
		this.InstalmentAmount = new SimpleDoubleProperty(instalmentamount);
		this.ProjectNo = new SimpleDoubleProperty(projectno);
		this.ODLastMonth = new SimpleDoubleProperty(odlastmonth);
		this.Investment = new SimpleDoubleProperty(investment);
		this.LastMonthOutStanding = new SimpleDoubleProperty(lastmonthos);
		this.DisbudsCurrentMonth = new SimpleDoubleProperty(disbudscurrentmonth);
		this.TotalBalance = new SimpleDoubleProperty(totalbalance);
		this.WeeklyInstolment = new SimpleDoubleProperty(weeklyinstolment);
		this.TotalCollection = new SimpleDoubleProperty(totalcollection);
		this.TotalOutStanding = new SimpleDoubleProperty(totaloutstanding);
		this.transID = new SimpleIntegerProperty(transid);
	}


	public void setDate(String date){
		this.Date.set(date);
	}
	public String getDate(){
		return this.Date.get();
	}

	public void setDisbudsAmount(String disbudsamount){
		this.Date.set(disbudsamount);
	}
	public double getDisbudsAmount(){
		return this.DisbudsAmount.get();
	}

	public void setInstalmentAmount(String instalmentamount){
		this.Date.set(instalmentamount);
	}
	public double getInstalmentAmount(){
		return this.InstalmentAmount.get();
	}

	public void setProjectNo(String projectno){
		this.Date.set(projectno);
	}
	public double getProjectNo(){
		return this.ProjectNo.get();
	}

	public void setODLastMonth(String odlastmonth){
		this.Date.set(odlastmonth);
	}
	public double getODLastMonth(){
		return this.ODLastMonth.get();
	}

	public void setInvestment(String investment){
		this.Date.set(investment);
	}
	public double getInvestment(){
		return this.Investment.get();
	}

	public void setLastMonthOutStanding(String lastmonthos){
		this.Date.set(lastmonthos);
	}
	public double getLastMonthOutStanding(){
		return this.LastMonthOutStanding.get();
	}

	public void setDisbudsCurrentMonth(String disbudscurrentmont){
		this.Date.set(disbudscurrentmont);
	}
	public double getDisbudsCurrentMonth(){
		return this.DisbudsCurrentMonth.get();
	}

	public void setTotalBalance(String totalbalance){
		this.Date.set(totalbalance);
	}
	public double getTotalBalance(){
		return this.TotalBalance.get();
	}

	public void setWeeklyInstolment(String weeklyinstolment){
		this.Date.set(weeklyinstolment);
	}
	public double getWeeklyInstolment(){
		return this.WeeklyInstolment.get();
	}

	public void setTotalCollection(String totalcollection){
		this.Date.set(totalcollection);
	}
	public double getTotalCollection(){
		return this.TotalCollection.get();
	}

	public void setTotalOutStanding(String totaloutstanding){
		this.Date.set(totaloutstanding);
	}
	public double getTotalOutStanding(){
		return this.TotalOutStanding.get();
	}

	public void setTransId(String transid){
		this.Date.set(transid);
	}
	public int getTransId(){
		return this.transID.get();
	}

}
