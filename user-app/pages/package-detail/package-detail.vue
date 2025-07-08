<template>
	<view class="content">
		<!-- 套餐封面图 -->
		<image class="package-banner" src="/static/images/package1.jpg" mode="aspectFill"></image>
		
		<!-- 套餐基本信息 -->
		<view class="package-info">
			<view class="package-name">{{packageInfo.name}}</view>
			<view class="package-price-box">
				<text class="package-price">¥{{packageInfo.price}}</text>
				<text class="package-original-price" v-if="packageInfo.originalPrice">¥{{packageInfo.originalPrice}}</text>
				<text class="package-discount" v-if="packageInfo.originalPrice">{{discount}}折</text>
			</view>
			<view class="package-tags">
				<text class="tag" v-for="(tag, index) in packageInfo.tags" :key="index">{{tag}}</text>
			</view>
		</view>
		
		<!-- 医院信息 -->
		<view class="hospital-info" @click="navigateTo(`/pages/hospital-detail/hospital-detail?id=${packageInfo.hospitalId}`)">
			<image class="hospital-image" :src="packageInfo.hospitalImage" mode="aspectFill"></image>
			<view class="hospital-detail">
				<text class="hospital-name">{{packageInfo.hospitalName}}</text>
				<view class="hospital-address">
					<text class="iconfont icon-location">&#xe60e;</text>
					<text class="address-text">{{packageInfo.hospitalAddress}}</text>
				</view>
			</view>
			<text class="iconfont icon-right">&#xe65f;</text>
		</view>
		
		<!-- 套餐详情 -->
		<view class="section">
			<view class="section-header">
				<text class="section-title">套餐详情</text>
			</view>
			<view class="package-desc">
				<text class="desc-text">{{packageInfo.description}}</text>
			</view>
		</view>
		
		<!-- 检查项目 -->
		<view class="section">
			<view class="section-header">
				<text class="section-title">检查项目</text>
				<text class="item-count">共{{packageInfo.items.length}}项</text>
			</view>
			<view class="item-list">
				<view class="item-category" v-for="(category, categoryIndex) in categoryItems" :key="categoryIndex">
					<view class="category-header">
						<text class="category-name">{{category.name}}</text>
						<text class="category-count">{{category.items.length}}项</text>
					</view>
					<view class="category-items">
						<view class="item" v-for="(item, itemIndex) in category.items" :key="itemIndex">
							<text class="item-name">{{item.name}}</text>
							<text class="item-desc">{{item.desc}}</text>
						</view>
					</view>
				</view>
			</view>
		</view>
		
		<!-- 适用人群 -->
		<view class="section">
			<view class="section-header">
				<text class="section-title">适用人群</text>
			</view>
			<view class="suitable-people">
				<text class="people-text">{{packageInfo.suitablePeople}}</text>
			</view>
		</view>
		
		<!-- 预约须知 -->
		<view class="section">
			<view class="section-header">
				<text class="section-title">预约须知</text>
			</view>
			<view class="notice-list">
				<view class="notice-item" v-for="(item, index) in packageInfo.notices" :key="index">
					<text class="notice-dot"></text>
					<text class="notice-text">{{item}}</text>
				</view>
			</view>
		</view>
		
		<!-- 用户评价 -->
		<view class="section">
			<view class="section-header">
				<text class="section-title">用户评价</text>
				<view class="more" @click="navigateTo(`/pages/reviews/reviews?packageId=${packageInfo.id}`)">
					<text>更多</text>
					<text class="iconfont icon-right">&#xe65f;</text>
				</view>
			</view>
			<view class="review-list" v-if="packageInfo.reviews.length > 0">
				<view class="review-item" v-for="(item, index) in packageInfo.reviews" :key="index">
					<view class="reviewer-info">
						<image class="reviewer-avatar" :src="item.avatar" mode="aspectFill"></image>
						<view class="reviewer-detail">
							<text class="reviewer-name">{{item.name}}</text>
							<text class="review-time">{{item.time}}</text>
						</view>
						<view class="review-rating">
							<text class="iconfont icon-star-fill" v-for="n in item.rating" :key="n"></text>
							<text class="iconfont icon-star" v-for="n in 5-item.rating" :key="n+5"></text>
						</view>
					</view>
					<text class="review-content">{{item.content}}</text>
				</view>
			</view>
			<view class="empty-reviews" v-else>
				<text class="empty-text">暂无评价</text>
			</view>
		</view>
		
		<!-- 底部按钮 -->
		<view class="bottom-btn-container">
			<view class="btn-group">
				<button class="btn-consult" @click="consult">在线咨询</button>
				<button class="btn-appointment" @click="makeAppointment">立即预约</button>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				packageInfo: {
					id: 1,
					name: '标准体检套餐',
					price: '299',
					originalPrice: '399',
					tags: ['热门', '体检', '全面'],
					hospitalId: 1,
					hospitalName: '沈阳市云医院-和平分院',
					hospitalAddress: '沈阳市和平区南京南街61号',
					hospitalImage: '/static/images/hospital1.jpg',
					description: '本套餐是针对25-45岁人群设计的基础体检套餐，包含血常规、尿常规、肝功能、肾功能、血脂、血糖等基础检查项目，能够全面了解身体健康状况，及早发现潜在健康风险。',
					suitablePeople: '适合25-45岁人群，特别是长期伏案工作、生活压力大、作息不规律的上班族，建议每年体检一次。',
					items: [
						{
							category: '一般检查',
							name: '身高体重',
							desc: '测量身高、体重，计算体重指数(BMI)'
						},
						{
							category: '一般检查',
							name: '血压',
							desc: '测量收缩压和舒张压'
						},
						{
							category: '一般检查',
							name: '视力',
							desc: '检查远视力和近视力'
						},
						{
							category: '血液检查',
							name: '血常规',
							desc: '检查红细胞、白细胞、血小板等指标'
						},
						{
							category: '血液检查',
							name: '肝功能',
							desc: '检查ALT、AST、总蛋白等肝功能指标'
						},
						{
							category: '血液检查',
							name: '肾功能',
							desc: '检查尿素氮、肌酐等肾功能指标'
						},
						{
							category: '血液检查',
							name: '血脂',
							desc: '检查总胆固醇、甘油三酯等血脂指标'
						},
						{
							category: '血液检查',
							name: '血糖',
							desc: '检查空腹血糖'
						},
						{
							category: '尿液检查',
							name: '尿常规',
							desc: '检查尿液颜色、比重、pH值等指标'
						},
						{
							category: '影像检查',
							name: '胸部X光',
							desc: '检查肺部、心脏等胸腔器官'
						},
						{
							category: '影像检查',
							name: 'B超',
							desc: '肝、胆、脾、胰、肾B超检查'
						}
					],
					notices: [
						'体检前一天请清淡饮食，避免辛辣、油腻食物',
						'体检当天请空腹，禁食8-12小时',
						'体检前一天晚上请保证充足睡眠',
						'体检当天请携带身份证等有效证件',
						'体检报告一般在3-5个工作日出具，可在APP查看'
					],
					reviews: [
						{
							id: 1,
							name: '张先生',
							avatar: '/static/images/avatar1.jpg',
							time: '2023-07-08',
							rating: 5,
							content: '套餐内容很全面，医生态度很好，检查很仔细，报告出来也很快，值得推荐！'
						},
						{
							id: 2,
							name: '李女士',
							avatar: '/static/images/avatar2.jpg',
							time: '2023-07-06',
							rating: 4,
							content: '体检很顺利，医护人员很专业，就是人比较多，需要排队等待。'
						}
					]
				}
			}
		},
		computed: {
			// 计算折扣
			discount() {
				if (this.packageInfo.originalPrice) {
					return Math.floor(this.packageInfo.price / this.packageInfo.originalPrice * 10);
				}
				return 10;
			},
			// 按类别分组的检查项目
			categoryItems() {
				const categories = {};
				this.packageInfo.items.forEach(item => {
					if (!categories[item.category]) {
						categories[item.category] = {
							name: item.category,
							items: []
						};
					}
					categories[item.category].items.push(item);
				});
				return Object.values(categories);
			}
		},
		onLoad(options) {
			// 获取套餐ID
			const packageId = options.id;
			// 根据ID获取套餐详情
			this.getPackageDetail(packageId);
		},
		methods: {
			// 获取套餐详情
			getPackageDetail(id) {
				// 这里可以替换为实际的API调用
				console.log('获取套餐详情，ID：', id);
			},
			// 在线咨询
			consult() {
				uni.navigateTo({
					url: '/pages/consult/consult'
				});
			},
			// 立即预约
			makeAppointment() {
				uni.navigateTo({
					url: `/pages/appointment-form/appointment-form?packageId=${this.packageInfo.id}&hospitalId=${this.packageInfo.hospitalId}`
				});
			},
			// 页面跳转
			navigateTo(url) {
				uni.navigateTo({
					url: url
				});
			}
		}
	}
