<template>
	<view class="content">
		<!-- 动态背景装饰 -->
		<view class="floating-shapes">
			<view class="shape shape-1"></view>
			<view class="shape shape-2"></view>
			<view class="shape shape-3"></view>
			<view class="shape shape-4"></view>
		</view>
		
		<view class="main-content">
		<!-- 用户信息 -->
		<view class="user-info-section">
			<view class="user-info-box">
				<view class="avatar-box">
					<image class="avatar" :src="userInfo.avatar || '/static/img/logo.png'" mode="aspectFill"></image>
						<view class="avatar-border"></view>
				</view>
				<view class="info-box">
					<view class="user-name">{{hasLogin ? userName : '未登录'}}</view>
					<view class="user-id" v-if="hasLogin">ID: {{userInfo.id}}</view>
					<button class="login-btn" v-if="!hasLogin" @click="bindLogin">立即登录</button>
				</view>
			</view>
			<view class="user-data-box">
				<view class="data-item" @click="navigateTo('/pages/my-appointment/my-appointment')">
					<text class="data-value">{{userInfo.appointmentCount || 0}}</text>
					<text class="data-label">体检预约</text>
				</view>
				<view class="data-item" @click="navigateTo('/pages/report/report')">
					<text class="data-value">{{userInfo.reportCount || 0}}</text>
					<text class="data-label">体检报告</text>
				</view>
				<view class="data-item" @click="navigateTo('/pages/health-record/health-record')">
					<text class="data-value">{{userInfo.favoriteCount || 0}}</text>
					<text class="data-label">收藏医生</text>
				</view>
			</view>
		</view>
		
		<!-- 我的服务 -->
			<view class="card service-card">
			<view class="card-header">
					<view class="card-title-wrapper">
						<text class="card-icon">🛠️</text>
				<text class="card-title">我的服务</text>
					</view>
			</view>
			<view class="service-grid">
				<view 
					class="service-item" 
					v-for="(item, index) in serviceList" 
					:key="index" 
					:class="{ 'coming-soon': !item.implemented }"
					@click="navigateTo(item.url)"
						:style="{ animationDelay: (index * 0.1) + 's' }"
				>
						<view class="service-icon-wrapper">
					<image :src="item.icon" class="service-icon" mode="aspectFit"></image>
						</view>
					<text class="service-name">{{item.name}}</text>
					<text class="coming-soon-tag" v-if="!item.implemented">即将上线</text>
				</view>
			</view>
		</view>
		
		<!-- 健康管理 -->
			<view class="card health-card">
			<view class="card-header">
					<view class="card-title-wrapper">
						<text class="card-icon">💊</text>
				<text class="card-title">健康管理</text>
					</view>
			</view>
			<view class="health-list">
				<view 
					class="health-item" 
					v-for="(item, index) in healthMenuList" 
					:key="index" 
					:class="{ 'coming-soon': !item.implemented }"
					@click="navigateTo(item.url)"
						:style="{ animationDelay: (index * 0.1) + 's' }"
				>
					<view class="health-item-left">
							<view class="health-icon-wrapper">
						<image :src="item.icon" class="health-icon" mode="aspectFit"></image>
							</view>
						<text class="health-name">{{item.name}}</text>
						<text class="coming-soon-text" v-if="!item.implemented">即将上线</text>
					</view>
					<text class="iconfont icon-right"></text>
				</view>
			</view>
		</view>
		
		<!-- 其他设置 -->
			<view class="card setting-card">
				<view class="card-header">
					<view class="card-title-wrapper">
						<text class="card-icon">⚙️</text>
						<text class="card-title">其他设置</text>
					</view>
				</view>
			<view class="setting-list">
				<view class="setting-item" @click="navigateTo('/pages/settings/settings')">
					<view class="setting-item-left">
							<view class="setting-icon-wrapper">
						<image src="/static/images/icons/icon-settings.png" class="setting-icon" mode="aspectFit"></image>
							</view>
						<text class="setting-name">设置</text>
					</view>
					<text class="iconfont icon-right"></text>
				</view>
				<view class="setting-item" @click="navigateTo('/pages/feedback/feedback')">
					<view class="setting-item-left">
							<view class="setting-icon-wrapper">
						<image src="/static/images/icons/icon-feedback.png" class="setting-icon" mode="aspectFit"></image>
							</view>
						<text class="setting-name">意见反馈</text>
					</view>
					<text class="iconfont icon-right"></text>
				</view>
				<view class="setting-item" @click="navigateTo('/pages/about/about')">
					<view class="setting-item-left">
							<view class="setting-icon-wrapper">
						<image src="/static/images/icons/icon-about.png" class="setting-icon" mode="aspectFit"></image>
							</view>
						<text class="setting-name">关于我们</text>
					</view>
					<text class="iconfont icon-right"></text>
				</view>
			</view>
		</view>
		
		<!-- 退出登录 -->
			<view class="logout-section">
				<button class="logout-btn" @click="bindLogout">
					<text class="logout-icon">🚪</text>
					<text class="logout-text">退出登录</text>
				</button>
			</view>
		</view>
	</view>
