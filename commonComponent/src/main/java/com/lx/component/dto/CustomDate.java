/**    
 * FileName: CustomDate.java
 * Version Info: commonComponent
 * Date: 2015年6月17日 
 * Copyright  Corporation 2015     
 * All Rights Reserved  
 */

package com.lx.component.dto;

import java.io.IOException;
import java.util.Date;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 *
 * Project Name：commonComponent Class Description： Author ：xliu Create
 * Time：2015年6月17日 下午1:31:05 自定义日期输出格式
 */

public class CustomDate extends JsonSerializer<Date> {

    /**
     * @see com.fasterxml.jackson.databind.JsonSerializer#serialize(java.lang.Object,
     *      com.fasterxml.jackson.core.JsonGenerator,
     *      com.fasterxml.jackson.databind.SerializerProvider)
     */

    @Override
    public void serialize(Date date, JsonGenerator jgen,
	    SerializerProvider provider) throws IOException,
	    JsonProcessingException {
	jgen.writeString(date.toString());
    }

}
