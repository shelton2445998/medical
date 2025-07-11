package com.fourth.medical.generator;

import com.fourth.medical.generator.config.SimpleGeneratorConfig;
import com.fourth.medical.generator.handler.SimpleGeneratorHandler;

/**
 * @author geekidea
 * @date 2022/7/2
 **/
public class SimpleGenerator {

    public static void main(String[] args) throws Exception {
        SimpleGeneratorConfig config = new SimpleGeneratorConfig();
        config.setModuleName("user");
        config.setName("UserSearch");
        config.setDesc("用户描述");
        config.setAuthor("geekidea");
        config.setGenerateController(true);
        config.setGenerateService(true);
        config.setGenerateMapper(true);
        SimpleGeneratorHandler.generateSimple(config);
    }

}
