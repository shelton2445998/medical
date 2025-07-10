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
					<text class="package-recommend" v-if="item.recommend">推荐</text>
				</view>
				<view class="package-price">
					<text class="price-label">套餐价格：</text>
					<text class="price-value">¥{{item.discountPrice}}</text>
					<text class="price-original">¥{{item.price}}</text>
					<text class="price-discount">{{Math.round(item.discountPrice/item.price*10)}}折</text>
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
					// 标准体检套餐
					{
						id: 1,
						name: '标准体检套餐',
						type: 1,
						price: 399,
						discountPrice: 299,
						description: '适合25-45岁人群，包含血常规、尿常规、肝功能、肾功能、血脂、血糖等基础检查项目，全面了解健康状况。',
						items: ['血常规', '尿常规', '肝功能', '肾功能', '血脂', '血糖', '身高体重', '血压', '视力', '胸部X光', 'B超'],
						sold: 1234,
						recommend: true
					},
					// 男性专项体检套餐
					{
						id: 2,
						name: '男性专项体检套餐',
						type: 3,
						price: 1200,
						discountPrice: 980,
						description: '针对男性健康，增加前列腺、肿瘤筛查等专项检查，适合30岁以上男性。',
						items: ['血常规', '尿常规', '肝功能', '肾功能', '血脂', '血糖', '前列腺B超', '肿瘤标志物', '心电图', '胸部X光'],
						sold: 756,
						recommend: false
					},
					// 女性专项体检套餐
					{
						id: 3,
						name: '女性专项体检套餐',
						type: 3,
						price: 1300,
						discountPrice: 1080,
						description: '针对女性健康，增加乳腺、妇科、HPV等专项检查，适合30岁以上女性。',
						items: ['血常规', '尿常规', '肝功能', '肾功能', '血脂', '血糖', '乳腺彩超', '妇科检查', 'HPV检测', '心电图', 'B超'],
						sold: 892,
						recommend: false
					},
					// 老年体检套餐
					{
						id: 4,
						name: '老年体检套餐',
						type: 3,
						price: 1500,
						discountPrice: 1280,
						description: '适合60岁以上老年人，重点检查心脑血管、骨密度、听力等项目。',
						items: ['血常规', '尿常规', '肝功能', '肾功能', '血脂', '血糖', '心电图', '脑血流图', '骨密度', '听力检查', '视力检查', 'B超'],
						sold: 654,
						recommend: false
					},
					// 高端全身体检套餐
					{
						id: 5,
						name: '高端全身体检套餐',
						type: 2,
						price: 3000,
						discountPrice: 2580,
						description: '适合高净值人群，包含全身多系统深度筛查，含肿瘤、心脑血管、基因检测等。',
						items: ['血常规', '尿常规', '肝功能', '肾功能', '血脂', '血糖', '肿瘤标志物', '心脏彩超', '脑MRI', '基因检测', '全身CT'],
						sold: 321,
						recommend: true
					},
					// 入职体检套餐
					{
						id: 6,
						name: '入职体检套餐',
						type: 1,
						price: 200,
						discountPrice: 180,
						description: '适合新员工入职，包含基础健康检查项目，满足大部分企业入职要求。',
						items: ['血常规', '尿常规', '肝功能', '胸部X光', '心电图', '身高体重', '血压'],
						sold: 888,
						recommend: false
					},
					// 儿童体检套餐
					{
						id: 7,
						name: '儿童体检套餐',
						type: 3,
						price: 600,
						discountPrice: 480,
						description: '适合3-14岁儿童，包含生长发育、视力、听力、常规血尿等检查，关注儿童健康成长。',
						items: ['血常规', '尿常规', '肝功能', '肾功能', '血糖', '微量元素', '生长发育评估', '视力检查', '听力筛查', '心电图', '胸部X光'],
						sold: 520,
						recommend: false
					},
					// 高级体检套餐
					{
						id: 8,
						name: '高级体检套餐',
						type: 2,
						price: 899,
						discountPrice: 699,
						description: '适合45岁以上人群，包含心脑血管、肿瘤筛查等全面检查，帮助早期发现慢性病和肿瘤风险。',
						items: ['血常规', '尿常规', '肝功能', '肾功能', '血脂', '血糖', '心电图', '心脏彩超', '脑血流图', '肿瘤标志物', '胸部X光', '腹部B超'],
						sold: 432,
						recommend: true
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