/**    
 * FileName: PassService.java
 * Version Info: mobile-service
 * Date: 2014年12月24日 
 * Copyright  Corporation 2014     
 * All Rights Reserved  
 */

package com.lx.passmanager.services;

import de.brendamour.jpasskit.PKPass;

/**    
 *
 * Project Name：mobile-service    
 * Class Name：PassService    
 * Class Description：   
 * Creater ：xliu    
 * Create Time：2014年12月24日 上午11:29:11    
 * Modifier：xliu    
 * Modification Time：2014年12月24日 上午11:29:11    
 * Modify Notes：    
 * 
 */
public interface IPassService {

    /**
     * 
     * create pass    
     * @param pass
     * @return
     * @throws Exception
     */
    public byte[] createPassService(PKPass pass) throws Exception;

}
