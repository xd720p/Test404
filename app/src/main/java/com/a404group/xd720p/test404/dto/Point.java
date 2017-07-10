package com.a404group.xd720p.test404.dto;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by xd720p on 09.07.2017.
 */

public class Point implements Serializable{

    private Date dateTime;
    private Double rate;

    public Point(Date dateTime, Double rate) {
        this.dateTime = dateTime;
        this.rate = rate;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

}
