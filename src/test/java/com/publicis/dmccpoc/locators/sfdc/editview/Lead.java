package com.publicis.dmccpoc.locators.sfdc.editview;

import com.microsoft.playwright.Page;

public class Lead {
    public static final String FIRST_NAME = "//input[@name='firstName']";
    public static final String LAST_NAME = "//input[@name='lastName']";
    public static final String EMAIL = "//input[@name='Email']";
    public static final String COMPANY = "//input[@name='Company']";
    public static final String COUNTRY_CODE = "//input[@name='Country_code__c']";
    public static final String AREA_CODE = "//input[@name='Area_code__c']";
    public static final String PHONE_NUMBER = "//input[@name='Phone_No__c']";
    public static final String DESCRIPTION = "(//textarea[ancestor::div[preceding-sibling::label[text()='Description']]])[1]";

    public static final String SAVE_BUTTON = "//button[@name='SaveEdit']";

    public static void fillPicklistWithValue(Page page, String picklistLabel, String picklistValue){
        page.click("(//button[ancestor::div[preceding-sibling::label[text()='"+picklistLabel+"']]])[1]");
        page.click("(//lightning-base-combobox-item[@data-value='"+picklistValue+"'])[last()]");
    }
}
