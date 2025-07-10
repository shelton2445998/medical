package com.fourth.medical.test;

import com.fourth.medical.auth.dto.LoginDto;
import com.fourth.medical.framework.response.ApiResult;
import com.fourth.medical.medical.query.AppHospitalQuery;
import com.fourth.medical.medical.query.AppSetmealQuery;
import com.fourth.medical.medical.service.HospitalService;
import com.fourth.medical.medical.service.SetmealService;
import com.fourth.medical.medical.vo.AppHospitalVo;
import com.fourth.medical.medical.vo.AppSetmealVo;
import com.fourth.medical.system.query.SysUserQuery;
import com.fourth.medical.system.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * API接口测试类
 *
 * @author fourth
 * @since 2025-07-09
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class ApiInterfaceTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SysUserService sysUserService;
    
    @Autowired
    private HospitalService hospitalService;
    
    @Autowired
    private SetmealService setmealService;

    /**
     * 测试管理员登录接口
     */
    @Test
    public void testAdminLogin() throws Exception {
        log.info("====== 测试管理员登录接口 ======");
        String requestBody = "{\"username\":\"admin\",\"password\":\"123456\"}";
        
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/admin/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();
                
        String responseContent = result.getResponse().getContentAsString();
        log.info("响应内容: {}", responseContent);
    }

    /**
     * 测试获取管理员信息接口
     */
    @Test
    public void testAdminInfo() throws Exception {
        log.info("====== 测试获取管理员信息接口 ======");
        // 注意：实际测试需要先登录获取token，这里仅作演示
        String token = "yourTokenHere";
        
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/admin/info")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andReturn();
                
        String responseContent = result.getResponse().getContentAsString();
        log.info("响应内容: {}", responseContent);
    }

    /**
     * 测试管理员退出接口
     */
    @Test
    public void testAdminLogout() throws Exception {
        log.info("====== 测试管理员退出接口 ======");
        // 注意：实际测试需要先登录获取token，这里仅作演示
        String token = "yourTokenHere";
        
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/admin/logout")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andReturn();
                
        String responseContent = result.getResponse().getContentAsString();
        log.info("响应内容: {}", responseContent);
    }

    /**
     * 测试获取用户列表接口
     */
    @Test
    public void testGetUserList() throws Exception {
        log.info("====== 测试获取用户列表接口 ======");
        // 注意：实际测试需要先登录获取token，这里仅作演示
        String token = "yourTokenHere";
        
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/admin/user/list")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andReturn();
                
        String responseContent = result.getResponse().getContentAsString();
        log.info("响应内容: {}", responseContent);
        
        // 测试Service方法
        SysUserQuery query = new SysUserQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        try {
            sysUserService.getSysUserPage(query);
            log.info("Service方法调用成功");
        } catch (Exception e) {
            log.error("Service调用异常", e);
        }
    }

    /**
     * 测试获取医院列表接口
     */
    @Test
    public void testGetHospitalList() throws Exception {
        log.info("====== 测试获取医院列表接口 ======");
        
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/hospital/list"))
                .andExpect(status().isOk())
                .andReturn();
                
        String responseContent = result.getResponse().getContentAsString();
        log.info("响应内容: {}", responseContent);
        
        // 测试Service方法
        AppHospitalQuery query = new AppHospitalQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        try {
            List<AppHospitalVo> recommendList = hospitalService.getRecommendHospitals();
            log.info("推荐医院数量: {}", recommendList.size());
        } catch (Exception e) {
            log.error("Service调用异常", e);
        }
    }

    /**
     * 测试获取体检套餐列表接口
     */
    @Test
    public void testGetPackageList() throws Exception {
        log.info("====== 测试获取体检套餐列表接口 ======");
        
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/package/list"))
                .andExpect(status().isOk())
                .andReturn();
                
        String responseContent = result.getResponse().getContentAsString();
        log.info("响应内容: {}", responseContent);
        
        // 测试Service方法
        AppSetmealQuery query = new AppSetmealQuery();
        query.setPageNum(1);
        query.setPageSize(10);
        try {
            List<AppSetmealVo> recommendList = setmealService.getRecommendSetmeals();
            log.info("推荐套餐数量: {}", recommendList.size());
        } catch (Exception e) {
            log.error("Service调用异常", e);
        }
    }

    /**
     * 测试创建预约接口
     */
    @Test
    public void testCreateAppointment() throws Exception {
        log.info("====== 测试创建预约接口 ======");
        // 注意：实际测试需要先登录获取token，这里仅作演示
        String token = "yourTokenHere";
        
        String requestBody = "{\"setmealId\":1,\"hospitalId\":1,\"familyMemberId\":1,\"appointmentDate\":\"2025-08-01\",\"appointmentTime\":\"上午\"}";
        
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/appointment/create")
                        .header("Authorization", token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();
                
        String responseContent = result.getResponse().getContentAsString();
        log.info("响应内容: {}", responseContent);
    }

    /**
     * 测试获取预约列表接口
     */
    @Test
    public void testGetAppointmentList() throws Exception {
        log.info("====== 测试获取预约列表接口 ======");
        // 注意：实际测试需要先登录获取token，这里仅作演示
        String token = "yourTokenHere";
        
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.get("/appointment/list")
                        .header("Authorization", token))
                .andExpect(status().isOk())
                .andReturn();
                
        String responseContent = result.getResponse().getContentAsString();
        log.info("响应内容: {}", responseContent);
    }

    /**
     * 测试用户注册接口
     */
    @Test
    public void testUserRegister() throws Exception {
        log.info("====== 测试用户注册接口 ======");
        
        String requestBody = "{\"username\":\"testuser\",\"password\":\"123456\"}";
        
        MvcResult result = mockMvc.perform(
                MockMvcRequestBuilders.post("/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();
                
        String responseContent = result.getResponse().getContentAsString();
        log.info("响应内容: {}", responseContent);
    }
} 