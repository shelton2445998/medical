<!--
  我的预约页面组件
  
  展示用户的所有体检预约记录，支持按状态筛选、查看详情、取消预约等功能
  提供预约状态管理、订单操作、报告查看等完整的预约生命周期管理
  包含预约列表展示、状态筛选、操作按钮、空状态提示等功能模块
  
  @author 用户端项目组
  @date 2024
  @version 1.0.0
-->
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
								<text class="info-value">{{item.id || 'N/A'}}</text>
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
					<!-- 待支付状态显示取消预约按钮 -->
					<button 
						class="action-btn cancel-btn" 
						v-if="item.status === 1" 
						@click.stop="cancelAppointment(item)"
					>
						<text class="btn-icon">❌</text>
						<text class="btn-text">取消预约</text>
					</button>
					
					<!-- 已支付状态显示等待报告按钮 -->
					<button 
						class="action-btn wait-btn" 
						v-if="item.status === 2" 
						@click.stop="viewReport(item)"
					>
						<text class="btn-icon">⏳</text>
						<text class="btn-text">等待报告</text>
					</button>
					
					<!-- 已完成状态显示查看报告按钮 -->
					<button 
						class="action-btn report-btn" 
						v-if="item.status === 3" 
						@click.stop="viewReport(item)"
					>
						<text class="btn-icon">📊</text>
						<text class="btn-text">查看报告</text>
					</button>
					
					<!-- 已取消状态显示删除订单按钮 -->
					<button 
						class="action-btn delete-btn" 
						v-if="item.status === 0" 
						@click.stop="deleteAppointment(item)"
					>
						<text class="btn-icon">🗑️</text>
						<text class="btn-text">删除订单</text>
					</button>
					
					<!-- 查看详情按钮（所有状态都显示） -->
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
/**
 * 我的预约页面逻辑
 * 
 * 处理预约列表获取、状态筛选、预约操作、订单管理等功能
 * 集成预约数据管理和用户交互处理
 */

import { appointmentApi } from '@/utils/api.js';
import { cancelAppointment } from '@/api/appointment.js';
import { reportApi } from '@/utils/api.js'; // Added import for reportApi

// API 基础URL
const API_BASE_URL = 'http://localhost:8888/api';

