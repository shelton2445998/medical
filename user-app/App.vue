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
		--primary-color: #0984e3;
		--primary-color-light: rgba(9, 132, 227, 0.1);
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
	
	/* 导航栏样式优化 */
	.uni-page-head {
		background: linear-gradient(135deg, #0984e3 0%, #74b9ff 100%) !important;
		box-shadow: 0 2rpx 8rpx rgba(9, 132, 227, 0.2) !important;
	}
	
	.uni-page-head__title {
		color: #ffffff !important;
		font-weight: bold !important;
		font-size: 36rpx !important;
		text-shadow: 0 1rpx 2rpx rgba(0, 0, 0, 0.1) !important;
	}
	
	.uni-page-head__btn {
		color: #ffffff !important;
		font-size: 32rpx !important;
	}
	
	.uni-page-head__btn:active {
		opacity: 0.8 !important;
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
	
	.text-secondary {
		color: var(--text-color-secondary);
	}
	
	.text-tertiary {
		color: var(--text-color-tertiary);
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
		display: inline-flex;
		align-items: center;
		justify-content: center;
		padding: 20rpx 40rpx;
		border-radius: 50rpx;
		font-size: 28rpx;
		font-weight: 500;
		text-decoration: none;
		border: none;
		cursor: pointer;
		transition: all 0.3s ease;
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1);
	}
	
	.btn-primary {
		background: linear-gradient(135deg, var(--primary-color), #74b9ff);
		color: #ffffff;
	}
	
	.btn-primary:hover {
		transform: translateY(-2rpx);
		box-shadow: 0 8rpx 20rpx rgba(9, 132, 227, 0.3);
	}
	
	.btn-secondary {
		background: #ffffff;
		color: var(--primary-color);
		border: 2rpx solid var(--primary-color);
	}
	
	.btn-secondary:hover {
		background: var(--primary-color-light);
		transform: translateY(-2rpx);
	}
	
	.btn-success {
		background: linear-gradient(135deg, var(--success-color), #66bb6a);
		color: #ffffff;
	}
	
	.btn-warning {
		background: linear-gradient(135deg, var(--warning-color), #ffa726);
		color: #ffffff;
	}
	
	.btn-error {
		background: linear-gradient(135deg, var(--error-color), #ff7675);
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
		background: #ffffff;
		border-radius: 16rpx;
		padding: 30rpx;
		margin-bottom: 20rpx;
		box-shadow: var(--box-shadow);
		transition: all 0.3s ease;
	}
	
	.card:hover {
		transform: translateY(-4rpx);
		box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.1);
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
	
	/* 输入框样式 */
	.input {
		width: 100%;
		padding: 20rpx 30rpx;
		border: 2rpx solid var(--border-color);
		border-radius: 12rpx;
		font-size: 28rpx;
		background: #ffffff;
		transition: all 0.3s ease;
	}
	
	.input:focus {
		border-color: var(--primary-color);
		box-shadow: 0 0 0 4rpx rgba(9, 132, 227, 0.1);
	}
	
	/* 标签样式 */
	.tag {
		display: inline-flex;
		align-items: center;
		padding: 8rpx 16rpx;
		border-radius: 20rpx;
		font-size: 24rpx;
		font-weight: 500;
		background: var(--primary-color-light);
		color: var(--primary-color);
	}
	
	.tag-success {
		background: rgba(66, 185, 131, 0.1);
		color: var(--success-color);
	}
	
	.tag-warning {
		background: rgba(255, 149, 0, 0.1);
		color: var(--warning-color);
	}
	
	.tag-error {
		background: rgba(255, 90, 95, 0.1);
		color: var(--error-color);
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
	
	/* 动画 */
	@keyframes fadeIn {
		from {
			opacity: 0;
			transform: translateY(20rpx);
		}
		to {
			opacity: 1;
			transform: translateY(0);
		}
	}
	
	@keyframes slideIn {
		from {
			transform: translateX(-100%);
		}
		to {
			transform: translateX(0);
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
	
	/* 工具类 */
	.fade-in {
		animation: fadeIn 0.6s ease-out;
	}
	
	.slide-in {
		animation: slideIn 0.4s ease-out;
	}
	
	.pulse {
		animation: pulse 2s infinite;
	}
	
	.hidden {
		display: none !important;
	}
	
	.visible {
		display: block !important;
	}
	
	/* 响应式 */
	@media (max-width: 750rpx) {
		.container {
			padding: 15rpx;
		}
		
		.card {
			padding: 20rpx;
		}
		
		.btn {
			padding: 16rpx 32rpx;
			font-size: 26rpx;
		}
	}
</style>
