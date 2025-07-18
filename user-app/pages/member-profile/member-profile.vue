<!--
@name: 用户资料页面
@description: 用户个人信息管理页面，支持查看和编辑个人资料信息
@author: 医疗系统开发团队
@created: 2024年医疗系统开发
@features:
- 显示用户头像和基本信息
- 支持在线编辑昵称、性别、手机号、身份证号等
- 支持头像上传和更换
- 支持个人简介编辑
- 实时保存编辑内容
- 表单验证和数据校验
- 刷新用户信息功能
@页面路径: /pages/member-profile/member-profile
@参数说明:
- 无参数，页面加载时自动获取当前用户信息
-->

<template>
	<view class="content">
		<!-- 
			动态背景装饰
			增强页面视觉效果
		-->
		<view class="floating-shapes">
			<view class="shape shape-1"></view>
			<view class="shape shape-2"></view>
			<view class="shape shape-3"></view>
			<view class="shape shape-4"></view>
		</view>
		
		<view class="main-content">
			<!-- 
				页面标题
				显示页面标题和描述
			-->
			<view class="page-header">
				<view class="header-icon">👤</view>
				<view class="header-title">个人信息</view>
				<view class="header-desc">管理您的个人资料和账户信息</view>
			</view>
			
			<!-- 
				用户信息卡片
				显示和编辑用户的个人信息
			-->
			<view class="user-card">
				<!-- 
					头像区域
					显示用户头像，支持点击更换
				-->
				<view class="avatar-section">
					<view class="avatar-container">
						<image 
							:src="userInfo.head || '/static/img/logo.png'" 
							class="avatar"
							@click="editAvatar"
							mode="aspectFill"
						/>
						<view class="avatar-overlay">
							<text class="avatar-edit-icon">📷</text>
						</view>
					</view>
					<view class="avatar-edit-hint">点击更换头像</view>
				</view>
				
				<!-- 
					基本信息编辑区域
					支持内联编辑各种用户信息
				-->
				<view class="info-section">
					<view class="section-title">
						<text class="title-icon">📝</text>
						<text class="title-text">基本信息</text>
					</view>
					
					<!-- 昵称编辑 -->
					<view class="info-item">
						<view class="item-header">
							<text class="item-icon">👤</text>
							<text class="item-label">昵称</text>
							<text class="required-mark">*</text>
						</view>
						<view class="value-container">
							<text v-if="editingField !== 'nickname'" class="value" @click="startEdit('nickname', userInfo.nickname)">
								{{ userInfo.nickname || '未设置' }}
							</text>
							<input 
								v-else 
								v-model="editValue" 
								class="edit-input"
								@blur="saveField('nickname')"
								@confirm="saveField('nickname')"
								placeholder="请输入昵称（必填，不能为空）"
							/>
						</view>
					</view>
					
					<!-- 性别选择 -->
					<view class="info-item">
						<view class="item-header">
							<text class="item-icon">⚧</text>
							<text class="item-label">性别</text>
							<text class="required-mark">*</text>
						</view>
						<view class="value-container">
							<text v-if="editingField !== 'gender'" class="value" @click="startEdit('gender', userInfo.gender)">
								{{ getGenderText(userInfo.gender) }}
							</text>
							<picker 
								v-else 
								:value="editValue" 
								:range="genderOptions" 
								range-key="label"
								@change="onGenderChange"
								class="edit-picker"
							>
								<view class="picker-text">{{ getGenderText(editValue) }}</view>
							</picker>
						</view>
					</view>
					
					<!-- 手机号码编辑 -->
					<view class="info-item">
						<view class="item-header">
							<text class="item-icon">📱</text>
							<text class="item-label">手机号码</text>
							<text class="required-mark">*</text>
						</view>
						<view class="value-container">
							<text v-if="editingField !== 'phone'" class="value" @click="startEdit('phone', userInfo.phone)">
								{{ userInfo.phone || '未设置' }}
							</text>
							<input 
								v-else 
								v-model="editValue" 
								class="edit-input"
								type="number"
								maxlength="11"
								@blur="saveField('phone')"
								@confirm="saveField('phone')"
								placeholder="请输入手机号码（必填，不能为空）"
							/>
						</view>
					</view>
					
					<!-- 身份证号编辑 -->
					<view class="info-item">
						<view class="item-header">
							<text class="item-icon">🆔</text>
							<text class="item-label">身份证号</text>
						</view>
						<view class="value-container">
							<text v-if="editingField !== 'idCard'" class="value" @click="startEdit('idCard', userInfo.idCard)">
								{{ userInfo.idCard || '未设置' }}
							</text>
							<input 
								v-else 
								v-model="editValue" 
								class="edit-input"
								maxlength="18"
								@blur="saveField('idCard')"
								@confirm="saveField('idCard')"
								placeholder="请输入身份证号"
							/>
						</view>
					</view>
					
					<!-- 个人简介编辑 -->
					<view class="info-item">
						<view class="item-header">
							<text class="item-icon">📄</text>
							<text class="item-label">个人简介</text>
							<text class="required-mark">*</text>
						</view>
						<view class="value-container">
							<text v-if="editingField !== 'introduction'" class="value" @click="startEdit('introduction', userInfo.introduction)">
								{{ userInfo.introduction || '未设置' }}
							</text>
							<textarea 
								v-else 
								v-model="editValue" 
								class="edit-textarea"
								@blur="saveField('introduction')"
								maxlength="200"
								placeholder="请输入个人简介（必填，不能为空）"
							></textarea>
						</view>
					</view>
					
					<!-- 备注信息编辑 -->
					<view class="info-item">
						<view class="item-header">
							<text class="item-icon">📝</text>
							<text class="item-label">备注</text>
						</view>
						<view class="value-container">
							<text v-if="editingField !== 'remark'" class="value" @click="startEdit('remark', userInfo.remark)">
								{{ userInfo.remark || '未设置' }}
							</text>
							<textarea 
								v-else 
								v-model="editValue" 
								class="edit-textarea"
								@blur="saveField('remark')"
								maxlength="500"
								placeholder="请输入备注信息（可选）"
							></textarea>
						</view>
					</view>
				</view>
				
				<!-- 
					账户信息区域
					显示账户相关信息
				-->
				<view class="info-section">
					<view class="section-title">
						<text class="title-icon">🔐</text>
						<text class="title-text">账户信息</text>
					</view>
					
					<!-- 注册时间 -->
					<view class="info-item">
						<view class="item-header">
							<text class="item-icon">📅</text>
							<text class="item-label">注册时间</text>
						</view>
						<view class="value-container">
							<text class="value readonly">{{ formatTime(userInfo.createTime) }}</text>
						</view>
					</view>
					
					<!-- 最后登录时间 -->
					<view class="info-item">
						<view class="item-header">
							<text class="item-icon">🕐</text>
							<text class="item-label">最后登录</text>
						</view>
						<view class="value-container">
							<text class="value readonly">{{ formatTime(userInfo.lastLoginTime) }}</text>
						</view>
					</view>
					
					<!-- 账户状态 -->
					<view class="info-item">
						<view class="item-header">
							<text class="item-icon">✅</text>
							<text class="item-label">账户状态</text>
						</view>
						<view class="value-container">
							<text class="value readonly status-active">正常</text>
						</view>
					</view>
				</view>
				
				<!-- 
					操作按钮区域
					提供刷新和修改密码等操作
				-->
				<view class="action-section">
					<button class="action-btn secondary" @click="refreshUserInfo">
						<text class="btn-icon">🔄</text>
						<text>刷新信息</text>
					</button>
					<button class="action-btn primary" @click="changePassword">
						<text class="btn-icon">🔑</text>
						<text>修改密码</text>
					</button>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		name: 'MemberProfile',
		
		/**
		 * 组件数据
		 * @returns {Object} 组件数据对象
		 */
		data() {
			return {
				// 用户信息数据
				userInfo: {
					id: null,
					nickname: '',
					phone: '',
					head: '',
					gender: null,
					idCard: '',
					introduction: '',
					remark: '',
					createTime: '',
					lastLoginTime: ''
				},
				
				// 编辑状态
				editingField: '',  // 当前正在编辑的字段
				editValue: '',     // 编辑时的临时值
				
				// 性别选项
				genderOptions: [
					{ label: '男', value: 1 },
					{ label: '女', value: 0 }
				]
			}
		},
		
		/**
		 * 页面加载时的处理
		 */
		onLoad() {
			this.loadUserInfo();
		},
		
		/**
		 * 页面显示时的处理
		 */
		onShow() {
			// 从其他页面返回时刷新用户信息
			this.loadUserInfo();
		},
		
		methods: {
			/**
			 * 加载用户信息
			 * 从后端API获取当前用户的详细信息
			 */
			loadUserInfo() {
				const token = uni.getStorageSync('uniIdToken');
				
				if (!token) {
					uni.showToast({
						title: '请先登录',
						icon: 'none'
					});
					setTimeout(() => {
						uni.navigateTo({
							url: '/pages/login/login'
						});
					}, 2000);
					return;
				}
				
				uni.showLoading({
					title: '加载中...'
				});
				
				// 使用API配置中的地址
				const API_BASE_URL = process.env.NODE_ENV === 'development' 
					? 'http://localhost:8888/api' 
					: 'http://39.104.57.236:8888/api';
				
				uni.request({
					url: `${API_BASE_URL}/app/getUserInfo`,
					method: 'GET',
					header: {
						'Authorization': token
					},
					success: (res) => {
						uni.hideLoading();
						console.log('获取用户信息响应：', res);
						
						if (res.statusCode === 200 && res.data.code === 200) {
							this.userInfo = res.data.data;
							console.log('用户信息：', this.userInfo);
						} else {
							console.error('获取用户信息失败：', res.data);
							uni.showToast({
								title: res.data.message || '获取用户信息失败',
								icon: 'none'
							});
						}
					},
					fail: (err) => {
						uni.hideLoading();
						console.error('获取用户信息网络错误：', err);
						uni.showToast({
							title: '网络连接失败',
							icon: 'none'
						});
					}
				});
			},
			
			/**
			 * 开始编辑字段
			 * @param {string} field 字段名
			 * @param {*} value 当前值
			 */
			startEdit(field, value) {
				this.editingField = field;
				this.editValue = value || '';
			},
			
			/**
			 * 性别选择变化处理
			 * @param {Event} e 选择事件
			 */
			onGenderChange(e) {
				const selectedIndex = e.detail.value;
				this.editValue = this.genderOptions[selectedIndex].value;
				this.saveField('gender');
			},
			
			/**
			 * 保存字段
			 * @param {string} field 字段名
			 */
			saveField(field) {
				// 必填字段验证
				if (['nickname', 'gender', 'phone', 'introduction'].includes(field)) {
					if (!this.editValue && this.editValue !== 0) {
						uni.showToast({
							title: '必填字段不能为空',
							icon: 'none'
						});
						return;
					}
				}
				
				// 手机号码验证
				if (field === 'phone') {
					const phoneRegex = /^1[3-9]\d{9}$/;
					if (!phoneRegex.test(this.editValue)) {
						uni.showToast({
							title: '请输入正确的手机号码',
							icon: 'none'
						});
						return;
					}
				}
				
				// 身份证号验证
				if (field === 'idCard' && this.editValue) {
					const idCardRegex = /^[1-9]\d{5}(18|19|20)\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\d|3[01])\d{3}[0-9Xx]$/;
					if (!idCardRegex.test(this.editValue)) {
						uni.showToast({
							title: '请输入正确的身份证号',
							icon: 'none'
						});
						return;
					}
				}
				
				// 个人简介不能为空验证
				if (field === 'introduction') {
					if (!this.editValue || this.editValue.trim() === '') {
						uni.showToast({
							title: '个人简介不能为空',
							icon: 'none'
						});
						return;
					}
					// 去除首尾空格
					this.editValue = this.editValue.trim();
				}
				
				// 更新用户信息
				const updateData = {};
				updateData[field] = this.editValue;
				this.updateUserInfo(updateData);
				
				// 退出编辑模式
				this.editingField = '';
				this.editValue = '';
			},
			
			/**
			 * 获取性别文本
			 * @param {number} gender 性别值
			 * @returns {string} 性别文本
			 */
			getGenderText(gender) {
				if (gender === null || gender === undefined) return '未设置';
				return gender === 1 ? '男' : '女';
			},
			
			/**
			 * 格式化时间
			 * @param {string} timeStr 时间字符串
			 * @returns {string} 格式化后的时间
			 */
			formatTime(timeStr) {
				if (!timeStr) return '未知';
				const date = new Date(timeStr);
				return date.toLocaleString('zh-CN');
			},
			
			/**
			 * 编辑头像
			 * 调用系统选择图片功能
			 */
			editAvatar() {
				uni.chooseImage({
					count: 1,
					sizeType: ['compressed'],
					sourceType: ['album', 'camera'],
					success: (res) => {
						const tempFilePath = res.tempFilePaths[0];
						// 这里可以上传头像到服务器
						// 暂时使用本地路径作为示例
						this.updateUserInfo({ head: tempFilePath });
						uni.showToast({
							title: '头像更新成功',
							icon: 'success'
						});
					},
					fail: (err) => {
						uni.showToast({
							title: '选择图片失败',
							icon: 'none'
						});
					}
				});
			},
			
			/**
			 * 更新用户信息
			 * @param {Object} updateData 要更新的数据
			 */
			updateUserInfo(updateData) {
				// 获取token
				const token = uni.getStorageSync('uniIdToken');
				
				if (!token) {
					uni.showToast({
						title: '请先登录',
						icon: 'none'
					});
					return;
				}
				
				// 显示加载提示
				uni.showLoading({
					title: '更新中...'
				});
				
				// 使用API配置中的地址
				const API_BASE_URL = process.env.NODE_ENV === 'development' 
					? 'http://localhost:8888/api' 
					: 'http://39.104.57.236:8888/api';
				
				// 适配后端AppUserUpdateProfileDto的字段
				const requestData = {};
				if (updateData.nickname !== undefined) {
					requestData.nickname = updateData.nickname;
				}
				if (updateData.phone !== undefined) {
					requestData.phone = updateData.phone;
				}
				if (updateData.head !== undefined) {
					requestData.head = updateData.head;
				}
				if (updateData.gender !== undefined) {
					requestData.gender = updateData.gender;
				}
				if (updateData.idCard !== undefined) {
					requestData.idCard = updateData.idCard;
				}
				if (updateData.introduction !== undefined) {
					requestData.introduction = updateData.introduction;
				}
				if (updateData.remark !== undefined) {
					requestData.remark = updateData.remark;
				}
				
				uni.request({
					url: `${API_BASE_URL}/app/updateUserProfile`,
					method: 'POST',
					header: {
						'Authorization': token,
						'Content-Type': 'application/json'
					},
					data: requestData,
					success: (res) => {
						uni.hideLoading();
						console.log('更新用户信息响应：', res);
						if (res.statusCode === 200 && res.data.code === 200) {
							uni.showToast({
								title: '更新成功',
								icon: 'success'
							});
							// 重新加载用户信息
							this.loadUserInfo();
						} else {
							console.error('更新用户信息失败：', res.data);
							uni.showToast({
								title: res.data.message || '更新失败',
								icon: 'none'
							});
						}
					},
					fail: (err) => {
						uni.hideLoading();
						console.error('更新用户信息网络错误：', err);
						uni.showToast({
							title: '网络连接失败',
							icon: 'none'
						});
					}
				});
			},
			
			/**
			 * 刷新用户信息
			 */
			refreshUserInfo() {
				uni.showLoading({
					title: '刷新中...'
				});
				this.loadUserInfo();
				setTimeout(() => {
					uni.hideLoading();
					uni.showToast({
						title: '刷新成功',
						icon: 'success'
					});
				}, 1000);
			},
			
			/**
			 * 修改密码
			 */
			changePassword() {
				uni.showModal({
					title: '修改密码',
					content: '密码修改功能需要单独实现，请联系管理员',
					showCancel: false
				});
			}
		}
	}
