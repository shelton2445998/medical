<template>
	<view class="content">
		<!-- 页面标题 -->
		<view class="page-header">
			<text class="page-title">健康指标</text>
		</view>
		
		<!-- 指标概览 -->
		<view class="overview-section">
			<view class="overview-item" v-for="(item, index) in overviewData" :key="index">
				<view class="overview-icon">
					<image :src="item.icon" mode="aspectFit"></image>
				</view>
				<view class="overview-info">
					<text class="overview-name">{{item.name}}</text>
					<text class="overview-value">{{item.value}}</text>
					<text class="overview-unit">{{item.unit}}</text>
				</view>
				<view class="overview-status" :class="item.status">
					<text>{{getStatusText(item.status)}}</text>
				</view>
			</view>
		</view>
		
		<!-- 指标详情 -->
		<view class="indicator-section">
			<view class="section-title">指标详情</view>
			<view class="indicator-list">
				<view class="indicator-item" v-for="(indicator, index) in indicators" :key="index" @click="viewDetail(indicator)">
					<view class="indicator-header">
						<view class="indicator-info">
							<image :src="indicator.icon" class="indicator-icon" mode="aspectFit"></image>
							<text class="indicator-name">{{indicator.name}}</text>
						</view>
						<view class="indicator-status" :class="indicator.status">
							<text>{{getStatusText(indicator.status)}}</text>
						</view>
					</view>
					<view class="indicator-data">
						<view class="data-item">
							<text class="data-label">当前值</text>
							<text class="data-value">{{indicator.currentValue}}{{indicator.unit}}</text>
						</view>
						<view class="data-item">
							<text class="data-label">正常范围</text>
							<text class="data-range">{{indicator.normalRange}}</text>
						</view>
						<view class="data-item">
							<text class="data-label">测量时间</text>
							<text class="data-time">{{indicator.measureTime}}</text>
						</view>
					</view>
					<view class="indicator-trend">
						<text class="trend-label">趋势</text>
						<view class="trend-chart">
							<view class="trend-point" v-for="(point, idx) in indicator.trend" :key="idx" :style="{height: point + '%'}"></view>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 添加记录 -->
		<view class="add-record-section">
			<button class="add-btn" @click="addRecord">
				<image src="/static/images/icons/icon-add.png" mode="aspectFit" class="add-icon"></image>
				<text>添加记录</text>
			</button>
		</view>
		
		<!-- 健康建议 -->
		<view class="advice-section">
			<view class="section-title">健康建议</view>
			<view class="advice-list">
				<view class="advice-item" v-for="(advice, index) in healthAdvice" :key="index">
					<view class="advice-icon">
						<image :src="advice.icon" mode="aspectFit"></image>
					</view>
					<view class="advice-content">
						<text class="advice-title">{{advice.title}}</text>
						<text class="advice-desc">{{advice.description}}</text>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				overviewData: [
					{
						name: '体重',
						value: '65.2',
						unit: 'kg',
						icon: '/static/images/icons/icon-weight.png',
						status: 'normal'
					},
					{
						name: '血压',
						value: '120/80',
						unit: 'mmHg',
						icon: '/static/images/icons/icon-blood-pressure.png',
						status: 'normal'
					},
					{
						name: '血糖',
						value: '5.2',
						unit: 'mmol/L',
						icon: '/static/images/icons/icon-blood-sugar.png',
						status: 'normal'
					},
					{
						name: '心率',
						value: '72',
						unit: 'bpm',
						icon: '/static/images/icons/icon-heart-rate.png',
						status: 'normal'
					}
				],
				indicators: [
					{
						id: 1,
						name: '体重',
						icon: '/static/images/icons/icon-weight.png',
						currentValue: '65.2',
						unit: 'kg',
						normalRange: '50-80kg',
						measureTime: '2023-07-15 08:30',
						status: 'normal',
						trend: [60, 62, 63, 64, 65, 65.2]
					},
					{
						id: 2,
						name: '血压',
						icon: '/static/images/icons/icon-blood-pressure.png',
						currentValue: '120/80',
						unit: 'mmHg',
						normalRange: '90-140/60-90mmHg',
						measureTime: '2023-07-15 09:00',
						status: 'normal',
						trend: [110, 115, 118, 120, 122, 120]
					},
					{
						id: 3,
						name: '血糖',
						icon: '/static/images/icons/icon-blood-sugar.png',
						currentValue: '5.2',
						unit: 'mmol/L',
						normalRange: '3.9-6.1mmol/L',
						measureTime: '2023-07-15 07:30',
						status: 'normal',
						trend: [4.8, 5.0, 5.1, 5.3, 5.2, 5.2]
					},
					{
						id: 4,
						name: '心率',
						icon: '/static/images/icons/icon-heart-rate.png',
						currentValue: '72',
						unit: 'bpm',
						normalRange: '60-100bpm',
						measureTime: '2023-07-15 08:45',
						status: 'normal',
						trend: [68, 70, 71, 73, 72, 72]
					}
				],
				healthAdvice: [
					{
						title: '定期监测',
						description: '建议每周测量一次体重，每天测量血压',
						icon: '/static/images/icons/icon-monitor.png'
					},
					{
						title: '健康饮食',
						description: '控制盐分摄入，多吃蔬菜水果',
						icon: '/static/images/icons/icon-diet.png'
					},
					{
						title: '适量运动',
						description: '每周进行3-5次有氧运动',
						icon: '/static/images/icons/icon-exercise.png'
					},
					{
						title: '规律作息',
						description: '保证充足睡眠，避免熬夜',
						icon: '/static/images/icons/icon-sleep.png'
					}
				]
			}
		},
		onLoad() {
			// 获取健康指标数据
			this.getHealthData();
		},
		methods: {
			// 获取健康数据
			getHealthData() {
				// 这里可以替换为实际的API调用
				console.log('获取健康数据');
			},
			// 查看详情
			viewDetail(indicator) {
				uni.navigateTo({
					url: `/pages/indicator-detail/indicator-detail?id=${indicator.id}&name=${indicator.name}`
				});
			},
			// 添加记录
			addRecord() {
				uni.navigateTo({
					url: '/pages/add-indicator/add-indicator'
				});
			},
			// 获取状态文本
			getStatusText(status) {
				const statusMap = {
					'excellent': '优秀',
					'good': '良好',
					'normal': '正常',
					'warning': '偏高',
					'danger': '异常'
				};
				return statusMap[status] || '';
			}
		}
	}
