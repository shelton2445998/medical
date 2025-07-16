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
			<!-- 医院封面图 -->
			<view class="hospital-banner-section">
				<image class="hospital-banner" src="/static/images/hospital1.jpg" mode="aspectFill"></image>
				<view class="banner-overlay">
					<view class="banner-content">
						<text class="banner-title">{{hospital.name}}</text>
						<view class="banner-rating">
							<text class="rating-score">{{hospital.rating}}</text>
							<text class="rating-text">综合评分</text>
						</view>
					</view>
				</view>
			</view>
			
			<!-- 医院基本信息卡片 -->
			<view class="hospital-info-card">
				<view class="card-header">
					<view class="header-icon">🏥</view>
					<view class="header-title">医院信息</view>
				</view>
				<view class="hospital-tags">
					<text class="tag" v-for="(tag, index) in hospital.tags" :key="index">{{tag}}</text>
				</view>
			</view>
			
			<!-- 医院联系信息卡片 -->
			<view class="contact-card">
				<view class="card-header">
					<view class="header-icon">📞</view>
					<view class="header-title">联系信息</view>
				</view>
				<view class="contact-content">
					<view class="contact-item" @click="makePhoneCall(hospital.phone)">
						<view class="contact-icon">📞</view>
						<view class="contact-detail">
							<text class="contact-label">联系电话</text>
							<text class="contact-text">{{hospital.phone}}</text>
						</view>
						<view class="contact-arrow">
							<text class="arrow-icon">→</text>
						</view>
					</view>
					<view class="contact-item" @click="openLocation(hospital.latitude, hospital.longitude)">
						<view class="contact-icon">📍</view>
						<view class="contact-detail">
							<text class="contact-label">医院地址</text>
							<text class="contact-text">{{hospital.address}}</text>
						</view>
						<view class="contact-arrow">
							<text class="arrow-icon">→</text>
						</view>
					</view>
				</view>
			</view>
			
			<!-- 医院介绍卡片 -->
			<view class="intro-card">
				<view class="card-header">
					<view class="header-icon">📋</view>
					<view class="header-title">医院介绍</view>
				</view>
				<view class="intro-content">
					<text class="intro-text">{{hospital.introduction}}</text>
					<view class="hospital-images" v-if="hospital.images && hospital.images.length > 0">
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
			
			<!-- 体检套餐卡片 -->
			<view class="package-card">
				<view class="card-header">
					<view class="header-left">
						<view class="header-icon">📋</view>
						<view class="header-title">体检套餐</view>
					</view>
					<view class="header-right">
						<view class="more-btn" @click="navigateTo('/pages/package/package?hospitalId=' + hospital.id)">
							<text>更多</text>
							<text class="arrow-icon">→</text>
						</view>
					</view>
				</view>
				<view class="package-content">
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
			
			<!-- 医院环境卡片 -->
			<view class="environment-card">
				<view class="card-header">
					<view class="header-icon">🏢</view>
					<view class="header-title">医院环境</view>
				</view>
				<view class="environment-content">
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
			</view>
			
			<!-- 用户评价卡片 -->
			<view class="reviews-card">
				<view class="card-header">
					<view class="header-icon">⭐</view>
					<view class="header-title">用户评价</view>
				</view>
				<view class="reviews-content" v-if="reviewList && reviewList.length > 0">
					<view class="review-item" v-for="(item, index) in reviewList" :key="index">
						<view class="reviewer-info">
							<view class="reviewer-detail">
								<image class="reviewer-avatar" :src="item.avatar" mode="aspectFill"></image>
								<view class="reviewer-text">
									<text class="reviewer-name">{{item.name}}</text>
									<text class="review-time">{{item.time}}</text>
								</view>
							</view>
							<view class="review-rating">
								<text class="star" v-for="n in item.rating" :key="n">⭐</text>
								<text class="star-empty" v-for="n in 5-item.rating" :key="n+5">☆</text>
							</view>
						</view>
						<text class="review-content">{{item.content}}</text>
					</view>
				</view>
				<view class="empty-reviews" v-else>
					<text class="empty-text">暂无评价</text>
				</view>
			</view>
		</view>
		
		<!-- 底部按钮 -->
		<view class="bottom-actions">
			<button class="action-btn appointment-btn" @click="goToAppointment">
				<text class="btn-icon">📅</text>
				<text class="btn-text">立即预约</text>
			</button>
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
			async getHospitalDetail(id) {
				try {
					// 这里可以替换为实际的API调用
					console.log('获取医院详情，ID：', id);
					
					// 模拟根据ID获取医院详情
					const hospitalData = {
						1001: {
							id: 1001,
							name: '第一人民医院',
							tags: ['三甲', '综合医院'],
							rating: '4.8',
							phone: '0123-12345678',
							address: '某某市中心路123号',
							latitude: 41.805699,
							longitude: 123.431436,
							introduction: '第一人民医院是一所集医疗、教学、科研、预防、保健、康复为一体的大型综合性三级甲等医院。医院占地面积5万平方米，建筑面积10万平方米，开放床位800张，拥有各类先进医疗设备2000余台（套），其中包括3.0T核磁共振、64排128层CT、直线加速器、伽马刀等大型医疗设备。医院设有临床科室40个，医技科室12个，专科特色突出，综合实力雄厚。',
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
						1002: {
							id: 1002,
							name: '第二人民医院',
							tags: ['三甲', '专业体检'],
							rating: '4.7',
							phone: '0123-87654321',
							address: '某某市健康路456号',
							latitude: 41.812977,
							longitude: 123.445235,
							introduction: '第二人民医院是专业体检中心，设备先进，服务贴心。医院专注于提供高质量的体检服务，拥有先进的医疗设备和专业的医疗团队。医院环境优美，服务周到，是您进行健康体检的理想选择。',
							images: [
								'/static/images/hospital2.jpg',
								'/static/images/hospital3.jpg'
							],
							environment: [
								'/static/images/hospital-env1.jpg',
								'/static/images/hospital-env2.jpg',
								'/static/images/hospital-env3.jpg'
							]
						},
						1: {
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
						'1003': {
							id: '1003',
							name: '内蒙古自治区人民医院',
							tags: ['三甲', '综合医院'],
							rating: '4.7',
							phone: '0471-3283999',
							address: '内蒙古自治区呼和浩特市新城区昭乌达路20号',
							latitude: 40.8429,
							longitude: 111.7494,
							introduction: '内蒙古自治区人民医院是内蒙古自治区规模最大、技术力量最雄厚的综合性三级甲等医院，是内蒙古自治区医疗、教学、科研、预防、保健、康复中心。医院集医疗、教学、科研、预防、保健、康复、急救为一体，拥有先进的医疗设备和专业的医疗团队。',
							images: [
								'/static/images/hospital3.jpg',
								'/static/images/hospital4.jpg'
							],
							environment: [
								'/static/images/hospital-env1.jpg',
								'/static/images/hospital-env2.jpg',
								'/static/images/hospital-env3.jpg'
							]
						},
						'1004': {
							id: '1004',
							name: '内蒙古医科大学附属医院',
							tags: ['三甲', '综合医院'],
							rating: '4.5',
							phone: '0471-3451120',
							address: '呼和浩特市回民区通道北路1号',
							latitude: 40.8429,
							longitude: 111.7494,
							introduction: '内蒙古医科大学附属医院是一所集医疗、教学、科研、预防、保健、康复为一体的综合性三级甲等医院，是内蒙古医科大学的主要临床教学基地。',
							images: [
								'/static/images/hospital4.jpg',
								'/static/images/hospital1.jpg'
							],
							environment: [
								'/static/images/hospital-env1.jpg',
								'/static/images/hospital-env2.jpg',
								'/static/images/hospital-env3.jpg'
							]
						},
						2: {
							id: 2,
							name: '沈阳市云医院-沈河分院',
							tags: ['三甲', '综合医院'],
							rating: '4.6',
							phone: '024-87654321',
							address: '沈阳市沈河区北站路36号',
							latitude: 41.812977,
							longitude: 123.445235,
							introduction: '沈阳市云医院-沈河分院是沈河区重点医疗机构，拥有先进的医疗设备和专业的医疗团队。医院专注于提供高质量的医疗服务，包括体检、治疗、康复等全方位医疗服务。',
							images: [
								'/static/images/hospital2.jpg',
								'/static/images/hospital3.jpg'
							],
							environment: [
								'/static/images/hospital-env1.jpg',
								'/static/images/hospital-env2.jpg',
								'/static/images/hospital-env3.jpg'
							]
						},
						3: {
							id: 3,
							name: '内蒙古自治区人民医院',
							tags: ['三甲', '综合医院'],
							rating: '4.7',
							phone: '0471-3283999',
							address: '内蒙古呼和浩特市昭乌达路20号',
							latitude: 40.8429,
							longitude: 111.7494,
							introduction: '内蒙古自治区人民医院是内蒙古自治区规模最大、技术力量最雄厚的综合性三级甲等医院，是内蒙古自治区医疗、教学、科研、预防、保健、康复中心。',
							images: [
								'/static/images/hospital3.jpg',
								'/static/images/hospital4.jpg'
							],
							environment: [
								'/static/images/hospital-env1.jpg',
								'/static/images/hospital-env2.jpg',
								'/static/images/hospital-env3.jpg'
							]
						},
						4: {
							id: 4,
							name: '内蒙古医科大学附属医院',
							tags: ['三甲', '综合医院'],
							rating: '4.5',
							phone: '0471-3451120',
							address: '呼和浩特市回民区通道北路1号',
							latitude: 40.8429,
							longitude: 111.7494,
							introduction: '内蒙古医科大学附属医院是一所集医疗、教学、科研、预防、保健、康复为一体的综合性三级甲等医院，是内蒙古医科大学的主要临床教学基地。',
							images: [
								'/static/images/hospital4.jpg',
								'/static/images/hospital1.jpg'
							],
							environment: [
								'/static/images/hospital-env1.jpg',
								'/static/images/hospital-env2.jpg',
								'/static/images/hospital-env3.jpg'
							]
						}
					};
					
					// 根据ID获取医院信息
					if (hospitalData[id]) {
						this.hospital = hospitalData[id];
					} else {
						// 如果找不到对应ID的医院，使用默认数据
						this.hospital = hospitalData[1];
						uni.showToast({
							title: '医院信息加载失败，显示默认信息',
							icon: 'none'
						});
					}
				} catch (error) {
					console.error('获取医院详情失败:', error);
					uni.showToast({
						title: '获取医院详情失败',
						icon: 'none'
					});
				}
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
				// 存储选择的医院信息
				uni.setStorageSync('selectedHospital', JSON.stringify(this.hospital));
				
				// 存储选择的套餐信息
				uni.setStorageSync('selectedPackage', JSON.stringify(pkg));
				
				// 跳转到预约流程页面
				uni.navigateTo({
					url: '/pages/appointment/appointment-flow'
				});
			},
			// 页面跳转
			navigateTo(url) {
				uni.navigateTo({
					url: url
				});
			},
			// 跳转到预约页面
			goToAppointment() {
				// 存储选择的医院信息
				uni.setStorageSync('selectedHospital', JSON.stringify(this.hospital));
				
				// 跳转到预约方式选择页面
				uni.navigateTo({
					url: '/pages/appointment/appointment-select'
				});
			}
		}
	}
