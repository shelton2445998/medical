<template>
	<view class="content">
		<!-- 报告基本信息 -->
		<view class="report-info-card">
			<view class="report-title">
				<text>{{reportInfo.packageName}}</text>
				<text class="report-date">{{reportInfo.reportDate}}</text>
			</view>
			<view class="info-list">
				<view class="info-item">
					<text class="info-label">体检人：</text>
					<text class="info-value">{{reportInfo.personName}}</text>
				</view>
				<view class="info-item">
					<text class="info-label">性别：</text>
					<text class="info-value">{{reportInfo.gender}}</text>
				</view>
				<view class="info-item">
					<text class="info-label">年龄：</text>
					<text class="info-value">{{reportInfo.age}}岁</text>
				</view>
				<view class="info-item">
					<text class="info-label">体检医院：</text>
					<text class="info-value">{{reportInfo.hospitalName}}</text>
				</view>
				<view class="info-item">
					<text class="info-label">体检时间：</text>
					<text class="info-value">{{reportInfo.examDate}}</text>
				</view>
				<view class="info-item">
					<text class="info-label">报告时间：</text>
					<text class="info-value">{{reportInfo.reportDate}}</text>
				</view>
			</view>
		</view>
		
		<!-- 体检结果概要 -->
		<view class="summary-card">
			<view class="card-title">体检结果概要</view>
			<view class="summary-content">
				<view class="summary-item" :class="{'abnormal': reportInfo.abnormalCount > 0}">
					<text class="summary-value">{{reportInfo.abnormalCount}}</text>
					<text class="summary-label">异常指标</text>
				</view>
				<view class="summary-item">
					<text class="summary-value">{{reportInfo.totalCount}}</text>
					<text class="summary-label">检查项目</text>
				</view>
				<view class="summary-item">
					<text class="summary-value">{{reportInfo.adviceCount}}</text>
					<text class="summary-label">健康建议</text>
				</view>
			</view>
		</view>
		
		<!-- 体检结果列表 -->
		<view class="result-card">
			<view class="card-title">体检结果</view>
			<view class="filter-tabs">
				<view 
					class="tab-item" 
					v-for="(item, index) in filterOptions" 
					:key="index"
					:class="{active: currentFilter === item.value}"
					@click="switchFilter(item.value)"
				>
					<text>{{item.label}}</text>
				</view>
			</view>
			
			<!-- 体检项目列表 -->
			<view class="result-list">
				<view 
					class="result-item" 
					v-for="(item, index) in filteredResults" 
					:key="index"
					:class="{'abnormal': item.isAbnormal}"
				>
					<view class="result-header" @click="toggleExpand(index)">
						<view class="result-name">
							<text class="status-icon" v-if="item.isAbnormal">!</text>
							<text>{{item.name}}</text>
						</view>
						<view class="result-value">
							<text :class="{'abnormal-text': item.isAbnormal}">{{item.value}}</text>
							<text class="result-unit">{{item.unit}}</text>
							<text class="expand-icon">{{item.expanded ? '∧' : '∨'}}</text>
						</view>
					</view>
					<view class="result-detail" v-if="item.expanded">
						<view class="reference-range">
							<text class="detail-label">参考范围：</text>
							<text class="detail-value">{{item.referenceRange}}</text>
						</view>
						<view class="result-desc" v-if="item.description">
							<text class="detail-label">结果说明：</text>
							<text class="detail-value">{{item.description}}</text>
						</view>
						<view class="result-advice" v-if="item.advice">
							<text class="detail-label">健康建议：</text>
							<text class="detail-value">{{item.advice}}</text>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 健康建议 -->
		<view class="advice-card">
			<view class="card-title">健康建议</view>
			<view class="advice-list">
				<view class="advice-item" v-for="(item, index) in reportInfo.adviceList" :key="index">
					<text class="advice-index">{{index + 1}}</text>
					<text class="advice-content">{{item}}</text>
				</view>
			</view>
		</view>
		
		<!-- 底部操作栏 -->
		<view class="bottom-actions">
			<button class="action-btn" @click="shareReport">
				<text class="iconfont icon-share">&#xe60f;</text>
				<text>分享</text>
			</button>
			<button class="action-btn" @click="downloadReport">
				<text class="iconfont icon-download">&#xe60f;</text>
				<text>下载</text>
			</button>
			<button class="action-btn primary" @click="makeConsultation">
				<text class="iconfont icon-doctor">&#xe60f;</text>
				<text>在线咨询</text>
			</button>
		</view>
	</view>
