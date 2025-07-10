package com.fourth.medical.config.properties;

import com.fourth.medical.util.YamlUtil;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author geekidea
 * @date 2022/6/26
 **/
@Data
@Component
@ConfigurationProperties(prefix = "login")
public class LoginProperties {

    /**
     * 排除的路径
     */
    private List<String> excludePaths;

    public void setExcludePaths(List<String> excludePaths) {
        this.excludePaths = YamlUtil.parseListArray(excludePaths);
    }


}
