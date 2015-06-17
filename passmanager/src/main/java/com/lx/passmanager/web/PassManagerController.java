/**    
 * FileName: PassManagerController.java
 * Version Info: passmanager
 * Date: 2014年12月31日 
 * Copyright  Corporation 2014     
 * All Rights Reserved  
 */

package com.lx.passmanager.web;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.type.JavaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.lx.passmanager.dto.APIResponse;
import com.lx.passmanager.dto.SubPKPass;
import com.lx.passmanager.services.IPassService;
import com.lx.passmanager.utils.JsonMapper;
import com.lx.passmanager.utils.PassStyle;

import de.brendamour.jpasskit.PKBarcode;
import de.brendamour.jpasskit.PKField;
import de.brendamour.jpasskit.PKLocation;
import de.brendamour.jpasskit.PKPass;
import de.brendamour.jpasskit.passes.PKCoupon;
import de.brendamour.jpasskit.passes.PKEventTicket;
import de.brendamour.jpasskit.passes.PKGenericPass;

/**    
 *
 * Project Name：passmanager    
 * Class Name：PassManagerController    
 * Class Description：   
 * Creater ：xliu    
 * Create Time：2014年12月31日 下午1:19:34    
 * Modifier：xliu    
 * Modification Time：2014年12月31日 下午1:19:34    
 * Modify Notes：    
 * 
 */
@Controller
@RequestMapping("/pass")
public class PassManagerController {

    private final Log logger = LogFactory.getLog(PassManagerController.class);

    private final JsonMapper mapper = new JsonMapper();

    @Autowired
    private IPassService passManagerService;

    @RequestMapping("/createPass")
    public void createPass(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String validateResult = validateActionName(request);

        if (StringUtils.isNotBlank(validateResult)) {
            writeJSONToResponse(validateResult, response);
            return;
        }

        PKPass pass = accsemblyPass(request);
        validateResult = validatePassParameters(pass);
        if (StringUtils.isNotBlank(validateResult)) {
            writeJSONToResponse(validateResult, response);
            return;
        }

        byte[] zippedpkPass = null;
        try {
            zippedpkPass = passManagerService.createPassService(pass);
            writeBytesToResponse(zippedpkPass, request, response);
        } catch (Exception e) {
            logger.error("create pass failed!", e);
            writeJSONToResponse(
                    getApiResponse("create pass failed! " + e.getMessage(), false, new HashMap<String, Object>())
                            .toJSONString(), response);
        }
    }

    @RequestMapping("/downloadPass")
    public void downloadPass(HttpServletRequest request, HttpServletResponse response) throws Exception {
        File file = new File("C:\\Users\\xliu\\Desktop\\Passbook Companion Files\\Sample Passes\\BoardingPass.pkpass");
        FileInputStream fis = new FileInputStream(file);
        response.setDateHeader("Last-Modified", new Date().getTime());
        response.addHeader("Content-Type", "application/vnd.apple.pkpass");
        response.addHeader("where", "form");

        byte[] bytes = new byte[fis.available()];
        int len = fis.read(bytes);
        logger.info(len + "==========");
        response.setContentType("application/vnd.apple.pkpass");
        response.addHeader("Content-Disposition", "attachment; filename=" + file.getName());
        response.setContentLength(bytes.length);
        response.getOutputStream().write(bytes);
        response.flushBuffer();
        response.getOutputStream().close();
    }

    private String validateActionName(HttpServletRequest request) {
        String actionName = request.getParameter("actionName");
        if (StringUtils.isBlank(actionName)) {
            return getApiResponse("actionName cannot be null!", false, new HashMap<String, Object>()).toJSONString();
        }
        if (!PassStyle.COUPON.getActionName().equals(actionName)
                && !PassStyle.EVENTTICKET.getActionName().equals(actionName)) {
            return getApiResponse(actionName + " are not supported!", false, new HashMap<String, Object>())
                    .toJSONString();
        }
        return "";
    }

    private String validatePassParameters(PKPass pass) {
        if (pass.isValid()) {
            return "";
        } else {
            List<String> errorList = pass.getValidationErrors();
            HashMap<String, Object> results = new HashMap<String, Object>();
            for (int i = 0; i < errorList.size(); i++) {
                results.put(i + 1 + "", errorList.get(i));
            }
            return getApiResponse("error pass field!", false, results).toJSONString();
        }
    }

    public APIResponse getApiResponse(String errorString, boolean status, HashMap<String, Object> results) {
        APIResponse apiResponse = new APIResponse();
        apiResponse.setErrorString(errorString);
        apiResponse.setSuccess(status);
        apiResponse.setResults(results);
        return apiResponse;
    }

    private void writeJSONToResponse(String jsonString, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        response.getWriter().write(jsonString);
        response.getWriter().flush();
        response.getWriter().close();
    }

