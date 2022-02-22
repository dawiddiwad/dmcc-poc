package com.publicis.dmccpoc.locators.sfdc;

import com.microsoft.playwright.Page;

public class StagesPath {
    public static final String CHANGE_CLOSED_STAGE_BUTTON = "//button[descendant::span[text()='Change Closed Stage']]";
    public static final String DONE_BUTTON = "//button[text()='Done']";


    public static void fillPicklistWithValue(Page page, String picklistLabel, String picklistValue){
        page.click("//button[@name='"+picklistLabel+"']");
        page.click("//lightning-base-combobox-item[descendant::span[text()='"+picklistValue+"']]");
    }
}
