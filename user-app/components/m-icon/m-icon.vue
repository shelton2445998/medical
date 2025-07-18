<template>
	<!-- 图标容器，根据type动态添加CSS类名，并绑定点击事件 -->
	<view class="m-icon" :class="['m-icon-'+type]" @click="onClick()"></view>
</template>

<script>
	// 导出图标组件配置
	export default {
		// 定义组件属性
		props: {
			/**
			 * 图标类型
			 */
			type: String // 图标类型，用于确定显示哪种图标
		},
		// 组件方法
		methods: {
			// 点击图标时的处理方法
			onClick() {
				this.$emit('click') // 触发click事件，通知父组件图标被点击
			}
		}
	}
</script>

<style>
	/* 导入图标样式文件 */
	@import "./m-icon.css";
</style>