export default {
	data() {
		return {
			currentStatus: 0, // 0表示全部
			statusList: [
				{ id: 0, name: '全部', icon: '📋' },
				{ id: 1, name: '待支付', icon: '⚪️' },
				{ id: 2, name: '已支付', icon: '✅' },
				{ id: 3, name: '已完成', icon: '✅' },
				{ id: -1, name: '已取消', icon: '❌' }
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
				} else if (statusId === -1) {
					// 已取消状态（status为0）
					this.filteredAppointments = this.appointments.filter(item => item.status === 0);
				} else {
					// 按状态筛选
					this.filteredAppointments = this.appointments.filter(item => item.status === statusId);
				}
			},
			// 获取状态名称
			getStatusName(statusId) {
				// 根据新的状态定义：0:已取消，1:待支付，2:已支付，3:已完成
				const statusMap = {
					0: '已取消',
					1: '待支付', 
					2: '已支付',
					3: '已完成'
				};
				return statusMap[statusId] || '未知状态';
			},
			// 获取状态图标
			getStatusIcon(statusId) {
				// 根据新的状态定义：0:已取消，1:待支付，2:已支付，3:已完成
				const iconMap = {
					0: '❌', // 已取消
					1: '⚪️', // 待支付
					2: '✅', // 已支付
					3: '✅'  // 已完成
				};
				return iconMap[statusId] || '📋'; // 默认图标
			},
			// 获取状态数量
			getStatusCount(statusId) {
				if (statusId === 0) {
					return this.appointments.length;
				} else if (statusId === -1) {
					// 已取消状态（status为0）
					return this.appointments.filter(item => item.status === 0).length;
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
			
			// 删除预约
			deleteAppointment(appointment) {
				uni.showModal({
					title: '确认删除',
					content: '确定要删除此订单吗？删除后订单和对应的报告都将被删除，无法恢复。',
					success: (res) => {
						if (res.confirm) {
							// 获取token
							const token = uni.getStorageSync('uniIdToken');
							
							// 先查找对应的报告
							uni.request({
								url: `${API_BASE_URL}/app/report/getAppReportPage`,
								method: 'POST',
								header: {
									'Authorization': token || '',
									'Content-Type': 'application/json'
								},
								data: {
									orderId: appointment.id,
									pageIndex: 1,
									pageSize: 1
								},
								success: (reportRes) => {
									console.log('获取报告列表响应：', reportRes);
									
									// 删除订单
									uni.request({
										url: appointmentApi.deleteAppointment(appointment.id),
										method: 'DELETE',
										header: {
											'Authorization': token || '',
											'Content-Type': 'application/json'
										},
										success: (orderRes) => {
											console.log('删除预约响应：', orderRes);
											if (orderRes.statusCode === 200 && orderRes.data.code === 200) {
												// 如果找到对应的报告，也删除报告
												if (reportRes.statusCode === 200 && reportRes.data.code === 200) {
													const reportList = reportRes.data.data.list;
													if (reportList && reportList.length > 0) {
														const report = reportList[0];
														
														// 删除报告
														uni.request({
															url: reportApi.deleteReport(report.id),
															method: 'DELETE',
															header: {
																'Authorization': token || '',
																'Content-Type': 'application/json'
															},
															success: (deleteReportRes) => {
																console.log('删除报告响应：', deleteReportRes);
																if (deleteReportRes.statusCode === 200 && deleteReportRes.data.code === 200) {
																	console.log('订单和报告删除成功');
																} else {
																	console.warn('报告删除失败：', deleteReportRes.data.msg);
																}
															},
															fail: (err) => {
																console.error('删除报告失败：', err);
															}
														});
													}
												}
												
												// 从本地列表中移除
												const index = this.appointments.findIndex(item => item.id === appointment.id);
												if (index !== -1) {
													this.appointments.splice(index, 1);
													// 重新筛选
													this.switchStatus(this.currentStatus);
												}
												
												uni.showToast({
													title: '删除成功',
													icon: 'success'
												});
											} else {
												uni.showToast({
													title: orderRes.data.msg || '删除失败',
													icon: 'none'
												});
											}
										},
										fail: (err) => {
											console.error('删除预约失败：', err);
											uni.showToast({
												title: '网络错误，请重试',
												icon: 'none'
											});
										}
									});
								},
								fail: (err) => {
									console.error('获取报告信息失败：', err);
									// 即使获取报告失败，也继续删除订单
									uni.request({
										url: appointmentApi.deleteAppointment(appointment.id),
										method: 'DELETE',
										header: {
											'Authorization': token || '',
											'Content-Type': 'application/json'
										},
										success: (orderRes) => {
											console.log('删除预约响应：', orderRes);
											if (orderRes.statusCode === 200 && orderRes.data.code === 200) {
												// 从本地列表中移除
												const index = this.appointments.findIndex(item => item.id === appointment.id);
												if (index !== -1) {
													this.appointments.splice(index, 1);
													// 重新筛选
													this.switchStatus(this.currentStatus);
												}
												
												uni.showToast({
													title: '删除成功',
													icon: 'success'
												});
											} else {
												uni.showToast({
													title: orderRes.data.msg || '删除失败',
													icon: 'none'
												});
											}
										},
										fail: (err) => {
											console.error('删除预约失败：', err);
											uni.showToast({
												title: '网络错误，请重试',
												icon: 'none'
											});
										}
									});
								}
							});
						}
					}
				});
			},
			// 查看报告
			viewReport(appointment) {
				// 获取用户信息
				const token = uni.getStorageSync('uniIdToken');
				
				if (!token) {
					uni.showToast({
						title: '请先登录',
						icon: 'none'
					});
					return;
				}
				
				// 先获取预约详情，然后根据订单ID获取报告ID
				uni.request({
					url: appointmentApi.getAppointmentDetail(appointment.id),
					method: 'GET',
					header: {
						'Authorization': token,
						'Content-Type': 'application/json'
					},
					success: (appointmentRes) => {
						console.log('获取预约详情响应：', appointmentRes);
						if (appointmentRes.statusCode === 200 && appointmentRes.data.code === 200) {
							const appointmentDetail = appointmentRes.data.data;
							
							// 根据订单ID获取报告ID
							uni.request({
								url: `${API_BASE_URL}/app/report/getAppReportPage`,
								method: 'POST',
								header: {
									'Authorization': token,
									'Content-Type': 'application/json'
								},
								data: {
									orderId: appointment.id,
									pageIndex: 1,
									pageSize: 1
								},
								success: (reportRes) => {
									console.log('获取报告列表响应：', reportRes);
									if (reportRes.statusCode === 200 && reportRes.data.code === 200) {
										const reportList = reportRes.data.data.list;
										if (reportList && reportList.length > 0) {
											const report = reportList[0];
											
											// 构建跳转参数，使用报告ID和预约详情中的年龄信息
											const params = {
												id: report.id, // 使用报告ID而不是订单ID
												personName: encodeURIComponent(appointmentDetail.patientName || ''),
												hospitalName: encodeURIComponent(appointmentDetail.hospitalName || ''),
												examDate: encodeURIComponent(appointmentDetail.appointmentDate || ''),
												patientGender: encodeURIComponent(appointmentDetail.patientGender || ''),
												patientAge: encodeURIComponent(appointmentDetail.patientAge || '25')
											};
											
											// 构建URL参数
											const urlParams = Object.keys(params)
												.map(key => `${key}=${params[key]}`)
												.join('&');
											
											uni.navigateTo({
												url: `/pages/report-detail/report-detail?${urlParams}`
											});
										} else {
											uni.showToast({
												title: '报告尚未生成',
												icon: 'none'
											});
										}
									} else {
										uni.showToast({
											title: '获取报告信息失败',
											icon: 'none'
										});
									}
								},
								fail: (err) => {
									console.error('获取报告信息失败：', err);
									uni.showToast({
										title: '网络错误，请重试',
										icon: 'none'
									});
								}
							});
						} else {
							uni.showToast({
								title: '获取预约详情失败',
								icon: 'none'
							});
						}
					},
					fail: (err) => {
						console.error('获取预约详情失败：', err);
						uni.showToast({
							title: '网络错误，请重试',
							icon: 'none'
						});
					}
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

<style lang="scss" scoped>
.content {
  background: linear-gradient(135deg, #0984e3 0%, #74b9ff 50%, #0984e3 100%);
	min-height: 100vh;
  padding-top: 0;
  padding-bottom: 40rpx;
  position: relative;
  overflow: hidden;
  
  &::before {
    content: '';
    position: absolute;
    top: -50%;
    left: -50%;
    width: 200%;
    height: 200%;
    background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 0%, transparent 70%);
    animation: flow 20s linear infinite;
    pointer-events: none;
  }
  
  &::after {
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
  pointer-events: none;
  z-index: -1;

  .shape {
    position: absolute;
    background: rgba(255, 255, 255, 0.1);
    border-radius: 50%;
    filter: blur(50px);
    animation: float 15s infinite ease-in-out;
    transition: all 0.3s ease;
    
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

    &.shape-1 {
      width: 100px;
      height: 100px;
      top: 10%;
      left: 10%;
      animation-delay: -2s;
    }
    &.shape-2 {
      width: 150px;
      height: 150px;
      top: 70%;
      left: 30%;
      animation-delay: -5s;
    }
    &.shape-3 {
      width: 120px;
      height: 120px;
      top: 20%;
      right: 20%;
      animation-delay: -8s;
    }
    &.shape-4 {
      width: 180px;
      height: 180px;
      bottom: 10%;
      right: 50%;
      animation-delay: -10s;
    }
  }
}

.main-content {
  padding: 20rpx 40rpx 0 40rpx;
  position: relative;
  z-index: 1;
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

.status-tabs {
	display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 40rpx;
  background: rgba(255, 255, 255, 0.95);
  border-radius: 50rpx;
  padding: 15rpx 30rpx;
  box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10rpx);
  animation: slideInDown 0.8s ease-out;
  flex-wrap: wrap;
  gap: 10rpx;
	
	.tab-item {
		display: flex;
		align-items: center;
    margin: 0 5rpx;
    padding: 12rpx 20rpx;
    border-radius: 25rpx;
    transition: all 0.3s ease;
    position: relative;
    cursor: pointer;
    white-space: nowrap;
    min-width: fit-content;
    
    .tab-icon {
      font-size: 22rpx;
      margin-right: 8rpx;
      transition: all 0.3s ease;
    }
    
    .tab-text {
      font-size: 24rpx;
		color: #666666;
      font-weight: 500;
      transition: all 0.3s ease;
    }
    
    .tab-count {
      background: linear-gradient(135deg, #ff7675, #fd79a8);
      color: #ffffff;
      font-size: 18rpx;
      padding: 3rpx 6rpx;
      border-radius: 8rpx;
      margin-left: 6rpx;
      font-weight: bold;
      transition: all 0.3s ease;
    }
    
    &:hover {
      background: rgba(9, 132, 227, 0.1);
      transform: translateY(-2rpx);
    }
		
		&.active {
      background: linear-gradient(135deg, #0984e3, #74b9ff);
      box-shadow: 0 6rpx 20rpx rgba(9, 132, 227, 0.3);
      
      .tab-icon, .tab-text {
        color: #ffffff;
      }
      
      .tab-count {
        background: rgba(255, 255, 255, 0.2);
			}
		}
	}
}

.appointment-list {
  animation: fadeInUp 0.8s ease-out;
	
	.appointment-item {
    background: rgba(255, 255, 255, 0.95);
    border-radius: 24rpx;
		padding: 30rpx;
    margin-bottom: 30rpx;
    box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
    backdrop-filter: blur(10rpx);
    transition: all 0.3s ease;
    cursor: pointer;
    
    &:hover {
      transform: translateY(-4rpx);
      box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.15);
    }
		
		.appointment-header {
			display: flex;
			justify-content: space-between;
			align-items: center;
      margin-bottom: 25rpx;
      
      .hospital-info {
        display: flex;
        align-items: center;
        
        .hospital-icon {
          font-size: 32rpx;
          margin-right: 15rpx;
          color: #0984e3;
        }
			
			.hospital-name {
				font-size: 32rpx;
				font-weight: bold;
				color: #333333;
			}
      }
      
      .status-badge {
        padding: 8rpx 16rpx;
        border-radius: 20rpx;
        display: flex;
        align-items: center;
				font-size: 24rpx;
        font-weight: bold;
        transition: all 0.3s ease;
				
        .status-icon {
          margin-right: 8rpx;
				}
				
				&.status-1 {
          background: linear-gradient(135deg, #ffa726, #ff9800);
          color: #ffffff;
				}
				
				&.status-2 {
          background: linear-gradient(135deg, #66bb6a, #4caf50);
          color: #ffffff;
				}
				
				&.status-3 {
          background: linear-gradient(135deg, #42a5f5, #2196f3);
          color: #ffffff;
				}
				
				&.status-0 {
          background: linear-gradient(135deg, #95a5a6, #7f8c8d);
          color: #ffffff;
				}
			}
		}
		
    .appointment-content {
      .info-grid {
        display: grid;
        grid-template-columns: repeat(2, 1fr);
        gap: 20rpx;
        margin-bottom: 25rpx;
        
        .info-item {
				.info-label {
            font-size: 24rpx;
					color: #666666;
            margin-bottom: 8rpx;
            display: block;
				}
				
				.info-value {
            font-size: 26rpx;
					color: #333333;
            font-weight: 500;
					
					&.price {
              color: #ff5a5f;
						font-weight: bold;
					}
				}
			}
		}
		
		.appointment-actions {
			display: flex;
        gap: 15rpx;
			
			.action-btn {
          flex: 1;
          height: 70rpx;
          border-radius: 35rpx;
				font-size: 24rpx;
          font-weight: bold;
          display: flex;
          align-items: center;
          justify-content: center;
          transition: all 0.3s ease;
          border: none;
          
          .btn-icon {
            font-size: 20rpx;
            margin-right: 8rpx;
          }
          
          &.cancel-btn {
            background: linear-gradient(135deg, #ff7675, #fd79a8);
            color: #ffffff;
            
            &:hover {
              transform: translateY(-2rpx);
              box-shadow: 0 6rpx 20rpx rgba(255, 118, 117, 0.3);
            }
          }
          
          &.report-btn {
            background: linear-gradient(135deg, #00b894, #00cec9);
					color: #ffffff;
            
            &:hover {
              transform: translateY(-2rpx);
              box-shadow: 0 6rpx 20rpx rgba(0, 184, 148, 0.3);
            }
				}
				
          &.wait-btn {
            background: linear-gradient(135deg, #fdcb6e, #e17055);
            color: #ffffff;
            
            &:hover {
              transform: translateY(-2rpx);
              box-shadow: 0 6rpx 20rpx rgba(253, 203, 110, 0.3);
            }
          }
          
          &.detail-btn {
            background: linear-gradient(135deg, #0984e3, #74b9ff);
            color: #ffffff;
            
            &:hover {
              transform: translateY(-2rpx);
              box-shadow: 0 6rpx 20rpx rgba(9, 132, 227, 0.3);
            }
          }
          
          &.delete-btn {
            background: linear-gradient(135deg, #e74c3c, #c0392b);
            color: #ffffff;
            
            &:hover {
              transform: translateY(-2rpx);
              box-shadow: 0 6rpx 20rpx rgba(231, 76, 60, 0.3);
            }
          }
				}
			}
		}
	}
}

.empty-state {
  text-align: center;
  padding: 80rpx 40rpx;
  animation: fadeInUp 0.8s ease-out;
	
  .empty-icon {
    font-size: 120rpx;
		margin-bottom: 30rpx;
    animation: bounce 2s infinite;
    transition: all 0.3s ease;
	}
	
  .empty-title {
    font-size: 36rpx;
    font-weight: bold;
    color: #333333;
    margin-bottom: 20rpx;
  }
  
  .empty-desc {
		font-size: 28rpx;
    color: #666666;
		margin-bottom: 40rpx;
    line-height: 1.5;
	}
	
	.make-appointment-btn {
    background: linear-gradient(135deg, #0984e3, #74b9ff);
		color: #ffffff;
    border: none;
    border-radius: 50rpx;
    padding: 20rpx 40rpx;
		font-size: 28rpx;
    font-weight: bold;
    display: flex;
    align-items: center;
    justify-content: center;
    margin: 0 auto;
    transition: all 0.3s ease;
    box-shadow: 0 8rpx 24rpx rgba(9, 132, 227, 0.3);
    
    &:hover {
      transform: translateY(-4rpx);
      box-shadow: 0 12rpx 32rpx rgba(9, 132, 227, 0.4);
    }
    
    .btn-icon {
      font-size: 24rpx;
      margin-right: 10rpx;
    }
  }
}

// 动画定义
@keyframes float {
  0% {
    transform: translateY(0) translateX(0) scale(1);
    opacity: 0.8;
  }
  25% {
    transform: translateY(-20px) translateX(20px) scale(1.1);
    opacity: 0.9;
  }
  50% {
    transform: translateY(0) translateX(0) scale(1);
    opacity: 0.8;
  }
  75% {
    transform: translateY(20px) translateX(-20px) scale(1.1);
    opacity: 0.9;
  }
  100% {
    transform: translateY(0) translateX(0) scale(1);
    opacity: 0.8;
  }
}

@keyframes flow {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
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

@keyframes slideInDown {
  from {
    opacity: 0;
    transform: translateY(-30rpx);
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
    transform: translateY(-10rpx);
  }
  60% {
    transform: translateY(-5rpx);
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
</style> 