</template>

<script>
	import { mapState, mapMutations } from 'vuex'
	
	export default {
		data() {
			return {
				userInfo: {
					avatar: '/static/img/logo.png',
					id: '',
					appointmentCount: 0,
					reportCount: 0,
					favoriteCount: 0
				},
				serviceList: [
					{
						name: '我的预约',
						icon: '/static/images/icons/icon-appointment.png',
						url: '/pages/my-appointment/my-appointment',
						implemented: true
					},
					{
						name: '体检报告',
						icon: '/static/images/icons/icon-report.png',
						url: '/pages/report/report',
						implemented: true
					},
					{
						name: '健康档案',
						icon: '/static/images/icons/icon-record.png',
						url: '/pages/health-record/health-record',
						implemented: true
					},
					{
						name: '家庭成员',
						icon: '/static/images/icons/icon-family.png',
						url: '/pages/family/family',
						implemented: true
					},
					{
						name: '在线咨询',
						icon: '/static/images/icons/icon-consult.png',
						url: '/pages/consult/consult',
						implemented: true
					},
					{
						name: '我的收藏',
						icon: '/static/images/icons/icon-favorite.png',
						url: '/pages/favorite/favorite',
						implemented: true
					}
				],
				healthMenuList: [
					{
						name: '健康评估',
						icon: '/static/images/icons/icon-assessment.png',
						url: '/pages/health-assessment/health-assessment',
						implemented: true
					},
					{
						name: '健康指标',
						icon: '/static/images/icons/icon-indicator.png',
						url: '/pages/health-indicator/health-indicator',
						implemented: true
					},
					{
						name: '健康档案',
						icon: '/static/images/icons/icon-record.png',
						url: '/pages/health-record/health-record',
						implemented: true
					}
				]
			}
		},
		computed: {
			...mapState(['hasLogin', 'forcedLogin', 'userName'])
		},
		onShow() {
			// 如果已登录，获取用户信息
			if (this.hasLogin) {
				this.getUserInfo();
			}
		},
		methods: {
			...mapMutations(['logout']),
			// 获取用户信息
			getUserInfo() {
				// 获取token
				const token = uni.getStorageSync('uniIdToken');
				
				if (!token) {
					console.log('未找到token，无法获取用户信息');
					return;
				}
				
				// 使用API配置中的地址
				const API_BASE_URL = process.env.NODE_ENV === 'development' 
					? 'http://localhost:8888/api' 
					: 'http://39.104.57.236:8888/api';
				
				uni.request({
					url: `${API_BASE_URL}/app/getLoginUserInfo`,
					method: 'POST',
					header: {
						'Authorization': token,
						'Content-Type': 'application/json'
					},
					success: (res) => {
						console.log('获取用户信息响应：', res);
						if (res.statusCode === 200 && res.data.code === 200) {
							const userData = res.data.data;
							this.userInfo.id = userData.userId;
							this.userInfo.avatar = userData.head || '/static/img/logo.png';
							// 可以设置其他用户信息
							console.log('用户信息更新成功：', this.userInfo);
						} else {
							console.error('获取用户信息失败：', res.data);
							// 如果服务器返回错误，显示提示
							uni.showToast({
								title: '获取用户信息失败',
								icon: 'none'
							});
						}
					},
					fail: (err) => {
						console.error('获取用户信息网络错误：', err);
						// 网络错误时显示提示
						uni.showToast({
							title: '网络连接失败，请检查服务器状态',
							icon: 'none',
							duration: 3000
						});
					}
				});
			},
			// 页面跳转
			navigateTo(url) {
				console.log('准备跳转到:', url);
				
				// 如果未登录且不是登录页，先跳转到登录页
				if (!this.hasLogin && url !== '/pages/login/login') {
					uni.navigateTo({
						url: '/pages/login/login'
					});
					return;
				}
				
				// 检查是否是tabBar页面
				const tabBarPages = [
					'/pages/index/index',
					'/pages/appointment/appointment',
					'/pages/report/report',
					'/pages/user/user'
				];
				
				if (tabBarPages.includes(url)) {
					// 使用switchTab跳转到tabBar页面
					uni.switchTab({
						url: url,
						success: () => {
							console.log('tabBar页面跳转成功:', url);
						},
						fail: (err) => {
							console.error('tabBar页面跳转失败:', err);
							uni.showToast({
								title: '页面跳转失败',
								icon: 'none'
							});
						}
					});
				} else {
					// 使用navigateTo跳转到普通页面
					uni.navigateTo({
						url: url,
						success: () => {
							console.log('页面跳转成功:', url);
						},
						fail: (err) => {
							console.error('页面跳转失败:', err);
							console.error('失败URL:', url);
							
							// 检查是否是页面不存在
							if (err.errMsg && err.errMsg.includes('not found')) {
								uni.showModal({
									title: '功能开发中',
									content: '该功能正在开发中，敬请期待！',
									showCancel: false,
									confirmText: '知道了'
								});
							} else {
								uni.showToast({
									title: '页面跳转失败',
									icon: 'none'
								});
							}
						}
					});
				}
			},
			// 登录
			bindLogin() {
				uni.navigateTo({
					url: '../login/login',
				});
			},
			// 退出登录
			bindLogout() {
				uni.showModal({
					title: '提示',
					content: '确定要退出登录吗？',
					success: (res) => {
						if (res.confirm) {
							this.logout();
							uni.removeStorageSync('uniIdToken');
							uni.removeStorageSync('username');
							this.userInfo = {
								avatar: '',
								id: '',
								appointmentCount: 0,
								reportCount: 0,
								favoriteCount: 0
							};
							
							if (this.forcedLogin) {
								uni.reLaunch({
									url: '../login/login',
								});
							}
						}
					}
				});
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
	background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
	min-height: 100vh;
	padding-bottom: 40rpx;
	flex-direction: column;
	position: relative;
}

.floating-shapes {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	overflow: hidden;
	z-index: -1;
	
	.shape {
		position: absolute;
		background: linear-gradient(45deg, rgba(9, 132, 227, 0.1), rgba(116, 185, 255, 0.05));
		border-radius: 50%;
		animation: float 10s infinite ease-in-out;
	}
	
	.shape-1 {
		width: 100px;
		height: 100px;
		left: 10%;
		top: 20%;
		animation-delay: -2s;
	}
	
	.shape-2 {
		width: 150px;
		height: 150px;
		right: 10%;
		bottom: 30%;
		animation-delay: -4s;
	}
	
	.shape-3 {
		width: 80px;
		height: 80px;
		left: 70%;
		top: 70%;
		animation-delay: -6s;
	}
	
	.shape-4 {
		width: 120px;
		height: 120px;
		right: 20%;
		bottom: 10%;
		animation-delay: -8s;
	}
}

@keyframes float {
	0%, 100% {
		transform: translateY(0px) rotate(0deg);
	}
	50% {
		transform: translateY(-20px) rotate(180deg);
	}
}

.main-content {
	padding: 0 30rpx;
	position: relative;
	z-index: 1;
}

.user-info-section {
	background: linear-gradient(135deg, #0984e3 0%, #74b9ff 100%);
	border-radius: 24rpx;
	padding: 40rpx 30rpx;
	margin: 30rpx 0 30rpx 0;
	color: #ffffff;
	box-shadow: 0 8rpx 32rpx rgba(9, 132, 227, 0.3);
	position: relative;
	overflow: hidden;
	
	&::before {
		content: '';
		position: absolute;
		top: -50%;
		right: -50%;
		width: 200%;
		height: 200%;
		background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 0%, transparent 70%);
		animation: rotate 20s linear infinite;
	}
	
	.user-info-box {
		display: flex;
		align-items: center;
		margin-bottom: 30rpx;
		position: relative;
		z-index: 2;
		
		.avatar-box {
			width: 120rpx;
			height: 120rpx;
			border-radius: 60rpx;
			border: 4rpx solid rgba(255, 255, 255, 0.3);
			overflow: hidden;
			margin-right: 20rpx;
			position: relative;
			
			.avatar {
				width: 100%;
				height: 100%;
			}
			
			.avatar-border {
				position: absolute;
				top: -5rpx;
				left: -5rpx;
				width: 130rpx;
				height: 130rpx;
				border: 4rpx solid rgba(255, 255, 255, 0.5);
				border-radius: 65rpx;
				box-sizing: border-box;
				animation: pulse 2s infinite;
			}
		}
		
		.info-box {
			flex: 1;
			
			.user-name {
				font-size: 36rpx;
				font-weight: bold;
				margin-bottom: 10rpx;
				text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.1);
			}
			
			.user-id {
				font-size: 24rpx;
				opacity: 0.8;
			}
			
			.login-btn {
				margin-top: 10rpx;
				background: linear-gradient(135deg, rgba(255, 255, 255, 0.2), rgba(255, 255, 255, 0.1));
				color: #ffffff;
				font-size: 24rpx;
				padding: 0 30rpx;
				height: 60rpx;
				line-height: 60rpx;
				border-radius: 30rpx;
				width: 200rpx;
				border: 2rpx solid rgba(255, 255, 255, 0.3);
				transition: all 0.3s ease;
				
				&::after {
					border: none;
				}
				
				&:active {
					transform: scale(0.95);
					background: linear-gradient(135deg, rgba(255, 255, 255, 0.3), rgba(255, 255, 255, 0.2));
				}
			}
		}
	}
	
	.user-data-box {
		display: flex;
		background: rgba(255, 255, 255, 0.15);
		backdrop-filter: blur(10rpx);
		border-radius: 16rpx;
		padding: 20rpx 0;
		position: relative;
		z-index: 2;
		
		.data-item {
			flex: 1;
			display: flex;
			flex-direction: column;
			align-items: center;
			position: relative;
			transition: all 0.3s ease;
			
			&:active {
				transform: scale(0.95);
			}
			
			&:not(:last-child)::after {
				content: '';
				position: absolute;
				right: 0;
				top: 20%;
				height: 60%;
				width: 1px;
				background: linear-gradient(to bottom, transparent, rgba(255, 255, 255, 0.3), transparent);
			}
			
			.data-value {
				font-size: 36rpx;
				font-weight: bold;
				margin-bottom: 10rpx;
				text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.1);
			}
			
			.data-label {
				font-size: 24rpx;
				opacity: 0.8;
			}
		}
	}
}

