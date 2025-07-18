package com.fourth.medical.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.system.*;
import cn.hutool.system.oshi.CpuInfo;
import cn.hutool.system.oshi.OshiUtil;
import com.fourth.medical.framework.bean.*;
import oshi.SystemInfo;
import oshi.hardware.GlobalMemory;
import oshi.software.os.FileSystem;
import oshi.software.os.OSFileStore;
import oshi.software.os.OSProcess;
import oshi.software.os.OperatingSystem;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 服务器系统信息监控工具类
 * 
 * <p><strong>功能描述：</strong></p>
 * <ul>
 *   <li>提供全面的服务器系统信息获取功能</li>
 *   <li>监控CPU、内存、JVM、磁盘等系统资源</li>
 *   <li>支持实时系统性能指标统计</li>
 *   <li>提供操作系统和进程信息获取</li>
 * </ul>
 * 
 * <p><strong>主要特性：</strong></p>
 * <ul>
 *   <li>多维度监控：CPU、内存、JVM、磁盘、操作系统</li>
 *   <li>实时数据：获取当前系统运行状态</li>
 *   <li>格式化输出：自动转换文件大小单位</li>
 *   <li>异常处理：优雅处理系统信息获取异常</li>
 *   <li>跨平台支持：基于OSHI库支持多操作系统</li>
 * </ul>
 * 
 * <p><strong>使用场景：</strong></p>
 * <ul>
 *   <li>系统监控面板</li>
 *   <li>性能监控和告警</li>
 *   <li>系统资源使用分析</li>
 *   <li>运维管理和故障诊断</li>
 *   <li>系统健康检查</li>
 * </ul>
 * 
 * <p><strong>技术实现：</strong></p>
 * <ul>
 *   <li>基于OSHI（Operating System and Hardware Information）库</li>
 *   <li>集成Hutool工具库进行数据处理</li>
 *   <li>支持Windows、Linux、macOS等操作系统</li>
 *   <li>提供统一的系统信息封装</li>
 * </ul>
 * 
 * <p><strong>监控指标：</strong></p>
 * <ul>
 *   <li>CPU：核心数、使用率、空闲率</li>
 *   <li>内存：总量、已用、可用、使用率</li>
 *   <li>JVM：内存分配、版本信息、进程信息</li>
 *   <li>磁盘：容量、使用情况、文件系统类型</li>
 *   <li>操作系统：版本、架构、运行时间</li>
 * </ul>
 * 
 * <p><strong>扩展点：</strong></p>
 * <ul>
 *   <li>可扩展网络监控功能</li>
 *   <li>可添加历史数据记录</li>
 *   <li>可支持自定义监控指标</li>
 *   <li>可集成第三方监控系统</li>
 * </ul>
 *
 * @author geekidea
 * @date 2023/12/16
 **/
public class ServerInfoUtil {

    /**
     * 获取服务器信息
     *
     * @return
     */
    public static ServerInfo getServerInfo() {
        Cpu cpu = getCpu();
        Memory memory = getMemory();
        Jvm jvm = getJvm();
        List<Disk> diskList = getDiskList();
        OperatingSystemInfo operatingSystemInfo = getOperatingSystemInfo();
        ServerInfo serverInfo = new ServerInfo();
        serverInfo.setCpu(cpu);
        serverInfo.setMemory(memory);
        serverInfo.setJvm(jvm);
        serverInfo.setDiskList(diskList);
        serverInfo.setOperatingSystemInfo(operatingSystemInfo);
        return serverInfo;
    }

