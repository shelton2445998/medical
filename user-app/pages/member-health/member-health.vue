<template>
	<view class="content">
		<!-- 页面标题 -->
		<view class="page-header">
			<text class="page-title">{{memberName}}的健康档案</text>
		</view>
		
		<!-- 成员基本信息 -->
		<view class="member-info-section">
			<view class="member-avatar">
				<image :src="memberInfo.avatar" mode="aspectFill"></image>
			</view>
			<view class="member-details">
				<text class="member-name">{{memberInfo.name}}</text>
				<text class="member-basic">{{memberInfo.age}}岁 | {{memberInfo.gender}} | {{memberInfo.relation}}</text>
				<text class="member-phone">{{memberInfo.phone}}</text>
			</view>
		</view>
		
		<!-- 健康概览 -->
		<view class="health-overview">
			<view class="section-title">健康概览</view>
			<view class="overview-grid">
				<view class="overview-item">
					<text class="overview-value">{{healthOverview.height}}cm</text>
					<text class="overview-label">身高</text>
				</view>
				<view class="overview-item">
					<text class="overview-value">{{healthOverview.weight}}kg</text>
					<text class="overview-label">体重</text>
				</view>
				<view class="overview-item">
					<text class="overview-value">{{healthOverview.bmi}}</text>
					<text class="overview-label">BMI</text>
				</view>
				<view class="overview-item">
					<text class="overview-value">{{healthOverview.bloodType}}</text>
					<text class="overview-label">血型</text>
				</view>
			</view>
		</view>
		
		<!-- 体检记录 -->
		<view class="exam-records">
			<view class="section-title">体检记录</view>
			<view class="record-list" v-if="examRecords.length > 0">
				<view class="record-item" v-for="(record, index) in examRecords" :key="index" @click="viewRecord(record)">
					<view class="record-info">
						<text class="record-title">{{record.title}}</text>
						<text class="record-date">{{record.date}}</text>
						<text class="record-hospital">{{record.hospital}}</text>
					</view>
					<view class="record-status" :class="record.status">
						<text>{{getStatusText(record.status)}}</text>
					</view>
				</view>
			</view>
			<view class="empty-records" v-else>
				<image src="/static/images/empty-records.png" mode="aspectFit" class="empty-image"></image>
				<text class="empty-text">暂无体检记录</text>
			</view>
		</view>
		
		<!-- 健康指标 -->
		<view class="health-indicators">
			<view class="section-title">健康指标</view>
			<view class="indicator-list">
				<view class="indicator-item" v-for="(indicator, index) in healthIndicators" :key="index">
					<view class="indicator-info">
						<text class="indicator-name">{{indicator.name}}</text>
						<text class="indicator-value">{{indicator.value}}{{indicator.unit}}</text>
					</view>
					<view class="indicator-status" :class="indicator.status">
						<text>{{getIndicatorStatus(indicator.status)}}</text>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 过敏史 -->
		<view class="allergies-section" v-if="memberInfo.allergies && memberInfo.allergies !== '无'">
			<view class="section-title">过敏史</view>
			<view class="allergies-content">
				<text class="allergies-text">{{memberInfo.allergies}}</text>
			</view>
		</view>
		
		<!-- 紧急联系人 -->
		<view class="emergency-contact">
			<view class="section-title">紧急联系人</view>
			<view class="contact-info">
				<view class="contact-item">
					<text class="contact-label">姓名：</text>
					<text class="contact-value">{{memberInfo.emergencyContact.name}}</text>
				</view>
				<view class="contact-item">
					<text class="contact-label">电话：</text>
					<text class="contact-value">{{memberInfo.emergencyContact.phone}}</text>
				</view>
				<view class="contact-item">
					<text class="contact-label">关系：</text>
					<text class="contact-value">{{memberInfo.emergencyContact.relation}}</text>
				</view>
			</view>
		</view>
		
		<!-- 操作按钮 -->
		<view class="action-buttons">
			<button class="action-btn" @click="makeAppointment">
				<image src="/static/images/icons/icon-appointment.png" mode="aspectFit" class="btn-icon"></image>
				<text>预约体检</text>
			</button>
			<button class="action-btn" @click="viewHealthReport">
				<image src="/static/images/icons/icon-report.png" mode="aspectFit" class="btn-icon"></image>
				<text>查看报告</text>
			</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				memberId: null,
				memberName: '',
				memberInfo: {
					name: '',
					age: '',
					gender: '',
					relation: '',
					phone: '',
					avatar: '',
					height: '',
					weight: '',
					bloodType: '',
					allergies: '',
					emergencyContact: {
						name: '',
						phone: '',
						relation: ''
					}
				},
				healthOverview: {
					height: 0,
					weight: 0,
					bmi: 0,
					bloodType: ''
				},
				examRecords: [
					{
						id: 1,
						title: '年度体检',
						date: '2023-06-15',
						hospital: '市第一人民医院',
						status: 'completed'
					},
					{
						id: 2,
						title: '专项检查',
						date: '2023-03-20',
						hospital: '市第二人民医院',
						status: 'completed'
					}
				],
				healthIndicators: [
					{
						name: '血压',
						value: '120/80',
						unit: 'mmHg',
						status: 'normal'
					},
					{
						name: '心率',
						value: '75',
						unit: '次/分',
						status: 'normal'
					},
					{
						name: '血糖',
						value: '5.2',
						unit: 'mmol/L',
						status: 'normal'
					},
					{
						name: '胆固醇',
						value: '4.8',
						unit: 'mmol/L',
						status: 'warning'
					}
				]
			}
		},
		onLoad(options) {
			if (options.memberId) {
				this.memberId = options.memberId;
			}
			if (options.memberName) {
				this.memberName = options.memberName;
			}
			this.loadMemberData();
		},
		methods: {
			// 加载成员数据
			loadMemberData() {
				// 模拟数据，实际应该从API获取
				const mockData = {
					1: {
						name: '张小明',
						age: '35',
						gender: '男',
						relation: '父亲',
						phone: '138****1234',
						avatar: '/static/images/avatar-father.jpg',
						height: '175',
						weight: '70',
						bloodType: 'A型',
						allergies: '无',
						emergencyContact: {
							name: '李小红',
							phone: '139****5678',
							relation: '妻子'
						}
					},
					2: {
						name: '李小红',
						age: '32',
						gender: '女',
						relation: '母亲',
						phone: '139****5678',
						avatar: '/static/images/avatar-mother.jpg',
						height: '165',
						weight: '55',
						bloodType: 'B型',
						allergies: '海鲜过敏',
						emergencyContact: {
							name: '张小明',
							phone: '138****1234',
							relation: '丈夫'
						}
					},
					3: {
						name: '张小华',
						age: '8',
						gender: '男',
						relation: '儿子',
						phone: '无',
						avatar: '/static/images/avatar-son.jpg',
						height: '130',
						weight: '30',
						bloodType: 'A型',
						allergies: '无',
						emergencyContact: {
							name: '张小明',
							phone: '138****1234',
							relation: '父亲'
						}
					}
				};
				
				const memberData = mockData[this.memberId];
				if (memberData) {
					this.memberInfo = { ...memberData };
					this.calculateHealthOverview();
				}
			},
			// 计算健康概览
			calculateHealthOverview() {
				const height = parseFloat(this.memberInfo.height);
				const weight = parseFloat(this.memberInfo.weight);
				
				this.healthOverview = {
					height: height,
					weight: weight,
					bmi: height > 0 ? (weight / Math.pow(height / 100, 2)).toFixed(1) : 0,
					bloodType: this.memberInfo.bloodType
				};
			},
			// 获取状态文本
			getStatusText(status) {
				const statusMap = {
					'completed': '已完成',
					'pending': '进行中',
					'cancelled': '已取消'
				};
				return statusMap[status] || '';
			},
			// 获取指标状态
			getIndicatorStatus(status) {
				const statusMap = {
					'normal': '正常',
					'warning': '偏高',
					'danger': '异常',
					'excellent': '优秀'
				};
				return statusMap[status] || '';
			},
			// 查看记录详情
			viewRecord(record) {
				uni.navigateTo({
					url: `/pages/exam-record-detail/exam-record-detail?id=${record.id}`
				});
			},
			// 预约体检
			makeAppointment() {
				uni.navigateTo({
					url: `/pages/hospital/hospital?memberId=${this.memberId}&memberName=${this.memberName}`
				});
			},
			// 查看健康报告
			viewHealthReport() {
				uni.navigateTo({
					url: `/pages/health-report/health-report?memberId=${this.memberId}&memberName=${this.memberName}`
				});
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

.member-info-section {
	background-color: #ffffff;
	border-radius: 20rpx;
	padding: 30rpx;
	margin-bottom: 20rpx;
	display: flex;
	align-items: center;
	
	.member-avatar {
		width: 120rpx;
		height: 120rpx;
		margin-right: 30rpx;
		
		image {
			width: 100%;
			height: 100%;
			border-radius: 60rpx;
		}
	}
	
	.member-details {
		flex: 1;
		
		.member-name {
			font-size: 32rpx;
			font-weight: bold;
			color: #333333;
			margin-bottom: 10rpx;
			display: block;
		}
		
		.member-basic {
			font-size: 26rpx;
			color: #666666;
			margin-bottom: 8rpx;
			display: block;
		}
		
		.member-phone {
			font-size: 24rpx;
			color: #999999;
			display: block;
		}
	}
}

.health-overview, .exam-records, .health-indicators, .allergies-section, .emergency-contact {
	background-color: #ffffff;
	border-radius: 20rpx;
	padding: 30rpx;
	margin-bottom: 20rpx;
	
	.section-title {
		font-size: 30rpx;
		font-weight: bold;
		color: #333333;
		margin-bottom: 25rpx;
		padding-left: 20rpx;
		border-left: 6rpx solid #1296db;
	}
}

.overview-grid {
	display: flex;
	justify-content: space-between;
	
	.overview-item {
		text-align: center;
		flex: 1;
		
		.overview-value {
			font-size: 32rpx;
			font-weight: bold;
			color: #1296db;
			margin-bottom: 10rpx;
			display: block;
		}
		
		.overview-label {
			font-size: 24rpx;
			color: #666666;
			display: block;
		}
	}
}

.record-list {
	.record-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 25rpx 0;
		border-bottom: 1px solid #f5f5f5;
		
		&:last-child {
			border-bottom: none;
		}
		
		.record-info {
			flex: 1;
			
			.record-title {
				font-size: 28rpx;
				font-weight: bold;
				color: #333333;
				margin-bottom: 8rpx;
				display: block;
			}
			
			.record-date {
				font-size: 24rpx;
				color: #666666;
				margin-bottom: 8rpx;
				display: block;
			}
			
			.record-hospital {
				font-size: 22rpx;
				color: #999999;
				display: block;
			}
		}
		
		.record-status {
			padding: 8rpx 16rpx;
			border-radius: 15rpx;
			font-size: 22rpx;
			
			&.completed {
				background-color: #52c41a;
				color: #ffffff;
			}
			
			&.pending {
				background-color: #faad14;
				color: #ffffff;
			}
			
			&.cancelled {
				background-color: #ff4d4f;
				color: #ffffff;
			}
		}
	}
}

.empty-records {
	text-align: center;
	padding: 60rpx 0;
	
	.empty-image {
		width: 150rpx;
		height: 150rpx;
		margin-bottom: 20rpx;
	}
	
	.empty-text {
		font-size: 26rpx;
		color: #999999;
	}
}

.indicator-list {
	.indicator-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 25rpx 0;
		border-bottom: 1px solid #f5f5f5;
		
		&:last-child {
			border-bottom: none;
		}
		
		.indicator-info {
			flex: 1;
			
			.indicator-name {
				font-size: 28rpx;
				color: #333333;
				margin-bottom: 8rpx;
				display: block;
			}
			
			.indicator-value {
				font-size: 26rpx;
				color: #666666;
				display: block;
			}
		}
		
		.indicator-status {
			padding: 8rpx 16rpx;
			border-radius: 15rpx;
			font-size: 22rpx;
			
			&.normal {
				background-color: #52c41a;
				color: #ffffff;
			}
			
			&.warning {
				background-color: #faad14;
				color: #ffffff;
			}
			
			&.danger {
				background-color: #ff4d4f;
				color: #ffffff;
			}
			
			&.excellent {
				background-color: #1296db;
				color: #ffffff;
			}
		}
	}
}

.allergies-content {
	.allergies-text {
		font-size: 28rpx;
		color: #333333;
		line-height: 1.5;
	}
}

.contact-info {
	.contact-item {
		display: flex;
		padding: 15rpx 0;
		
		.contact-label {
			width: 120rpx;
			font-size: 28rpx;
			color: #666666;
		}
		
		.contact-value {
			flex: 1;
			font-size: 28rpx;
			color: #333333;
		}
	}
}

.action-buttons {
	display: flex;
	justify-content: space-between;
	margin-top: 30rpx;
	
	.action-btn {
		width: 48%;
		height: 90rpx;
		border-radius: 45rpx;
		font-size: 28rpx;
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
			margin-right: 10rpx;
		}
	}
}
</style> 