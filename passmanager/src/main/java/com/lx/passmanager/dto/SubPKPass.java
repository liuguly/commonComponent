/**    
 * FileName: SubPKPass.java
 * Version Info: fnd-webserver-mobile
 * Date: 2014年12月26日 
 * Copyright  Corporation 2014     
 * All Rights Reserved  
 */

package com.lx.passmanager.dto;

import java.awt.Color;
import java.io.Serializable;

import de.brendamour.jpasskit.PKPass;

/**    
 *
 * Project Name：fnd-webserver-mobile    
 * Class Name：SubPKPass    
 * Class Description：   
 * Creater ：xliu    
 * Create Time：2014年12月26日 下午1:05:32    
 * Modifier：xliu    
 * Modification Time：2014年12月26日 下午1:05:32    
 * Modify Notes：    
 * 
 */

public class SubPKPass extends PKPass implements Serializable {

    private static final long serialVersionUID = 4039204150482622862L;

    /**
     * (non-Javadoc)    
     * @see de.brendamour.jpasskit.PKPass#convertStringToColor(java.lang.String)    
     */
    @Override
    public Color convertStringToColor(String arg0) {
        return super.convertStringToColor(arg0);
    }
}
