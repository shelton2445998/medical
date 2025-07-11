<template>
	<view class="content">
		<!-- 搜索框 -->
		<view class="search-box">
			<text class="iconfont icon-search"></text>
			<input type="text" v-model="searchKeyword" placeholder="搜索医院名称" @input="searchHospitals" />
		</view>
		
		<!-- 医院列表 -->
		<view class="hospital-list">
			<!-- 加载状态 -->
			<view v-if="loading" class="loading-container">
				<text class="loading-text">正在加载医院列表...</text>
			</view>
			
			<!-- 空状态 -->
			<view v-else-if="filteredHospitals.length === 0" class="empty-container">
				<text class="empty-text">暂无医院数据</text>
			</view>
			
			<!-- 医院列表 -->
			<view v-else>
				<view class="hospital-item" v-for="(item, index) in filteredHospitals" :key="index" @click="selectHospital(item)">
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
				let packageUrl = '/pages/package/package';
				if (this.memberId && this.memberName) {
					packageUrl += `?memberId=${this.memberId}&memberName=${this.memberName}`;
				}
				
				// 跳转到套餐选择页面
				uni.navigateTo({
					url: packageUrl
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
	background-color: #f5f5f5;
	min-height: 100vh;
}

.btn-icon {
  width: 40rpx;
  height: 40rpx;
  margin-right: 10rpx;
  vertical-align: middle;
}

.search-box {
	display: flex;
	align-items: center;
	background-color: #ffffff;
	padding: 20rpx 30rpx;
	border-radius: 10rpx;
	margin: 20rpx;
	box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
	
	.iconfont {
		font-size: 40rpx;
		color: #999999;
		margin-right: 20rpx;
	}
	
	input {
		flex: 1;
		font-size: 28rpx;
		color: #333333;
	}
}

.hospital-list {
	padding: 0 20rpx;
	
	.loading-container, .empty-container {
		display: flex;
		justify-content: center;
		align-items: center;
		padding: 100rpx 0;
		
		.loading-text, .empty-text {
			font-size: 28rpx;
			color: #999999;
		}
	}
	
	.hospital-item {
		display: flex;
		background-color: #ffffff;
		border-radius: 10rpx;
		margin-bottom: 20rpx;
		padding: 30rpx;
		box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
		position: relative;
		
		.hospital-image {
			width: 180rpx;
			height: 180rpx;
			border-radius: 10rpx;
			margin-right: 20rpx;
		}
		
		.hospital-info {
			flex: 1;
			
			.hospital-header {
				display: flex;
				align-items: center;
				margin-bottom: 10rpx;
				
				.hospital-name {
					font-size: 32rpx;
					font-weight: bold;
					color: #333333;
					margin-right: 20rpx;
				}
				
				.hospital-tag {
					font-size: 22rpx;
					color: #1296db;
					background-color: rgba(18, 150, 219, 0.1);
					padding: 4rpx 10rpx;
					border-radius: 6rpx;
				}
			}
			
			.hospital-address, .hospital-time, .hospital-phone {
				display: flex;
				align-items: center;
				font-size: 24rpx;
				color: #666666;
				margin-bottom: 10rpx;
				
				.iconfont {
					font-size: 28rpx;
					color: #999999;
					margin-right: 10rpx;
				}
			}
			
			.hospital-actions {
				display: flex;
				margin-top: 20rpx;
				
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
					
					.iconfont {
						font-size: 28rpx;
						margin-right: 6rpx;
					}
					
					&::after {
						border: none;
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
		}
	}
}
</style> 