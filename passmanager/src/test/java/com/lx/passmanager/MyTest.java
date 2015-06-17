/**    
 * FileName: Test.java
 * Version Info: passmanager
 * Date: 2015年1月2日 
 * Copyright  Corporation 2015     
 * All Rights Reserved  
 */

package com.lx.passmanager;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.junit.Test;

import com.google.common.io.Files;
import com.lx.passmanager.utils.MobileUtils;

/**    
 *
 * Project Name：passmanager    
 * Class Name：Test    
 * Class Description：   
 * Creater ：xliu    
 * Create Time：2015年1月2日 下午9:44:04    
 * Modifier：xliu    
 * Modification Time：2015年1月2日 下午9:44:04    
 * Modify Notes：    
 * 
 */

public class MyTest {

    @Test
    public void test1() throws IOException, Exception {
        File tempPassDir = Files.createTempDir();
        FileUtils.copyDirectory(new File(MobileUtils.getFilePathFromJar("pass.defaultstyle/coupon")), tempPassDir);

        System.out.println(tempPassDir.getAbsolutePath());

    }

}
