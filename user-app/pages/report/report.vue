<template>
	<view class="content">
		<!-- 报告列表 -->
		<view class="report-list" v-if="reportList.length > 0">
			<view class="report-item" v-for="(item, index) in reportList" :key="index" @click="viewReport(item)">
				<view class="report-header">
					<text class="report-title">{{item.packageName}}</text>
					<text class="report-date">{{item.reportDate}}</text>
				</view>
				<view class="report-info">
					<view class="info-row">
						<text class="info-label">体检医院：</text>
						<text class="info-value">{{item.hospitalName}}</text>
					</view>
					<view class="info-row">
						<text class="info-label">体检人：</text>
						<text class="info-value">{{item.personName}}</text>
					</view>
					<view class="info-row">
						<text class="info-label">体检时间：</text>
						<text class="info-value">{{item.examDate}}</text>
					</view>
				</view>
				<view class="report-summary">
					<view class="summary-item" :class="{'abnormal': item.abnormalCount > 0}">
						<text class="summary-value">{{item.abnormalCount}}</text>
						<text class="summary-label">异常指标</text>
					</view>
					<view class="summary-item">
						<text class="summary-value">{{item.totalCount}}</text>
						<text class="summary-label">检查项目</text>
					</view>
					<view class="summary-item">
						<text class="summary-value">{{item.adviceCount}}</text>
						<text class="summary-label">健康建议</text>
					</view>
				</view>
				<view class="report-actions">
					<button class="action-btn" @click.stop="shareReport(item)">
					  <image src="/static/icon/share.png" mode="aspectFit" class="btn-icon"></image>
					  <text>分享</text>
					</button>
					<button class="action-btn" @click.stop="downloadReport(item)">
						<image src="/static/icon/download.png" mode="aspectFit" class="btn-icon"></image>
						<text>下载</text>
					</button>
					<button class="action-btn primary" @click.stop="viewReport(item)">
						<image src="/static/icon/look3.png" mode="aspectFit" class="btn-icon"></image>
						<text>查看</text>
					</button>
				</view>
			</view>
		</view>
		
		<!-- 空状态 -->
		<view class="empty-state" v-else>
			<image class="empty-image" src="/static/images/empty-report.png" mode="aspectFit"></image>
			<text class="empty-text">暂无体检报告</text>
			<button class="make-appointment-btn" @click="makeAppointment">立即预约体检</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				reportList: [
					{
						id: 1,
						packageName: '基础体检套餐',
						reportDate: '2023-06-25',
						hospitalName: '沈阳市云医院-和平分院',
						personName: '张三',
						examDate: '2023-06-20',
						abnormalCount: 2,
						totalCount: 35,
						adviceCount: 3
					},
					{
						id: 2,
						packageName: '高级体检套餐',
						reportDate: '2023-05-15',
						hospitalName: '沈阳市云医院-沈河分院',
						personName: '张三',
						examDate: '2023-05-10',
						abnormalCount: 0,
						totalCount: 50,
						adviceCount: 2
					},
					{
						id: 3,
						packageName: '男性健康套餐',
						reportDate: '2023-03-20',
						hospitalName: '沈阳市云医院-和平分院',
						personName: '张三',
						examDate: '2023-03-15',
						abnormalCount: 1,
						totalCount: 45,
						adviceCount: 4
					}
				]
			}
		},
		onLoad() {
			// 获取报告列表
			this.getReportList();
		},
		methods: {
			// 获取报告列表
			getReportList() {
				// 这里可以替换为实际的API调用
				console.log('获取报告列表');
			},
			// 查看报告
			viewReport(report) {
				uni.navigateTo({
					url: `/pages/report-detail/report-detail?id=${report.id}`
				});
			},
			// 分享报告
			shareReport(report) {
				uni.showToast({
					title: '分享功能开发中',
					icon: 'none'
				});
			},
			// 下载报告
			downloadReport(report) {
				uni.showLoading({
					title: '下载中...'
				});
				
				// 模拟下载
				setTimeout(() => {
					uni.hideLoading();
					uni.showToast({
						title: '下载成功',
						icon: 'success'
					});
				}, 2000);
			},
			// 立即预约
			makeAppointment() {
				uni.switchTab({
					url: '/pages/appointment/appointment'
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

.btn-icon {
  width: 40rpx;
  height: 40rpx;
  margin-right: 10rpx;
  vertical-align: middle;
}

.content {
	background-color: #f5f5f5;
	min-height: 100vh;
}



.report-list {
	padding: 20rpx;
	
	.report-item {
		background-color: #ffffff;
		border-radius: 10rpx;
		margin-bottom: 20rpx;
		padding: 30rpx;
		box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
		
		.report-header {
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin-bottom: 20rpx;
			
			.report-title {
				font-size: 32rpx;
				font-weight: bold;
				color: #333333;
			}
			
			.report-date {
				font-size: 24rpx;
				color: #999999;
			}
		}
		
		.report-info {
			margin-bottom: 20rpx;
			
			.info-row {
				display: flex;
				margin-bottom: 10rpx;
				font-size: 28rpx;
				
				.info-label {
					width: 160rpx;
					color: #666666;
				}
				
				.info-value {
					flex: 1;
					color: #333333;
				}
			}
		}
		
		.report-summary {
			display: flex;
			background-color: #f9f9f9;
			border-radius: 10rpx;
			padding: 20rpx 0;
			margin-bottom: 20rpx;
			
			.summary-item {
				flex: 1;
				text-align: center;
				position: relative;
				
				&:not(:last-child)::after {
					content: '';
					position: absolute;
					right: 0;
					top: 20%;
					height: 60%;
					width: 1px;
					background-color: #eeeeee;
				}
				
				&.abnormal {
					.summary-value {
						color: #ff5a5f;
					}
				}
				
				.summary-value {
					font-size: 36rpx;
					font-weight: bold;
					color: #333333;
					display: block;
					margin-bottom: 5rpx;
				}
				
				.summary-label {
					font-size: 24rpx;
					color: #999999;
				}
			}
		}
		
		.report-actions {
			display: flex;
			justify-content: flex-end;
			
			.action-btn {
				display: flex;
				align-items: center;
				justify-content: center;
				font-size: 24rpx;
				padding: 0 20rpx;
				height: 60rpx;
				line-height: 1;
				border-radius: 30rpx;
				margin-left: 20rpx;
				background-color: #f5f5f5;
				color: #666666;
				
				.iconfont {
					font-size: 28rpx;
					margin-right: 6rpx;
					font-family: texticons;
				}
				
				&.primary {
					background-color: #1296db;
					color: #ffffff;
				}
				
				&::after {
					border: none;
				}
			}
		}
	}
}

.empty-state {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 100rpx 0;
	
	.empty-image {
		width: 200rpx;
		height: 200rpx;
		margin-bottom: 30rpx;
	}
	
	.empty-text {
		font-size: 28rpx;
		color: #999999;
		margin-bottom: 40rpx;
	}
	
	.make-appointment-btn {
		background-color: #1296db;
		color: #ffffff;
		font-size: 28rpx;
		padding: 0 40rpx;
		height: 80rpx;
		line-height: 80rpx;
		border-radius: 40rpx;
		
		&::after {
			border: none;
		}
	}
}
</style> 