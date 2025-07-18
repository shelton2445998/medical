// API 配置文件
// 注意：如果服务器无法访问，请检查以下地址：
// 1. http://localhost:8888/api (本地开发，推荐)
// 2. http://39.104.57.236:8888/api (远程服务器)

// 环境配置 - 统一使用本地地址进行测试
// 定义API基础URL，指向本地开发服务器
const API_BASE_URL = 'http://localhost:8888/api';

// 如果本地服务器无法访问，可以手动切换到远程地址
// const API_BASE_URL = 'http://39.104.57.236:8888/api';

// 输出API配置信息到控制台，用于调试
console.log('API配置 - 基础URL:', API_BASE_URL);

// 医院相关接口配置对象
export const hospitalApi = {
  // 获取推荐医院列表的接口地址
  getRecommendHospitals: `${API_BASE_URL}/hospital/recommend`,
  // 获取医院列表的接口地址
  getHospitalList: `${API_BASE_URL}/hospital/list`,
  // 获取医院详情的接口地址，需要传入医院ID
  getHospitalDetail: (id) => `${API_BASE_URL}/hospital/detail/${id}`,
};

// 套餐相关接口配置对象
export const packageApi = {
  // 获取套餐列表的接口地址
  getPackageList: `${API_BASE_URL}/package/list`,
  // 获取套餐详情的接口地址，需要传入套餐ID
  getPackageDetail: (id) => `${API_BASE_URL}/package/detail/${id}`,
  // 获取推荐套餐的接口地址
  getRecommendPackages: `${API_BASE_URL}/package/recommend`,
  // 获取套餐详情的接口地址（使用setmeal表），需要传入套餐ID
  getSetmealDetail: (id) => `${API_BASE_URL}/app/setmealDetail/getAppSetmealDetail/${id}`,
  // 获取套餐详情分页列表的接口地址
  getSetmealDetailPage: `${API_BASE_URL}/app/setmealDetail/getAppSetmealDetailPage`
};

// 预约相关接口配置对象 - 修正为正确的后端接口路径
export const appointmentApi = {
  // 创建预约的接口地址
  createAppointment: `${API_BASE_URL}/app/appointment/create`,
  // 获取预约列表的接口地址
  getAppointmentList: `${API_BASE_URL}/app/appointment/list`,
  // 获取预约详情的接口地址，需要传入预约ID
  getAppointmentDetail: (id) => `${API_BASE_URL}/app/appointment/detail/${id}`,
  // 取消预约的接口地址，需要传入预约ID
  cancelAppointment: (id) => `${API_BASE_URL}/app/appointment/cancel/${id}`,
  // 确认支付的接口地址，需要传入预约ID
  confirmPayment: (id) => `${API_BASE_URL}/app/appointment/confirmPayment/${id}`,
  // 删除预约的接口地址，需要传入预约ID
  deleteAppointment: (id) => `${API_BASE_URL}/app/appointment/delete/${id}`,
  // 获取医生列表的接口地址
  getDoctorList: `${API_BASE_URL}/app/doctor/getAppDoctorPage`,
  // 根据医院和部门查找医生的接口地址，需要传入医院ID和部门ID
  getDoctorByHospitalAndDepartment: (hospitalId, departmentId) => `${API_BASE_URL}/app/doctor/getDoctorByHospitalAndDepartment?hospitalId=${hospitalId}&departmentId=${departmentId}`,
};

// 检查项相关接口配置对象
export const checkitemApi = {
  // 获取检查项列表的接口地址
  getCheckitemList: `${API_BASE_URL}/app/checkitem/getAppCheckitemPage`,
  // 获取检查项详情的接口地址，需要传入检查项ID
  getCheckitemDetail: (id) => `${API_BASE_URL}/app/checkitem/getAppCheckitem/${id}`,
  // 获取检查项明细列表的接口地址
  getCheckitemDetailList: `${API_BASE_URL}/app/checkitemDetail/getAppCheckitemDetailPage`,
  // 根据检查项ID获取部门信息的接口地址，需要传入检查项ID列表
  getDepartmentByCheckitemIds: (checkitemIds) => `${API_BASE_URL}/app/checkitem/getDepartmentByCheckitemIds?checkitemIds=${checkitemIds}`,
};

// 报告相关接口配置对象
export const reportApi = {
  // 获取报告列表的接口地址
  getReportList: `${API_BASE_URL}/app/report/getAppReportPage`,
  // 获取报告详情的接口地址，需要传入报告ID
  getReportDetail: (id) => `${API_BASE_URL}/app/report/getAppReport/${id}`,
  // 获取App报告检查项信息的接口地址，需要传入报告项ID
  getAppReportItem: (id) => `${API_BASE_URL}/app/reportItem/getAppReportItem/${id}`,
  // 获取App报告检查项信息分页列表的接口地址
  getAppReportItemPage: `${API_BASE_URL}/app/reportItem/getAppReportItemPage`,
};

// 用户相关接口配置对象
export const userApi = {
  // 用户登录的接口地址
  login: `${API_BASE_URL}/user/login`,
  // 账号密码登录的接口地址
  accountLogin: `${API_BASE_URL}/app/accountLogin`,
  // 用户注册的接口地址
  register: `${API_BASE_URL}/app/register`,
  // 获取用户信息的接口地址
  getUserInfo: `${API_BASE_URL}/user/info`,
  // 获取登录用户信息的接口地址
  getLoginUserInfo: `${API_BASE_URL}/app/getLoginUserInfo`,
  // 更新用户个人信息的接口地址（使用User表接口）
  updateUserProfile: `${API_BASE_URL}/app/updateUserProfile`,
};

// AI聊天相关接口配置对象
export const chatApi = {
  // 发送消息给AI的接口地址
  sendMessage: `${API_BASE_URL}/app/chat/send`,
  // 获取聊天历史的接口地址
  getChatHistory: `${API_BASE_URL}/app/chat/history`,
  // 清空聊天记录的接口地址
  clearChatHistory: `${API_BASE_URL}/app/chat/clear`,
  // 获取AI医生列表的接口地址
  getAIDoctors: `${API_BASE_URL}/app/chat/doctors`,
};

// 默认导出所有API配置对象
export default {
  hospitalApi, // 医院相关接口
  packageApi, // 套餐相关接口
  appointmentApi, // 预约相关接口
  checkitemApi, // 检查项相关接口
  reportApi, // 报告相关接口
  userApi, // 用户相关接口
  chatApi, // AI聊天相关接口
}; 