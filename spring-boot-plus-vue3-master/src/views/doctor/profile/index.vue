<template>
  <div class="profile-container">
    <el-row :gutter="20">
      <!-- 左侧：个人信息卡片 -->
      <el-col :span="8">
        <el-card shadow="hover">
          <div class="doctor-info">
            <div class="avatar-container">
              <el-avatar :size="100" :src="doctorInfo.avatar || defaultAvatar" />
            </div>
            <h2>{{ doctorInfo.name }}</h2>
            <div class="doctor-title">{{ doctorInfo.title }}</div>
            <div class="doctor-department">{{ doctorInfo.departmentName }}</div>
            <div class="doctor-hospital">{{ doctorInfo.hospitalName }}</div>
          </div>
          
          <el-divider />
          
          <div class="contact-info">
            <el-descriptions :column="1" border>
              <el-descriptions-item label="工号">{{ doctorInfo.workNo }}</el-descriptions-item>
              <el-descriptions-item label="手机号码">{{ doctorInfo.phone }}</el-descriptions-item>
              <el-descriptions-item label="邮箱">{{ doctorInfo.email }}</el-descriptions-item>
            </el-descriptions>
          </div>
          
          <div class="action-buttons">
            <el-button type="primary" @click="activeTab = 'basic'">编辑资料</el-button>
            <el-button type="warning" @click="activeTab = 'password'">修改密码</el-button>
          </div>
        </el-card>
      </el-col>
      
      <!-- 右侧：表单区域 -->
      <el-col :span="16">
        <el-card>
          <el-tabs v-model="activeTab">
            <!-- 基本信息表单 -->
            <el-tab-pane label="个人资料" name="basic">
              <el-form
                ref="basicFormRef"
                :model="basicForm"
                :rules="basicRules"
                label-width="100px"
                label-position="right"
              >
                <el-form-item label="姓名" prop="name">
                  <el-input v-model="basicForm.name" />
                </el-form-item>
                <el-form-item label="性别" prop="gender">
                  <el-select v-model="basicForm.gender" style="width: 100%">
                    <el-option label="男" value="男" />
                    <el-option label="女" value="女" />
                  </el-select>
                </el-form-item>
                <el-form-item label="手机号" prop="phone">
                  <el-input v-model="basicForm.phone" />
                </el-form-item>
                <el-form-item label="邮箱" prop="email">
                  <el-input v-model="basicForm.email" />
                </el-form-item>
                <el-form-item label="医院" prop="hospitalId">
                  <el-select v-model="basicForm.hospitalId" @change="handleHospitalChange" style="width: 100%">
                    <el-option
                      v-for="item in hospitalOptions"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="科室" prop="departmentId">
                  <el-select v-model="basicForm.departmentId" style="width: 100%">
                    <el-option
                      v-for="item in departmentOptions"
                      :key="item.id"
                      :label="item.name"
                      :value="item.id"
                    />
                  </el-select>
                </el-form-item>
                <el-form-item label="职称" prop="title">
                  <el-select v-model="basicForm.title" style="width: 100%">
                    <el-option label="主任医师" value="主任医师" />
                    <el-option label="副主任医师" value="副主任医师" />
                    <el-option label="主治医师" value="主治医师" />
                    <el-option label="住院医师" value="住院医师" />
                  </el-select>
                </el-form-item>
                <el-form-item label="擅长领域" prop="specialty">
                  <el-input v-model="basicForm.specialty" type="textarea" :rows="3" />
                </el-form-item>
                <el-form-item label="头像" prop="avatar">
                  <el-upload
                    class="avatar-uploader"
                    action="/api/doctor/common/upload"
                    :show-file-list="false"
                    :on-success="handleAvatarSuccess"
                    :before-upload="beforeAvatarUpload"
                  >
                    <img v-if="basicForm.avatar" :src="basicForm.avatar" class="avatar" />
                    <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
                  </el-upload>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="submitBasicForm">保存</el-button>
                  <el-button @click="resetBasicForm">重置</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
            
            <!-- 修改密码表单 -->
            <el-tab-pane label="修改密码" name="password">
              <el-form
                ref="passwordFormRef"
                :model="passwordForm"
                :rules="passwordRules"
                label-width="120px"
                label-position="right"
              >
                <el-form-item label="原密码" prop="oldPassword">
                  <el-input
                    v-model="passwordForm.oldPassword"
                    type="password"
                    placeholder="请输入原密码"
                    show-password
                  />
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                  <el-input
                    v-model="passwordForm.newPassword"
                    type="password"
                    placeholder="请输入新密码"
                    show-password
                  />
                </el-form-item>
                <el-form-item label="确认新密码" prop="confirmPassword">
                  <el-input
                    v-model="passwordForm.confirmPassword"
                    type="password"
                    placeholder="请再次输入新密码"
                    show-password
                  />
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="submitPasswordForm">修改密码</el-button>
                  <el-button @click="resetPasswordForm">重置</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>
            
            <!-- 出诊设置表单 -->
            <el-tab-pane label="出诊设置" name="schedule">
              <el-calendar v-model="currentDate">
                <template #dateCell="{ data }">
                  <div class="calendar-date-cell">
                    <div class="calendar-date">{{ data.day.split('-')[2] }}</div>
                    <div
                      class="schedule-status"
                      :class="{
                        'is-scheduled': isScheduledDate(data.day),
                        'is-holiday': isHolidayDate(data.day)
                      }"
                    >
                      {{ getScheduleText(data.day) }}
                    </div>
                    <div class="edit-schedule">
                      <el-button link @click.stop="openScheduleDialog(data.day)">设置</el-button>
                    </div>
                  </div>
                </template>
              </el-calendar>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
    
    <!-- 出诊设置对话框 -->
    <el-dialog v-model="scheduleDialog.visible" :title="`出诊设置 - ${scheduleDialog.date}`" width="500px">
      <el-form :model="scheduleForm" label-width="100px">
        <el-form-item label="出诊状态">
          <el-select v-model="scheduleForm.status" style="width: 100%">
            <el-option label="正常出诊" value="normal" />
            <el-option label="停诊" value="stopped" />
            <el-option label="假期" value="holiday" />
          </el-select>
        </el-form-item>
        <el-form-item label="时段设置" v-if="scheduleForm.status === 'normal'">
          <div class="time-slots">
            <div v-for="(slot, index) in scheduleForm.timeSlots" :key="index" class="time-slot-item">
              <el-checkbox v-model="slot.enabled">{{ slot.name }}</el-checkbox>
              <el-input-number v-model="slot.quota" :min="0" :max="20" :disabled="!slot.enabled" />
            </div>
          </div>
        </el-form-item>
        <el-form-item label="备注">
          <el-input v-model="scheduleForm.remark" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="scheduleDialog.visible = false">取消</el-button>
        <el-button type="primary" @click="saveSchedule">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue';
