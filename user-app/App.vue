<script>
	import {
		mapState,
		mapMutations
	} from 'vuex'

	export default {
		onLaunch: function() {
			let uniIdToken = uni.getStorageSync('uniIdToken')
			if (uniIdToken) {
				this.login(uni.getStorageSync('username'))
			}
			console.log('App Launch');
		},
		onShow: function() {
			console.log('App Show');
		},
		onHide: function() {
			console.log('App Hide');
		},
		methods: {
			...mapMutations(['login']),
		}
	}
</script>

<style lang="scss">
	/* 引入字体图标 */
	@import './static/css/iconfont.css';
	
	/* 全局样式 */
	page {
		background-color: #f5f5f5;
		font-family: -apple-system, BlinkMacSystemFont, 'Helvetica Neue', Helvetica, Segoe UI, Arial, Roboto, 'PingFang SC', 'miui', 'Hiragino Sans GB', 'Microsoft Yahei', sans-serif;
		color: #333333;
		font-size: 28rpx;
		line-height: 1.5;
		box-sizing: border-box;
		-webkit-font-smoothing: antialiased;
	}
	
	/* 主题色 */
	:root {
		--primary-color: #1296db;
		--primary-color-light: rgba(18, 150, 219, 0.1);
		--success-color: #42b983;
		--warning-color: #ff9500;
		--error-color: #ff5a5f;
		--text-color: #333333;
		--text-color-secondary: #666666;
		--text-color-tertiary: #999999;
		--border-color: #eeeeee;
		--background-color: #f5f5f5;
		--background-color-light: #f9f9f9;
		--background-color-dark: #f0f0f0;
		--box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
	}
	
	/* 通用样式 */
	.container {
		padding: 20rpx;
	}
	
	.flex-row {
		display: flex;
		flex-direction: row;
	}
	
	.flex-column {
		display: flex;
		flex-direction: column;
	}
	
	.flex-center {
		display: flex;
		justify-content: center;
		align-items: center;
	}
	
	.flex-between {
		display: flex;
		justify-content: space-between;
		align-items: center;
	}
	
	.flex-around {
		display: flex;
		justify-content: space-around;
		align-items: center;
	}
	
	.flex-wrap {
		flex-wrap: wrap;
	}
	
	.flex-1 {
		flex: 1;
	}
	
	/* 文本样式 */
	.text-primary {
		color: var(--primary-color);
	}
	
	.text-success {
		color: var(--success-color);
	}
	
	.text-warning {
		color: var(--warning-color);
	}
	
	.text-error {
		color: var(--error-color);
	}
	
	.text-center {
		text-align: center;
	}
	
	.text-left {
		text-align: left;
	}
	
	.text-right {
		text-align: right;
	}
	
	.text-bold {
		font-weight: bold;
	}
	
	.text-ellipsis {
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
	}
	
	.text-ellipsis-2 {
		display: -webkit-box;
		-webkit-box-orient: vertical;
		-webkit-line-clamp: 2;
		overflow: hidden;
		text-overflow: ellipsis;
	}
	
	/* 边距样式 */
	.m-0 { margin: 0; }
	.m-10 { margin: 10rpx; }
	.m-20 { margin: 20rpx; }
	.m-30 { margin: 30rpx; }
	
	.mt-0 { margin-top: 0; }
	.mt-10 { margin-top: 10rpx; }
	.mt-20 { margin-top: 20rpx; }
	.mt-30 { margin-top: 30rpx; }
	
	.mr-0 { margin-right: 0; }
	.mr-10 { margin-right: 10rpx; }
	.mr-20 { margin-right: 20rpx; }
	.mr-30 { margin-right: 30rpx; }
	
	.mb-0 { margin-bottom: 0; }
	.mb-10 { margin-bottom: 10rpx; }
	.mb-20 { margin-bottom: 20rpx; }
	.mb-30 { margin-bottom: 30rpx; }
	
	.ml-0 { margin-left: 0; }
	.ml-10 { margin-left: 10rpx; }
	.ml-20 { margin-left: 20rpx; }
	.ml-30 { margin-left: 30rpx; }
	
	.p-0 { padding: 0; }
	.p-10 { padding: 10rpx; }
	.p-20 { padding: 20rpx; }
	.p-30 { padding: 30rpx; }
	
	.pt-0 { padding-top: 0; }
	.pt-10 { padding-top: 10rpx; }
	.pt-20 { padding-top: 20rpx; }
	.pt-30 { padding-top: 30rpx; }
	
	.pr-0 { padding-right: 0; }
	.pr-10 { padding-right: 10rpx; }
	.pr-20 { padding-right: 20rpx; }
	.pr-30 { padding-right: 30rpx; }
	
	.pb-0 { padding-bottom: 0; }
	.pb-10 { padding-bottom: 10rpx; }
	.pb-20 { padding-bottom: 20rpx; }
	.pb-30 { padding-bottom: 30rpx; }
	
	.pl-0 { padding-left: 0; }
	.pl-10 { padding-left: 10rpx; }
	.pl-20 { padding-left: 20rpx; }
	.pl-30 { padding-left: 30rpx; }
	
	/* 按钮样式 */
	.btn {
		height: 80rpx;
		line-height: 80rpx;
		font-size: 28rpx;
		padding: 0 30rpx;
		border-radius: 40rpx;
		text-align: center;
		box-sizing: border-box;
		
		&::after {
			border: none;
		}
	}
	
	.btn-primary {
		background-color: var(--primary-color);
		color: #ffffff;
	}
	
	.btn-outline {
		background-color: transparent;
		color: var(--primary-color);
		border: 1rpx solid var(--primary-color);
	}
	
	.btn-success {
		background-color: var(--success-color);
		color: #ffffff;
	}
	
	.btn-warning {
		background-color: var(--warning-color);
		color: #ffffff;
	}
	
	.btn-error {
		background-color: var(--error-color);
		color: #ffffff;
	}
	
	.btn-gray {
		background-color: #f5f5f5;
		color: var(--text-color-secondary);
	}
	
	.btn-block {
		display: block;
		width: 100%;
	}
	
	.btn-lg {
		height: 90rpx;
		line-height: 90rpx;
		font-size: 32rpx;
		border-radius: 45rpx;
	}
	
	.btn-sm {
		height: 60rpx;
		line-height: 60rpx;
		font-size: 24rpx;
		border-radius: 30rpx;
	}
	
	/* 卡片样式 */
	.card {
		background-color: #ffffff;
		border-radius: 10rpx;
		padding: 30rpx;
		margin-bottom: 20rpx;
		box-shadow: var(--box-shadow);
	}
	
	.card-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 20rpx;
		
		.card-title {
			font-size: 32rpx;
			font-weight: bold;
			color: var(--text-color);
		}
		
		.card-extra {
			font-size: 26rpx;
			color: var(--text-color-tertiary);
		}
	}
	
	/* 列表样式 */
	.list {
		background-color: #ffffff;
		
		.list-item {
			display: flex;
			justify-content: space-between;
			align-items: center;
			padding: 30rpx;
			border-bottom: 1rpx solid var(--border-color);
			
			&:last-child {
				border-bottom: none;
			}
			
			.list-content {
				flex: 1;
				
				.list-title {
					font-size: 28rpx;
					color: var(--text-color);
					margin-bottom: 10rpx;
				}
				
				.list-desc {
					font-size: 24rpx;
					color: var(--text-color-tertiary);
				}
			}
			
			.list-extra {
				font-size: 26rpx;
				color: var(--text-color-tertiary);
			}
		}
	}
	
	/* 表单样式 */
	.form-item {
		display: flex;
		padding: 30rpx;
		border-bottom: 1rpx solid var(--border-color);
		
		&:last-child {
			border-bottom: none;
		}
		
		.form-label {
			width: 180rpx;
			font-size: 28rpx;
			color: var(--text-color);
		}
		
		.form-content {
			flex: 1;
			
			input, textarea {
				width: 100%;
				font-size: 28rpx;
				color: var(--text-color);
			}
		}
	}
	
	/* 标签样式 */
	.tag {
		display: inline-block;
		padding: 4rpx 16rpx;
		font-size: 24rpx;
		border-radius: 6rpx;
		margin-right: 10rpx;
		margin-bottom: 10rpx;
		
		&.tag-primary {
			color: var(--primary-color);
			background-color: var(--primary-color-light);
		}
		
		&.tag-success {
			color: var(--success-color);
			background-color: rgba(66, 185, 131, 0.1);
		}
		
		&.tag-warning {
			color: var(--warning-color);
			background-color: rgba(255, 149, 0, 0.1);
		}
		
		&.tag-error {
			color: var(--error-color);
			background-color: rgba(255, 90, 95, 0.1);
		}
	}
	
	/* 徽标样式 */
	.badge {
		position: relative;
		
		.badge-dot {
			position: absolute;
			top: -6rpx;
			right: -6rpx;
			width: 16rpx;
			height: 16rpx;
			border-radius: 50%;
			background-color: var(--error-color);
		}
		
		.badge-count {
			position: absolute;
			top: -16rpx;
			right: -16rpx;
			min-width: 32rpx;
			height: 32rpx;
			line-height: 32rpx;
			border-radius: 16rpx;
			background-color: var(--error-color);
			color: #ffffff;
			font-size: 20rpx;
			text-align: center;
			padding: 0 6rpx;
			box-sizing: border-box;
		}
	}
	
	/* 分割线样式 */
	.divider {
		height: 1rpx;
		background-color: var(--border-color);
		margin: 20rpx 0;
		
		&.divider-dashed {
			background: none;
			border-top: 1rpx dashed var(--border-color);
		}
		
		&.divider-text {
			display: flex;
			align-items: center;
			color: var(--text-color-tertiary);
			font-size: 24rpx;
			
			&::before, &::after {
				content: '';
				flex: 1;
				height: 1rpx;
				background-color: var(--border-color);
			}
			
			&::before {
				margin-right: 20rpx;
			}
			
			&::after {
				margin-left: 20rpx;
			}
		}
	}
	
	/* 空状态样式 */
	.empty {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 100rpx 0;
		
		.empty-image {
			width: 200rpx;
			height: 200rpx;
			margin-bottom: 30rpx;
		}
		
		.empty-text {
			font-size: 28rpx;
			color: var(--text-color-tertiary);
			margin-bottom: 40rpx;
		}
	}
</style>
