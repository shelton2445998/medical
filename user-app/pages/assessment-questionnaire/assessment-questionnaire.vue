<template>
	<view class="content">
		<!-- 页面标题 -->
		<view class="page-header">
			<text class="page-title">{{assessmentType}}</text>
			<text class="page-subtitle">请如实回答以下问题</text>
		</view>
		
		<!-- 进度条 -->
		<view class="progress-section">
			<view class="progress-bar">
				<view class="progress-fill" :style="{width: progress + '%'}"></view>
			</view>
			<text class="progress-text">{{currentIndex + 1}} / {{questions.length}}</text>
		</view>
		
		<!-- 问题列表 -->
		<view class="question-section" v-if="currentQuestion">
			<view class="question-item">
				<text class="question-title">{{currentQuestion.title}}</text>
				<text class="question-desc" v-if="currentQuestion.description">{{currentQuestion.description}}</text>
				
				<!-- 单选题 -->
				<view class="options-list" v-if="currentQuestion.type === 'single'">
					<view 
						class="option-item" 
						v-for="(option, index) in currentQuestion.options" 
						:key="index"
						:class="{selected: selectedAnswer === option.value}"
						@click="selectAnswer(option.value)"
					>
						<text class="option-text">{{option.text}}</text>
						<view class="option-icon" v-if="selectedAnswer === option.value">
							<text class="iconfont icon-check">?</text>
						</view>
					</view>
				</view>
				
				<!-- 多选题 -->
				<view class="options-list" v-if="currentQuestion.type === 'multiple'">
					<view 
						class="option-item" 
						v-for="(option, index) in currentQuestion.options" 
						:key="index"
						:class="{selected: selectedAnswers.includes(option.value)}"
						@click="toggleAnswer(option.value)"
					>
						<text class="option-text">{{option.text}}</text>
						<view class="option-icon" v-if="selectedAnswers.includes(option.value)">
							<text class="iconfont icon-check">?</text>
						</view>
					</view>
				</view>
				
				<!-- 数值输入 -->
				<view class="input-section" v-if="currentQuestion.type === 'number'">
					<input 
						class="number-input" 
						type="number" 
						v-model="numberAnswer"
						:placeholder="currentQuestion.placeholder"
					/>
					<text class="input-unit" v-if="currentQuestion.unit">{{currentQuestion.unit}}</text>
				</view>
			</view>
		</view>
		
		<!-- 操作按钮 -->
		<view class="action-section">
			<button 
				class="action-btn prev-btn" 
				v-if="currentIndex > 0"
				@click="prevQuestion"
			>上一题</button>
			<button 
				class="action-btn next-btn" 
				v-if="currentIndex < questions.length - 1"
				@click="nextQuestion"
				:disabled="!canNext"
			>下一题</button>
			<button 
				class="action-btn submit-btn" 
				v-if="currentIndex === questions.length - 1"
				@click="submitAssessment"
				:disabled="!canSubmit"
			>提交评估</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				assessmentType: '',
				typeId: '',
				currentIndex: 0,
				selectedAnswer: '',
				selectedAnswers: [],
				numberAnswer: '',
				answers: [],
				questions: [
					{
						id: 1,
						title: '您的年龄是多少？',
						type: 'number',
						placeholder: '请输入年龄',
						unit: '岁'
					},
					{
						id: 2,
						title: '您的性别是？',
						type: 'single',
						options: [
							{value: 'male', text: '男'},
							{value: 'female', text: '女'}
						]
					},
					{
						id: 3,
						title: '您的身高是多少？',
						type: 'number',
						placeholder: '请输入身高',
						unit: 'cm'
					},
					{
						id: 4,
						title: '您的体重是多少？',
						type: 'number',
						placeholder: '请输入体重',
						unit: 'kg'
					},
					{
						id: 5,
						title: '您是否有高血压病史？',
						type: 'single',
						options: [
							{value: 'yes', text: '是'},
							{value: 'no', text: '否'}
						]
					},
					{
						id: 6,
						title: '您是否有糖尿病病史？',
						type: 'single',
						options: [
							{value: 'yes', text: '是'},
							{value: 'no', text: '否'}
						]
					},
					{
						id: 7,
						title: '您是否吸烟？',
						type: 'single',
						options: [
							{value: 'yes', text: '是'},
							{value: 'no', text: '否'},
							{value: 'quit', text: '已戒烟'}
						]
					},
					{
						id: 8,
						title: '您每周运动频率如何？',
						type: 'single',
						options: [
							{value: 'never', text: '从不运动'},
							{value: 'rarely', text: '偶尔运动'},
							{value: 'sometimes', text: '有时运动'},
							{value: 'often', text: '经常运动'},
							{value: 'always', text: '每天运动'}
						]
					}
				]
			}
		},
		computed: {
			currentQuestion() {
				return this.questions[this.currentIndex];
			},
			progress() {
				return ((this.currentIndex + 1) / this.questions.length) * 100;
			},
			canNext() {
				const question = this.currentQuestion;
				if (question.type === 'single') {
					return this.selectedAnswer !== '';
				} else if (question.type === 'multiple') {
					return this.selectedAnswers.length > 0;
				} else if (question.type === 'number') {
					return this.numberAnswer !== '';
				}
				return false;
			},
			canSubmit() {
				return this.answers.length === this.questions.length;
			}
		},
		onLoad(options) {
			this.assessmentType = options.typeName || '健康评估';
			this.typeId = options.typeId || '';
		},
		methods: {
			// 选择答案（单选题）
			selectAnswer(value) {
				this.selectedAnswer = value;
			},
			// 切换答案（多选题）
			toggleAnswer(value) {
				const index = this.selectedAnswers.indexOf(value);
				if (index > -1) {
					this.selectedAnswers.splice(index, 1);
				} else {
					this.selectedAnswers.push(value);
				}
			},
			// 保存当前答案
			saveAnswer() {
				const question = this.currentQuestion;
				let answer = '';
				
				if (question.type === 'single') {
					answer = this.selectedAnswer;
				} else if (question.type === 'multiple') {
					answer = this.selectedAnswers.join(',');
				} else if (question.type === 'number') {
					answer = this.numberAnswer;
				}
				
				this.answers[this.currentIndex] = {
					questionId: question.id,
					answer: answer
				};
			},
			// 下一题
			nextQuestion() {
				if (!this.canNext) return;
				
				this.saveAnswer();
				this.currentIndex++;
				this.resetAnswer();
			},
			// 上一题
			prevQuestion() {
				if (this.currentIndex > 0) {
					this.currentIndex--;
					this.loadAnswer();
				}
			},
			// 重置答案
			resetAnswer() {
				this.selectedAnswer = '';
				this.selectedAnswers = [];
				this.numberAnswer = '';
			},
			// 加载答案
			loadAnswer() {
				const savedAnswer = this.answers[this.currentIndex];
				if (savedAnswer) {
					const question = this.currentQuestion;
					if (question.type === 'single') {
						this.selectedAnswer = savedAnswer.answer;
					} else if (question.type === 'multiple') {
						this.selectedAnswers = savedAnswer.answer.split(',');
					} else if (question.type === 'number') {
						this.numberAnswer = savedAnswer.answer;
					}
				}
			},
			// 提交评估
			submitAssessment() {
				if (!this.canSubmit) return;
				
				this.saveAnswer();
				
				uni.showLoading({
					title: '正在评估...'
				});
				
				// 模拟评估过程
				setTimeout(() => {
					uni.hideLoading();
					
					// 计算评估结果
					const score = this.calculateScore();
					const resultLevel = this.getResultLevel(score);
					
					uni.navigateTo({
						url: `/pages/assessment-result/assessment-result?score=${score}&level=${resultLevel}&type=${this.assessmentType}`
					});
				}, 2000);
			},
			// 计算评分
			calculateScore() {
				let score = 100;
				
				// 简单的评分逻辑
				this.answers.forEach(answer => {
					if (answer.questionId === 5 && answer.answer === 'yes') {
						score -= 20; // 高血压减分
					}
					if (answer.questionId === 6 && answer.answer === 'yes') {
						score -= 25; // 糖尿病减分
					}
					if (answer.questionId === 7 && answer.answer === 'yes') {
						score -= 15; // 吸烟减分
					}
					if (answer.questionId === 8 && answer.answer === 'never') {
						score -= 10; // 不运动减分
					}
				});
				
				return Math.max(score, 0);
			},
			// 获取结果等级
			getResultLevel(score) {
				if (score >= 90) return 'excellent';
				if (score >= 80) return 'good';
				if (score >= 60) return 'normal';
				return 'poor';
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
		margin-bottom: 10rpx;
		display: block;
	}
	
	.page-subtitle {
		font-size: 24rpx;
		color: #666666;
		display: block;
	}
}

