<template>
	<view class="content">
		<!-- 页面标题 -->
		<view class="page-header">
			<view class="back-btn" @click="goBack">
				<text class="iconfont icon-back"></text>
			</view>
			<text class="page-title">选择体检套餐</text>
		</view>
		
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
				</view>
				<view class="package-price">
					<text class="price-label">套餐价格：</text>
					<text class="price-value">¥{{item.discountPrice}}</text>
					<text class="price-original">¥{{item.price}}</text>
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
				packages: [
					{
						id: 1,
						name: '基础体检套餐',
						type: 1,
						price: 300,
						discountPrice: 250,
						description: '适合一般健康体检需求，包含基本体格检查、血常规、尿常规等基本项目',
						items: ['血常规', '尿常规', '肝功能', '血压测量', '血糖测量'],
						sold: 1234
					},
					{
						id: 2,
						name: '高级体检套餐',
						type: 2,
						price: 800,
						discountPrice: 680,
						description: '全面的健康体检方案，包含基础套餐全部项目，另加心电图、彩超、血脂检测等',
						items: ['血常规', '尿常规', '肝功能', '心电图', 'B超', '血压测量', '血糖测量'],
						sold: 986
					},
					{
						id: 3,
						name: '男性健康套餐',
						type: 3,
						price: 1200,
						discountPrice: 980,
						description: '针对男性健康的专项检查，包含前列腺检查等',
						items: ['血常规', '尿常规', '肝功能', '心电图', 'B超', '前列腺检查', '血压测量', '血糖测量'],
						sold: 756
					},
					{
						id: 4,
						name: '女性健康套餐',
						type: 3,
						price: 1300,
						discountPrice: 1080,
						description: '针对女性健康的专项检查，包含乳腺、妇科检查等',
						items: ['血常规', '尿常规', '肝功能', '心电图', 'B超', '乳腺检查', '妇科检查', '血压测量', '血糖测量'],
						sold: 892
					},
					{
						id: 5,
						name: '老年体检套餐',
						type: 3,
						price: 1500,
						discountPrice: 1280,
						description: '针对老年人的专项体检，重点检查心脑血管等',
						items: ['血常规', '尿常规', '肝功能', '心电图', 'B超', 'X光', '血压测量', '血糖测量', '视力检查', '听力检查'],
						sold: 654
					}
				],
				filteredPackages: [],
				selectedHospital: null
			}
		},
		onLoad() {
			// 初始化显示所有套餐
			this.filteredPackages = this.packages;
			
			// 获取选择的医院信息
			let hospitalInfo = uni.getStorageSync('selectedHospital');
			if (hospitalInfo) {
				this.selectedHospital = JSON.parse(hospitalInfo);
			}
			
			// 获取套餐列表数据
			this.getPackageList();
		},
		methods: {
			// 获取套餐列表
			getPackageList() {
				// 这里可以替换为实际的API调用
				console.log('获取套餐列表');
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
				
				// 跳转到预约确认页面
				uni.navigateTo({
					url: '/pages/appointment-confirm/appointment-confirm'
				});
			},
			// 查看套餐详情
			showPackageDetail(pkg) {
				uni.navigateTo({
					url: `/pages/package-detail/package-detail?id=${pkg.id}`
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
			margin-bottom: 20rpx;
			
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