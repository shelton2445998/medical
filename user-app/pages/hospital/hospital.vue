<template>
	<view class="content">
		<!-- 动态背景装饰 -->
		<view class="floating-shapes">
			<view class="shape shape-1"></view>
			<view class="shape shape-2"></view>
			<view class="shape shape-3"></view>
			<view class="shape shape-4"></view>
		</view>
		
		<!-- 搜索框 -->
		<view class="search-box">
			<text class="iconfont icon-search"></text>
			<input type="text" v-model="searchKeyword" placeholder="搜索医院名称" @input="searchHospitals" />
		</view>
		
		<!-- 医院列表 -->
		<view class="hospital-list">
			<!-- 加载状态 -->
			<view v-if="loading" class="loading-container">
				<view class="loading-spinner"></view>
				<text class="loading-text">正在加载医院列表...</text>
			</view>
			
			<!-- 空状态 -->
			<view v-else-if="filteredHospitals.length === 0" class="empty-container">
				<view class="empty-icon">🏥</view>
				<text class="empty-text">暂无医院数据</text>
			</view>
			
			<!-- 医院列表 -->
			<view v-else>
				<view 
					class="hospital-item" 
					v-for="(item, index) in filteredHospitals" 
					:key="index" 
					@click="selectHospital(item)"
					:style="{ animationDelay: index * 0.1 + 's' }"
				>
					<image class="hospital-image" :src="item.image" mode="aspectFill"></image>
					<view class="hospital-info">
						<view class="hospital-header">
							<text class="hospital-name">{{item.name}}</text>
							<text class="hospital-tag">{{item.tag}}</text>
						</view>
						<view class="hospital-address">
							<text class="iconfont icon-location"></text>
							<text>{{item.address}}</text>
						</view>
						<view class="hospital-time">
							<text class="iconfont icon-time"></text>
							<text>{{item.time}}</text>
						</view>
						<view class="hospital-phone">
							<text class="iconfont icon-phone"></text>
							<text>{{item.phone}}</text>
						</view>
						<view class="hospital-actions">
							<button class="call-btn" @click.stop="callHospital(item.phone)">
								<image src="/static/icon/dial1.png" mode="aspectFit" class="btn-icon"></image>
								<text>拨打电话</text>
							</button>
							<button class="map-btn" @click.stop="openMap(item)">
								<image src="/static/icon/map.png" mode="aspectFit" class="btn-icon"></image>
								<text>查看地图</text>
							</button>
						</view>
					</view>
					<text class="iconfont icon-arrow-right"></text>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import { get, hospitalApi } from '@/utils/request.js';
	
	export default {
		data() {
			return {
				searchKeyword: '',
				hospitals: [],
				filteredHospitals: [],
				loading: false,
				memberId: null,
				memberName: ''
			}
		},
		onLoad(options) {
			// 获取家庭成员信息
			if (options.memberId) {
				this.memberId = options.memberId;
			}
			if (options.memberName) {
				this.memberName = options.memberName;
			}
			// 获取医院列表数据
			this.getHospitalList();
		},
		methods: {
			// 获取医院列表
			async getHospitalList() {
				this.loading = true;
				
				try {
					const result = await get(hospitalApi.getHospitalList, {
						keyword: '',
						pageIndex: 1,
						pageSize: 20
					});
					console.log('医院接口返回', result);
					// 兼容后端ApiResult<Paging<AppHospitalVo>>结构
					if (result && result.data && result.data.list) {
						this.hospitals = result.data.list;
					} else if (result && result.data && Array.isArray(result.data)) {
						this.hospitals = result.data;
					} else if (result && result.records) {
						this.hospitals = result.records;
					} else if (Array.isArray(result)) {
						this.hospitals = result;
					} else {
						this.hospitals = [];
					}
					// 处理医院数据，确保有默认图片和必要字段
					this.hospitals.forEach((hospital, index) => {
						const defaultImages = [
							'/static/images/hospital1.jpg',
							'/static/images/hospital2.jpg',
							'/static/images/hospital3.jpg',
							'/static/images/hospital4.jpg'
						];
						hospital.image = defaultImages[index % defaultImages.length];
						if (!hospital.tag) hospital.tag = '三甲';
						if (!hospital.address) hospital.address = '地址信息待完善';
						if (!hospital.time) hospital.time = '上午8:00-12:00，下午14:00-17:00';
						if (!hospital.phone) hospital.phone = '400-123-4567';
						if (!hospital.latitude || !hospital.longitude) {
							hospital.latitude = 41.805699;
							hospital.longitude = 123.431541;
						}
					});
					console.log('最终医院列表', this.hospitals);
					this.filteredHospitals = this.hospitals;
				} catch (error) {
					// 如果接口失败，使用测试数据
					this.hospitals = [
						{
							id: 1,
							name: '沈阳市云医院-和平分院',
							tag: '三甲',
							address: '沈阳市和平区南京街125号',
							time: '上午7:30-11:30，下午13:00-15:30',
							phone: '4008-123-456',
							image: '/static/images/hospital1.jpg',
							latitude: 41.805699,
							longitude: 123.431541
						},
						{
							id: 2,
							name: '沈阳市云医院-沈河分院',
							tag: '三甲',
							address: '沈阳市沈河区北站路36号',
							time: '上午7:30-11:30，下午13:30-16:00',
							phone: '4008-123-789',
							image: '/static/images/hospital2.jpg',
							latitude: 41.812977,
							longitude: 123.445235
						},
						{
							id: 3,
							name: '内蒙古自治区人民医院',
							tag: '三甲',
							address: '内蒙古呼和浩特市昭乌达路20号',
							time: '上午8:00-12:00，下午14:30-17:30',
							phone: '0471-3283999',
							image: '/static/images/hospital3.jpg',
							latitude: 41.812977,
							longitude: 123.445235
						},
						{
							id: 4,
							name: '内蒙古医科大学附属医院',
							tag: '三甲',
							address: '呼和浩特市回民区通道北路1号',
							time: '上午8:00-12:00，下午14:30-17:30',
							phone: '0471-3451120',
							image: '/static/images/hospital4.jpg',
							latitude: 41.812977,
							longitude: 123.445235
						}
					];
					this.filteredHospitals = this.hospitals;
					
					uni.showToast({
						title: '使用测试数据',
						icon: 'none'
					});
				} finally {
					this.loading = false;
				}
			},
			// 搜索医院
			async searchHospitals() {
				if (!this.searchKeyword.trim()) {
					this.getHospitalList();
					return;
				}
				try {
					const result = await get(hospitalApi.getHospitalList, {
						keyword: this.searchKeyword.trim(),
						pageIndex: 1,
						pageSize: 20
					});
					console.log('搜索医院接口返回', result);
					if (result && result.data && result.data.list) {
						this.hospitals = result.data.list;
					} else if (result && result.data && Array.isArray(result.data)) {
						this.hospitals = result.data;
					} else if (result && result.records) {
						this.hospitals = result.records;
					} else if (Array.isArray(result)) {
						this.hospitals = result;
					} else {
						this.hospitals = [];
					}
					this.hospitals.forEach((hospital, index) => {
						const defaultImages = [
							'/static/images/hospital1.jpg',
							'/static/images/hospital2.jpg',
							'/static/images/hospital3.jpg',
							'/static/images/hospital4.jpg'
						];
						hospital.image = defaultImages[index % defaultImages.length];
						if (!hospital.tag) hospital.tag = '三甲';
						if (!hospital.address) hospital.address = '地址信息待完善';
						if (!hospital.time) hospital.time = '上午8:00-12:00，下午14:00-17:00';
						if (!hospital.phone) hospital.phone = '400-123-4567';
						if (!hospital.latitude || !hospital.longitude) {
							hospital.latitude = 41.805699;
							hospital.longitude = 123.431541;
						}
					});
					console.log('最终搜索医院列表', this.hospitals);
					this.filteredHospitals = this.hospitals;
				} catch (error) {
					uni.showToast({
						title: error.message || '搜索医院失败',
						icon: 'none'
					});
				}
			},
			// 选择医院
			selectHospital(hospital) {
				// 存储选择的医院信息
				uni.setStorageSync('selectedHospital', JSON.stringify(hospital));
				
				// 构建跳转URL，包含家庭成员信息
				let selectUrl = '/pages/appointment/appointment-select';
				if (this.memberId && this.memberName) {
					selectUrl += `?memberId=${this.memberId}&memberName=${this.memberName}`;
				}
				
				// 跳转到预约方式选择页面
				uni.navigateTo({
					url: selectUrl
				});
			},
			// 拨打电话
			callHospital(phone) {
				uni.makePhoneCall({
					phoneNumber: phone
				});
			},
			// 打开地图
			openMap(hospital) {
				uni.openLocation({
					latitude: hospital.latitude,
					longitude: hospital.longitude,
					name: hospital.name,
					address: hospital.address,
					scale: 18
				});
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
	z-index: -1;
	
	.shape {
		position: absolute;
		background-color: rgba(255, 255, 255, 0.1);
		border-radius: 50%;
		animation: float 10s infinite ease-in-out;
		transition: all 0.3s ease;
		
		&:hover {
			transform: scale(1.3);
			background-color: rgba(255, 255, 255, 0.2);
		}
		
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
		width: 120px;
		height: 120px;
		bottom: 20%;
		left: 30%;
		animation-delay: -6s;
	}
	
	.shape-4 {
		width: 180px;
		height: 180px;
		bottom: 50%;
		right: 20%;
		animation-delay: -8s;
	}
}

.btn-icon {
  width: 40rpx;
  height: 40rpx;
  margin-right: 10rpx;
  vertical-align: middle;
  transition: all 0.3s ease;
}

.search-box {
	display: flex;
	align-items: center;
	background-color: rgba(255, 255, 255, 0.95);
	padding: 20rpx 30rpx;
	border-radius: 16rpx;
	margin: 20rpx;
	box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
	backdrop-filter: blur(10rpx);
	transition: all 0.3s ease;
	animation: slideInDown 0.8s ease-out;
	position: relative;
	overflow: hidden;
	
	&::before {
		content: '';
		position: absolute;
		top: 0;
		left: -100%;
		width: 100%;
		height: 100%;
		background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.3), transparent);
		animation: shimmer 3s ease-in-out infinite;
	}
	
	&:hover {
		transform: translateY(-2rpx);
		box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.15);
		background-color: rgba(255, 255, 255, 0.98);
	}
	
	.iconfont {
		font-size: 40rpx;
		color: #999999;
		margin-right: 20rpx;
		transition: all 0.3s ease;
		
		&:hover {
			color: #0984e3;
			transform: scale(1.1);
		}
	}
	
	input {
		flex: 1;
		font-size: 28rpx;
		color: #333333;
		transition: all 0.3s ease;
		
		&:focus {
			color: #0984e3;
		}
	}
}

