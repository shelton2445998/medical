import { userApi } from './utils/api.js';
import request from './utils/request.js';
import md5 from 'md5.js';

// 管理账号信息
const USERS_KEY = 'USERS_KEY';
const TOKEN_KEY = 'TOKEN_KEY';

const getUsers = function() {
  let ret = '';
  ret = uni.getStorageSync(USERS_KEY);
  if (!ret) {
    ret = '[]';
  }
  return JSON.parse(ret);
}

const addUser = function(userInfo) {
  let users = getUsers();
  users.push({
    account: userInfo.account,
    password: userInfo.password
  });
  uni.setStorageSync(USERS_KEY, JSON.stringify(users));
}

// 账号密码登录
const accountLogin = async function(account, password) {
  try {
    // MD5加密密码
           // 确保password存在且为字符串
           if (!password || typeof password !== 'string') {
             throw new Error('密码参数无效');
           }
           const encryptedPwd = md5(password).toString(); // 确保输出为字符串
    
    // 调用登录接口
    const res = await request.post(userApi.accountLogin, {
      username: account,
      password: encryptedPwd
    });
    
    // 存储token
    if (res.data && res.data.token) {
      uni.setStorageSync(TOKEN_KEY, res.data.token);
    }
    
    return res.data;
  } catch (error) {
    console.error('登录失败:', error);
    throw error;
  }
}

// 获取token
const getToken = function() {
  return uni.getStorageSync(TOKEN_KEY);
}

export default {
  getUsers,
  addUser,
  accountLogin,
  getToken
}