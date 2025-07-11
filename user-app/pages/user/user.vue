<template>
	<view class="content">
		<!-- 用户信息 -->
		<view class="user-info-section">
			<view class="user-info-box">
				<view class="avatar-box">
					<image class="avatar" :src="userInfo.avatar || '/static/img/logo.png'" mode="aspectFill"></image>
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
		<view class="card">
			<view class="card-header">
				<text class="card-title">我的服务</text>
			</view>
			<view class="service-grid">
				<view class="service-item" v-for="(item, index) in serviceList" :key="index" @click="navigateTo(item.url)">
					<image :src="item.icon" class="service-icon" mode="aspectFit"></image>
					<text class="service-name">{{item.name}}</text>
				</view>
			</view>
		</view>
		
		<!-- 健康管理 -->
		<view class="card">
			<view class="card-header">
				<text class="card-title">健康管理</text>
			</view>
			<view class="health-list">
				<view class="health-item" v-for="(item, index) in healthMenuList" :key="index" @click="navigateTo(item.url)">
					<view class="health-item-left">
						<image :src="item.icon" class="health-icon" mode="aspectFit"></image>
						<text class="health-name">{{item.name}}</text>
					</view>
					<text class="iconfont icon-right"></text>
				</view>
			</view>
		</view>
		
		<!-- 其他设置 -->
		<view class="card">
			<view class="setting-list">
				<view class="setting-item" @click="navigateTo('/pages/settings/settings')">
					<view class="setting-item-left">
						<image src="/static/images/icons/icon-settings.png" class="setting-icon" mode="aspectFit"></image>
						<text class="setting-name">设置</text>
					</view>
					<text class="iconfont icon-right"></text>
				</view>
				<view class="setting-item" @click="navigateTo('/pages/feedback/feedback')">
					<view class="setting-item-left">
						<image src="/static/images/icons/icon-feedback.png" class="setting-icon" mode="aspectFit"></image>
						<text class="setting-name">意见反馈</text>
					</view>
					<text class="iconfont icon-right"></text>
				</view>
				<view class="setting-item" @click="navigateTo('/pages/about/about')">
					<view class="setting-item-left">
						<image src="/static/images/icons/icon-about.png" class="setting-icon" mode="aspectFit"></image>
						<text class="setting-name">关于我们</text>
					</view>
					<text class="iconfont icon-right"></text>
				</view>
			</view>
		</view>
		
		<!-- 退出登录 -->
		<view class="logout-btn" @click="bindLogout">退出登录</view>
	</view>
</template>

