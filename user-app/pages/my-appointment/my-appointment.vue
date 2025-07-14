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
			<!-- 页面头部 -->
			<view class="page-header">
				<view class="header-icon">📋</view>
				<view class="header-title">我的预约</view>
				<view class="header-desc">查看和管理您的体检预约</view>
			</view>
			
			<!-- 预约状态选项卡 -->
			<view class="status-tabs">
				<view 
					class="tab-item" 
					v-for="(item, index) in statusList" 
					:key="index"
					:class="{active: currentStatus === item.id}"
					@click="switchStatus(item.id)"
				>
					<text class="tab-icon">{{item.icon}}</text>
					<text class="tab-text">{{item.name}}</text>
					<view class="tab-count" v-if="getStatusCount(item.id) > 0">{{getStatusCount(item.id)}}</view>
				</view>
			</view>
			
			<!-- 预约列表 -->
			<view class="appointment-list" v-if="filteredAppointments.length > 0">
				<view class="appointment-item" v-for="(item, index) in filteredAppointments" :key="index" @click="viewDetail(item)">
					<view class="appointment-header">
						<view class="hospital-info">
							<text class="hospital-icon">🏥</text>
							<text class="hospital-name">{{item.hospitalName || '体检医院'}}</text>
						</view>
						<view class="status-badge" :class="'status-' + item.status">
							<text class="status-icon">{{getStatusIcon(item.status)}}</text>
							<text class="status-text">{{getStatusName(item.status)}}</text>
						</view>
					</view>
					
					<view class="appointment-content">
						<view class="info-grid">
							<view class="info-item">
								<text class="info-label">预约时间</text>
								<text class="info-value">{{formatDate(item.appointmentDate)}} {{item.timeSlot || '上午'}}</text>
							</view>
							<view class="info-item">
								<text class="info-label">订单号</text>
								<text class="info-value">{{item.orderNumber || 'N/A'}}</text>
							</view>
							<view class="info-item" v-if="item.price">
								<text class="info-label">套餐价格</text>
								<text class="info-value price">¥{{item.price}}</text>
							</view>
							<view class="info-item" v-if="item.amount">
								<text class="info-label">订单金额</text>
								<text class="info-value price">¥{{item.amount}}</text>
							</view>
						</view>
						
						<view class="appointment-actions">
							<button 
								class="action-btn cancel-btn" 
								v-if="item.status === 1" 
								@click.stop="cancelAppointment(item)"
							>
								<text class="btn-icon">❌</text>
								<text class="btn-text">取消预约</text>
							</button>
							<button 
								class="action-btn report-btn" 
								v-if="item.status === 2 || item.status === 3" 
								@click.stop="viewReport(item)"
							>
								<text class="btn-icon">📊</text>
								<text class="btn-text">查看报告</text>
							</button>
							<button 
								class="action-btn detail-btn" 
								@click.stop="viewDetail(item)"
							>
								<text class="btn-icon">👁️</text>
								<text class="btn-text">查看详情</text>
							</button>
						</view>
					</view>
				</view>
			</view>
			
			<!-- 空状态 -->
			<view class="empty-state" v-else>
				<view class="empty-icon">📋</view>
				<text class="empty-title">暂无预约记录</text>
				<text class="empty-desc">您还没有任何体检预约，快来预约一次专业的体检吧</text>
				<button class="make-appointment-btn" @click="makeAppointment">
					<text class="btn-icon">🏥</text>
					<text class="btn-text">立即预约</text>
				</button>
			</view>
		</view>
	</view>
</template>

<script>
import { appointmentApi } from '@/utils/api.js';
import { cancelAppointment } from '@/api/appointment.js';

