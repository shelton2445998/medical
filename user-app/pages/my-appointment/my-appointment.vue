<template>
	<view class="content">
		<!-- 页面标题 -->
		<view class="page-header">
<!-- 			<view class="back-btn" @click="goBack">
				<text class="iconfont icon-back">&#xe65e;</text>
			</view> -->
			<text class="page-title">我的预约</text>
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
				<text>{{item.name}}</text>
			</view>
		</view>
		
		<!-- 预约列表 -->
		<view class="appointment-list" v-if="filteredAppointments.length > 0">
			<view class="appointment-item" v-for="(item, index) in filteredAppointments" :key="index" @click="viewDetail(item)">
				<view class="appointment-header">
					<text class="hospital-name">{{item.hospitalName}}</text>
					<text class="appointment-status" :class="'status-' + item.status">{{getStatusName(item.status)}}</text>
				</view>
				<view class="appointment-info">
					<view class="info-row">
						<text class="info-label">体检套餐：</text>
						<text class="info-value">{{item.packageName}}</text>
					</view>
					<view class="info-row">
						<text class="info-label">预约时间：</text>
						<text class="info-value">{{item.appointmentDate}} {{item.appointmentTime}}</text>
					</view>
					<view class="info-row">
						<text class="info-label">预约人：</text>
						<text class="info-value">{{item.personName}}</text>
					</view>
					<view class="info-row">
						<text class="info-label">预约号：</text>
						<text class="info-value">{{item.appointmentNo}}</text>
					</view>
				</view>
				<view class="appointment-actions">
					<button 
						class="action-btn" 
						v-if="item.status === 1" 
						@click.stop="cancelAppointment(item)"
					>取消预约</button>
					<button 
						class="action-btn" 
						v-if="item.status === 2" 
						@click.stop="viewReport(item)"
					>查看报告</button>
					<button 
						class="action-btn primary" 
						v-if="item.status === 1" 
						@click.stop="viewDetail(item)"
					>查看详情</button>
				</view>
			</view>
		</view>
		
		<!-- 空状态 -->
		<view class="empty-state" v-else>
			<image class="empty-image" src="/static/images/empty.png" mode="aspectFit"></image>
			<text class="empty-text">暂无预约记录</text>
			<button class="make-appointment-btn" @click="makeAppointment">立即预约</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				currentStatus: 0, // 0表示全部
				statusList: [
					{ id: 0, name: '全部' },
					{ id: 1, name: '待体检' },
					{ id: 2, name: '已完成' },
					{ id: 3, name: '已取消' }
				],
				appointments: [
					{
						id: 1,
						hospitalName: '沈阳市云医院-和平分院',
						packageName: '基础体检套餐',
						appointmentDate: '2023-07-15',
						appointmentTime: '上午 9:00-10:00',
						personName: '张三',
						appointmentNo: 'A20230715001',
						status: 1 // 1-待体检，2-已完成，3-已取消
					},
					{
						id: 2,
						hospitalName: '沈阳市云医院-沈河分院',
						packageName: '高级体检套餐',
						appointmentDate: '2023-06-20',
						appointmentTime: '上午 10:00-11:00',
						personName: '张三',
						appointmentNo: 'A20230620002',
						status: 2
					},
					{
						id: 3,
						hospitalName: '沈阳市云医院-和平分院',
						packageName: '男性健康套餐',
						appointmentDate: '2023-05-10',
						appointmentTime: '下午 14:00-15:00',
						personName: '张三',
						appointmentNo: 'A20230510003',
						status: 3
					}
				],
				filteredAppointments: []
			}
		},
		onLoad() {
			// 初始化显示所有预约
			this.filteredAppointments = this.appointments;
			
			// 获取预约列表数据
			this.getAppointmentList();
		},
		methods: {
			// 获取预约列表
			getAppointmentList() {
				// 这里可以替换为实际的API调用
				console.log('获取预约列表');
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
							// 这里可以替换为实际的API调用
							console.log('取消预约', appointment.id);
							
							// 模拟取消成功
							setTimeout(() => {
								// 更新状态
								const index = this.appointments.findIndex(item => item.id === appointment.id);
								if (index !== -1) {
									this.appointments[index].status = 3;
									// 重新筛选
									this.switchStatus(this.currentStatus);
								}
								
								uni.showToast({
									title: '取消预约成功',
									icon: 'success'
								});
							}, 1000);
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
}

.page-header {
	position: relative;
	background-color: #1296db;
	padding: 20rpx 0;
	text-align: center;
	
	.back-btn {
		position: absolute;
		left: 30rpx;
		top: 50%;
		transform: translateY(-50%);
		width: 60rpx;
		height: 60rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		
		.iconfont {
			color: #ffffff;
			font-size: 40rpx;
			font-family: texticons;
		}
	}
	
	.page-title {
		color: #ffffff;
		font-size: 36rpx;
		font-weight: bold;
	}
}

.status-tabs {
	display: flex;
	background-color: #ffffff;
	padding: 0 20rpx;
	margin-bottom: 20rpx;
	box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
	
	.tab-item {
		flex: 1;
		height: 80rpx;
		display: flex;
		justify-content: center;
		align-items: center;
		font-size: 28rpx;
		color: #666666;
		position: relative;
		
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
	padding: 0 20rpx;
	
	.appointment-item {
		background-color: #ffffff;
		border-radius: 10rpx;
		margin-bottom: 20rpx;
		padding: 30rpx;
		box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
		
		.appointment-header {
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin-bottom: 20rpx;
			
			.hospital-name {
				font-size: 32rpx;
				font-weight: bold;
				color: #333333;
			}
			
			.appointment-status {
				font-size: 24rpx;
				padding: 4rpx 16rpx;
				border-radius: 20rpx;
				
				&.status-1 {
					color: #1296db;
					background-color: rgba(18, 150, 219, 0.1);
				}
				
				&.status-2 {
					color: #42b983;
					background-color: rgba(66, 185, 131, 0.1);
				}
				
				&.status-3 {
					color: #999999;
					background-color: rgba(153, 153, 153, 0.1);
				}
			}
		}
		
		.appointment-info {
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
		
		.appointment-actions {
			display: flex;
			justify-content: flex-end;
			
			.action-btn {
				font-size: 24rpx;
				padding: 0 30rpx;
				height: 60rpx;
				line-height: 60rpx;
				border-radius: 30rpx;
				margin-left: 20rpx;
				background-color: #f5f5f5;
				color: #666666;
				
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