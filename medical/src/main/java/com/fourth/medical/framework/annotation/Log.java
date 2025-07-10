package com.fourth.medical.framework.annotation;

import com.fourth.medical.common.enums.SysLogType;

/**
 * @author geekidea
 * @date 2022/8/3
 **/
public @interface Log {

    /**
     * 描述
     *
     * @return
     */
    String value() default "";


    SysLogType type() default SysLogType.OTHER;

}
