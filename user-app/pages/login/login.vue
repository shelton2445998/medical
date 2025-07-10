<template>
	<view class="content">
		<!-- 页面标题 -->
		<view class="page-header">
			<!-- <view class="back-btn" @click="goBack">
				<text class="iconfont icon-back">&#xe65e;</text>
			</view> -->
			<text class="page-title">欢迎登录熙康健康</text>
		</view>
		
		<!-- 登录表单 -->
		<view class="login-form">
			<!-- 手机号 -->
			<view class="form-item">
				<image src="/static/icon/phone.png" mode="aspectFit" class="btn-icon"></image>
				<input type="text" v-model="account" placeholder="请输入手机号码" maxlength="11" />
			</view>
			
			<!-- 密码 -->
			<view class="form-item">
				<image src="/static/icon/password.png" mode="aspectFit" class="btn-icon"></image>
				<input type="password" v-model="password" placeholder="请输入密码" password />
				<text class="forget-pwd" @click="navigateTo('/pages/pwd/pwd')">忘记密码?</text>
			</view>
			
			<!-- 登录按钮 -->
			<button class="login-btn" @click="bindLogin">登 录</button>
			
			<!-- 其他登录方式 -->
			<view class="other-login">
				<view class="divider">
					<view class="line"></view>
					<text>其他登录方式</text>
					<view class="line"></view>
				</view>
				<view class="oauth-box">
					<view class="oauth-item" @click="oauthLogin('weixin')">
						<image src="/static/icon/WeChat.png" mode="aspectFit" class="btn-icon"></image>
					</view>
					<view class="oauth-item" @click="oauthLogin('qq')">
						<image src="/static/icon/qq.png" mode="aspectFit" class="btn-icon"></image>
					</view>
					<view class="oauth-item" @click="oauthLogin('weibo')">
						<image src="/static/icon/weibo.png" mode="aspectFit" class="btn-icon"></image>
					</view>
				</view>
			</view>
			
			<!-- 注册链接 -->
			<view class="register-link">
				<text>还没有账号？</text>
				<text class="link" @click="navigateTo('/pages/register/register')">立即注册</text>
			</view>
		</view>
		
		<!-- 帮助信息 -->
		<view class="help-info">
			<text class="help-text">登录遇到问题？请联系客服</text>
			<text class="help-phone">400-XXX-XXXX</text>
		</view>
	</view>
</template>

<script>
	import service from '../../service.js'
	import { mapMutations } from 'vuex'
	
	export default {
		data() {
			return {
				account: '',
				password: '',
				positionTop: 0
			}
		},
		methods: {
			...mapMutations(['login']),
			// 登录
			bindLogin() {
				if (this.account.length < 5) {
					uni.showToast({
						icon: 'none',
						title: '账号最短为 5 个字符'
					});
					return;
				}
				if (this.password.length < 6) {
					uni.showToast({
						icon: 'none',
						title: '密码最短为 6 个字符'
					});
					return;
				}
				
				const users = service.getUsers();
				const userInfo = users.find(user => user.account === this.account && user.password === this.password);
				
				if (userInfo) {
					this.login(this.account);
					uni.setStorageSync('uniIdToken', 'mock-token');
					uni.setStorageSync('username', this.account);
					
					uni.showToast({
						title: '登录成功'
					});
					
					uni.switchTab({
						url: '/pages/index/index'
					});
				} else {
					uni.showToast({
						icon: 'none',
						title: '用户名或密码错误'
					});
				}
			},
			// 第三方登录
			oauthLogin(provider) {
				uni.showToast({
					title: `${provider}登录功能开发中`,
					icon: 'none'
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

.btn-icon {
  width: 60rpx;
  height: 60rpx;
  // margin-right: 10rpx;
  vertical-align: middle;
  object-fit: cover;
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

.login-form {
	padding: 50rpx 40rpx;
	
	.form-item {
		position: relative;
		display: flex;
		align-items: center;
		border-bottom: 1px solid #eeeeee;
		padding: 20rpx 0;
		margin-bottom: 30rpx;
		
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
		
		.forget-pwd {
			font-size: 26rpx;
			color: #1296db;
		}
	}
	
	.login-btn {
		background-color: #1296db;
		color: #ffffff;
		border-radius: 50rpx;
		font-size: 32rpx;
		margin-top: 60rpx;
		height: 90rpx;
		line-height: 90rpx;
		
		&::after {
			border: none;
		}
	}
	
	.other-login {
		margin-top: 80rpx;
		
		.divider {
			display: flex;
			align-items: center;
			justify-content: center;
			margin-bottom: 40rpx;
			
			.line {
				height: 1px;
				width: 100rpx;
				background-color: #dddddd;
			}
			
			text {
				color: #999999;
				font-size: 26rpx;
				margin: 0 20rpx;
			}
		}
		
		.oauth-box {
			display: flex;
			justify-content: center;
			
			.oauth-item {
				width: 80rpx;
				height: 80rpx;
				border-radius: 50%;
				background-color: #f5f5f5;
				display: flex;
				justify-content: center;
				align-items: center;
				margin: 0 30rpx;
				
				.iconfont {
					font-size: 50rpx;
					color: #666666;
					font-family: texticons;
				}
				
				.icon-weixin {
					color: #09bb07;
				}
				
				.icon-qq {
					color: #1296db;
				}
				
				.icon-weibo {
					color: #e6162d;
				}
			}
		}
	}
	
	.register-link {
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

.help-info {
	margin-top: auto;
	text-align: center;
	padding: 30rpx;
	
	.help-text {
		font-size: 26rpx;
		color: #999999;
		display: block;
		margin-bottom: 10rpx;
	}
	
	.help-phone {
		font-size: 28rpx;
		color: #1296db;
		font-weight: bold;
	}
}
</style>
