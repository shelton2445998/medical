<template>
	<view class="content">
		<view class="test-section">
			<text class="section-title">API 测试页面</text>
			
			<view class="test-item">
				<text class="test-label">API 地址:</text>
				<input type="text" v-model="customApiUrl" placeholder="请输入API地址" class="api-input" />
			</view>
			
			<view class="test-item">
				<text class="test-label">推荐医院接口:</text>
				<button class="test-btn" @click="testRecommendHospitals">测试</button>
			</view>
			
			<view class="test-item">
				<text class="test-label">医院列表接口:</text>
				<button class="test-btn" @click="testHospitalList">测试</button>
			</view>
			
			<view class="test-item">
				<text class="test-label">医院详情接口:</text>
				<button class="test-btn" @click="testHospitalDetail">测试</button>
			</view>
		</view>
		
		<view class="result-section">
			<text class="section-title">测试结果</text>
			<view class="result-content">
				<text class="result-text">{{testResult}}</text>
			</view>
		</view>
	</view>
</template>

<script>
	import { get, hospitalApi } from '@/utils/request.js';
	
	export default {
		data() {
			return {
				testResult: '点击上方按钮开始测试...',
				customApiUrl: 'http://localhost:8888/api'
			}
		},
		methods: {
			// 测试推荐医院接口
			async testRecommendHospitals() {
				this.testResult = '正在测试推荐医院接口...';
				
				try {
					const url = `${this.customApiUrl}/hospital/recommend`;
					const result = await get(url);
					
					this.testResult = `推荐医院接口测试成功！\n返回数据: ${JSON.stringify(result, null, 2)}`;
				} catch (error) {
					this.testResult = `推荐医院接口测试失败！\n错误信息: ${error.message}`;
				}
			},
			
			// 测试医院列表接口
			async testHospitalList() {
				this.testResult = '正在测试医院列表接口...';
				
				try {
					const url = `${this.customApiUrl}/hospital/list`;
					const result = await get(url, {
						keyword: '',
						pageIndex: 1,
						pageSize: 5
					});
					
					this.testResult = `医院列表接口测试成功！\n返回数据: ${JSON.stringify(result, null, 2)}`;
				} catch (error) {
					this.testResult = `医院列表接口测试失败！\n错误信息: ${error.message}`;
				}
			},
			
			// 测试医院详情接口
			async testHospitalDetail() {
				this.testResult = '正在测试医院详情接口...';
				
				try {
					const hospitalId = 1; // 测试医院ID
					const url = `${this.customApiUrl}/hospital/detail/${hospitalId}`;
					const result = await get(url);
					
					this.testResult = `医院详情接口测试成功！\n返回数据: ${JSON.stringify(result, null, 2)}`;
				} catch (error) {
					this.testResult = `医院详情接口测试失败！\n错误信息: ${error.message}`;
				}
			}
		}
	}
</script>

<style lang="scss">
	.content {
		padding: 30rpx;
	}
	
	.test-section {
		margin-bottom: 40rpx;
		
		.section-title {
			font-size: 32rpx;
			font-weight: bold;
			color: #333333;
			margin-bottom: 20rpx;
		}
		
		.test-item {
			display: flex;
			align-items: center;
			margin-bottom: 20rpx;
			
			.test-label {
				flex: 1;
				font-size: 28rpx;
				color: #666666;
			}
			
			.test-btn {
				width: 120rpx;
				height: 60rpx;
				background-color: #1296db;
				color: #ffffff;
				font-size: 24rpx;
				border-radius: 30rpx;
			}
			
			.api-input {
				flex: 1;
				height: 60rpx;
				border: 1rpx solid #ddd;
				border-radius: 6rpx;
				padding: 0 10rpx;
				font-size: 24rpx;
			}
		}
	}
	
	.result-section {
		.section-title {
			font-size: 32rpx;
			font-weight: bold;
			color: #333333;
			margin-bottom: 20rpx;
		}
		
		.result-content {
			background-color: #f5f5f5;
			padding: 20rpx;
			border-radius: 10rpx;
			
			.result-text {
				font-size: 24rpx;
				color: #333333;
				line-height: 1.5;
				white-space: pre-wrap;
			}
		}
	}
</style> 