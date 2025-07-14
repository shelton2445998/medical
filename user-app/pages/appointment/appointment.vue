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
				<view class="header-icon">🏥</view>
				<view class="header-title">体检预约</view>
				<view class="header-desc">专业体检，健康生活</view>
			</view>
			
			<!-- 搜索框 -->
			<view class="search-section">
				<view class="search-box">
					<text class="search-icon">🔍</text>
					<input 
						class="search-input" 
						type="text" 
						placeholder="搜索医院、体检套餐"
						placeholder-class="search-placeholder"
					/>
				</view>
			</view>
			
			<!-- 轮播图 -->
			<view class="banner-section">
				<swiper class="banner-swiper" circular indicator-dots autoplay interval="3000" duration="500" indicator-active-color="#0984e3">
					<swiper-item v-for="(item, index) in bannerList" :key="index">
						<view class="banner-item" @click="navigateTo(item.url)">
							<image :src="item.image" mode="aspectFill" class="banner-image"></image>
							<view class="banner-overlay">
								<text class="banner-title">{{item.title}}</text>
								<text class="banner-desc">{{item.desc}}</text>
							</view>
						</view>
					</swiper-item>
				</swiper>
			</view>
			
			<!-- 快捷服务 -->
			<view class="quick-service">
				<view class="section-title">快捷服务</view>
				<view class="service-grid">
					<view class="service-item" v-for="(item, index) in serviceList" :key="index" @click="navigateTo(item.url)">
						<view class="service-icon">
							<text class="icon-symbol">{{item.icon}}</text>
						</view>
						<text class="service-name">{{item.name}}</text>
					</view>
				</view>
			</view>
			
			<!-- 推荐医院 -->
			<view class="section">
				<view class="section-header">
					<text class="section-title">推荐医院</text>
					<view class="more" @click="navigateTo('/pages/hospital/hospital')">
						<text>更多</text>
						<text class="more-icon">→</text>
					</view>
				</view>
				<view class="hospital-list">
					<view class="hospital-item" v-for="(item, index) in hospitalList" :key="index" @click="selectHospital(item)">
						<view class="hospital-image">
							<image :src="item.image" mode="aspectFill"></image>
						</view>
						<view class="hospital-info">
							<text class="hospital-name">{{item.name}}</text>
							<view class="hospital-tags">
								<text class="tag" v-for="(tag, tagIndex) in item.tags" :key="tagIndex">{{tag}}</text>
							</view>
							<view class="hospital-address">
								<text class="address-icon">📍</text>
								<text class="address-text">{{item.address}}</text>
							</view>
						</view>
					</view>
				</view>
			</view>
			
			<!-- 热门套餐 -->
			<view class="section">
				<view class="section-header">
					<text class="section-title">热门套餐</text>
					<view class="more" @click="navigateTo('/pages/package/package')">
						<text>更多</text>
						<text class="more-icon">→</text>
					</view>
				</view>
				<view class="package-list">
					<view class="package-item" v-for="(item, index) in packageList" :key="index" @click="selectPackage(item)">
						<view class="package-image">
							<image :src="item.image" mode="aspectFill"></image>
						</view>
						<view class="package-info">
							<text class="package-name">{{item.name}}</text>
							<text class="package-desc">{{item.description}}</text>
							<view class="package-price-box">
								<text class="package-price">¥{{item.price}}</text>
								<text class="package-original-price" v-if="item.originalPrice">¥{{item.originalPrice}}</text>
							</view>
						</view>
					</view>
				</view>
			</view>
			
			<!-- 预约流程 -->
			<view class="section">
				<view class="section-header">
					<text class="section-title">预约流程</text>
				</view>
				<view class="process-list">
					<view class="process-item">
						<view class="process-icon">
							<text class="process-number">1</text>
						</view>
						<text class="process-name">选择医院</text>
					</view>
					<view class="process-arrow">
						<text class="arrow-icon">→</text>
					</view>
					<view class="process-item">
						<view class="process-icon">
							<text class="process-number">2</text>
						</view>
						<text class="process-name">选择套餐</text>
					</view>
					<view class="process-arrow">
						<text class="arrow-icon">→</text>
					</view>
					<view class="process-item">
						<view class="process-icon">
							<text class="process-number">3</text>
						</view>
						<text class="process-name">填写信息</text>
					</view>
					<view class="process-arrow">
						<text class="arrow-icon">→</text>
					</view>
					<view class="process-item">
						<view class="process-icon">
							<text class="process-number">4</text>
						</view>
						<text class="process-name">支付预约</text>
					</view>
				</view>
			</view>
			
			<!-- 预约须知 -->
			<view class="section">
				<view class="section-header">
					<text class="section-title">预约须知</text>
				</view>
				<view class="notice-list">
					<view class="notice-item" v-for="(item, index) in noticeList" :key="index">
						<view class="notice-dot"></view>
						<text class="notice-text">{{item}}</text>
					</view>
				</view>
			</view>
			
			<!-- 底部按钮 -->
			<view class="bottom-btn-container">
				<button class="bottom-btn" @click="navigateTo('/pages/hospital/hospital')">
					<text class="btn-icon">🏥</text>
					<text class="btn-text">立即预约</text>
				</button>
			</view>
		</view>
	</view>
