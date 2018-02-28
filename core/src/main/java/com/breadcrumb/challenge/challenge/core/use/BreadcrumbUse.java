package com.breadcrumb.challenge.challenge.core.use;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.sling.api.resource.ValueMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.cq.sightly.WCMUse;
import com.day.cq.wcm.api.Page;
/**
 * Sightly Use class for use in Breadcrumb Component
 */
public class BreadcrumbUse extends WCMUse {
    private static final Logger LOGGER = LoggerFactory
            .getLogger(BreadcrumbUse.class);
    private List<Page> navList = new ArrayList<>();
    @Override
    public void activate() throws Exception {
        setBreadCrumbItems();
    }
    /**
     * This method populates list of pages to be part of Breadcrumb
     * depending upon the start level and end level
     * start level 1 means /content/breadcrumbchallenge
     * start level 2 means /content/breadcrumbchallenge/homepage and likewise.
     * end level 0 means the current level 
     * end level 1 mean one level up the current level and likewise. 
     */ 
    private void setBreadCrumbItems() {
        int startLevel = getCurrentStyle().get("absParent", 2);
        LOGGER.debug("Start Level is {} ", startLevel);
        int endLevel = getCurrentStyle().get("relParent", 0);
        LOGGER.debug("End Level is {} ", endLevel);
        int currentLevel = getCurrentPage().getDepth();
        LOGGER.debug("Current Level is {} ", currentLevel);
        while (startLevel < currentLevel - endLevel) {
            Page trailPage = getCurrentPage().getAbsoluteParent(startLevel);
            LOGGER.debug("Trail Page Path is {}", trailPage.getPath());
            ValueMap test  =  trailPage.getProperties();
            String hideInBreadcrumb = test.getOrDefault("hideInBread", "false").toString();
            if(hideInBreadcrumb != null && hideInBreadcrumb.equalsIgnoreCase("false")){
	            this.getNavList().add(trailPage);	           
            }
            startLevel++;
        }
    }
    /**
     * @return list of Navigation Pages which would be part of Breadcrumb
     */
    public List<Page> getNavList() {
        return this.navList;
    }
}