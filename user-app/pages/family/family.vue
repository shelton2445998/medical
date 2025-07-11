<template>
	<view class="content">
		<!-- 页面标题 -->
		<view class="page-header">
			<text class="page-title">家庭成员</text>
		</view>
		
		<!-- 家庭成员列表 -->
		<view class="family-list" v-if="familyMembers.length > 0">
			<view class="member-item" v-for="(member, index) in familyMembers" :key="index" @click="editMember(member)">
				<view class="member-info">
					<view class="member-avatar">
						<image :src="member.avatar" mode="aspectFill"></image>
						<view class="member-relation">{{member.relation}}</view>
					</view>
					<view class="member-details">
						<text class="member-name">{{member.name}}</text>
						<text class="member-age">{{member.age}}岁 | {{member.gender}}</text>
						<text class="member-phone">{{member.phone}}</text>
					</view>
				</view>
				<view class="member-actions">
					<button class="action-btn" @click.stop="viewHealth(member)">
						<image src="/static/images/icons/icon-health.png" mode="aspectFit" class="btn-icon"></image>
						<text>健康档案</text>
					</button>
					<button class="action-btn" @click.stop="makeAppointment(member)">
						<image src="/static/images/icons/icon-appointment.png" mode="aspectFit" class="btn-icon"></image>
						<text>预约体检</text>
					</button>
				</view>
			</view>
		</view>
		
		<!-- 空状态 -->
		<view class="empty-state" v-else>
			<image class="empty-image" src="/static/images/empty-family.png" mode="aspectFit"></image>
			<text class="empty-text">暂无家庭成员</text>
			<text class="empty-desc">添加家庭成员，为他们管理健康档案</text>
		</view>
		
		<!-- 添加成员按钮 -->
		<view class="add-member-section">
			<button class="add-btn" @click="addMember">
				<image src="/static/images/icons/icon-add.png" mode="aspectFit" class="add-icon"></image>
				<text>添加家庭成员</text>
			</button>
		</view>
		
		<!-- 健康提醒 -->
		<view class="reminder-section" v-if="healthReminders.length > 0">
			<view class="section-title">健康提醒</view>
			<view class="reminder-list">
				<view class="reminder-item" v-for="(reminder, index) in healthReminders" :key="index">
					<view class="reminder-icon">
						<image :src="reminder.icon" mode="aspectFit"></image>
					</view>
					<view class="reminder-content">
						<text class="reminder-title">{{reminder.title}}</text>
						<text class="reminder-desc">{{reminder.description}}</text>
						<text class="reminder-time">{{reminder.time}}</text>
					</view>
					<view class="reminder-status" :class="reminder.status">
						<text>{{getStatusText(reminder.status)}}</text>
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
				familyMembers: [
					{
						id: 1,
						name: '张小明',
						age: 35,
						gender: '男',
						relation: '父亲',
						phone: '138****1234',
						avatar: '/static/images/avatar-father.jpg',
						healthStatus: 'normal'
					},
					{
						id: 2,
						name: '李小红',
						age: 32,
						gender: '女',
						relation: '母亲',
						phone: '139****5678',
						avatar: '/static/images/avatar-mother.jpg',
						healthStatus: 'warning'
					},
					{
						id: 3,
						name: '张小华',
						age: 8,
						gender: '男',
						relation: '儿子',
						phone: '无',
						avatar: '/static/images/avatar-son.jpg',
						healthStatus: 'excellent'
					}
				],
				healthReminders: [
					{
						id: 1,
						title: '父亲体检提醒',
						description: '建议父亲进行年度体检',
						time: '2023-08-15',
						icon: '/static/images/icons/icon-reminder.png',
						status: 'pending'
					},
					{
						id: 2,
						title: '母亲血压监测',
						description: '母亲血压偏高，需要定期监测',
						time: '2023-07-20',
						icon: '/static/images/icons/icon-blood-pressure.png',
						status: 'completed'
					},
					{
						id: 3,
						title: '儿子疫苗接种',
						description: '儿子需要接种流感疫苗',
						time: '2023-09-01',
						icon: '/static/images/icons/icon-vaccine.png',
						status: 'pending'
					}
				]
			}
		},
		onLoad() {
			// 获取家庭成员数据
			this.getFamilyMembers();
		},
		methods: {
			// 获取家庭成员数据
			getFamilyMembers() {
				// 这里可以替换为实际的API调用
				console.log('获取家庭成员数据');
			},
			// 添加成员
			addMember() {
				uni.navigateTo({
					url: '/pages/add-family-member/add-family-member'
				});
			},
			// 编辑成员
			editMember(member) {
				uni.navigateTo({
					url: `/pages/edit-family-member/edit-family-member?id=${member.id}`
				});
			},
			// 查看健康档案
			viewHealth(member) {
				uni.navigateTo({
					url: `/pages/member-health/member-health?memberId=${member.id}&memberName=${member.name}`
				});
			},
			// 预约体检
			makeAppointment(member) {
				uni.navigateTo({
					url: `/pages/hospital/hospital?memberId=${member.id}&memberName=${member.name}`
				});
			},
			// 获取状态文本
			getStatusText(status) {
				const statusMap = {
					'pending': '待处理',
					'completed': '已完成',
					'overdue': '已逾期'
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

.family-list {
	background-color: #ffffff;
	border-radius: 20rpx;
	padding: 30rpx;
	margin-bottom: 30rpx;
	
	.member-item {
		background-color: #f8f9fa;
		border-radius: 15rpx;
		padding: 25rpx;
		margin-bottom: 20rpx;
		
		.member-info {
			display: flex;
			align-items: center;
			margin-bottom: 20rpx;
			
			.member-avatar {
				position: relative;
				width: 100rpx;
				height: 100rpx;
				margin-right: 20rpx;
				
				image {
					width: 100%;
					height: 100%;
					border-radius: 50%;
				}
				
				.member-relation {
					position: absolute;
					bottom: -5rpx;
					left: 50%;
					transform: translateX(-50%);
					background-color: #1296db;
					color: #ffffff;
					font-size: 20rpx;
					padding: 4rpx 12rpx;
					border-radius: 10rpx;
					white-space: nowrap;
				}
			}
			
			.member-details {
				flex: 1;
				
				.member-name {
					font-size: 30rpx;
					font-weight: bold;
					color: #333333;
					margin-bottom: 8rpx;
					display: block;
				}
				
				.member-age {
					font-size: 24rpx;
					color: #666666;
					margin-bottom: 8rpx;
					display: block;
				}
				
				.member-phone {
					font-size: 22rpx;
					color: #999999;
					display: block;
				}
			}
		}
		
		.member-actions {
			display: flex;
			justify-content: space-between;
			
			.action-btn {
				flex: 1;
				height: 70rpx;
				border-radius: 35rpx;
				font-size: 24rpx;
				margin: 0 10rpx;
				display: flex;
				align-items: center;
				justify-content: center;
				
				&::after {
					border: none;
				}
				
				&:first-child {
					background-color: #1296db;
					color: #ffffff;
				}
				
				&:last-child {
					background-color: #52c41a;
					color: #ffffff;
				}
				
				.btn-icon {
					width: 30rpx;
					height: 30rpx;
					margin-right: 8rpx;
				}
			}
		}
	}
}

.empty-state {
	background-color: #ffffff;
	border-radius: 20rpx;
	padding: 60rpx 30rpx;
	text-align: center;
	margin-bottom: 30rpx;
	
	.empty-image {
		width: 200rpx;
		height: 200rpx;
		margin-bottom: 30rpx;
	}
	
	.empty-text {
		font-size: 28rpx;
		color: #333333;
		margin-bottom: 15rpx;
		display: block;
	}
	
	.empty-desc {
		font-size: 24rpx;
		color: #999999;
		display: block;
	}
}

.add-member-section {
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

.reminder-section {
	background-color: #ffffff;
	border-radius: 20rpx;
	padding: 30rpx;
	
	.section-title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333333;
		margin-bottom: 30rpx;
	}
	
	.reminder-list {
		.reminder-item {
			display: flex;
			align-items: center;
			padding: 20rpx 0;
			border-bottom: 1px solid #f5f5f5;
			
			&:last-child {
				border-bottom: none;
			}
			
			.reminder-icon {
				width: 60rpx;
				height: 60rpx;
				margin-right: 20rpx;
				
				image {
					width: 100%;
					height: 100%;
				}
			}
			
			.reminder-content {
				flex: 1;
				
				.reminder-title {
					font-size: 28rpx;
					font-weight: bold;
					color: #333333;
					margin-bottom: 8rpx;
					display: block;
				}
				
				.reminder-desc {
					font-size: 24rpx;
					color: #666666;
					margin-bottom: 8rpx;
					display: block;
				}
				
				.reminder-time {
					font-size: 22rpx;
					color: #999999;
					display: block;
				}
			}
			
			.reminder-status {
				padding: 8rpx 16rpx;
				border-radius: 15rpx;
				font-size: 22rpx;
				
				&.pending {
					background-color: #faad14;
					color: #ffffff;
				}
				
				&.completed {
					background-color: #52c41a;
					color: #ffffff;
				}
				
				&.overdue {
					background-color: #ff4d4f;
					color: #ffffff;
				}
			}
		}
	}
}
</style> 