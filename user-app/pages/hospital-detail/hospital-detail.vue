<!--
@name: 医院详情页面
@description: 显示医院的详细信息，包括基本信息、联系方式、介绍、体检套餐、医院环境、用户评价等
@author: 医疗系统开发团队
@created: 2024年医疗系统开发
@features:
- 显示医院基本信息（名称、评分、标签）
- 展示医院联系方式（电话、地址）
- 支持一键拨打电话和导航功能
- 显示医院介绍和图片
- 展示体检套餐列表
- 显示医院环境图片
- 展示用户评价信息
- 支持预约功能
@页面路径: /pages/hospital-detail/hospital-detail
@参数说明:
- id: 医院ID，用于获取医院详情
-->

<template>
	<view class="content">
		<!-- 
			动态背景装饰
			添加浮动的装饰性元素增强视觉效果
		-->
		<view class="floating-shapes">
			<view class="shape shape-1"></view>
			<view class="shape shape-2"></view>
			<view class="shape shape-3"></view>
			<view class="shape shape-4"></view>
		</view>
		
		<view class="main-content">
			<!-- 
				医院封面图片区域
				显示医院横幅图片和基本信息
			-->
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
			
			<!-- 
				医院基本信息卡片
				显示医院标签等基本信息
			-->
			<view class="hospital-info-card">
				<view class="card-header">
					<view class="header-icon">🏥</view>
					<view class="header-title">医院信息</view>
				</view>
				<view class="hospital-tags">
					<text class="tag" v-for="(tag, index) in hospital.tags" :key="index">{{tag}}</text>
				</view>
			</view>
			
			<!-- 
				医院联系信息卡片
				显示医院电话和地址，支持一键拨打和导航
			-->
			<view class="contact-card">
				<view class="card-header">
					<view class="header-icon">📞</view>
					<view class="header-title">联系信息</view>
				</view>
				<view class="contact-content">
					<!-- 电话联系项 -->
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
					<!-- 地址导航项 -->
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
			
			<!-- 
				医院介绍卡片
				显示医院详细介绍文本和相关图片
			-->
			<view class="intro-card">
				<view class="card-header">
					<view class="header-icon">📋</view>
					<view class="header-title">医院介绍</view>
				</view>
				<view class="intro-content">
					<text class="intro-text">{{hospital.introduction}}</text>
					<!-- 医院图片展示 -->
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
			
			<!-- 
				体检套餐卡片
				显示医院提供的体检套餐列表
			-->
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
					<!-- 套餐列表项 -->
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
			
			<!-- 
				医院环境卡片
				展示医院环境图片，支持横向滚动
			-->
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
			
			<!-- 
				用户评价卡片
				显示用户对医院的评价和评分
			-->
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
							<!-- 评分星级 -->
							<view class="rating-stars">
								<text class="star" v-for="i in 5" :key="i" :class="{ active: i <= item.rating }">★</text>
							</view>
						</view>
						<text class="review-content">{{item.content}}</text>
					</view>
				</view>
				<!-- 暂无评价提示 -->
				<view class="no-reviews" v-else>
					<text class="no-reviews-text">暂无用户评价</text>
				</view>
			</view>
		</view>
		
		<!-- 
			底部操作栏
			提供预约和导航功能
		-->
		<view class="bottom-actions">
			<button class="action-btn secondary" @click="openLocation(hospital.latitude, hospital.longitude)">
				<text class="btn-icon">📍</text>
				<text>导航</text>
			</button>
			<button class="action-btn primary" @click="goToAppointment">
				<text class="btn-icon">📅</text>
				<text>立即预约</text>
			</button>
		</view>
	</view>
</template>

