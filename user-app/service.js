// 导入用户相关API接口
import { userApi } from './utils/api.js';
// 导入HTTP请求方法
import { post } from './utils/request.js';
// 导入MD5加密库
import md5 from 'md5';

// 定义本地存储的键名常量
// 管理账号信息的存储键
const USERS_KEY = 'USERS_KEY';
// 管理token的存储键
const TOKEN_KEY = 'TOKEN_KEY';

// 获取所有用户信息的函数
const getUsers = function() {
  // 声明返回变量
  let ret = '';
  // 从本地存储中获取用户信息
  ret = uni.getStorageSync(USERS_KEY);
  // 如果没有获取到数据，则返回空数组字符串
  if (!ret) {
    ret = '[]';
  }
  // 将JSON字符串解析为对象并返回
  return JSON.parse(ret);
}

// 添加新用户信息的函数
const addUser = function(userInfo) {
  // 获取现有用户列表
  let users = getUsers();
  // 将新用户信息添加到用户列表中
  users.push({
    account: userInfo.account, // 用户账号
    password: userInfo.password // 用户密码
  });
  // 将更新后的用户列表保存到本地存储
  uni.setStorageSync(USERS_KEY, JSON.stringify(users));
}

// 账号密码登录函数
const accountLogin = async function(account, password) {
  try {
    // MD5加密密码
    // 确保password存在且为字符串
    if (!password || typeof password !== 'string') {
      throw new Error('密码参数无效');
    }
    // 对密码进行MD5加密并确保输出为字符串
    const encryptedPwd = md5(password).toString(); // 确保输出为字符串
    
    // 调用登录接口，传入用户名和加密后的密码
    const res = await post(userApi.accountLogin, {
      username: account, // 用户名
      password: encryptedPwd // 加密后的密码
    });
    
    // 如果响应中包含token，则存储到本地
    if (res.data && res.data.token) {
      uni.setStorageSync(TOKEN_KEY, res.data.token);
    }
    
    // 返回响应数据
    return res.data;
  } catch (error) {
    // 输出登录失败的错误信息
    console.error('登录失败:', error);
    // 抛出错误
    throw error;
  }
}

// 获取token的函数
const getToken = function() {
  // 优先获取uniIdToken，如果没有则获取TOKEN_KEY
  const uniIdToken = uni.getStorageSync('uniIdToken');
  // 如果存在uniIdToken，则返回它
  if (uniIdToken) {
    return uniIdToken;
  }
  // 否则返回TOKEN_KEY对应的token
  return uni.getStorageSync(TOKEN_KEY);
}

// 导出所有函数供其他模块使用
export default {
  getUsers, // 获取用户列表
  addUser, // 添加用户
  accountLogin, // 账号登录
  getToken // 获取token
}