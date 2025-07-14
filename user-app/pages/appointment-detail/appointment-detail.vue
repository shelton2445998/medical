<template>
	<view class="content">
		<!-- 页面标题 -->
		<view class="page-header">
			<view class="back-btn" @click="goBack">
				<text class="iconfont icon-back">&#xe65e;</text>
			</view>
			<text class="page-title">预约详情</text>
		</view>
		
		<!-- 加载状态 -->
		<view class="loading-container" v-if="loading">
			<text class="loading-text">加载中...</text>
		</view>
		
		<!-- 预约详情内容 -->
		<view class="appointment-detail" v-else-if="appointmentDetail">
			<!-- 预约状态卡片 -->
			<view class="status-card">
				<view class="status-header">
					<text class="status-title">预约状态</text>
					<text class="status-badge" :class="'status-' + appointmentDetail.status">
						{{getStatusName(appointmentDetail.status)}}
					</text>
				</view>
				<view class="order-info">
					<text class="order-number">订单号：{{appointmentDetail.orderNumber || '暂无'}}</text>
				</view>
			</view>
			
			<!-- 预约信息卡片 -->
			<view class="info-card">
				<view class="card-title">预约信息</view>
				<view class="info-list">
					<view class="info-item">
						<text class="info-label">医院名称：</text>
						<text class="info-value">{{appointmentDetail.hospitalName || '未知医院'}}</text>
					</view>
					<view class="info-item">
						<text class="info-label">套餐名称：</text>
						<text class="info-value">{{appointmentDetail.setmealName || '未知套餐'}}</text>
					</view>
					<view class="info-item">
						<text class="info-label">预约日期：</text>
						<text class="info-value">{{formatDate(appointmentDetail.appointmentDate)}}</text>
					</view>
					<view class="info-item">
						<text class="info-label">预约时间：</text>
						<text class="info-value">{{appointmentDetail.timeSlot}}</text>
					</view>
					<view class="info-item">
						<text class="info-label">套餐价格：</text>
						<text class="info-value price">?{{appointmentDetail.price || 0}}</text>
					</view>
					<view class="info-item" v-if="appointmentDetail.amount">
						<text class="info-label">订单金额：</text>
						<text class="info-value price">?{{appointmentDetail.amount || 0}}</text>
					</view>
					<view class="info-item" v-if="appointmentDetail.payTime">
						<text class="info-label">支付时间：</text>
						<text class="info-value">{{formatDateTime(appointmentDetail.payTime)}}</text>
					</view>
					<view class="info-item" v-if="appointmentDetail.cancelTime">
						<text class="info-label">取消时间：</text>
						<text class="info-value">{{formatDateTime(appointmentDetail.cancelTime)}}</text>
					</view>
				</view>
			</view>
			
			<!-- 套餐详情卡片 -->
			<view class="package-card" v-if="packageInfo">
				<view class="card-title">套餐详情</view>
				<view class="package-info">
					<view class="package-name">{{packageInfo.name}}</view>
					<view class="package-desc">{{packageInfo.description}}</view>
					<view class="package-price">
						<text class="price-label">套餐价格：</text>
						<text class="price-value">?{{packageInfo.price}}</text>
					</view>
				</view>
			</view>
			
			<!-- 医院信息卡片 -->
			<view class="hospital-card" v-if="hospitalInfo">
				<view class="card-title">医院信息</view>
				<view class="hospital-info">
					<view class="hospital-name">{{hospitalInfo.name}}</view>
					<view class="hospital-address">
						<text class="iconfont icon-location">&#xe60e;</text>
						<text class="address-text">{{hospitalInfo.address}}</text>
					</view>
					<view class="hospital-phone" v-if="hospitalInfo.phone">
						<text class="iconfont icon-phone">&#xe60f;</text>
						<text class="phone-text">{{hospitalInfo.phone}}</text>
					</view>
				</view>
			</view>
			
			<!-- 操作按钮 -->
			<view class="action-buttons">
				<button 
					class="action-btn cancel-btn" 
					v-if="appointmentDetail.status === 1" 
					@click="handleCancelAppointment"
					:disabled="cancelling"
				>
					{{cancelling ? '取消中...' : '取消预约'}}
				</button>
				<button 
					class="action-btn primary-btn" 
					@click="contactHospital"
				>
					联系医院
				</button>
			</view>
		</view>
		
		<!-- 错误状态 -->
		<view class="error-container" v-else>
			<text class="error-text">{{errorMessage || '加载失败'}}</text>
			<button class="retry-btn" @click="loadAppointmentDetail">重新加载</button>
		</view>
	</view>
</template>