<script>
	export default {
		name: 'HospitalDetail',
		
		/**
		 * 组件数据
		 * @returns {Object} 组件数据对象
		 */
		data() {
			return {
				// 医院基本信息
				hospital: {
					id: '',
					name: '',
					tags: [],
					rating: '',
					phone: '',
					address: '',
					latitude: 0,
					longitude: 0,
					introduction: '',
					images: [],
					environment: []
				},
				
				// 体检套餐列表
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
				
				// 用户评价列表
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
		
		/**
		 * 页面加载时的处理
		 * @param {Object} options 页面参数
		 */
		onLoad(options) {
			// 获取医院ID
			const hospitalId = options.id;
			// 根据ID获取医院详情
			this.getHospitalDetail(hospitalId);
		},
		
		methods: {
			/**
			 * 获取医院详情
			 * @param {string} id 医院ID
			 */
			async getHospitalDetail(id) {
				try {
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
			
			/**
			 * 拨打电话
			 * @param {string} phone 电话号码
			 */
			makePhoneCall(phone) {
				uni.makePhoneCall({
					phoneNumber: phone
				});
			},
			
			/**
			 * 打开地图导航
			 * @param {number} latitude 纬度
			 * @param {number} longitude 经度
			 */
			openLocation(latitude, longitude) {
				uni.openLocation({
					latitude: latitude,
					longitude: longitude,
					name: this.hospital.name,
					address: this.hospital.address,
					scale: 18
				});
			},
			
			/**
			 * 预览医院图片
			 * @param {number} index 图片索引
			 */
			previewImage(index) {
				uni.previewImage({
					current: index,
					urls: this.hospital.images
				});
			},
			
			/**
			 * 预览环境图片
			 * @param {number} index 图片索引
			 */
			previewEnvironment(index) {
				uni.previewImage({
					current: index,
					urls: this.hospital.environment
				});
			},
			
			/**
			 * 选择套餐
			 * @param {Object} pkg 套餐对象
			 */
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
			
			/**
			 * 页面跳转
			 * @param {string} url 跳转地址
			 */
			navigateTo(url) {
				uni.navigateTo({
					url: url
				});
			},
			
			/**
			 * 跳转到预约页面
			 */
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
/* 
	页面整体样式
	设置页面背景和基本布局
*/
.content {
	background: linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%);
	min-height: 100vh;
	padding-bottom: 120rpx;
	position: relative;
	overflow: hidden;
}

/* 
	浮动装饰元素
	增强页面视觉效果
*/
.floating-shapes {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	z-index: -1;
	
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

/* 
	主要内容区域
	设置内容的布局和间距
*/
.main-content {
	position: relative;
	z-index: 1;
}

/* 
	医院横幅区域
	显示医院主图和基本信息
*/
.hospital-banner-section {
	position: relative;
	height: 400rpx;
	border-radius: 20rpx;
	overflow: hidden;
	margin: 20rpx;
	
	.hospital-banner {
		width: 100%;
		height: 100%;
	}
	
	.banner-overlay {
		position: absolute;
		bottom: 0;
		left: 0;
		right: 0;
		background: linear-gradient(transparent, rgba(0, 0, 0, 0.6));
		padding: 40rpx 30rpx 30rpx 30rpx;
		
		.banner-content {
			color: white;
			
			.banner-title {
				font-size: 36rpx;
				font-weight: bold;
				margin-bottom: 10rpx;
			}
			
			.banner-rating {
				display: flex;
				align-items: center;
				gap: 10rpx;
				
				.rating-score {
					font-size: 32rpx;
					font-weight: bold;
					color: #ffd700;
				}
				
				.rating-text {
					font-size: 24rpx;
					color: rgba(255, 255, 255, 0.8);
				}
			}
		}
	}
}

/* 
	通用卡片样式
	统一的卡片样式设置
*/
.hospital-info-card,
.contact-card,
.intro-card,
.package-card,
.environment-card,
.reviews-card {
	background: white;
	border-radius: 20rpx;
	margin: 20rpx;
	padding: 30rpx;
	box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);
	
	.card-header {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-bottom: 30rpx;
		
		.header-left {
			display: flex;
			align-items: center;
		}
		
		.header-icon {
			font-size: 32rpx;
			margin-right: 15rpx;
			color: #0984e3;
		}
		
		.header-title {
			font-size: 32rpx;
			font-weight: bold;
			color: #333;
		}
		
		.header-right {
			.more-btn {
				display: flex;
				align-items: center;
				gap: 5rpx;
				color: #0984e3;
				font-size: 24rpx;
				
				.arrow-icon {
					font-size: 20rpx;
				}
			}
		}
	}
}

/* 
	医院标签样式
	显示医院特色标签
*/
.hospital-tags {
	display: flex;
	flex-wrap: wrap;
	gap: 15rpx;
	
	.tag {
		background: linear-gradient(135deg, #0984e3, #74b9ff);
		color: white;
		padding: 10rpx 20rpx;
		border-radius: 25rpx;
		font-size: 24rpx;
		font-weight: bold;
	}
}

/* 
	联系信息样式
	显示电话和地址信息
*/
.contact-content {
	.contact-item {
		display: flex;
		align-items: center;
		padding: 20rpx 0;
		border-bottom: 1px solid #f0f0f0;
		
		&:last-child {
			border-bottom: none;
		}
		
		.contact-icon {
			font-size: 28rpx;
			margin-right: 20rpx;
			color: #0984e3;
		}
		
		.contact-detail {
			flex: 1;
			
			.contact-label {
				display: block;
				font-size: 24rpx;
				color: #666;
				margin-bottom: 5rpx;
			}
			
			.contact-text {
				font-size: 28rpx;
				color: #333;
			}
		}
		
		.contact-arrow {
			.arrow-icon {
				font-size: 24rpx;
				color: #0984e3;
			}
		}
	}
}

/* 
	医院介绍样式
	显示医院介绍文本和图片
*/
.intro-content {
	.intro-text {
		font-size: 28rpx;
		line-height: 1.6;
		color: #333;
		margin-bottom: 30rpx;
	}
	
	.hospital-images {
		display: flex;
		gap: 15rpx;
		
		.hospital-image {
			width: 150rpx;
			height: 150rpx;
			border-radius: 10rpx;
		}
	}
}

/* 
	套餐列表样式
	显示体检套餐信息
*/
.package-content {
	.package-item {
		display: flex;
		align-items: center;
		justify-content: space-between;
		padding: 25rpx 0;
		border-bottom: 1px solid #f0f0f0;
		
		&:last-child {
			border-bottom: none;
		}
		
		.package-info {
			flex: 1;
			
			.package-name {
				display: block;
				font-size: 30rpx;
				font-weight: bold;
				color: #333;
				margin-bottom: 10rpx;
			}
			
			.package-desc {
				display: block;
				font-size: 24rpx;
				color: #666;
				margin-bottom: 15rpx;
			}
			
			.package-price-box {
				display: flex;
				align-items: center;
				gap: 15rpx;
				
				.package-price {
					font-size: 32rpx;
					font-weight: bold;
					color: #ff4757;
				}
				
				.package-original-price {
					font-size: 24rpx;
					color: #999;
					text-decoration: line-through;
				}
			}
		}
		
		.package-btn {
			background: linear-gradient(135deg, #0984e3, #74b9ff);
			color: white;
			padding: 15rpx 30rpx;
			border-radius: 25rpx;
			font-size: 26rpx;
			font-weight: bold;
		}
	}
}

/* 
	环境图片样式
	显示医院环境图片
*/
.environment-content {
	.environment-scroll {
		white-space: nowrap;
		
		.environment-image {
			width: 200rpx;
			height: 150rpx;
			border-radius: 10rpx;
			margin-right: 15rpx;
			display: inline-block;
		}
	}
}

/* 
	用户评价样式
	显示用户评价信息
*/
.reviews-content {
	.review-item {
		padding: 25rpx 0;
		border-bottom: 1px solid #f0f0f0;
		
		&:last-child {
			border-bottom: none;
		}
		
		.reviewer-info {
			display: flex;
			align-items: center;
			justify-content: space-between;
			margin-bottom: 15rpx;
			
			.reviewer-detail {
				display: flex;
				align-items: center;
				
				.reviewer-avatar {
					width: 60rpx;
					height: 60rpx;
					border-radius: 50%;
					margin-right: 15rpx;
				}
				
				.reviewer-text {
					.reviewer-name {
						display: block;
						font-size: 28rpx;
						font-weight: bold;
						color: #333;
						margin-bottom: 5rpx;
					}
					
					.review-time {
						font-size: 24rpx;
						color: #666;
					}
				}
			}
			
			.rating-stars {
				.star {
					font-size: 24rpx;
					color: #ddd;
					margin-right: 2rpx;
					
					&.active {
						color: #ffd700;
					}
				}
			}
		}
		
		.review-content {
			font-size: 26rpx;
			line-height: 1.6;
			color: #333;
		}
	}
}

/* 
	无评价提示样式
*/
.no-reviews {
	text-align: center;
	padding: 50rpx 0;
	
	.no-reviews-text {
		font-size: 28rpx;
		color: #999;
	}
}

/* 
	底部操作栏样式
	固定在底部的操作按钮
*/
.bottom-actions {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;
	background: white;
	padding: 20rpx 30rpx;
	box-shadow: 0 -2rpx 10rpx rgba(0, 0, 0, 0.1);
	display: flex;
	gap: 20rpx;
	
	.action-btn {
		flex: 1;
		height: 88rpx;
		border: none;
		border-radius: 44rpx;
		font-size: 28rpx;
		font-weight: bold;
		display: flex;
		align-items: center;
		justify-content: center;
		
		.btn-icon {
			font-size: 24rpx;
			margin-right: 10rpx;
		}
		
		&.secondary {
			background: #f8f9fa;
			color: #0984e3;
		}
		
		&.primary {
			background: linear-gradient(135deg, #0984e3, #74b9ff);
			color: white;
		}
	}
}

/* 
	动画效果定义
*/
@keyframes float {
	0%, 100% { transform: translateY(0px); }
	50% { transform: translateY(-20px); }
}
</style> 