    private void writeBytesToResponse(byte[] bytes, HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        //ios 7 don't recognition this mime type
        response.setContentType("application/vnd.apple.pkpass");
        //        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=" + request.getParameter("actionName")
                + ".pkpass");
        response.setContentLength(bytes.length);
        response.getOutputStream().write(bytes);
        response.flushBuffer();
        response.getOutputStream().close();
    }

    private PKPass accsemblyPass(HttpServletRequest request) {
        SubPKPass pass = new SubPKPass();
        String serialNumber = request.getParameter("serialNumber");
        String passTypeIdentifier = request.getParameter("passTypeIdentifier");
        String teamIdentifier = request.getParameter("teamIdentifier");
        String description = request.getParameter("description");
        String formatVersion = request.getParameter("formatVersion");
        String organizationName = request.getParameter("organizationName");

        //can be null
        String webServiceURL = request.getParameter("webServiceURL");
        String authenticationToken = request.getParameter("authenticationToken");
        String appLaunchURL = request.getParameter("appLaunchURL");
        String associatedStoreIdentifiers = request.getParameter("associatedStoreIdentifiers");
        String groupingIdentifier = request.getParameter("groupingIdentifier");
        try {
            if (StringUtils.isNotBlank(webServiceURL))
                pass.setWebServiceURL(new URL(webServiceURL));
        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        }
        pass.setSerialNumber(serialNumber);
        pass.setTeamIdentifier(teamIdentifier);
        pass.setPassTypeIdentifier(passTypeIdentifier);
        pass.setOrganizationName(organizationName);
        pass.setDescription(description);
        if (StringUtils.isNotBlank(formatVersion)) {
            try {
                pass.setFormatVersion(Integer.parseInt(formatVersion));
            } catch (NumberFormatException e) {
                logger.error(e.getMessage(), e);
            }
        }
        pass.setAuthenticationToken(authenticationToken);
        pass.setAppLaunchURL(appLaunchURL);
        if (StringUtils.isNotBlank(associatedStoreIdentifiers)) {
            String[] ids = associatedStoreIdentifiers.split(",");
            List<Long> result = new ArrayList<Long>(ids.length);
            for (String id : ids) {
                result.add(Long.valueOf(id));
            }
            pass.setAssociatedStoreIdentifiers(result);
        }
        pass.setGroupingIdentifier(groupingIdentifier);
        if (StringUtils.isNotBlank(groupingIdentifier)) {
            // to be create eventTicket or boardingPass
        }

        String barcode = request.getParameter("barcode");
        PKBarcode pkbarcode = mapper.fromJson(barcode, PKBarcode.class);
        pass.setBarcode(pkbarcode);

        String locationStr = request.getParameter("locations");
        JavaType locationList = mapper.contructCollectionType(List.class, PKLocation.class);
        List<PKLocation> locations = mapper.fromJson(locationStr, locationList);
        pass.setLocations(locations);

        String logoText = request.getParameter("logoText");
        String foregroundColor = request.getParameter("foregroundColor");
        String backgroundColor = request.getParameter("backgroundColor");
        pass.setLogoText(logoText);
        pass.setForegroundColorAsObject(pass.convertStringToColor(foregroundColor));
        pass.setBackgroundColorAsObject(pass.convertStringToColor(backgroundColor));

        //        pass.setUserInfo("ActiveNetwork");
        //        pass.setVoided(true);

        assemblyPassStyle(request, pass);

        return pass;
    }

    private void assemblyPassStyle(HttpServletRequest request, PKPass pass) {

        String actionName = request.getParameter("actionName");
        if (PassStyle.COUPON.getActionName().equals(actionName)) {
            PKCoupon gpass = (PKCoupon) assemblyGenericPass(request, new PKCoupon());
            pass.setCoupon(gpass);
        } else if (PassStyle.EVENTTICKET.getActionName().equals(actionName)) {
            PKEventTicket gpass = (PKEventTicket) assemblyGenericPass(request, new PKEventTicket());
            pass.setEventTicket(gpass);
        }
    }

    private PKGenericPass assemblyGenericPass(HttpServletRequest request, PKGenericPass gpass) {
        // gpass --- begin 
        String headerFields = request.getParameter("headerFields");
        String primaryFields = request.getParameter("primaryFields");
        String secondaryFields = request.getParameter("secondaryFields");
        String auxiliaryFields = request.getParameter("auxiliaryFields");
        String backFields = request.getParameter("backFields");
        // gpass ---end

        JavaType listType = mapper.contructCollectionType(List.class, PKField.class);
        List<PKField> headerFieldList = mapper.fromJson(headerFields, listType);
        List<PKField> primaryFieldList = mapper.fromJson(primaryFields, listType);
        List<PKField> secondaryFieldList = mapper.fromJson(secondaryFields, listType);
        List<PKField> auxiliaryFieldList = mapper.fromJson(auxiliaryFields, listType);
        List<PKField> backFieldList = mapper.fromJson(backFields, listType);

        gpass.setHeaderFields(headerFieldList);
        gpass.setPrimaryFields(primaryFieldList);
        gpass.setSecondaryFields(secondaryFieldList);
        gpass.setAuxiliaryFields(auxiliaryFieldList);
        gpass.setBackFields(backFieldList);

        return gpass;
    }

    public static void main(String[] args) throws JsonParseException, JsonMappingException, IOException {
        JsonMapper map = new JsonMapper();
        JavaType listType = map.contructCollectionType(List.class, PKField.class);
        List<PKField> result = map.fromJson(
                "[{\"key\" : \"offer\",\"label\" : \"Any premium dog food\",\"value\" : \"20% off\"}]", listType);
        System.out.println(Arrays.toString(result.toArray()));

        List<PKField> result2 = map.fromJson(
                "[{\"key\" : \"offer123\",\"label\" : \"Any premium dog food123\",\"value\" : \"20% off\"}]", listType);
        System.out.println(Arrays.toString(result2.toArray()));

        //        List<PKField> result3 = map.fromJson("3243243", listType);
        //        System.out.println(Arrays.toString(result3.toArray()));

        List<PKField> result4 = map.fromJson(
                "[{\"key\" : \"offer\",\"label\" : \"Any premium dog food\",\"value\" : \"20% off\"}]", listType);
        System.out.println(Arrays.toString(result4.toArray()));

        PKBarcode barcode = map
                .fromJson(
                        "{\"message\" : \"123456789\",\"format\" : \"PKBarcodeFormatPDF417\",\"messageEncoding\" : \"iso-8859-1\"}",
                        PKBarcode.class);

        barcode = map.fromJson("", PKBarcode.class);
        System.out.println("=========" + barcode.getFormat());
    }

}
