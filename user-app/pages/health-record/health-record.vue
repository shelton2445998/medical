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
			<!-- 页面标题 -->
			<view class="page-header">
				<view class="header-icon">📋</view>
				<view class="header-title">健康档案</view>
				<view class="header-desc">您的健康数据管理中心</view>
			</view>
			
			<!-- 基本信息卡片 -->
			<view class="info-card">
				<view class="avatar-section">
					<image class="avatar" src="/static/icon/phone.png" mode="aspectFill"></image>
					<view class="user-basic">
						<text class="name">张三</text>
						<text class="meta">男 · 35岁</text>
					</view>
				</view>
				
				<view class="health-summary">
					<view class="summary-item">
						<text class="value">172</text>
						<text class="label">身高(cm)</text>
					</view>
					<view class="summary-item">
						<text class="value">65</text>
						<text class="label">体重(kg)</text>
					</view>
					<view class="summary-item">
						<text class="value">22.5</text>
						<text class="label">BMI</text>
					</view>
					<view class="summary-item">
						<text class="value">120/80</text>
						<text class="label">血压</text>
					</view>
				</view>
			</view>

			<!-- 健康指标 -->
			<view class="section">
				<view class="section-header">
					<text class="title">健康指标</text>
					<text class="more">历史记录 ></text>
				</view>
				<view class="indicators">
					<view class="indicator" v-for="item in indicators" :key="item.name">
						<view class="indicator-header">
							<text class="name">{{item.name}}</text>
							<text class="value" :class="{'abnormal': item.status === 'abnormal'}">{{item.value}}</text>
						</view>
						<view class="indicator-range">
							<text class="range">{{item.range}}</text>
							<text class="status" :class="item.status">{{item.statusText}}</text>
						</view>
					</view>
				</view>
			</view>

			<!-- 健康记录 -->
			<view class="section">
				<view class="section-header">
					<text class="title">健康记录</text>
					<text class="more">全部记录 ></text>
				</view>
				<view class="records">
					<view class="record-item" v-for="item in records" :key="item.id" @click="viewDetail(item)">
						<view class="record-left">
							<text class="record-title">{{item.title}}</text>
							<text class="record-date">{{item.date}}</text>
						</view>
						<view class="record-right">
							<text class="record-status" :class="item.status">{{item.statusText}}</text>
							<image class="arrow" src="/static/icon/arrow-right.png" mode="aspectFit"></image>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 底部操作按钮 -->
		<view class="action-bar">
			<button class="action-btn" @click="addRecord">
				<image src="/static/icon/add.png" mode="aspectFit" class="btn-icon"></image>
				<text>添加记录</text>
			</button>
			<button class="action-btn" @click="shareRecord">
				<image src="/static/icon/share.png" mode="aspectFit" class="btn-icon"></image>
				<text>分享档案</text>
			</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				indicators: [
					{name: '血糖', value: '5.2', range: '3.9-6.1 mmol/L', status: 'normal', statusText: '正常'},
					{name: '胆固醇', value: '5.8', range: '2.8-5.2 mmol/L', status: 'abnormal', statusText: '偏高'},
					{name: '尿酸', value: '380', range: '150-420 μmol/L', status: 'normal', statusText: '正常'},
					{name: '肝功能', value: '28', range: '0-40 U/L', status: 'normal', statusText: '正常'}
				],
				records: [
					{id: 1, title: '年度体检报告', date: '2023-06-20', status: 'completed', statusText: '已完成'},
					{id: 2, title: '血压监测记录', date: '2023-06-18', status: 'ongoing', statusText: '进行中'},
					{id: 3, title: '血糖监测记录', date: '2023-06-15', status: 'completed', statusText: '已完成'}
				]
			}
		},
		methods: {
			goBack() {
				uni.navigateBack();
			},
			viewDetail(item) {
				uni.navigateTo({
					url: `/pages/health/record-detail?id=${item.id}`
				});
			},
			addRecord() {
				uni.navigateTo({
					url: '/pages/health/add-record'
				});
			},
			shareRecord() {
				uni.showToast({
					title: '分享功能开发中',
					icon: 'none'
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
	background: linear-gradient(135deg, #0984e3 0%, #74b9ff 50%, #0984e3 100%);
	min-height: 100vh;
	position: relative;
	overflow: hidden;
	
	&::before {
		content: '';
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: linear-gradient(45deg, transparent 30%, rgba(255, 255, 255, 0.05) 50%, transparent 70%);
		animation: shimmer 8s ease-in-out infinite;
		pointer-events: none;
	}
}

.floating-shapes {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	z-index: -1;
	
	.shape {
		position: absolute;
		background: rgba(255, 255, 255, 0.1);
		border-radius: 50%;
		animation: float 10s infinite ease-in-out;
		transition: all 0.3s ease;
		
		&:hover {
			transform: scale(1.2);
			background: rgba(255, 255, 255, 0.2);
		}
		
		&::before {
			content: '';
			position: absolute;
			top: -10%;
			left: -10%;
			width: 120%;
			height: 120%;
			background: radial-gradient(circle, rgba(255, 255, 255, 0.2) 0%, transparent 70%);
			border-radius: 50%;
			animation: pulse 3s ease-in-out infinite;
		}
	}
	
	.shape-1 {
		width: 200px;
		height: 200px;
		top: 10%;
		left: 10%;
		animation-delay: 0s;
	}
	
	.shape-2 {
		width: 150px;
		height: 150px;
		bottom: 20%;
		right: 20%;
		animation-delay: 1s;
	}
	
	.shape-3 {
		width: 100px;
		height: 100px;
		top: 70%;
		left: 70%;
		animation-delay: 2s;
	}
	
	.shape-4 {
		width: 120px;
		height: 120px;
		bottom: 50%;
		right: 50%;
		animation-delay: 3s;
	}
}

.main-content {
	padding: 20rpx 40rpx 0 40rpx;
}

.page-header {
	text-align: center;
	margin-bottom: 40rpx;
	animation: fadeInDown 0.8s ease-out;
	
	.header-icon {
		font-size: 80rpx;
		margin-bottom: 20rpx;
		animation: bounce 2s infinite;
		transition: all 0.3s ease;
		
		&:hover {
			transform: scale(1.2);
		}
	}
	
	.header-title {
		font-size: 44rpx;
		font-weight: bold;
		color: #ffffff;
		margin-bottom: 16rpx;
		text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.3);
		transition: all 0.3s ease;
	}
	
	.header-desc {
		font-size: 28rpx;
		color: rgba(255, 255, 255, 0.8);
		transition: all 0.3s ease;
	}
}

.info-card {
	background: rgba(255, 255, 255, 0.95);
	border-radius: 20rpx;
	padding: 30rpx;
	margin-bottom: 30rpx;
	box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
	backdrop-filter: blur(10rpx);
	animation: fadeInUp 0.6s ease-out;
	transition: all 0.3s ease;
	
	&:hover {
		transform: translateY(-2rpx);
		box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.15);
	}
	
	.avatar-section {
		display: flex;
		align-items: center;
		margin-bottom: 30rpx;
		
		.avatar {
			width: 120rpx;
			height: 120rpx;
			border-radius: 50%;
			margin-right: 20rpx;
			border: 4rpx solid rgba(9, 132, 227, 0.2);
			transition: all 0.3s ease;
		}
		
		.user-basic {
			display: flex;
			flex-direction: column;
			
			.name {
				font-size: 36rpx;
				font-weight: bold;
				color: #333;
				margin-bottom: 10rpx;
				transition: all 0.3s ease;
			}
			
			.meta {
				font-size: 28rpx;
				color: #999;
				transition: all 0.3s ease;
			}
		}
	}
	
	.health-summary {
		display: flex;
		justify-content: space-between;
		border-top: 1rpx solid rgba(9, 132, 227, 0.1);
		padding-top: 30rpx;
		
		.summary-item {
			text-align: center;
			flex: 1;
			transition: all 0.3s ease;
			
			&:hover {
				transform: translateY(-2rpx);
			}
			
			.value {
				font-size: 36rpx;
				font-weight: bold;
				color: #0984e3;
				display: block;
				margin-bottom: 5rpx;
				transition: all 0.3s ease;
			}
			
			.label {
				font-size: 24rpx;
				color: #999;
				transition: all 0.3s ease;
			}
		}
	}
}

.section {
	background: rgba(255, 255, 255, 0.95);
	border-radius: 20rpx;
	margin-bottom: 30rpx;
	padding: 0 30rpx;
	box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
	backdrop-filter: blur(10rpx);
	animation: fadeInUp 0.6s ease-out;
	transition: all 0.3s ease;
	
	&:hover {
		transform: translateY(-2rpx);
		box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.15);
	}
	
	.section-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 30rpx 0;
		border-bottom: 1rpx solid rgba(9, 132, 227, 0.1);
		
		.title {
			font-size: 32rpx;
			font-weight: bold;
			color: #333;
			transition: all 0.3s ease;
		}
		
		.more {
			font-size: 26rpx;
			color: #0984e3;
			transition: all 0.3s ease;
			
			&:hover {
				color: #74b9ff;
			}
		}
	}
	
	.indicators {
		padding: 20rpx 0;
		
		.indicator {
			padding: 20rpx 0;
			border-bottom: 1rpx solid rgba(9, 132, 227, 0.1);
			transition: all 0.3s ease;
			
			&:last-child {
				border-bottom: none;
			}
			
			&:hover {
				background: rgba(9, 132, 227, 0.05);
				border-radius: 12rpx;
				padding: 20rpx;
				margin: 0 -20rpx;
			}
			
			.indicator-header {
				display: flex;
				justify-content: space-between;
				margin-bottom: 10rpx;
				
				.name {
					font-size: 28rpx;
					color: #666;
					transition: all 0.3s ease;
				}
				
				.value {
					font-size: 28rpx;
					font-weight: bold;
					color: #333;
					transition: all 0.3s ease;
					
					&.abnormal {
						color: #ff5a5f;
					}
				}
			}
			
			.indicator-range {
				display: flex;
				justify-content: space-between;
				
				.range {
					font-size: 24rpx;
					color: #999;
					transition: all 0.3s ease;
				}
				
				.status {
					font-size: 24rpx;
					transition: all 0.3s ease;
					
					&.normal {
						color: #09bb07;
					}
					
					&.abnormal {
						color: #ff5a5f;
					}
				}
			}
		}
	}
	
	.records {
		padding: 20rpx 0;
		
		.record-item {
			display: flex;
			justify-content: space-between;
			align-items: center;
			padding: 25rpx 0;
			border-bottom: 1rpx solid rgba(9, 132, 227, 0.1);
			transition: all 0.3s ease;
			cursor: pointer;
			
			&:last-child {
				border-bottom: none;
			}
			
			&:hover {
				background: rgba(9, 132, 227, 0.05);
				border-radius: 12rpx;
				padding: 25rpx 20rpx;
				margin: 0 -20rpx;
				transform: translateX(5rpx);
			}
			
			.record-left {
				.record-title {
					font-size: 30rpx;
					color: #333;
					margin-bottom: 10rpx;
					display: block;
					transition: all 0.3s ease;
				}
				
				.record-date {
					font-size: 24rpx;
					color: #999;
					transition: all 0.3s ease;
				}
			}
			
			.record-right {
				display: flex;
				align-items: center;
				
				.record-status {
					font-size: 26rpx;
					margin-right: 20rpx;
					transition: all 0.3s ease;
					
					&.completed {
						color: #09bb07;
					}
					
					&.ongoing {
						color: #0984e3;
					}
				}
				
				.arrow {
					width: 24rpx;
					height: 24rpx;
					transition: all 0.3s ease;
				}
			}
		}
	}
}

