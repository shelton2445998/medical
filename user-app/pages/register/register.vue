<template>
	<view class="content">
		<!-- 页面标题 -->
		<view class="page-header">
			<view class="back-btn" @click="goBack">
				<text class="iconfont icon-back">&#xe65e;</text>
			</view>
			<text class="page-title">欢迎注册熙康健康</text>
		</view>
		
		<!-- 注册表单 -->
		<view class="register-form">
			<!-- 手机号 -->
			<view class="form-item">
				<text class="iconfont icon-phone">&#xe639;</text>
				<input type="number" v-model="registerForm.mobile" placeholder="请输入手机号码" maxlength="11" />
				<button class="code-btn" :disabled="codeSending || countdown > 0" @click="sendVerifyCode">
					{{ countdown > 0 ? `${countdown}s后重发` : '获取验证码' }}
				</button>
			</view>
			
			<!-- 验证码 -->
			<view class="form-item">
				<text class="iconfont icon-code">&#xe60f;</text>
				<input type="number" v-model="registerForm.verifyCode" placeholder="请输入验证码" maxlength="6" />
			</view>
			
			<!-- 真实姓名 -->
			<view class="form-item">
				<text class="iconfont icon-user">&#xe60f;</text>
				<input type="text" v-model="registerForm.realName" placeholder="请输入真实姓名" />
			</view>
			
			<!-- 身份证号 -->
			<view class="form-item">
				<text class="iconfont icon-idcard">&#xe60f;</text>
				<input type="idcard" v-model="registerForm.idCard" placeholder="请输入身份证号码" maxlength="18" />
			</view>
			
			<!-- 性别 -->
			<view class="form-item">
				<text class="iconfont icon-gender">&#xe60f;</text>
				<view class="gender-selector">
					<view class="gender-option" :class="{active: registerForm.gender === 1}" @click="registerForm.gender = 1">
						<text class="gender-icon">&#xe60f;</text>
						<text>男</text>
					</view>
					<view class="gender-option" :class="{active: registerForm.gender === 0}" @click="registerForm.gender = 0">
						<text class="gender-icon">&#xe60f;</text>
						<text>女</text>
					</view>
				</view>
			</view>
			
			<!-- 密码 -->
			<view class="form-item">
				<text class="iconfont icon-lock">&#xe60f;</text>
				<input type="password" v-model="registerForm.password" placeholder="请设置登录密码" password />
			</view>
			
			<!-- 确认密码 -->
			<view class="form-item">
				<text class="iconfont icon-lock">&#xe60f;</text>
				<input type="password" v-model="registerForm.confirmPassword" placeholder="请确认登录密码" password />
			</view>
			
			<!-- 协议同意 -->
			<view class="agreement">
				<checkbox-group @change="changeAgree">
					<label>
						<checkbox :checked="isAgree" color="#1296db" style="transform:scale(0.7)" />
						<text>我已阅读并同意</text>
						<text class="link" @click.stop="showAgreement('user')">《用户协议》</text>
						<text>和</text>
						<text class="link" @click.stop="showAgreement('privacy')">《隐私政策》</text>
					</label>
				</checkbox-group>
			</view>
			
			<!-- 注册按钮 -->
			<button class="register-btn" :disabled="!isAgree" @click="handleRegister">注 册</button>
			
			<!-- 登录链接 -->
			<view class="login-link">
				<text>已有账号？</text>
				<text class="link" @click="navigateTo('/pages/login/login')">立即登录</text>
			</view>
		</view>
	</view>
</template>