<script>
	import { getAppointmentDetail, cancelAppointment } from '@/api/appointment'
	
	export default {
		data() {
			return {
				appointmentId: null,
				appointmentDetail: null,
				packageInfo: null,
				hospitalInfo: null,
				loading: false,
				cancelling: false,
				errorMessage: '',
				statusList: [
					{ id: 0, name: '已取消' },
					{ id: 1, name: '待支付' },
					{ id: 2, name: '已支付' },
					{ id: 3, name: '已完成' }
				]
			}
		},
		onLoad(options) {
			if (options.id) {
				this.appointmentId = options.id;
				this.loadAppointmentDetail();
			} else {
				this.errorMessage = '预约ID不能为空';
			}
		},
		methods: {
			// 加载预约详情
			async loadAppointmentDetail() {
				if (!this.appointmentId) {
					this.errorMessage = '预约ID不能为空';
					return;
				}
				
				this.loading = true;
				this.errorMessage = '';
				
				try {
					const token = uni.getStorageSync('uniIdToken');
					if (!token) {
						uni.showToast({
							title: '请先登录',
							icon: 'none'
						});
						return;
					}
					
					const res = await getAppointmentDetail(this.appointmentId);
					
					if (res && res.data) {
						this.appointmentDetail = res.data;
						
						// 加载套餐信息
						if (this.appointmentDetail.setmealId) {
							this.loadPackageInfo(this.appointmentDetail.setmealId);
						}
						
						// 加载医院信息
						if (this.appointmentDetail.hospitalId) {
							this.loadHospitalInfo(this.appointmentDetail.hospitalId);
						}
					} else {
						this.errorMessage = '获取预约详情失败';
					}
				} catch (error) {
					console.error('获取预约详情失败:', error);
					this.errorMessage = '网络错误，请检查网络连接';
				} finally {
					this.loading = false;
				}
			},
			
			// 加载套餐信息
			async loadPackageInfo(setmealId) {
				try {
					// 这里可以调用套餐详情API
					// const res = await getPackageDetail(setmealId);
					// this.packageInfo = res.data;
					
					// 暂时使用模拟数据
					this.packageInfo = {
						name: '标准体检套餐',
						description: '包含血常规、尿常规、肝功能等基础检查项目',
						price: this.appointmentDetail.price || 0
					};
				} catch (error) {
					console.error('加载套餐信息失败:', error);
				}
			},
			
			// 加载医院信息
			async loadHospitalInfo(hospitalId) {
				try {
					// 这里可以调用医院详情API
					// const res = await getHospitalDetail(hospitalId);
					// this.hospitalInfo = res.data;
					
					// 暂时使用模拟数据
					this.hospitalInfo = {
						name: '沈阳市云医院-和平分院',
						address: '沈阳市和平区南京南街61号',
						phone: '024-12345678'
					};
				} catch (error) {
					console.error('加载医院信息失败:', error);
				}
			},
			
			// 获取状态名称
			getStatusName(statusId) {
				const status = this.statusList.find(item => item.id === statusId);
				return status ? status.name : '未知状态';
			},
			
			// 格式化日期
			formatDate(dateStr) {
				if (!dateStr) return '';
				const date = new Date(dateStr);
				return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
			},
			
			// 格式化日期时间
			formatDateTime(dateStr) {
				if (!dateStr) return '';
				const date = new Date(dateStr);
				return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')} ${String(date.getHours()).padStart(2, '0')}:${String(date.getMinutes()).padStart(2, '0')}`;
			},
			
			// 取消预约
			async handleCancelAppointment() {
				uni.showModal({
					title: '确认取消',
					content: '确定要取消此次预约吗？取消后无法恢复。',
					success: async (res) => {
						if (res.confirm) {
							await this.cancelAppointment();
						}
					}
				});
			},
			
			// 执行取消预约
			async cancelAppointment() {
				if (!this.appointmentId) {
					uni.showToast({
						title: '预约ID不能为空',
						icon: 'none'
					});
					return;
				}
				
				this.cancelling = true;
				
				try {
					const token = uni.getStorageSync('uniIdToken');
					if (!token) {
						uni.showToast({
							title: '请先登录',
							icon: 'none'
						});
						return;
					}
					
					const res = await cancelAppointment(this.appointmentId);
					
					if (res && res.code === 200) {
						uni.showToast({
							title: '取消预约成功',
							icon: 'success'
						});
						
						// 重新加载预约详情
						this.loadAppointmentDetail();
					} else {
						uni.showToast({
							title: res.msg || '取消预约失败',
							icon: 'none'
						});
					}
				} catch (error) {
					console.error('取消预约失败:', error);
					uni.showToast({
						title: '网络错误，请检查网络连接',
						icon: 'none'
					});
				} finally {
					this.cancelling = false;
				}
			},
			
			// 联系医院
			contactHospital() {
				if (this.hospitalInfo && this.hospitalInfo.phone) {
					uni.makePhoneCall({
						phoneNumber: this.hospitalInfo.phone
					});
				} else {
					uni.showToast({
						title: '医院电话信息暂不可用',
						icon: 'none'
					});
				}
			},
			
			// 返回上一页
			goBack() {
				uni.navigateBack();
			}
		}
	}