</script>

<style lang="scss">
.content {
	background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
	min-height: 100vh;
	padding-bottom: 120rpx;
	position: relative; /* For floating shapes */
	overflow: hidden; /* Hide overflow for floating shapes */
}

.floating-shapes {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	z-index: -1; /* Ensure shapes are behind content */
	
	.shape {
		position: absolute;
		background: linear-gradient(45deg, rgba(9, 132, 227, 0.1), rgba(116, 185, 255, 0.05));
		border-radius: 50%;
		animation: float 10s infinite ease-in-out;
	}
	
	.shape-1 {
		width: 100px;
		height: 100px;
		top: 10%;
		left: 10%;
		animation-delay: -2s;
	}
	
	.shape-2 {
		width: 150px;
		height: 150px;
		top: 70%;
		right: 10%;
		animation-delay: -4s;
	}
	
	.shape-3 {
		width: 200px;
		height: 200px;
		bottom: 20%;
		left: 30%;
		animation-delay: -6s;
	}
	
	.shape-4 {
		width: 120px;
		height: 120px;
		bottom: 50%;
		right: 20%;
		animation-delay: -8s;
	}
}

.main-content {
	padding: 20rpx; /* Adjust padding for main content */
}

.hospital-banner-section {
	position: relative;
	margin-bottom: 20rpx;
	
	.hospital-banner {
		width: 100%;
		height: 400rpx;
		border-radius: 15rpx;
		overflow: hidden;
		box-shadow: 0 5rpx 15rpx rgba(0, 0, 0, 0.1);
	}
	
	.banner-overlay {
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		background: linear-gradient(to bottom, rgba(0, 0, 0, 0.6), rgba(0, 0, 0, 0.2));
		border-radius: 15rpx;
		display: flex;
		justify-content: center;
		align-items: flex-end;
		padding-bottom: 40rpx;
	}
	
	.banner-content {
		text-align: center;
		color: #ffffff;
		
		.banner-title {
			font-size: 48rpx;
			font-weight: bold;
			margin-bottom: 10rpx;
			text-shadow: 2rpx 2rpx 4rpx rgba(0, 0, 0, 0.5);
		}
		
		.banner-rating {
			display: flex;
			align-items: baseline;
			font-size: 30rpx;
			color: #ffc107; /* Gold color for rating */
			text-shadow: 1rpx 1rpx 2rpx rgba(0, 0, 0, 0.5);
			
			.rating-score {
				font-size: 40rpx;
				font-weight: bold;
				margin-right: 10rpx;
			}
			
			.rating-text {
				font-size: 28rpx;
				color: #ffffff;
			}
		}
	}
}

