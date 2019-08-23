package com.itheimaxia.easyiot.common.config;

import com.itheimaxia.easyiot.common.error.GlobalExceptionTranslator;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Use this common config for Rest API
 */
@Configuration
@Import(value = {GlobalExceptionTranslator.class})
public class EasyIotRestConfig {
}