</script>

<style lang="scss">
.content {
	background-color: #f5f5f5;
	min-height: 100vh;
	padding-bottom: 120rpx;
}

.page-header {
	display: flex;
	align-items: center;
	padding: 20rpx 30rpx;
	background-color: #ffffff;
	border-bottom: 1rpx solid #e5e5e5;
	
	.back-btn {
		width: 60rpx;
		height: 60rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		margin-right: 20rpx;
		
		.icon-back {
			font-size: 36rpx;
			color: #333333;
		}
	}
	
	.page-title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333333;
	}
}

.loading-container {
	display: flex;
	align-items: center;
	justify-content: center;
	padding: 100rpx 0;
	
	.loading-text {
		font-size: 28rpx;
		color: #999999;
	}
}

.error-container {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding: 100rpx 0;
	
	.error-text {
		font-size: 28rpx;
		color: #ff5a5f;
		margin-bottom: 30rpx;
	}
	
	.retry-btn {
		background-color: #1296db;
		color: #ffffff;
		border: none;
		padding: 20rpx 40rpx;
		border-radius: 10rpx;
		font-size: 28rpx;
	}
}

.appointment-detail {
	padding: 20rpx;
}

.status-card {
	background-color: #ffffff;
	border-radius: 10rpx;
	padding: 30rpx;
	margin-bottom: 20rpx;
	box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
	
	.status-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 20rpx;
		
		.status-title {
			font-size: 32rpx;
			font-weight: bold;
			color: #333333;
		}
		
		.status-badge {
			padding: 8rpx 16rpx;
			border-radius: 20rpx;
			font-size: 24rpx;
			color: #ffffff;
			
			&.status-0, &.status-4 {
				background-color: #999999;
			}
			
			&.status-1 {
				background-color: #ff9500;
			}
			
			&.status-2 {
				background-color: #1296db;
			}
			
			&.status-3 {
				background-color: #52c41a;
			}
		}
	}
	
	.order-info {
		.order-number {
			font-size: 28rpx;
			color: #666666;
		}
	}
}

.info-card, .package-card, .hospital-card {
	background-color: #ffffff;
	border-radius: 10rpx;
	padding: 30rpx;
	margin-bottom: 20rpx;
	box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
	
	.card-title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333333;
		margin-bottom: 20rpx;
	}
}

.info-list {
	.info-item {
		display: flex;
		align-items: center;
		margin-bottom: 15rpx;
		
		.info-label {
			font-size: 28rpx;
			color: #666666;
			width: 160rpx;
			flex-shrink: 0;
		}
		
		.info-value {
			font-size: 28rpx;
			color: #333333;
			flex: 1;
			
			&.price {
				color: #ff5a5f;
				font-weight: bold;
			}
		}
	}
}

.package-info {
	.package-name {
		font-size: 30rpx;
		font-weight: bold;
		color: #333333;
		margin-bottom: 15rpx;
	}
	
	.package-desc {
		font-size: 28rpx;
		color: #666666;
		margin-bottom: 15rpx;
		line-height: 1.5;
	}
	
	.package-price {
		display: flex;
		align-items: center;
		
		.price-label {
			font-size: 28rpx;
			color: #666666;
		}
		
		.price-value {
			font-size: 30rpx;
			color: #ff5a5f;
			font-weight: bold;
		}
	}
}

.hospital-info {
	.hospital-name {
		font-size: 30rpx;
		font-weight: bold;
		color: #333333;
		margin-bottom: 15rpx;
	}
	
	.hospital-address, .hospital-phone {
		display: flex;
		align-items: center;
		margin-bottom: 10rpx;
		
		.icon-location, .icon-phone {
			font-size: 24rpx;
			color: #1296db;
			margin-right: 10rpx;
		}
		
		.address-text, .phone-text {
			font-size: 28rpx;
			color: #666666;
		}
	}
}

.action-buttons {
	display: flex;
	gap: 20rpx;
	padding: 30rpx 20rpx;
	
	.action-btn {
		flex: 1;
		height: 80rpx;
		border-radius: 10rpx;
		font-size: 28rpx;
		border: none;
		
		&.cancel-btn {
			background-color: #ffffff;
			color: #ff5a5f;
			border: 2rpx solid #ff5a5f;
			
			&:disabled {
				background-color: #f5f5f5;
				color: #999999;
				border-color: #e5e5e5;
			}
		}
		
		&.primary-btn {
			background-color: #1296db;
			color: #ffffff;
		}
	}
}
</style> 