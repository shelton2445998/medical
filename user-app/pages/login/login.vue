<template>
	<view class="content">
		<!-- 页面标题 -->
		<view class="page-header">
			<!-- 返回按钮（已注释） -->
			<!-- <view class="back-btn" @click="goBack">
				<text class="iconfont icon-back">&#xe65e;</text>
			</view> -->
			<!-- 页面标题文本 -->
			<text class="page-title">欢迎登录东软熙心健康</text>
		</view>
		
		<!-- 登录表单 -->
		<view class="login-form">
			<!-- 用户名输入框 -->
			<view class="form-item">
				<!-- 用户图标 -->
				<image src="/static/icon/user1.png" mode="aspectFit" class="btn-icon"></image>
				<!-- 用户名输入框 -->
				<input type="text" v-model="account" placeholder="请输入用户名" maxlength="11" />
			</view>
			
			<!-- 密码输入框 -->
			<view class="form-item">
				<!-- 密码图标 -->
				<image src="/static/icon/password.png" mode="aspectFit" class="btn-icon"></image>
				<!-- 密码输入框 -->
				<input type="password" v-model="password" placeholder="请输入密码" password />
				<!-- 忘记密码链接 -->
				<text class="forget-pwd" @click="navigateTo('/pages/pwd/pwd')">忘记密码?</text>
			</view>
			
			<!-- 登录按钮 -->
			<button class="login-btn" @click="bindLogin">登 录</button>
			
			<!-- 其他登录方式 -->
			<view class="other-login">
				<!-- 分隔线 -->
				<view class="divider">
					<view class="line"></view>
					<text>其他登录方式</text>
					<view class="line"></view>
				</view>
				<!-- 第三方登录选项 -->
				<view class="oauth-box">
					<!-- 微信登录 -->
					<view class="oauth-item" @click="oauthLogin('weixin')">
						<image src="/static/icon/WeChat.png" mode="aspectFit" class="btn-icon"></image>
					</view>
					<!-- QQ登录 -->
					<view class="oauth-item" @click="oauthLogin('qq')">
						<image src="/static/icon/qq.png" mode="aspectFit" class="btn-icon"></image>
					</view>
					<!-- 微博登录 -->
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
			<!-- 帮助提示文本 -->
			<text class="help-text">登录遇到问题？请联系客服</text>
			<!-- 客服电话 -->
			<text class="help-phone">400-XXX-XXXX</text>
		</view>
	</view>
</template>

<script>
	// 导入服务层模块
	import service from '../../service.js'
	// 导入Vuex的mapMutations辅助函数
	import { mapMutations } from 'vuex'
	// 导入MD5加密库
	import md5 from "js-md5"
	
	// 导出登录页面组件配置
	export default {
		// 组件数据
		data() {
			return {
				account: '', // 用户名/账号
				password: '', // 密码
				positionTop: 0 // 页面顶部位置
			}
		},
		// 组件方法
		methods: {
			// 使用mapMutations将vuex中的login方法映射到组件中
			...mapMutations(['login']),
			
			// 登录方法
			async bindLogin() {
				// 验证用户名是否为空
				if (!this.account.trim()) {
					uni.showToast({
						icon: 'none', // 不显示图标
						title: '请输入用户名' // 提示信息
					});
					return; // 终止执行
				}
				// 验证密码长度是否满足要求
				if (this.password.length < 6) {
					uni.showToast({
						icon: 'none', // 不显示图标
						title: '密码最短为 6 个字符' // 提示信息
					});
					return; // 终止执行
				}
				
				try {
					// 显示加载提示
					uni.showLoading({
						title: '登录中...' // 加载提示文字
					});
					
					// 调用后端登录接口，传入用户名和密码
					const res = await service.accountLogin(this.account, this.password);
					
					// 登录成功后，存储用户信息到Vuex和本地存储
					this.login(this.account); // 更新Vuex中的登录状态
					uni.setStorageSync('uniIdToken', res.token); // 存储token到本地
					uni.setStorageSync('username', this.account); // 存储用户名到本地
					
					// 隐藏加载提示
					uni.hideLoading();
					// 显示登录成功提示
					uni.showToast({
						title: '登录成功' // 成功提示文字
					});
					
					// 跳转到首页
					uni.switchTab({
						url: '/pages/index/index' // 首页路径
					});
				} catch (error) {
					// 登录失败处理
					uni.hideLoading(); // 隐藏加载提示
					uni.showToast({
						icon: 'none', // 不显示图标
						title: error.message || '登录失败，请重试' // 错误提示信息
					});
				}
			},
			
			// 第三方登录方法
			oauthLogin(provider) {
				// 显示第三方登录功能开发中的提示
				uni.showToast({
					title: `${provider}登录功能开发中`, // 提示信息
					icon: 'none' // 不显示图标
				});
			},
			
			// 页面跳转方法
			navigateTo(url) {
				// 使用uni.navigateTo进行页面跳转
				uni.navigateTo({
					url: url // 目标页面路径
				});
			},
			
			// 返回上一页方法
			goBack() {
				// 使用uni.navigateBack返回上一页
				uni.navigateBack();
			}
		}
	}