</script>

<style lang="scss">
/* 
	页面整体样式
	设置页面背景和基本布局
*/
.content {
	background: linear-gradient(135deg, #0984e3 0%, #74b9ff 50%, #0984e3 100%);
	min-height: 100vh;
	padding-top: 0;
	padding-bottom: 40rpx;
	position: relative;
	overflow: hidden;
	
	&::before {
		content: '';
		position: absolute;
		top: -50%;
		left: -50%;
		width: 200%;
		height: 200%;
		background: radial-gradient(circle, rgba(255, 255, 255, 0.1) 0%, transparent 70%);
		animation: flow 20s linear infinite;
		pointer-events: none;
	}
	
	&::after {
		content: '';
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: linear-gradient(45deg, transparent 30%, rgba(255, 255, 255, 0.05) 50%, transparent 70%);
		animation: shimmer 8s ease-in-out infinite;
		pointer-events: none;
	}
}

/* 
	动态背景装饰
	添加浮动的装饰性元素
*/
.floating-shapes {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	pointer-events: none;
	z-index: -1;

	.shape {
		position: absolute;
		background: rgba(255, 255, 255, 0.1);
		border-radius: 50%;
		filter: blur(50px);
		animation: float 15s infinite ease-in-out;
		transition: all 0.3s ease;
		
		&::before {
			content: '';
			position: absolute;
			top: -10%;
			left: -10%;
			width: 120%;
			height: 120%;
			background: radial-gradient(circle, rgba(255, 255, 255, 0.2) 0%, transparent 70%);
			border-radius: 50%;
			animation: pulse 3s ease-in-out infinite;
		}

		&.shape-1 {
			width: 100px;
			height: 100px;
			top: 10%;
			left: 10%;
			animation-delay: -2s;
		}
		&.shape-2 {
			width: 150px;
			height: 150px;
			top: 70%;
			left: 30%;
			animation-delay: -5s;
		}
		&.shape-3 {
			width: 120px;
			height: 120px;
			top: 20%;
			right: 20%;
			animation-delay: -8s;
		}
		&.shape-4 {
			width: 180px;
			height: 180px;
			bottom: 10%;
			right: 50%;
			animation-delay: -10s;
		}
	}
}

/* 
	主要内容区域
	设置内容的定位和层级
*/
.main-content {
	padding: 20rpx 40rpx 0 40rpx;
	position: relative;
	z-index: 1;
}

/* 
	页面头部样式
	设置标题和描述的样式
*/
.page-header {
	text-align: center;
	margin-bottom: 40rpx;
	animation: fadeInDown 0.8s ease-out;
	
	.header-icon {
		font-size: 80rpx;
		margin-bottom: 20rpx;
		animation: bounce 2s infinite;
		transition: all 0.3s ease;
		
		&:hover {
			transform: scale(1.2);
		}
	}
	
	.header-title {
		font-size: 44rpx;
		font-weight: bold;
		color: #ffffff;
		margin-bottom: 16rpx;
		text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.3);
		transition: all 0.3s ease;
	}
	
	.header-desc {
		font-size: 28rpx;
		color: rgba(255, 255, 255, 0.8);
		transition: all 0.3s ease;
	}
}