.progress-section {
	background-color: #ffffff;
	border-radius: 20rpx;
	padding: 30rpx;
	margin-bottom: 30rpx;
	
	.progress-bar {
		height: 10rpx;
		background-color: #f0f0f0;
		border-radius: 5rpx;
		overflow: hidden;
		margin-bottom: 15rpx;
		
		.progress-fill {
			height: 100%;
			background-color: #1296db;
			transition: width 0.3s ease;
		}
	}
	
	.progress-text {
		font-size: 24rpx;
		color: #666666;
		text-align: center;
	}
}

.question-section {
	background-color: #ffffff;
	border-radius: 20rpx;
	padding: 30rpx;
	margin-bottom: 30rpx;
	
	.question-item {
		.question-title {
			font-size: 32rpx;
			font-weight: bold;
			color: #333333;
			margin-bottom: 15rpx;
			display: block;
		}
		
		.question-desc {
			font-size: 26rpx;
			color: #666666;
			margin-bottom: 30rpx;
			display: block;
		}
		
		.options-list {
			.option-item {
				display: flex;
				justify-content: space-between;
				align-items: center;
				padding: 25rpx;
				border: 2rpx solid #e8e8e8;
				border-radius: 15rpx;
				margin-bottom: 20rpx;
				transition: all 0.3s ease;
				
				&.selected {
					border-color: #1296db;
					background-color: #f0f8ff;
				}
				
				.option-text {
					font-size: 28rpx;
					color: #333333;
				}
				
				.option-icon {
					width: 40rpx;
					height: 40rpx;
					background-color: #1296db;
					border-radius: 50%;
					display: flex;
					align-items: center;
					justify-content: center;
					color: #ffffff;
					font-size: 24rpx;
				}
			}
		}
		
		.input-section {
			display: flex;
			align-items: center;
			
			.number-input {
				flex: 1;
				height: 80rpx;
				border: 2rpx solid #e8e8e8;
				border-radius: 15rpx;
				padding: 0 20rpx;
				font-size: 28rpx;
				margin-right: 15rpx;
			}
			
			.input-unit {
				font-size: 28rpx;
				color: #666666;
			}
		}
	}
}

.action-section {
	display: flex;
	justify-content: space-between;
	
	.action-btn {
		flex: 1;
		height: 90rpx;
		border-radius: 45rpx;
		font-size: 28rpx;
		margin: 0 10rpx;
		
		&::after {
			border: none;
		}
		
		&.prev-btn {
			background-color: #f5f5f5;
			color: #666666;
		}
		
		&.next-btn {
			background-color: #1296db;
			color: #ffffff;
			
			&:disabled {
				background-color: #cccccc;
				color: #999999;
			}
		}
		
		&.submit-btn {
			background-color: #52c41a;
			color: #ffffff;
			
			&:disabled {
				background-color: #cccccc;
				color: #999999;
			}
		}
	}
}
</style> 