</script>

<style lang="scss">
.content {
	padding-bottom: 120rpx;
}

.package-banner {
	width: 100%;
	height: 400rpx;
}

.package-info {
	background-color: #ffffff;
	padding: 30rpx;
	
	.package-name {
		font-size: 36rpx;
		font-weight: bold;
		color: #333333;
		margin-bottom: 20rpx;
	}
	
	.package-price-box {
		display: flex;
		align-items: baseline;
		margin-bottom: 20rpx;
		
		.package-price {
			font-size: 40rpx;
			font-weight: bold;
			color: #ff5a5f;
			margin-right: 15rpx;
		}
		
		.package-original-price {
			font-size: 28rpx;
			color: #999999;
			text-decoration: line-through;
			margin-right: 15rpx;
		}
		
		.package-discount {
			font-size: 24rpx;
			color: #ffffff;
			background-color: #ff5a5f;
			padding: 4rpx 10rpx;
			border-radius: 4rpx;
		}
	}
	
	.package-tags {
		display: flex;
		flex-wrap: wrap;
		
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
}

.hospital-info {
	display: flex;
	align-items: center;
	background-color: #ffffff;
	padding: 20rpx 30rpx;
	margin-top: 20rpx;
	
	.hospital-image {
		width: 100rpx;
		height: 100rpx;
		border-radius: 10rpx;
		margin-right: 20rpx;
	}
	
	.hospital-detail {
		flex: 1;
		
		.hospital-name {
			font-size: 30rpx;
			font-weight: bold;
			color: #333333;
			margin-bottom: 10rpx;
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
	
	.iconfont {
		font-size: 24rpx;
		color: #999999;
	}
}

.section {
	margin-top: 20rpx;
	background-color: #ffffff;
	padding: 30rpx;
	
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
		
		.item-count {
			font-size: 24rpx;
			color: #999999;
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

.package-desc {
	.desc-text {
		font-size: 28rpx;
		color: #666666;
		line-height: 1.6;
	}
}

.item-list {
	.item-category {
		margin-bottom: 30rpx;
		
		&:last-child {
			margin-bottom: 0;
		}
		
		.category-header {
			display: flex;
			justify-content: space-between;
			align-items: center;
			padding: 15rpx 0;
			border-bottom: 1px solid #f5f5f5;
			margin-bottom: 15rpx;
			
			.category-name {
				font-size: 30rpx;
				font-weight: bold;
				color: #333333;
			}
			
			.category-count {
				font-size: 24rpx;
				color: #999999;
			}
		}
		
		.category-items {
			.item {
				display: flex;
				flex-direction: column;
				padding: 15rpx 0;
				border-bottom: 1px dashed #f5f5f5;
				
				&:last-child {
					border-bottom: none;
				}
				
				.item-name {
					font-size: 28rpx;
					color: #333333;
					margin-bottom: 10rpx;
				}
				
				.item-desc {
					font-size: 24rpx;
					color: #999999;
				}
			}
		}
	}
}

.suitable-people {
	.people-text {
		font-size: 28rpx;
		color: #666666;
		line-height: 1.6;
	}
}

.notice-list {
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

.review-list {
	.review-item {
		margin-bottom: 30rpx;
		
		&:last-child {
			margin-bottom: 0;
		}
		
		.reviewer-info {
			display: flex;
			align-items: center;
			margin-bottom: 15rpx;
			
			.reviewer-avatar {
				width: 60rpx;
				height: 60rpx;
				border-radius: 50%;
				margin-right: 15rpx;
			}
			
			.reviewer-detail {
				flex: 1;
				
				.reviewer-name {
					font-size: 28rpx;
					color: #333333;
					margin-bottom: 5rpx;
				}
				
				.review-time {
					font-size: 22rpx;
					color: #999999;
				}
			}
			
			.review-rating {
				.iconfont {
					font-size: 24rpx;
					margin-left: 2rpx;
					
					&.icon-star-fill {
						color: #ff9500;
					}
					
					&.icon-star {
						color: #dddddd;
					}
				}
			}
		}
		
		.review-content {
			font-size: 26rpx;
			color: #666666;
			line-height: 1.6;
		}
	}
}

.empty-reviews {
	padding: 50rpx 0;
	text-align: center;
	
	.empty-text {
		font-size: 28rpx;
		color: #999999;
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
	
	.btn-group {
		display: flex;
		
		.btn-consult {
			width: 240rpx;
			height: 90rpx;
			line-height: 90rpx;
			background-color: #ffffff;
			color: #1296db;
			font-size: 30rpx;
			border-radius: 45rpx;
			border: 1px solid #1296db;
			margin-right: 20rpx;
			
			&::after {
				border: none;
			}
		}
		
		.btn-appointment {
			flex: 1;
			height: 90rpx;
			line-height: 90rpx;
			background-color: #1296db;
			color: #ffffff;
			font-size: 30rpx;
			border-radius: 45rpx;
			
			&::after {
				border: none;
			}
		}
	}
}
</style> 