</script>

<style lang="scss">
.content {
	background-color: #f5f5f5;
	min-height: 100vh;
	padding: 20rpx;
}

.page-header {
	text-align: center;
	padding: 40rpx 0;
	
	.page-title {
		font-size: 36rpx;
		font-weight: bold;
		color: #333333;
	}
}

.overview-section {
	background-color: #ffffff;
	border-radius: 20rpx;
	padding: 30rpx;
	margin-bottom: 30rpx;
	
	.overview-item {
		display: flex;
		align-items: center;
		padding: 20rpx 0;
		border-bottom: 1px solid #f5f5f5;
		
		&:last-child {
			border-bottom: none;
		}
		
		.overview-icon {
			width: 60rpx;
			height: 60rpx;
			margin-right: 20rpx;
			
			image {
				width: 100%;
				height: 100%;
			}
		}
		
		.overview-info {
			flex: 1;
			
			.overview-name {
				font-size: 28rpx;
				color: #333333;
				margin-bottom: 8rpx;
				display: block;
			}
			
			.overview-value {
				font-size: 32rpx;
				font-weight: bold;
				color: #1296db;
				margin-bottom: 4rpx;
				display: block;
			}
			
			.overview-unit {
				font-size: 22rpx;
				color: #999999;
				display: block;
			}
		}
		
		.overview-status {
			padding: 8rpx 16rpx;
			border-radius: 15rpx;
			font-size: 22rpx;
			
			&.excellent {
				background-color: #52c41a;
				color: #ffffff;
			}
			
			&.good {
				background-color: #1296db;
				color: #ffffff;
			}
			
			&.normal {
				background-color: #faad14;
				color: #ffffff;
			}
			
			&.warning {
				background-color: #ff7a45;
				color: #ffffff;
			}
			
			&.danger {
				background-color: #ff4d4f;
				color: #ffffff;
			}
		}
	}
}