import { ElMessage } from 'element-plus';
import { Plus } from '@element-plus/icons-vue';
import { getProfileApi, updateProfileApi, updatePasswordApi, getDepartmentListApi, getHospitalListApi } from '@/api/doctor';

// 默认头像
const defaultAvatar = 'https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png';

// 当前激活的标签页
const activeTab = ref('basic');

// 医生信息
const doctorInfo = ref({
  id: '',
  workNo: '',
  name: '',
  gender: '',
  phone: '',
  email: '',
  avatar: '',
  hospitalId: '',
  hospitalName: '',
  departmentId: '',
  departmentName: '',
  title: '',
  specialty: ''
});

// 基本信息表单
const basicFormRef = ref(null);
const basicForm = reactive({
  name: '',
  gender: '',
  phone: '',
  email: '',
  hospitalId: '',
  departmentId: '',
  title: '',
  specialty: '',
  avatar: ''
});

// 表单验证规则
const basicRules = {
  name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
  gender: [{ required: true, message: '请选择性别', trigger: 'change' }],
  phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
  hospitalId: [{ required: true, message: '请选择医院', trigger: 'change' }],
  departmentId: [{ required: true, message: '请选择科室', trigger: 'change' }]
};

// 密码表单
const passwordFormRef = ref(null);
const passwordForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
});

