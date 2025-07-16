<template>
	<view class="content">
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
							<text class="process-icon-text">🏥</text>
							<text class="process-number">1</text>
						</view>
						<text class="process-name">选择医院</text>
					</view>
					<view class="process-arrow">
						<text class="arrow-icon">→</text>
					</view>
					<view class="process-item">
						<view class="process-icon">
							<text class="process-icon-text">📋</text>
							<text class="process-number">2</text>
						</view>
						<text class="process-name">选择套餐</text>
					</view>
					<view class="process-arrow">
						<text class="arrow-icon">→</text>
					</view>
					<view class="process-item">
						<view class="process-icon">
							<text class="process-icon-text">✏️</text>
							<text class="process-number">3</text>
						</view>
						<text class="process-name">填写信息</text>
					</view>
					<view class="process-arrow">
						<text class="arrow-icon">→</text>
					</view>
					<view class="process-item">
						<view class="process-icon">
							<text class="process-icon-text">💳</text>
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
					this.hospitalList = result.data.slice(0, 3).map((hospital, index) => {
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
			// 存储选择的医院信息
			uni.setStorageSync('selectedHospital', JSON.stringify(hospital));
			
			uni.navigateTo({ 
				url: `/pages/hospital-detail/hospital-detail?id=${hospital.id}` 
			});
		},
		selectPackage(pkg) {
			uni.navigateTo({ url: `/pages/package-detail/package-detail?id=${pkg.id}` });
		}
	}
}
</script>

<style lang="scss" scoped>
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

.main-content {
	padding: 20rpx 40rpx 0 40rpx;
	position: relative;
	z-index: 1;
}

.page-header {
	text-align: center;
	margin-bottom: 20rpx;
	animation: fadeInDown 0.8s ease-out;
	
	.header-icon {
		font-size: 40rpx;
		margin-bottom: 10rpx;
		animation: bounce 2s infinite;
		transition: all 0.3s ease;
		
		&:hover {
			transform: scale(1.2);
		}
	}
	
	.header-title {
		font-size: 22rpx;
		font-weight: bold;
		color: #ffffff;
		margin-bottom: 8rpx;
		text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.3);
		transition: all 0.3s ease;
	}
	
	.header-desc {
		font-size: 14rpx;
		color: rgba(255, 255, 255, 0.8);
		transition: all 0.3s ease;
	}
}

