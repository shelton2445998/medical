<template>
	<view class="content">
		<!-- 报告列表 -->
		<view class="report-list" v-if="reportList.length > 0">
			<view class="report-item beautify-report-item" v-for="(item, index) in reportList" :key="index">
				<view class="report-header beautify-header">
					<view class="title-center-group">
						<!-- <text class="package-icon">📄</text> -->
						<text class="report-title">{{item.packageName}}</text>
					</view>
					<text class="report-date">{{item.reportDate}}</text>
				</view>
				<view class="report-info beautify-info-list">
					<view class="info-row beautify-info-item">
						<text class="info-label">体检医院</text>
						<text class="info-value">{{item.hospitalName}}</text>
					</view>
					<view class="info-row beautify-info-item">
						<text class="info-label">体检人</text>
						<text class="info-value">{{item.personName}}</text>
					</view>
					<view class="info-row beautify-info-item">
						<text class="info-label">体检时间</text>
						<text class="info-value">{{item.examDate}}</text>
					</view>
				</view>
				<view class="report-summary beautify-summary">
					<view class="summary-item" :class="{'abnormal': item.abnormalCount > 0}">
						<text class="summary-value">{{item.abnormalCount}}</text>
						<text class="summary-label">异常指标</text>
					</view>
					<view class="summary-item">
						<text class="summary-value">{{item.totalCount}}</text>
						<text class="summary-label">检查项目</text>
					</view>
					<view class="summary-item">
						<text class="summary-value">{{item.finishedCount}}</text>
						<text class="summary-label">完成项目</text>
					</view>
				</view>
				<view class="report-actions beautify-actions">
					<button class="action-btn" @click.stop="shareReport(item)">
					  <image src="/static/icon/share.png" mode="aspectFit" class="btn-icon"></image>
					  <text>分享</text>
					</button>
					<button class="action-btn" @click.stop="downloadReport(item)">
						<image src="/static/icon/download.png" mode="aspectFit" class="btn-icon"></image>
						<text>下载</text>
					</button>
					<button class="action-btn primary" @click.stop="viewReport(item)">
						<image src="/static/icon/look3.png" mode="aspectFit" class="btn-icon"></image>
						<text>详情</text>
					</button>
				</view>
			</view>
		</view>
		
		<!-- 空状态 -->
		<view class="empty-state" v-else>
			<image class="empty-image" src="/static/images/empty-report.png" mode="aspectFit"></image>
			<text class="empty-text">暂无体检报告</text>
			<button class="make-appointment-btn" @click="makeAppointment">立即预约体检</button>
		</view>
	</view>
</template>

