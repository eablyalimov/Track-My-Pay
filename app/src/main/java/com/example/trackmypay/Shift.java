package com.example.trackmypay;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Shift implements Parcelable, Serializable {

    private int id;
    private long date;
    private long startTime;
    private long endTime;
    private double hourlyRate;
    private long paidBreakMin;
    private long unpaidBreakMin;
    private double bonus;
    private double expenses;
    private String comments;
    private boolean isCommission;
    private double salesMade;
    private double target;


    Shift(int id, long date, long startTime, long endTime, double hourlyRate, long paidBreakMin, long unpaidBreakMin, double bonus, double expenses, String comments, boolean isCommission, double salesMade, double target)
    {
        this.id = id;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.comments = comments;
        this.hourlyRate = hourlyRate;
        this.paidBreakMin = paidBreakMin;
        this.unpaidBreakMin = unpaidBreakMin;
        this.bonus = bonus;
        this.expenses = expenses;
        this.isCommission = isCommission;
        this.salesMade = salesMade;
        this.target = target;


    }

    protected Shift(Parcel in) {
        id = in.readInt();
        date = in.readLong();
        startTime = in.readLong();
        endTime = in.readLong();
        hourlyRate = in.readDouble();
        paidBreakMin = in.readLong();
        unpaidBreakMin = in.readLong();
        bonus = in.readDouble();
        expenses = in.readDouble();
        comments = in.readString();
        isCommission = in.readByte() != 0;
        salesMade = in.readDouble();
        target = in.readDouble();
    }

    public static final Creator<Shift> CREATOR = new Creator<Shift>() {
        @Override
        public Shift createFromParcel(Parcel in) {
            return new Shift(in);
        }

        @Override
        public Shift[] newArray(int size) {
            return new Shift[size];
        }
    };

    public int getId()
    {
        return id;
    }

    public long getStartTime() { return startTime; }


    public long getDate() {
        return date;
    }


    public long getEndTime() {
        return endTime;
    }


    public String getComments() {
        return comments;
    }

    public long getPaidBreakMin() {
        return paidBreakMin;
    }

    public long getUnpaidBreakMin() {
        return unpaidBreakMin;
    }

    public double getHourlyRate() {
        return hourlyRate;
    }

    public double getBonus() {
        return bonus;
    }

    public double getExpenses() {
        return expenses;
    }

    public double getSalesMade() {
        return salesMade;
    }

    public double getTarget() {
        return target;
    }

    public boolean isCommission() {
        return isCommission;
    }

    public long calculateTimeWorked() {

        long timeWorked = this.getEndTime() - this.getStartTime() - this.unpaidBreakMin - this.paidBreakMin;
        return timeWorked;
    }

    public double calculateGrossPay()
    {
        long paidTimeMilliseconds = this.getEndTime() - this.getStartTime() - this.unpaidBreakMin;

        double grossPay = (paidTimeMilliseconds / 3600000.00) * this.getHourlyRate() + this.bonus - this.expenses;
        return grossPay;

    }

    public double calculateCommission(boolean deficit)
    {



        double commissionTot = 0;

        if (deficit)
        {
            if (getSalesMade() < getTarget())
            {
                commissionTot = getSalesMade() - getTarget();
            }
            else if (getSalesMade() > getTarget())
            {
                commissionTot = (getSalesMade() - getTarget()) * 0.1;
            }
        }

        else if (!deficit)
        {
            commissionTot = getSalesMade() * 0.1;
        }

        return commissionTot;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeLong(date);
        dest.writeLong(startTime);
        dest.writeLong(endTime);
        dest.writeDouble(hourlyRate);
        dest.writeLong(paidBreakMin);
        dest.writeLong(unpaidBreakMin);
        dest.writeDouble(bonus);
        dest.writeDouble(expenses);
        dest.writeString(comments);
        dest.writeByte((byte) (isCommission ? 1 : 0));
        dest.writeDouble(salesMade);
        dest.writeDouble(target);
    }
}
