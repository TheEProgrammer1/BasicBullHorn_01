package com.example.demo;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Entity
//@EntityListeners(AuditingEntityListener.class)
public class Message
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String content;


    private String posteddate;
    private String sentby;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


    public String getSentby() {
        return sentby;
    }

    public void setSentby(String sentby)
    {
            this.sentby = sentby;
    }


    public String getPosteddate() {
        return posteddate;
    }

    public void setPosteddate(String posteddate) {
        Date date = new Date();
        this.posteddate = (date.toString());
    }
}
