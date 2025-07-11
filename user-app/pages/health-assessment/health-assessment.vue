<template>
	<view class="content">
		<!-- 页面标题 -->
		<view class="page-header">
			<text class="page-title">健康评估</text>
		</view>
		
		<!-- 评估类型选择 -->
		<view class="assessment-types">
			<view class="type-item" v-for="(type, index) in assessmentTypes" :key="index" @click="startAssessment(type)">
				<view class="type-icon">
					<image :src="type.icon" mode="aspectFit"></image>
				</view>
				<view class="type-info">
					<text class="type-name">{{type.name}}</text>
					<text class="type-desc">{{type.description}}</text>
					<text class="type-duration">预计用时: {{type.duration}}</text>
				</view>
				<view class="type-status" :class="type.status">
					<text>{{getStatusText(type.status)}}</text>
				</view>
			</view>
		</view>
		
		<!-- 历史评估记录 -->
		<view class="history-section" v-if="assessmentHistory.length > 0">
			<view class="section-title">历史评估</view>
			<view class="history-list">
				<view class="history-item" v-for="(item, index) in assessmentHistory" :key="index" @click="viewResult(item)">
					<view class="history-info">
						<text class="history-name">{{item.typeName}}</text>
						<text class="history-date">{{item.date}}</text>
						<text class="history-score">评分: {{item.score}}分</text>
					</view>
					<view class="history-result" :class="item.resultLevel">
						<text>{{getResultText(item.resultLevel)}}</text>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 健康建议 -->
		<view class="advice-section">
			<view class="section-title">健康建议</view>
			<view class="advice-list">
				<view class="advice-item" v-for="(advice, index) in healthAdvice" :key="index">
					<view class="advice-icon">
						<image :src="advice.icon" mode="aspectFit"></image>
					</view>
					<view class="advice-content">
						<text class="advice-title">{{advice.title}}</text>
						<text class="advice-desc">{{advice.description}}</text>
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
				assessmentTypes: [
					{
						id: 1,
						name: '心血管健康评估',
						description: '评估心血管疾病风险，包括血压、血脂等指标',
						icon: '/static/images/icons/icon-heart.png',
						duration: '5-10分钟',
						status: 'available'
					},
					{
						id: 2,
						name: '糖尿病风险评估',
						description: '评估糖尿病发病风险，包括血糖、体重等指标',
						icon: '/static/images/icons/icon-diabetes.png',
						duration: '3-5分钟',
						status: 'available'
					},
					{
						id: 3,
						name: '心理健康评估',
						description: '评估心理健康状况，包括压力、情绪等指标',
						icon: '/static/images/icons/icon-mental.png',
						duration: '8-12分钟',
						status: 'coming-soon'
					},
					{
						id: 4,
						name: '营养健康评估',
						description: '评估营养状况，包括饮食、维生素等指标',
						icon: '/static/images/icons/icon-nutrition.png',
						duration: '5-8分钟',
						status: 'coming-soon'
					}
				],
				assessmentHistory: [
					{
						id: 1,
						typeName: '心血管健康评估',
						date: '2023-07-15',
						score: 85,
						resultLevel: 'good'
					},
					{
						id: 2,
						typeName: '糖尿病风险评估',
						date: '2023-07-10',
						score: 72,
						resultLevel: 'normal'
					},
					{
						id: 3,
						typeName: '心血管健康评估',
						date: '2023-06-20',
						score: 78,
						resultLevel: 'normal'
					}
				],
				healthAdvice: [
					{
						title: '定期体检',
						description: '建议每年进行一次全面体检，及时发现健康问题',
						icon: '/static/images/icons/icon-checkup.png'
					},
					{
						title: '健康饮食',
						description: '保持均衡饮食，多吃蔬菜水果，少油少盐',
						icon: '/static/images/icons/icon-diet.png'
					},
					{
						title: '适量运动',
						description: '每周进行3-5次中等强度运动，每次30-60分钟',
						icon: '/static/images/icons/icon-exercise.png'
					},
					{
						title: '戒烟限酒',
						description: '戒烟限酒，保持健康的生活方式',
						icon: '/static/images/icons/icon-smoke.png'
					}
				]
			}
		},
		onLoad() {
			// 获取评估历史
			this.getAssessmentHistory();
		},
		methods: {
			// 获取评估历史
			getAssessmentHistory() {
				// 这里可以替换为实际的API调用
				console.log('获取评估历史');
			},
			// 开始评估
			startAssessment(type) {
				if (type.status === 'coming-soon') {
					uni.showToast({
						title: '功能即将上线',
						icon: 'none'
					});
					return;
				}
				
				uni.navigateTo({
					url: `/pages/assessment-questionnaire/assessment-questionnaire?typeId=${type.id}&typeName=${type.name}`
				});
			},
			// 查看评估结果
			viewResult(item) {
				uni.navigateTo({
					url: `/pages/assessment-result/assessment-result?id=${item.id}`
				});
			},
			// 获取状态文本
			getStatusText(status) {
				const statusMap = {
					'available': '立即评估',
					'coming-soon': '即将上线'
				};
				return statusMap[status] || '';
			},
			// 获取结果文本
			getResultText(level) {
				const resultMap = {
					'excellent': '优秀',
					'good': '良好',
					'normal': '一般',
					'poor': '较差'
				};
				return resultMap[level] || '';
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

.assessment-types {
	background-color: #ffffff;
	border-radius: 20rpx;
	padding: 30rpx;
	margin-bottom: 30rpx;
	
	.type-item {
		display: flex;
		align-items: center;
		padding: 30rpx 0;
		border-bottom: 1px solid #f5f5f5;
		
		&:last-child {
			border-bottom: none;
		}
		
		.type-icon {
			width: 80rpx;
			height: 80rpx;
			margin-right: 20rpx;
			
			image {
				width: 100%;
				height: 100%;
			}
		}
		
		.type-info {
			flex: 1;
			
			.type-name {
				font-size: 30rpx;
				font-weight: bold;
				color: #333333;
				margin-bottom: 8rpx;
				display: block;
			}
			
			.type-desc {
				font-size: 24rpx;
				color: #666666;
				margin-bottom: 8rpx;
				display: block;
			}
			
			.type-duration {
				font-size: 22rpx;
				color: #999999;
				display: block;
			}
		}
		
		.type-status {
			padding: 10rpx 20rpx;
			border-radius: 20rpx;
			font-size: 24rpx;
			
			&.available {
				background-color: #1296db;
				color: #ffffff;
			}
			
			&.coming-soon {
				background-color: #f5f5f5;
				color: #999999;
			}
		}
	}
}

.history-section {
	background-color: #ffffff;
	border-radius: 20rpx;
	padding: 30rpx;
	margin-bottom: 30rpx;
	
	.section-title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333333;
		margin-bottom: 30rpx;
	}
	
	.history-list {
		.history-item {
			display: flex;
			justify-content: space-between;
			align-items: center;
			padding: 20rpx 0;
			border-bottom: 1px solid #f5f5f5;
			
			&:last-child {
				border-bottom: none;
			}
			
			.history-info {
				flex: 1;
				
				.history-name {
					font-size: 28rpx;
					font-weight: bold;
					color: #333333;
					margin-bottom: 8rpx;
					display: block;
				}
				
				.history-date {
					font-size: 24rpx;
					color: #999999;
					margin-bottom: 8rpx;
					display: block;
				}
				
				.history-score {
					font-size: 24rpx;
					color: #1296db;
					display: block;
				}
			}
			
			.history-result {
				padding: 8rpx 16rpx;
				border-radius: 15rpx;
				font-size: 22rpx;
				
				&.excellent {
					background-color: #52c41a;
					color: #ffffff;
				}
				
				&.good {
					background-color: #1296db;
					color: #ffffff;
				}
				
				&.normal {
					background-color: #faad14;
					color: #ffffff;
				}
				
				&.poor {
					background-color: #ff4d4f;
					color: #ffffff;
				}
			}
		}
	}
}

.advice-section {
	background-color: #ffffff;
	border-radius: 20rpx;
	padding: 30rpx;
	
	.section-title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333333;
		margin-bottom: 30rpx;
	}
	
	.advice-list {
		.advice-item {
			display: flex;
			align-items: center;
			padding: 20rpx 0;
			border-bottom: 1px solid #f5f5f5;
			
			&:last-child {
				border-bottom: none;
			}
			
			.advice-icon {
				width: 60rpx;
				height: 60rpx;
				margin-right: 20rpx;
				
				image {
					width: 100%;
					height: 100%;
				}
			}
			
			.advice-content {
				flex: 1;
				
				.advice-title {
					font-size: 28rpx;
					font-weight: bold;
					color: #333333;
					margin-bottom: 8rpx;
					display: block;
				}
				
				.advice-desc {
					font-size: 24rpx;
					color: #666666;
					display: block;
				}
			}
		}
	}
}
</style> 