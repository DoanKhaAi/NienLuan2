package com.khaai.NuoiEm.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="payment")
public class Payment {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="payment_id")
    private Integer paymentID;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private MyUser user;

    @ManyToOne
    @JoinColumn(name = "month_year_id")
    private MonthYear monthYear;
    
    
    @Column(name="need_received_money")
   	private Integer needReceivedMoney;
    
    
    @Column(name="received_money")
   	private Integer receivedMoney;
    
    
    public Payment() {

	}
    
    
	public Payment(MyUser user, MonthYear monthYear, Integer needReceivedMoney, Integer receivedMoney) {
		super();
		this.user = user;
		this.monthYear = monthYear;
		this.needReceivedMoney = needReceivedMoney;
		this.receivedMoney = receivedMoney;
	}



	public Integer getPaymentID() {
		return paymentID;
	}

	public void setPaymentID(Integer paymentID) {
		this.paymentID = paymentID;
	}

	public MyUser getUser() {
		return user;
	}

	public void setUser(MyUser user) {
		this.user = user;
	}

	public MonthYear getMonthYear() {
		return monthYear;
	}

	public void setMonthYear(MonthYear monthYear) {
		this.monthYear = monthYear;
	}


	public Integer getNeedReceivedMoney() {
		return needReceivedMoney;
	}


	public void setNeedReceivedMoney(Integer needReceivedMoney) {
		this.needReceivedMoney = needReceivedMoney;
	}


	public Integer getReceivedMoney() {
		return receivedMoney;
	}


	public void setReceivedMoney(Integer receivedMoney) {
		this.receivedMoney = receivedMoney;
	}

    

}