.hospital-list {
	padding: 0 20rpx;
	
	.loading-container {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		padding: 100rpx 0;
		
		.loading-spinner {
			width: 60rpx;
			height: 60rpx;
			border: 4rpx solid rgba(255, 255, 255, 0.3);
			border-top: 4rpx solid #ffffff;
			border-radius: 50%;
			animation: spin 1s linear infinite;
			margin-bottom: 20rpx;
		}
		
		.loading-text {
			font-size: 28rpx;
			color: #ffffff;
			text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.3);
		}
	}
	
	.empty-container {
		display: flex;
		flex-direction: column;
		justify-content: center;
		align-items: center;
		padding: 100rpx 0;
		
		.empty-icon {
			font-size: 80rpx;
			margin-bottom: 20rpx;
			animation: bounce 2s infinite;
			transition: all 0.3s ease;
			
			&:hover {
				transform: scale(1.2);
			}
		}
		
		.empty-text {
			font-size: 28rpx;
			color: #ffffff;
			text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.3);
		}
	}
	
	.hospital-item {
		display: flex;
		background-color: rgba(255, 255, 255, 0.95);
		border-radius: 16rpx;
		margin-bottom: 20rpx;
		padding: 30rpx;
		box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
		backdrop-filter: blur(10rpx);
		position: relative;
		overflow: hidden;
		animation: fadeIn 0.5s ease-out;
		transition: all 0.4s cubic-bezier(0.25, 0.46, 0.45, 0.94);
		cursor: pointer;
		
		&:nth-child(1) { animation-delay: 0.1s; }
		&:nth-child(2) { animation-delay: 0.2s; }
		&:nth-child(3) { animation-delay: 0.3s; }
		&:nth-child(4) { animation-delay: 0.4s; }
		&:nth-child(5) { animation-delay: 0.5s; }
		&:nth-child(6) { animation-delay: 0.6s; }
		&:nth-child(7) { animation-delay: 0.7s; }
		&:nth-child(8) { animation-delay: 0.8s; }
		&:nth-child(9) { animation-delay: 0.9s; }
		&:nth-child(10) { animation-delay: 1s; }
		
		&::before {
			content: '';
			position: absolute;
			top: 0;
			left: -100%;
			width: 100%;
			height: 100%;
			background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.4), transparent);
			transition: left 0.6s ease;
		}
		
		&::after {
			content: '';
			position: absolute;
			top: 0;
			left: 0;
			right: 0;
			bottom: 0;
			background: linear-gradient(135deg, rgba(116, 185, 255, 0.1), rgba(9, 132, 227, 0.1));
			opacity: 0;
			transition: opacity 0.3s ease;
			pointer-events: none;
		}
		
		/* 流动治愈感背景 */
		&::before {
			content: '';
			position: absolute;
			top: -50%;
			left: -50%;
			width: 200%;
			height: 200%;
			background: radial-gradient(circle, rgba(116, 185, 255, 0.1) 0%, transparent 70%);
			animation: flow 15s linear infinite;
			pointer-events: none;
		}
		
		&:hover {
			transform: translateY(-8rpx) scale(1.02);
			box-shadow: 0 12rpx 32rpx rgba(0, 0, 0, 0.2);
			background-color: rgba(255, 255, 255, 0.98);
			
			&::before {
				left: 100%;
			}
			
			&::after {
				opacity: 1;
			}
			
			.hospital-name {
				color: #0984e3;
				transform: translateX(5rpx);
			}
			
			.hospital-tag {
				transform: scale(1.1);
			}
			
			.hospital-image {
				transform: scale(1.05);
			}
		}
		
		&:active {
			transform: translateY(-4rpx) scale(0.98);
			transition: all 0.1s ease;
		}
		
		.hospital-image {
			width: 180rpx;
			height: 180rpx;
			border-radius: 10rpx;
			margin-right: 20rpx;
			transition: all 0.3s ease;
			box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
		}
		
		.hospital-info {
			flex: 1;
			
			.hospital-header {
				display: flex;
				align-items: center;
				margin-bottom: 10rpx;
				transition: all 0.3s ease;
				
				.hospital-name {
					font-size: 32rpx;
					font-weight: bold;
					color: #333333;
					margin-right: 20rpx;
					transition: all 0.3s ease;
				}
				
				.hospital-tag {
					font-size: 22rpx;
					color: #0984e3;
					background-color: rgba(9, 132, 227, 0.1);
					padding: 4rpx 10rpx;
					border-radius: 6rpx;
					transition: all 0.3s ease;
					animation: pulse 2s infinite;
				}
			}
			
			.hospital-address, .hospital-time, .hospital-phone {
				display: flex;
				align-items: center;
				font-size: 24rpx;
				color: #666666;
				margin-bottom: 10rpx;
				transition: all 0.3s ease;
				
				.iconfont {
					font-size: 28rpx;
					color: #999999;
					margin-right: 10rpx;
					transition: all 0.3s ease;
				}
				
				&:hover {
					color: #0984e3;
					
					.iconfont {
						color: #0984e3;
						transform: scale(1.1);
					}
				}
			}
			
			.hospital-actions {
				display: flex;
				margin-top: 20rpx;
				transition: all 0.3s ease;
				
				button {
					display: flex;
					align-items: center;
					justify-content: center;
					font-size: 24rpx;
					color: #1296db;
					background-color: rgba(18, 150, 219, 0.1);
					border: none;
					border-radius: 30rpx;
					padding: 0 20rpx;
					margin-right: 20rpx;
					height: 60rpx;
					line-height: 1;
					transition: all 0.3s ease;
					cursor: pointer;
					
					.iconfont {
						font-size: 28rpx;
						margin-right: 6rpx;
						transition: all 0.3s ease;
					}
					
					&::after {
						border: none;
					}
					
					&:hover {
						background-color: rgba(18, 150, 219, 0.2);
						transform: translateY(-3rpx) scale(1.05);
						box-shadow: 0 4rpx 12rpx rgba(18, 150, 219, 0.3);
					}
					
					&:active {
						transform: translateY(-1rpx) scale(0.98);
					}
				}
			}
		}
		
		.icon-arrow-right {
			position: absolute;
			right: 30rpx;
			top: 50%;
			transform: translateY(-50%);
			font-size: 40rpx;
			color: #cccccc;
			transition: all 0.3s ease;
		}
		
		&:hover .icon-arrow-right {
			color: #0984e3;
			transform: translateY(-50%) translateX(5rpx);
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
		transform: translateY(0) translateX(0) scale(1);
		opacity: 0.8;
	}
	75% {
		transform: translateY(10px) translateX(-10px) scale(1.05);
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

@keyframes fadeIn {
	from {
		opacity: 0;
		transform: translateY(20px);
	}
	to {
		opacity: 1;
		transform: translateY(0);
	}
}

@keyframes slideInDown {
	from {
		opacity: 0;
		transform: translateY(-30rpx);
	}
	to {
		opacity: 1;
		transform: translateY(0);
	}
}

@keyframes spin {
	0% {
		transform: rotate(0deg);
	}
	100% {
		transform: rotate(360deg);
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

@keyframes pulse {
	0% {
		transform: scale(1);
	}
	50% {
		transform: scale(1.05);
	}
	100% {
		transform: scale(1);
	}
}
</style> 