</template>

<script>
	import { getReportDetail } from '@/api/report'
	
	export default {
		data() {
			return {
				reportId: null,
				currentFilter: 'all', // all, abnormal
				filterOptions: [
					{ label: '全部', value: 'all' },
					{ label: '异常', value: 'abnormal' }
				],
				reportInfo: {
					packageName: '',
					reportDate: '',
					hospitalName: '',
					personName: '',
					gender: '',
					age: 0,
					examDate: '',
					abnormalCount: 0,
					totalCount: 0,
					adviceCount: 0,
					adviceList: []
				},
				examResults: [],
				filteredResults: [],
				loading: false
			}
		},
		onLoad(options) {
			if (options.id) {
				this.reportId = options.id;
				// 获取报告详情
				this.getReportDetail();
			}
			
			// 初始化显示所有检查结果
			this.filteredResults = this.examResults;
		},
		methods: {
			// 获取报告详情
			async getReportDetail() {
				this.loading = true;
				try {
					const res = await getReportDetail(this.reportId);
					
					// 更新报告基本信息
					this.reportInfo = {
						packageName: res.packageName || '',
						reportDate: res.reportDate || '',
						hospitalName: res.hospitalName || '',
						personName: res.personName || '',
						gender: res.gender || '',
						age: res.age || 0,
						examDate: res.examDate || '',
						abnormalCount: res.abnormalCount || 0,
						totalCount: res.totalCount || 0,
						adviceCount: res.adviceList?.length || 0,
						adviceList: res.adviceList || []
					};
					
					// 更新检查结果数据
					this.examResults = (res.examResults || []).map(item => ({
						name: item.name || '',
						value: item.value || '',
						unit: item.unit || '',
						referenceRange: item.referenceRange || '',
						isAbnormal: item.isAbnormal || false,
						description: item.description || '',
						advice: item.advice || '',
						expanded: false
					}));
					
					// 初始化过滤结果
					this.filteredResults = this.examResults;
					
				} catch (error) {
					console.error('获取报告详情失败:', error);
					uni.showToast({
						title: '获取报告详情失败',
						icon: 'none'
					});
				} finally {
					this.loading = false;
				}
			},
			// 切换筛选条件
			switchFilter(filter) {
				this.currentFilter = filter;
				
				if (filter === 'all') {
					this.filteredResults = this.examResults;
				} else if (filter === 'abnormal') {
					this.filteredResults = this.examResults.filter(item => item.isAbnormal);
				}
			},
			// 展开/收起检查项
			toggleExpand(index) {
				this.filteredResults[index].expanded = !this.filteredResults[index].expanded;
			},
			// 分享报告
			shareReport() {
				uni.showToast({
					title: '分享功能开发中',
					icon: 'none'
				});
			},
			// 下载报告
			downloadReport() {
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
			// 在线咨询
			makeConsultation() {
				uni.showToast({
					title: '在线咨询功能开发中',
					icon: 'none'
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
	background-color: #f5f5f5;
	min-height: 100vh;
	padding-bottom: 120rpx;
}



.report-info-card {
	background-color: #ffffff;
	margin: 20rpx;
	padding: 30rpx;
	border-radius: 10rpx;
	box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
	
	.report-title {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 20rpx;
		font-size: 32rpx;
		font-weight: bold;
		color: #333333;
		
		.report-date {
			font-size: 24rpx;
			font-weight: normal;
			color: #999999;
		}
	}
	
	.info-list {
		.info-item {
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
}

.summary-card, .result-card, .advice-card {
	background-color: #ffffff;
	margin: 20rpx;
	padding: 30rpx;
	border-radius: 10rpx;
	box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
	
	.card-title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333333;
		margin-bottom: 20rpx;
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

.summary-content {
	display: flex;
	background-color: #f9f9f9;
	border-radius: 10rpx;
	padding: 20rpx 0;
	
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

.filter-tabs {
	display: flex;
	margin-bottom: 20rpx;
	
	.tab-item {
		padding: 10rpx 30rpx;
		font-size: 28rpx;
		color: #666666;
		background-color: #f5f5f5;
		border-radius: 30rpx;
		margin-right: 20rpx;
		
		&.active {
			color: #ffffff;
			background-color: #1296db;
		}
	}
}

.result-list {
	.result-item {
		border-bottom: 1px solid #eeeeee;
		padding: 20rpx 0;
		
		&:last-child {
			border-bottom: none;
		}
		
		&.abnormal {
			.result-header {
				.result-name {
					.status-icon {
						display: inline-block;
						width: 32rpx;
						height: 32rpx;
						line-height: 32rpx;
						text-align: center;
						background-color: #ff5a5f;
						color: #ffffff;
						border-radius: 50%;
						font-size: 24rpx;
						margin-right: 10rpx;
					}
				}
			}
		}
		
		.result-header {
			display: flex;
			justify-content: space-between;
			align-items: center;
			
			.result-name {
				font-size: 28rpx;
				color: #333333;
				display: flex;
				align-items: center;
			}
			
			.result-value {
				font-size: 28rpx;
				color: #333333;
				display: flex;
				align-items: center;
				
				.abnormal-text {
					color: #ff5a5f;
				}
				
				.result-unit {
					font-size: 24rpx;
					color: #999999;
					margin-left: 5rpx;
				}
				
				.expand-icon {
					margin-left: 20rpx;
					color: #999999;
				}
			}
		}
		
		.result-detail {
			margin-top: 15rpx;
			padding: 15rpx;
			background-color: #f9f9f9;
			border-radius: 8rpx;
			font-size: 26rpx;
			
			.reference-range, .result-desc, .result-advice {
				margin-bottom: 10rpx;
				
				&:last-child {
					margin-bottom: 0;
				}
				
				.detail-label {
					color: #666666;
				}
				
				.detail-value {
					color: #333333;
				}
			}
			
			.result-advice {
				.detail-value {
					color: #1296db;
				}
			}
		}
	}
}

.advice-list {
	.advice-item {
		display: flex;
		margin-bottom: 15rpx;
		
		&:last-child {
			margin-bottom: 0;
		}
		
		.advice-index {
			width: 40rpx;
			height: 40rpx;
			line-height: 40rpx;
			text-align: center;
			background-color: #1296db;
			color: #ffffff;
			border-radius: 50%;
			font-size: 24rpx;
			margin-right: 15rpx;
			flex-shrink: 0;
		}
		
		.advice-content {
			flex: 1;
			font-size: 28rpx;
			color: #333333;
			line-height: 1.5;
		}
	}
}

.bottom-actions {
	position: fixed;
	left: 0;
	right: 0;
	bottom: 0;
	background-color: #ffffff;
	display: flex;
	padding: 20rpx;
	box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
	
	.action-btn {
		flex: 1;
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 28rpx;
		height: 80rpx;
		line-height: 1;
		border-radius: 40rpx;
		margin: 0 10rpx;
		background-color: #f5f5f5;
		color: #666666;
		
		.iconfont {
			font-size: 32rpx;
			margin-right: 8rpx;
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
</style> 