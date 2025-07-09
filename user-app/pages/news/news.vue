<template>
	<view class="content">
		<!-- 页面标题 -->
		<view class="page-header">
			<view class="back-btn" @click="goBack">
				<text class="iconfont icon-back"></text>
			</view>
			<text class="page-title">健康资讯</text>
		</view>

		<!-- 资讯分类 -->
		<view class="news-tabs">
			<view class="tab-item" v-for="(item, index) in newsTypes" :key="index" :class="{active: currentType === item.id}"
			 @click="switchType(item.id)">
				<text>{{item.name}}</text>
			</view>
		</view>

		<!-- 资讯列表 -->
		<view class="news-list">
			<view class="news-item" v-for="(item, index) in filteredNews" :key="index" @click="viewNews(item)">
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
</template>

<script>
	export default {
		data() {
			return {
				currentType: 0, // 0表示全部
				newsTypes: [{
						id: 0,
						name: '全部'
					},
					{
						id: 1,
						name: '养生'
					},
					{
						id: 2,
						name: '疾病预防'
					},
					{
						id: 3,
						name: '饮食健康'
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
					},
					{
						id: 4,
						title: '高血压患者的饮食注意事项',
						image: '/static/images/news4.jpg',
						source: '健康时报',
						time: '2023-07-04',
						type: 2,
						url: 'https://news.99.com.cn/jiankang/20250303/1019678.htm'
					},
					{
						id: 5,
						title: '健康早餐的5个标准，你达标了吗？',
						image: '/static/images/news5.jpg',
						source: '生活健康',
						time: '2023-07-03',
						type: 3
					}
				],
				filteredNews: []
			}
		},
		onLoad() {
			// 初始化显示所有资讯
			this.filteredNews = this.newsList;
		},
		methods: {
			// 切换资讯类型
			switchType(typeId) {
				this.currentType = typeId;

				if (typeId === 0) {
					// 全部资讯
					this.filteredNews = this.newsList;
				} else {
					// 按类型筛选
					this.filteredNews = this.newsList.filter(item => item.type === typeId);
				}
			},
			// 查看资讯详情
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
			},
			// 返回上一页
			goBack() {
				uni.navigateBack();
			}
		}
	}
</script>

<style lang="scss">
	.content {
		background-color: #f5f5f5;
		min-height: 100vh;
	}

	.page-header {
		position: relative;
		background-color: #1296db;
		padding: 20rpx 0;
		text-align: center;

		.back-btn {
			position: absolute;
			left: 30rpx;
			top: 50%;
			transform: translateY(-50%);
			width: 60rpx;
			height: 60rpx;
			display: flex;
			justify-content: center;
			align-items: center;

			.iconfont {
				color: #ffffff;
				font-size: 40rpx;
			}
		}

		.page-title {
			color: #ffffff;
			font-size: 36rpx;
			font-weight: bold;
		}
	}

	.news-tabs {
		display: flex;
		background-color: #ffffff;
		padding: 0 20rpx;
		margin-bottom: 20rpx;
		box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);

		.tab-item {
			flex: 1;
			height: 80rpx;
			display: flex;
			justify-content: center;
			align-items: center;
			font-size: 28rpx;
			color: #666666;
			position: relative;

			&.active {
				color: #1296db;
				font-weight: bold;

				&::after {
					content: '';
					position: absolute;
					left: 50%;
					bottom: 0;
					transform: translateX(-50%);
					width: 60rpx;
					height: 4rpx;
					background-color: #1296db;
					border-radius: 2rpx;
				}
			}
		}
	}

	.news-list {
		padding: 0 30rpx;

		.news-item {
			display: flex;
			background-color: #ffffff;
			border-radius: 10rpx;
			margin-bottom: 20rpx;
			padding: 30rpx;
			box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);

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
					font-size: 30rpx;
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
