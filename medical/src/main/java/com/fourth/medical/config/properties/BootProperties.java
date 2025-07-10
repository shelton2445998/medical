package com.fourth.medical.config.properties;

import com.fourth.medical.util.YamlUtil;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 框架公共配置
 *
 * @author geekidea
 * @date 2023/12/03
 **/
@Data
@Component
@ConfigurationProperties(prefix = "boot")
public class BootProperties {

    /**
     * 排除的路径
     */
    private List<String> excludePaths;

    public void setExcludePaths(List<String> excludePaths) {
        this.excludePaths = YamlUtil.parseListArray(excludePaths);
    }

}
