package com.publicis.dmccpoc.locators.sfdc;

public class NavigationBar {
    private NavigationBar() {
    }

    public static final String APP_LAUNCHER = "//button[descendant::*[contains(text(), 'App Launcher')]]";
    public static final String APP_LAUNCHER_SEARCH_FIELD = "//input[contains(@type, 'search') and ancestor::one-app-launcher-menu]";

    public static String getAppLauncherSearchResultsItemByLabel(String label){
        return String.format("//one-app-launcher-menu-item[descendant::*[@*='%s']]", label);
    }

}