.action-bar {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;
	background: rgba(255, 255, 255, 0.95);
	padding: 20rpx 30rpx;
	display: flex;
	justify-content: space-between;
	border-top: 1rpx solid rgba(9, 132, 227, 0.1);
	box-shadow: 0 -4rpx 16rpx rgba(0, 0, 0, 0.1);
	backdrop-filter: blur(10rpx);
	
	.action-btn {
		flex: 1;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 28rpx;
		height: 80rpx;
		border-radius: 40rpx;
		margin: 0 10rpx;
		background: linear-gradient(135deg, #0984e3, #74b9ff);
		color: #fff;
		transition: all 0.3s ease;
		
		.btn-icon {
			width: 36rpx;
			height: 36rpx;
			margin-right: 10rpx;
			transition: all 0.3s ease;
		}
		
		&:after {
			border: none;
		}
		
		&:hover {
			transform: translateY(-3rpx) scale(1.05);
			box-shadow: 0 6rpx 20rpx rgba(9, 132, 227, 0.5);
			background: linear-gradient(135deg, #74b9ff, #0984e3);
		}
	}
}

/* Animation keyframes */
@keyframes float {
	0% {
		transform: translateY(0) translateX(0) scale(1);
		opacity: 0.8;
	}
	25% {
		transform: translateY(-10px) translateX(10px) scale(1.05);
		opacity: 0.9;
	}
	50% {
		transform: translateY(5px) translateX(-5px) scale(1.02);
		opacity: 1;
	}
	75% {
		transform: translateY(-5px) translateX(5px) scale(1.03);
		opacity: 0.9;
	}
	100% {
		transform: translateY(0) translateX(0) scale(1);
		opacity: 0.8;
	}
}

@keyframes shimmer {
	0%, 100% {
		opacity: 0.3;
		transform: translateX(-100%);
	}
	50% {
		opacity: 0.6;
		transform: translateX(100%);
	}
}

@keyframes fadeInDown {
	from {
		opacity: 0;
		transform: translateY(-30rpx);
	}
	to {
		opacity: 1;
		transform: translateY(0);
	}
}

@keyframes fadeInUp {
	from {
		opacity: 0;
		transform: translateY(30rpx);
	}
	to {
		opacity: 1;
		transform: translateY(0);
	}
}

@keyframes pulse {
	0% {
		transform: scale(1);
	}
	50% {
		transform: scale(1.05);
	}
	100% {
		transform: scale(1);
	}
}

@keyframes bounce {
	0%, 20%, 50%, 80%, 100% {
		transform: translateY(0);
	}
	40% {
		transform: translateY(-5rpx);
	}
	60% {
		transform: translateY(-3rpx);
	}
}
</style>