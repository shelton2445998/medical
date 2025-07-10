<template>
	<view class="content">
		<!-- 搜索框 -->
		<view class="search-box">
			<text class="iconfont icon-search"></text>
			<input type="text" v-model="searchKeyword" placeholder="搜索医院名称" @input="searchHospitals" />
		</view>
		
		<!-- 医院列表 -->
		<view class="hospital-list">
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
							<text class="iconfont icon-map"></text>
							<text>查看地图</text>
						</button>
					</view>
				</view>
				<text class="iconfont icon-arrow-right"></text>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				searchKeyword: '',
				hospitals: [
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
						time: '上午8:00-12:00 ，下午14:30-17:30',
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
						time: '上午8:00-12:00 ，下午14:30-17:30',
						phone: '0471-3451120',
						image: '/static/images/hospital4.jpg',
						latitude: 41.812977,
						longitude: 123.445235
					}
				],
				filteredHospitals: []
			}
		},
		onLoad() {
			this.filteredHospitals = this.hospitals;
			// 获取医院列表数据
			this.getHospitalList();
		},
		methods: {
			// 获取医院列表
			getHospitalList() {
				// 这里可以替换为实际的API调用
				console.log('获取医院列表');
			},
			// 搜索医院
			searchHospitals() {
				if (!this.searchKeyword) {
					this.filteredHospitals = this.hospitals;
					return;
				}
				
				this.filteredHospitals = this.hospitals.filter(item => {
					return item.name.includes(this.searchKeyword) || 
						   item.address.includes(this.searchKeyword);
				});
			},
			// 选择医院
			selectHospital(hospital) {
				// 存储选择的医院信息
				uni.setStorageSync('selectedHospital', JSON.stringify(hospital));
				
				// 跳转到套餐选择页面
				uni.navigateTo({
					url: '/pages/package/package'
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