    /**
     * 获取CPU信息
     *
     * @return
     */
    public static Cpu getCpu() {
        try {
            CpuInfo cpuInfo = OshiUtil.getCpuInfo();
            // CPU核心数
            Integer cpuNum = cpuInfo.getCpuNum();
            // 总CPU使用率：获取用户+系统的总的CPU使用率
            double used = cpuInfo.getUsed();
            // CPU系统使用率
            double sys = cpuInfo.getSys();
            // CPU用户使用率
            double user = cpuInfo.getUser();
            // CPU当前空闲率
            double free = cpuInfo.getFree();
            Cpu cpu = new Cpu();
            cpu.setCpuNum(cpuNum);
            cpu.setSys(sys);
            cpu.setUser(user);
            cpu.setFree(free);
            cpu.setUsed(used);
            return cpu;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取内存信息
     *
     * @return
     */
    public static Memory getMemory() {
        try {
            GlobalMemory globalMemory = OshiUtil.getMemory();
            // 总内存
            long total = globalMemory.getTotal();
            // 空闲内存
            long available = globalMemory.getAvailable();
            // 已使用的
            long used = total - available;
            String totalUnit = FileUtil.readableFileSize(total);
            String availableUnit = FileUtil.readableFileSize(available);
            String usedUnit = FileUtil.readableFileSize(used);
            BigDecimal availableRate = NumberUtil.round(available * 1.0 / total * 100, 2);
            BigDecimal usedRate = NumberUtil.sub(100, availableRate);
            Memory memory = new Memory();
            memory.setTotal(total);
            memory.setAvailable(available);
            memory.setUsed(used);
            memory.setTotalUnit(totalUnit);
            memory.setAvailableUnit(availableUnit);
            memory.setUsedUnit(usedUnit);
            memory.setAvailableRate(availableRate);
            memory.setUsedRate(usedRate);
            return memory;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取JVM信息
     *
     * @return
     */
    public static Jvm getJvm() {
        try {
            RuntimeInfo runtimeInfo = SystemUtil.getRuntimeInfo();
            // JVM已分配内存
            long totalMemory = runtimeInfo.getTotalMemory();
            // JVM最大内存
            long maxMemory = runtimeInfo.getMaxMemory();
            // JVM最大可用内存
            long usableMemory = runtimeInfo.getUsableMemory();
            // JVM已分配内存中的剩余空间
            long freeMemory = runtimeInfo.getFreeMemory();
            String totalMemoryUnit = FileUtil.readableFileSize(totalMemory);
            String maxMemoryUnit = FileUtil.readableFileSize(maxMemory);
            String usableMemoryUnit = FileUtil.readableFileSize(usableMemory);
            String freeMemoryUnit = FileUtil.readableFileSize(freeMemory);
            JvmInfo jvmInfo = SystemUtil.getJvmInfo();
            // JVM名称
            String name = jvmInfo.getName();
            // JVM厂商
            String vendor = jvmInfo.getVendor();
            JavaInfo javaInfo = SystemUtil.getJavaInfo();
            // java版本
            String version = javaInfo.getVersion();
            // java按照路径
            String home = SystemUtil.get("java.home");
            OSProcess currentProcess = OshiUtil.getCurrentProcess();
            // 当前进程ID
            int processID = currentProcess.getProcessID();
            // 当前进程启动时间
            long startTime = currentProcess.getStartTime();
            // 当前进程已使用时间
            long userTime = currentProcess.getUserTime();
            // 当前进程已使用时间描述
            String userTimeDesc = DateDiffUtil.getDiffDaySecond(userTime);
            // 当前进程Cpu使用率
            double processCpuLoadCumulative = currentProcess.getProcessCpuLoadCumulative();
            BigDecimal processCpuRate = NumberUtil.round(processCpuLoadCumulative * 100, 2);
            Jvm jvm = new Jvm();
            jvm.setTotalMemory(totalMemory);
            jvm.setMaxMemory(maxMemory);
            jvm.setUsableMemory(usableMemory);
            jvm.setFreeMemory(freeMemory);
            jvm.setTotalMemoryUnit(totalMemoryUnit);
            jvm.setMaxMemoryUnit(maxMemoryUnit);
            jvm.setUsableMemoryUnit(usableMemoryUnit);
            jvm.setFreeMemoryUnit(freeMemoryUnit);
            jvm.setName(name);
            jvm.setVendor(vendor);
            jvm.setVersion(version);
            jvm.setHome(home);
            jvm.setProcessId(processID);
            jvm.setStartTime(new Date(startTime));
            jvm.setUserTime(userTime);
            jvm.setUserTimeDesc(userTimeDesc);
            jvm.setProcessCpuRate(processCpuRate);
            return jvm;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取磁盘列表
     *
     * @return
     */
    public static List<Disk> getDiskList() {
        try {
            SystemInfo systemInfo = new SystemInfo();
            OperatingSystem operatingSystem = systemInfo.getOperatingSystem();
            FileSystem fileSystem = operatingSystem.getFileSystem();
            List<OSFileStore> fileStores = fileSystem.getFileStores();
            List<Disk> disks = new ArrayList<>();
            for (OSFileStore fileStore : fileStores) {
                // 磁盘路径
                String mount = fileStore.getMount();
                // 文件系统类型
                String type = fileStore.getType();
                // 磁盘名称
                String name = fileStore.getName();
                // 磁盘总大小
                long totalSpace = fileStore.getTotalSpace();
                // 磁盘可用空间大小
                long usableSpace = fileStore.getUsableSpace();
                // 磁盘已使用空间大小
                long usedSpace = totalSpace - usableSpace;
                String totalSpaceUnit = FileUtil.readableFileSize(totalSpace);
                String usableSpaceUnit = FileUtil.readableFileSize(usableSpace);
                String usedSpaceUnit = FileUtil.readableFileSize(usedSpace);
                BigDecimal usableRate = NumberUtil.round(usableSpace * 1.0 / totalSpace * 100, 2);
                BigDecimal usedRate = NumberUtil.sub(100.0, usableRate);
                Disk disk = new Disk();
                disk.setMount(mount);
                disk.setType(type);
                disk.setName(name);
                disk.setTotalSpace(totalSpace);
                disk.setUsableSpace(usableSpace);
                disk.setUsedSpace(usedSpace);
                disk.setTotalSpaceUnit(totalSpaceUnit);
                disk.setUsableSpaceUnit(usableSpaceUnit);
                disk.setUsedSpaceSpaceUnit(usedSpaceUnit);
                disk.setUsableRate(usableRate);
                disk.setUsedRate(usedRate);
                disks.add(disk);
            }
            return disks;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取操作系统信息
     *
     * @return
     */
    public static OperatingSystemInfo getOperatingSystemInfo() {
        try {
            OsInfo osInfo = SystemUtil.getOsInfo();
            String name = osInfo.getName();
            String arch = osInfo.getArch();
            String version = osInfo.getVersion();
            HostInfo hostInfo = SystemUtil.getHostInfo();
            String address = hostInfo.getAddress();
            OperatingSystemInfo operatingSystemInfo = new OperatingSystemInfo();
            operatingSystemInfo.setName(name);
            operatingSystemInfo.setArch(arch);
            operatingSystemInfo.setVersion(version);
            operatingSystemInfo.setAddress(address);
            return operatingSystemInfo;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