</template>

<script>
import { get, hospitalApi, packageApi } from '@/utils/request.js';
export default {
	data() {
		return {
			bannerList: [
				{ 
					image: '/static/images/banner1.jpg', 
					url: '/pages/package/package',
					title: '专业体检套餐',
					desc: '全面健康检查，专业医疗团队'
				},
				{ 
					image: '/static/images/banner2.jpg', 
					url: '/pages/news/news',
					title: '健康资讯',
					desc: '最新健康资讯，科学养生知识'
				},
				{ 
					image: '/static/images/banner3.jpg', 
					url: '/pages/appointment/appointment',
					title: '在线预约',
					desc: '便捷预约服务，快速体检安排'
				}
			],
			serviceList: [
				{ name: '全身体检', icon: '🏥', url: '/pages/package/package?type=full' },
				{ name: '男性体检', icon: '👨', url: '/pages/package/package?type=male' },
				{ name: '女性体检', icon: '👩', url: '/pages/package/package?type=female' },
				{ name: '老年体检', icon: '👴', url: '/pages/package/package?type=elder' },
				{ name: '儿童体检', icon: '👶', url: '/pages/package/package?type=child' }
			],
			hospitalList: [],
			packageList: [],
			noticeList: [
				'体检前一天请清淡饮食，避免辛辣、油腻食物',
				'体检当天请空腹，禁食8-12小时',
				'体检前一天晚上请保证充足睡眠',
				'体检当天请携带身份证等有效证件',
				'体检报告一般在3-5个工作日出具，可在APP查看'
			]
		}
	},
	onLoad() {
		this.getRecommendHospitals();
		this.getRecommendPackages();
	},
	methods: {
		async getRecommendHospitals() {
			try {
				const result = await get(hospitalApi.getRecommendHospitals);
				if (result && result.data) {
					this.hospitalList = result.data.map((hospital, index) => {
						const defaultImages = [
							'/static/images/hospital1.jpg',
							'/static/images/hospital2.jpg',
							'/static/images/hospital3.jpg',
							'/static/images/hospital4.jpg'
						];
						return {
							...hospital,
							image: defaultImages[index % defaultImages.length],
							tags: hospital.tags || ['综合医院'],
							address: hospital.address || '地址信息待完善'
						};
					});
				} else {
					this.hospitalList = [];
				}
			} catch (e) {
				this.hospitalList = [];
			}
		},
		async getRecommendPackages() {
			try {
				const result = await get(packageApi.getRecommendPackages);
				if (result && result.data) {
					this.packageList = result.data.map((item, index) => ({
						id: item.id,
						name: item.name,
						price: item.price || 0,
						originalPrice: item.originalPrice || item.price || 0,
						description: item.description || '',
						tags: item.tags || [],
						image: `/static/images/package${(index % 4) + 1}.jpg`
					}));
				} else {
					this.packageList = [];
				}
			} catch (e) {
				this.packageList = [];
			}
		},
		navigateTo(url) {
			uni.navigateTo({ url });
		},
		selectHospital(hospital) {
			uni.navigateTo({ url: `/pages/hospital-detail/hospital-detail?id=${hospital.id}` });
		},
		selectPackage(pkg) {
			uni.navigateTo({ url: `/pages/package-detail/package-detail?id=${pkg.id}` });
		}
	}
}
</script>

<style lang="scss">
.content {
	position: relative;
	min-height: 100vh;
	background-color: #f0f2f5;
	overflow-x: hidden;
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
		background-color: #0984e3;
		border-radius: 50%;
		opacity: 0.1;
	}
	
	.shape-1 {
		width: 100px;
		height: 100px;
		top: 10%;
		left: 10%;
	}
	
	.shape-2 {
		width: 150px;
		height: 150px;
		bottom: 20%;
		right: 20%;
	}
	
	.shape-3 {
		width: 200px;
		height: 200px;
		top: 50%;
		left: 30%;
	}
	
	.shape-4 {
		width: 120px;
		height: 120px;
		bottom: 10%;
		right: 50%;
	}
}

