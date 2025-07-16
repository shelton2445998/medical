package com.fourth.medical.test;

import com.fourth.medical.auth.dto.LoginDto;
import com.fourth.medical.auth.dto.DoctorLoginDto;
import com.fourth.medical.auth.service.AppLoginService;
import com.fourth.medical.auth.service.DoctorLoginService;
import com.fourth.medical.auth.service.LoginService;
import com.fourth.medical.auth.service.LoginRedisService;
import com.fourth.medical.auth.util.TokenUtil;
import com.fourth.medical.auth.vo.LoginVo;
import com.fourth.medical.common.enums.SystemType;
import com.fourth.medical.framework.exception.LoginException;
import com.fourth.medical.medical.entity.Doctor;
import com.fourth.medical.medical.entity.Orders;
import com.fourth.medical.medical.entity.Report;
import com.fourth.medical.medical.entity.ReportItem;
import com.fourth.medical.medical.mapper.DoctorMapper;
import com.fourth.medical.medical.mapper.OrdersMapper;
import com.fourth.medical.medical.mapper.ReportMapper;
import com.fourth.medical.medical.service.OrdersService;
import com.fourth.medical.medical.service.ReportService;
import com.fourth.medical.system.entity.SysUser;
import com.fourth.medical.system.mapper.SysUserMapper;
import com.fourth.medical.util.PasswordUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Date;
import java.util.List;

/**
 * 医疗系统功能演示测试类
 *
 * @author fourth
 * @date 2025-07-16
 */
@Slf4j
@SpringBootTest
public class DemoTest {

    @Autowired
    private LoginService loginService;

    @Autowired
    private AppLoginService appLoginService;

    @Autowired
    private DoctorLoginService doctorLoginService;

    @Autowired
    private LoginRedisService loginRedisService;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private OrdersService ordersService;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private ReportService reportService;

    @Autowired
    private ReportMapper reportMapper;

    // 测试方法将在后续添加

    /**
     * 演示1：安全测试 - 用户注册加密过程
     * 展示用户注册时的密码加密过程和最终存储结果
     */
    @Test
    public void testUserRegistrationSecurity() {
        log.info("=== 开始演示用户注册的密码加密过程 ===");

        // 1. 模拟用户注册信息
        String username = "testuser_" + System.currentTimeMillis();
        String rawPassword = "Test123456";

        log.info("用户名: {}", username);
        log.info("原始密码: {}", rawPassword);

        // 2. 生成随机盐值
        String salt = PasswordUtil.generateSalt();
        log.info("生成的随机盐值: {}", salt);

        // 3. 第一步：将原始密码进行MD5加密
        String md5Password = org.apache.commons.codec.digest.DigestUtils.md5Hex(rawPassword);
        log.info("第一步 - MD5加密后的密码: {}", md5Password);

        // 4. 第二步：将MD5加密后的密码与盐值拼接后进行SHA-256加密
        String encryptedPassword = PasswordUtil.encrypt(rawPassword, salt);
        log.info("第二步 - 最终加密后的密码(SHA-256): {}", encryptedPassword);

        // 5. 创建用户对象，准备存储到数据库
        SysUser sysUser = new SysUser();
        sysUser.setUsername(username);
        sysUser.setPassword(encryptedPassword);
        sysUser.setSalt(salt);
        sysUser.setNickname(username);
        sysUser.setStatus(true);
        sysUser.setIsAdmin(false);
        sysUser.setCreateTime(new Date());

        log.info("准备存储到数据库的用户对象: {}", sysUser);

        // 6. 模拟验证过程
        log.info("=== 模拟用户登录验证过程 ===");
        String loginPassword = "Test123456";
        log.info("用户输入的登录密码: {}", loginPassword);

        // 7. 验证密码
        String verifyEncryptedPassword = PasswordUtil.encrypt(loginPassword, salt);
        log.info("验证加密后的密码: {}", verifyEncryptedPassword);
        log.info("密码验证结果: {}", encryptedPassword.equals(verifyEncryptedPassword));

        log.info("=== 用户注册的密码加密过程演示结束 ===");
    }

