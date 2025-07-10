<template>
	<view class="content">
		<!-- 套餐分类 -->
		<view class="package-tabs">
			<view 
				class="tab-item" 
				v-for="(item, index) in packageTypes" 
				:key="index"
				:class="{active: currentType === item.id}"
				@click="switchType(item.id)"
			>
				<text>{{item.name}}</text>
			</view>
		</view>
		
		<!-- 套餐列表 -->
		<view class="package-list">
			<view class="package-item" v-for="(item, index) in filteredPackages" :key="index" @click="selectPackage(item)">
				<view class="package-header">
					<text class="package-name">{{item.name}}</text>
					<text class="package-tag">{{getTypeName(item.type)}}</text>
					<text class="package-recommend" v-if="item.recommend">推荐</text>
				</view>
				<view class="package-price">
					<view class="price-left">
						<text class="price-label">套餐价格：</text>
						<text class="price-value">¥{{item.discountPrice}}</text>
						<text class="price-original">¥{{item.price}}</text>
					</view>
					<text class="price-discount">{{getDiscountText(item)}}</text>
				</view>
				<view class="package-desc">
					<text>{{item.description}}</text>
				</view>
				<view class="package-items">
					<text class="items-title">检查项目：</text>
					<view class="items-list">
						<text class="item-tag" v-for="(tag, tagIndex) in item.items" :key="tagIndex">{{tag}}</text>
					</view>
				</view>
				<view class="package-footer">
					<text class="package-sold">已售 {{item.sold}}</text>
					<button class="detail-btn" @click.stop="showPackageDetail(item)">查看详情</button>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	import { get, packageApi } from '@/utils/request.js';
	export default {
		data() {
			return {
				currentType: 0, // 0表示全部
				packageTypes: [
					{ id: 0, name: '全部' },
					{ id: 1, name: '基础' },
					{ id: 2, name: '高级' },
					{ id: 3, name: '专项' }
				],
				packages: [],
				filteredPackages: [],
				selectedHospital: null
			}
		},
		onLoad() {
			this.getPackageList();
			
			// 获取选择的医院信息
			let hospitalInfo = uni.getStorageSync('selectedHospital');
			if (hospitalInfo) {
				this.selectedHospital = JSON.parse(hospitalInfo);
			}
		},
		methods: {
			async getPackageList() {
				try {
					const result = await get(packageApi.getPackageList, { pageIndex: 1, pageSize: 20 });
					if (result && result.data && result.data.list) {
						this.packages = result.data.list.map((item, index) => ({
							id: item.id,
							name: item.name,
							type: item.type || 1,
							price: item.price || 0,
							discountPrice: item.discountPrice || item.price || 0,
							description: item.description || '',
							items: item.items || [],
							sold: item.sold || 0,
							recommend: item.recommend || false
						}));
					} else {
						this.packages = [];
					}
					this.filteredPackages = this.packages;
				} catch (e) {
					uni.showToast({ title: '获取套餐失败', icon: 'none' });
				}
			},
			// 切换套餐类型
			switchType(typeId) {
				this.currentType = typeId;
				
				if (typeId === 0) {
					// 全部套餐
					this.filteredPackages = this.packages;
				} else {
					// 按类型筛选
					this.filteredPackages = this.packages.filter(item => item.type === typeId);
				}
			},
			// 获取类型名称
			getTypeName(typeId) {
				const type = this.packageTypes.find(item => item.id === typeId);
				return type ? type.name : '';
			},
			// 选择套餐
			selectPackage(pkg) {
				// 存储选择的套餐信息
				uni.setStorageSync('selectedPackage', JSON.stringify(pkg));
				
				// 跳转到原来的预约流程页面
				uni.navigateTo({
					url: '/pages/appointment/appointment-flow'
				});
			},
			// 查看套餐详情
			showPackageDetail(pkg) {
				uni.navigateTo({
					url: `/pages/package-detail/package-detail?id=${pkg.id}`,
					success: () => {
						console.log('成功跳转到套餐详情页');
					},
					fail: (err) => {
						console.error('跳转失败:', err);
						uni.showToast({
							title: '跳转失败，请稍后重试',
							icon: 'none'
						});
					}
				});
			},
			// 返回上一页
			goBack() {
				uni.navigateBack();
			},
			// 获取折扣文本
			getDiscountText(item) {
				const discount = (item.discountPrice / item.price * 10).toFixed(1);
				return discount + '折';
			}
		}
	}
</script>

<style lang="scss">
.content {
	background-color: #f5f5f5;
	min-height: 100vh;
}



.package-tabs {
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

.package-list {
	padding: 0 20rpx;
	
	.package-item {
		background-color: #ffffff;
		border-radius: 10rpx;
		margin-bottom: 20rpx;
		padding: 30rpx;
		box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
		transition: all 0.3s;
		&:active {
			transform: scale(0.98);
			opacity: 0.9;
		}
		.package-header {
			display: flex;
			align-items: center;
			margin-bottom: 20rpx;
			
			.package-name {
				font-size: 32rpx;
				font-weight: bold;
				color: #333333;
				margin-right: 20rpx;
			}
			
			.package-tag {
				font-size: 22rpx;
				color: #1296db;
				background-color: rgba(18, 150, 219, 0.1);
				padding: 4rpx 10rpx;
				border-radius: 6rpx;
			}
		}
		
		.package-price {
			display: flex;
			align-items: center;
			justify-content: space-between;
			margin-bottom: 20rpx;
			
			.price-left {
				display: flex;
				align-items: center;
			}
			
			.price-label {
				font-size: 26rpx;
				color: #666666;
			}
			
			.price-value {
				font-size: 36rpx;
				color: #ff5a5f;
				font-weight: bold;
				margin-right: 10rpx;
			}
			
			.price-original {
				font-size: 24rpx;
				color: #999999;
				text-decoration: line-through;
			}
			
			.price-discount {
				font-size: 22rpx;
				color: #ffffff;
				background-color: #ff5a5f;
				padding: 4rpx 12rpx;
				border-radius: 20rpx;
			}
		}
		
		.package-desc {
			font-size: 26rpx;
			color: #666666;
			margin-bottom: 20rpx;
			line-height: 1.5;
		}
		
		.package-items {
			margin-bottom: 20rpx;
			
			.items-title {
				font-size: 26rpx;
				color: #666666;
				margin-bottom: 10rpx;
				display: block;
			}
			
			.items-list {
				display: flex;
				flex-wrap: wrap;
				
				.item-tag {
					font-size: 22rpx;
					color: #666666;
					background-color: #f5f5f5;
					padding: 6rpx 16rpx;
					border-radius: 30rpx;
					margin-right: 10rpx;
					margin-bottom: 10rpx;
				}
			}
		}
		
		.package-footer {
			display: flex;
			justify-content: space-between;
			align-items: center;
			
			.package-sold {
				font-size: 24rpx;
				color: #999999;
			}
			
			.detail-btn {
				font-size: 24rpx;
				color: #ffffff;
				background-color: #1296db;
				border: none;
				border-radius: 30rpx;
				padding: 0 30rpx;
				height: 60rpx;
				line-height: 60rpx;
				
				&::after {
					border: none;
				}
			}
		}
	}
}
</style>