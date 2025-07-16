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
			<!-- 页面标题 -->
			<view class="page-header">
				<view class="header-icon">🏥</view>
				<view class="header-title">体检套餐选择</view>
				<view class="header-desc">为您推荐最适合的体检套餐</view>
			</view>
			
			<!-- 搜索框 -->
			<view class="search-section">
				<view class="search-box">
					<text class="search-icon">🔍</text>
					<input 
						class="search-input" 
						v-model="searchKeyword" 
						placeholder="搜索套餐名称或项目"
						placeholder-class="search-placeholder"
						@input="onSearchInput"
					/>
					<text class="search-clear" v-if="searchKeyword" @click="clearSearch">✕</text>
				</view>
			</view>
			
			<!-- 套餐分类标签 -->
			<view class="package-tabs">
				<view 
					class="tab-item" 
					v-for="(item, index) in packageTypes" 
					:key="index"
					:class="{active: currentType === item.id}"
					@click="switchType(item.id)"
				>
					<text class="tab-icon">{{item.icon}}</text>
					<text class="tab-text">{{item.name}}</text>
				</view>
			</view>
			

			
			<!-- 套餐列表 -->
			<view class="package-list">
				<view 
					class="package-item" 
					v-for="(item, index) in filteredPackages" 
					:key="index" 
					@click="selectPackage(item)"
					:style="{ animationDelay: index * 0.1 + 's' }"
				>
					<!-- 推荐标签 -->
					<view class="recommend-badge" v-if="item.recommend">
						<text class="badge-icon">⭐</text>
						<text class="badge-text">推荐</text>
					</view>
					
					<!-- 套餐头部 -->
					<view class="package-header">
						<view class="package-info">
							<text class="package-name">{{item.name}}</text>
							<view class="package-tags">
								<text class="package-tag">{{getTypeName(item.type)}}</text>
								<!-- 移除适用人群标签，只在详情页显示 -->
							</view>
						</view>
						<view class="package-price-section">
							<view class="price-main">
								<text class="price-symbol">¥</text>
								<text class="price-value">{{item.discountPrice}}</text>
							</view>
							<text class="price-original">¥{{item.price}}</text>
							<text class="price-discount">{{getDiscountText(item)}}</text>
						</view>
					</view>
					
					<!-- 套餐描述 - 只显示基本信息 -->
					<view class="package-desc">
						<text>{{getPackageSummary(item)}}</text>
					</view>
					
					<!-- 检查项目 -->
					<view class="package-items">
						<view class="items-header">
							<text class="items-title">检查项目</text>
							<text class="items-count">{{item.checkitemCount || 0}}项</text>
						</view>
						<view class="items-preview">
							<text class="preview-text">点击详情查看具体检查项目</text>
						</view>
					</view>
					
					<!-- 套餐底部 -->
					<view class="package-footer">
						<view class="package-stats">
							<text class="package-sold">已售 {{item.sold}}</text>
							<text class="package-rating">⭐ 4.8</text>
						</view>
						<view class="package-actions">
							<button class="detail-btn" @click.stop="showPackageDetail(item)">
								<text class="btn-icon">📋</text>
								<text class="btn-text">详情</text>
							</button>
							<button class="select-btn" @click.stop="selectPackage(item)">
								<text class="btn-icon">✅</text>
								<text class="btn-text">选择</text>
							</button>
						</view>
					</view>
				</view>
				
				<!-- 空状态 -->
				<view class="empty-state" v-if="filteredPackages.length === 0">
					<view class="empty-icon">🔍</view>
					<view class="empty-title">暂无相关套餐</view>
					<view class="empty-desc">试试调整筛选条件或搜索关键词</view>
					<button class="reset-btn" @click="resetFilters">
						<text class="btn-icon">🔄</text>
						<text class="btn-text">重置筛选</text>
					</button>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import { get, packageApi } from '@/utils/request.js';
	import { getCheckitemById, getCheckitemPage } from '@/api/report.js';
	export default {
		data() {
			return {
				currentType: 0, // 0表示全部
				packageTypes: [
					{ id: 0, name: '全部', icon: '📦' },
					{ id: 1, name: '基础', icon: '🏥' },
					{ id: 2, name: '高级', icon: '⭐' },
					{ id: 3, name: '专项', icon: '🎯' }
				],

				searchKeyword: '',
				packages: [],
				filteredPackages: [],
				selectedHospital: null,
				memberId: null,
				memberName: '',

			}
		},
		onLoad(options) {
			// 获取家庭成员信息
			if (options.memberId) {
				this.memberId = options.memberId;
			}
			if (options.memberName) {
				this.memberName = options.memberName;
			}
			
			this.getPackageList();
			
			// 获取选择的医院信息
			let hospitalInfo = uni.getStorageSync('selectedHospital');
			if (hospitalInfo) {
				this.selectedHospital = JSON.parse(hospitalInfo);
			}
		},
		methods: {
			async getPackageList() {
				try {
					const result = await get(packageApi.getPackageList, { pageIndex: 1, pageSize: 20 });
					if (result && result.data && result.data.list) {
						this.packages = result.data.list.map((item, index) => {
							// 计算检查项目数量
							let checkitemCount = 0;
							if (item.checkitemIds) {
								checkitemCount = item.checkitemIds.split(',').length;
							}
							
							return {
								id: item.id,
								name: item.name,
								type: item.type || 1,
								price: item.price || 0,
								discountPrice: item.discountPrice || item.price || 0,
								description: item.description || '',
								checkitemIds: item.checkitemIds || '',
								checkitemCount: checkitemCount,
								// 移除详细信息，只在详情页显示
								// suitableCrowd: item.suitableCrowd || '适合一般人群',
								// appointmentNotice: item.appointmentNotice || '请按医院要求准备',
								sold: item.sold || 0,
								recommend: item.recommend || false,
								popular: item.popular || false,
								new: item.new || false
							};
						});
					} else {
						this.packages = [];
					}
					this.filteredPackages = this.packages;
				} catch (e) {
					uni.showToast({ title: '获取套餐失败', icon: 'none' });
				}
			},
			

			// 切换套餐类型
			switchType(typeId) {
				this.currentType = typeId;
				this.applyFilters();
			},
			// 搜索输入
			onSearchInput() {
				this.applyFilters();
			},
			// 清除搜索
			clearSearch() {
				this.searchKeyword = '';
				this.applyFilters();
			},
			// 切换筛选标签
			toggleFilter(filterValue) {
				const index = this.activeFilters.indexOf(filterValue);
				if (index > -1) {
					this.activeFilters.splice(index, 1);
				} else {
					this.activeFilters.push(filterValue);
				}
				this.applyFilters();
			},
			// 重置筛选
			resetFilters() {
				this.currentType = 0;
				this.searchKeyword = '';
				this.applyFilters();
			},
			// 应用筛选
			applyFilters() {
				let filtered = [...this.packages];
				
				// 按类型筛选
				if (this.currentType !== 0) {
					filtered = filtered.filter(item => item.type === this.currentType);
				}
				
				// 按关键词搜索
				if (this.searchKeyword.trim()) {
					const keyword = this.searchKeyword.toLowerCase();
					filtered = filtered.filter(item => 
						item.name.toLowerCase().includes(keyword) ||
						item.description.toLowerCase().includes(keyword)
					);
				}
				
				this.filteredPackages = filtered;
			},
			// 获取类型名称
			getTypeName(typeId) {
				const type = this.packageTypes.find(item => item.id === typeId);
				return type ? type.name : '';
			},
			// 选择套餐
			selectPackage(pkg) {
				// 存储选择的套餐信息
				uni.setStorageSync('selectedPackage', JSON.stringify(pkg));
				
				// 构建跳转URL，包含家庭成员信息
				let flowUrl = '/pages/appointment/appointment-flow';
				if (this.memberId && this.memberName) {
					flowUrl += `?memberId=${this.memberId}&memberName=${this.memberName}`;
				}
				
				// 跳转到预约流程页面
				uni.navigateTo({
					url: flowUrl
				});
			},
			// 查看套餐详情
			showPackageDetail(pkg) {
				uni.navigateTo({
					url: `/pages/package-detail/package-detail?id=${pkg.id}`,
					success: () => {
						console.log('成功跳转到套餐详情页');
					},
					fail: (err) => {
						console.error('跳转失败:', err);
						uni.showToast({
							title: '跳转失败，请稍后重试',
							icon: 'none'
						});
					}
				});
			},
			// 返回上一页
			goBack() {
				uni.navigateBack();
			},
			// 获取折扣文本
			getDiscountText(item) {
				const discount = (item.discountPrice / item.price * 10).toFixed(1);
				return discount + '折';
			},
			// 获取套餐摘要
			getPackageSummary(item) {
				if (item.description) {
					// 限制描述长度，提供简洁的摘要
					const maxLength = 80;
					if (item.description.length > maxLength) {
						return item.description.substring(0, maxLength) + '...';
					}
					return item.description;
				}
				// 如果没有描述，根据套餐类型提供默认描述
				const defaultDescriptions = {
					1: '基础体检套餐，包含常规检查项目',
					2: '高级体检套餐，包含深度筛查项目',
					3: '专项体检套餐，针对特定人群设计'
				};
				return defaultDescriptions[item.type] || '体检套餐，详情请点击查看';
			}
		}
	}
