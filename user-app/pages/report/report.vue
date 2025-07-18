<template>
	<view class="content">
		<!-- 报告列表 -->
		<view class="report-list" v-if="reportList.length > 0">
			<view class="report-item beautify-report-item" v-for="(item, index) in reportList" :key="index">
				<view class="green-header">
					<view class="green-header-content">
						<text class="package-name">{{item.packageName}}</text>
						<text class="report-date">{{item.reportDate}}</text>
					</view>
				</view>
				<view class="light-green-header">
					<view class="light-green-header-content">
						<view class="info-row">
							<text class="info-label">体检医院</text>
							<text class="info-value">{{item.hospitalName}}</text>
						</view>
						<view class="info-row">
							<text class="info-label">体检人</text>
							<text class="info-value">{{item.personName}}</text>
						</view>
						<view class="info-row">
							<text class="info-label">体检时间</text>
							<text class="info-value">{{item.examDate}}</text>
						</view>
						<view class="report-actions" style="position: relative;right: 0;">
							<view>
								<button class="action-btn primary" @click.stop="viewReport(item)">
									<image src="/static/icon/look3.png" mode="aspectFit" class="btn-icon"></image>
									<text>详情</text>
								</button>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>

		<!-- 空状态 -->
		<view class="empty-state" v-else>
			<image class="empty-image" src="/static/images/empty-report.png" mode="aspectFit"></image>
			<text class="empty-text">暂无体检报告</text>
			<button class="make-appointment-btn" @click="makeAppointment">立即预约体检</button>
		</view>

		<!-- 分页组件 -->
		<view class="pagination" v-if="reportList.length > 0 && totalPages > 1">
			<view class="pagination-controls">
				<view class="page-btn-row">
					<view class="button back" :class="{disabled: currentPage <= 1}" @click="currentPage > 1 && goToPage(currentPage - 1)">
						上一页
					</view>
					<text class="page-info">{{currentPage}} / {{totalPages}}</text>
					<view class="button next" :class="{disabled: currentPage >= totalPages}" @click="currentPage < totalPages && handleNextPage()">
						下一页
					</view>
				</view>
			</view>
		</view>
	</view>
</template>