.hospital-info-card,
.contact-card,
.intro-card,
.package-card,
.environment-card,
.reviews-card {
	background-color: #ffffff;
	border-radius: 15rpx;
	padding: 30rpx;
	margin-bottom: 20rpx;
	box-shadow: 0 5rpx 15rpx rgba(0, 0, 0, 0.08);
	
	.card-header {
		display: flex;
		align-items: center;
		margin-bottom: 20rpx;
		
		.header-icon {
			font-size: 40rpx;
			margin-right: 15rpx;
			color: #1296db; /* Primary color for icons */
		}
		
		.header-title {
			font-size: 36rpx;
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
	}
}

.hospital-tags {
	display: flex;
	flex-wrap: wrap;
	margin-bottom: 20rpx;
	
	.tag {
		font-size: 24rpx;
		color: #1296db;
		background-color: rgba(18, 150, 219, 0.1);
		padding: 8rpx 16rpx;
		border-radius: 10rpx;
		margin-right: 10rpx;
		margin-bottom: 10rpx;
		font-weight: bold;
	}
}

.contact-content {
	.contact-item {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 15rpx 0;
		border-bottom: 1px solid #f5f5f5;
		
		&:last-child {
			border-bottom: none;
		}
		
		.contact-icon {
			font-size: 36rpx;
			color: #1296db;
			margin-right: 15rpx;
		}
		
		.contact-detail {
			flex: 1;
			
			.contact-label {
				font-size: 26rpx;
				color: #666666;
				margin-bottom: 5rpx;
			}
			
			.contact-text {
				font-size: 28rpx;
				color: #333333;
				font-weight: bold;
			}
		}
		
		.contact-arrow {
			font-size: 32rpx;
			color: #999999;
			margin-left: 10rpx;
		}
	}
}

.intro-content {
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

.package-content {
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

.environment-content {
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
}

.reviews-content {
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
			
			.reviewer-text {
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
				.star {
					font-size: 24rpx;
					color: #ff9500; /* Gold color for stars */
				}
				
				.star-empty {
					font-size: 24rpx;
					color: #dddddd; /* Gray color for empty stars */
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
	text-align: center;
	padding: 40rpx 0;
	color: #999999;
	font-size: 28rpx;
}

.bottom-actions {
	position: fixed;
	left: 0;
	right: 0;
	bottom: 0;
	padding: 20rpx;
	background-color: #ffffff;
	box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.05);
	z-index: 10; /* Ensure it's above floating shapes */
	
	.action-btn {
		height: 90rpx;
		line-height: 90rpx;
		background: linear-gradient(to right, #1296db, #007bff); /* Gradient background */
		color: #ffffff;
		font-size: 32rpx;
		border-radius: 45rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		box-shadow: 0 5rpx 15rpx rgba(0, 0, 0, 0.1);
		
		&::after {
			border: none;
		}
		
		.btn-icon {
			font-size: 36rpx;
			margin-right: 10rpx;
		}
		
		.btn-text {
			font-weight: bold;
		}
	}
}

/* 卡片头部样式 */
.card-header {
	display: flex;
	align-items: center;
	margin-bottom: 20rpx;
	
	.header-left {
		display: flex;
		align-items: center;
		flex: 1;
	}
	
	.header-right {
		display: flex;
		align-items: center;
	}
	
	.header-icon {
		font-size: 40rpx;
		margin-right: 15rpx;
		color: #1296db;
	}
	
	.header-title {
		font-size: 36rpx;
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
	
	.more-btn {
		display: flex;
		align-items: center;
		font-size: 26rpx;
		color: #999999;
		
		.arrow-icon {
			font-size: 24rpx;
			margin-left: 5rpx;
		}
	}
}

/* 套餐内容样式 */
.package-content {
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
				margin-bottom: 8rpx;
			}
			
			.package-desc {
				font-size: 24rpx;
				color: #666666;
				margin-bottom: 10rpx;
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
			background-color: #1296db;
			color: #ffffff;
			padding: 10rpx 20rpx;
			border-radius: 20rpx;
			font-size: 24rpx;
		}
	}
}

/* 评价者信息样式 */
.reviewer-detail {
	display: flex;
	align-items: center;
	flex: 1;
	
	.reviewer-avatar {
		width: 60rpx;
		height: 60rpx;
		border-radius: 50%;
		margin-right: 15rpx;
	}
	
	.reviewer-text {
		flex: 1;
		
		.reviewer-name {
			font-size: 28rpx;
			color: #333333;
			margin-bottom: 5rpx;
			display: block;
		}
		
		.review-time {
			font-size: 22rpx;
			color: #999999;
			display: block;
		}
	}
}

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
</style> 