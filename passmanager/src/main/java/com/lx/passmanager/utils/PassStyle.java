/**    
 * FileName: PassStyle.java
 * Version Info: fnd-webserver-mobile
 * Date: 2014年12月22日 
 * Copyright  Corporation 2014     
 * All Rights Reserved  
 */

package com.lx.passmanager.utils;

/**    
 *
 * Project Name：fnd-webserver-mobile    
 * Class Name：PassStyle    
 * Class Description：   
 * Creater ：xliu    
 * Create Time：2014年12月22日 下午1:46:05    
 * Modifier：xliu    
 * Modification Time：2014年12月22日 下午1:46:05    
 * Modify Notes：    
 * 
 */

public enum PassStyle {

    BOARDINGPASS("createBoardingPass"), COUPON("createCouponPass"), EVENTTICKET("createEventTicketPass"), GENERIC(
            "createGenericPass"), STORECARD("createStoreCardPass");

    private String actionName;

    private PassStyle(String actionName) {
        this.actionName = actionName;
    }

    public String getActionName() {
        return actionName;
    }

}