.indicator-section {
	background-color: #ffffff;
	border-radius: 20rpx;
	padding: 30rpx;
	margin-bottom: 30rpx;
	
	.section-title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333333;
		margin-bottom: 30rpx;
	}
	
	.indicator-list {
		.indicator-item {
			background-color: #f8f9fa;
			border-radius: 15rpx;
			padding: 25rpx;
			margin-bottom: 20rpx;
			
			.indicator-header {
				display: flex;
				justify-content: space-between;
				align-items: center;
				margin-bottom: 20rpx;
				
				.indicator-info {
					display: flex;
					align-items: center;
					
					.indicator-icon {
						width: 40rpx;
						height: 40rpx;
						margin-right: 15rpx;
					}
					
					.indicator-name {
						font-size: 28rpx;
						font-weight: bold;
						color: #333333;
					}
				}
				
				.indicator-status {
					padding: 6rpx 12rpx;
					border-radius: 12rpx;
					font-size: 20rpx;
					
					&.excellent {
						background-color: #52c41a;
						color: #ffffff;
					}
					
					&.good {
						background-color: #1296db;
						color: #ffffff;
					}
					
					&.normal {
						background-color: #faad14;
						color: #ffffff;
					}
					
					&.warning {
						background-color: #ff7a45;
						color: #ffffff;
					}
					
					&.danger {
						background-color: #ff4d4f;
						color: #ffffff;
					}
				}
			}
			
			.indicator-data {
				display: flex;
				justify-content: space-between;
				margin-bottom: 20rpx;
				
				.data-item {
					flex: 1;
					text-align: center;
					
					.data-label {
						font-size: 22rpx;
						color: #999999;
						margin-bottom: 8rpx;
						display: block;
					}
					
					.data-value {
						font-size: 26rpx;
						font-weight: bold;
						color: #333333;
						display: block;
					}
					
					.data-range {
						font-size: 24rpx;
						color: #666666;
						display: block;
					}
					
					.data-time {
						font-size: 22rpx;
						color: #999999;
						display: block;
					}
				}
			}
			
			.indicator-trend {
				.trend-label {
					font-size: 24rpx;
					color: #666666;
					margin-bottom: 15rpx;
					display: block;
				}
				
				.trend-chart {
					display: flex;
					align-items: end;
					height: 60rpx;
					gap: 8rpx;
					
					.trend-point {
						flex: 1;
						background-color: #1296db;
						border-radius: 2rpx;
						min-height: 4rpx;
					}
				}
			}
		}
	}
}

.add-record-section {
	margin-bottom: 30rpx;
	
	.add-btn {
		width: 100%;
		height: 90rpx;
		background-color: #1296db;
		color: #ffffff;
		border-radius: 45rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 28rpx;
		
		.add-icon {
			width: 40rpx;
			height: 40rpx;
			margin-right: 15rpx;
		}
		
		&::after {
			border: none;
		}
	}
}

.advice-section {
	background-color: #ffffff;
	border-radius: 20rpx;
	padding: 30rpx;
	
	.section-title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333333;
		margin-bottom: 30rpx;
	}
	
	.advice-list {
		.advice-item {
			display: flex;
			align-items: center;
			padding: 20rpx 0;
			border-bottom: 1px solid #f5f5f5;
			
			&:last-child {
				border-bottom: none;
			}
			
			.advice-icon {
				width: 60rpx;
				height: 60rpx;
				margin-right: 20rpx;
				
				image {
					width: 100%;
					height: 100%;
				}
			}
			
			.advice-content {
				flex: 1;
				
				.advice-title {
					font-size: 28rpx;
					font-weight: bold;
					color: #333333;
					margin-bottom: 8rpx;
					display: block;
				}
				
				.advice-desc {
					font-size: 24rpx;
					color: #666666;
					display: block;
				}
			}
		}
	}
}
</style> 