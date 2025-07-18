<template>
	<view class="m-input-view">
		<input :focus="focus" :type="inputType" :value="value" @input="onInput" class="m-input-input" :placeholder="placeholder"
		 :password="type==='password'&&!showPassword" @focus="onFocus" @blur="onBlur" />
		<!-- 优先显示密码可见按钮 -->
		<view v-if="clearable&&!displayable&&value.length" class="m-input-icon">
			<m-icon color="#666666" type="clear" @click="clear"></m-icon>
		</view>
		<view v-if="displayable" class="m-input-icon">
			<m-icon :style="{color:showPassword?'#666666':'#cccccc'}" type="eye" @click="display"></m-icon>
		</view>
	</view>
</template>

<script>
	// 导入图标组件
	import mIcon from './m-icon/m-icon.vue'

	// 导出输入框组件配置
	export default {
		// 注册子组件
		components: {
			mIcon // 注册图标组件
		},
		// 定义组件属性
		props: {
			/**
			 * 输入类型
			 */
			type: String, // 输入框类型（如text、password等）
			/**
			 * 值
			 */
			value: String, // 输入框的值
			/**
			 * 占位符
			 */
			placeholder: String, // 输入框占位符文本
			/**
			 * 是否显示清除按钮
			 */
			clearable: {
				type: [Boolean, String], // 支持布尔值和字符串类型
				default: false // 默认不显示清除按钮
			},
			/**
			 * 是否显示密码可见按钮
			 */
			displayable: {
				type: [Boolean, String], // 支持布尔值和字符串类型
				default: false // 默认不显示密码可见按钮
			},
			/**
			 * 自动获取焦点
			 */
			focus: {
				type: [Boolean, String], // 支持布尔值和字符串类型
				default: false // 默认不自动获取焦点
			}
		},
		// 定义v-model的属性和事件
		model: {
			prop: 'value', // 指定v-model绑定的属性
			event: 'input' // 指定v-model触发的事件
		},
		// 组件数据
		data() {
			return {
				/**
				 * 显示密码明文
				 */
				showPassword: false, // 控制密码是否显示为明文，默认为false
				/**
				 * 是否获取焦点
				 */
				isFocus: false // 记录输入框是否获得焦点，默认为false
			}
		},
		// 计算属性
		computed: {
			// 计算输入框的实际类型
			inputType() {
				const type = this.type // 获取传入的类型
				return type === 'password' ? 'text' : type // 如果是password类型则返回text，否则返回原类型
			}
		},
		// 组件方法
		methods: {
			// 清除输入内容的方法
			clear() {
				this.$emit('input', '') // 触发input事件，传递空字符串
			},
			// 切换密码显示状态的方法
			display() {
				this.showPassword = !this.showPassword // 切换密码显示状态
			},
			// 输入框获得焦点时的处理方法
			onFocus() {
				this.isFocus = true // 设置焦点状态为true
			},
			// 输入框失去焦点时的处理方法
			onBlur() {
				this.$nextTick(() => {
					this.isFocus = false // 在下一个tick中设置焦点状态为false
				})
			},
			// 输入内容变化时的处理方法
			onInput(e) {
				this.$emit('input', e.detail.value) // 触发input事件，传递输入的值
			}
		}
	}
</script>

<style>
	.m-input-view {
		display: inline-flex;
		flex-direction: row;
		align-items: center;
		/* width: 100%; */
		flex: 1;
		padding: 0 10px;
	}

	.m-input-input {
		flex: 1;
		width: 100%;
		height: 20px;
		line-height: 20px;
		background-color: rgba(0, 0, 0, 0);
	}

	.m-input-icon {
		width: 20px;
		font-size: 20px;
		line-height: 20px;
		color: #666666;
	}
</style>