// 密码表单验证规则
const passwordRules = {
  oldPassword: [{ required: true, message: '请输入原密码', trigger: 'blur' }],
  newPassword: [
    { required: true, message: '请输入新密码', trigger: 'blur' },
    { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
  ],
  confirmPassword: [
    { required: true, message: '请再次输入新密码', trigger: 'blur' },
    {
      validator: (rule, value, callback) => {
        if (value !== passwordForm.newPassword) {
          callback(new Error('两次输入的密码不一致'));
        } else {
          callback();
        }
      },
      trigger: 'blur'
    }
  ]
};

// 出诊设置相关
const currentDate = ref(new Date());
const scheduledDates = ref({});

// 医院和科室选项
const hospitalOptions = ref([]);
const departmentOptions = ref([]);

// 出诊设置对话框
const scheduleDialog = reactive({
  visible: false,
  date: ''
});

// 出诊设置表单
const scheduleForm = reactive({
  status: 'normal',
  timeSlots: [
    { name: '上午 8:00-10:00', enabled: true, quota: 10 },
    { name: '上午 10:00-12:00', enabled: true, quota: 10 },
    { name: '下午 14:00-16:00', enabled: true, quota: 10 },
    { name: '下午 16:00-18:00', enabled: true, quota: 10 }
  ],
  remark: ''
});

// 初始化加载
onMounted(async () => {
  await Promise.all([
    getDoctorInfo(),
    getHospitals(),
  ]);
});

// 获取医生个人信息
const getDoctorInfo = async () => {
  try {
    const res = await getProfileApi();
    if (res && res.data) {
      doctorInfo.value = res.data;
      
      // 初始化表单数据
      basicForm.name = doctorInfo.value.name || '';
      basicForm.gender = doctorInfo.value.gender || '';
      basicForm.phone = doctorInfo.value.phone || '';
      basicForm.email = doctorInfo.value.email || '';
      basicForm.hospitalId = doctorInfo.value.hospitalId || '';
      basicForm.departmentId = doctorInfo.value.departmentId || '';
      basicForm.title = doctorInfo.value.title || '';
      basicForm.specialty = doctorInfo.value.specialty || '';
      basicForm.avatar = doctorInfo.value.avatar || '';
      
      // 加载科室选项
      if (basicForm.hospitalId) {
        await getDepartments(basicForm.hospitalId);
      }
    }
  } catch (error) {
    console.error('获取医生信息失败:', error);
    ElMessage.error('获取医生信息失败');
  }
};

// 获取医院列表
const getHospitals = async () => {
  try {
    const res = await getHospitalListApi();
    if (res && res.data) {
      hospitalOptions.value = res.data;
    }
  } catch (error) {
    console.error('获取医院列表失败:', error);
    ElMessage.error('获取医院列表失败');
  }
};

// 获取科室列表
const getDepartments = async (hospitalId) => {
  try {
    const res = await getDepartmentListApi(hospitalId);
    if (res && res.data) {
      departmentOptions.value = res.data;
    }
  } catch (error) {
    console.error('获取科室列表失败:', error);
    ElMessage.error('获取科室列表失败');
  }
};

// 医院选择变化时，更新科室列表
const handleHospitalChange = async (hospitalId) => {
  basicForm.departmentId = ''; // 重置科室选择
  await getDepartments(hospitalId);
};

// 提交基本信息表单
const submitBasicForm = async () => {
  if (!basicFormRef.value) return;
  
  await basicFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const res = await updateProfileApi(basicForm);
        if (res && res.success) {
          ElMessage.success('个人资料更新成功');
          await getDoctorInfo();
        }
      } catch (error) {
        console.error('更新个人资料失败:', error);
        ElMessage.error('更新个人资料失败');
      }
    }
  });
};

// 重置基本信息表单
const resetBasicForm = () => {
  if (basicFormRef.value) {
    basicFormRef.value.resetFields();
    // 重新加载表单数据
    basicForm.name = doctorInfo.value.name || '';
    basicForm.gender = doctorInfo.value.gender || '';
    basicForm.phone = doctorInfo.value.phone || '';
    basicForm.email = doctorInfo.value.email || '';
    basicForm.hospitalId = doctorInfo.value.hospitalId || '';
    basicForm.departmentId = doctorInfo.value.departmentId || '';
    basicForm.title = doctorInfo.value.title || '';
    basicForm.specialty = doctorInfo.value.specialty || '';
    basicForm.avatar = doctorInfo.value.avatar || '';
  }
};

// 提交密码修改表单
const submitPasswordForm = async () => {
  if (!passwordFormRef.value) return;
  
  await passwordFormRef.value.validate(async (valid) => {
    if (valid) {
      try {
        const res = await updatePasswordApi({
          oldPassword: passwordForm.oldPassword,
          newPassword: passwordForm.newPassword
        });
        if (res && res.success) {
          ElMessage.success('密码修改成功');
          resetPasswordForm();
        }
      } catch (error) {
        console.error('修改密码失败:', error);
        ElMessage.error('修改密码失败');
      }
    }
  });
};