.search-section {
	margin-bottom: 30rpx;
	animation: fadeInUp 0.8s ease-out 0.1s both;
	
	.search-box {
		display: flex;
		align-items: center;
		height: 80rpx;
		background: rgba(255, 255, 255, 0.95);
		border-radius: 40rpx;
		padding: 0 30rpx;
		box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
		backdrop-filter: blur(10rpx);
		transition: all 0.3s ease;
		
		&:hover {
			transform: translateY(-2rpx);
			box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.15);
		}
		
		.search-icon {
			font-size: 40rpx;
			color: #0984e3;
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
	animation: fadeInUp 0.8s ease-out 0.2s both;
	
	.banner-swiper {
		width: 100%;
		height: 350rpx;
		border-radius: 24rpx;
		overflow: hidden;
		box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
		transition: all 0.3s ease;
		
		&:hover {
			transform: translateY(-4rpx);
			box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.15);
		}
		
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
				border-bottom-left-radius: 24rpx;
				border-bottom-right-radius: 24rpx;
				
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
	background: rgba(255, 255, 255, 0.95);
	border-radius: 24rpx;
	padding: 40rpx;
	box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
	backdrop-filter: blur(10rpx);
	transition: all 0.3s ease;
	animation: fadeInUp 0.8s ease-out 0.3s both;
	position: relative;
	overflow: hidden;
	
	&::before {
		content: '';
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: 
			radial-gradient(circle at 10% 10%, rgba(9, 132, 227, 0.02) 0%, transparent 50%),
			radial-gradient(circle at 90% 90%, rgba(116, 185, 255, 0.02) 0%, transparent 50%);
		pointer-events: none;
		animation: gentleFlow 10s ease-in-out infinite;
	}
	
	&::after {
		content: '';
		position: absolute;
		top: 20rpx;
		right: 20rpx;
		width: 60rpx;
		height: 60rpx;
		background: linear-gradient(135deg, rgba(9, 132, 227, 0.1), rgba(116, 185, 255, 0.1));
		border-radius: 50%;
		animation: float 6s ease-in-out infinite;
	}
	
	&:hover {
		transform: translateY(-4rpx);
		box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.15);
	}
	
	.section-title {
		font-size: 32rpx;
		font-weight: bold;
		color: #333333;
		margin-bottom: 30rpx;
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
			animation: gentlePulse 3s ease-in-out infinite;
		}
	}
	
	.service-grid {
		display: grid;
		grid-template-columns: repeat(2, 1fr);
		gap: 30rpx;
		transition: all 0.3s ease;
	}
	
	.service-item {
		display: flex;
		flex-direction: column;
		align-items: center;
		padding: 25rpx 15rpx;
		transition: all 0.3s ease;
		border-radius: 16rpx;
		background: rgba(255, 255, 255, 0.8);
		backdrop-filter: blur(5rpx);
		
		&:hover {
			transform: translateY(-4rpx);
			background: rgba(255, 255, 255, 0.95);
			box-shadow: 0 8rpx 24rpx rgba(9, 132, 227, 0.15);
		}
		
		.service-icon {
			width: 90rpx;
			height: 90rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			background: linear-gradient(135deg, #e0f2fe, #b3e5fc);
			border-radius: 50%;
			margin-bottom: 15rpx;
			transition: all 0.3s ease;
			box-shadow: 0 4rpx 16rpx rgba(9, 132, 227, 0.2);
			
			&:hover {
				transform: scale(1.1);
				box-shadow: 0 8rpx 24rpx rgba(9, 132, 227, 0.3);
			}
			
			.icon-symbol {
				font-size: 40rpx;
			}
		}
		
		.service-name {
			font-size: 28rpx;
			color: #333333;
			text-align: center;
			font-weight: 500;
			line-height: 1.2;
		}
	}
}

