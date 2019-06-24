package com.example;

import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;

public class AutomatedDataDelegate implements JavaDelegate {

    @Override
    public void execute(DelegateExecution execution) {
        Date now = new Date();
        execution.setVariable("autoWelcomeTime", now);
        System.out.println("Faux call to backend for ["
            + execution.getVariable("fullName") + "]");
    }

}