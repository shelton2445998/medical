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
				<view class="header-icon">💊</view>
				<view class="header-title">健康评估</view>
				<view class="header-desc">科学评估，健康管理</view>
			</view>
			
		<!-- 评估类型选择 -->
			<view class="card assessment-card">
				<view class="card-header">
					<view class="card-title-wrapper">
						<text class="card-icon">📊</text>
						<text class="card-title">评估类型</text>
					</view>
				</view>
		<view class="assessment-types">
					<view 
						class="type-item" 
						v-for="(type, index) in assessmentTypes" 
						:key="index" 
						@click="startAssessment(type)"
						:style="{ animationDelay: (index * 0.1) + 's' }"
					>
						<view class="type-icon-wrapper">
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
		</view>
		
		<!-- 历史评估记录 -->
			<view class="card history-card" v-if="assessmentHistory.length > 0">
				<view class="card-header">
					<view class="card-title-wrapper">
						<text class="card-icon">📈</text>
						<text class="card-title">历史评估</text>
					</view>
				</view>
			<view class="history-list">
					<view 
						class="history-item" 
						v-for="(item, index) in assessmentHistory" 
						:key="index" 
						@click="viewResult(item)"
						:style="{ animationDelay: (index * 0.1) + 's' }"
					>
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
			<view class="card advice-card">
				<view class="card-header">
					<view class="card-title-wrapper">
						<text class="card-icon">💡</text>
						<text class="card-title">健康建议</text>
					</view>
				</view>
			<view class="advice-list">
					<view 
						class="advice-item" 
						v-for="(advice, index) in healthAdvice" 
						:key="index"
						:style="{ animationDelay: (index * 0.1) + 's' }"
					>
						<view class="advice-icon-wrapper">
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
	background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
	min-height: 100vh;
	padding: 20rpx;
	position: relative;
}

.floating-shapes {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	overflow: hidden;
	z-index: -1;
	
	.shape {
		position: absolute;
		background: linear-gradient(45deg, rgba(9, 132, 227, 0.1), rgba(116, 185, 255, 0.05));
		border-radius: 50%;
		animation: float 10s infinite ease-in-out;
	}
	
	.shape-1 {
		width: 100px;
		height: 100px;
		left: 10%;
		top: 20%;
		animation-delay: -2s;
	}
	
	.shape-2 {
		width: 150px;
		height: 150px;
		right: 10%;
		bottom: 30%;
		animation-delay: -4s;
	}
	
	.shape-3 {
		width: 80px;
		height: 80px;
		left: 70%;
		top: 70%;
		animation-delay: -6s;
	}
	
	.shape-4 {
		width: 120px;
		height: 120px;
		right: 20%;
		bottom: 10%;
		animation-delay: -8s;
	}
}

@keyframes float {
	0%, 100% {
		transform: translateY(0px) rotate(0deg);
	}
	50% {
		transform: translateY(-20px) rotate(180deg);
	}
}

.main-content {
	padding: 0 30rpx;
	position: relative;
	z-index: 1;
}

.page-header {
	text-align: center;
	padding: 40rpx 0;
	
	.header-icon {
		font-size: 80rpx;
		margin-bottom: 20rpx;
		animation: pulse 2s infinite;
	}
	
	.header-title {
		font-size: 40rpx;
		font-weight: bold;
		color: #333333;
		margin-bottom: 10rpx;
}

	.header-desc {
		font-size: 28rpx;
		color: #666666;
	}
}

.card {
	background: #ffffff;
	border-radius: 20rpx;
	padding: 30rpx;
	margin-bottom: 20rpx;
	box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.08);
	transition: all 0.3s ease;
	position: relative;
	overflow: hidden;
	
	&::before {
		content: '';
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		height: 4rpx;
		background: linear-gradient(90deg, #0984e3, #74b9ff);
	}
	
	&:hover {
		transform: translateY(-4rpx);
		box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.12);
	}
	
	.card-header {
	margin-bottom: 30rpx;
	
		.card-title-wrapper {
			display: flex;
			align-items: center;
			position: relative;
			
			.card-icon {
				font-size: 40rpx;
				margin-right: 15rpx;
				color: #0984e3;
			}
			
			.card-title {
				font-size: 32rpx;
				font-weight: bold;
				color: #333333;
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
					background: linear-gradient(135deg, #0984e3, #74b9ff);
					border-radius: 3rpx;
				}
			}
		}
	}
}