<script>
	import { getAppReportPage, getAppointmentDetail } from '@/api/report';
	import { getLoginUserInfo } from '@/api/user';
	
	export default {
		data() {
			return {
				reportList: [],
				loading: false,
				error: null,
				userInfo: null // 存储用户信息
			}
		},
		onLoad() {
			// 先获取用户信息，然后再获取报告列表
			this.getUserInfo().then(() => {
				this.getReportList();
			}).catch(error => {
				console.error('初始化数据失败:', error);
				// 即使获取用户信息失败，也尝试获取报告列表
				this.getReportList();
			});
		},
		methods: {
			// 获取用户信息
			async getUserInfo() {
				return new Promise(async (resolve, reject) => {
					try {
						const res = await getLoginUserInfo();
						console.log('用户信息:', res);
						
						if (res && res.success && res.data) {
							this.userInfo = res.data;
							console.log('获取到的用户信息:', this.userInfo);
							resolve(this.userInfo);
						} else {
							console.error('获取用户信息失败:', res);
							uni.showToast({
								title: '获取用户信息失败',
								icon: 'none'
							});
							reject(new Error('获取用户信息失败'));
						}
					} catch (error) {
						console.error('获取用户信息出错:', error);
						uni.showToast({
							title: '获取用户信息失败',
							icon: 'none'
						});
						reject(error);
					}
				});
			},
			
			// 获取报告列表
			async getReportList() {
				this.loading = true;
				this.error = null;
				
				try {
					const query = {
						pageNum: 1,
						pageSize: 10,
						userId: this.userInfo?.id
					};
					
					const res = await getAppReportPage(query);
					console.log('API返回数据:', res);
					
					// 根据API实际返回的数据结构处理
					if (res && res.success && res.data && Array.isArray(res.data.list)) {
						// 使用res.data.list作为数据源，并获取预约详情
						const reportListWithDetails = await Promise.all(
							res.data.list.map(async (item) => {
								let packageName = item.packageName;
								
								// 如果没有套餐名称，尝试通过预约详情获取
								if (!packageName && item.orderId) {
									try {
										console.log('正在获取预约详情，orderId:', item.orderId);
										const appointmentRes = await getAppointmentDetail(item.orderId);
										console.log('预约详情返回:', appointmentRes);
										if (appointmentRes && appointmentRes.success && appointmentRes.data) {
											packageName = appointmentRes.data.setmealName || '定制套餐';

											// 使用预约详情中的医院和体检时间
											return {
												id: item.id,
												orderId: item.orderId || '',
												packageName: packageName,
												reportDate: item.reportDate || item.createTime || '',
												hospitalName: appointmentRes.data.hospitalName || item.hospitalName || '',
												personName: item.personName || (this.userInfo ? this.userInfo.nickname : '') || '未知用户',
												examDate: (appointmentRes.data.appointmentDate && appointmentRes.data.timeSlot)
												  ? `${appointmentRes.data.appointmentDate.slice(0, 10)} ${appointmentRes.data.timeSlot}`
												  : (item.examDate || ''),
												abnormalCount: item.abnormalCount || 0,
												totalCount: (item.checkitemIds ? item.checkitemIds.split(',').filter(id => id.trim() !== '').length : 0),
												adviceCount: item.adviceCount || 0,
												patientGender: appointmentRes.data.patientGender || '',
												patientAge: appointmentRes.data.patientAge || '',
												finishedCount: 0 // 新增统计 finishedCount
											};
										} else {
											console.warn('预约详情返回数据格式异常:', appointmentRes);
											packageName = '定制套餐';
										}
									} catch (error) {
										console.error('获取预约详情失败，orderId:', item.orderId, '错误:', error);
										// 如果是业务异常（如订单不存在），直接跳过，不再获取
										if (error.message && error.message.includes('预约订单不存在')) {
											console.warn('订单不存在，跳过套餐名称获取');
											// 不设置packageName，保留原有值
											return {
												id: item.id,
												orderId: item.orderId || '',
												packageName: '定制套餐',
												reportDate: item.reportDate || item.createTime || '',
												hospitalName: item.hospitalName || '',
												personName: item.personName || (this.userInfo ? this.userInfo.nickname : '') || '未知用户',
												examDate: item.examDate || '',
												abnormalCount: item.abnormalCount || 0,
												totalCount: (item.checkitemIds ? item.checkitemIds.split(',').filter(id => id.trim() !== '').length : 0),
												adviceCount: item.adviceCount || 0,
												patientGender: '',
												patientAge: '',
												finishedCount: 0 // 新增统计 finishedCount
											};
										}
										packageName = '未命名套餐';
									}
								} else if (!packageName) {
									packageName = '未命名套餐';
								}
								
								// 在 getReportList 处理每个报告时，异步获取每个报告的明细并统计 conclusion
								let finishedCount = 0;
								// 需要调用接口获取每个报告的所有项目明细
								try {
									// 假设有 getAppReportItemList(reportId) 返回该报告所有项目明细
									if (item.id) {
										const detailRes = await this.$api?.getAppReportItemList?.(item.id);
										if (detailRes && detailRes.success && Array.isArray(detailRes.data)) {
											finishedCount = detailRes.data.filter(
												d => d.conclusion && d.conclusion !== '体检结果正在生成'
											).length;
										}
									}
								} catch (e) {
									finishedCount = 0;
								}
								
								return {
									id: item.id,
									orderId: item.orderId || '',
									packageName: packageName,
									reportDate: item.reportDate || item.createTime || '',
									hospitalName: item.hospitalName || '',
									personName: item.personName || (this.userInfo ? this.userInfo.nickname : '') || '未知用户',
									examDate: item.examDate || '',
									abnormalCount: item.abnormalCount || 0,
									totalCount: (item.checkitemIds ? item.checkitemIds.split(',').filter(id => id.trim() !== '').length : 0),
									adviceCount: item.adviceCount || 0,
									patientGender: '',
									patientAge: '',
									finishedCount: finishedCount // 新增统计 finishedCount
								};
							})
						);
						
						this.reportList = reportListWithDetails;
					} else {
						// 如果返回的数据结构不符合预期
						this.reportList = [];
						console.error('API返回的数据结构不符合预期:', res);
					}
				} catch (error) {
					console.error('获取报告列表失败:', error);
					this.error = error;
					uni.showToast({
						title: '获取报告列表失败',
						icon: 'none'
					});
				} finally {
					this.loading = false;
				}
			},
			
			// 分享报告
			shareReport(report) {
				uni.showToast({
					title: '分享功能开发中',
					icon: 'none'
				});
			},
			// 下载报告
			downloadReport(report) {
				uni.showLoading({
					title: '下载中...'
				});
				
				// 模拟下载
				setTimeout(() => {
					uni.hideLoading();
					uni.showToast({
						title: '下载成功',
						icon: 'success'
					});
				}, 2000);
			},
			// 查看报告详情
			viewReport(report) {
				// 传递reportId等参数
				const paramsObj = {
					id: report.id, // 这里确保是report.id
					personName: report.personName || '',
					hospitalName: report.hospitalName || '',
					examDate: report.examDate || '',
					patientGender: report.patientGender !== undefined ? report.patientGender : (report.gender !== undefined ? report.gender : ''),
					patientAge: report.patientAge !== undefined ? report.patientAge : (report.age !== undefined ? report.age : '')
				};
				console.log('即将跳转到report-detail，传递的参数：', paramsObj);
				const params = Object.keys(paramsObj)
					.map(key => `${key}=${encodeURIComponent(paramsObj[key])}`)
					.join('&');
				uni.navigateTo({
					url: `/pages/report-detail/report-detail?${params}`
				});
			},
			// 立即预约
			makeAppointment() {
				uni.switchTab({
					url: '/pages/appointment/appointment'
				});
			}
		}
	}
