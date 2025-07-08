<template>
  <div class="system-settings-container">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>系统设置</span>
        </div>
      </template>
      
      <el-tabs v-model="activeTab">
        <el-tab-pane label="基础设置" name="basic">
          <el-form ref="basicFormRef" :model="basicForm" label-width="180px">
            <el-form-item label="系统名称">
              <el-input v-model="basicForm.systemName" />
            </el-form-item>
            <el-form-item label="系统Logo">
              <el-upload
                class="avatar-uploader"
                action="#"
                :show-file-list="false"
                :auto-upload="false"
                :on-change="handleLogoChange">
                <img v-if="basicForm.logoUrl" :src="basicForm.logoUrl" class="avatar" />
                <el-icon v-else class="avatar-uploader-icon"><el-icon-plus /></el-icon>
              </el-upload>
            </el-form-item>
            <el-form-item label="系统描述">
              <el-input v-model="basicForm.description" type="textarea" :rows="3" />
            </el-form-item>
            <el-form-item label="技术支持联系方式">
              <el-input v-model="basicForm.supportContact" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveBasicSettings">保存设置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="预约设置" name="appointment">
          <el-form ref="appointmentFormRef" :model="appointmentForm" label-width="180px">
            <el-form-item label="预约提前天数">
              <el-input-number v-model="appointmentForm.advanceDays" :min="1" :max="30" />
              <span class="form-help-text">用户可提前预约的天数</span>
            </el-form-item>
            <el-form-item label="每日最大预约人数">
              <el-input-number v-model="appointmentForm.maxAppointmentsPerDay" :min="10" :max="500" />
            </el-form-item>
            <el-form-item label="预约时间段">
              <div v-for="(timeSlot, index) in appointmentForm.timeSlots" :key="index" class="time-slot-item">
                <el-time-picker
                  v-model="timeSlot.startTime"
                  format="HH:mm"
                  placeholder="开始时间"
                  style="width: 120px"
                />
                <span class="time-separator">-</span>
                <el-time-picker
                  v-model="timeSlot.endTime"
                  format="HH:mm"
                  placeholder="结束时间"
                  style="width: 120px"
                />
                <el-input-number
                  v-model="timeSlot.maxCount"
                  :min="1"
                  :max="50"
                  style="width: 120px; margin: 0 10px"
                  placeholder="人数上限"
                />
                <el-button type="danger" icon="el-icon-delete" circle @click="removeTimeSlot(index)" />
              </div>
              <div>
                <el-button type="primary" icon="el-icon-plus" @click="addTimeSlot">添加时间段</el-button>
              </div>
            </el-form-item>
            <el-form-item label="禁用预约日期">
              <el-date-picker
                v-model="appointmentForm.disabledDates"
                type="dates"
                placeholder="选择禁用日期"
                style="width: 100%"
              />
              <span class="form-help-text">选择不开放预约的日期（如节假日）</span>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveAppointmentSettings">保存设置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="支付设置" name="payment">
          <el-form ref="paymentFormRef" :model="paymentForm" label-width="180px">
            <el-form-item label="启用支付宝支付">
              <el-switch v-model="paymentForm.enableAlipay" />
            </el-form-item>
            <el-form-item label="支付宝AppId" v-if="paymentForm.enableAlipay">
              <el-input v-model="paymentForm.alipayAppId" />
            </el-form-item>
            <el-form-item label="支付宝私钥" v-if="paymentForm.enableAlipay">
              <el-input v-model="paymentForm.alipayPrivateKey" type="textarea" :rows="3" />
            </el-form-item>
            <el-form-item label="支付宝公钥" v-if="paymentForm.enableAlipay">
              <el-input v-model="paymentForm.alipayPublicKey" type="textarea" :rows="3" />
            </el-form-item>
            
            <el-divider />
            
            <el-form-item label="启用微信支付">
              <el-switch v-model="paymentForm.enableWechat" />
            </el-form-item>
            <el-form-item label="微信支付商户ID" v-if="paymentForm.enableWechat">
              <el-input v-model="paymentForm.wechatMchId" />
            </el-form-item>
            <el-form-item label="微信支付AppId" v-if="paymentForm.enableWechat">
              <el-input v-model="paymentForm.wechatAppId" />
            </el-form-item>
            <el-form-item label="微信支付密钥" v-if="paymentForm.enableWechat">
              <el-input v-model="paymentForm.wechatKey" type="textarea" :rows="2" />
            </el-form-item>
            
            <el-form-item>
              <el-button type="primary" @click="savePaymentSettings">保存设置</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="邮件设置" name="mail">
          <el-form ref="mailFormRef" :model="mailForm" label-width="180px">
            <el-form-item label="启用邮件通知">
              <el-switch v-model="mailForm.enableMail" />
            </el-form-item>
            <el-form-item label="SMTP服务器" v-if="mailForm.enableMail">
              <el-input v-model="mailForm.smtpServer" />
            </el-form-item>
            <el-form-item label="SMTP端口" v-if="mailForm.enableMail">
              <el-input v-model="mailForm.smtpPort" />
            </el-form-item>
            <el-form-item label="发件人邮箱" v-if="mailForm.enableMail">
              <el-input v-model="mailForm.senderEmail" />
            </el-form-item>
            <el-form-item label="发件人名称" v-if="mailForm.enableMail">
              <el-input v-model="mailForm.senderName" />
            </el-form-item>
            <el-form-item label="邮箱账号" v-if="mailForm.enableMail">
              <el-input v-model="mailForm.username" />
            </el-form-item>
            <el-form-item label="邮箱密码" v-if="mailForm.enableMail">
              <el-input v-model="mailForm.password" type="password" show-password />
            </el-form-item>
            <el-form-item label="启用SSL/TLS" v-if="mailForm.enableMail">
              <el-switch v-model="mailForm.enableSsl" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveMailSettings">保存设置</el-button>
              <el-button @click="testMailConnection">测试连接</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
        
        <el-tab-pane label="短信设置" name="sms">
          <el-form ref="smsFormRef" :model="smsForm" label-width="180px">
            <el-form-item label="启用短信通知">
              <el-switch v-model="smsForm.enableSms" />
            </el-form-item>
            <el-form-item label="短信服务商" v-if="smsForm.enableSms">
              <el-select v-model="smsForm.provider" style="width: 100%">
                <el-option label="阿里云" value="aliyun" />
                <el-option label="腾讯云" value="tencent" />
                <el-option label="华为云" value="huawei" />
              </el-select>
            </el-form-item>
            <el-form-item label="Access Key ID" v-if="smsForm.enableSms">
              <el-input v-model="smsForm.accessKeyId" />
            </el-form-item>
            <el-form-item label="Access Key Secret" v-if="smsForm.enableSms">
              <el-input v-model="smsForm.accessKeySecret" show-password />
            </el-form-item>
            <el-form-item label="短信签名" v-if="smsForm.enableSms">
              <el-input v-model="smsForm.signName" />
            </el-form-item>
            <el-form-item label="验证码模板ID" v-if="smsForm.enableSms">
              <el-input v-model="smsForm.verifyTemplateId" />
            </el-form-item>
            <el-form-item label="预约通知模板ID" v-if="smsForm.enableSms">
              <el-input v-model="smsForm.appointmentTemplateId" />
            </el-form-item>
            <el-form-item>
              <el-button type="primary" @click="saveSmsSettings">保存设置</el-button>
              <el-button @click="testSmsConnection">测试连接</el-button>
            </el-form-item>
          </el-form>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

