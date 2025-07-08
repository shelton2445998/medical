<template>
	<view class="content">
		<!-- 顶部搜索栏 -->
		<view class="search-container">
			<view class="search-box">
				<text class="iconfont icon-search">&#xe65c;</text>
				<input type="text" placeholder="搜索医院、体检套餐" />
			</view>
		</view>
		
		<!-- 轮播图 -->
		<swiper class="banner-swiper" circular indicator-dots autoplay interval="3000" duration="500" indicator-active-color="#1296db">
			<swiper-item v-for="(item, index) in bannerList" :key="index">
				<image :src="item.image" mode="aspectFill" class="banner-image" @click="navigateTo(item.url)"></image>
			</swiper-item>
		</swiper>
		
		<!-- 快捷服务 -->
		<view class="quick-service">
			<view class="service-item" v-for="(item, index) in serviceList" :key="index" @click="navigateTo(item.url)">
				<image :src="item.icon" mode="aspectFit" class="service-icon"></image>
				<text class="service-name">{{item.name}}</text>
			</view>
		</view>
		
		<!-- 推荐医院 -->
		<view class="section">
			<view class="section-header">
				<text class="section-title">推荐医院</text>
				<view class="more" @click="navigateTo('/pages/hospital/hospital')">
					<text>更多</text>
					<text class="iconfont icon-right">&#xe65f;</text>
				</view>
			</view>
			<view class="hospital-list">
				<view class="hospital-item" v-for="(item, index) in hospitalList" :key="index" @click="selectHospital(item)">
					<image :src="item.image" mode="aspectFill" class="hospital-image"></image>
					<view class="hospital-info">
						<text class="hospital-name">{{item.name}}</text>
						<view class="hospital-tags">
							<text class="tag" v-for="(tag, tagIndex) in item.tags" :key="tagIndex">{{tag}}</text>
						</view>
						<view class="hospital-address">
							<text class="iconfont icon-location">&#xe60e;</text>
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
					<text class="iconfont icon-right">&#xe65f;</text>
				</view>
			</view>
			<view class="package-list">
				<view class="package-item" v-for="(item, index) in packageList" :key="index" @click="selectPackage(item)">
					<image :src="item.image" mode="aspectFill" class="package-image"></image>
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
						<image src="/static/images/icons/icon-select-hospital.png" mode="aspectFit"></image>
						<text class="process-number">1</text>
					</view>
					<text class="process-name">选择医院</text>
				</view>
				<view class="process-arrow">
					<text class="iconfont icon-right">&#xe65f;</text>
				</view>
				<view class="process-item">
					<view class="process-icon">
						<image src="/static/images/icons/icon-select-package.png" mode="aspectFit"></image>
						<text class="process-number">2</text>
					</view>
					<text class="process-name">选择套餐</text>
				</view>
				<view class="process-arrow">
					<text class="iconfont icon-right">&#xe65f;</text>
				</view>
				<view class="process-item">
					<view class="process-icon">
						<image src="/static/images/icons/icon-fill-info.png" mode="aspectFit"></image>
						<text class="process-number">3</text>
					</view>
					<text class="process-name">填写信息</text>
				</view>
				<view class="process-arrow">
					<text class="iconfont icon-right">&#xe65f;</text>
				</view>
				<view class="process-item">
					<view class="process-icon">
						<image src="/static/images/icons/icon-pay.png" mode="aspectFit"></image>
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
					<text class="notice-dot"></text>
					<text class="notice-text">{{item}}</text>
				</view>
			</view>
		</view>
		
		<!-- 底部按钮 -->
		<view class="bottom-btn-container">
			<button class="bottom-btn" @click="navigateTo('/pages/hospital/hospital')">立即预约</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				bannerList: [
					{
						image: '/static/images/banner1.jpg',
						url: '/pages/package/package'
					},
					{
						image: '/static/images/banner2.jpg',
						url: '/pages/hospital/hospital'
					},
					{
						image: '/static/images/banner3.jpg',
						url: '/pages/appointment/appointment'
					}
				],
				serviceList: [
					{
						name: '全身体检',
						icon: '/static/images/icons/icon-full-body.png',
						url: '/pages/package/package?type=full'
					},
					{
						name: '男性体检',
						icon: '/static/images/icons/icon-male.png',
						url: '/pages/package/package?type=male'
					},
					{
						name: '女性体检',
						icon: '/static/images/icons/icon-female.png',
						url: '/pages/package/package?type=female'
					},
					{
						name: '老年体检',
						icon: '/static/images/icons/icon-elder.png',
						url: '/pages/package/package?type=elder'
					},
					{
						name: '儿童体检',
						icon: '/static/images/icons/icon-child.png',
						url: '/pages/package/package?type=child'
					}
				],
				hospitalList: [
					{
						id: 1,
						name: '沈阳市云医院-和平分院',
						image: '/static/images/hospital1.jpg',
						tags: ['三甲', '综合医院'],
						address: '沈阳市和平区南京南街61号'
					},
					{
						id: 2,
						name: '沈阳市云医院-沈河分院',
						image: '/static/images/hospital2.jpg',
						tags: ['三甲', '综合医院'],
						address: '沈阳市沈河区北站路33号'
					}
				],
				packageList: [
					{
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
					},
					{
						id: 3,
						name: '女性专属套餐',
						image: '/static/images/package3.jpg',
						description: '针对女性健康定制，包含妇科检查、乳腺检查等女性特有项目',
						price: '499',
						originalPrice: '599'
					}
				],
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
			
		},
		methods: {
			navigateTo(url) {
				uni.navigateTo({
					url: url
				});
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
			}
		}
	}
