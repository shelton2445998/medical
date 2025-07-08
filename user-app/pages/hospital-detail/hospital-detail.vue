<template>
	<view class="content">
		<!-- 医院封面图 -->
		<image class="hospital-banner" src="/static/images/hospital1.jpg" mode="aspectFill"></image>
		
		<!-- 医院基本信息 -->
		<view class="hospital-info">
			<view class="hospital-name">{{hospital.name}}</view>
			<view class="hospital-tags">
				<text class="tag" v-for="(tag, index) in hospital.tags" :key="index">{{tag}}</text>
			</view>
			<view class="hospital-rating">
				<text class="rating-score">{{hospital.rating}}</text>
				<text class="rating-text">综合评分</text>
			</view>
		</view>
		
		<!-- 医院联系信息 -->
		<view class="contact-info">
			<view class="contact-item" @click="makePhoneCall(hospital.phone)">
				<text class="iconfont icon-phone"></text>
				<text class="contact-text">{{hospital.phone}}</text>
			</view>
			<view class="contact-item" @click="openLocation(hospital.latitude, hospital.longitude)">
				<text class="iconfont icon-location"></text>
				<text class="contact-text">{{hospital.address}}</text>
			</view>
		</view>
		
		<!-- 医院介绍 -->
		<view class="section">
			<view class="section-header">
				<text class="section-title">医院介绍</text>
			</view>
			<view class="hospital-intro">
				<text class="intro-text">{{hospital.introduction}}</text>
				<view class="hospital-images">
					<image 
						v-for="(img, index) in hospital.images" 
						:key="index" 
						:src="img" 
						mode="aspectFill" 
						class="hospital-image"
						@click="previewImage(index)"
					></image>
				</view>
			</view>
		</view>
		
		<!-- 体检套餐 -->
		<view class="section">
			<view class="section-header">
				<text class="section-title">体检套餐</text>
				<view class="more" @click="navigateTo('/pages/package/package?hospitalId=' + hospital.id)">
					<text>更多</text>
					<text class="iconfont icon-right">&#xe65f;</text>
				</view>
			</view>
			<view class="package-list">
				<view class="package-item" v-for="(item, index) in packageList" :key="index" @click="selectPackage(item)">
					<view class="package-info">
						<text class="package-name">{{item.name}}</text>
						<text class="package-desc">{{item.description}}</text>
						<view class="package-price-box">
							<text class="package-price">¥{{item.price}}</text>
							<text class="package-original-price" v-if="item.originalPrice">¥{{item.originalPrice}}</text>
						</view>
					</view>
					<view class="package-btn">预约</view>
				</view>
			</view>
		</view>
		
		<!-- 医院环境 -->
		<view class="section">
			<view class="section-header">
				<text class="section-title">医院环境</text>
			</view>
			<scroll-view class="environment-scroll" scroll-x>
				<image 
					v-for="(img, index) in hospital.environment" 
					:key="index" 
					:src="img" 
					mode="aspectFill" 
					class="environment-image"
					@click="previewEnvironment(index)"
				></image>
			</scroll-view>
		</view>
		
		<!-- 用户评价 -->
		<view class="section">
			<view class="section-header">
				<text class="section-title">用户评价</text>
				<view class="more" @click="navigateTo('/pages/reviews/reviews?hospitalId=' + hospital.id)">
					<text>更多</text>
					<text class="iconfont icon-right">&#xe65f;</text>
				</view>
			</view>
			<view class="review-list">
				<view class="review-item" v-for="(item, index) in reviewList" :key="index">
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
		</view>
		
		<!-- 底部按钮 -->
		<view class="bottom-btn-container">
			<button class="bottom-btn" @click="navigateTo('/pages/appointment/appointment?hospitalId=' + hospital.id)">立即预约</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				hospital: {
					id: 1,
					name: '沈阳市云医院-和平分院',
					tags: ['三甲', '综合医院'],
					rating: '4.8',
					phone: '024-12345678',
					address: '沈阳市和平区南京南街61号',
					latitude: 41.805699,
					longitude: 123.431436,
					introduction: '沈阳市云医院-和平分院是一所集医疗、教学、科研、预防、保健、康复为一体的大型综合性三级甲等医院。医院占地面积5万平方米，建筑面积10万平方米，开放床位800张，拥有各类先进医疗设备2000余台（套），其中包括3.0T核磁共振、64排128层CT、直线加速器、伽马刀等大型医疗设备。医院设有临床科室40个，医技科室12个，专科特色突出，综合实力雄厚。',
					images: [
						'/static/images/hospital1.jpg',
						'/static/images/hospital2.jpg'
					],
					environment: [
						'/static/images/hospital-env1.jpg',
						'/static/images/hospital-env2.jpg',
						'/static/images/hospital-env3.jpg'
					]
				},
				packageList: [
					{
						id: 1,
						name: '标准体检套餐',
						description: '适合25-45岁人群，包含血常规、尿常规、肝功能等基础检查',
						price: '299',
						originalPrice: '399'
					},
					{
						id: 2,
						name: '高级体检套餐',
						description: '适合45岁以上人群，包含心脑血管、肿瘤筛查等全面检查',
						price: '699',
						originalPrice: '899'
					}
				],
				reviewList: [
					{
						id: 1,
						name: '张先生',
						avatar: '/static/images/avatar1.jpg',
						time: '2023-07-08',
						rating: 5,
						content: '医院环境很好，医生态度也很好，检查很全面，报告出来也很快，值得推荐！'
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
		},
		onLoad(options) {
			// 获取医院ID
			const hospitalId = options.id;
			// 根据ID获取医院详情
			this.getHospitalDetail(hospitalId);
		},
		methods: {
			// 获取医院详情
			getHospitalDetail(id) {
				// 这里可以替换为实际的API调用
				console.log('获取医院详情，ID：', id);
			},
			// 拨打电话
			makePhoneCall(phone) {
				uni.makePhoneCall({
					phoneNumber: phone
				});
			},
			// 打开地图
			openLocation(latitude, longitude) {
				uni.openLocation({
					latitude: latitude,
					longitude: longitude,
					name: this.hospital.name,
					address: this.hospital.address,
					scale: 18
				});
			},
			// 预览医院图片
			previewImage(index) {
				uni.previewImage({
					current: index,
					urls: this.hospital.images
				});
			},
			// 预览环境图片
			previewEnvironment(index) {
				uni.previewImage({
					current: index,
					urls: this.hospital.environment
				});
			},
			// 选择套餐
			selectPackage(pkg) {
				uni.navigateTo({
					url: `/pages/package-detail/package-detail?id=${pkg.id}&hospitalId=${this.hospital.id}`
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

.hospital-banner {
	width: 100%;
	height: 400rpx;
}

.hospital-info {
	background-color: #ffffff;
	padding: 30rpx;
	
	.hospital-name {
		font-size: 36rpx;
		font-weight: bold;
		color: #333333;
		margin-bottom: 20rpx;
	}
	
	.hospital-tags {
		display: flex;
		flex-wrap: wrap;
		margin-bottom: 20rpx;
		
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
	
	.hospital-rating {
		display: flex;
		align-items: baseline;
		
		.rating-score {
			font-size: 32rpx;
			font-weight: bold;
			color: #ff5a5f;
			margin-right: 10rpx;
		}
		
		.rating-text {
			font-size: 24rpx;
			color: #999999;
		}
	}
}

.contact-info {
	background-color: #ffffff;
	padding: 20rpx 30rpx;
	margin-bottom: 20rpx;
	
	.contact-item {
		display: flex;
		align-items: center;
		padding: 15rpx 0;
		
		.iconfont {
			font-size: 32rpx;
			color: #1296db;
			margin-right: 15rpx;
		}
		
		.contact-text {
			font-size: 28rpx;
			color: #333333;
		}
	}
}

.section {
	margin: 0 0 20rpx;
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

.hospital-intro {
	.intro-text {
		font-size: 28rpx;
		color: #666666;
		line-height: 1.6;
		margin-bottom: 20rpx;
	}
	
	.hospital-images {
		display: flex;
		flex-wrap: wrap;
		
		.hospital-image {
			width: 220rpx;
			height: 165rpx;
			margin-right: 15rpx;
			margin-bottom: 15rpx;
			border-radius: 8rpx;
		}
	}
}

.package-list {
	.package-item {
		display: flex;
		justify-content: space-between;
		align-items: center;
		padding: 20rpx 0;
		border-bottom: 1px solid #f5f5f5;
		
		&:last-child {
			border-bottom: none;
		}
		
		.package-info {
			flex: 1;
			
			.package-name {
				font-size: 30rpx;
				font-weight: bold;
				color: #333333;
				margin-bottom: 10rpx;
			}
			
			.package-desc {
				font-size: 24rpx;
				color: #666666;
				margin-bottom: 10rpx;
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
		
		.package-btn {
			width: 120rpx;
			height: 60rpx;
			line-height: 60rpx;
			text-align: center;
			background-color: #1296db;
			color: #ffffff;
			font-size: 26rpx;
			border-radius: 30rpx;
		}
	}
}

.environment-scroll {
	white-space: nowrap;
	
	.environment-image {
		display: inline-block;
		width: 280rpx;
		height: 180rpx;
		margin-right: 15rpx;
		border-radius: 8rpx;
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