</script>

<style lang="scss">
/* 导入字体图标 */
@font-face {
	font-family: texticons;
	font-weight: normal;
	font-style: normal;
	src: url('https://at.alicdn.com/t/font_984210_5cs13ndgqsn.ttf') format('truetype');
}

/* 按钮图标样式 */
.btn-icon {
  width: 60rpx; /* 图标宽度 */
  height: 60rpx; /* 图标高度 */
  vertical-align: middle; /* 垂直居中对齐 */
  object-fit: cover; /* 图片适应容器 */
}

/* 页面容器样式 */
.content {
	background-color: #ffffff; /* 背景色为白色 */
	min-height: 100vh; /* 最小高度为视口高度 */
	display: flex; /* 使用弹性布局 */
	flex-direction: column; /* 垂直排列 */
}

/* 页面头部样式 */
.page-header {
	position: relative; /* 相对定位 */
	background-color: #1296db; /* 背景色为蓝色 */
	padding: 40rpx 0; /* 上下内边距 */
	text-align: center; /* 文本居中 */
	
	/* 返回按钮样式 */
	.back-btn {
		position: absolute; /* 绝对定位 */
		left: 30rpx; /* 左边距 */
		top: 50%; /* 垂直居中 */
		transform: translateY(-50%); /* 垂直居中调整 */
		width: 60rpx; /* 宽度 */
		height: 60rpx; /* 高度 */
		display: flex; /* 使用弹性布局 */
		justify-content: center; /* 水平居中 */
		align-items: center; /* 垂直居中 */
		
		/* 图标样式 */
		.iconfont {
			color: #ffffff; /* 图标颜色为白色 */
			font-size: 40rpx; /* 图标大小 */
			font-family: texticons; /* 字体图标 */
		}
	}
	
	/* 页面标题样式 */
	.page-title {
		color: #ffffff; /* 文字颜色为白色 */
		font-size: 36rpx; /* 字体大小 */
		font-weight: bold; /* 字体粗细 */
	}
}