</script>

<style lang="scss">
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
	z-index: -1;
	
	.shape {
		position: absolute;
		background: rgba(255, 255, 255, 0.1);
		border-radius: 50%;
		animation: float 10s infinite ease-in-out;
		transition: all 0.3s ease;
		
		&:hover {
			transform: scale(1.2);
			background: rgba(255, 255, 255, 0.2);
		}
		
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
	}
	
	.shape-1 {
		width: 200px;
		height: 200px;
		top: 10%;
		left: 10%;
		animation-delay: 0s;
	}
	
	.shape-2 {
		width: 150px;
		height: 150px;
		bottom: 20%;
		right: 20%;
		animation-delay: 1s;
	}
	
	.shape-3 {
		width: 100px;
		height: 100px;
		top: 70%;
		left: 70%;
		animation-delay: 2s;
	}
	
	.shape-4 {
		width: 120px;
		height: 120px;
		bottom: 50%;
		right: 50%;
		animation-delay: 3s;
	}
}

.main-content {
  padding: 20rpx 40rpx 0 40rpx;
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

.search-section {
	margin-bottom: 30rpx;
	animation: slideInDown 0.8s ease-out 0.1s both;
	opacity: 0;
	transform: translateY(-20rpx);
	
	.search-box {
		background: rgba(255, 255, 255, 0.95);
		border-radius: 50rpx;
		padding: 20rpx 30rpx;
		display: flex;
		align-items: center;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
		backdrop-filter: blur(10rpx);
		transition: all 0.3s ease;
		
		&:hover {
			transform: translateY(-2rpx);
			box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.15);
		}
		
		.search-icon {
			font-size: 32rpx;
			color: #999999;
			margin-right: 20rpx;
			transition: all 0.3s ease;
		}
		
		.search-input {
			flex: 1;
			font-size: 28rpx;
			color: #333333;
			background: transparent;
			border: none;
			transition: all 0.3s ease;
			
			&:focus {
				color: #0984e3;
			}
		}
		
		.search-clear {
			font-size: 24rpx;
			color: #999999;
			padding: 10rpx;
			border-radius: 50%;
			transition: all 0.3s ease;
			cursor: pointer;
			
			&:hover {
				background: rgba(0, 0, 0, 0.1);
				color: #666666;
			}
		}
	}
}