@keyframes rotate {
	from {
		transform: rotate(0deg);
	}
	to {
		transform: rotate(360deg);
	}
}

@keyframes pulse {
	0%, 100% {
		transform: scale(1);
		opacity: 0.5;
	}
	50% {
		transform: scale(1.05);
		opacity: 0.8;
	}
}

.card {
	background: #ffffff;
	border-radius: 20rpx;
	padding: 30rpx;
	margin-bottom: 20rpx;
	box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.08);
	transition: all 0.3s ease;
	position: relative;
	overflow: hidden;
	
	&::before {
		content: '';
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		height: 4rpx;
		background: linear-gradient(90deg, #0984e3, #74b9ff);
	}
	
	&:hover {
		transform: translateY(-4rpx);
		box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.12);
	}
	
	.card-header {
		margin-bottom: 30rpx;
		
		.card-title-wrapper {
			display: flex;
			align-items: center;
			position: relative;
			
			.card-icon {
				font-size: 40rpx;
				margin-right: 15rpx;
				color: #0984e3;
			}
		
		.card-title {
			font-size: 32rpx;
			font-weight: bold;
			color: #333333;
			position: relative;
			padding-left: 20rpx;
			
			&::before {
				content: '';
				position: absolute;
				left: 0;
				top: 50%;
				transform: translateY(-50%);
				width: 6rpx;
				height: 30rpx;
					background: linear-gradient(135deg, #0984e3, #74b9ff);
				border-radius: 3rpx;
				}
			}
		}
	}
}

