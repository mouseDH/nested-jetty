package com.jetty.nested.demo;

import com.dianping.cat.Cat;
import com.dianping.cat.message.Message;
import com.dianping.cat.message.Transaction;

public class TestCat {
    public static void main(String[] args) {
        for (int i = 0; i < 5000; i++) {
            Transaction transaction = Cat.newTransaction("URL","test");
            Cat.logEvent("SQL.method","hello", Message.SUCCESS,"hello world");
            transaction.complete();
        }
    }
}
