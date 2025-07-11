<template>
	<view class="content">
		<!-- 页面标题 -->
		<view class="page-header">
			<text class="page-title">健康检查项目</text>
			<text class="page-subtitle">了解您的健康指标正常范围</text>
		</view>
		
		<!-- 搜索框 -->
		<view class="search-box">
			<view class="search-input">
				<text class="iconfont icon-search"></text>
				<input type="text" v-model="searchText" placeholder="搜索检查项目" @input="filterIndicators" />
				<text class="iconfont icon-clear" v-if="searchText" @tap="clearSearch"></text>
			</view>
		</view>
		
		<!-- 指标详情 -->
		<view class="indicator-section">
			<view class="section-header">
				<view class="section-title">
					<text class="title-icon"></text>
					<text>项目详情</text>
				</view>
				<text class="indicator-count">共 {{filteredIndicators.length}} 项</text>
			</view>
			
			<!-- 无数据提示 -->
			<view class="no-data" v-if="filteredIndicators.length === 0">
				<text class="no-data-text">暂无匹配的检查项目</text>
			</view>
			
			<!-- 指标列表 -->
			<view class="indicator-list">
				<view class="indicator-item" 
					  v-for="(indicator, index) in filteredIndicators" 
					  :key="index"
					  :class="{'expanded': expandedIndex === index}"
					  @tap="toggleExpand(index)">
					<view class="indicator-header">
						<view class="indicator-info">
							<text class="indicator-index">{{index + 1}}</text>
							<text class="indicator-name">{{indicator.name}}</text>
						</view>
						<view class="expand-icon">
							<text class="iconfont" :class="expandedIndex === index ? 'icon-up' : 'icon-down'"></text>
						</view>
					</view>
					<view class="indicator-data">
						<view class="data-item">
							<text class="data-label">正常范围</text>
							<text class="data-range">{{indicator.normalValue || '暂无数据'}}</text>
						</view>
					</view>
					<view class="indicator-detail" v-if="expandedIndex === index">
						<view class="detail-item">
							<text class="detail-label">检查项ID</text>
							<text class="detail-value">{{indicator.id}}</text>
						</view>
						<view class="detail-item" v-if="indicator.description">
							<text class="detail-label">项目描述</text>
							<text class="detail-value">{{indicator.description || '暂无描述'}}</text>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 返回顶部按钮 -->
		<view class="back-to-top" v-if="showBackToTop" @tap="scrollToTop">
			<text class="iconfont icon-top"></text>
		</view>
	</view>
</template>