.service-grid {
	display: flex;
	flex-wrap: wrap;
	
	.service-item {
		width: 33.33%;
		display: flex;
		flex-direction: column;
		align-items: center;
		margin-bottom: 30rpx;
		position: relative;
		animation: fadeInUp 0.6s ease-out forwards;
		opacity: 0;
		transform: translateY(20rpx);
		
		&.coming-soon {
			opacity: 0.6;
		}
		
		.service-icon-wrapper {
			width: 80rpx;
			height: 80rpx;
			border-radius: 40rpx;
			background: transparent;
			border: 3rpx solid #0984e3;
			display: flex;
			justify-content: center;
			align-items: center;
			margin-bottom: 15rpx;
			box-shadow: 0 4rpx 12rpx rgba(9, 132, 227, 0.2);
			transition: all 0.3s ease;
			
			&:active {
				transform: scale(0.9);
				background: rgba(9, 132, 227, 0.1);
				box-shadow: 0 2rpx 8rpx rgba(9, 132, 227, 0.3);
			}
		}
		
		.service-icon {
			width: 40rpx;
			height: 40rpx;
			filter: brightness(0) saturate(100%) invert(27%) sepia(51%) saturate(2878%) hue-rotate(199deg) brightness(104%) contrast(97%);
		}
		
		.service-name {
			font-size: 26rpx;
			color: #333333;
			font-weight: 500;
		}
		
		.coming-soon-tag {
			position: absolute;
			top: -5rpx;
			right: 10rpx;
			background: linear-gradient(135deg, #ff9500, #ffa726);
			color: #ffffff;
			font-size: 20rpx;
			padding: 4rpx 8rpx;
			border-radius: 10rpx;
			transform: scale(0.8);
			box-shadow: 0 2rpx 8rpx rgba(255, 149, 0, 0.3);
		}
	}
}

.health-list, .setting-list {
	.health-item, .setting-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 25rpx 0;
		border-bottom: 1px solid #f5f5f5;
		animation: fadeInUp 0.6s ease-out forwards;
		opacity: 0;
		transform: translateY(20rpx);
		transition: all 0.3s ease;
		
		&:last-child {
			border-bottom: none;
		}
		
		&:active {
			background: rgba(9, 132, 227, 0.05);
			border-radius: 12rpx;
			padding-left: 20rpx;
			padding-right: 20rpx;
			
			.health-icon-wrapper, .setting-icon-wrapper {
				background: rgba(9, 132, 227, 0.1);
				box-shadow: 0 2rpx 8rpx rgba(9, 132, 227, 0.25);
			}
		}
		
		&.coming-soon {
			opacity: 0.6;
		}
		
		.health-item-left, .setting-item-left {
			display: flex;
			align-items: center;
			position: relative;
			
			.health-icon-wrapper, .setting-icon-wrapper {
				width: 40rpx;
				height: 40rpx;
				border-radius: 20rpx;
				background: transparent;
				border: 2rpx solid #0984e3;
				display: flex;
				justify-content: center;
				align-items: center;
				margin-right: 20rpx;
				box-shadow: 0 2rpx 8rpx rgba(9, 132, 227, 0.15);
				transition: all 0.3s ease;
			}
			
			.health-icon, .setting-icon {
				width: 24rpx;
				height: 24rpx;
				filter: brightness(0) saturate(100%) invert(27%) sepia(51%) saturate(2878%) hue-rotate(199deg) brightness(104%) contrast(97%);
			}
			
			.health-name, .setting-name {
				font-size: 28rpx;
				color: #333333;
				font-weight: 500;
			}
			
			.coming-soon-text {
				position: absolute;
				right: -80rpx;
				top: 50%;
				transform: translateY(-50%);
				background: linear-gradient(135deg, #ff9500, #ffa726);
				color: #ffffff;
				font-size: 20rpx;
				padding: 4rpx 8rpx;
				border-radius: 10rpx;
				transform: scale(0.8);
				box-shadow: 0 2rpx 8rpx rgba(255, 149, 0, 0.3);
			}
		}
		
		.iconfont {
			font-size: 24rpx;
			color: #999999;
			transition: all 0.3s ease;
		}
		
		&:active .iconfont {
			color: #0984e3;
			transform: translateX(4rpx);
		}
	}
}

@keyframes fadeInUp {
	to {
		opacity: 1;
		transform: translateY(0);
	}
}

.logout-section {
	margin-top: 50rpx;
	text-align: center;
}

.logout-btn {
	width: 90%;
	height: 90rpx;
	border-radius: 45rpx;
	box-shadow: 0 4rpx 16rpx rgba(255, 90, 95, 0.3);
	display: flex;
	align-items: center;
	justify-content: center;
	background: linear-gradient(135deg, #ff5a5f, #ff7675);
	color: #ffffff;
	font-size: 30rpx;
	font-weight: 500;
	border: none;
	transition: all 0.3s ease;
	
	&::after {
		border: none;
	}
	
	&:active {
		transform: scale(0.95);
		box-shadow: 0 2rpx 8rpx rgba(255, 90, 95, 0.4);
	}
	
	.logout-icon {
		font-size: 36rpx;
		margin-right: 10rpx;
	}
	
	.logout-text {
		font-weight: 500;
	}
}
</style>