<script>
	import { mapState, mapMutations } from 'vuex'
	
	export default {
		data() {
			return {
				userInfo: {
					avatar: '/static/img/logo.png',
					id: '1001',
					appointmentCount: 5,
					reportCount: 3,
					favoriteCount: 2
				},
				serviceList: [
					{
						name: '我的预约',
						icon: '/static/images/icons/icon-appointment.png',
						url: '/pages/my-appointment/my-appointment'
					},
					{
						name: '体检报告',
						icon: '/static/images/icons/icon-report.png',
						url: '/pages/report/report'
					},
					{
						name: '健康档案',
						icon: '/static/images/icons/icon-record.png',
						url: '/pages/health-record/health-record'
					},
					{
						name: '家庭成员',
						icon: '/static/images/icons/icon-family.png',
						url: '/pages/family/family'
					},
					{
						name: '在线咨询',
						icon: '/static/images/icons/icon-consult.png',
						url: '/pages/consult/consult'
					},
					{
						name: '我的收藏',
						icon: '/static/images/icons/icon-favorite.png',
						url: '/pages/favorite/favorite'
					}
				],
				healthMenuList: [
					{
						name: '健康评估',
						icon: '/static/images/icons/icon-assessment.png',
						url: '/pages/health-assessment/health-assessment'
					},
					{
						name: '健康指标',
						icon: '/static/images/icons/icon-indicator.png',
						url: '/pages/health-indicator/health-indicator'
					},
					{
						name: '健康档案',
						icon: '/static/images/icons/icon-record.png',
						url: '/pages/health-record/health-record'
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
				// 这里可以替换为实际的API调用
				console.log('获取用户信息');
			},
			// 页面跳转
			navigateTo(url) {
				// 如果未登录且不是登录页，先跳转到登录页
				if (!this.hasLogin && url !== '/pages/login/login') {
					uni.navigateTo({
						url: '/pages/login/login'
					});
					return;
				}
				console.log(url);
				uni.navigateTo({
					url: url
				});
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
	background-color: #f5f5f5;
	min-height: 100vh;
	padding-bottom: 40rpx;
	flex-direction: column;
}

.user-info-section {
	background-color: #1296db;
	border-radius: 20rpx;
	padding: 40rpx 30rpx;
	margin-bottom: 20rpx;
	color: #ffffff;
	box-shadow: 0 4rpx 20rpx rgba(18, 150, 219, 0.3);
	
	.user-info-box {
		display: flex;
		align-items: center;
		margin-bottom: 30rpx;
		
		.avatar-box {
			width: 120rpx;
			height: 120rpx;
			border-radius: 60rpx;
			border: 4rpx solid rgba(255, 255, 255, 0.3);
			overflow: hidden;
			margin-right: 20rpx;
			
			.avatar {
				width: 100%;
				height: 100%;
			}
		}
		
		.info-box {
			flex: 1;
			
			.user-name {
				font-size: 36rpx;
				font-weight: bold;
				margin-bottom: 10rpx;
			}
			
			.user-id {
				font-size: 24rpx;
				opacity: 0.8;
			}
			
			.login-btn {
				margin-top: 10rpx;
				background-color: rgba(255, 255, 255, 0.2);
				color: #ffffff;
				font-size: 24rpx;
				padding: 0 30rpx;
				height: 60rpx;
				line-height: 60rpx;
				border-radius: 30rpx;
				width: 200rpx;
				
				&::after {
					border: none;
				}
			}
		}
	}
	
	.user-data-box {
		display: flex;
		background-color: rgba(255, 255, 255, 0.1);
		border-radius: 10rpx;
		padding: 20rpx 0;
		
		.data-item {
			flex: 1;
			display: flex;
			flex-direction: column;
			align-items: center;
			position: relative;
			
			&:not(:last-child)::after {
				content: '';
				position: absolute;
				right: 0;
				top: 20%;
				height: 60%;
				width: 1px;
				background-color: rgba(255, 255, 255, 0.2);
			}
			
			.data-value {
				font-size: 36rpx;
				font-weight: bold;
				margin-bottom: 10rpx;
			}
			
			.data-label {
				font-size: 24rpx;
				opacity: 0.8;
			}
		}
	}
}

.card {
	background-color: #ffffff;
	border-radius: 20rpx;
	padding: 30rpx;
	margin-bottom: 20rpx;
	box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
	
	.card-header {
		margin-bottom: 30rpx;
		
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
				background-color: #1296db;
				border-radius: 3rpx;
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
		
		.service-icon {
			width: 80rpx;
			height: 80rpx;
			margin-bottom: 15rpx;
		}
		
		.service-name {
			font-size: 26rpx;
			color: #333333;
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
		
		&:last-child {
			border-bottom: none;
		}
		
		.health-item-left, .setting-item-left {
			display: flex;
			align-items: center;
			
			.health-icon, .setting-icon {
				width: 40rpx;
				height: 40rpx;
				margin-right: 20rpx;
			}
			
			.health-name, .setting-name {
				font-size: 28rpx;
				color: #333333;
			}
		}
		
		.iconfont {
			font-size: 24rpx;
			color: #999999;
		}
	}
}

.logout-btn {
	margin: 50rpx auto;
	width: 90%;
	height: 90rpx;
	line-height: 90rpx;
	text-align: center;
	background-color: #ffffff;
	color: #ff5a5f;
	font-size: 30rpx;
	border-radius: 45rpx;
	box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
}
</style>

