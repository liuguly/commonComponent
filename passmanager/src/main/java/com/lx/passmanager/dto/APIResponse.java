package com.lx.passmanager.dto;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class represents the response sent back for API calls.
 * 
 * @author pdetagyos
 *
 */
public class APIResponse {

    private static Logger log = LoggerFactory.getLogger(APIResponse.class);

    private boolean success;

    private String errorString;

    private HashMap<String, Object> results;

    public APIResponse() {
        this.success = false;
        this.errorString = "";
        this.results = new HashMap<String, Object>();
    }

    public APIResponse(boolean success, String errorString, HashMap<String, Object> results) {
        this.success = success;
        this.errorString = errorString;
        this.results = results;
    }

    public boolean getSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorString() {
        return errorString;
    }

    public void setErrorString(String errorString) {
        this.errorString = errorString;
    }

    public HashMap<String, Object> getResults() {
        return results;
    }

    public void setResults(HashMap<String, Object> results) {
        this.results = results;
    }

    public void addResultData(String key, Object value) {
        this.results.put(key, value);
    }

    /**
     * Convert the class to a JSON string
     * 
     * @return JSON string representation of the current object state
     */
    @SuppressWarnings("unchecked")
    public String toJSONString() {
        log.debug("Generating JSON for result data");
        JSONObject json = new JSONObject();

        try {
            json.put("success", this.success);
            if (!this.errorString.isEmpty()) {
                json.put("errorString", this.errorString);
            }
            JSONObject resultsObj = new JSONObject(this.results);
            for (String key : this.results.keySet()) {
                log.debug("Generating JSON for result data element with type: "
                        + this.results.get(key).getClass().toString());
                if (this.results.get(key) instanceof ArrayList) {
                    log.debug("Generating JSON for result data - adding JSON array");
                    ArrayList<Object> arr = (ArrayList<Object>) this.results.get(key);
                    JSONArray jsonArr = new JSONArray();
                    for (Object o : arr) {
                        JSONArray domainValues = new JSONArray();
                        String productMapString = null;
                        for (Field f : o.getClass().getDeclaredFields()) {
                            if (f.getType() == List.class) {
                                domainValues.put(BeanUtils.getArrayProperty(o, f.getName()));
                            }
                        }
                        JSONObject j = new JSONObject(o);
                        j.put("domainValues", domainValues);
                        if (StringUtils.isNotBlank(productMapString)) {
                            j.put("mobileProducts", new JSONObject(productMapString));
                        }
                        jsonArr.put(j);
                    }
                    resultsObj.put(key, jsonArr);
                } else {
                    if (this.results.get(key).getClass() == String.class) {
                        resultsObj.put(key, this.results.get(key));
                    } else {
                        resultsObj.put(key, new JSONObject(this.results.get(key)));
                    }
                }
            }
            json.put("results", resultsObj);
        } catch (JSONException e) {
            log.warn("There was a problem converting APIResponse data to JSON format.", e);
        } catch (IllegalArgumentException e) {
            log.warn("There was a problem converting APIResponse data to JSON format.", e);
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            log.warn("There was a problem converting APIResponse data to JSON format.", e);
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            log.warn("There was a problem converting APIResponse data to JSON format.", e);
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            log.warn("There was a problem converting APIResponse data to JSON format.", e);
            e.printStackTrace();
        }

        return json.toString();
    }

}