</script>

<style lang="scss">
.content {
	padding-bottom: 120rpx;
}

.search-container {
	padding: 20rpx 30rpx;
	background-color: #1296db;
	
	.search-box {
		display: flex;
		align-items: center;
		height: 70rpx;
		background-color: #ffffff;
		border-radius: 35rpx;
		padding: 0 30rpx;
		
		.iconfont {
			font-size: 36rpx;
			color: #999999;
			margin-right: 10rpx;
		}
		
		input {
			flex: 1;
			height: 70rpx;
			font-size: 28rpx;
		}
	}
}

.banner-swiper {
	width: 100%;
	height: 300rpx;
	
	.banner-image {
		width: 100%;
		height: 100%;
	}
}

.quick-service {
	display: flex;
	padding: 30rpx 20rpx;
	background-color: #ffffff;
	margin-bottom: 20rpx;
	
	.service-item {
		flex: 1;
		display: flex;
		flex-direction: column;
		align-items: center;
		
		.service-icon {
			width: 80rpx;
			height: 80rpx;
			margin-bottom: 15rpx;
		}
		
		.service-name {
			font-size: 26rpx;
			color: #333333;
		}
	}
}

.section {
	margin: 0 20rpx 20rpx;
	background-color: #ffffff;
	border-radius: 10rpx;
	padding: 20rpx;
	box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
	
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
				background-color: #1296db;
				border-radius: 3rpx;
			}
		}
		
		.more {
			display: flex;
			align-items: center;
			font-size: 26rpx;
			color: #999999;
			
			.iconfont {
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
					color: #1296db;
					background-color: rgba(18, 150, 219, 0.1);
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
				
				.iconfont {
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
		box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
		
		&:last-child {
			margin-right: 0;
		}
		
		.package-image {
			width: 100%;
			height: 180rpx;
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
			
			image {
				width: 100%;
				height: 100%;
			}
			
			.process-number {
				position: absolute;
				top: -10rpx;
				right: -10rpx;
				width: 36rpx;
				height: 36rpx;
				line-height: 36rpx;
				text-align: center;
				background-color: #ff5a5f;
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
		
		.iconfont {
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
			background-color: #1296db;
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
	bottom: 0;
	padding: 20rpx;
	background-color: #ffffff;
	box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
	
	.bottom-btn {
		height: 90rpx;
		line-height: 90rpx;
		background-color: #1296db;
		color: #ffffff;
		font-size: 32rpx;
		border-radius: 45rpx;
		
		&::after {
			border: none;
		}
	}
}
</style> 