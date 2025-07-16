<template>
	<view class="content">
		<!-- 动态背景装饰 -->
		<view class="floating-shapes">
			<view class="shape shape-1"></view>
			<view class="shape shape-2"></view>
			<view class="shape shape-3"></view>
			<view class="shape shape-4"></view>
		</view>
		
		<view class="main-content">
			<!-- 页面标题 -->
			<view class="page-header">
				<view class="header-icon">👤</view>
				<view class="header-title">个人信息</view>
				<view class="header-desc">管理您的个人资料和账户信息</view>
			</view>
			
			<!-- 用户信息卡片 -->
			<view class="user-card">
				<!-- 头像区域 -->
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
				
				<!-- 基本信息 -->
				<view class="info-section">
					<view class="section-title">
						<text class="title-icon">📝</text>
						<text class="title-text">基本信息</text>
					</view>
					
					<!-- 昵称 -->
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
					
					<!-- 性别 -->
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
					
					<!-- 手机号码 -->
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
					
					<!-- 身份证号 -->
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
					
					<!-- 个人简介 -->
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
								maxlength="200"
								@blur="saveField('introduction')"
								placeholder="请输入个人简介（必填，不能为空）"
							/>
						</view>
					</view>
					
					<!-- 备注 -->
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
								maxlength="500"
								@blur="saveField('remark')"
								placeholder="请输入备注"
							/>
						</view>
					</view>
				</view>
				
				<!-- 账户信息 -->
				<view class="account-section">
					<view class="section-title">
						<text class="title-icon">🔐</text>
						<text class="title-text">账户信息</text>
					</view>
					<view class="account-grid">
						<view class="account-item">
							<text class="account-label">账号</text>
							<text class="account-value">{{ userInfo.username }}</text>
						</view>
						<view class="account-item">
							<text class="account-label">最后登录</text>
							<text class="account-value">{{ formatTime(userInfo.last_login_time) }}</text>
						</view>
					</view>
				</view>
				
				<!-- 操作按钮 -->
				<view class="action-buttons">
					<button class="btn btn-primary" @click="refreshUserInfo">
						<text class="btn-icon">🔄</text>
						<text class="btn-text">刷新信息</text>
					</button>
					<button class="btn btn-secondary" @click="changePassword">
						<text class="btn-icon">🔒</text>
						<text class="btn-text">修改密码</text>
					</button>
				</view>
			</view>
		</view>
		
		<!-- 加载遮罩 -->
		<view v-if="loading" class="loading-mask">
			<view class="loading-content">
				<view class="loading-spinner"></view>
				<text class="loading-text">加载中...</text>
			</view>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				loading: false,
				editingField: '', // 当前编辑的字段
				editValue: '', // 编辑的值
				genderOptions: [
					{ value: 0, label: '女' },
					{ value: 1, label: '男' }
				],
				userInfo: {
					id: '',
					username: '',
					nickname: '',
					openid: '',
					phone: '',
					head: '/static/img/logo.png',
					gender: null,
					idCard: '',
					introduction: '',
					user_role_id: 1,
					status: 1,
					register_time: '',
					register_ip: '',
					register_ip_area: '',
					last_login_time: '',
					last_login_ip: '',
					last_login_ip_area: '',
					remark: ''
				}
			}
		},
		onLoad() {
			this.loadUserInfo();
		},
		methods: {
			// 加载用户信息
			loadUserInfo() {
				// 获取token
				const token = uni.getStorageSync('uniIdToken');
				
				if (!token) {
					uni.showToast({
						title: '请先登录',
						icon: 'none'
					});
					return;
				}
				
				this.loading = true;
				
				// 使用API配置中的地址
				const API_BASE_URL = process.env.NODE_ENV === 'development' 
					? 'http://localhost:8888/api' 
					: 'http://39.104.57.236:8888/api';
				
				uni.request({
					url: `${API_BASE_URL}/app/getLoginUserInfo`,
					method: 'POST',
					header: {
						'Authorization': token,
						'Content-Type': 'application/json'
					},
					success: (res) => {
						this.loading = false;
						console.log('获取用户信息响应：', res);
						if (res.statusCode === 200 && res.data.code === 200) {
							const userData = res.data.data;
							// 更新用户信息，处理字段名映射
							this.userInfo = {
								id: userData.userId || '',
								username: userData.username || '',
								nickname: userData.nickname || '',
								openid: userData.openid || '',
								phone: userData.phone || '',
								head: userData.head || '/static/img/logo.png',
								gender: userData.gender,
								idCard: userData.idCard || '',
								introduction: userData.introduction || '',
								user_role_id: userData.userRoleId || 1,
								status: userData.status || 1,
								register_time: userData.registerTime || '',
								register_ip: userData.registerIp || '',
								register_ip_area: userData.registerIpArea || '',
								last_login_time: userData.lastLoginTime || '',
								last_login_ip: userData.lastLoginIp || '',
								last_login_ip_area: userData.lastLoginIpArea || '',
								remark: userData.remark || ''
							};
							console.log('用户信息更新成功：', this.userInfo);
						} else {
							console.error('获取用户信息失败：', res.data);
							uni.showToast({
								title: res.data.message || '获取用户信息失败',
								icon: 'none'
							});
						}
					},
					fail: (err) => {
						this.loading = false;
						console.error('获取用户信息网络错误：', err);
						uni.showToast({
							title: '网络连接失败',
							icon: 'none'
						});
					}
				});
			},
			
			// 开始编辑字段
			startEdit(field, value) {
				this.editingField = field;
				this.editValue = value || '';
			},
			
			// 性别选择器变化
			onGenderChange(e) {
				const index = e.detail.value;
				this.editValue = this.genderOptions[index].value;
				this.saveField('gender');
			},
			
			// 保存字段
			saveField(field) {
				if (this.editValue === this.userInfo[field]) {
					// 值没有变化，直接退出编辑模式
					this.editingField = '';
					this.editValue = '';
					return;
				}
				
				// 验证输入
				if (field === 'nickname') {
					if (!this.editValue || this.editValue.trim() === '') {
						uni.showToast({
							title: '昵称不能为空',
							icon: 'none'
						});
						return;
					}
					// 去除首尾空格
					this.editValue = this.editValue.trim();
				}
				
				if (field === 'gender') {
					if (this.editValue === null || this.editValue === undefined || this.editValue === '') {
						uni.showToast({
							title: '请选择性别',
							icon: 'none'
						});
						return;
					}
				}
				
				if (field === 'phone') {
					if (!this.editValue || this.editValue.trim() === '') {
						uni.showToast({
							title: '手机号码不能为空',
							icon: 'none'
						});
						return;
					}
					// 去除首尾空格
					this.editValue = this.editValue.trim();
					// 验证手机号格式
					if (!/^1[3-9]\d{9}$/.test(this.editValue)) {
						uni.showToast({
							title: '请输入正确的手机号码',
							icon: 'none'
						});
						return;
					}
				}
				
				if (field === 'idCard' && this.editValue) {
					if (!/^[1-9]\d{5}(18|19|20)\d{2}((0[1-9])|(1[0-2]))(([0-2][1-9])|10|20|30|31)\d{3}[0-9Xx]$/.test(this.editValue)) {
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
			
			// 获取性别文本
			getGenderText(gender) {
				if (gender === null || gender === undefined) return '未设置';
				return gender === 1 ? '男' : '女';
			},
			

			
			// 格式化时间
			formatTime(timeStr) {
				if (!timeStr) return '未知';
				const date = new Date(timeStr);
				return date.toLocaleString('zh-CN');
			},
			
			// 编辑头像
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
			
			// 更新用户信息
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
			
			// 刷新用户信息
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
			
			// 修改密码
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

.floating-shapes {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	z-index: -1;
	
	.shape {
		position: absolute;
		background: linear-gradient(135deg, rgba(255, 255, 255, 0.1), rgba(255, 255, 255, 0.05));
		border-radius: 50%;
		animation: float 10s infinite ease-in-out;
		transition: all 0.3s ease;
		
		&:hover {
			transform: scale(1.2);
			background: linear-gradient(135deg, rgba(255, 255, 255, 0.2), rgba(255, 255, 255, 0.1));
		}
		
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
	}
	
	.shape-1 {
		width: 200px;
		height: 200px;
		top: 10%;
		left: 10%;
		animation-delay: 0s;
	}
	
	.shape-2 {
		width: 150px;
		height: 150px;
		bottom: 20%;
		right: 20%;
		animation-delay: 1s;
	}
	
	.shape-3 {
		width: 100px;
		height: 100px;
		top: 70%;
		left: 70%;
		animation-delay: 2s;
	}
	
	.shape-4 {
		width: 120px;
		height: 120px;
		top: 30%;
		right: 10%;
		animation-delay: 3s;
	}
}

.main-content {
	padding: 20rpx 40rpx 0 40rpx;
	position: relative;
	z-index: 1;
}

.page-header {
	text-align: center;
	margin-bottom: 20rpx;
	animation: fadeInDown 0.8s ease-out;
	
	.header-icon {
		font-size: 32rpx;
		margin-bottom: 8rpx;
		animation: bounce 2s infinite;
		transition: all 0.3s ease;
		
		&:hover {
			transform: scale(1.2);
		}
	}
	
	.header-title {
		font-size: 36rpx;
		font-weight: bold;
		color: #ffffff;
		margin-bottom: 6rpx;
		text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.3);
		transition: all 0.3s ease;
	}
	
	.header-desc {
		font-size: 24rpx;
		color: rgba(255, 255, 255, 0.8);
		transition: all 0.3s ease;
	}
}

.user-card {
	background: rgba(255, 255, 255, 0.95);
	border-radius: 24rpx;
	padding: 40rpx;
	box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
	backdrop-filter: blur(10rpx);
	transition: all 0.3s ease;
	animation: fadeInUp 0.8s ease-out 0.3s both;
	position: relative;
	overflow: hidden;
	
	&::before {
		content: '';
		position: absolute;
		top: 0;
		left: 0;
		right: 0;
		bottom: 0;
		background: 
			radial-gradient(circle at 10% 10%, rgba(9, 132, 227, 0.02) 0%, transparent 50%),
			radial-gradient(circle at 90% 90%, rgba(116, 185, 255, 0.02) 0%, transparent 50%);
		pointer-events: none;
		animation: gentleFlow 10s ease-in-out infinite;
	}
	
	&::after {
		content: '';
		position: absolute;
		top: 20rpx;
		right: 20rpx;
		width: 60rpx;
		height: 60rpx;
		background: linear-gradient(135deg, rgba(9, 132, 227, 0.1), rgba(116, 185, 255, 0.1));
		border-radius: 50%;
		animation: float 6s ease-in-out infinite;
	}
	
	&:hover {
		transform: translateY(-4rpx);
		box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.15);
	}
}

.avatar-section {
	text-align: center;
	margin-bottom: 40rpx;
	
	.avatar-container {
		position: relative;
		width: 120rpx;
		height: 120rpx;
		margin: 0 auto;
		border-radius: 50%;
		overflow: hidden;
		box-shadow: 0 8rpx 24rpx rgba(0, 0, 0, 0.2);
		transition: all 0.3s ease;
		
		&:hover {
			transform: scale(1.05);
			box-shadow: 0 12rpx 32rpx rgba(0, 0, 0, 0.3);
		}
	}
	
	.avatar {
		width: 100%;
		height: 100%;
		border-radius: 50%;
		border: 4rpx solid #fff;
		transition: all 0.3s ease;
		
		&:active {
			transform: scale(0.95);
		}
	}
	
	.avatar-overlay {
		position: absolute;
		top: 0;
		left: 0;
		width: 100%;
		height: 100%;
		border-radius: 50%;
		background: rgba(0, 0, 0, 0.5);
		display: flex;
		align-items: center;
		justify-content: center;
		opacity: 0;
		transition: opacity 0.3s ease;
		
		&:active {
			opacity: 1;
		}
		
		.avatar-edit-icon {
			font-size: 28rpx;
			color: white;
		}
	}
	
	.avatar-edit-hint {
		margin-top: 15rpx;
		font-size: 24rpx;
		color: #666;
		opacity: 0.8;
	}
}

.info-section {
	margin-bottom: 40rpx;
	
	.section-title {
		display: flex;
		align-items: center;
		margin-bottom: 25rpx;
		padding-bottom: 15rpx;
		border-bottom: 2rpx solid #e9ecef;
		
		.title-icon {
			font-size: 32rpx;
			margin-right: 10rpx;
			color: #007bff;
		}
		
		.title-text {
			font-size: 30rpx;
			font-weight: bold;
			color: #333;
		}
	}
}

.info-item {
	margin-bottom: 25rpx;
	padding: 20rpx;
	background: rgba(248, 249, 250, 0.8);
	border-radius: 16rpx;
	border: 1rpx solid rgba(0, 123, 255, 0.1);
	transition: all 0.3s ease;
	
	&:hover {
		background: rgba(248, 249, 250, 1);
		border-color: rgba(0, 123, 255, 0.2);
		transform: translateY(-2rpx);
		box-shadow: 0 4rpx 16rpx rgba(0, 123, 255, 0.1);
	}
	
	.item-header {
		display: flex;
		align-items: center;
		margin-bottom: 10rpx;
		
		.item-icon {
			font-size: 30rpx;
			margin-right: 10rpx;
			color: #007bff;
		}
		
		.item-label {
			font-size: 28rpx;
			color: #333;
			font-weight: 500;
		}
		
		.required-mark {
			font-size: 28rpx;
			color: #ff4757;
			margin-left: 5rpx;
			font-weight: bold;
		}
	}
	
	.value-container {
		margin-top: 10rpx;
	}
	
	.value {
		display: block;
		font-size: 28rpx;
		color: #666;
		padding: 12rpx 16rpx;
		border-radius: 12rpx;
		background: rgba(255, 255, 255, 0.8);
		border: 1rpx solid rgba(0, 123, 255, 0.1);
		transition: all 0.3s ease;
		
		&:active {
			background: rgba(0, 123, 255, 0.1);
			border-color: rgba(0, 123, 255, 0.3);
		}
	}
	
	.edit-input {
		width: 100%;
		padding: 12rpx 16rpx;
		border: 2rpx solid #007bff;
		border-radius: 12rpx;
		font-size: 28rpx;
		background: white;
		transition: all 0.3s ease;
		
		&:focus {
			border-color: #0056b3;
			box-shadow: 0 0 0 3rpx rgba(0, 123, 255, 0.1);
		}
	}
	
	.edit-textarea {
		width: 100%;
		padding: 12rpx 16rpx;
		border: 2rpx solid #007bff;
		border-radius: 12rpx;
		font-size: 28rpx;
		background: white;
		min-height: 80rpx;
		transition: all 0.3s ease;
		
		&:focus {
			border-color: #0056b3;
			box-shadow: 0 0 0 3rpx rgba(0, 123, 255, 0.1);
		}
	}
	
	.edit-picker {
		width: 100%;
		
		.picker-text {
			padding: 12rpx 16rpx;
			border: 2rpx solid #007bff;
			border-radius: 12rpx;
			font-size: 28rpx;
			background: white;
			color: #333;
			transition: all 0.3s ease;
			
			&:active {
				background: rgba(0, 123, 255, 0.1);
			}
		}
	}
}

.account-section {
	margin-bottom: 40rpx;
	
	.section-title {
		display: flex;
		align-items: center;
		margin-bottom: 25rpx;
		padding-bottom: 15rpx;
		border-bottom: 2rpx solid #e9ecef;
		
		.title-icon {
			font-size: 32rpx;
			margin-right: 10rpx;
			color: #007bff;
		}
		
		.title-text {
			font-size: 30rpx;
			font-weight: bold;
			color: #333;
		}
	}
}

.account-grid {
	display: grid;
	grid-template-columns: repeat(auto-fit, minmax(300rpx, 1fr));
	gap: 20rpx;
	padding: 10rpx 0;
}

.account-item {
	display: flex;
	align-items: center;
	padding: 20rpx;
	background: rgba(248, 249, 250, 0.8);
	border-radius: 16rpx;
	border: 1rpx solid rgba(0, 123, 255, 0.1);
	transition: all 0.3s ease;
	
	&:hover {
		background: rgba(248, 249, 250, 1);
		border-color: rgba(0, 123, 255, 0.2);
		transform: translateY(-2rpx);
		box-shadow: 0 4rpx 16rpx rgba(0, 123, 255, 0.1);
	}
	
	.account-label {
		font-size: 26rpx;
		color: #666;
		margin-right: 15rpx;
		font-weight: 500;
	}
	
	.account-value {
		font-size: 26rpx;
		color: #333;
		font-weight: 500;
	}
}

.action-buttons {
	display: flex;
	gap: 20rpx;
	margin-top: 30rpx;
	
	.btn {
		flex: 1;
		display: flex;
		align-items: center;
		justify-content: center;
		padding: 20rpx 30rpx;
		border-radius: 16rpx;
		font-size: 28rpx;
		font-weight: 500;
		border: none;
		transition: all 0.3s ease;
		position: relative;
		overflow: hidden;
		
		&::before {
			content: '';
			position: absolute;
			top: 0;
			left: -100%;
			width: 100%;
			height: 100%;
			background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.2), transparent);
			transition: left 0.5s ease;
		}
		
		&:active::before {
			left: 100%;
		}
		
		&.btn-primary {
			background: linear-gradient(135deg, #007bff, #0056b3);
			color: white;
			box-shadow: 0 4rpx 16rpx rgba(0, 123, 255, 0.3);
			
			&:active {
				transform: translateY(2rpx);
				box-shadow: 0 2rpx 8rpx rgba(0, 123, 255, 0.4);
			}
		}
		
		&.btn-secondary {
			background: linear-gradient(135deg, #6c757d, #545b62);
			color: white;
			box-shadow: 0 4rpx 16rpx rgba(108, 117, 125, 0.3);
			
			&:active {
				transform: translateY(2rpx);
				box-shadow: 0 2rpx 8rpx rgba(108, 117, 125, 0.4);
			}
		}
		
		.btn-icon {
			font-size: 30rpx;
			margin-right: 8rpx;
		}
		
		.btn-text {
			font-size: 28rpx;
		}
	}
}

.loading-mask {
	position: fixed;
	top: 0;
	left: 0;
	right: 0;
	bottom: 0;
	background: rgba(0, 0, 0, 0.5);
	display: flex;
	align-items: center;
	justify-content: center;
	z-index: 9999;
	backdrop-filter: blur(10rpx);
	
	.loading-content {
		background: rgba(255, 255, 255, 0.95);
		padding: 40rpx;
		border-radius: 20rpx;
		text-align: center;
		box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.2);
		backdrop-filter: blur(10rpx);
		
		.loading-spinner {
			border: 4rpx solid #f3f3f3;
			border-top: 4rpx solid #3498db;
			border-radius: 50%;
			width: 40rpx;
			height: 40rpx;
			animation: spin 1s linear infinite;
			margin: 0 auto 20rpx;
		}
		
		.loading-text {
			font-size: 16rpx;
			color: #333;
		}
	}
}

@keyframes shimmer {
	0%, 100% {
		transform: translateX(-100%);
	}
	50% {
		transform: translateX(100%);
	}
}

@keyframes float {
	0%, 100% {
		transform: translateY(0px) rotate(0deg);
	}
	50% {
		transform: translateY(-20px) rotate(180deg);
	}
}

@keyframes pulse {
	0%, 100% {
		opacity: 0.5;
		transform: scale(1);
	}
	50% {
		opacity: 0.8;
		transform: scale(1.1);
	}
}

@keyframes flow {
	0% {
		transform: rotate(0deg);
	}
	100% {
		transform: rotate(360deg);
	}
}

@keyframes gentleFlow {
	0%, 100% {
		opacity: 0.3;
		transform: scale(1);
	}
	50% {
		opacity: 0.6;
		transform: scale(1.05);
	}
}

@keyframes fadeInDown {
	from {
		opacity: 0;
		transform: translateY(-30rpx);
	}
	to {
		opacity: 1;
		transform: translateY(0);
	}
}

@keyframes fadeInUp {
	from {
		opacity: 0;
		transform: translateY(30rpx);
	}
	to {
		opacity: 1;
		transform: translateY(0);
	}
}

@keyframes bounce {
	0%, 20%, 50%, 80%, 100% {
		transform: translateY(0);
	}
	40% {
		transform: translateY(-10rpx);
	}
	60% {
		transform: translateY(-5rpx);
	}
}

@keyframes spin {
	0% { transform: rotate(0deg); }
	100% { transform: rotate(360deg); }
}
</style> 