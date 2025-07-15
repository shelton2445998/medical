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
			
			<!-- 顶部搜索栏 -->
			<view class="search-section">
				<view class="search-box">
					<text class="search-icon">🔍</text>
					<input type="text" placeholder="搜索医院、体检套餐" v-model="searchKeyword" @input="searchHospitals" />
				</view>
			</view>

			<!-- 轮播图 -->
			<view class="banner-section">
				<swiper class="banner-swiper" circular indicator-dots autoplay interval="3000" duration="500" indicator-active-color="#0984e3">
					<swiper-item v-for="(item, index) in bannerList" :key="index">
						<view class="banner-item" @click="navigateTo(item.url)">
							<image :src="item.image" mode="aspectFill" class="banner-image"></image>
							<view class="banner-overlay">
								<text class="banner-title">健康体检</text>
								<text class="banner-desc">专业医疗团队为您服务</text>
							</view>
						</view>
					</swiper-item>
				</swiper>
			</view>

			<!-- 快捷服务 -->
			<view class="quick-service">
				<view class="service-header">
					<text class="section-title">快捷服务</text>
					<view class="layout-toggle" @click="toggleLayout">
						<text class="toggle-text">{{ layoutMode === 2 ? '2列' : '4列' }}</text>
						<text class="toggle-icon">🔄</text>
					</view>
				</view>
				<view class="service-grid" :class="{ 'service-grid-four': layoutMode === 4 }">
					<view class="service-item" v-for="(item, index) in serviceList" :key="index" @click="navigateTo(item.url)">
						<view class="service-icon">
							<image :src="item.icon" mode="aspectFit" class="service-icon-img"></image>
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
					<view class="hospital-item" v-for="(item, index) in hospitalList.slice(0, 3)" :key="index" @click="selectHospital(item)">
						<view class="hospital-image">
							<image :src="item.image || '/static/images/hospital1.jpg'" mode="aspectFill"></image>
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
				<!-- 加载状态 -->
				<view class="loading-container" v-if="hospitalLoading">
					<text class="loading-text">加载中...</text>
				</view>
				<!-- 空状态 -->
				<view class="empty-container" v-if="!hospitalLoading && hospitalList.length === 0">
					<text class="empty-text">暂无推荐医院</text>
				</view>
			</view>

			<!-- 推荐套餐 -->
			<view class="section">
				<view class="section-header">
					<text class="section-title">热门套餐</text>
					<view class="more" @click="navigateTo('/pages/package/package')">
						<text>更多</text>
						<text class="more-icon">→</text>
					</view>
				</view>
				<view class="package-list">
					<view class="package-item" v-for="(item, index) in recommendPackages" :key="index" @click="selectPackage(item)">
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

			<!-- 健康资讯 -->
			<view class="section">
				<view class="section-header">
					<text class="section-title">健康资讯</text>
					<view class="more" @click="navigateTo('/pages/news/news')">
						<text>更多</text>
						<text class="more-icon">→</text>
					</view>
				</view>
				<view class="news-list">
					<view class="news-item" v-for="(item, index) in newsList" :key="index" @click="viewNews(item)">
						<view class="news-image">
							<image :src="item.image" mode="aspectFill"></image>
						</view>
						<view class="news-info">
							<text class="news-title">{{item.title}}</text>
							<view class="news-meta">
								<text class="news-source">{{item.source}}</text>
								<text class="news-time">{{item.time}}</text>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import { get, hospitalApi, packageApi } from '@/utils/request.js';
	
	export default {
		data() {
			return {
				// 搜索关键词
				searchKeyword: '',
				// 医院加载状态
				hospitalLoading: false,
				// 医院列表
				hospitalList: [],
				// 基础数据
				bannerList: [{
						image: '/static/images/banner1.jpg',
						url: '/pages/package/package'
					},
					{
						image: '/static/images/banner2.jpg',
						url: '/pages/news/news'
					},
					{
						image: '/static/images/banner3.jpg',
						url: '/pages/appointment/appointment'
					}
				],
				serviceList: [{
						name: '体检预约',
						icon: '/static/images/icon-appointment.png',
						url: '/pages/appointment/appointment'
					},
					{
						name: '体检报告',
						icon: '/static/images/icon-report.png',
						url: '/pages/report/report'
					},
					{
						name: '我的预约',
						icon: '/static/images/icon-my-appointment.png',
						url: '/pages/my-appointment/my-appointment'
					},
					{
						name: '在线咨询',
						icon: '/static/images/icon-consult.png',
						url: '/pages/consult/consult'
					}
				],
				packageList: [{
						id: 1,
						name: '标准体检套餐',
						image: '/static/images/package1.jpg',
						description: '适合25-45岁人群，包含血常规、尿常规、肝功能等基础检查',
						price: '299',
						originalPrice: '399'
					},
					{
						id: 2,
						name: '高级体检套餐',
						image: '/static/images/package2.jpg',
						description: '适合45岁以上人群，包含心脑血管、肿瘤筛查等全面检查',
						price: '699',
						originalPrice: '899'
					}
				],
				newsList: [{
						id: 1,
						title: '每天一个苹果，医生远离我？水果的健康真相',
						image: '/static/images/news1.jpg',
						source: '健康时报',
						time: '2023-07-08',
						type: 3,
						url: 'https://www.thepaper.cn/newsDetail_forward_16646201'
					},
					{
						id: 2,
						title: '夏季养生指南：这些食物帮你清热解暑',
						image: '/static/images/news2.jpg',
						source: '生活健康',
						time: '2023-07-06',
						type: 1,
						url: "https://foodmate.net/foodsafe/health/170514.html"
					},
					{
						id: 3,
						title: '中年人体检必查的5项指标，你都了解吗？',
						image: '/static/images/news3.jpg',
						source: '医学科普',
						time: '2023-07-05',
						type: 2,
						url: 'https://www.163.com/dy/article/I6JGCGOQ0552CRD4.html'
					}
				],
				recommendPackages: [],
				// 布局模式 (2 或 4)
				layoutMode: 2,
			}
		},
		// 页面标题配置
		onLoad() {
			// 设置页面标题
			uni.setNavigationBarTitle({
				title: '杏林春暖·健康预约'
			});
			
			// 页面加载时获取推荐医院
			this.getRecommendHospitals();
			this.getRecommendPackages();
		},
		methods: {
			// 获取推荐医院列表
			async getRecommendHospitals() {
				this.hospitalLoading = true;
				
				try {
					const result = await get(hospitalApi.getRecommendHospitals);
					
					// 检查返回的数据结构
					if (result && result.data) {
						this.hospitalList = result.data;
					} else if (Array.isArray(result)) {
						this.hospitalList = result;
					} else {
						this.hospitalList = [];
					}
					// 处理医院数据，确保有默认图片和标签
					this.hospitalList.forEach((hospital, index) => {
						// 设置默认图片（使用多张图片循环）
						const defaultImages = [
							'/static/images/hospital1.jpg',
							'/static/images/hospital2.jpg',
							'/static/images/hospital3.jpg',
							'/static/images/hospital4.jpg'
						];
						hospital.image = defaultImages[index % defaultImages.length];
						// 设置默认标签
						hospital.tags = ['综合医院'];
						// 确保地址字段存在
						if (!hospital.address) {
							hospital.address = '地址信息待完善';
						}
					});
				} catch (error) {
					// 如果接口失败，使用测试数据
					this.hospitalList = [
						{
							id: 1,
							name: '华夏健康体检中心-总院',
							image: '/static/images/hospital1.jpg',
							tags: ['三甲', '综合医院'],
							address: '沈阳市和平区南京南街61号'
						},
						{
							id: 2,
							name: '华夏健康体检中心-分院',
							image: '/static/images/hospital2.jpg',
							tags: ['三甲', '综合医院'],
							address: '沈阳市沈河区北站路33号'
						}
					];
					uni.showToast({
						title: '使用测试数据',
						icon: 'none'
					});
				} finally {
					this.hospitalLoading = false;
				}
			},
			
			// 搜索医院
			async searchHospitals() {
				if (!this.searchKeyword.trim()) {
					// 如果搜索关键词为空，重新获取推荐医院
					this.getRecommendHospitals();
					return;
				}
				
				try {
					const result = await get(hospitalApi.getHospitalList, {
						keyword: this.searchKeyword.trim(),
						pageIndex: 1,
						pageSize: 10
					});
					
					// 检查返回的数据结构
					let hospitalData = null;
					if (result && result.data) {
						hospitalData = result.data;
					} else if (result && result.records) {
						hospitalData = result;
					}
					
					if (hospitalData && hospitalData.records) {
						this.hospitalList = hospitalData.records;
						// 处理医院数据
						this.hospitalList.forEach((hospital, index) => {
							// 设置默认图片（使用多张图片循环）
							const defaultImages = [
								'/static/images/hospital1.jpg',
								'/static/images/hospital2.jpg',
								'/static/images/hospital3.jpg',
								'/static/images/hospital4.jpg'
							];
							hospital.image = defaultImages[index % defaultImages.length];
							// 设置默认标签
							hospital.tags = ['综合医院'];
							// 确保地址字段存在
							if (!hospital.address) {
								hospital.address = '地址信息待完善';
							}
						});
					}
				} catch (error) {
					uni.showToast({
						title: error.message || '搜索医院失败',
						icon: 'none'
					});
				}
			},
			
			navigateTo(url) {
				// 如果是 tabBar 页面，改用 switchTab
				if (url === '/pages/report/report') {
					// 体检报告跳转到指定URL
					// #ifdef H5
					window.location.href = 'http://localhost:8080/#/pages/report/report';
					// #endif
					// #ifndef H5
					uni.navigateTo({ 
						url: '/pages/report/report' 
					});
					// #endif
				} else if (url === '/pages/appointment/appointment') {
					// 体检预约跳转到医院选择页面
					// #ifdef H5
					window.location.href = 'http://localhost:8080/#/pages/hospital/hospital';
					// #endif
					// #ifndef H5
					uni.navigateTo({ 
						url: '/pages/hospital/hospital' 
					});
					// #endif
				} else {
					uni.navigateTo({ url });
				}
			},
			selectHospital(hospital) {
				uni.navigateTo({
					url: `/pages/hospital-detail/hospital-detail?id=${hospital.id}`
				});
			},
			selectPackage(pkg) {
				uni.navigateTo({
					url: `/pages/package-detail/package-detail?id=${pkg.id}`
				});
			},
			viewNews(news) {
				if (news.url) {
					uni.showLoading({
						title: '加载中...'
					});
			
					uni.navigateTo({
						url: `/pages/news-web-view/news-web-view?url=${encodeURIComponent(news.url)}`,
						complete: () => {
							uni.hideLoading();
						}
					});
				} else {
					uni.navigateTo({
						url: `/pages/news-detail/news-detail?id=${news.id}`
					});
				}
			},
			async getRecommendPackages() {
				try {
					console.log('开始获取推荐套餐...');
					const result = await get(packageApi.getRecommendPackages);
					console.log('推荐套餐接口返回结果:', result);
					
					if (result && result.data) {
						this.recommendPackages = result.data.map((item, index) => ({
							id: item.id,
							name: item.name,
							price: item.price || 0,
							description: item.description || '',
							tags: item.tags || [],
							image: `/static/images/package${(index % 4) + 1}.jpg`
						}));
						console.log('处理后的推荐套餐数据:', this.recommendPackages);
					} else if (Array.isArray(result)) {
						// 如果直接返回数组
						this.recommendPackages = result.map((item, index) => ({
							id: item.id,
							name: item.name,
							price: item.price || 0,
							description: item.description || '',
							tags: item.tags || [],
							image: `/static/images/package${(index % 4) + 1}.jpg`
						}));
						console.log('处理后的推荐套餐数据:', this.recommendPackages);
					} else {
						console.log('接口返回数据格式异常:', result);
						this.recommendPackages = [];
					}
				} catch (e) {
					console.error('获取推荐套餐失败:', e);
					this.recommendPackages = [];
					// 使用测试数据
					this.recommendPackages = [
						{
							id: 4001,
							name: '基础体检套餐',
							price: 269,
							description: '包含常规体检项目，适合一般健康检查',
							image: '/static/images/package1.jpg'
						},
						{
							id: 4002,
							name: '高级体检套餐',
							price: 599,
							description: '包含基础套餐及更多专项检查，适合中老年人',
							image: '/static/images/package2.jpg'
						}
					];
				}
			},
			// 切换布局模式
			toggleLayout() {
				this.layoutMode = this.layoutMode === 2 ? 4 : 2;
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

	.floating-shapes {
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		pointer-events: none;
		z-index: -1;

		.shape {
			position: absolute;
			background: rgba(255, 255, 255, 0.1);
			border-radius: 50%;
			filter: blur(50px);
			animation: float 15s infinite ease-in-out;
			transition: all 0.3s ease;
			
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

			&.shape-1 {
				width: 100px;
				height: 100px;
				top: 10%;
				left: 10%;
				animation-delay: -2s;
			}
			&.shape-2 {
				width: 150px;
				height: 150px;
				top: 70%;
				left: 30%;
				animation-delay: -5s;
			}
			&.shape-3 {
				width: 120px;
				height: 120px;
				top: 20%;
				right: 20%;
				animation-delay: -8s;
			}
			&.shape-4 {
				width: 180px;
				height: 180px;
				bottom: 10%;
				right: 50%;
				animation-delay: -10s;
			}
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
			
			input {
				flex: 1;
				height: 100%;
				font-size: 32rpx;
				color: #333333;
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
		
		.service-header {
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
			
			.layout-toggle {
				display: flex;
				align-items: center;
				font-size: 26rpx;
				color: #0984e3;
				transition: all 0.3s ease;
				padding: 8rpx 16rpx;
				border-radius: 20rpx;
				background: rgba(9, 132, 227, 0.1);
				border: 1rpx solid rgba(9, 132, 227, 0.2);
				
				&:hover {
					transform: translateX(4rpx);
					background: rgba(9, 132, 227, 0.15);
					box-shadow: 0 4rpx 12rpx rgba(9, 132, 227, 0.2);
				}
				
				&:active {
					transform: scale(0.95);
				}
				
				.toggle-text {
					margin-right: 8rpx;
					font-weight: 500;
				}
				
				.toggle-icon {
					font-size: 24rpx;
					transition: transform 0.3s ease;
				}
				
				&:hover .toggle-icon {
					transform: rotate(180deg);
				}
			}
		}
		
		.service-grid {
			display: grid;
			grid-template-columns: repeat(2, 1fr);
			gap: 30rpx;
			transition: all 0.3s ease;
		}
		
		/* 可选：每行4个的布局 */
		.service-grid-four {
			display: grid;
			grid-template-columns: repeat(4, 1fr);
			gap: 15rpx;
			
			.service-item {
				padding: 15rpx 10rpx;
				
				.service-icon {
					width: 70rpx;
					height: 70rpx;
					margin-bottom: 10rpx;
					
					.service-icon-img {
						width: 40rpx;
						height: 40rpx;
					}
				}
				
				.service-name {
					font-size: 24rpx;
					line-height: 1.1;
				}
			}
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
				animation: gentlePulse 3s ease-in-out infinite;
				
				&:hover {
					transform: scale(1.1);
					box-shadow: 0 8rpx 24rpx rgba(9, 132, 227, 0.3);
					animation: none;
				}
				
				.service-icon-img {
					width: 55rpx;
					height: 55rpx;
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
			animation: gentleFlow 8s ease-in-out infinite, gentleBreathing 6s ease-in-out infinite;
		}
		
		&:hover {
			transform: translateY(-4rpx);
			box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.15);
		}
		
		// 静置时的呼吸动画
		animation: gentleBreathing 4s ease-in-out infinite, cardGlow 6s ease-in-out infinite;
		
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
					animation: gentlePulse 3s ease-in-out infinite;
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
				animation: gentlePulse 3s ease-in-out infinite, borderGlow 4s ease-in-out infinite;
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

	// 加载状态样式
	.loading-container {
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 40rpx 0;
		
		.loading-text {
			font-size: 28rpx;
			color: #999999;
		}
	}

	// 空状态样式
	.empty-container {
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 40rpx 0;
		
		.empty-text {
			font-size: 28rpx;
			color: #999999;
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

	.news-list {
		.news-item {
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
				animation: gentlePulse 3.5s ease-in-out infinite;
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
			
			.news-image {
				width: 140rpx;
				height: 100rpx;
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
					transition: transform 0.3s ease;
				}
				
				&:hover image {
					transform: scale(1.1);
				}
			}
			
			.news-info {
				flex: 1;
				display: flex;
				flex-direction: column;
				justify-content: space-between;
				
				.news-title {
					font-size: 28rpx;
					color: #333333;
					line-height: 1.5;
					display: -webkit-box;
					-webkit-box-orient: vertical;
					-webkit-line-clamp: 2;
					overflow: hidden;
					text-overflow: ellipsis;
					margin-bottom: 12rpx;
					font-weight: 500;
					position: relative;
					
					&::after {
						content: '';
						position: absolute;
						bottom: -2rpx;
						left: 0;
						width: 0;
						height: 2rpx;
						background: linear-gradient(90deg, #0984e3, #74b9ff);
						transition: width 0.3s ease;
					}
				}
				
				&:hover .news-title::after {
					width: 100%;
				}
				
				.news-meta {
					display: flex;
					justify-content: space-between;
					align-items: center;
					font-size: 22rpx;
					color: #999999;
					
					.news-source {
						color: #0984e3;
						background: linear-gradient(135deg, rgba(9, 132, 227, 0.1), rgba(116, 185, 255, 0.1));
						padding: 4rpx 8rpx;
						border-radius: 8rpx;
						font-weight: 500;
						transition: all 0.3s ease;
						
						&:hover {
							background: linear-gradient(135deg, rgba(9, 132, 227, 0.2), rgba(116, 185, 255, 0.2));
							transform: scale(1.05);
						}
					}
					
					.news-time {
						color: #666666;
						font-size: 20rpx;
					}
				}
			}
		}
	}

	// 动画定义
	@keyframes float {
		0% {
			transform: translateY(0) translateX(0) scale(1);
			opacity: 0.8;
		}
		25% {
			transform: translateY(-20px) translateX(20px) scale(1.1);
			opacity: 0.9;
		}
		50% {
			transform: translateY(0) translateX(0) scale(1);
			opacity: 0.8;
		}
		75% {
			transform: translateY(20px) translateX(-20px) scale(1.1);
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

	@keyframes pulse {
		0%, 100% {
			transform: scale(1);
		}
		50% {
			transform: scale(1.05);
		}
	}

	@keyframes gentleBreathing {
		0%, 100% {
			transform: scale(1);
			box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
			background: linear-gradient(135deg, rgba(255, 255, 255, 0.95), rgba(248, 250, 255, 0.9));
		}
		50% {
			transform: scale(1.002);
			box-shadow: 0 12rpx 40rpx rgba(9, 132, 227, 0.08);
			background: linear-gradient(135deg, rgba(255, 255, 255, 0.98), rgba(248, 250, 255, 0.95));
		}
	}

	@keyframes gentlePulse {
		0%, 100% {
			transform: scale(1);
			opacity: 0.8;
		}
		50% {
			transform: scale(1.02);
			opacity: 1;
		}
	}

	@keyframes gentleFlow {
		0% {
			transform: translate(0, 0);
			opacity: 0.8;
		}
		50% {
			transform: translate(10px, 10px);
			opacity: 1;
		}
		100% {
			transform: translate(0, 0);
			opacity: 0.8;
		}
	}

	@keyframes cardGlow {
		0%, 100% {
			box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
		}
		50% {
			box-shadow: 0 8rpx 32rpx rgba(9, 132, 227, 0.15);
		}
	}

	@keyframes borderGlow {
		0%, 100% {
			box-shadow: 0 0 8rpx rgba(9, 132, 227, 0.3);
		}
		50% {
			box-shadow: 0 0 12rpx rgba(9, 132, 227, 0.5);
		}
	}

	@keyframes slideInLeft {
		from {
			opacity: 0;
			transform: translateX(-30rpx);
		}
		to {
			opacity: 1;
			transform: translateX(0);
		}
	}

	// 为不同部分添加动画延迟
	.hospital-list .hospital-item {
		animation: slideInLeft 0.6s ease-out both;
		
		&:nth-child(1) { animation-delay: 0.1s; }
		&:nth-child(2) { animation-delay: 0.2s; }
		&:nth-child(3) { animation-delay: 0.3s; }
	}

	.package-list .package-item {
		animation: fadeInUp 0.8s ease-out both;
		
		&:nth-child(1) { animation-delay: 0.1s; }
		&:nth-child(2) { animation-delay: 0.2s; }
		&:nth-child(3) { animation-delay: 0.3s; }
	}

	.news-list .news-item {
		animation: slideInLeft 0.6s ease-out both;
		
		&:nth-child(1) { animation-delay: 0.1s; }
		&:nth-child(2) { animation-delay: 0.2s; }
		&:nth-child(3) { animation-delay: 0.3s; }
	}
</style>