<script>
	import { getCheckitemDetailList } from '@/api/health-indicator.js';
	
	export default {
		data() {
			return {
				indicators: [],         // 原始指标数据
				searchText: '',         // 搜索文本
				expandedIndex: -1,      // 当前展开的项目索引，-1表示没有展开的项目
				showBackToTop: false,   // 是否显示返回顶部按钮
				scrollTop: 0            // 页面滚动位置
			}
		},
		computed: {
			// 过滤后的指标列表
			filteredIndicators() {
				if (!this.searchText) {
					return this.indicators;
				}
				
				const searchLower = this.searchText.toLowerCase();
				return this.indicators.filter(item => 
					item.name.toLowerCase().includes(searchLower) || 
					(item.normalValue && item.normalValue.toLowerCase().includes(searchLower))
				);
			}
		},
		onLoad() {
			try {
				console.log("页面加载 - onLoad触发");
				// 获取健康指标数据
				this.getHealthData();
			} catch (error) {
				console.error("onLoad错误:", error);
			}
		},
		
		// // 每次页面显示时调用
		// onShow() {
		// 	try {
		// 		// 获取健康指标数据
		// 		console.log("页面显示 - onShow触发");
		// 		this.getHealthData();
		// 	} catch (error) {
		// 		console.error("onShow错误:", error);
		// 	}
		// },
		
		// 页面初次渲染完成时
		onReady() {
			try {
				console.log("页面初次渲染完成 - onReady触发");
			} catch (error) {
				console.error("onReady错误:", error);
			}
		},
		
		// 监听页面滚动
		onPageScroll(e) {
			this.scrollTop = e.scrollTop;
			this.showBackToTop = e.scrollTop > 200;
		},
		
		methods: {
			// 获取健康数据
			async getHealthData() {
				console.log('开始获取健康数据...');
				uni.showLoading({
					title: '加载中...'
				});
				
				try {
					// 调用API获取检查项明细数据
					const response = await getCheckitemDetailList();
					
					// 检查响应结构
					if (!response) {
						throw new Error('API响应为空');
					}
					
					if (!response.data) {
						throw new Error('API响应缺少data字段');
					}
					
					if (!response.data.list) {
						throw new Error('API响应缺少list字段');
					}
					
					// 将API返回的数据映射到indicators数组
					this.indicators = response.data.list.map((item, index) => {
						return {
							id: item.id || index + 1,
							name: item.name || `指标${index + 1}`,
							normalValue: item.normalValue || '暂无数据',
							description: item.description || null
						};
					});
					
					console.log('成功获取健康指标数据', this.indicators);
					
					// 如果数据为空，显示提示
					if (this.indicators.length === 0) {
						uni.showToast({
							title: '暂无检查项目数据',
							icon: 'none'
						});
					}
				} catch (error) {
					console.error('获取健康指标数据失败', error);
					uni.showToast({
						title: '获取数据失败: ' + (error.message || '未知错误'),
						icon: 'none',
						duration: 3000
					});
				} finally {
					uni.hideLoading();
				}
			},
			
			// 过滤指标
			filterIndicators() {
				// 重置展开状态
				this.expandedIndex = -1;
			},
			
			// 清除搜索
			clearSearch() {
				this.searchText = '';
				this.expandedIndex = -1;
			},
			
			// 切换展开/折叠状态
			toggleExpand(index) {
				if (this.expandedIndex === index) {
					this.expandedIndex = -1; // 如果点击的是当前展开项，则折叠
				} else {
					this.expandedIndex = index; // 否则展开点击项
				}
			},
			
			// 返回顶部
			scrollToTop() {
				uni.pageScrollTo({
					scrollTop: 0,
					duration: 300
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
	padding: 30rpx 0 40rpx;
	
	.page-title {
		font-size: 40rpx;
		font-weight: bold;
		color: #2979ff;
		margin-bottom: 10rpx;
		display: block;
	}
	
	.page-subtitle {
		font-size: 26rpx;
		color: #666;
		display: block;
	}
}

.search-box {
	padding: 0 20rpx 20rpx;
	
	.search-input {
		background-color: #ffffff;
		border-radius: 40rpx;
		padding: 15rpx 30rpx;
		display: flex;
		align-items: center;
		box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
		
		.iconfont {
			color: #999;
			font-size: 32rpx;
			margin-right: 10rpx;
		}
		
		input {
			flex: 1;
			height: 60rpx;
			font-size: 28rpx;
		}
		
		.icon-clear {
			padding: 10rpx;
		}
	}
}

.indicator-section {
	background-color: #ffffff;
	border-radius: 20rpx;
	padding: 30rpx;
	margin-bottom: 30rpx;
	box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
	
	.section-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 30rpx;
		
		.section-title {
			display: flex;
			align-items: center;
			
			.title-icon {
				width: 8rpx;
				height: 30rpx;
				background-color: #2979ff;
				border-radius: 4rpx;
				margin-right: 15rpx;
			}
			
			text {
				font-size: 32rpx;
				font-weight: bold;
				color: #333333;
			}
		}
		
		.indicator-count {
			font-size: 24rpx;
			color: #999;
		}
	}
	
	.no-data {
		padding: 100rpx 0;
		text-align: center;
		
		.no-data-text {
			font-size: 28rpx;
			color: #999;
		}
	}
	
	.indicator-list {
		.indicator-item {
			background-color: #f8f9fa;
			border-radius: 15rpx;
			padding: 25rpx;
			margin-bottom: 20rpx;
			transition: all 0.3s;
			
			&.expanded {
				background-color: #eef5ff;
				box-shadow: 0 4rpx 12rpx rgba(41, 121, 255, 0.1);
			}
			
			.indicator-header {
				display: flex;
				justify-content: space-between;
				align-items: center;
				margin-bottom: 20rpx;
				
				.indicator-info {
					display: flex;
					align-items: center;
					
					.indicator-index {
						width: 40rpx;
						height: 40rpx;
						background-color: #2979ff;
						color: #fff;
						font-size: 24rpx;
						border-radius: 50%;
						display: flex;
						align-items: center;
						justify-content: center;
						margin-right: 15rpx;
					}
					
					.indicator-name {
						font-size: 30rpx;
						font-weight: bold;
						color: #333333;
					}
				}
				
				.expand-icon {
					.iconfont {
						font-size: 24rpx;
						color: #999;
						transition: transform 0.3s;
					}
					
					.icon-up {
						transform: rotate(180deg);
					}
				}
			}
			
			.indicator-data {
				display: flex;
				justify-content: space-between;
				margin-bottom: 20rpx;
				
				.data-item {
					flex: 1;
					
					.data-label {
						font-size: 24rpx;
						color: #999999;
						margin-bottom: 8rpx;
						display: block;
					}
					
					.data-range {
						font-size: 28rpx;
						color: #2979ff;
						font-weight: 500;
						display: block;
					}
				}
			}
			
			.indicator-detail {
				background-color: rgba(255, 255, 255, 0.7);
				border-radius: 10rpx;
				padding: 20rpx;
				margin-top: 20rpx;
				border-top: 1px dashed #e0e0e0;
				
				.detail-item {
					margin-bottom: 15rpx;
					
					&:last-child {
						margin-bottom: 0;
					}
					
					.detail-label {
						font-size: 24rpx;
						color: #999;
						margin-bottom: 5rpx;
						display: block;
					}
					
					.detail-value {
						font-size: 26rpx;
						color: #666;
						display: block;
					}
				}
			}
		}
	}
}

.back-to-top {
	position: fixed;
	right: 30rpx;
	bottom: 100rpx;
	width: 80rpx;
	height: 80rpx;
	background-color: rgba(41, 121, 255, 0.8);
	border-radius: 50%;
	display: flex;
	align-items: center;
	justify-content: center;
	box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.2);
	z-index: 99;
	
	.iconfont {
		color: #fff;
		font-size: 40rpx;
	}
}

/* 简单的图标字体定义 */
.iconfont {
	font-family: "iconfont" !important;
	font-style: normal;
	-webkit-font-smoothing: antialiased;
	-moz-osx-font-smoothing: grayscale;
}

/* 使用通用字符作为图标 */
.icon-search:before {
	content: "\1F50D"; /* 放大镜 */
}
.icon-clear:before {
	content: "\2715"; /* X符号 */
}
.icon-down:before {
	content: "\25BC"; /* 向下箭头 */
}
.icon-up:before {
	content: "\25B2"; /* 向上箭头 */
}
.icon-top:before {
	content: "\2191"; /* 上箭头 */
}
</style>