</script>

<style lang="scss">
@font-face {
	font-family: texticons;
	font-weight: normal;
	font-style: normal;
	src: url('https://at.alicdn.com/t/font_984210_5cs13ndgqsn.ttf') format('truetype');
}

.btn-icon {
  width: 40rpx;
  height: 40rpx;
  margin-right: 10rpx;
  vertical-align: middle;
}

.content {
	background-color: #f5f5f5;
	min-height: 100vh;
}

.report-list {
	padding: 20rpx;
	
	.report-item {
		background-color: #ffffff;
		border-radius: 10rpx;
		margin-bottom: 20rpx;
		padding: 30rpx;
		box-shadow: 0 2rpx 10rpx rgba(0, 0, 0, 0.05);
		
		.report-header {
			display: flex;
			justify-content: space-between;
			align-items: center;
			margin-bottom: 20rpx;
			
			.report-title {
				font-size: 32rpx;
				font-weight: bold;
				color: #333333;
			}
			
			.report-date {
				font-size: 24rpx;
				color: #999999;
			}
		}
		
		.report-info {
			margin-bottom: 20rpx;
			
			.info-row {
				display: flex;
				margin-bottom: 10rpx;
				font-size: 28rpx;
				
				.info-label {
					width: 160rpx;
					color: #666666;
				}
				
				.info-value {
					flex: 1;
					color: #333333;
				}
			}
		}
		
		.report-summary {
			display: flex;
			background-color: #f9f9f9;
			border-radius: 10rpx;
			padding: 20rpx 0;
			margin-bottom: 20rpx;
			
			.summary-item {
				flex: 1;
				text-align: center;
				position: relative;
				
				&:not(:last-child)::after {
					content: '';
					position: absolute;
					right: 0;
					top: 20%;
					height: 60%;
					width: 1px;
					background-color: #eeeeee;
				}
				
				&.abnormal {
					.summary-value {
						color: #ff5a5f;
					}
				}
				
				.summary-value {
					font-size: 36rpx;
					font-weight: bold;
					color: #333333;
					display: block;
					margin-bottom: 5rpx;
				}
				
				.summary-label {
					font-size: 24rpx;
					color: #999999;
				}
			}
		}
		
		.report-actions {
			display: flex;
			justify-content: flex-end;
			
			.action-btn {
				display: flex;
				align-items: center;
				justify-content: center;
				font-size: 24rpx;
				padding: 0 20rpx;
				height: 60rpx;
				line-height: 1;
				border-radius: 30rpx;
				margin-left: 20rpx;
				background-color: #f5f5f5;
				color: #666666;
				
				.iconfont {
					font-size: 28rpx;
					margin-right: 6rpx;
					font-family: texticons;
				}
				
				&.primary {
					background-color: #1296db;
					color: #ffffff;
				}
				
				&::after {
					border: none;
				}
			}
		}
	}
}

