package com.shubh.eato.Models;

public class ordersModel {
    String PayableAmount, Status, Time, verificationCode, key;



   public ordersModel(){}

    public ordersModel(String payableAmount, String status, String time, String verificationCode) {
        this.PayableAmount = payableAmount;
        this.Status = status;
        this.Time = time;
        this.verificationCode = verificationCode;
    }
    public ordersModel(String key){
       this.key = key;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getPayableAmount() {
        return PayableAmount;
    }

    public void setPayableAmount(String payableAmount) {
        PayableAmount = payableAmount;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }
}