.package-tabs {
	display: flex;
	background-color: rgba(255, 255, 255, 0.95);
	padding: 20rpx;
	margin-bottom: 30rpx;
	border-radius: 20rpx;
	box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
	backdrop-filter: blur(10rpx);
	animation: slideInDown 0.8s ease-out 0.2s both;
	opacity: 0;
	transform: translateY(-20rpx);
	transition: all 0.3s ease;
	
	&:hover {
		transform: translateY(-2rpx);
		box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.15);
	}
	
	.tab-item {
		flex: 1;
		height: 80rpx;
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		position: relative;
		transition: all 0.3s ease;
		cursor: pointer;
		border-radius: 12rpx;
		
		.tab-icon {
			font-size: 32rpx;
			margin-bottom: 8rpx;
			transition: all 0.3s ease;
		}
		
		.tab-text {
			font-size: 24rpx;
			color: #666666;
			transition: all 0.3s ease;
		}
		
		&:hover {
			background: rgba(9, 132, 227, 0.1);
			transform: translateY(-2rpx);
			
			.tab-icon {
				transform: scale(1.1);
			}
			
			.tab-text {
				color: #0984e3;
			}
		}
		
		&.active {
			background: rgba(9, 132, 227, 0.15);
			
			.tab-icon {
				color: #0984e3;
				transform: scale(1.1);
			}
			
			.tab-text {
				color: #0984e3;
				font-weight: bold;
			}
			
			&::after {
				content: '';
				position: absolute;
				left: 50%;
				bottom: -10rpx;
				transform: translateX(-50%);
				width: 40rpx;
				height: 4rpx;
				background-color: #0984e3;
				border-radius: 2rpx;
				animation: slideInUp 0.3s ease-out;
			}
		}
	}
}

