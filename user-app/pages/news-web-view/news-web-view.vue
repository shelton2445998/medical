<template>
	<view class="content">
		<!-- 页面标题 -->
		<view class="page-header">
			<view class="back-btn" @click="goBack">
				<text class="iconfont icon-back"></text>
			</view>
			<text class="page-title">资讯详情</text>
		</view>

		<!-- 进度条覆盖层 -->
		<view class="progress-mask" v-if="showProgress">
			<view class="progress-container">
				<view class="progress-bar">
					<view class="progress" :style="{ width: progress + '%' }"></view>
				</view>
				<text class="progress-text">{{ progress }}%</text>
			</view>
		</view>

		<!-- web-view 始终存在 -->
		<web-view :src="webUrl" @load="onLoad" @error="onError" :style="{
		        height: webviewHeight + 'px',
		        position: 'absolute',
		        top: headerHeight + 'px',
		        zIndex: showProgress ? -1 : 10
		      }"></web-view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				webUrl: '',
				showProgress: true,
				progress: 0,
				progressTimer: null,
				webviewHeight: 0, // 新增
				headerHeight: 60 // 标题栏高度(rpx)
			}
		},
		onLoad(options) {
			// 获取屏幕高度
			const sysInfo = uni.getSystemInfoSync()
			this.webviewHeight = sysInfo.windowHeight - uni.upx2px(this.headerHeight)

			if (options.url) {
				this.webUrl = decodeURIComponent(options.url);
				this.startProgress();
			}
		},
		methods: {
			startProgress() {
				this.progressTimer = setInterval(() => {
					this.progress += Math.random() * 10;
					if (this.progress >= 90) {
						this.progress = 90; // 保持在90%，等待页面加载完成
					}
				}, 200);
			},
			onLoad() {
				// 页面加载完成
				this.progress = 100;
				setTimeout(() => {
					this.showProgress = false;
					this.clearProgressTimer();
				}, 500);
			},
			onError() {
				// 页面加载失败
				this.clearProgressTimer();
				this.showProgress = false;
				uni.showToast({
					title: '页面加载失败',
					icon: 'none'
				});
			},
			goBack() {
				uni.navigateBack();
			},
			clearProgressTimer() {
				if (this.progressTimer) {
					clearInterval(this.progressTimer);
					this.progressTimer = null;
				}
			}
		},
		onUnload() {
			this.clearProgressTimer();
		}
	}
</script>

<style lang="scss" scoped>
	// .content {
	// 	background-color: #f5f5f5;
	// 	min-height: 100vh;
	// }
	.custom-header {
		position: relative;
		background-color: #1296db;
		padding: 20rpx 0;
		text-align: center;
		height: 60rpx;
		/* 固定高度 */
		z-index: 100;
		/* 确保在web-view上方 */

		.back-btn {
			position: absolute;
			left: 30rpx;
			top: 50%;
			transform: translateY(-50%);
			width: 60rpx;
			height: 60rpx;
			z-index: 101;
			/* 高于标题栏 */

			.iconfont {
				color: #ffffff;
				font-size: 40rpx;
			}
		}

		.page-title {
			color: #ffffff;
			font-size: 36rpx;
			font-weight: bold;
			line-height: 60rpx;
			/* 垂直居中 */
		}
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

	.progress-mask {
		position: fixed;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		background-color: #ffffff;
		z-index: 9999;
		display: flex;
		justify-content: center;
		align-items: center;
	}

	.progress-container {
		width: 80%;
		max-width: 600rpx;
		background-color: #ffffff;
		border-radius: 20rpx;
		padding: 40rpx;
		box-shadow: 0 4rpx 20rpx rgba(0, 0, 0, 0.1);

		.progress-bar {
			width: 100%;
			height: 20rpx;
			background-color: #e0e0e0;
			border-radius: 10rpx;
			overflow: hidden;
			margin-bottom: 20rpx;

			.progress {
				height: 100%;
				background-color: #1296db;
				border-radius: 10rpx;
				transition: width 0.3s ease;
			}
		}

		.progress-text {
			display: block;
			text-align: center;
			font-size: 28rpx;
			color: #666666;
		}
	}
</style>
