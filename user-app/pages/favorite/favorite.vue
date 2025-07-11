<template>
	<view class="content">
		<!-- 收藏医生列表 -->
		<view class="favorite-list" v-if="favoriteList.length > 0">
			<view class="doctor-item" v-for="(doctor, index) in favoriteList" :key="index">
				<view class="doctor-info">
					<image class="doctor-avatar" :src="doctor.avatar" mode="aspectFill"></image>
					<view class="doctor-details">
						<view class="doctor-name">{{doctor.name}}</view>
						<view class="doctor-title">{{doctor.title}}</view>
						<view class="doctor-dept">{{doctor.department}}</view>
						<view class="doctor-hospital">{{doctor.hospital}}</view>
					</view>
				</view>
				<view class="doctor-actions">
					<button class="action-btn consult-btn" @click="consultDoctor(doctor)">
						<image src="/static/icon/chat.png" mode="aspectFit" class="btn-icon"></image>
						<text>咨询</text>
					</button>
					<button class="action-btn remove-btn" @click="removeFavorite(doctor)">
						<image src="/static/icon/delete.png" mode="aspectFit" class="btn-icon"></image>
						<text>取消收藏</text>
					</button>
				</view>
			</view>
		</view>
		
		<!-- 空状态 -->
		<view class="empty-state" v-else>
			<image class="empty-image" src="/static/images/empty-favorite.png" mode="aspectFit"></image>
			<text class="empty-text">暂无收藏医生</text>
			<button class="find-doctor-btn" @click="findDoctors">去发现医生</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				favoriteList: [
					{
						id: 1,
						name: '王医生',
						title: '主任医师',
						department: '心内科',
						hospital: '沈阳市云医院-和平分院',
						avatar: '/static/images/avatar1.jpg'
					},
					{
						id: 2,
						name: '李医生',
						title: '副主任医师',
						department: '肿瘤科',
						hospital: '沈阳市云医院-沈河分院',
						avatar: '/static/images/avatar2.jpg'
					},
					{
						id: 3,
						name: '赵医生',
						title: '主治医师',
						department: '内分泌科',
						hospital: '沈阳市云医院-和平分院',
						avatar: '/static/images/avatar3.jpg'
					}
				]
			}
		},
		onLoad() {
			// 获取收藏列表
			this.getFavoriteList();
		},
		methods: {
			// 获取收藏列表
			getFavoriteList() {
				// 这里可以替换为实际的API调用
				console.log('获取收藏列表');
			},
			// 咨询医生
			consultDoctor(doctor) {
				uni.navigateTo({
					url: `/pages/chat/chat?doctorId=${doctor.id}&doctorName=${doctor.name}`
				});
			},
			// 取消收藏
			removeFavorite(doctor) {
				uni.showModal({
					title: '确认取消收藏',
					content: `确定要取消收藏${doctor.name}吗？`,
					success: (res) => {
						if (res.confirm) {
							// 从列表中移除
							const index = this.favoriteList.findIndex(item => item.id === doctor.id);
							if (index > -1) {
								this.favoriteList.splice(index, 1);
								uni.showToast({
									title: '已取消收藏',
									icon: 'success'
								});
							}
						}
					}
				});
			},
			// 去发现医生
			findDoctors() {
				uni.navigateTo({
					url: '/pages/consult/consult'
				});
			}
		}
	}
</script>

<style lang="scss">
.btn-icon {
	width: 40rpx;
	height: 40rpx;
	margin-right: 10rpx;
	vertical-align: middle;
}

.content {
	background-color: #f5f5f5;
	min-height: 100vh;
	padding: 20rpx;
}

.favorite-list {
	.doctor-item {
		background-color: #ffffff;
		border-radius: 10rpx;
		margin-bottom: 20rpx;
		padding: 30rpx;
		box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
		
		.doctor-info {
			display: flex;
			align-items: center;
			margin-bottom: 20rpx;
			
			.doctor-avatar {
				width: 120rpx;
				height: 120rpx;
				border-radius: 60rpx;
				margin-right: 20rpx;
			}
			
			.doctor-details {
				flex: 1;
				
				.doctor-name {
					font-size: 32rpx;
					font-weight: bold;
					color: #333333;
					margin-bottom: 8rpx;
				}
				
				.doctor-title {
					font-size: 24rpx;
					color: #1296db;
					margin-bottom: 8rpx;
				}
				
				.doctor-dept {
					font-size: 24rpx;
					color: #666666;
					margin-bottom: 8rpx;
				}
				
				.doctor-hospital {
					font-size: 22rpx;
					color: #999999;
				}
			}
		}
		
		.doctor-actions {
			display: flex;
			justify-content: space-between;
			
			.action-btn {
				flex: 1;
				height: 80rpx;
				line-height: 80rpx;
				border-radius: 40rpx;
				font-size: 28rpx;
				margin: 0 10rpx;
				display: flex;
				align-items: center;
				justify-content: center;
				
				&::after {
					border: none;
				}
				
				&.consult-btn {
					background-color: #1296db;
					color: #ffffff;
				}
				
				&.remove-btn {
					background-color: #f5f5f5;
					color: #666666;
				}
			}
		}
	}
}

.empty-state {
	display: flex;
	flex-direction: column;
	align-items: center;
	justify-content: center;
	padding-top: 200rpx;
	
	.empty-image {
		width: 200rpx;
		height: 200rpx;
		margin-bottom: 40rpx;
	}
	
	.empty-text {
		font-size: 28rpx;
		color: #999999;
		margin-bottom: 60rpx;
	}
	
	.find-doctor-btn {
		background-color: #1296db;
		color: #ffffff;
		font-size: 28rpx;
		padding: 0 60rpx;
		height: 80rpx;
		line-height: 80rpx;
		border-radius: 40rpx;
		
		&::after {
			border: none;
		}
	}
}
</style> 