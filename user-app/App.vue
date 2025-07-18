<script>
	// 从vuex中导入mapState和mapMutations辅助函数
	import {
		mapState,
		mapMutations
	} from 'vuex'

	// 导出App根组件配置
	export default {
		// 应用启动时的生命周期函数
		onLaunch: function() {
			// 从本地存储中获取用户登录令牌
			let uniIdToken = uni.getStorageSync('uniIdToken')
			// 如果存在令牌，则自动登录
			if (uniIdToken) {
				// 调用login方法，传入存储的用户名进行自动登录
				this.login(uni.getStorageSync('username'))
			}
			// 输出应用启动日志
			console.log('App Launch');
		},
		// 应用显示时的生命周期函数
		onShow: function() {
			// 输出应用显示日志
			console.log('App Show');
		},
		// 应用隐藏时的生命周期函数
		onHide: function() {
			// 输出应用隐藏日志
			console.log('App Hide');
		},
		// 组件方法定义
		methods: {
			// 使用mapMutations将vuex中的login方法映射到组件中
			...mapMutations(['login']),
		}
	}
</script>

<style lang="scss">
	/* 引入字体图标 */
	@import './static/css/iconfont.css';
	
	/* 全局页面样式 */
	page {
		background-color: #f5f5f5; /* 页面背景色 */
		font-family: -apple-system, BlinkMacSystemFont, 'Helvetica Neue', Helvetica, Segoe UI, Arial, Roboto, 'PingFang SC', 'miui', 'Hiragino Sans GB', 'Microsoft Yahei', sans-serif; /* 字体族 */
		color: #333333; /* 字体颜色 */
		font-size: 28rpx; /* 字体大小 */
		line-height: 1.5; /* 行高 */
		box-sizing: border-box; /* 盒模型 */
		-webkit-font-smoothing: antialiased; /* 字体平滑 */
	}
	
	/* 主题色变量定义 */
	:root {
		--primary-color: #0984e3; /* 主色调 */
		--primary-color-light: rgba(9, 132, 227, 0.1); /* 主色调浅色 */
		--success-color: #42b983; /* 成功色 */
		--warning-color: #ff9500; /* 警告色 */
		--error-color: #ff5a5f; /* 错误色 */
		--text-color: #333333; /* 主文字颜色 */
		--text-color-secondary: #666666; /* 次要文字颜色 */
		--text-color-tertiary: #999999; /* 第三级文字颜色 */
		--border-color: #eeeeee; /* 边框颜色 */
		--background-color: #f5f5f5; /* 背景色 */
		--background-color-light: #f9f9f9; /* 浅背景色 */
		--background-color-dark: #f0f0f0; /* 深背景色 */
		--box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05); /* 阴影效果 */
	}
	
	/* 导航栏样式优化 */
	.uni-page-head {
		background: linear-gradient(135deg, #0984e3 0%, #74b9ff 100%) !important; /* 渐变背景 */
		box-shadow: 0 2rpx 8rpx rgba(9, 132, 227, 0.2) !important; /* 阴影效果 */
	}
	
	/* 导航栏标题样式 */
	.uni-page-head__title {
		color: #ffffff !important; /* 标题颜色 */
		font-weight: bold !important; /* 字体粗细 */
		font-size: 36rpx !important; /* 字体大小 */
		text-shadow: 0 1rpx 2rpx rgba(0, 0, 0, 0.1) !important; /* 文字阴影 */
	}
	
	/* 导航栏按钮样式 */
	.uni-page-head__btn {
		color: #ffffff !important; /* 按钮颜色 */
		font-size: 32rpx !important; /* 字体大小 */
	}
	
	/* 导航栏按钮激活状态 */
	.uni-page-head__btn:active {
		opacity: 0.8 !important; /* 透明度 */
	}
	
	/* 通用容器样式 */
	.container {
		padding: 20rpx; /* 内边距 */
	}
	
	/* 弹性布局工具类 */
	.flex-row {
		display: flex; /* 弹性布局 */
		flex-direction: row; /* 横向排列 */
	}
	
	.flex-column {
		display: flex; /* 弹性布局 */
		flex-direction: column; /* 纵向排列 */
	}
	
	.flex-center {
		display: flex; /* 弹性布局 */
		justify-content: center; /* 水平居中 */
		align-items: center; /* 垂直居中 */
	}
	
	.flex-between {
		display: flex; /* 弹性布局 */
		justify-content: space-between; /* 两端对齐 */
		align-items: center; /* 垂直居中 */
	}
	
	.flex-around {
		display: flex; /* 弹性布局 */
		justify-content: space-around; /* 环绕对齐 */
		align-items: center; /* 垂直居中 */
	}
	
	.flex-wrap {
		flex-wrap: wrap; /* 允许换行 */
	}
	
	.flex-1 {
		flex: 1; /* 占据剩余空间 */
	}
	
	/* 文本样式工具类 */
	.text-primary {
		color: var(--primary-color); /* 主色调文字 */
	}
	
	.text-success {
		color: var(--success-color); /* 成功色文字 */
	}
	
	.text-warning {
		color: var(--warning-color); /* 警告色文字 */
	}
	
	.text-error {
		color: var(--error-color); /* 错误色文字 */
	}
	
	.text-secondary {
		color: var(--text-color-secondary); /* 次要文字颜色 */
	}
	
	.text-tertiary {
		color: var(--text-color-tertiary); /* 第三级文字颜色 */
	}
	
	.text-center {
		text-align: center; /* 文本居中 */
	}
	
	.text-left {
		text-align: left; /* 文本左对齐 */
	}
	
	.text-right {
		text-align: right; /* 文本右对齐 */
	}
	
	.text-bold {
		font-weight: bold; /* 字体粗体 */
	}
	
	.text-ellipsis {
		white-space: nowrap; /* 不换行 */
		overflow: hidden; /* 隐藏溢出 */
		text-overflow: ellipsis; /* 省略号 */
	}
	
	.text-ellipsis-2 {
		display: -webkit-box; /* webkit盒子模式 */
		-webkit-box-orient: vertical; /* 垂直排列 */
		-webkit-line-clamp: 2; /* 限制2行 */
		overflow: hidden; /* 隐藏溢出 */
		text-overflow: ellipsis; /* 省略号 */
	}
	
	/* 边距样式工具类 */
	.m-0 { margin: 0; } /* 外边距为0 */
	.m-10 { margin: 10rpx; } /* 外边距10rpx */
	.m-20 { margin: 20rpx; } /* 外边距20rpx */
	.m-30 { margin: 30rpx; } /* 外边距30rpx */
	
	.mt-0 { margin-top: 0; } /* 上外边距为0 */
	.mt-10 { margin-top: 10rpx; } /* 上外边距10rpx */
	.mt-20 { margin-top: 20rpx; } /* 上外边距20rpx */
	.mt-30 { margin-top: 30rpx; } /* 上外边距30rpx */
	
	.mr-0 { margin-right: 0; } /* 右外边距为0 */
	.mr-10 { margin-right: 10rpx; } /* 右外边距10rpx */
	.mr-20 { margin-right: 20rpx; } /* 右外边距20rpx */
	.mr-30 { margin-right: 30rpx; } /* 右外边距30rpx */
	
	.mb-0 { margin-bottom: 0; } /* 下外边距为0 */
	.mb-10 { margin-bottom: 10rpx; } /* 下外边距10rpx */
	.mb-20 { margin-bottom: 20rpx; } /* 下外边距20rpx */
	.mb-30 { margin-bottom: 30rpx; } /* 下外边距30rpx */
	
	.ml-0 { margin-left: 0; } /* 左外边距为0 */
	.ml-10 { margin-left: 10rpx; } /* 左外边距10rpx */
	.ml-20 { margin-left: 20rpx; } /* 左外边距20rpx */
	.ml-30 { margin-left: 30rpx; } /* 左外边距30rpx */
	
	/* 内边距样式工具类 */
	.p-0 { padding: 0; } /* 内边距为0 */
	.p-10 { padding: 10rpx; } /* 内边距10rpx */
	.p-20 { padding: 20rpx; } /* 内边距20rpx */
	.p-30 { padding: 30rpx; } /* 内边距30rpx */
	
	.pt-0 { padding-top: 0; } /* 上内边距为0 */
	.pt-10 { padding-top: 10rpx; } /* 上内边距10rpx */
	.pt-20 { padding-top: 20rpx; } /* 上内边距20rpx */
	.pt-30 { padding-top: 30rpx; } /* 上内边距30rpx */
	
	.pr-0 { padding-right: 0; } /* 右内边距为0 */
	.pr-10 { padding-right: 10rpx; } /* 右内边距10rpx */
	.pr-20 { padding-right: 20rpx; } /* 右内边距20rpx */
	.pr-30 { padding-right: 30rpx; } /* 右内边距30rpx */
	
	.pb-0 { padding-bottom: 0; } /* 下内边距为0 */
	.pb-10 { padding-bottom: 10rpx; } /* 下内边距10rpx */
	.pb-20 { padding-bottom: 20rpx; } /* 下内边距20rpx */
	.pb-30 { padding-bottom: 30rpx; } /* 下内边距30rpx */
	
	.pl-0 { padding-left: 0; } /* 左内边距为0 */
	.pl-10 { padding-left: 10rpx; } /* 左内边距10rpx */
	.pl-20 { padding-left: 20rpx; } /* 左内边距20rpx */
	.pl-30 { padding-left: 30rpx; } /* 左内边距30rpx */
	
	/* 按钮样式工具类 */
	.btn {
		display: inline-flex; /* 内联弹性布局 */
		align-items: center; /* 垂直居中 */
		justify-content: center; /* 水平居中 */
		padding: 20rpx 40rpx; /* 内边距 */
		border-radius: 50rpx; /* 圆角 */
		font-size: 28rpx; /* 字体大小 */
		font-weight: 500; /* 字体粗细 */
		text-decoration: none; /* 无文本装饰 */
		border: none; /* 无边框 */
		cursor: pointer; /* 鼠标指针 */
		transition: all 0.3s ease; /* 过渡效果 */
		box-shadow: 0 4rpx 12rpx rgba(0, 0, 0, 0.1); /* 阴影效果 */
	}
	
	/* 主要按钮样式 */
	.btn-primary {
		background: linear-gradient(135deg, var(--primary-color), #74b9ff); /* 渐变背景 */
		color: #ffffff; /* 文字颜色 */
	}
	
	/* 主要按钮悬停效果 */
	.btn-primary:hover {
		transform: translateY(-2rpx); /* 向上偏移 */
		box-shadow: 0 8rpx 20rpx rgba(9, 132, 227, 0.3); /* 阴影效果 */
	}
	
	/* 次要按钮样式 */
	.btn-secondary {
		background: #ffffff; /* 白色背景 */
		color: var(--primary-color); /* 主色调文字 */
		border: 2rpx solid var(--primary-color); /* 边框 */
	}
	
	/* 次要按钮悬停效果 */
	.btn-secondary:hover {
		background: var(--primary-color-light); /* 浅色背景 */
		transform: translateY(-2rpx); /* 向上偏移 */
	}
	
	/* 成功按钮样式 */
	.btn-success {
		background: linear-gradient(135deg, var(--success-color), #66bb6a); /* 渐变背景 */
		color: #ffffff; /* 文字颜色 */
	}
	
	/* 警告按钮样式 */
	.btn-warning {
		background: linear-gradient(135deg, var(--warning-color), #ffa726); /* 渐变背景 */
		color: #ffffff; /* 文字颜色 */
	}
	
	/* 错误按钮样式 */
	.btn-error {
		background: linear-gradient(135deg, var(--error-color), #ff7675); /* 渐变背景 */
		color: #ffffff; /* 文字颜色 */
	}
	
	/* 灰色按钮样式 */
	.btn-gray {
		background-color: #f5f5f5; /* 灰色背景 */
		color: var(--text-color-secondary); /* 次要文字颜色 */
	}
	
	/* 块级按钮样式 */
	.btn-block {
		display: block; /* 块级元素 */
		width: 100%; /* 宽度100% */
	}
	
	/* 大按钮样式 */
	.btn-lg {
		height: 90rpx; /* 高度 */
		line-height: 90rpx; /* 行高 */
		font-size: 32rpx; /* 字体大小 */
		border-radius: 45rpx; /* 圆角 */
	}
	
	/* 小按钮样式 */
	.btn-sm {
		height: 60rpx; /* 高度 */
		line-height: 60rpx; /* 行高 */
		font-size: 24rpx; /* 字体大小 */
		border-radius: 30rpx; /* 圆角 */
	}
	
	/* 卡片样式工具类 */
	.card {
		background: #ffffff; /* 白色背景 */
		border-radius: 16rpx; /* 圆角 */
		padding: 30rpx; /* 内边距 */
		margin-bottom: 20rpx; /* 底部边距 */
		box-shadow: var(--box-shadow); /* 阴影效果 */
		transition: all 0.3s ease; /* 过渡效果 */
	}
	
	/* 卡片悬停效果 */
	.card:hover {
		transform: translateY(-4rpx); /* 向上偏移 */
		box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.1); /* 阴影效果 */
	}
	
	/* 卡片头部样式 */
	.card-header {
		display: flex; /* 弹性布局 */
		justify-content: space-between; /* 两端对齐 */
		align-items: center; /* 垂直居中 */
		margin-bottom: 20rpx; /* 底部边距 */
		
		/* 卡片标题样式 */
		.card-title {
			font-size: 32rpx; /* 字体大小 */
			font-weight: bold; /* 字体粗细 */
			color: var(--text-color); /* 文字颜色 */
		}
		
		/* 卡片额外内容样式 */
		.card-extra {
			font-size: 26rpx; /* 字体大小 */
			color: var(--text-color-tertiary); /* 第三级文字颜色 */
		}
	}
	
	/* 列表样式工具类 */
	.list {
		background-color: #ffffff; /* 白色背景 */
		
		/* 列表项样式 */
		.list-item {
			display: flex; /* 弹性布局 */
			justify-content: space-between; /* 两端对齐 */
			align-items: center; /* 垂直居中 */
			padding: 30rpx; /* 内边距 */
			border-bottom: 1rpx solid var(--border-color); /* 底部边框 */
			
			/* 最后一个列表项样式 */
			&:last-child {
				border-bottom: none; /* 无底部边框 */
			}
			
			/* 列表内容样式 */
			.list-content {
				flex: 1; /* 占据剩余空间 */
				
				/* 列表标题样式 */
				.list-title {
					font-size: 28rpx; /* 字体大小 */
					color: var(--text-color); /* 文字颜色 */
					margin-bottom: 10rpx; /* 底部边距 */
				}
				
				/* 列表描述样式 */
				.list-desc {
					font-size: 24rpx; /* 字体大小 */
					color: var(--text-color-tertiary); /* 第三级文字颜色 */
				}
			}
			
			/* 列表额外内容样式 */
			.list-extra {
				font-size: 26rpx; /* 字体大小 */
				color: var(--text-color-tertiary); /* 第三级文字颜色 */
			}
		}
	}
	
	/* 表单样式工具类 */
	.form-item {
		display: flex; /* 弹性布局 */
		padding: 30rpx; /* 内边距 */
		border-bottom: 1rpx solid var(--border-color); /* 底部边框 */
		
		/* 最后一个表单项样式 */
		&:last-child {
			border-bottom: none; /* 无底部边框 */
		}
		
		/* 表单标签样式 */
		.form-label {
			width: 180rpx; /* 标签宽度 */
			font-size: 28rpx; /* 字体大小 */
			color: var(--text-color); /* 文字颜色 */
		}
		
		/* 表单内容样式 */
		.form-content {
			flex: 1; /* 占据剩余空间 */
			
			input, textarea {
				width: 100%; /* 输入框宽度 */
				font-size: 28rpx; /* 字体大小 */
				color: var(--text-color); /* 文字颜色 */
			}
		}
	}
	
	/* 输入框样式工具类 */
	.input {
		width: 100%; /* 宽度100% */
		padding: 20rpx 30rpx; /* 内边距 */
		border: 2rpx solid var(--border-color); /* 边框 */
		border-radius: 12rpx; /* 圆角 */
		font-size: 28rpx; /* 字体大小 */
		background: #ffffff; /* 背景色 */
		transition: all 0.3s ease; /* 过渡效果 */
	}
	
	/* 输入框获得焦点时的样式 */
	.input:focus {
		border-color: var(--primary-color); /* 边框颜色 */
		box-shadow: 0 0 0 4rpx rgba(9, 132, 227, 0.1); /* 阴影效果 */
	}
	
	/* 标签样式工具类 */
	.tag {
		display: inline-flex; /* 内联弹性布局 */
		align-items: center; /* 垂直居中 */
		padding: 8rpx 16rpx; /* 内边距 */
		border-radius: 20rpx; /* 圆角 */
		font-size: 24rpx; /* 字体大小 */
		font-weight: 500; /* 字体粗细 */
		background: var(--primary-color-light); /* 背景色 */
		color: var(--primary-color); /* 文字颜色 */
	}
	
	/* 成功标签样式 */
	.tag-success {
		background: rgba(66, 185, 131, 0.1); /* 成功色背景 */
		color: var(--success-color); /* 成功色文字 */
	}
	
	/* 警告标签样式 */
	.tag-warning {
		background: rgba(255, 149, 0, 0.1); /* 警告色背景 */
		color: var(--warning-color); /* 警告色文字 */
	}
	
	/* 错误标签样式 */
	.tag-error {
		background: rgba(255, 90, 95, 0.1); /* 错误色背景 */
		color: var(--error-color); /* 错误色文字 */
	}
	
	/* 徽标样式工具类 */
	.badge {
		position: relative; /* 相对定位 */
		
		/* 徽标点样式 */
		.badge-dot {
			position: absolute; /* 绝对定位 */
			top: -6rpx; /* 顶部偏移 */
			right: -6rpx; /* 右侧偏移 */
			width: 16rpx; /* 宽度 */
			height: 16rpx; /* 高度 */
			border-radius: 50%; /* 圆形 */
			background-color: var(--error-color); /* 背景色 */
		}
		
		/* 徽标数字样式 */
		.badge-count {
			position: absolute; /* 绝对定位 */
			top: -16rpx; /* 顶部偏移 */
			right: -16rpx; /* 右侧偏移 */
			min-width: 32rpx; /* 最小宽度 */
			height: 32rpx; /* 高度 */
			line-height: 32rpx; /* 行高 */
			border-radius: 16rpx; /* 圆角 */
			background-color: var(--error-color); /* 背景色 */
			color: #ffffff; /* 文字颜色 */
			font-size: 20rpx; /* 字体大小 */
			text-align: center; /* 文本居中 */
			padding: 0 6rpx; /* 内边距 */
			box-sizing: border-box; /* 盒模型 */
		}
	}
	
	/* 分割线样式工具类 */
	.divider {
		height: 1rpx; /* 高度 */
		background-color: var(--border-color); /* 背景色 */
		margin: 20rpx 0; /* 上下边距 */
		
		/* 虚线分割线样式 */
		&.divider-dashed {
			background: none; /* 无背景 */
			border-top: 1rpx dashed var(--border-color); /* 虚线边框 */
		}
		
		/* 带文本的分割线样式 */
		&.divider-text {
			display: flex; /* 弹性布局 */
			align-items: center; /* 垂直居中 */
			color: var(--text-color-tertiary); /* 文字颜色 */
			font-size: 24rpx; /* 字体大小 */
			
			/* 分割线前后的线条 */
			&::before, &::after {
				content: ''; /* 空内容 */
				flex: 1; /* 占据剩余空间 */
				height: 1rpx; /* 高度 */
				background-color: var(--border-color); /* 背景色 */
			}
			
			/* 分割线前的线条 */
			&::before {
				margin-right: 20rpx; /* 右边距 */
			}
			
			/* 分割线后的线条 */
			&::after {
				margin-left: 20rpx; /* 左边距 */
			}
		}
	}
	
	/* 空状态样式工具类 */
	.empty {
		display: flex; /* 弹性布局 */
		flex-direction: column; /* 垂直排列 */
		align-items: center; /* 水平居中 */
		justify-content: center; /* 垂直居中 */
		padding: 100rpx 0; /* 内边距 */
		
		/* 空状态图片样式 */
		.empty-image {
			width: 200rpx; /* 宽度 */
			height: 200rpx; /* 高度 */
			margin-bottom: 30rpx; /* 底部边距 */
		}
		
		/* 空状态文本样式 */
		.empty-text {
			font-size: 28rpx; /* 字体大小 */
			color: var(--text-color-tertiary); /* 文字颜色 */
			margin-bottom: 40rpx; /* 底部边距 */
		}
	}
	
	/* 动画效果定义 */
	@keyframes fadeIn {
		from {
			opacity: 0; /* 初始透明度 */
			transform: translateY(20rpx); /* 初始位置 */
		}
		to {
			opacity: 1; /* 最终透明度 */
			transform: translateY(0); /* 最终位置 */
		}
	}
	
	/* 滑入动画 */
	@keyframes slideIn {
		from {
			transform: translateX(-100%); /* 初始位置 */
		}
		to {
			transform: translateX(0); /* 最终位置 */
		}
	}
	
	/* 脉冲动画 */
	@keyframes pulse {
		0% {
			transform: scale(1); /* 初始缩放 */
		}
		50% {
			transform: scale(1.05); /* 中间缩放 */
		}
		100% {
			transform: scale(1); /* 最终缩放 */
		}
	}
	
	/* 动画工具类 */
	.fade-in {
		animation: fadeIn 0.6s ease-out; /* 淡入动画 */
	}
	
	.slide-in {
		animation: slideIn 0.4s ease-out; /* 滑入动画 */
	}
	
	.pulse {
		animation: pulse 2s infinite; /* 脉冲动画 */
	}
	
	/* 隐藏工具类 */
	.hidden {
		display: none !important; /* 隐藏元素 */
	}
	
	/* 不透明度工具类 */
	.opacity-0 {
		opacity: 0; /* 完全透明 */
	}
	
	.opacity-50 {
		opacity: 0.5; /* 半透明 */
	}
	
	.opacity-100 {
		opacity: 1; /* 完全不透明 */
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
