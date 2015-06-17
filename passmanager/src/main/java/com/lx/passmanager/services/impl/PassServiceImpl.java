/**    
 * FileName: PassServiceImpl.java
 * Version Info: mobile-service
 * Date: 2014年12月24日 
 * Copyright  Corporation 2014     
 * All Rights Reserved  
 */

package com.lx.passmanager.services.impl;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.PostConstruct;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.lx.passmanager.services.IPassService;
import com.lx.passmanager.utils.MobileUtils;

import de.brendamour.jpasskit.PKPass;
import de.brendamour.jpasskit.signing.PKSigningInformation;
import de.brendamour.jpasskit.signing.PKSigningUtil;

/**    
 *
 * Project Name：mobile-service    
 * Class Name：PassServiceImpl    
 * Class Description：   
 * Creater ：xliu    
 * Create Time：2014年12月24日 上午11:29:47    
 * Modifier：xliu    
 * Modification Time：2014年12月24日 上午11:29:47    
 * Modify Notes：    
 * 
 */
@Service
public class PassServiceImpl implements IPassService {

    @Value("${WWDRcertificatePath}")
    private String WWDRCertificatePath;

    @Value("${signingCertPath}")
    private String signingCertPath;

    @Value("${couponPassPath}")
    private String couponPassPath;

    @Value("${eventTicketPassPath}")
    private String eventTicketPassPath;

    @Value("${privateKeyPasswd}")
    private String privateKeyPasswd;

    @Value("${passStoreDirectory}")
    private String passStoreDirectory;

    private static final Logger LOG = LoggerFactory.getLogger(PassServiceImpl.class);

    /**
     * Certificate and privateKey info
     */
    private PKSigningInformation cerAndKeyinformation;

    @PostConstruct
    public void initCertAndKey() throws Exception {
        InputStream signCertStream = MobileUtils.getInputStreamFromJar(signingCertPath);
        InputStream wwdrCertStream = MobileUtils.getInputStreamFromJar(WWDRCertificatePath);
        cerAndKeyinformation = PKSigningUtil.loadSigningInformationFromPKCS12AndIntermediateCertificateStreams(
                signCertStream, privateKeyPasswd, wwdrCertStream);
    }

    /**
     * (non-Javadoc)    
     * @see com.active.services.mobile.service.IPassService#createPassService(de.brendamour.jpasskit.PKPass)    
     */

    @Override
    public byte[] createPassService(PKPass request) throws Exception {

        String passStylePath = getPassStylePathBy(request);

        if (StringUtils.isBlank(passStylePath))
            return new byte[0];
        LOG.info("path======================" + MobileUtils.getFilePathFromJar(passStylePath));
        byte[] passZipBytes = null;
        try {
            passZipBytes = PKSigningUtil.createSignedAndZippedPkPassArchive(request,
                    MobileUtils.getFilePathFromJar(passStylePath), cerAndKeyinformation);
            generatePassZip(passZipBytes);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }

        return passZipBytes;
    }

    private String getPassStylePathBy(PKPass pass) {
        String passPath = "";
        if (pass.getCoupon() != null) {
            passPath = couponPassPath;
        } else if (pass.getEventTicket() != null) {
            passPath = eventTicketPassPath;
        }
        return passPath;
    }

    private void generatePassZip(byte[] bytes) {
        String absolutepkPassPath = MobileUtils.getCurrentPathFromDisk() + File.separator + passStoreDirectory
                + File.separator + System.currentTimeMillis() + ".pkpass";

        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);
        try {
            FileOutputStream outStream = new FileOutputStream(MobileUtils.createFile(absolutepkPassPath));
            IOUtils.copy(inputStream, outStream);
            inputStream.close();
            outStream.close();
        } catch (FileNotFoundException e) {
            LOG.error(absolutepkPassPath + " not found!!!", e);
        } catch (IOException e) {
            LOG.error(e.getMessage(), e);
        }
    }

}
