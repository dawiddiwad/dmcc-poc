package com.publicis.dmccpoc.locators.sfdc;

public class LeadConvert {
    public static final String CONVERT_BUTTON = "//button[descendant::span[text()='Convert']]";
    public static final String OPPORTUNITY_LINK = "(//a[@data-refid='recordId' and ancestor::div[contains(@class, 'primaryField')] and ancestor::div[descendant::div[text()='Opportunity']]])[last()]";
}