/* 登录表单样式 */
.login-form {
	padding: 50rpx 40rpx; /* 内边距 */
	
	/* 表单项样式 */
	.form-item {
		position: relative; /* 相对定位 */
		display: flex; /* 使用弹性布局 */
		align-items: center; /* 垂直居中 */
		border-bottom: 1px solid #eeeeee; /* 底部边框 */
		padding: 20rpx 0; /* 上下内边距 */
		margin-bottom: 30rpx; /* 底部外边距 */
		
		/* 图标样式 */
		.iconfont {
			font-size: 40rpx; /* 图标大小 */
			color: #999999; /* 图标颜色 */
			margin-right: 20rpx; /* 右边距 */
			font-family: texticons; /* 字体图标 */
		}
		
		/* 输入框样式 */
		input {
			flex: 1; /* 占据剩余空间 */
			font-size: 30rpx; /* 字体大小 */
			color: #333333; /* 文字颜色 */
		}
		
		/* 忘记密码链接样式 */
		.forget-pwd {
			font-size: 26rpx; /* 字体大小 */
			color: #1296db; /* 文字颜色为蓝色 */
		}
	}
	
	/* 登录按钮样式 */
	.login-btn {
		background-color: #1296db; /* 背景色为蓝色 */
		color: #ffffff; /* 文字颜色为白色 */
		border-radius: 50rpx; /* 圆角 */
		font-size: 32rpx; /* 字体大小 */
		margin-top: 60rpx; /* 上边距 */
		height: 90rpx; /* 高度 */
		line-height: 90rpx; /* 行高 */
		
		/* 移除默认边框 */
		&::after {
			border: none; /* 无边框 */
		}
	}
	
	/* 其他登录方式样式 */
	.other-login {
		margin-top: 80rpx; /* 上边距 */
		
		/* 分隔线样式 */
		.divider {
			display: flex; /* 使用弹性布局 */
			align-items: center; /* 垂直居中 */
			justify-content: center; /* 水平居中 */
			margin-bottom: 40rpx; /* 底部边距 */
			
			/* 分隔线样式 */
			.line {
				height: 1px; /* 高度 */
				width: 100rpx; /* 宽度 */
				background-color: #dddddd; /* 背景色 */
			}
			
			/* 文本样式 */
			text {
				color: #999999; /* 文字颜色 */
				font-size: 26rpx; /* 字体大小 */
				margin: 0 20rpx; /* 左右边距 */
			}
		}
		
		/* 第三方登录选项容器样式 */
		.oauth-box {
			display: flex; /* 使用弹性布局 */
			justify-content: center; /* 水平居中 */
			
			/* 第三方登录选项样式 */
			.oauth-item {
				width: 80rpx; /* 宽度 */
				height: 80rpx; /* 高度 */
				border-radius: 50%; /* 圆形 */
				background-color: #f5f5f5; /* 背景色 */
				display: flex; /* 使用弹性布局 */
				justify-content: center; /* 水平居中 */
				align-items: center; /* 垂直居中 */
				margin: 0 30rpx; /* 左右边距 */
				
				/* 图标样式 */
				.iconfont {
					font-size: 50rpx; /* 图标大小 */
					color: #666666; /* 图标颜色 */
					font-family: texticons; /* 字体图标 */
				}
				
				/* 微信图标颜色 */
				.icon-weixin {
					color: #09bb07; /* 微信绿色 */
				}
				
				/* QQ图标颜色 */
				.icon-qq {
					color: #1296db; /* QQ蓝色 */
				}
				
				/* 微博图标颜色 */
				.icon-weibo {
					color: #e6162d; /* 微博红色 */
				}
			}
		}
	}
	
	/* 注册链接样式 */
	.register-link {
		text-align: center; /* 文本居中 */
		margin-top: 40rpx; /* 上边距 */
		font-size: 28rpx; /* 字体大小 */
		color: #666666; /* 文字颜色 */
		
		/* 链接样式 */
		.link {
			color: #1296db; /* 链接颜色为蓝色 */
			margin-left: 10rpx; /* 左边距 */
		}
	}
}

/* 帮助信息样式 */
.help-info {
	margin-top: auto; /* 自动上边距，推到底部 */
	text-align: center; /* 文本居中 */
	padding: 30rpx; /* 内边距 */
	
	/* 帮助文本样式 */
	.help-text {
		font-size: 26rpx; /* 字体大小 */
		color: #999999; /* 文字颜色 */
		display: block; /* 块级元素 */
		margin-bottom: 10rpx; /* 底部边距 */
	}
	
	/* 客服电话样式 */
	.help-phone {
		font-size: 28rpx; /* 字体大小 */
		color: #1296db; /* 文字颜色为蓝色 */
		font-weight: bold; /* 字体粗细 */
	}
}
</style>