/* 
	用户信息卡片样式
	设置用户信息卡片的样式
*/
.user-card {
	background: rgba(255, 255, 255, 0.95);
	border-radius: 24rpx;
	padding: 40rpx;
	box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
	backdrop-filter: blur(10rpx);
	transition: all 0.3s ease;
	animation: fadeInUp 0.8s ease-out;
	
	&:hover {
		transform: translateY(-4rpx);
		box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.15);
	}
}

/* 
	头像区域样式
	设置头像显示和编辑的样式
*/
.avatar-section {
	text-align: center;
	margin-bottom: 40rpx;
	
	.avatar-container {
		position: relative;
		display: inline-block;
		margin-bottom: 20rpx;
		
		.avatar {
			width: 120rpx;
			height: 120rpx;
			border-radius: 50%;
			border: 4rpx solid rgba(9, 132, 227, 0.2);
			transition: all 0.3s ease;
			
			&:hover {
				border-color: #0984e3;
			}
		}
		
		.avatar-overlay {
			position: absolute;
			top: 0;
			left: 0;
			right: 0;
			bottom: 0;
			background: rgba(0, 0, 0, 0.5);
			border-radius: 50%;
			display: flex;
			align-items: center;
			justify-content: center;
			opacity: 0;
			transition: opacity 0.3s ease;
			
			&:hover {
				opacity: 1;
			}
			
			.avatar-edit-icon {
				color: white;
				font-size: 32rpx;
			}
		}
	}
	
	.avatar-edit-hint {
		font-size: 24rpx;
		color: #666;
	}
}