export default {
	data() {
		return {
			currentStatus: 0, // 0表示全部
			statusList: [
				{ id: 0, name: '全部', icon: '📋' },
				{ id: 1, name: '待支付', icon: '⚪️' },
				{ id: 2, name: '已支付', icon: '✅' },
				{ id: 3, name: '已完成', icon: '✅' }
			],
			appointments: [],
			filteredAppointments: [],
			loading: false
		}
	},
		onLoad() {
			// 获取预约列表数据
			this.getAppointmentList();
		},
		
		onShow() {
			// 页面显示时刷新数据
			this.getAppointmentList();
		},
		methods: {
			// 获取预约列表
			getAppointmentList() {
				if (this.loading) return;
				
				this.loading = true;
				
				// 获取token
				const token = uni.getStorageSync('uniIdToken');
				console.log('获取到的token:', token);
				
				if (!token) {
					uni.showToast({
						title: '请先登录',
						icon: 'none'
					});
					return;
				}
				
				uni.request({
					url: appointmentApi.getAppointmentList,
					method: 'GET',
					header: {
						'Authorization': token || ''
					},
					success: (res) => {
						console.log('获取预约列表响应：', res);
						console.log('响应数据结构：', res.data);
						
						if (res.statusCode === 200 && res.data.code === 200) {
							// 修复：后端返回的是Paging对象，数据在list字段中
							this.appointments = res.data.data.list || [];
							console.log('预约列表数据：', this.appointments);
							console.log('预约列表长度：', this.appointments.length);
							// 初始化显示所有预约
							this.switchStatus(this.currentStatus);
						} else {
							console.error('获取预约列表失败：', res.data);
							uni.showToast({
								title: res.data.msg || '获取预约列表失败',
								icon: 'none'
							});
						}
					},
					fail: (err) => {
						console.error('获取预约列表失败：', err);
						uni.showToast({
							title: '网络错误，请检查网络连接',
							icon: 'none'
						});
					},
					complete: () => {
						this.loading = false;
					}
				});
			},
			// 切换状态
			switchStatus(statusId) {
				this.currentStatus = statusId;
				
				if (statusId === 0) {
					// 全部预约
					this.filteredAppointments = this.appointments;
				} else {
					// 按状态筛选
					this.filteredAppointments = this.appointments.filter(item => item.status === statusId);
				}
			},
			// 获取状态名称
			getStatusName(statusId) {
				const status = this.statusList.find(item => item.id === statusId);
				return status ? status.name : '';
			},
			// 获取状态图标
			getStatusIcon(statusId) {
				if (statusId === 1) return '⚪️';
				if (statusId === 2) return '✅';
				if (statusId === 3) return '✅';
				return '📋'; // 默认图标
			},
			// 获取状态数量
			getStatusCount(statusId) {
				if (statusId === 0) {
					return this.appointments.length;
				} else {
					return this.appointments.filter(item => item.status === statusId).length;
				}
			},
			// 查看详情
			viewDetail(appointment) {
				uni.navigateTo({
					url: `/pages/appointment-detail/appointment-detail?id=${appointment.id}`
				});
			},
			// 取消预约
			cancelAppointment(appointment) {
				uni.showModal({
					title: '提示',
					content: '确定要取消此次预约吗？',
					success: (res) => {
						if (res.confirm) {
							// 获取token
							const token = uni.getStorageSync('uniIdToken');
							
							// 使用API函数调用
							cancelAppointment(appointment.id).then(res => {
								console.log('取消预约响应：', res);
								if (res && res.code === 200) {
									// 更新本地数据
									const index = this.appointments.findIndex(item => item.id === appointment.id);
									if (index !== -1) {
										this.appointments[index].status = 0; // 0表示已取消
										// 重新筛选
										this.switchStatus(this.currentStatus);
									}
									
									uni.showToast({
										title: '取消预约成功',
										icon: 'success'
									});
								} else {
									uni.showToast({
										title: res.msg || '取消预约失败',
										icon: 'none'
									});
								}
							}).catch(err => {
								console.error('取消预约失败：', err);
								uni.showToast({
									title: '网络错误，请重试',
									icon: 'none'
								});
							});
						}
					}
				});
			},
			// 查看报告
			viewReport(appointment) {
				uni.navigateTo({
					url: `/pages/report-detail/report-detail?appointmentId=${appointment.id}`
				});
			},
			// 立即预约
			makeAppointment() {
				uni.switchTab({
					url: '/pages/appointment/appointment'
				});
			},
			// 格式化日期
			formatDate(dateStr) {
				if (!dateStr) return '';
				const date = new Date(dateStr);
				return date.getFullYear() + '-' + 
					String(date.getMonth() + 1).padStart(2, '0') + '-' + 
					String(date.getDate()).padStart(2, '0');
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
	position: relative;
	overflow: hidden;
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
		background: linear-gradient(45deg, #4facfe, #00f2fe);
		border-radius: 50%;
		opacity: 0.1;
		animation: float 10s infinite ease-in-out;
	}
	
	.shape-1 {
		width: 100px;
		height: 100px;
		top: 10%;
		left: 10%;
	}
	
	.shape-2 {
		width: 150px;
		height: 150px;
		bottom: 20%;
		right: 20%;
	}
	
	.shape-3 {
		width: 200px;
		height: 200px;
		top: 50%;
		left: 30%;
	}
	
	.shape-4 {
		width: 120px;
		height: 120px;
		bottom: 10%;
		right: 50%;
	}
}

.main-content {
	padding: 20rpx;
}

.page-header {
	text-align: center;
	padding: 40rpx 0;
	
	.header-icon {
		font-size: 80rpx;
		color: #1296db;
		margin-bottom: 10rpx;
	}
	
	.header-title {
		font-size: 40rpx;
		font-weight: bold;
		color: #333333;
		margin-bottom: 10rpx;
	}
	
	.header-desc {
		font-size: 24rpx;
		color: #666666;
	}
}

.status-tabs {
	display: flex;
	background-color: #ffffff;
	padding: 0 20rpx;
	margin-bottom: 20rpx;
	box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
	border-radius: 10rpx;
	
	.tab-item {
		flex: 1;
		height: 80rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		position: relative;
		font-size: 28rpx;
		color: #666666;
		
		.tab-icon {
			font-size: 36rpx;
			margin-right: 10rpx;
		}
		
		.tab-text {
			font-weight: bold;
		}
		
		.tab-count {
			position: absolute;
			top: 0;
			right: 0;
			background-color: #1296db;
			color: #ffffff;
			font-size: 20rpx;
			font-weight: bold;
			border-radius: 20rpx;
			padding: 4rpx 10rpx;
			transform: translate(50%, -50%);
		}
		
		&.active {
			color: #1296db;
			font-weight: bold;
			
			&::after {
				content: '';
				position: absolute;
				left: 50%;
				bottom: 0;
				transform: translateX(-50%);
				width: 60rpx;
				height: 4rpx;
				background-color: #1296db;
				border-radius: 2rpx;
			}
		}
	}
}

.appointment-list {
	background-color: #ffffff;
	border-radius: 15rpx;
	box-shadow: 0 5rpx 20rpx rgba(0, 0, 0, 0.1);
	padding: 20rpx;
	
	.appointment-item {
		border-bottom: 1rpx solid #eeeeee;
		padding: 20rpx 0;
		transition: all 0.3s ease;
		
		&:last-child {
			border-bottom: none;
		}
		
		&:hover {
			transform: translateY(-5rpx);
			box-shadow: 0 8rpx 25rpx rgba(0, 0, 0, 0.1);
		}
		
		.appointment-header {
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin-bottom: 15rpx;
			
			.hospital-info {
				display: flex;
				align-items: center;
				
				.hospital-icon {
					font-size: 36rpx;
					color: #1296db;
					margin-right: 10rpx;
				}
				
				.hospital-name {
					font-size: 32rpx;
					font-weight: bold;
					color: #333333;
				}
			}
			
			.status-badge {
				display: flex;
				align-items: center;
				padding: 8rpx 15rpx;
				border-radius: 20rpx;
				font-size: 24rpx;
				font-weight: bold;
				
				.status-icon {
					font-size: 28rpx;
					margin-right: 8rpx;
				}
				
				&.status-0 {
					background-color: #ff9800;
					color: #ffffff;
				}
				
				&.status-1 {
					background-color: #1296db;
					color: #ffffff;
				}
				
				&.status-2 {
					background-color: #42b983;
					color: #ffffff;
				}
				
				&.status-3 {
					background-color: #42b983;
					color: #ffffff;
				}
				
				&.status-4 {
					background-color: #999999;
					color: #ffffff;
				}
			}
		}
		
		.appointment-content {
			margin-bottom: 15rpx;
			
			.info-grid {
				display: grid;
				grid-template-columns: 1fr 1fr;
				gap: 15rpx;
				margin-bottom: 15rpx;
			}
			
			.info-item {
				display: flex;
				align-items: center;
				font-size: 28rpx;
				
				.info-label {
					width: 180rpx;
					color: #666666;
					margin-right: 10rpx;
				}
				
				.info-value {
					flex: 1;
					color: #333333;
					font-weight: bold;
					
					&.price {
						color: #ff6b35;
					}
				}
			}
		}
		
		.appointment-actions {
			display: flex;
			justify-content: flex-end;
			
			.action-btn {
				display: flex;
				align-items: center;
				font-size: 28rpx;
				padding: 0 30rpx;
				height: 60rpx;
				line-height: 60rpx;
				border-radius: 30rpx;
				margin-left: 20rpx;
				transition: all 0.3s ease;
				
				&.cancel-btn {
					background-color: #ff6b35;
					color: #ffffff;
					
					&:hover {
						background-color: #e65c28;
						transform: translateY(-3rpx);
					}
				}
				
				&.report-btn {
					background-color: #42b983;
					color: #ffffff;
					
					&:hover {
						background-color: #36a070;
						transform: translateY(-3rpx);
					}
				}
				
				&.detail-btn {
					background-color: #1296db;
					color: #ffffff;
					
					&:hover {
						background-color: #0d7ac6;
						transform: translateY(-3rpx);
					}
				}
				
				.btn-icon {
					font-size: 28rpx;
					margin-right: 10rpx;
				}
				
				.btn-text {
					font-weight: bold;
				}
				
				&::after {
					border: none;
				}
			}
		}
	}
}

.empty-state {
	text-align: center;
	padding: 100rpx 0;
	
	.empty-icon {
		font-size: 100rpx;
		color: #1296db;
		margin-bottom: 30rpx;
	}
	
	.empty-title {
		font-size: 36rpx;
		font-weight: bold;
		color: #333333;
		margin-bottom: 10rpx;
	}
	
	.empty-desc {
		font-size: 24rpx;
		color: #666666;
		margin-bottom: 40rpx;
	}
	
	.make-appointment-btn {
		display: flex;
		align-items: center;
		justify-content: center;
		background: linear-gradient(45deg, #4facfe, #00f2fe);
		color: #ffffff;
		font-size: 28rpx;
		padding: 0 40rpx;
		height: 80rpx;
		line-height: 80rpx;
		border-radius: 40rpx;
		box-shadow: 0 5rpx 15rpx rgba(0, 0, 0, 0.2);
		
		&:hover {
			transform: translateY(-5rpx);
			box-shadow: 0 8rpx 25rpx rgba(0, 0, 0, 0.3);
		}
		
		.btn-icon {
			font-size: 36rpx;
			margin-right: 10rpx;
		}
		
		.btn-text {
			font-weight: bold;
		}
		
		&::after {
			border: none;
		}
	}
}

@keyframes float {
	0% {
		transform: translateY(0) translateX(0) rotate(0deg);
	}
	50% {
		transform: translateY(-10px) translateX(10px) rotate(5deg);
	}
	100% {
		transform: translateY(0) translateX(0) rotate(0deg);
	}
}
</style> 