.assessment-types {
	.type-item {
		display: flex;
		align-items: center;
		padding: 30rpx 0;
		border-bottom: 1px solid #f5f5f5;
		animation: fadeInUp 0.6s ease-out forwards;
		opacity: 0;
		transform: translateY(20rpx);
		transition: all 0.3s ease;
		
		&:last-child {
			border-bottom: none;
		}
		
		&:active {
			background: rgba(9, 132, 227, 0.05);
			border-radius: 12rpx;
			padding-left: 20rpx;
			padding-right: 20rpx;
		}
		
		.type-icon-wrapper {
			width: 80rpx;
			height: 80rpx;
			border-radius: 40rpx;
			background: transparent;
			border: 3rpx solid #0984e3;
			display: flex;
			justify-content: center;
			align-items: center;
			margin-right: 20rpx;
			box-shadow: 0 4rpx 12rpx rgba(9, 132, 227, 0.2);
			transition: all 0.3s ease;
			flex-shrink: 0;
			
			&:active {
				transform: scale(0.9);
				background: rgba(9, 132, 227, 0.1);
				box-shadow: 0 2rpx 8rpx rgba(9, 132, 227, 0.3);
			}
			
			image {
				width: 40rpx;
				height: 40rpx;
				filter: brightness(0) saturate(100%) invert(27%) sepia(51%) saturate(2878%) hue-rotate(199deg) brightness(104%) contrast(97%);
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
			font-weight: 500;
			transition: all 0.3s ease;
			
			&.available {
				background: linear-gradient(135deg, #0984e3, #74b9ff);
				color: #ffffff;
				box-shadow: 0 2rpx 8rpx rgba(9, 132, 227, 0.3);
			}
			
			&.coming-soon {
				background: linear-gradient(135deg, #f5f5f5, #e0e0e0);
				color: #999999;
				border: 2rpx solid #e0e0e0;
			}
		}
	}
	}
	
	.history-list {
		.history-item {
			display: flex;
			justify-content: space-between;
			align-items: center;
			padding: 20rpx 0;
			border-bottom: 1px solid #f5f5f5;
		animation: fadeInUp 0.6s ease-out forwards;
		opacity: 0;
		transform: translateY(20rpx);
		transition: all 0.3s ease;
			
			&:last-child {
				border-bottom: none;
			}
		
		&:active {
			background: rgba(9, 132, 227, 0.05);
			border-radius: 12rpx;
			padding-left: 20rpx;
			padding-right: 20rpx;
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
				color: #0984e3;
				font-weight: 500;
					display: block;
				}
			}
			
			.history-result {
				padding: 8rpx 16rpx;
				border-radius: 15rpx;
				font-size: 22rpx;
			font-weight: 500;
			box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
				
				&.excellent {
				background: linear-gradient(135deg, #52c41a, #73d13d);
					color: #ffffff;
				}
				
				&.good {
				background: linear-gradient(135deg, #0984e3, #74b9ff);
					color: #ffffff;
				}
				
				&.normal {
				background: linear-gradient(135deg, #faad14, #ffc53d);
					color: #ffffff;
				}
				
				&.poor {
				background: linear-gradient(135deg, #ff4d4f, #ff7875);
					color: #ffffff;
				}
			}
		}
	}
	
	.advice-list {
		.advice-item {
			display: flex;
			align-items: center;
			padding: 20rpx 0;
			border-bottom: 1px solid #f5f5f5;
		animation: fadeInUp 0.6s ease-out forwards;
		opacity: 0;
		transform: translateY(20rpx);
		transition: all 0.3s ease;
			
			&:last-child {
				border-bottom: none;
			}
			
		&:active {
			background: rgba(9, 132, 227, 0.05);
			border-radius: 12rpx;
			padding-left: 20rpx;
			padding-right: 20rpx;
		}
		
		.advice-icon-wrapper {
				width: 60rpx;
				height: 60rpx;
			border-radius: 30rpx;
			background: transparent;
			border: 2rpx solid #0984e3;
			display: flex;
			justify-content: center;
			align-items: center;
				margin-right: 20rpx;
			box-shadow: 0 2rpx 8rpx rgba(9, 132, 227, 0.15);
			transition: all 0.3s ease;
			flex-shrink: 0;
			
			&:active {
				transform: scale(0.9);
				background: rgba(9, 132, 227, 0.1);
				box-shadow: 0 2rpx 8rpx rgba(9, 132, 227, 0.25);
			}
				
				image {
				width: 30rpx;
				height: 30rpx;
				filter: brightness(0) saturate(100%) invert(27%) sepia(51%) saturate(2878%) hue-rotate(199deg) brightness(104%) contrast(97%);
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

@keyframes pulse {
	0%, 100% {
		transform: scale(1);
		opacity: 0.8;
	}
	50% {
		transform: scale(1.05);
		opacity: 1;
	}
}

@keyframes fadeInUp {
	to {
		opacity: 1;
		transform: translateY(0);
	}
}
</style> 