/* 
	信息区域样式
	设置信息区域的样式
*/
.info-section {
	margin-bottom: 40rpx;
	
	.section-title {
		display: flex;
		align-items: center;
		margin-bottom: 30rpx;
		padding-bottom: 20rpx;
		border-bottom: 2rpx solid rgba(9, 132, 227, 0.1);
		
		.title-icon {
			font-size: 28rpx;
			color: #0984e3;
			margin-right: 15rpx;
		}
		
		.title-text {
			font-size: 30rpx;
			font-weight: bold;
			color: #333;
		}
	}
	
	.info-item {
		margin-bottom: 30rpx;
		
		.item-header {
			display: flex;
			align-items: center;
			margin-bottom: 15rpx;
			
			.item-icon {
				font-size: 24rpx;
				color: #0984e3;
				margin-right: 10rpx;
			}
			
			.item-label {
				font-size: 28rpx;
				color: #333;
				font-weight: 500;
			}
			
			.required-mark {
				color: #ff4757;
				font-size: 24rpx;
				margin-left: 5rpx;
			}
		}
		
		.value-container {
			.value {
				font-size: 30rpx;
				color: #0984e3;
				padding: 15rpx 20rpx;
				background: rgba(9, 132, 227, 0.1);
				border-radius: 12rpx;
				min-height: 50rpx;
				display: flex;
				align-items: center;
				transition: all 0.3s ease;
				
				&:hover {
					background: rgba(9, 132, 227, 0.15);
				}
				
				&.readonly {
					color: #666;
					background: #f8f9fa;
					cursor: not-allowed;
				}
				
				&.status-active {
					color: #20bf6b;
					background: rgba(32, 191, 107, 0.1);
				}
			}
			
			.edit-input,
			.edit-textarea {
				font-size: 30rpx;
				color: #333;
				padding: 15rpx 20rpx;
				background: white;
				border: 2rpx solid #0984e3;
				border-radius: 12rpx;
				min-height: 50rpx;
				width: 100%;
				outline: none;
				transition: all 0.3s ease;
				
				&:focus {
					border-color: #74b9ff;
					box-shadow: 0 0 0 4rpx rgba(9, 132, 227, 0.1);
				}
			}
			
			.edit-textarea {
				min-height: 120rpx;
				resize: vertical;
			}
			
			.edit-picker {
				.picker-text {
					font-size: 30rpx;
					color: #333;
					padding: 15rpx 20rpx;
					background: white;
					border: 2rpx solid #0984e3;
					border-radius: 12rpx;
					min-height: 50rpx;
					display: flex;
					align-items: center;
				}
			}
		}
	}
}

