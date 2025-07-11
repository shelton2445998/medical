<template>
	<view class="content">
<!-- 		页面标题
		<view class="page-header">
			<text class="page-title">健康档案</text>
		</view> -->
		
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
					<text class="value">72</text>
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
	background-color: #f5f5f5;
	min-height: 100vh;
	padding: 0 0 120rpx;
	position: relative;
}

.page-header {
	position: relative;
	background-color: #1296db;
	padding: 20rpx 0;
	text-align: center;
	
	.page-title {
		color: #ffffff;
		font-size: 36rpx;
		font-weight: bold;
	}
}

.back-btn {
	position: absolute;
	top: 30rpx;
	left: 30rpx;
	z-index: 10;
	width: 60rpx;
	height: 60rpx;
	border-radius: 50%;
	background-color: rgba(255,255,255,0.8);
	display: flex;
	align-items: center;
	justify-content: center;
	
	.back-icon {
		width: 36rpx;
		height: 36rpx;
	}
}

.info-card {
	background-color: #ffffff;
	padding: 30rpx;
	margin-bottom: 20rpx;
	
	.avatar-section {
		display: flex;
		align-items: center;
		margin-bottom: 30rpx;
		
		.avatar {
			width: 120rpx;
			height: 120rpx;
			border-radius: 50%;
			margin-right: 20rpx;
		}
		
		.user-basic {
			display: flex;
			flex-direction: column;
			
			.name {
				font-size: 36rpx;
				font-weight: bold;
				color: #333;
				margin-bottom: 10rpx;
			}
			
			.meta {
				font-size: 28rpx;
				color: #999;
			}
		}
	}
	
	.health-summary {
		display: flex;
		justify-content: space-between;
		border-top: 1rpx solid #eee;
		padding-top: 30rpx;
		
		.summary-item {
			text-align: center;
			flex: 1;
			
			.value {
				font-size: 36rpx;
				font-weight: bold;
				color: #1296db;
				display: block;
				margin-bottom: 5rpx;
			}
			
			.label {
				font-size: 24rpx;
				color: #999;
			}
		}
	}
}

.section {
	background-color: #ffffff;
	margin-bottom: 20rpx;
	padding: 0 30rpx;
	
	.section-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 30rpx 0;
		border-bottom: 1rpx solid #f5f5f5;
		
		.title {
			font-size: 32rpx;
			font-weight: bold;
			color: #333;
		}
		
		.more {
			font-size: 26rpx;
			color: #999;
		}
	}
	
	.indicators {
		padding: 20rpx 0;
		
		.indicator {
			padding: 20rpx 0;
			border-bottom: 1rpx solid #f5f5f5;
			
			&:last-child {
				border-bottom: none;
			}
			
			.indicator-header {
				display: flex;
				justify-content: space-between;
				margin-bottom: 10rpx;
				
				.name {
					font-size: 28rpx;
					color: #666;
				}
				
				.value {
					font-size: 28rpx;
					font-weight: bold;
					color: #333;
					
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
				}
				
				.status {
					font-size: 24rpx;
					
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
			border-bottom: 1rpx solid #f5f5f5;
			
			&:last-child {
				border-bottom: none;
			}
			
			.record-left {
				.record-title {
					font-size: 30rpx;
					color: #333;
					margin-bottom: 10rpx;
					display: block;
				}
				
				.record-date {
					font-size: 24rpx;
					color: #999;
				}
			}
			
			.record-right {
				display: flex;
				align-items: center;
				
				.record-status {
					font-size: 26rpx;
					margin-right: 20rpx;
					
					&.completed {
						color: #09bb07;
					}
					
					&.ongoing {
						color: #1296db;
					}
				}
				
				.arrow {
					width: 24rpx;
					height: 24rpx;
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
	background-color: #fff;
	padding: 20rpx 30rpx;
	display: flex;
	justify-content: space-between;
	border-top: 1rpx solid #eee;
	box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
	
	.action-btn {
		flex: 1;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 28rpx;
		height: 80rpx;
		border-radius: 40rpx;
		margin: 0 10rpx;
		background-color: #1296db;
		color: #fff;
		
		.btn-icon {
			width: 36rpx;
			height: 36rpx;
			margin-right: 10rpx;
		}
		
		&:after {
			border: none;
		}
	}
}
</style>