<script>
	// 导入报告相关的API方法
	import {
		getAppReportPage, // 获取报告分页列表
		getAppointmentDetail // 获取预约详情
	} from '@/api/report';
	// 导入用户相关的API方法
	import {
		getLoginUserInfo // 获取登录用户信息
	} from '@/api/user';

	// 导出报告页面组件配置
	export default {
		// 组件数据
		data() {
			return {
				allReports: [], // 保存所有报告数据
				reportList: [], // 当前页显示的报告列表
				error: null, // 错误信息
				userInfo: null, // 存储用户信息
				// 分页相关数据
				currentPage: 1, // 当前页码
				pageSize: 5, // 每页显示5个报告
				total: 0, // 总报告数
				totalPages: 0, // 总页数
				hasMore: true, // 是否还有更多数据
				inputPage: 1, // 当前页显示
				pendingInputPage: 1 // 用户输入的页码
			}
		},
		// 计算属性
		computed: {
			// 计算可见的页码列表
			visiblePages() {
				const pages = []; // 页码数组
				const maxVisible = 5; // 最多显示5个页码
				// 计算起始页码，确保当前页在中间
				let start = Math.max(1, this.currentPage - Math.floor(maxVisible / 2));
				// 计算结束页码
				let end = Math.min(this.totalPages, start + maxVisible - 1);

				// 调整start，确保显示maxVisible个页码
				if (end - start + 1 < maxVisible) {
					start = Math.max(1, end - maxVisible + 1);
				}

				// 生成页码数组
				for (let i = start; i <= end; i++) {
					pages.push(i);
				}

				return pages; // 返回可见页码数组
			}
		},
		// 页面加载时的生命周期函数
		onLoad() {
			// 先获取用户信息，然后再获取所有报告
			this.getUserInfo().then(() => {
				this.getAllReports(); // 获取用户信息成功后获取报告
			}).catch(error => {
				this.getAllReports(); // 获取用户信息失败也继续获取报告
			});
		},
		// 组件方法
		methods: {
			// 获取用户信息的异步方法
			async getUserInfo() {
				return new Promise(async (resolve, reject) => {
					try {
						// 调用API获取登录用户信息
						const res = await getLoginUserInfo();
						if (res && res.success && res.data) {
							// 获取成功，保存用户信息
							this.userInfo = res.data;
							resolve(this.userInfo); // 解析Promise
						} else {
							// 获取失败，显示错误提示
							uni.showToast({
								title: '获取用户信息失败', // 错误提示
								icon: 'none' // 不显示图标
							});
							reject(new Error('获取用户信息失败')); // 拒绝Promise
						}
					} catch (error) {
						// 捕获异常，显示错误提示
						uni.showToast({
							title: '获取用户信息失败', // 错误提示
							icon: 'none' // 不显示图标
						});
						reject(error); // 拒绝Promise
					}
				});
			},

			/**
			 * 获取所有报告并保存，前端分页的异步方法
			 * 从服务器获取用户的所有体检报告，并处理数据格式
			 */
			async getAllReports() {
				this.error = null; // 清空错误信息
				try {
					// 构建查询参数
					const query = {
						pageNum: 1, // 页码，从第1页开始
						pageSize: 9999, // 一次性获取所有数据（前端分页）
						userId: this.userInfo ?.id // 用户ID，如果userInfo存在则使用其id
					};
					// 调用API获取报告分页数据
					const res = await getAppReportPage(query);
					console.log('API返回数据:', res); // 输出API返回数据用于调试
					
					// 检查API响应是否成功
					if (res && res.success && res.data) {
						// 获取成功，处理数据
						const list = Array.isArray(res.data.list) ? res.data.list : []; // 确保list是数组
						// 使用Promise.all并行处理每个报告项，补充详细信息
						const reportListWithDetails = await Promise.all(list.map(async (item) => {
							let packageName = item.packageName; // 套餐名称
							// 处理性别逻辑：0转换为2（女性），1保持为1（男性），其它保持原值
							let processedGender = '';
							if (item.patientGender !== undefined && item.patientGender !== null) {
								processedGender = item.patientGender === 0 ? '2' : item.patientGender === 1 ? '1' : '';
							} else if (item.gender !== undefined && item.gender !== null) {
								processedGender = item.gender === 0 ? '2' : item.gender === 1 ? '1' : '';
							}
							
							// 如果没有套餐名称但有订单ID，尝试从预约详情获取
							if (!packageName && item.orderId) {
								try {
									// 获取预约详情
									const appointmentRes = await getAppointmentDetail(item.orderId);
									if (appointmentRes && appointmentRes.success && appointmentRes.data) {
										packageName = appointmentRes.data.setmealName || '定制套餐'; // 设置套餐名称
										// 处理预约详情中的性别
										let appointmentGender = '';
										if (appointmentRes.data.patientGender !== undefined && appointmentRes.data.patientGender !== null) {
											appointmentGender = appointmentRes.data.patientGender === 0 ? '2' : appointmentRes.data.patientGender === 1 ? '1' : '';
										}
										
										// 返回完整的报告信息
										return {
											id: item.id, // 报告ID
											orderId: item.orderId || '', // 订单ID
											packageName, // 套餐名称
											reportDate: item.reportDate || item.createTime || '', // 报告日期
											hospitalName: appointmentRes.data.hospitalName || item.hospitalName || '', // 医院名称
											personName: item.personName || (this.userInfo ? this.userInfo.nickname : '') || '未知用户', // 体检人姓名
											examDate: (appointmentRes.data.appointmentDate && appointmentRes.data.timeSlot) ?
												`${appointmentRes.data.appointmentDate.slice(0, 10)} ${appointmentRes.data.timeSlot}` :
												(item.examDate || ''), // 体检日期
											adviceCount: item.adviceCount || 0, // 建议数量
											patientGender: appointmentGender || processedGender, // 患者性别
											patientAge: appointmentRes.data.patientAge || item.patientAge || '' // 患者年龄
										};
									}
								} catch (error) {
									// 获取预约详情失败，继续处理
								}
							}
							// 返回基础报告信息
							return {
								id: item.id, // 报告ID
								orderId: item.orderId || '', // 订单ID
								packageName: packageName || '未命名套餐', // 套餐名称
								reportDate: item.reportDate || item.createTime || '', // 报告日期
								hospitalName: item.hospitalName || '', // 医院名称
								personName: item.personName || (this.userInfo ? this.userInfo.nickname : '') || '未知用户', // 体检人姓名
								examDate: item.examDate || '',
								adviceCount: item.adviceCount || 0,
								patientGender: processedGender,
								patientAge: item.patientAge || ''
							};
						}));
						this.allReports = reportListWithDetails;
						this.total = this.allReports.length;
						this.totalPages = Math.ceil(this.total / this.pageSize);
						this.setReportListByPage(1);
					} else {
						this.allReports = [];
						this.reportList = [];
						this.total = 0;
						this.totalPages = 0;
					}
				} catch (error) {
					this.allReports = [];
					this.reportList = [];
					this.total = 0;
					this.totalPages = 0;
				}
			},

			/**
			 * 根据页码设置当前显示的报告
			 * 实现前端分页功能，从所有报告中截取指定页面的数据
			 * @param {Number} page - 要显示的页码
			 */
			setReportListByPage(page) {
				this.currentPage = page; // 设置当前页码
				const start = (page - 1) * this.pageSize; // 计算开始索引
				const end = start + this.pageSize; // 计算结束索引
				this.reportList = this.allReports.slice(start, end); // 从所有报告中截取当前页的数据
				this.inputPage = page; // 更新输入框显示的页码
				this.pendingInputPage = page; // 保证输入框和当前页同步
				
				// 切换页码后回到顶部（立即显示）
				if (typeof uni !== 'undefined' && uni.pageScrollTo) {
					uni.pageScrollTo({
						scrollTop: 0, // 滚动到顶部
						duration: 0 // 立即滚动，无动画
					});
				}
			},

			/**
			 * 分享报告方法
			 * 用于分享体检报告给其他用户（功能开发中）
			 * @param {Object} report - 要分享的报告对象
			 */
			shareReport(report) {
				uni.showToast({
					title: '分享功能开发中', // 提示信息
					icon: 'none' // 不显示图标
				});
			},
			
			/**
			 * 下载报告方法
			 * 用于下载体检报告文件（目前为模拟下载）
			 * @param {Object} report - 要下载的报告对象
			 */
			downloadReport(report) {
				// 显示加载提示
				uni.showLoading({
					title: '下载中...' // 加载提示文本
				});

				// 模拟下载过程（实际项目中应调用下载API）
				setTimeout(() => {
					uni.hideLoading(); // 隐藏加载提示
					uni.showToast({
						title: '下载成功', // 成功提示
						icon: 'success' // 成功图标
					});
				}, 2000); // 2秒后完成模拟下载
			},
			
			/**
			 * 查看报告详情方法
			 * 跳转到报告详情页面并传递相关参数
			 * @param {Object} report - 要查看的报告对象
			 */
			viewReport(report) {
				// 传递reportId等参数
				const paramsObj = {
					id: report.id, // 这里确保是report.id
					personName: report.personName || '',
					hospitalName: report.hospitalName || '',
					examDate: report.examDate || '',
					patientGender: report.patientGender,
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
			},

			// 分页跳转
			goToPage(page) {
				if (page < 1 || page > this.totalPages) return;
				this.setReportListByPage(page);
				// 跳转后回到顶部（立即显示）
				if (typeof uni !== 'undefined' && uni.pageScrollTo) {
					uni.pageScrollTo({
						scrollTop: 0,
						duration: 0
					});
				}
			},
			jumpToInputPage() {
				let page = Number(this.pendingInputPage);
				if (!page || page < 1) page = 1;
				if (page > this.totalPages) page = this.totalPages;
				if (page !== this.currentPage) {
					this.goToPage(page);
				}
			},
			handleNextPage() {
				let page = Number(this.pendingInputPage);
				if (page && page !== this.currentPage && page >= 1 && page <= this.totalPages) {
					this.goToPage(page);
				} else if (this.currentPage < this.totalPages) {
					this.goToPage(this.currentPage + 1);
				}
			},
			handlePrevPage() {
				if (this.currentPage > 1) {
					this.goToPage(this.currentPage - 1);
				}
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
		box-shadow: 0 6rpx 32rpx rgba(18, 150, 219, 0.08), 0 1rpx 4rpx rgba(0, 0, 0, 0.04);
	border-radius: 18rpx;
	padding: 0;
	margin-bottom: 28rpx;
	overflow: hidden;
}

.green-header {
	background: linear-gradient(135deg, #4CAF50 0%, #66BB6A 100%);
	padding: 24rpx 28rpx;
	border-radius: 18rpx 18rpx 0 0;
	box-shadow: 0 2rpx 8rpx rgba(76, 175, 80, 0.2);
}

.green-header-content {
	display: flex;
	flex-direction: column;
	align-items: center;
	gap: 8rpx;
}

.package-name {
	font-size: 36rpx;
	font-weight: 700;
	color: #ffffff;
	text-align: center;
	letter-spacing: 1rpx;
}

.report-date {
	font-size: 24rpx;
	color: rgba(255, 255, 255, 0.9);
	font-weight: 400;
	text-align: right;
	align-self: flex-end;
	width: 100%;
}

.light-green-header {
	background: linear-gradient(135deg, #E8F5E8 0%, #F0F8F0 100%);
	padding: 24rpx 28rpx;
	border-radius: 0 0 18rpx 18rpx;
	box-shadow: 0 2rpx 8rpx rgba(76, 175, 80, 0.1);
	margin-top: -1rpx;
}

.light-green-header-content {
	display: flex;
	flex-direction: column;
	gap: 16rpx;
}

.light-green-header .info-row {
	display: flex;
	justify-content: space-between;
	align-items: center;
	font-size: 28rpx;
}

.light-green-header .info-label {
	color: #4CAF50;
	font-weight: 600;
	font-size: 26rpx;
}

.light-green-header .info-value {
	color: #2E7D32;
	font-weight: 500;
	font-size: 28rpx;
}

.light-green-header .report-actions {
	display: flex;
	justify-content: flex-end;
	margin-top: 20rpx;
	padding-top: 20rpx;
	border-top: 1px solid rgba(76, 175, 80, 0.2);
}

.light-green-header .action-btn {
	display: flex;
	align-items: center;
	justify-content: center;
	font-size: 24rpx;
	padding: 0 20rpx;
	height: 60rpx;
	line-height: 1;
	border-radius: 30rpx;
		margin-left: 0;
	background-color: rgba(255, 255, 255, 0.8);
	color: #4CAF50;
	border: 1px solid rgba(76, 175, 80, 0.3);
		position: relative;
		right: 0;

	&.primary {
		background-color: #4CAF50;
		color: #ffffff;
		border: 1px solid #4CAF50;
	}

	&::after {
		border: none;
	}
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

	.pagination {
		display: flex;
		flex-direction: column;
		align-items: center;
		margin: 32rpx 0 0 0;

		.pagination-btn-row {
			display: flex;
			align-items: center;
			margin-bottom: 16rpx;
		}

		.pagination-info {
			font-size: 26rpx;
			color: #888;
			margin-bottom: 12rpx;
		}

		.pagination-controls {
			display: flex;
			flex-direction: column;
			align-items: center;
			margin: 32rpx 0 0 0;

			.page-btn-row {
				display: flex;
				align-items: center;
				justify-content: center;
				margin-bottom: 18rpx;

				.button {
					margin: 0 10rpx;
				}

				.page-info {
					font-size: 30rpx;
					color: #1296db;
					font-weight: 700;
					margin: 0 16rpx;
					min-width: 80rpx;
					text-align: center;
					letter-spacing: 2rpx;
				}
			}

			.page-jump-row {
				display: flex;
				align-items: center;
				justify-content: center;
				margin-top: 0;
				margin-bottom: 0;

				.page-input {
					width: 80rpx;
					height: 48rpx;
					border: 1px solid #e3eaf2;
					border-radius: 12rpx;
					margin-right: 12rpx;
					text-align: center;
					font-size: 28rpx;
					color: #1296db;
					background: #f3f8fd;
					box-sizing: border-box;
				}

				.button.jump {
					margin-left: 0;
					min-width: 70rpx;
				}
			}
		}
	}

	.button {
		display: inline-block;
		position: relative;
		color: #888;
		text-shadow: 0 1px 0 rgba(255, 255, 255, 0.8);
		text-decoration: none;
		text-align: center;
		padding: 8px 12px;
		font-size: 12px;
		font-weight: 700;
		font-family: helvetica, arial, sans-serif;
		border-radius: 4px;
		border: 1px solid #bcbcbc;
		-webkit-box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12);
		box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12);
		background-image: -webkit-linear-gradient(top, rgba(255, 255, 255, 1) 0%, rgba(239, 239, 239, 1) 60%, rgba(225, 223, 226, 1) 100%);
		background-image: -moz-linear-gradient(top, rgba(255, 255, 255, 1) 0%, rgba(239, 239, 239, 1) 60%, rgba(225, 223, 226, 1) 100%);
		background-image: -o-linear-gradient(top, rgba(255, 255, 255, 1) 0%, rgba(239, 239, 239, 1) 60%, rgba(225, 223, 226, 1) 100%);
		background-image: -ms-linear-gradient(top, rgba(255, 255, 255, 1) 0%, rgba(239, 239, 239, 1) 60%, rgba(225, 223, 226, 1) 100%);
		background-image: linear-gradient(top, rgba(255, 255, 255, 1) 0%, rgba(239, 239, 239, 1) 60%, rgba(225, 223, 226, 1) 100%);
		margin: 0 10rpx;
		min-width: 70rpx;
		height: 48rpx;
		line-height: 1.5;
		cursor: pointer;
		transition: background 0.2s, color 0.2s;

		&:disabled {
			background: #f0f3fa;
			color: #bbb;
			border: 1px solid #e3eaf2;
			cursor: not-allowed;
		}
	}

	.button.back,
	.button.next {
		background: #f5f7fa;
		color: #1296db;
		font-size: 15px;
		border: 1px solid #bcdffb;
		min-width: 70rpx;
		height: 48rpx;
		line-height: 48rpx;
		font-weight: 400;
		box-shadow: 0 1px 3px rgba(0, 0, 0, 0.08);
		text-shadow: none;
		transition: background 0.2s, color 0.2s;
	}

	.button.back:active,
	.button.next:active {
		background: #e3f2fd;
	}

	.button.back {
		border-left: none;
	}

	.button.back:after {
		content: '';
		position: absolute;
		height: 50%;
		width: 15px;
		border-left: 1px solid #bcbcbc;
		background-image: -webkit-linear-gradient(top, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 1) 1%, rgba(240, 240, 240, 1) 100%);
		background-image: -moz-linear-gradient(top, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 1) 1%, rgba(240, 240, 240, 1) 100%);
		background-image: -o-linear-gradient(top, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 1) 1%, rgba(240, 240, 240, 1) 100%);
		background-image: -ms-linear-gradient(top, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 1) 1%, rgba(240, 240, 240, 1) 100%);
		background-image: linear-gradient(top, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 1) 1%, rgba(240, 240, 240, 1) 100%);
		left: -5px;
		top: 1px;
		-webkit-transform: skew(-35deg, 0);
		-moz-transform: skew(-35deg, 0);
		-o-transform: skew(-35deg, 0);
		-ms-transform: skew(-35deg, 0);
		transform: skew(-35deg, 0);
	}

	.button.back:before {
		content: '';
		position: absolute;
		height: 48%;
		width: 15px;
		border-left: 1px solid #bcbcbc;
		bottom: 1px;
		left: -5px;
		-webkit-transform: skew(35deg, 0);
		-moz-transform: skew(35deg, 0);
		-o-transform: skew(35deg, 0);
		-ms-transform: skew(35deg, 0);
		transform: skew(35deg, 0);
		background-image: -webkit-linear-gradient(top, rgba(240, 240, 240, 1) 0%, rgba(239, 239, 239, 1) 10%, rgba(225, 223, 226, 1) 100%);
		background-image: -moz-linear-gradient(top, rgba(240, 240, 240, 1) 0%, rgba(239, 239, 239, 1) 10%, rgba(225, 223, 226, 1) 100%);
		background-image: -o-linear-gradient(top, rgba(240, 240, 240, 1) 0%, rgba(239, 239, 239, 1) 10%, rgba(225, 223, 226, 1) 100%);
		background-image: -ms-linear-gradient(top, rgba(240, 240, 240, 1) 0%, rgba(239, 239, 239, 1) 10%, rgba(225, 223, 226, 1) 100%);
		background-image: linear-gradient(top, rgba(240, 240, 240, 1) 0%, rgba(239, 239, 239, 1) 10%, rgba(225, 223, 226, 1) 100%);
		-webkit-box-shadow: -2px 1px 2px rgba(100, 100, 100, 0.1);
		box-shadow: -2px 1px 2px rgba(100, 100, 100, 0.1);
	}

	.button.next {
		border-right: none;
	}

	.button.next:after {
		content: '';
		position: absolute;
		height: 48%;
		width: 15px;
		border-right: 1px solid #bcbcbc;
		background-image: -webkit-linear-gradient(top, rgba(240, 240, 240, 1) 0%, rgba(239, 239, 239, 1) 10%, rgba(225, 223, 226, 1) 100%);
		background-image: -moz-linear-gradient(top, rgba(240, 240, 240, 1) 0%, rgba(239, 239, 239, 1) 10%, rgba(225, 223, 226, 1) 100%);
		background-image: -o-linear-gradient(top, rgba(240, 240, 240, 1) 0%, rgba(239, 239, 239, 1) 10%, rgba(225, 223, 226, 1) 100%);
		background-image: -ms-linear-gradient(top, rgba(240, 240, 240, 1) 0%, rgba(239, 239, 239, 1) 10%, rgba(225, 223, 226, 1) 100%);
		background-image: linear-gradient(top, rgba(240, 240, 240, 1) 0%, rgba(239, 239, 239, 1) 10%, rgba(225, 223, 226, 1) 100%);
		right: -5px;
		bottom: 1px;
		-webkit-transform: skew(-35deg, 0);
		-moz-transform: skew(-35deg, 0);
		-o-transform: skew(-35deg, 0);
		-ms-transform: skew(-35deg, 0);
		transform: skew(-35deg, 0);
		-webkit-box-shadow: 2px 1px 2px rgba(100, 100, 100, 0.1);
		box-shadow: 2px 1px 2px rgba(100, 100, 100, 0.1);
	}

	.button.next:before {
		content: '';
		position: absolute;
		background-image: -webkit-linear-gradient(top, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 1) 1%, rgba(240, 240, 240, 1) 100%);
		background-image: -moz-linear-gradient(top, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 1) 1%, rgba(240, 240, 240, 1) 100%);
		background-image: -o-linear-gradient(top, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 1) 1%, rgba(240, 240, 240, 1) 100%);
		background-image: -ms-linear-gradient(top, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 1) 1%, rgba(240, 240, 240, 1) 100%);
		background-image: linear-gradient(top, rgba(255, 255, 255, 0) 0%, rgba(255, 255, 255, 1) 1%, rgba(240, 240, 240, 1) 100%);
		height: 50%;
		width: 15px;
		border-right: 1px solid #bcbcbc;
		top: 1px;
		right: -5px;
		-webkit-transform: skew(35deg, 0);
		-moz-transform: skew(35deg, 0);
		-o-transform: skew(35deg, 0);
		-ms-transform: skew(35deg, 0);
		transform: skew(35deg, 0);
	}

	.button.jump {
		margin-left: 0;
		background: #1296db;
		color: #fff;
		border-radius: 12rpx;
		font-size: 15px;
		font-weight: 400;
		padding: 0 20rpx;
		height: 48rpx;
		line-height: 48rpx;
		border: none;
		box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12);
		text-align: center;
		text-shadow: none !important;

		&:active {
			background: #0d7fc1;
		}
	}

	.button.disabled {
		pointer-events: none;
		opacity: 0.5;
		filter: grayscale(60%);
	}

	.result-conclusion {
		margin-top: 18rpx;
		background: #f3f8fd;
		border-radius: 10rpx;
		padding: 18rpx;
		font-size: 28rpx;
		box-shadow: 0 1rpx 4rpx rgba(18, 150, 219, 0.04);
		.detail-label {
			color: #1296db;
			font-weight: 700;
			margin-bottom: 8rpx;
			display: block;
		}
		.conclusion-content {
			color: #333;
			line-height: 1.8;
			font-size: 30rpx;
			padding-left: 10rpx;
			.conclusion-text {
				margin-bottom: 8rpx;
				white-space: pre-line;
			}
			.conclusion-list {
				.conclusion-item {
					margin-bottom: 10rpx;
					padding-left: 8rpx;
					.conclusion-category {
						color: #4caf50;
						font-weight: 600;
						margin-right: 8rpx;
					}
					.conclusion-name {
						color: #1296db;
						font-weight: 600;
						margin-right: 8rpx;
					}
					.conclusion-desc {
						color: #666;
					}
				}
			}
		}
	}
</style> 
