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
					<text class="iconfont icon-right"></text>
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
							<text class="iconfont icon-location"></text>
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
					<text class="iconfont icon-right"></text>
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

		<!-- 健康资讯 -->
		<view class="section">
			<view class="section-header">
				<text class="section-title">健康资讯</text>
				<view class="more" @click="navigateTo('/pages/news/news')">
					<text>更多</text>
					<text class="iconfont icon-right"></text>
				</view>
			</view>
			<view class="news-list">
				<view class="news-item" v-for="(item, index) in newsList" :key="index" @click="viewNews(item)">
					<image :src="item.image" mode="aspectFill" class="news-image"></image>
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
</template>

<script>
	export default {
		data() {
			return {
				bannerList: [{
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
				hospitalList: [{
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
						url: "https://www.sohu.com/a/904293109_121679638"
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
			},
			viewNews(news) {
				console.log("111111111111111111111")
				if (news.url) {
					console.log("加载出")
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
					console.log("未加载出")
					uni.navigateTo({
						url: `/pages/news-detail/news-detail?id=${news.id}`
					});
				}
			}
		}
	}
</script>

<style lang="scss">
	.content {
		padding-bottom: 30rpx;
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
			border-radius: 0 0 20rpx 20rpx;
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
				width: 100rpx;
				height: 100rpx;
				margin-bottom: 15rpx;
			}

			.service-name {
				font-size: 26rpx;
				color: #333333;
			}
		}
	}

	.section {
		margin-bottom: 20rpx;
		background-color: #ffffff;
		padding: 20rpx 30rpx;

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
		margin: 0 -30rpx;
		padding: 0 30rpx;

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

	.news-list {
		.news-item {
			display: flex;
			margin-bottom: 30rpx;

			&:last-child {
				margin-bottom: 0;
			}

			.news-image {
				width: 200rpx;
				height: 140rpx;
				border-radius: 10rpx;
				margin-right: 20rpx;
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
				}

				.news-meta {
					display: flex;
					justify-content: space-between;
					font-size: 24rpx;
					color: #999999;

					.news-source {
						color: #1296db;
					}
				}
			}
		}
	}
</style>