    /**
     * 演示2：三端登录过程
     * 展示管理端、医生端、用户端的登录过程和未登录访问接口的情况
     */
    @Test
    public void testLoginProcess() {
        log.info("=== 开始演示三端登录过程 ===");

        // 设置模拟请求上下文
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        // 1. 未登录状态下访问接口
        log.info("=== 1. 未登录状态下访问接口 ===");
        try {
            // 尝试获取登录用户信息
            LoginVo loginVo = loginService.getLoginUserInfo();
            log.info("获取到的登录用户信息: {}", loginVo);
        } catch (LoginException e) {
            log.info("未登录访问接口结果: {}", e.getMessage());
        }

        // 2. 管理端登录过程
        log.info("=== 2. 管理端登录过程 ===");
        LoginDto adminLoginDto = new LoginDto();
        adminLoginDto.setUsername("admin");
        adminLoginDto.setPassword("123456");

        try {
            // 登录验证过程
            log.info("管理端登录请求参数: {}", adminLoginDto);

            // 模拟登录过程
            log.info("2.1 根据用户名查询用户信息");
            SysUser sysUser = sysUserMapper.getSysUserByUsername(adminLoginDto.getUsername());
            if (sysUser != null) {
                log.info("查询到用户: {}", sysUser.getUsername());

                log.info("2.2 验证密码");
                String dbPassword = sysUser.getPassword();
                String dbSalt = sysUser.getSalt();
                String password = adminLoginDto.getPassword();
                String encryptPassword = PasswordUtil.encrypt(password, dbSalt);

                log.info("数据库存储的密码: {}", dbPassword);
                log.info("加密后的输入密码: {}", encryptPassword);
                log.info("密码验证结果: {}", dbPassword.equals(encryptPassword));

                if (dbPassword.equals(encryptPassword)) {
                    log.info("2.3 生成Token");
                    String token = TokenUtil.generateAdminToken(sysUser.getId());
                    log.info("生成的Token: {}", token);

                    log.info("2.4 保存登录信息到Redis");
                    log.info("用户ID: {}, 用户名: {}, 系统类型: {}",
                            sysUser.getId(), sysUser.getUsername(), SystemType.ADMIN.getCode());
                }
            } else {
                log.info("用户不存在");
            }
        } catch (Exception e) {
            log.error("管理端登录异常: {}", e.getMessage());
        }

        // 3. 医生端登录过程
        log.info("=== 3. 医生端登录过程 ===");
        DoctorLoginDto doctorLoginDto = new DoctorLoginDto();
        doctorLoginDto.setMobile("13900001111");
        doctorLoginDto.setPassword("123456");

        try {
            // 登录验证过程
            log.info("医生端登录请求参数: {}", doctorLoginDto);

            // 模拟登录过程
            log.info("3.1 根据手机号查询医生信息");
            Doctor doctor = doctorMapper.getDoctorByMobile(doctorLoginDto.getMobile());
            if (doctor != null) {
                log.info("查询到医生: {}", doctor.getName());

                log.info("3.2 验证密码");
                String dbPassword = doctor.getPassword();
                String dbSalt = doctor.getSalt();
                String password = doctorLoginDto.getPassword();
                String encryptPassword = PasswordUtil.encrypt(password, dbSalt);

                log.info("数据库存储的密码: {}", dbPassword);
                log.info("加密后的输入密码: {}", encryptPassword);
                log.info("密码验证结果: {}", dbPassword.equals(encryptPassword));

                if (dbPassword.equals(encryptPassword)) {
                    log.info("3.3 生成Token");
                    String token = TokenUtil.generateDoctorToken(doctor.getId());
                    log.info("生成的Token: {}", token);

                    log.info("3.4 保存登录信息到Redis");
                    log.info("医生ID: {}, 医生姓名: {}, 系统类型: {}",
                            doctor.getId(), doctor.getName(), SystemType.DOCTOR.getCode());
                }
            } else {
                log.info("医生不存在");
            }
        } catch (Exception e) {
            log.error("医生端登录异常: {}", e.getMessage());
        }

        log.info("=== 三端登录过程演示结束 ===");
    }

