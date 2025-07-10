package com.fourth.medical.test;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.fourth.medical.medical.entity.*;
import com.fourth.medical.medical.mapper.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * 数据库表数据打印测试
 *
 * @author fourth
 * @since 2025-07-09
 */
@SpringBootTest
public class DatabaseTableDataPrintTest {

    @Autowired
    private CheckitemMapper checkitemMapper;

    @Autowired
    private CheckitemDetailMapper checkitemDetailMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private DoctorMapper doctorMapper;

    @Autowired
    private DoctorScheduleMapper doctorScheduleMapper;

    @Autowired
    private FamilyMemberMapper familyMemberMapper;

    @Autowired
    private HospitalMapper hospitalMapper;

    @Autowired
    private InvoiceMapper invoiceMapper;

    @Autowired
    private NotificationMapper notificationMapper;

    @Autowired
    private OrdersMapper ordersMapper;

    @Autowired
    private OverallResultMapper overallResultMapper;

    @Autowired
    private ReportItemMapper reportItemMapper;

    @Autowired
    private ReportItemDetailMapper reportItemDetailMapper;

    @Autowired
    private SetmealMapper setmealMapper;

    @Autowired
    private SetmealDetailMapper setmealDetailMapper;

    @Test
    public void printAllTablesData() {
        System.out.println("============ 打印所有数据库表数据 ============");
        
        // 打印检查项表数据
        printCheckitemData();
        
        // 打印检查项详情表数据
        printCheckitemDetailData();
        
        // 打印科室表数据
        printDepartmentData();
        
        // 打印医生表数据
        printDoctorData();
        
        // 打印医生排班表数据
        printDoctorScheduleData();
        
        // 打印家庭成员表数据
        printFamilyMemberData();
        
        // 打印医院表数据
        printHospitalData();
        
        // 打印发票表数据
        printInvoiceData();
        
        // 打印通知表数据
        printNotificationData();
        
        // 打印订单表数据
        printOrdersData();
        
        // 打印总体结果表数据
        printOverallResultData();
        
        // 打印报告项表数据
        printReportItemData();
        
        // 打印报告项详情表数据
        printReportItemDetailData();
        
        // 打印套餐表数据
        printSetmealData();
        
        // 打印套餐详情表数据
        printSetmealDetailData();
    }

    /**
     * 打印检查项表数据
     */
    private void printCheckitemData() {
        System.out.println("\n===== 检查项表(checkitem)数据 =====");
        LambdaQueryWrapper<Checkitem> wrapper = Wrappers.lambdaQuery(Checkitem.class);
        List<Checkitem> list = checkitemMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }

    /**
     * 打印检查项详情表数据
     */
    private void printCheckitemDetailData() {
        System.out.println("\n===== 检查项详情表(checkitem_detail)数据 =====");
        LambdaQueryWrapper<CheckitemDetail> wrapper = Wrappers.lambdaQuery(CheckitemDetail.class);
        List<CheckitemDetail> list = checkitemDetailMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }

    /**
     * 打印科室表数据
     */
    private void printDepartmentData() {
        System.out.println("\n===== 科室表(department)数据 =====");
        LambdaQueryWrapper<Department> wrapper = Wrappers.lambdaQuery(Department.class);
        List<Department> list = departmentMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }

    /**
     * 打印医生表数据
     */
    private void printDoctorData() {
        System.out.println("\n===== 医生表(doctor)数据 =====");
        LambdaQueryWrapper<Doctor> wrapper = Wrappers.lambdaQuery(Doctor.class);
        List<Doctor> list = doctorMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }

    /**
     * 打印医生排班表数据
     */
    private void printDoctorScheduleData() {
        System.out.println("\n===== 医生排班表(doctor_schedule)数据 =====");
        LambdaQueryWrapper<DoctorSchedule> wrapper = Wrappers.lambdaQuery(DoctorSchedule.class);
        List<DoctorSchedule> list = doctorScheduleMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }

    /**
     * 打印家庭成员表数据
     */
    private void printFamilyMemberData() {
        System.out.println("\n===== 家庭成员表(family_member)数据 =====");
        LambdaQueryWrapper<FamilyMember> wrapper = Wrappers.lambdaQuery(FamilyMember.class);
        List<FamilyMember> list = familyMemberMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }

    /**
     * 打印医院表数据
     */
    private void printHospitalData() {
        System.out.println("\n===== 医院表(hospital)数据 =====");
        LambdaQueryWrapper<Hospital> wrapper = Wrappers.lambdaQuery(Hospital.class);
        List<Hospital> list = hospitalMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }

    /**
     * 打印发票表数据
     */
    private void printInvoiceData() {
        System.out.println("\n===== 发票表(invoice)数据 =====");
        LambdaQueryWrapper<Invoice> wrapper = Wrappers.lambdaQuery(Invoice.class);
        List<Invoice> list = invoiceMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }

    /**
     * 打印通知表数据
     */
    private void printNotificationData() {
        System.out.println("\n===== 通知表(notification)数据 =====");
        LambdaQueryWrapper<Notification> wrapper = Wrappers.lambdaQuery(Notification.class);
        List<Notification> list = notificationMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }

    /**
     * 打印订单表数据
     */
    private void printOrdersData() {
        System.out.println("\n===== 订单表(orders)数据 =====");
        LambdaQueryWrapper<Orders> wrapper = Wrappers.lambdaQuery(Orders.class);
        List<Orders> list = ordersMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }

    /**
     * 打印总体结果表数据
     */
    private void printOverallResultData() {
        System.out.println("\n===== 总体结果表(overall_result)数据 =====");
        LambdaQueryWrapper<OverallResult> wrapper = Wrappers.lambdaQuery(OverallResult.class);
        List<OverallResult> list = overallResultMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }

    /**
     * 打印报告项表数据
     */
    private void printReportItemData() {
        System.out.println("\n===== 报告项表(report_item)数据 =====");
        LambdaQueryWrapper<ReportItem> wrapper = Wrappers.lambdaQuery(ReportItem.class);
        List<ReportItem> list = reportItemMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }

    /**
     * 打印报告项详情表数据
     */
    private void printReportItemDetailData() {
        System.out.println("\n===== 报告项详情表(report_item_detail)数据 =====");
        LambdaQueryWrapper<ReportItemDetail> wrapper = Wrappers.lambdaQuery(ReportItemDetail.class);
        List<ReportItemDetail> list = reportItemDetailMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }

    /**
     * 打印套餐表数据
     */
    private void printSetmealData() {
        System.out.println("\n===== 套餐表(setmeal)数据 =====");
        LambdaQueryWrapper<Setmeal> wrapper = Wrappers.lambdaQuery(Setmeal.class);
        List<Setmeal> list = setmealMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }

    /**
     * 打印套餐详情表数据
     */
    private void printSetmealDetailData() {
        System.out.println("\n===== 套餐详情表(setmeal_detail)数据 =====");
        LambdaQueryWrapper<SetmealDetail> wrapper = Wrappers.lambdaQuery(SetmealDetail.class);
        List<SetmealDetail> list = setmealDetailMapper.selectList(wrapper);
        list.forEach(System.out::println);
    }
} 