.empty-state {
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
		color: #999999;
		margin-bottom: 40rpx;
	}
	
	.make-appointment-btn {
		background-color: #1296db;
		color: #ffffff;
		font-size: 28rpx;
		padding: 0 40rpx;
		height: 80rpx;
		line-height: 80rpx;
		border-radius: 40rpx;
		
		&::after {
			border: none;
		}
	}
}

.beautify-report-item {
	box-shadow: 0 6rpx 32rpx rgba(18, 150, 219, 0.08), 0 1rpx 4rpx rgba(0,0,0,0.04);
	border-radius: 18rpx;
	padding: 36rpx 28rpx;
	margin-bottom: 28rpx;
}
.beautify-header {
	display: flex;
	flex-direction: column;
	align-items: center;
	margin-bottom: 18rpx;
	.title-center-group {
		display: flex;
		align-items: center;
		justify-content: center;
	}
	.package-icon {
		display: none;
	}
	.report-title {
		font-size: 52rpx;
		font-weight: 700;
		color: #1296db;
		letter-spacing: 1rpx;
	}
	.report-date {
		font-size: 26rpx;
		color: #999;
		font-weight: 400;
		margin-top: 4rpx;
		text-align: right;
		align-self: flex-end;
		width: 100%;
	}
}
.beautify-info-list {
	display: flex;
	flex-direction: column;
	gap: 0;
	padding: 10rpx 0 0 0;
	.beautify-info-item {
		padding: 18rpx 0 12rpx 0;
		border-bottom: 1px solid #f0f3fa;
		display: flex;
		flex-direction: column;
		justify-content: flex-start;
		.info-label {
			font-size: 24rpx;
			color: #999;
			font-weight: 400;
			margin-bottom: 4rpx;
		}
		.info-value {
			font-size: 30rpx;
			color: #222;
			font-weight: 600;
			word-break: break-all;
		}
	}
	.beautify-info-item:last-child {
		border-bottom: none;
	}
}
.beautify-summary {
	display: flex;
	background: #f3f8fd;
	border-radius: 12rpx;
	padding: 24rpx 0;
	box-shadow: 0 2rpx 8rpx rgba(18, 150, 219, 0.04);
	margin-bottom: 18rpx;
	.summary-item {
		flex: 1;
		text-align: center;
		position: relative;
		&:not(:last-child)::after {
			content: '';
			position: absolute;
			right: 0;
			top: 20%;
			height: 60%;
			width: 1px;
			background: #e3eaf2;
		}
		&.abnormal .summary-value {
			color: #ff5a5f;
		}
		.summary-value {
			font-size: 40rpx;
			font-weight: 700;
			display: block;
			margin-bottom: 6rpx;
		}
		.summary-label {
			font-size: 26rpx;
			color: #888;
		}
	}
}
.beautify-actions {
	display: flex;
	justify-content: flex-end;
	.action-btn {
		display: flex;
		align-items: center;
		justify-content: center;
		font-size: 28rpx;
		padding: 0 24rpx;
		height: 72rpx;
		line-height: 1;
		border-radius: 36rpx;
		margin-left: 18rpx;
		background: #f3f8fd;
		color: #1296db;
		font-weight: 700;
		box-shadow: 0 1rpx 4rpx rgba(18, 150, 219, 0.04);
		transition: background 0.2s, color 0.2s;
		.iconfont {
			font-size: 32rpx;
			margin-right: 8rpx;
			font-family: texticons;
		}
		&.primary {
			background: linear-gradient(90deg, #1296db 0%, #6ec6ff 100%);
			color: #fff;
		}
		&:active {
			background: #e0e7ff;
		}
		&::after {
			border: none;
		}
	}
}
</style> 