.filter-section {
	margin-bottom: 30rpx;
	animation: slideInDown 0.8s ease-out 0.3s both;
	opacity: 0;
	transform: translateY(-20rpx);
	
	.filter-title {
		font-size: 28rpx;
		color: #ffffff;
		margin-bottom: 20rpx;
		text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.3);
	}
	
	.filter-tags {
		display: flex;
		flex-wrap: wrap;
		gap: 15rpx;
		
		.filter-tag {
			background: rgba(255, 255, 255, 0.2);
			border-radius: 30rpx;
			padding: 12rpx 20rpx;
			display: flex;
			align-items: center;
			transition: all 0.3s ease;
			cursor: pointer;
			backdrop-filter: blur(10rpx);
			
			.tag-icon {
				font-size: 24rpx;
				margin-right: 8rpx;
				transition: all 0.3s ease;
			}
			
			.tag-text {
				font-size: 24rpx;
				color: rgba(255, 255, 255, 0.9);
				transition: all 0.3s ease;
			}
			
			&:hover {
				background: rgba(255, 255, 255, 0.3);
				transform: translateY(-2rpx);
			}
			
			&.active {
				background: rgba(255, 255, 255, 0.4);
				transform: translateY(-2rpx);
				
				.tag-icon {
					transform: scale(1.1);
				}
				
				.tag-text {
					font-weight: bold;
				}
			}
		}
	}
}

