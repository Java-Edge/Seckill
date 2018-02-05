package com.sss.redis;

public interface KeyPrefix {

     int expireSeconds();

     String getPrefix();

}