.main-content {
	padding: 20rpx;
}

.page-header {
	text-align: center;
	padding: 40rpx 0;
	
	.header-icon {
		font-size: 100rpx;
		color: #0984e3;
		margin-bottom: 10rpx;
	}
	
	.header-title {
		font-size: 48rpx;
		font-weight: bold;
		color: #333333;
		margin-bottom: 10rpx;
	}
	
	.header-desc {
		font-size: 28rpx;
		color: #666666;
	}
}

.search-section {
	margin-top: 30rpx;
	margin-bottom: 20rpx;
	
	.search-box {
		display: flex;
		align-items: center;
		height: 80rpx;
		background-color: #ffffff;
		border-radius: 40rpx;
		padding: 0 30rpx;
		box-shadow: 0 4rpx 15rpx rgba(0, 0, 0, 0.08);
		
		.search-icon {
			font-size: 40rpx;
			color: #999999;
			margin-right: 15rpx;
		}
		
		.search-input {
			flex: 1;
			height: 100%;
			font-size: 32rpx;
			color: #333333;
		}
		
		.search-placeholder {
			color: #999999;
		}
	}
}

.banner-section {
	margin-bottom: 30rpx;
	
	.banner-swiper {
		width: 100%;
		height: 350rpx;
		border-radius: 20rpx;
		overflow: hidden;
		box-shadow: 0 8rpx 20rpx rgba(0, 0, 0, 0.1);
		
		.banner-item {
			position: relative;
			width: 100%;
			height: 100%;
			
			.banner-image {
				width: 100%;
				height: 100%;
				object-fit: cover;
			}
			
			.banner-overlay {
				position: absolute;
				bottom: 0;
				left: 0;
				width: 100%;
				padding: 20rpx;
				background: linear-gradient(to top, rgba(0, 0, 0, 0.6), transparent);
				border-bottom-left-radius: 20rpx;
				border-bottom-right-radius: 20rpx;
				
				.banner-title {
					font-size: 36rpx;
					font-weight: bold;
					color: #ffffff;
					margin-bottom: 5rpx;
				}
				
				.banner-desc {
					font-size: 24rpx;
					color: #ffffff;
					opacity: 0.9;
				}
			}
		}
	}
}

.quick-service {
	margin-bottom: 30rpx;
	background-color: #ffffff;
	border-radius: 15rpx;
	padding: 20rpx;
	box-shadow: 0 4rpx 15rpx rgba(0, 0, 0, 0.08);
	
	.section-title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333333;
		margin-bottom: 20rpx;
		padding-left: 10rpx;
		
		&::before {
			content: '';
			position: absolute;
			left: 0;
			top: 50%;
			transform: translateY(-50%);
			width: 6rpx;
			height: 30rpx;
			background-color: #0984e3;
			border-radius: 3rpx;
		}
	}
	
	.service-grid {
		display: grid;
		grid-template-columns: repeat(auto-fill, minmax(150rpx, 1fr));
		gap: 20rpx;
	}
	
	.service-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 15rpx 0;
		
		.service-icon {
			width: 80rpx;
			height: 80rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			background-color: #e0f2fe;
			border-radius: 50%;
			margin-bottom: 10rpx;
			
			.icon-symbol {
				font-size: 40rpx;
				color: #0984e3;
			}
		}
		
		.service-name {
			font-size: 26rpx;
			color: #333333;
			text-align: center;
		}
	}
}

.section {
	margin-bottom: 30rpx;
	background-color: #ffffff;
	border-radius: 15rpx;
	padding: 20rpx;
	box-shadow: 0 4rpx 15rpx rgba(0, 0, 0, 0.08);
	
	.section-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 20rpx;
		
		.section-title {
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
				background-color: #0984e3;
				border-radius: 3rpx;
			}
		}
		
		.more {
			display: flex;
			align-items: center;
			font-size: 26rpx;
			color: #999999;
			
			.more-icon {
				font-size: 24rpx;
				margin-left: 5rpx;
			}
		}
	}
}