.section {
	margin-bottom: 30rpx;
	background: linear-gradient(135deg, rgba(255, 255, 255, 0.95), rgba(248, 250, 255, 0.9));
	border-radius: 24rpx;
	padding: 40rpx;
	box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
	backdrop-filter: blur(10rpx);
	transition: all 0.3s ease;
	animation: fadeInUp 0.8s ease-out 0.4s both;
	position: relative;
	overflow: hidden;
	
	&::before {
		content: '';
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: 
			radial-gradient(circle at 20% 20%, rgba(9, 132, 227, 0.03) 0%, transparent 50%),
			radial-gradient(circle at 80% 80%, rgba(116, 185, 255, 0.03) 0%, transparent 50%),
			linear-gradient(45deg, transparent 40%, rgba(9, 132, 227, 0.01) 50%, transparent 60%),
			linear-gradient(-45deg, transparent 30%, rgba(116, 185, 255, 0.01) 40%, transparent 50%);
		pointer-events: none;
		animation: gentleFlow 8s ease-in-out infinite;
	}
	
	&:hover {
		transform: translateY(-4rpx);
		box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.15);
	}
	
	.section-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 30rpx;
		
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
				background: linear-gradient(135deg, #0984e3, #74b9ff);
				border-radius: 3rpx;
			}
		}
		
		.more {
			display: flex;
			align-items: center;
			font-size: 26rpx;
			color: #0984e3;
			transition: all 0.3s ease;
			
			&:hover {
				transform: translateX(4rpx);
			}
			
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
		margin-bottom: 25rpx;
		padding: 20rpx;
		border-radius: 16rpx;
		background: linear-gradient(135deg, rgba(255, 255, 255, 0.9), rgba(240, 248, 255, 0.8));
		backdrop-filter: blur(10rpx);
		border: 1rpx solid rgba(9, 132, 227, 0.1);
		transition: all 0.3s ease;
		position: relative;
		overflow: hidden;
		
		&::before {
			content: '';
			position: absolute;
			top: 0;
			left: 0;
			width: 4rpx;
			height: 100%;
			background: linear-gradient(135deg, #0984e3, #74b9ff);
			border-radius: 2rpx;
			box-shadow: 0 0 8rpx rgba(9, 132, 227, 0.3);
		}
		
		&:last-child {
			margin-bottom: 0;
		}
		
		&:hover {
			transform: translateX(8rpx) translateY(-2rpx);
			box-shadow: 0 8rpx 24rpx rgba(9, 132, 227, 0.15);
			background: linear-gradient(135deg, rgba(255, 255, 255, 0.95), rgba(240, 248, 255, 0.9));
		}
		
		.hospital-image {
			width: 120rpx;
			height: 90rpx;
			border-radius: 12rpx;
			margin-right: 20rpx;
			overflow: hidden;
			box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
			transition: all 0.3s ease;
			position: relative;
			
			&::after {
				content: '';
				position: absolute;
				top: 0;
				left: 0;
				right: 0;
				bottom: 0;
				background: linear-gradient(45deg, transparent, rgba(255, 255, 255, 0.1));
				pointer-events: none;
			}
			
			&:hover {
				transform: scale(1.05);
				box-shadow: 0 8rpx 24rpx rgba(9, 132, 227, 0.2);
			}
			
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
				font-size: 28rpx;
				font-weight: bold;
				color: #333333;
				margin-bottom: 10rpx;
				white-space: nowrap;
				overflow: hidden;
				text-overflow: ellipsis;
			}
			
			.hospital-tags {
				display: flex;
				flex-wrap: wrap;
				margin-bottom: 10rpx;
				
				.tag {
					font-size: 20rpx;
					color: #0984e3;
					background: linear-gradient(135deg, rgba(9, 132, 227, 0.1), rgba(116, 185, 255, 0.1));
					padding: 6rpx 12rpx;
					border-radius: 12rpx;
					margin-right: 8rpx;
					margin-bottom: 6rpx;
					transition: all 0.3s ease;
					border: 1rpx solid rgba(9, 132, 227, 0.2);
					
					&:hover {
						background: linear-gradient(135deg, rgba(9, 132, 227, 0.2), rgba(116, 185, 255, 0.2));
						transform: scale(1.05);
						box-shadow: 0 2rpx 8rpx rgba(9, 132, 227, 0.2);
					}
				}
			}
			
			.hospital-address {
				display: flex;
				align-items: center;
				font-size: 22rpx;
				color: #666666;
				
				.address-icon {
					font-size: 20rpx;
					margin-right: 6rpx;
					color: #0984e3;
				}
				
				.address-text {
					white-space: nowrap;
					overflow: hidden;
					text-overflow: ellipsis;
					max-width: 200rpx;
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
		flex: 0 0 320rpx;
		margin-right: 25rpx;
		border-radius: 20rpx;
		overflow: hidden;
		background: linear-gradient(135deg, rgba(255, 255, 255, 0.95), rgba(240, 248, 255, 0.9));
		backdrop-filter: blur(10rpx);
		border: 1rpx solid rgba(9, 132, 227, 0.1);
		box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
		transition: all 0.4s ease;
		position: relative;
		
		&::before {
			content: '';
			position: absolute;
			top: 0;
			left: 0;
			right: 0;
			height: 4rpx;
			background: linear-gradient(90deg, #0984e3, #74b9ff, #0984e3);
			background-size: 200% 100%;
			animation: shimmer 3s ease-in-out infinite;
		}
		
		&:last-child {
			margin-right: 0;
		}
		
		&:hover {
			transform: translateY(-12rpx) scale(1.02);
			box-shadow: 0 16rpx 48rpx rgba(9, 132, 227, 0.2);
			background: linear-gradient(135deg, rgba(255, 255, 255, 1), rgba(240, 248, 255, 0.95));
		}
		
		.package-image {
			width: 100%;
			height: 200rpx;
			position: relative;
			overflow: hidden;
			
			&::after {
				content: '';
				position: absolute;
				bottom: 0;
				left: 0;
				right: 0;
				height: 60rpx;
				background: linear-gradient(to top, rgba(0, 0, 0, 0.3), transparent);
			}
			
			image {
				width: 100%;
				height: 100%;
				object-fit: cover;
				transition: transform 0.4s ease;
			}
			
			&:hover image {
				transform: scale(1.1);
			}
		}
		
		.package-info {
			padding: 25rpx;
			position: relative;
			
			.package-name {
				font-size: 30rpx;
				font-weight: bold;
				color: #333333;
				margin-bottom: 12rpx;
				white-space: nowrap;
				overflow: hidden;
				text-overflow: ellipsis;
			}
			
			.package-desc {
				font-size: 26rpx;
				color: #666666;
				margin-bottom: 20rpx;
				height: 72rpx;
				display: -webkit-box;
				-webkit-box-orient: vertical;
				-webkit-line-clamp: 2;
				overflow: hidden;
				text-overflow: ellipsis;
				line-height: 1.4;
			}
			
			.package-price-box {
				display: flex;
				align-items: baseline;
				justify-content: space-between;
				
				.package-price {
					font-size: 36rpx;
					font-weight: bold;
					color: #ff5a5f;
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
		transition: all 0.3s ease;
		
		&:hover {
			transform: translateY(-4rpx);
		}
		
		.process-icon {
			position: relative;
			width: 70rpx;
			height: 70rpx;
			margin-bottom: 15rpx;
			display: flex;
			align-items: center;
			justify-content: center;
			background: linear-gradient(135deg, #e0f2fe, #b3e5fc);
			border-radius: 50%;
			box-shadow: 0 4rpx 16rpx rgba(9, 132, 227, 0.2);
			transition: all 0.3s ease;
			
			&:hover {
				transform: scale(1.1);
				box-shadow: 0 8rpx 24rpx rgba(9, 132, 227, 0.3);
			}
			
			.process-icon-text {
				font-size: 28rpx;
				color: #0984e3;
			}
			
			.process-number {
				position: absolute;
				top: -8rpx;
				right: -8rpx;
				width: 28rpx;
				height: 28rpx;
				line-height: 28rpx;
				text-align: center;
				background: linear-gradient(135deg, #0984e3, #74b9ff);
				color: #ffffff;
				font-size: 18rpx;
				border-radius: 50%;
				box-shadow: 0 2rpx 8rpx rgba(9, 132, 227, 0.3);
			}
		}
		
		.process-name {
			font-size: 24rpx;
			color: #333333;
			font-weight: 500;
			text-align: center;
		}
	}
	
	.process-arrow {
		color: #cccccc;
		
		.arrow-icon {
			font-size: 20rpx;
		}
	}
}

.notice-list {
	padding: 10rpx 0;
	
	.notice-item {
		display: flex;
		align-items: flex-start;
		margin-bottom: 20rpx;
		transition: all 0.3s ease;
		
		&:last-child {
			margin-bottom: 0;
		}
		
		&:hover {
			transform: translateX(8rpx);
		}
		
		.notice-dot {
			width: 12rpx;
			height: 12rpx;
			border-radius: 50%;
			background: linear-gradient(135deg, #0984e3, #74b9ff);
			margin-right: 15rpx;
			margin-top: 12rpx;
			box-shadow: 0 2rpx 8rpx rgba(9, 132, 227, 0.3);
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
	background: rgba(255, 255, 255, 0.95);
	backdrop-filter: blur(10rpx);
	box-shadow: 0 -8rpx 32rpx rgba(0, 0, 0, 0.1);
	z-index: 999;
	animation: fadeInUp 0.8s ease-out 0.5s both;
	
	.bottom-btn {
		height: 90rpx;
		line-height: 90rpx;
		background: linear-gradient(135deg, #0984e3, #74b9ff);
		color: #ffffff;
		font-size: 32rpx;
		font-weight: bold;
		border-radius: 45rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 8rpx 24rpx rgba(9, 132, 227, 0.3);
		transition: all 0.3s ease;
		
		&::after {
			border: none;
		}
		
		&:hover {
			transform: translateY(-4rpx);
			box-shadow: 0 12rpx 32rpx rgba(9, 132, 227, 0.4);
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

// 动画定义
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

@keyframes bounce {
	0%, 20%, 50%, 80%, 100% {
		transform: translateY(0);
	}
	40% {
		transform: translateY(-10rpx);
	}
	60% {
		transform: translateY(-5rpx);
	}
}

@keyframes gentleFlow {
	0% {
		transform: translate(0, 0);
	}
	50% {
		transform: translate(10px, 10px);
	}
	100% {
		transform: translate(0, 0);
	}
}
</style> 