.package-list {
	.package-item {
		background-color: rgba(255, 255, 255, 0.95);
		border-radius: 20rpx;
		margin-bottom: 30rpx;
		padding: 40rpx;
		box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
		backdrop-filter: blur(10rpx);
		transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
		animation: fadeInUp 0.6s ease-out forwards;
		opacity: 0;
		transform: translateY(30rpx);
		position: relative;
		overflow: hidden;
		cursor: pointer;
		
		&::before {
			content: '';
			position: absolute;
			top: 0;
			left: -100%;
			width: 100%;
			height: 100%;
			background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.4), transparent);
			transition: left 0.6s ease;
		}
		
		&::after {
			content: '';
			position: absolute;
			top: 0;
			left: 0;
			right: 0;
			bottom: 0;
			background: linear-gradient(135deg, rgba(116, 185, 255, 0.1), rgba(9, 132, 227, 0.1));
			opacity: 0;
			transition: opacity 0.3s ease;
			pointer-events: none;
		}
		
		/* 流动治愈感背景 */
		&::before {
			content: '';
			position: absolute;
			top: -50%;
			left: -50%;
			width: 200%;
			height: 200%;
			background: radial-gradient(circle, rgba(116, 185, 255, 0.1) 0%, transparent 70%);
			animation: flow 15s linear infinite;
			pointer-events: none;
		}
		
		&:hover {
			transform: translateY(-8rpx) scale(1.02);
			box-shadow: 0 12rpx 32rpx rgba(0, 0, 0, 0.2);
			background-color: rgba(255, 255, 255, 0.98);
			
			&::before {
				left: 100%;
			}
			
			&::after {
				opacity: 1;
			}
			
			.package-name {
				color: #0984e3;
				transform: translateX(5rpx);
			}
			
			.package-tag {
				transform: scale(1.1);
			}
			
			.price-value {
				color: #ff4757;
				transform: scale(1.05);
				
				&::after {
					width: 100%;
				}
			}
		}
		
		&:active {
			transform: translateY(-4rpx) scale(0.98);
			transition: all 0.1s ease;
		}
		
		.recommend-badge {
			position: absolute;
			top: 20rpx;
			right: 20rpx;
			background: linear-gradient(135deg, #ff6b6b, #ee5a24);
			border-radius: 20rpx;
			padding: 8rpx 16rpx;
			display: flex;
			align-items: center;
			animation: bounce 2s infinite;
			transition: all 0.3s ease;
			z-index: 10;
			
			.badge-icon {
				font-size: 20rpx;
				margin-right: 6rpx;
				transition: all 0.3s ease;
			}
			
			.badge-text {
				font-size: 20rpx;
				color: #ffffff;
				font-weight: bold;
				transition: all 0.3s ease;
			}
			
			&:hover {
				transform: scale(1.1);
			}
		}
		
		.package-header {
			display: flex;
			justify-content: space-between;
			align-items: flex-start;
			margin-bottom: 25rpx;
			transition: all 0.3s ease;
			
			.package-info {
				flex: 1;
				margin-right: 20rpx;
				
				.package-name {
					font-size: 36rpx;
					font-weight: bold;
					color: #333333;
					margin-bottom: 15rpx;
					transition: all 0.3s ease;
					display: block;
				}
				
				.package-tags {
					display: flex;
					flex-wrap: wrap;
					gap: 10rpx;
					
					.package-tag {
						font-size: 22rpx;
						color: #0984e3;
						background-color: rgba(9, 132, 227, 0.1);
						padding: 6rpx 12rpx;
						border-radius: 8rpx;
						transition: all 0.3s ease;
					}
				}
			}
			
			.package-price-section {
				text-align: right;
				
				.price-main {
					display: flex;
					align-items: baseline;
					justify-content: flex-end;
					margin-bottom: 8rpx;
					
					.price-symbol {
						font-size: 24rpx;
						color: #ff5a5f;
						font-weight: bold;
						margin-right: 4rpx;
					}
					
					.price-value {
						font-size: 48rpx;
						color: #ff5a5f;
						font-weight: bold;
						transition: all 0.3s ease;
						position: relative;
						
						&::after {
							content: '';
							position: absolute;
							bottom: -2rpx;
							left: 0;
							width: 0;
							height: 2rpx;
							background: linear-gradient(90deg, #ff5a5f, #ff6b6b);
							transition: width 0.3s ease;
						}
					}
				}
				
				.price-original {
					font-size: 24rpx;
					color: #999999;
					text-decoration: line-through;
					transition: all 0.3s ease;
					display: block;
					margin-bottom: 8rpx;
				}
				
				.price-discount {
					font-size: 22rpx;
					color: #ffffff;
					background-color: #ff5a5f;
					padding: 6rpx 12rpx;
					border-radius: 20rpx;
					transition: all 0.3s ease;
					position: relative;
					overflow: hidden;
					display: inline-block;
					
					&::before {
						content: '';
						position: absolute;
						top: 0;
						left: -100%;
						width: 100%;
						height: 100%;
						background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
						transition: left 0.6s ease;
					}
					
					&:hover {
						transform: scale(1.1);
						background-color: #ff6b6b;
						
						&::before {
							left: 100%;
						}
					}
				}
			}
		}
		
		.package-desc {
			font-size: 28rpx;
			color: #666666;
			margin-bottom: 25rpx;
			line-height: 1.6;
			transition: all 0.3s ease;
		}
		
		.package-items {
			margin-bottom: 25rpx;
			transition: all 0.3s ease;
			
			.items-header {
				display: flex;
				justify-content: space-between;
				align-items: center;
				margin-bottom: 15rpx;
				
				.items-title {
					font-size: 28rpx;
					color: #333333;
					font-weight: bold;
					transition: all 0.3s ease;
				}
				
				.items-count {
					font-size: 24rpx;
					color: #0984e3;
					background: rgba(9, 132, 227, 0.1);
					padding: 4rpx 12rpx;
					border-radius: 20rpx;
					transition: all 0.3s ease;
				}
			}
			
			.items-preview {
				margin-top: 15rpx;
				
				.preview-text {
					font-size: 26rpx;
					color: #666666;
					text-align: center;
					padding: 20rpx;
					background: rgba(116, 185, 255, 0.05);
					border-radius: 12rpx;
					border: 2rpx dashed rgba(116, 185, 255, 0.2);
					transition: all 0.3s ease;
					
					&:hover {
						background: rgba(116, 185, 255, 0.1);
						border-color: rgba(116, 185, 255, 0.3);
					}
				}
			}
		}
		
		.package-footer {
			display: flex;
			justify-content: space-between;
			align-items: center;
			transition: all 0.3s ease;
			
			.package-stats {
				display: flex;
				align-items: center;
				gap: 20rpx;
				
				.package-sold {
					font-size: 24rpx;
					color: #999999;
					transition: all 0.3s ease;
				}
				
				.package-rating {
					font-size: 24rpx;
					color: #ffa500;
					transition: all 0.3s ease;
				}
			}
			
			.package-actions {
				display: flex;
				gap: 15rpx;
				
				.detail-btn, .select-btn {
					font-size: 24rpx;
					color: #ffffff;
					border: none;
					border-radius: 30rpx;
					padding: 0 24rpx;
					height: 60rpx;
					display: flex;
					align-items: center;
					transition: all 0.3s ease;
					cursor: pointer;
					
					.btn-icon {
						font-size: 24rpx;
						margin-right: 6rpx;
						transition: all 0.3s ease;
					}
					
					.btn-text {
						font-size: 24rpx;
						transition: all 0.3s ease;
					}
					
					&::after {
						border: none;
					}
				}
				
				.detail-btn {
					background: rgba(116, 185, 255, 0.8);
					
					&:hover {
						background: rgba(116, 185, 255, 1);
						transform: translateY(-3rpx) scale(1.05);
						box-shadow: 0 6rpx 20rpx rgba(116, 185, 255, 0.5);
					}
				}
				
				.select-btn {
					background: linear-gradient(135deg, #74b9ff, #0984e3);
					
					&:hover {
						transform: translateY(-3rpx) scale(1.05);
						box-shadow: 0 6rpx 20rpx rgba(116, 185, 255, 0.5);
						background: linear-gradient(135deg, #0984e3, #74b9ff);
					}
				}
			}
		}
	}
}

.empty-state {
	text-align: center;
	padding: 80rpx 40rpx;
	animation: fadeInUp 0.6s ease-out;
	
	.empty-icon {
		font-size: 120rpx;
		margin-bottom: 30rpx;
		animation: bounce 2s infinite;
		transition: all 0.3s ease;
		
		&:hover {
			transform: scale(1.2);
		}
	}
	
	.empty-title {
		font-size: 36rpx;
		color: #333333;
		margin-bottom: 15rpx;
		font-weight: bold;
		transition: all 0.3s ease;
	}
	
	.empty-desc {
		font-size: 28rpx;
		color: #666666;
		margin-bottom: 40rpx;
		transition: all 0.3s ease;
	}
	
	.reset-btn {
		background: linear-gradient(135deg, #74b9ff, #0984e3);
		color: #ffffff;
		border: none;
		border-radius: 50rpx;
		padding: 20rpx 40rpx;
		font-size: 28rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		transition: all 0.3s ease;
		cursor: pointer;
		
		.btn-icon {
			font-size: 28rpx;
			margin-right: 10rpx;
			transition: all 0.3s ease;
		}
		
		.btn-text {
			font-size: 28rpx;
			transition: all 0.3s ease;
		}
		
		&:hover {
			transform: translateY(-3rpx) scale(1.05);
			box-shadow: 0 6rpx 20rpx rgba(116, 185, 255, 0.5);
		}
	}
}

/* 占位符样式 */
.search-placeholder {
	color: #999999;
}

/* Animation keyframes */
@keyframes float {
	0% {
		transform: translateY(0) translateX(0) scale(1);
		opacity: 0.8;
	}
	25% {
		transform: translateY(-10px) translateX(10px) scale(1.05);
		opacity: 0.9;
	}
	50% {
		transform: translateY(5px) translateX(-5px) scale(1.02);
		opacity: 1;
	}
	75% {
		transform: translateY(-5px) translateX(5px) scale(1.03);
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

@keyframes slideInUp {
	from {
		opacity: 0;
		transform: translateY(10rpx);
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
		transform: translateY(-5rpx);
	}
	60% {
		transform: translateY(-3rpx);
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
</style>