.hospital-list {
	.hospital-item {
		display: flex;
		margin-bottom: 30rpx;
		
		&:last-child {
			margin-bottom: 0;
		}
		
		.hospital-image {
			width: 180rpx;
			height: 140rpx;
			border-radius: 10rpx;
			margin-right: 20rpx;
			overflow: hidden;
			
			image {
				width: 100%;
				height: 100%;
				object-fit: cover;
			}
		}
		
		.hospital-info {
			flex: 1;
			display: flex;
			flex-direction: column;
			justify-content: space-between;
			
			.hospital-name {
				font-size: 30rpx;
				font-weight: bold;
				color: #333333;
				margin-bottom: 10rpx;
			}
			
			.hospital-tags {
				display: flex;
				flex-wrap: wrap;
				margin-bottom: 10rpx;
				
				.tag {
					font-size: 22rpx;
					color: #0984e3;
					background-color: rgba(9, 132, 227, 0.1);
					padding: 4rpx 12rpx;
					border-radius: 6rpx;
					margin-right: 10rpx;
					margin-bottom: 10rpx;
				}
			}
			
			.hospital-address {
				display: flex;
				align-items: center;
				font-size: 24rpx;
				color: #999999;
				
				.address-icon {
					font-size: 24rpx;
					margin-right: 6rpx;
				}
				
				.address-text {
					white-space: nowrap;
					overflow: hidden;
					text-overflow: ellipsis;
				}
			}
		}
	}
}

.package-list {
	display: flex;
	flex-wrap: nowrap;
	overflow-x: scroll;
	margin: 0 -20rpx;
	padding: 0 20rpx;
	
	&::-webkit-scrollbar {
		display: none;
	}
	
	.package-item {
		flex: 0 0 300rpx;
		margin-right: 20rpx;
		border-radius: 10rpx;
		overflow: hidden;
		box-shadow: 0 4rpx 15rpx rgba(0, 0, 0, 0.08);
		
		&:last-child {
			margin-right: 0;
		}
		
		.package-image {
			width: 100%;
			height: 180rpx;
			
			image {
				width: 100%;
				height: 100%;
				object-fit: cover;
			}
		}
		
		.package-info {
			padding: 15rpx;
			
			.package-name {
				font-size: 28rpx;
				font-weight: bold;
				color: #333333;
				margin-bottom: 10rpx;
				white-space: nowrap;
				overflow: hidden;
				text-overflow: ellipsis;
			}
			
			.package-desc {
				font-size: 24rpx;
				color: #666666;
				margin-bottom: 10rpx;
				height: 68rpx;
				display: -webkit-box;
				-webkit-box-orient: vertical;
				-webkit-line-clamp: 2;
				overflow: hidden;
				text-overflow: ellipsis;
			}
			
			.package-price-box {
				display: flex;
				align-items: baseline;
				
				.package-price {
					font-size: 32rpx;
					font-weight: bold;
					color: #ff5a5f;
					margin-right: 10rpx;
				}
				
				.package-original-price {
					font-size: 24rpx;
					color: #999999;
					text-decoration: line-through;
				}
			}
		}
	}
}

.process-list {
	display: flex;
	align-items: center;
	justify-content: space-between;
	padding: 20rpx 0;
	
	.process-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		
		.process-icon {
			position: relative;
			width: 80rpx;
			height: 80rpx;
			margin-bottom: 15rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			background-color: #e0f2fe;
			border-radius: 50%;
			
			.process-number {
				position: absolute;
				top: -10rpx;
				right: -10rpx;
				width: 36rpx;
				height: 36rpx;
				line-height: 36rpx;
				text-align: center;
				background-color: #0984e3;
				color: #ffffff;
				font-size: 22rpx;
				border-radius: 50%;
			}
		}
		
		.process-name {
			font-size: 24rpx;
			color: #333333;
		}
	}
	
	.process-arrow {
		color: #cccccc;
		
		.arrow-icon {
			font-size: 24rpx;
		}
	}
}

.notice-list {
	padding: 10rpx 0;
	
	.notice-item {
		display: flex;
		align-items: flex-start;
		margin-bottom: 15rpx;
		
		&:last-child {
			margin-bottom: 0;
		}
		
		.notice-dot {
			width: 12rpx;
			height: 12rpx;
			border-radius: 50%;
			background-color: #0984e3;
			margin-right: 15rpx;
			margin-top: 12rpx;
		}
		
		.notice-text {
			flex: 1;
			font-size: 26rpx;
			color: #666666;
			line-height: 1.6;
		}
	}
}

.bottom-btn-container {
	position: fixed;
	left: 0;
	right: 0;
	bottom: 100rpx;
	padding: 20rpx;
	background-color: #ffffff;
	box-shadow: 0 -4rpx 15rpx rgba(0, 0, 0, 0.08);
	z-index: 999;
	
	.bottom-btn {
		height: 90rpx;
		line-height: 90rpx;
		background: linear-gradient(to right, #0984e3, #00b894);
		color: #ffffff;
		font-size: 32rpx;
		font-weight: bold;
		border-radius: 45rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		
		&::after {
			border: none;
		}
		
		.btn-icon {
			font-size: 36rpx;
			margin-right: 10rpx;
		}
		
		.btn-text {
			font-size: 32rpx;
		}
	}
}
</style> 