/* 
	操作按钮区域样式
	设置操作按钮的样式
*/
.action-section {
	display: flex;
	gap: 20rpx;
	margin-top: 40rpx;
	
	.action-btn {
		flex: 1;
		height: 88rpx;
		border: none;
		border-radius: 44rpx;
		font-size: 28rpx;
		font-weight: bold;
		display: flex;
		align-items: center;
		justify-content: center;
		transition: all 0.3s ease;
		box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.1);
		
		.btn-icon {
			font-size: 24rpx;
			margin-right: 10rpx;
		}
		
		&:hover {
			transform: translateY(-4rpx);
			box-shadow: 0 12rpx 32rpx rgba(0, 0, 0, 0.15);
		}
		
		&.secondary {
			background: #f8f9fa;
			color: #0984e3;
		}
		
		&.primary {
			background: linear-gradient(135deg, #0984e3, #74b9ff);
			color: white;
		}
		
		&::after {
			border: none;
		}
	}
}

/* 
	动画效果定义
	定义各种动画效果
*/
@keyframes flow {
	0%, 100% { transform: translate(-50%, -50%) rotate(0deg); }
	50% { transform: translate(-50%, -50%) rotate(180deg); }
}

@keyframes shimmer {
	0%, 100% { opacity: 0.3; }
	50% { opacity: 0.8; }
}

@keyframes float {
	0%, 100% { transform: translateY(0px) rotate(0deg); }
	33% { transform: translateY(-30px) rotate(120deg); }
	66% { transform: translateY(-15px) rotate(240deg); }
}

@keyframes pulse {
	0%, 100% { opacity: 0.8; transform: scale(1); }
	50% { opacity: 1; transform: scale(1.05); }
}

@keyframes fadeInDown {
	from { opacity: 0; transform: translateY(-30px); }
	to { opacity: 1; transform: translateY(0); }
}

@keyframes fadeInUp {
	from { opacity: 0; transform: translateY(30px); }
	to { opacity: 1; transform: translateY(0); }
}

@keyframes bounce {
	0%, 20%, 50%, 80%, 100% { transform: translateY(0); }
	40% { transform: translateY(-10px); }
	60% { transform: translateY(-5px); }
}
</style> 