// 重置密码表单
const resetPasswordForm = () => {
  if (passwordFormRef.value) {
    passwordFormRef.value.resetFields();
    passwordForm.oldPassword = '';
    passwordForm.newPassword = '';
    passwordForm.confirmPassword = '';
  }
};

// 处理头像上传成功
const handleAvatarSuccess = (res) => {
  if (res && res.success && res.data) {
    basicForm.avatar = res.data.url;
  }
};

// 头像上传前的验证
const beforeAvatarUpload = (file) => {
  const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
  const isLt2M = file.size / 1024 / 1024 < 2;

  if (!isJpgOrPng) {
    ElMessage.error('上传头像图片只能是 JPG 或 PNG 格式!');
  }
  
  if (!isLt2M) {
    ElMessage.error('上传头像图片大小不能超过 2MB!');
  }
  
  return isJpgOrPng && isLt2M;
};

// 是否是安排出诊的日期
const isScheduledDate = (date) => {
  return scheduledDates.value[date] && scheduledDates.value[date].status === 'normal';
};

// 是否是假期日期
const isHolidayDate = (date) => {
  return scheduledDates.value[date] && scheduledDates.value[date].status === 'holiday';
};

// 获取日程显示文本
const getScheduleText = (date) => {
  if (!scheduledDates.value[date]) {
    return '';
  }
  
  const schedule = scheduledDates.value[date];
  if (schedule.status === 'normal') {
    return '出诊';
  } else if (schedule.status === 'stopped') {
    return '停诊';
  } else if (schedule.status === 'holiday') {
    return '休假';
  }
  return '';
};

// 打开出诊设置对话框
const openScheduleDialog = (date) => {
  scheduleDialog.date = date;
  scheduleDialog.visible = true;
  
  // 初始化表单数据
  if (scheduledDates.value[date]) {
    const schedule = scheduledDates.value[date];
    scheduleForm.status = schedule.status;
    scheduleForm.remark = schedule.remark || '';
    
    if (schedule.timeSlots) {
      scheduleForm.timeSlots.forEach((slot, index) => {
        if (schedule.timeSlots[index]) {
          slot.enabled = schedule.timeSlots[index].enabled;
          slot.quota = schedule.timeSlots[index].quota;
        }
      });
    }
  } else {
    // 重置为默认值
    scheduleForm.status = 'normal';
    scheduleForm.remark = '';
    scheduleForm.timeSlots.forEach(slot => {
      slot.enabled = true;
      slot.quota = 10;
    });
  }
};

// 保存出诊设置
const saveSchedule = () => {
  scheduledDates.value = {
    ...scheduledDates.value,
    [scheduleDialog.date]: {
      status: scheduleForm.status,
      timeSlots: [...scheduleForm.timeSlots],
      remark: scheduleForm.remark
    }
  };
  
  ElMessage.success('出诊设置已保存');
  scheduleDialog.visible = false;
};
</script>

<style scoped>
.profile-container {
  padding: 20px;
}

.doctor-info {
  text-align: center;
  padding: 20px 0;
}

.avatar-container {
  margin-bottom: 20px;
}

.doctor-title {
  color: #409EFF;
  margin: 5px 0;
}

.doctor-department,
.doctor-hospital {
  color: #606266;
  margin-bottom: 5px;
}

.contact-info {
  margin: 20px 0;
}

.action-buttons {
  display: flex;
  justify-content: center;
  gap: 10px;
  margin-top: 20px;
}

.avatar-uploader {
  display: flex;
  justify-content: center;
  margin-top: 10px;
}

.avatar-uploader .avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  display: block;
}

.avatar-uploader .el-icon.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 100px;
  height: 100px;
  line-height: 100px;
  text-align: center;
  border: 1px dashed #d9d9d9;
  border-radius: 50%;
}

.calendar-date-cell {
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: space-between;
  padding: 4px 0;
}

.calendar-date {
  font-size: 14px;
  margin-bottom: 5px;
}

.schedule-status {
  font-size: 12px;
  padding: 2px 6px;
  border-radius: 4px;
}

.schedule-status.is-scheduled {
  background-color: #409EFF;
  color: white;
}

.schedule-status.is-holiday {
  background-color: #F56C6C;
  color: white;
}

.edit-schedule {
  margin-top: 5px;
  font-size: 12px;
  visibility: hidden;
}

.calendar-date-cell:hover .edit-schedule {
  visibility: visible;
}

.time-slots {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.time-slot-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-right: 20px;
}
</style> 