// 激活的标签页
const activeTab = ref('basic')

// 基础设置表单
const basicForm = reactive({
  systemName: '预约体检系统',
  logoUrl: '',
  description: '预约体检系统是一个一体化平台，旨在实现体检用户在线预约体检服务、医生管理体检项目和查看体检报告、管理员进行系统维护和数据管理的功能。',
  supportContact: '400-123-4567'
})

// 预约设置表单
const appointmentForm = reactive({
  advanceDays: 14,
  maxAppointmentsPerDay: 100,
  timeSlots: [
    { startTime: new Date(2023, 0, 1, 8, 0), endTime: new Date(2023, 0, 1, 10, 0), maxCount: 30 },
    { startTime: new Date(2023, 0, 1, 10, 0), endTime: new Date(2023, 0, 1, 12, 0), maxCount: 30 },
    { startTime: new Date(2023, 0, 1, 14, 0), endTime: new Date(2023, 0, 1, 16, 0), maxCount: 30 }
  ],
  disabledDates: []
})

// 支付设置表单
const paymentForm = reactive({
  enableAlipay: true,
  alipayAppId: '2021000000000000',
  alipayPrivateKey: '',
  alipayPublicKey: '',
  enableWechat: false,
  wechatMchId: '',
  wechatAppId: '',
  wechatKey: ''
})

// 邮件设置表单
const mailForm = reactive({
  enableMail: false,
  smtpServer: 'smtp.example.com',
  smtpPort: '25',
  senderEmail: 'service@example.com',
  senderName: '预约体检系统',
  username: 'username',
  password: '',
  enableSsl: true
})

