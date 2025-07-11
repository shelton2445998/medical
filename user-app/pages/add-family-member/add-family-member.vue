<template>
	<view class="content">
		<!-- 页面标题 -->
		<view class="page-header">
			<text class="page-title">添加家庭成员</text>
		</view>
		
		<!-- 表单内容 -->
		<view class="form-container">
			<!-- 头像上传 -->
			<view class="avatar-section">
				<view class="avatar-upload" @click="chooseAvatar">
					<image v-if="formData.avatar" :src="formData.avatar" mode="aspectFill" class="avatar-preview"></image>
					<view v-else class="avatar-placeholder">
						<image src="/static/images/icons/icon-camera.png" mode="aspectFit" class="camera-icon"></image>
						<text class="upload-text">点击上传头像</text>
					</view>
				</view>
			</view>
			
			<!-- 基本信息 -->
			<view class="form-section">
				<view class="section-title">基本信息</view>
				<view class="form-item">
					<text class="label">姓名</text>
					<input class="input" v-model="formData.name" placeholder="请输入姓名" />
				</view>
				<view class="form-item">
					<text class="label">性别</text>
					<picker class="picker" :value="genderIndex" :range="genderOptions" @change="onGenderChange">
						<view class="picker-text">{{genderOptions[genderIndex]}}</view>
					</picker>
				</view>
				<view class="form-item">
					<text class="label">年龄</text>
					<input class="input" v-model="formData.age" type="number" placeholder="请输入年龄" />
				</view>
				<view class="form-item">
					<text class="label">关系</text>
					<picker class="picker" :value="relationIndex" :range="relationOptions" @change="onRelationChange">
						<view class="picker-text">{{relationOptions[relationIndex]}}</view>
					</picker>
				</view>
				<view class="form-item">
					<text class="label">手机号</text>
					<input class="input" v-model="formData.phone" type="number" placeholder="请输入手机号" />
				</view>
			</view>
			
			<!-- 健康信息 -->
			<view class="form-section">
				<view class="section-title">健康信息</view>
				<view class="form-item">
					<text class="label">身高(cm)</text>
					<input class="input" v-model="formData.height" type="number" placeholder="请输入身高" />
				</view>
				<view class="form-item">
					<text class="label">体重(kg)</text>
					<input class="input" v-model="formData.weight" type="number" placeholder="请输入体重" />
				</view>
				<view class="form-item">
					<text class="label">血型</text>
					<picker class="picker" :value="bloodTypeIndex" :range="bloodTypeOptions" @change="onBloodTypeChange">
						<view class="picker-text">{{bloodTypeOptions[bloodTypeIndex]}}</view>
					</picker>
				</view>
				<view class="form-item">
					<text class="label">过敏史</text>
					<textarea class="textarea" v-model="formData.allergies" placeholder="请输入过敏史（如无请填写无）" />
				</view>
			</view>
			
			<!-- 紧急联系人 -->
			<view class="form-section">
				<view class="section-title">紧急联系人</view>
				<view class="form-item">
					<text class="label">联系人姓名</text>
					<input class="input" v-model="formData.emergencyContact.name" placeholder="请输入紧急联系人姓名" />
				</view>
				<view class="form-item">
					<text class="label">联系人电话</text>
					<input class="input" v-model="formData.emergencyContact.phone" type="number" placeholder="请输入紧急联系人电话" />
				</view>
				<view class="form-item">
					<text class="label">与联系人关系</text>
					<input class="input" v-model="formData.emergencyContact.relation" placeholder="请输入与联系人关系" />
				</view>
			</view>
		</view>
		
		<!-- 保存按钮 -->
		<view class="save-section">
			<button class="save-btn" @click="saveMember">保存</button>
		</view>
	</view>
</template>