<script>
	import service from '../../service.js'
	
	export default {
		data() {
			return {
				registerForm: {
					mobile: '',
					verifyCode: '',
					realName: '',
					idCard: '',
					gender: 1, // 默认男性
					password: '',
					confirmPassword: ''
				},
				isAgree: false,
				countdown: 0,
				codeSending: false,
				timer: null
			}
		},
		onUnload() {
			// 清除定时器
			if (this.timer) {
				clearInterval(this.timer);
				this.timer = null;
			}
		},
		methods: {
			// 发送验证码
			sendVerifyCode() {
				// 验证手机号
				if (!this.validateMobile()) {
					return;
				}
				
				this.codeSending = true;
				
				// 模拟发送验证码
				setTimeout(() => {
					this.codeSending = false;
					this.countdown = 60;
					
					// 倒计时
					this.timer = setInterval(() => {
						if (this.countdown > 0) {
							this.countdown--;
						} else {
							clearInterval(this.timer);
							this.timer = null;
						}
					}, 1000);
					
					uni.showToast({
						title: '验证码已发送',
						icon: 'success'
					});
				}, 1000);
			},
			// 验证手机号
			validateMobile() {
				const mobileReg = /^1[3-9]\d{9}$/;
				if (!this.registerForm.mobile) {
					uni.showToast({
						title: '请输入手机号码',
						icon: 'none'
					});
					return false;
				}
				if (!mobileReg.test(this.registerForm.mobile)) {
					uni.showToast({
						title: '手机号格式不正确',
						icon: 'none'
					});
					return false;
				}
				return true;
			},
			// 验证身份证号
			validateIdCard() {
				const idCardReg = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;
				if (!this.registerForm.idCard) {
					uni.showToast({
						title: '请输入身份证号码',
						icon: 'none'
					});
					return false;
				}
				if (!idCardReg.test(this.registerForm.idCard)) {
					uni.showToast({
						title: '身份证号格式不正确',
						icon: 'none'
					});
					return false;
				}
				return true;
			},
			// 验证表单
			validateForm() {
				if (!this.validateMobile()) {
					return false;
				}
				
				if (!this.registerForm.verifyCode) {
					uni.showToast({
						title: '请输入验证码',
						icon: 'none'
					});
					return false;
				}
				
				if (!this.registerForm.realName) {
					uni.showToast({
						title: '请输入真实姓名',
						icon: 'none'
					});
					return false;
				}
				
				if (!this.validateIdCard()) {
					return false;
				}
				
				if (!this.registerForm.password) {
					uni.showToast({
						title: '请设置登录密码',
						icon: 'none'
					});
					return false;
				}
				
				if (this.registerForm.password.length < 6) {
					uni.showToast({
						title: '密码长度不能少于6位',
						icon: 'none'
					});
					return false;
				}
				
				if (this.registerForm.password !== this.registerForm.confirmPassword) {
					uni.showToast({
						title: '两次密码输入不一致',
						icon: 'none'
					});
					return false;
				}
				
				if (!this.isAgree) {
					uni.showToast({
						title: '请阅读并同意用户协议和隐私政策',
						icon: 'none'
					});
					return false;
				}
				
				return true;
			},
			// 处理注册
			handleRegister() {
				if (!this.validateForm()) {
					return;
				}
				
				// 模拟注册
				uni.showLoading({
					title: '注册中...'
				});
				
				// 这里可以替换为实际的API调用
				setTimeout(() => {
					uni.hideLoading();
					
					// 添加用户
					service.addUser({
						account: this.registerForm.mobile,
						password: this.registerForm.password
					});
					
					uni.showToast({
						title: '注册成功',
						icon: 'success'
					});
					
					// 注册成功后跳转到登录页
					setTimeout(() => {
						uni.redirectTo({
							url: '/pages/login/login'
						});
					}, 1500);
				}, 2000);
			},
			// 切换协议同意状态
			changeAgree(e) {
				this.isAgree = e.detail.value.length > 0;
			},
			// 显示协议
			showAgreement(type) {
				const title = type === 'user' ? '用户协议' : '隐私政策';
				uni.navigateTo({
					url: `/pages/agreement/agreement?type=${type}&title=${title}`
				});
			},
			// 页面跳转
			navigateTo(url) {
				uni.navigateTo({
					url: url
				});
			},
			// 返回上一页
			goBack() {
				uni.navigateBack();
			}
		}
	}
</script>

<style lang="scss">
@font-face {
	font-family: texticons;
	font-weight: normal;
	font-style: normal;
	src: url('https://at.alicdn.com/t/font_984210_5cs13ndgqsn.ttf') format('truetype');
}

.content {
	background-color: #ffffff;
	min-height: 100vh;
	display: flex;
	flex-direction: column;
}

.page-header {
	position: relative;
	background-color: #1296db;
	padding: 40rpx 0;
	text-align: center;
	
	.back-btn {
		position: absolute;
		left: 30rpx;
		top: 50%;
		transform: translateY(-50%);
		width: 60rpx;
		height: 60rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		
		.iconfont {
			color: #ffffff;
			font-size: 40rpx;
			font-family: texticons;
		}
	}
	
	.page-title {
		color: #ffffff;
		font-size: 36rpx;
		font-weight: bold;
	}
}

.register-form {
	padding: 40rpx;
	
	.form-item {
		position: relative;
		display: flex;
		align-items: center;
		border-bottom: 1px solid #eeeeee;
		padding: 20rpx 0;
		margin-bottom: 20rpx;
		
		.iconfont {
			font-size: 40rpx;
			color: #999999;
			margin-right: 20rpx;
			font-family: texticons;
		}
		
		input {
			flex: 1;
			font-size: 30rpx;
			color: #333333;
		}
		
		.code-btn {
			background-color: #1296db;
			color: #ffffff;
			font-size: 24rpx;
			padding: 0 20rpx;
			height: 60rpx;
			line-height: 60rpx;
			border-radius: 30rpx;
			margin-left: 20rpx;
			
			&[disabled] {
				background-color: #cccccc;
				color: #ffffff;
			}
			
			&::after {
				border: none;
			}
		}
		
		.gender-selector {
			flex: 1;
			display: flex;
			
			.gender-option {
				display: flex;
				align-items: center;
				margin-right: 40rpx;
				padding: 10rpx 0;
				color: #666666;
				
				&.active {
					color: #1296db;
				}
				
				.gender-icon {
					font-size: 36rpx;
					margin-right: 10rpx;
					font-family: texticons;
				}
			}
		}
	}
	
	.agreement {
		display: flex;
		align-items: center;
		margin: 30rpx 0;
		font-size: 26rpx;
		color: #666666;
		
		label {
			display: flex;
			align-items: center;
			flex-wrap: wrap;
		}
		
		.link {
			color: #1296db;
			margin: 0 5rpx;
		}
	}
	
	.register-btn {
		background-color: #1296db;
		color: #ffffff;
		border-radius: 50rpx;
		font-size: 32rpx;
		margin-top: 40rpx;
		height: 90rpx;
		line-height: 90rpx;
		
		&[disabled] {
			background-color: #cccccc;
			color: #ffffff;
		}
		
		&::after {
			border: none;
		}
	}
	
	.login-link {
		text-align: center;
		margin-top: 40rpx;
		font-size: 28rpx;
		color: #666666;
		
		.link {
			color: #1296db;
			margin-left: 10rpx;
		}
	}
}
</style> 