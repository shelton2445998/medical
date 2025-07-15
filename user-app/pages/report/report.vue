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
		
		<!-- 报告详情弹窗 -->
		<view class="report-detail-modal" v-if="reportDetail" @click="closeReportDetail">
			<view class="modal-content" @click.stop>
				<view class="modal-header">
					<text class="modal-title">体检报告详情</text>
					<text class="modal-close" @click="closeReportDetail">×</text>
				</view>
				<view class="modal-body">
					<view class="detail-section">
						<view class="detail-item">
							<text class="detail-label">报告ID：</text>
							<text class="detail-value">{{reportDetail.id}}</text>
						</view>
						<view class="detail-item">
							<text class="detail-label">订单ID：</text>
							<text class="detail-value">{{reportDetail.orderId}}</text>
						</view>
						<view class="detail-item">
							<text class="detail-label">用户ID：</text>
							<text class="detail-value">{{reportDetail.userId}}</text>
						</view>
						<view class="detail-item">
							<text class="detail-label">状态：</text>
							<text class="detail-value" :class="{'status-completed': reportDetail.status === 1, 'status-pending': reportDetail.status === 0}">
								{{reportDetail.status === 1 ? '已完成' : '未完成'}}
							</text>
						</view>
						<view class="detail-item">
							<text class="detail-label">医生ID：</text>
							<text class="detail-value">{{reportDetail.doctorId}}</text>
						</view>
						<view class="detail-item">
							<text class="detail-label">报告日期：</text>
							<text class="detail-value">{{formatDate(reportDetail.reportDate)}}</text>
						</view>
						<view class="detail-item">
							<text class="detail-label">创建时间：</text>
							<text class="detail-value">{{formatDateTime(reportDetail.createTime)}}</text>
						</view>
						<view class="detail-item">
							<text class="detail-label">总结论：</text>
							<text class="detail-value">{{reportDetail.conclusion || '暂无结论'}}</text>
						</view>
						<view class="detail-item">
							<text class="detail-label">检查项ID：</text>
							<text class="detail-value">{{reportDetail.checkitemIds || '无'}}</text>
						</view>
						<view class="detail-item">
							<text class="detail-label">报告项ID：</text>
							<text class="detail-value">{{reportDetail.reportItemIds || '无'}}</text>
						</view>
					</view>
				</view>
				<view class="modal-footer">
					<button class="modal-btn" @click="closeReportDetail">关闭</button>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import { getCurrentUserReportItemPage, getAppReportById } from '@/api/report';
	import { getLoginUserInfo } from '@/api/user';
	
	export default {
		data() {
			return {
				reportList: [],
				loading: false,
				error: null,
				userInfo: null, // 存储用户信息
				reportDetail: null // 存储报告详情
			}
		},
		onLoad() {
			// 先获取用户信息，然后再获取报告列表
			this.getUserInfo().then(() => {
				this.getReportList();
			}).catch(error => {
				console.error('初始化数据失败:', error);
				// 即使获取用户信息失败，也尝试获取报告列表
				this.getReportList();
			});
		},
		methods: {
			// 获取用户信息
			async getUserInfo() {
				return new Promise(async (resolve, reject) => {
					try {
						const res = await getLoginUserInfo();
						console.log('用户信息:', res);
						
						if (res && res.success && res.data) {
							this.userInfo = res.data;
							console.log('获取到的用户信息:', this.userInfo);
							resolve(this.userInfo);
						} else {
							console.error('获取用户信息失败:', res);
							uni.showToast({
								title: '获取用户信息失败',
								icon: 'none'
							});
							reject(new Error('获取用户信息失败'));
						}
					} catch (error) {
						console.error('获取用户信息出错:', error);
						uni.showToast({
							title: '获取用户信息失败',
							icon: 'none'
						});
						reject(error);
					}
				});
			},
			
			// 获取报告列表
			async getReportList() {
				this.loading = true;
				this.error = null;
				
				try {
					// 添加查询参数，包含分页信息和用户ID（如果有）
					const query = {
						pageNum: 1,
						pageSize: 10
					};
					
					// 如果已经获取到用户信息，添加用户ID到查询参数
					if (this.userInfo && this.userInfo.id) {
						query.userId = this.userInfo.id;
					}
					
					const res = await getCurrentUserReportItemPage(query);
					console.log('API返回数据:', res);
					
					// 根据API实际返回的数据结构处理
					if (res && res.success && res.data && res.data.list && Array.isArray(res.data.list)) {
						// 使用res.data.list作为数据源
						this.reportList = res.data.list.map(item => ({
							id: item.id,
							packageName: item.packageName || '未命名套餐',
							reportDate: item.createTime || '',
							hospitalName: item.hospitalName || '',
							personName: item.personName || '',
							examDate: item.examDate || '',
							abnormalCount: item.abnormalCount || 0,
							totalCount: item.totalCount || 0,
							adviceCount: item.adviceCount || 0
						}));
					} else {
						// 如果返回的数据结构不符合预期
						this.reportList = [];
						console.error('API返回的数据结构不符合预期:', res);
					}
				} catch (error) {
					console.error('获取报告列表失败:', error);
					this.error = error;
					uni.showToast({
						title: '获取报告列表失败',
						icon: 'none'
					});
				} finally {
					this.loading = false;
				}
			},
			
			// 查看报告详情
			async viewReport(report) {
				try {
					uni.showLoading({
						title: '加载中...'
					});
					
					// 调用 getAppReportById 接口获取报告详情
					const res = await getAppReportById(report.id);
					console.log('报告详情数据:', res);
					
					if (res && res.success && res.data) {
						this.reportDetail = res.data;
						// 直接显示模态框，不需要额外调用
					} else {
						uni.showToast({
							title: '获取报告详情失败',
							icon: 'none'
						});
					}
				} catch (error) {
					console.error('获取报告详情失败:', error);
					uni.showToast({
						title: '获取报告详情失败',
						icon: 'none'
					});
				} finally {
					uni.hideLoading();
				}
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
			},
			// 关闭报告详情弹窗
			closeReportDetail() {
				this.reportDetail = null;
			},
			// 格式化日期
			formatDate(timestamp) {
				if (!timestamp) return '';
				const date = new Date(timestamp);
				return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')}`;
			},
			// 格式化日期时间
			formatDateTime(timestamp) {
				if (!timestamp) return '';
				const date = new Date(timestamp);
				return `${date.getFullYear()}-${(date.getMonth() + 1).toString().padStart(2, '0')}-${date.getDate().toString().padStart(2, '0')} ${date.getHours().toString().padStart(2, '0')}:${date.getMinutes().toString().padStart(2, '0')}`;
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

/* 报告详情弹窗样式 */
.report-detail-modal {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background-color: rgba(0, 0, 0, 0.6);
	display: flex;
	justify-content: center;
	align-items: center;
	z-index: 1000;
}

.modal-content {
	background-color: #ffffff;
	border-radius: 20rpx;
	width: 90%;
	max-height: 80%;
	display: flex;
	flex-direction: column;
}

.modal-header {
	display: flex;
	justify-content: space-between;
	align-items: center;
	padding: 30rpx;
	border-bottom: 1rpx solid #eeeeee;
}

.modal-title {
	font-size: 36rpx;
	font-weight: bold;
	color: #333333;
}

.modal-close {
	font-size: 48rpx;
	color: #999999;
}

.modal-body {
	padding: 30rpx;
	overflow-y: auto;
	max-height: 60%;
}

.detail-section {
	margin-bottom: 20rpx;
}

.detail-item {
	display: flex;
	justify-content: space-between;
	align-items: center;
	font-size: 28rpx;
	color: #333333;
	margin-bottom: 15rpx;
}

.detail-label {
	font-weight: bold;
	color: #666666;
}

.detail-value {
	flex: 1;
	text-align: right;
	color: #333333;
}

.status-completed {
	color: #1296db;
}

.status-pending {
	color: #ff5a5f;
}

.modal-footer {
	padding: 30rpx;
	border-top: 1rpx solid #eeeeee;
	text-align: right;
}

.modal-btn {
	background-color: #1296db;
	color: #ffffff;
	font-size: 32rpx;
	padding: 0 40rpx;
	height: 80rpx;
	line-height: 80rpx;
	border-radius: 40rpx;
	
	&::after {
		border: none;
	}
}
</style> 