<script>
	export default {
		data() {
			return {
				formData: {
					name: '',
					gender: '男',
					age: '',
					relation: '父亲',
					phone: '',
					avatar: '',
					height: '',
					weight: '',
					bloodType: 'A型',
					allergies: '',
					emergencyContact: {
						name: '',
						phone: '',
						relation: ''
					}
				},
				genderOptions: ['男', '女'],
				genderIndex: 0,
				relationOptions: ['父亲', '母亲', '儿子', '女儿', '丈夫', '妻子', '祖父', '祖母', '外祖父', '外祖母', '其他'],
				relationIndex: 0,
				bloodTypeOptions: ['A型', 'B型', 'AB型', 'O型', '未知'],
				bloodTypeIndex: 0
			}
		},
		methods: {
			// 选择头像
			chooseAvatar() {
				uni.chooseImage({
					count: 1,
					sizeType: ['compressed'],
					sourceType: ['album', 'camera'],
					success: (res) => {
						this.formData.avatar = res.tempFilePaths[0];
					}
				});
			},
			// 性别选择
			onGenderChange(e) {
				this.genderIndex = e.detail.value;
				this.formData.gender = this.genderOptions[this.genderIndex];
			},
			// 关系选择
			onRelationChange(e) {
				this.relationIndex = e.detail.value;
				this.formData.relation = this.relationOptions[this.relationIndex];
			},
			// 血型选择
			onBloodTypeChange(e) {
				this.bloodTypeIndex = e.detail.value;
				this.formData.bloodType = this.bloodTypeOptions[this.bloodTypeIndex];
			},
			// 保存成员
			saveMember() {
				// 表单验证
				if (!this.formData.name) {
					uni.showToast({
						title: '请输入姓名',
						icon: 'none'
					});
					return;
				}
				
				if (!this.formData.age) {
					uni.showToast({
						title: '请输入年龄',
						icon: 'none'
					});
					return;
				}
				
				if (!this.formData.phone) {
					uni.showToast({
						title: '请输入手机号',
						icon: 'none'
					});
					return;
				}
				
				// 验证手机号格式
				const phoneReg = /^1[3-9]\d{9}$/;
				if (!phoneReg.test(this.formData.phone)) {
					uni.showToast({
						title: '请输入正确的手机号',
						icon: 'none'
					});
					return;
				}
				
				// 保存数据
				const memberData = {
					id: Date.now(), // 临时ID
					...this.formData,
					createTime: new Date().toISOString()
				};
				
				// 这里可以调用API保存数据
				console.log('保存家庭成员数据:', memberData);
				
				uni.showToast({
					title: '保存成功',
					icon: 'success'
				});
				
				// 返回上一页
				setTimeout(() => {
					uni.navigateBack();
				}, 1500);
			}
		}
	}
</script>

<style lang="scss">
.content {
	background-color: #f5f5f5;
	min-height: 100vh;
	padding: 20rpx;
}

.page-header {
	text-align: center;
	padding: 40rpx 0;
	
	.page-title {
		font-size: 36rpx;
		font-weight: bold;
		color: #333333;
	}
}

.form-container {
	background-color: #ffffff;
	border-radius: 20rpx;
	padding: 30rpx;
	margin-bottom: 30rpx;
}

.avatar-section {
	text-align: center;
	margin-bottom: 40rpx;
	
	.avatar-upload {
		width: 150rpx;
		height: 150rpx;
		border-radius: 75rpx;
		border: 2rpx dashed #cccccc;
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		margin: 0 auto;
		
		.avatar-preview {
			width: 100%;
			height: 100%;
			border-radius: 75rpx;
		}
		
		.avatar-placeholder {
			display: flex;
			flex-direction: column;
			align-items: center;
			
			.camera-icon {
				width: 60rpx;
				height: 60rpx;
				margin-bottom: 10rpx;
			}
			
			.upload-text {
				font-size: 24rpx;
				color: #999999;
			}
		}
	}
}

.form-section {
	margin-bottom: 40rpx;
	
	.section-title {
		font-size: 30rpx;
		font-weight: bold;
		color: #333333;
		margin-bottom: 25rpx;
		padding-left: 20rpx;
		border-left: 6rpx solid #1296db;
	}
	
	.form-item {
		display: flex;
		align-items: center;
		padding: 25rpx 0;
		border-bottom: 1px solid #f5f5f5;
		
		&:last-child {
			border-bottom: none;
		}
		
		.label {
			width: 180rpx;
			font-size: 28rpx;
			color: #333333;
		}
		
		.input {
			flex: 1;
			font-size: 28rpx;
			color: #333333;
		}
		
		.picker {
			flex: 1;
			
			.picker-text {
				font-size: 28rpx;
				color: #333333;
			}
		}
		
		.textarea {
			flex: 1;
			font-size: 28rpx;
			color: #333333;
			height: 120rpx;
			line-height: 1.5;
		}
	}
}

.save-section {
	.save-btn {
		width: 100%;
		height: 90rpx;
		background-color: #1296db;
		color: #ffffff;
		border-radius: 45rpx;
		font-size: 30rpx;
		display: flex;
		align-items: center;
		justify-content: center;
		
		&::after {
			border: none;
		}
	}
}
</style> 