    /**
     * 演示3：复杂业务逻辑 - 体检报告生成流程
     * 展示从订单创建到体检报告生成的完整业务流程
     */
    @Test
    public void testComplexBusinessLogic() {
        log.info("=== 开始演示复杂业务逻辑 - 体检报告生成流程 ===");

        try {
            // 1. 查询一个已存在的订单
            log.info("1. 查询体检订单信息");
            List<Orders> ordersList = ordersMapper.selectList(null);
            if (ordersList == null || ordersList.isEmpty()) {
                log.info("没有找到可用的订单，演示结束");
                return;
            }

            Orders order = ordersList.get(0);
            log.info("获取到订单信息: 订单ID={}, 用户ID={}, 状态={}",
                    order.getId(), order.getUserId(), order.getStatus());

            // 2. 获取订单中的检查项目
            log.info("2. 获取订单中的检查项目");
            String checkitemIds = order.getCheckitemIds();
            log.info("订单包含的检查项ID: {}", checkitemIds);

            if (checkitemIds == null) {
                log.info("订单没有检查项，演示结束");
                return;
            }

            // 3. 模拟体检报告生成流程
            log.info("3. 开始体检报告生成流程");

            // 3.1 创建主报告
            log.info("3.1 创建主体检报告");
            Report report = new Report();
            report.setId(System.currentTimeMillis());
            report.setOrderId(order.getId());
            report.setUserId(order.getUserId());
            report.setCheckitemIds(checkitemIds);
            report.setStatus(0); // 未完成状态
            report.setCreateTime(new Date());
            log.info("创建的主报告对象: {}", report);

            // 3.2 为每个检查项创建报告项
            log.info("3.2 为每个检查项创建报告项");
            String[] itemIds = checkitemIds.split(",");
            for (String itemIdStr : itemIds) {
                Long itemId = Long.parseLong(itemIdStr.trim());

                // 创建报告项
                ReportItem reportItem = new ReportItem();
                reportItem.setId(System.currentTimeMillis() + itemId);
                reportItem.setOrderId(order.getId());
                reportItem.setUserId(order.getUserId());
                reportItem.setItemId(itemId);
                reportItem.setReportStatus(0); // 未生成状态
                reportItem.setCreateTime(new Date());

                log.info("创建的报告项: 检查项ID={}, 报告项ID={}", itemId, reportItem.getId());

                // 模拟分配医生
                log.info("根据检查项分配对应科室的医生");
                // 实际业务中会根据检查项对应的科室分配医生

                // 模拟保存报告项
                log.info("保存报告项到数据库");
            }

            // 3.3 更新主报告的报告项ID列表
            log.info("3.3 更新主报告的报告项ID列表");
            report.setReportItemIds("模拟的报告项ID列表");
            log.info("主报告更新后的报告项ID列表: {}", report.getReportItemIds());

            // 3.4 医生填写检查结果
            log.info("3.4 医生填写检查结果");
            log.info("医生登录系统，查看待处理的报告");
            log.info("医生填写检查结果和诊断意见");

            // 3.5 生成最终报告
            log.info("3.5 生成最终报告");
            report.setConclusion("体检结果正常，建议定期体检，保持良好的生活习惯。");
            report.setStatus(1); // 已完成状态
            report.setReportDate(new Date());
            report.setDoctorId(3001L); // 假设由ID为3001的医生完成
            report.setUpdateTime(new Date());

            log.info("最终生成的体检报告: {}", report);
            log.info("报告状态: 已完成");
            log.info("报告结论: {}", report.getConclusion());

            log.info("体检报告生成流程完成");
        } catch (Exception e) {
            log.error("体检报告生成流程演示异常: {}", e.getMessage(), e);
        }

        log.info("=== 复杂业务逻辑演示结束 ===");
    }
}
