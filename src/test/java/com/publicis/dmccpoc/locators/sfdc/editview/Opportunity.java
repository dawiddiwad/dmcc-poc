package com.publicis.dmccpoc.locators.sfdc.editview;

import com.microsoft.playwright.Page;

public class Opportunity {
    public static final String LEAD_EMAIL = "//input[@name='Lead_Email__c']";
    public static final String SAVE_BUTTON = "//button[@name='SaveEdit']";

    public static void selectItemOnLookup(Page page, String lookupLabel, String lookupItem){
        page.fill("//input[ancestor::div[preceding-sibling::label[text()='"+lookupLabel+"']]]", lookupItem);
        page.click("//input[ancestor::div[preceding-sibling::label[text()='"+lookupLabel+"']]]");
        page.click("//lightning-base-combobox-formatted-text[@title='"+lookupItem+"']");
    }
}