// 短信设置表单
const smsForm = reactive({
  enableSms: false,
  provider: 'aliyun',
  accessKeyId: '',
  accessKeySecret: '',
  signName: '预约体检系统',
  verifyTemplateId: '',
  appointmentTemplateId: ''
})

// 页面加载时获取系统设置
onMounted(async () => {
  try {
    // 实际项目中应从API获取系统设置
    // const res = await settingApi.getSettings()
    // if (res.data) {
    //   Object.assign(basicForm, res.data.basicSettings || {})
    //   Object.assign(appointmentForm, res.data.appointmentSettings || {})
    //   Object.assign(paymentForm, res.data.paymentSettings || {})
    //   Object.assign(mailForm, res.data.mailSettings || {})
    //   Object.assign(smsForm, res.data.smsSettings || {})
    // }
    
    // 这里使用模拟数据
    console.log('页面加载时获取系统设置')
  } catch (error) {
    console.error('获取系统设置失败:', error)
    ElMessage.error('获取系统设置失败')
  }
})

// 添加时间段
const addTimeSlot = () => {
  appointmentForm.timeSlots.push({
    startTime: new Date(2023, 0, 1, 8, 0),
    endTime: new Date(2023, 0, 1, 10, 0),
    maxCount: 30
  })
}

// 删除时间段
const removeTimeSlot = (index) => {
  appointmentForm.timeSlots.splice(index, 1)
}

// 处理Logo变更
const handleLogoChange = (file) => {
  // 实际项目中应上传到服务器
  const reader = new FileReader()
  reader.onload = (e) => {
    basicForm.logoUrl = e.target.result
  }
  reader.readAsDataURL(file.raw)
}

// 保存基础设置
const saveBasicSettings = async () => {
  try {
    // 实际项目中应调用API保存设置
    // await settingApi.updateSettings({ basicSettings: basicForm })
    ElMessage.success('基础设置保存成功')
  } catch (error) {
    console.error('保存基础设置失败:', error)
    ElMessage.error('保存基础设置失败')
  }
}

// 保存预约设置
const saveAppointmentSettings = async () => {
  try {
    // 实际项目中应调用API保存设置
    // await settingApi.updateSettings({ appointmentSettings: appointmentForm })
    ElMessage.success('预约设置保存成功')
  } catch (error) {
    console.error('保存预约设置失败:', error)
    ElMessage.error('保存预约设置失败')
  }
}

// 保存支付设置
const savePaymentSettings = async () => {
  try {
    // 实际项目中应调用API保存设置
    // await settingApi.updateSettings({ paymentSettings: paymentForm })
    ElMessage.success('支付设置保存成功')
  } catch (error) {
    console.error('保存支付设置失败:', error)
    ElMessage.error('保存支付设置失败')
  }
}

// 保存邮件设置
const saveMailSettings = async () => {
  try {
    // 实际项目中应调用API保存设置
    // await settingApi.updateSettings({ mailSettings: mailForm })
    ElMessage.success('邮件设置保存成功')
  } catch (error) {
    console.error('保存邮件设置失败:', error)
    ElMessage.error('保存邮件设置失败')
  }
}

// 测试邮件连接
const testMailConnection = () => {
  if (!mailForm.enableMail) {
    ElMessage.warning('请先启用邮件通知')
    return
  }
  
  ElMessage.info('正在测试邮件连接...')
  setTimeout(() => {
    ElMessage.success('邮件服务器连接成功')
  }, 1500)
}

// 保存短信设置
const saveSmsSettings = async () => {
  try {
    // 实际项目中应调用API保存设置
    // await settingApi.updateSettings({ smsSettings: smsForm })
    ElMessage.success('短信设置保存成功')
  } catch (error) {
    console.error('保存短信设置失败:', error)
    ElMessage.error('保存短信设置失败')
  }
}

// 测试短信连接
const testSmsConnection = () => {
  if (!smsForm.enableSms) {
    ElMessage.warning('请先启用短信通知')
    return
  }
  
  ElMessage.info('正在测试短信连接...')
  setTimeout(() => {
    ElMessage.success('短信服务连接成功')
  }, 1500)
}
</script>

<style scoped>
.system-settings-container {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.form-help-text {
  margin-left: 10px;
  font-size: 12px;
  color: #909399;
}

.time-slot-item {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}

.time-separator {
  margin: 0 10px;
}

.avatar-uploader {
  display: flex;
  justify-content: center;
}

.avatar-uploader .avatar {
  width: 178px;
  height: 178px;
  display: block;
}

.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  transition: var(--el-transition-duration-fast);
}

.avatar-uploader .el-upload:hover {
  border-color: var(--el-color-primary);
}

.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 178px;
  height: 178px;
  text-align: center;
}
</style> 