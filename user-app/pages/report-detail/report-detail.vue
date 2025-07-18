<!--
@name: 体检报告详情页面
@description: 显示体检报告的详细信息，包括基本信息、检查结果概要、详细检查结果等
@author: 医疗系统开发团队
@created: 2024年医疗系统开发
@features:
- 显示体检报告基本信息（姓名、性别、年龄、医院等）
- 显示检查结果概要（检查项目数量、完成项目数量）
- 显示详细检查结果列表（可展开查看详情）
- 支持报告下载和分享功能
- 支持在线咨询功能
@页面路径: /pages/report-detail/report-detail
@参数说明:
- id: 报告ID，用于获取报告详情
- personName: 体检人姓名
- hospitalName: 体检医院名称
- examDate: 体检时间
- patientGender: 性别（1:男, 2:女, 0:女）
- patientAge: 年龄
-->

<template>
	<view class="content">
		<!-- 
			报告基本信息卡片
			显示体检报告的基础信息，包括套餐名称、报告时间、体检人信息等
		-->
		<view class="report-info-card enhanced-card">
			<!-- 报告标题区域 -->
			<view class="report-title beautify-title">
				<view class="title-center-group">
					<text class="package-icon">📄</text>
					<text class="package-name">{{reportInfo.packageName}}</text>
				</view>
				<text class="report-date">{{reportInfo.reportDate}}</text>
			</view>
			
			<!-- 报告基本信息列表 -->
			<view class="info-list beautify-info-list">
				<view class="info-item beautify-info-item">
					<text class="info-label">体检人</text>
					<text class="info-value main">{{reportInfo.personName}}</text>
				</view>
				<view class="info-item beautify-info-item">
					<text class="info-label">性别</text>
					<text class="info-value">{{reportInfo.gender}}</text>
				</view>
				<view class="info-item beautify-info-item">
					<text class="info-label">年龄</text>
					<text class="info-value">{{reportInfo.age}}岁</text>
				</view>
				<view class="info-item beautify-info-item">
					<text class="info-label">体检医院</text>
					<text class="info-value main">{{reportInfo.hospitalName}}</text>
				</view>
				<view class="info-item beautify-info-item">
					<text class="info-label">体检时间</text>
					<text class="info-value">{{reportInfo.examDate}}</text>
				</view>
				<view class="info-item beautify-info-item">
					<text class="info-label">报告时间</text>
					<text class="info-value">{{reportInfo.reportDate}}</text>
				</view>
			</view>
		</view>

		<!-- 
			体检结果概要卡片
			显示体检结果的统计信息，包括检查项目数量、完成项目数量等
		-->
		<view class="summary-card enhanced-card">
			<view class="card-title">体检结果概要</view>
			<view class="summary-content">
				<!-- 总检查项目数量 -->
				<view class="summary-item">
					<text class="summary-value">{{reportInfo.totalCount}}</text>
					<text class="summary-label">检查项目</text>
				</view>
				<!-- 已完成项目数量 -->
				<view class="summary-item">
					<text class="summary-value highlight-advice">{{reportInfo.finishedCount}}</text>
					<text class="summary-label">完成项目</text>
				</view>
			</view>
		</view>

		<!-- 
			体检结果详细列表
			显示所有检查项目的详细结果，支持展开查看详情
		-->
		<view class="result-card enhanced-card">
			<view class="card-title">体检结果</view>
			
			<!-- 体检项目列表 -->
			<view class="result-list">
				<view class="result-item" v-for="(item, index) in filteredResults" :key="index" 
					:class="{'abnormal': item.isAbnormal, 'expanded': item.expanded}">
					<!-- 检查项目基本信息 -->
					<view class="result-header" @click="toggleExpand(index)">
						<view class="result-name">
							<text>{{item.name}}</text>
						</view>
						<view class="result-value">
							<text class="result-unit">{{item.unit}}</text>
							<text class="expand-icon">{{item.expanded ? '∧' : '∨'}}</text>
						</view>
					</view>
					
					<!-- 检查项目详细信息（可展开显示） -->
					<view class="result-detail animated-expand" v-if="item.expanded">
						<!-- 结果说明 -->
						<view class="result-desc" v-if="item.description">
							<text class="detail-label">结果说明：</text>
							<text class="detail-value">{{item.description}}</text>
						</view>
						
						<!-- 医生结论 -->
						<view class="result-conclusion">
							<text class="detail-label">医生结论：</text>
							<view class="conclusion-content">
								<!-- 单个结论文本 -->
								<view v-if="typeof item.conclusion === 'string'" class="conclusion-text">
									{{'　　' + item.conclusion}}
								</view>
								<!-- 多个结论列表 -->
								<view v-else-if="Array.isArray(item.conclusion)" class="conclusion-list">
									<view v-for="(conclusion, index) in item.conclusion" :key="index" class="conclusion-item">
										<text class="conclusion-category">{{conclusion.category}}</text>
										<text class="conclusion-name">{{conclusion.name}}</text>
										<text class="conclusion-desc">{{'　　' + conclusion.desc}}</text>
									</view>
								</view>
							</view>
						</view>
					</view>
				</view>
			</view>
		</view>

		<!-- 
			底部操作栏
			提供分享报告和在线咨询功能
		-->
		<view class="enhanced-bottom">
			<!-- 分享报告按钮 -->
			<button class="action-btn" @click="generateAndShareReport">
				<text class="btn-icon">📤</text>
				<text>分享报告</text>
			</button>
			<!-- 在线咨询按钮 -->
			<button class="action-btn primary" @click="makeConsultation">
				<text class="btn-icon">💬</text>
				<text>在线咨询</text>
			</button>
		</view>

		<!-- 底部占位，防止内容被底部栏遮挡 -->
		<view style="height: 130rpx;"></view>
	</view>
</template>

<script>
	// 引入API接口
	import { getAppReportById, getAppReportItem, getCheckitemById } from '../../api/report.js';
	
	export default {
		name: 'ReportDetail',
		/**
		 * 组件数据
		 * @returns {Object} 组件数据对象
		 */
		data() {
			return {
				// 报告ID，用于获取报告详情
				reportId: '',
				// 当前筛选条件
				currentFilter: 'all',
				// 筛选选项配置
				filterOptions: [
					{
						label: '全部',
						value: 'all'
					},
					{
						label: '异常',
						value: 'abnormal'
					}
				],
				// 报告基本信息
				reportInfo: {
					packageName: '',    // 套餐名称
					reportDate: '',     // 报告时间
					hospitalName: '',   // 医院名称
					personName: '',     // 体检人姓名
					gender: '',         // 性别
					age: 0,             // 年龄
					examDate: '',       // 体检时间
					abnormalCount: 0,   // 异常项目数量
					totalCount: 0,      // 总检查项目数量
					adviceCount: 0,     // 建议数量
					finishedCount: 0,   // 完成项目数量
					adviceList: []      // 建议列表
				},
				// 检查结果原始数据
				examResults: [],
				// 筛选后的检查结果
				filteredResults: [],
				// 加载状态
				loading: false
			}
		},
		
		/**
		 * 页面加载时的处理
		 * @param {Object} options 页面参数
		 */
		onLoad(options) {
			console.log('report-detail页面收到的参数:', options);
			if (options.id) {
				this.reportId = options.id;
				console.log('进入report-detail页面，orderId:', options.id);
				
				// 接收并赋值传递的参数
				if (options.personName) {
					this.reportInfo.personName = decodeURIComponent(options.personName);
				}
				if (options.hospitalName) {
					this.reportInfo.hospitalName = decodeURIComponent(options.hospitalName);
				}
				if (options.examDate) {
					this.reportInfo.examDate = decodeURIComponent(options.examDate);
				}
				
				// 处理性别参数
				if (options.patientGender) {
					let genderValue = decodeURIComponent(options.patientGender);
					if (genderValue === '1') {
						this.reportInfo.gender = '男';
					} else if (genderValue === '2') {
						this.reportInfo.gender = '女';
					} else if (genderValue === '0') {
						this.reportInfo.gender = '女';
					} else {
						this.reportInfo.gender = genderValue;
					}
				}
				
				// 处理年龄参数
				if (options.patientAge) {
					const ageValue = decodeURIComponent(options.patientAge);
					// 确保年龄是有效数字
					if (ageValue && ageValue !== 'null' && ageValue !== 'undefined' && ageValue !== '') {
						this.reportInfo.age = parseInt(ageValue) || 25;
					} else {
						this.reportInfo.age = 25; // 默认年龄
					}
				} else {
					this.reportInfo.age = 25; // 默认年龄
				}
				
				// 获取报告详情
				this.getReportDetail();
			}

			// 初始化显示所有检查结果
			this.filteredResults = this.examResults;
		},
		
		methods: {
			/**
			 * 获取报告详情
			 * 调用API接口获取报告的详细信息和检查结果
			 */
			async getReportDetail() {
				this.loading = true;
				try {
					// 清空之前的检查结果
					this.examResults = [];
					
					// 调用报告详情API
					const res = await getAppReportById(this.reportId);
					console.log('getAppReportById返回:', res);
					
					if (res && res.success && res.data) {
						// 更新报告基本信息
						this.reportInfo.id = res.data.id;
						this.reportInfo.orderId = res.data.orderId;
						this.reportInfo.userId = res.data.userId;
						this.reportInfo.checkitemIds = res.data.checkitemIds;
						this.reportInfo.reportItemIds = res.data.reportItemIds;
						this.reportInfo.status = res.data.status;
						this.reportInfo.conclusion = res.data.conclusion;
						this.reportInfo.doctorId = res.data.doctorId;
						this.reportInfo.reportDate = res.data.createTime; // 用createTime作为报告时间
						this.reportInfo.createId = res.data.createId;
						this.reportInfo.createTime = res.data.createTime;
						this.reportInfo.updateId = res.data.updateId;
						this.reportInfo.updateTime = res.data.updateTime;
						
						// 设置套餐名称
						this.reportInfo.packageName = res.data.packageName || '体检报告';

						// 遍历报告项目，获取详细信息
						if (res.data.reportItemIds) {
							const ids = res.data.reportItemIds.split(',').map(id => id.trim()).filter(id => id);
							let abnormalCount = 0;
							let finishedCount = 0;
							
							// 遍历每个检查项目
							for (const id of ids) {
								try {
									// 获取检查项目结果
									const itemRes = await getAppReportItem(id);
									console.log('getAppReportItem返回:', itemRes);
									
									if (itemRes && itemRes.success && itemRes.data && itemRes.data.itemId) {
										// 获取检查项目基本信息
										const checkitemRes = await getCheckitemById(itemRes.data.itemId);
										console.log('getCheckitemById返回:', checkitemRes);
										
										if (checkitemRes && checkitemRes.success && checkitemRes.data) {
											// 构建检查结果对象
											const examResult = {
												id: itemRes.data.id,
												name: checkitemRes.data.name || '未知检查项',
												value: itemRes.data.value || '--',
												unit: checkitemRes.data.unit || '',
												referenceRange: checkitemRes.data.referenceRange || '--',
												isAbnormal: itemRes.data.isAbnormal === 1,
												description: itemRes.data.description || '',
												conclusion: itemRes.data.conclusion || '体检结果正在生成',
												advice: itemRes.data.advice || '',
												expanded: false // 默认不展开
											};
											
											// 添加到检查结果数组
											this.examResults.push(examResult);
											
											// 统计异常项目数量
											if (examResult.isAbnormal) {
												abnormalCount++;
											}
											
											// 统计完成项目数量
											if (itemRes.data.conclusion && itemRes.data.conclusion !== '体检结果正在生成') {
												finishedCount++;
											}
										}
									}
								} catch (e) {
									console.error('获取检查项目失败:', id, e);
								}
							}
							
							// 更新报告概要信息
							this.reportInfo.abnormalCount = abnormalCount;
							this.reportInfo.totalCount = this.examResults.length;
							this.reportInfo.finishedCount = finishedCount;
							
							// 更新健康建议
							const adviceList = this.examResults
								.filter(item => item.advice)
								.map(item => item.advice);
							this.reportInfo.adviceCount = adviceList.length;
							this.reportInfo.adviceList = adviceList;
						}
						
						// 更新筛选后的结果
						this.switchFilter(this.currentFilter);
					} else {
						uni.showToast({
							title: '获取报告详情失败',
							icon: 'none'
						});
					}
				} catch (error) {
					console.error('获取报告详情失败:', error);
					uni.showToast({
						title: '获取报告详情失败',
						icon: 'none'
					});
				} finally {
					this.loading = false;
				}
			},
			
			/**
			 * 更新报告基本信息
			 * 根据检查结果更新报告的统计信息
			 */
			updateReportInfo() {
				const abnormalCount = this.examResults.filter(item => item.isAbnormal).length;
				const totalCount = this.examResults.length;

				this.reportInfo = {
					packageName: '体检报告',
					reportDate: new Date().toLocaleDateString(),
					hospitalName: '体检医院',
					personName: '体检人',
					gender: '男',
					age: 30,
					examDate: new Date().toLocaleDateString(),
					abnormalCount: abnormalCount,
					totalCount: totalCount,
					adviceCount: 0,
					adviceList: []
				};
			},
			
			/**
			 * 切换筛选条件
			 * @param {string} filter 筛选类型（all: 全部, abnormal: 异常）
			 */
			switchFilter(filter) {
				this.currentFilter = filter;

				if (filter === 'all') {
					this.filteredResults = this.examResults;
				} else if (filter === 'abnormal') {
					this.filteredResults = this.examResults.filter(item => item.isAbnormal);
				}
			},
			
			/**
			 * 展开/收起检查项详情
			 * @param {number} index 检查项索引
			 */
			toggleExpand(index) {
				this.filteredResults[index].expanded = !this.filteredResults[index].expanded;
			},
			
			/**
			 * 分享报告
			 * 显示分享功能提示
			 */
			shareReport() {
				uni.showToast({
					title: '分享功能开发中',
					icon: 'none'
				});
			},
			
			/**
			 * 下载报告
			 * 模拟报告下载功能
			 */
			downloadReport() {
				uni.showLoading({
					title: '下载中...'
				});

				// 模拟下载过程
				setTimeout(() => {
					uni.hideLoading();
					uni.showToast({
						title: '下载成功',
						icon: 'success'
					});
				}, 2000);
			},
			
			/**
			 * 在线咨询
			 * 显示在线咨询功能提示
			 */
			makeConsultation() {
				uni.showToast({
					title: '在线咨询功能开发中',
					icon: 'none'
				});
			},
			
			/**
			 * 返回上一页
			 */
			goBack() {
				uni.navigateBack();
			},
			
			/**
			 * 生成并分享报告
			 * 将报告内容生成为文本格式并提供下载
			 */
			async generateAndShareReport() {
				try {
					uni.showLoading({
						title: '生成报告中...'
					});
					
					// 构建报告内容
					const reportContent = this.buildReportContent();
					
					// 显示分享选项
					uni.hideLoading();
					this.showShareOptions(reportContent);
					
				} catch (error) {
					console.error('生成报告失败:', error);
					uni.hideLoading();
					uni.showToast({
						title: '生成报告失败',
						icon: 'none'
					});
				}
			},
			
			/**
			 * 显示分享选项
			 * @param {string} reportContent 报告内容
			 */
			showShareOptions(reportContent) {
				// 直接保存到本地
				this.saveToLocal(reportContent);
			},
			
			/**
			 * 保存报告到本地
			 * @param {string} content 报告内容
			 */
			async saveToLocal(content) {
				try {
					// 在H5环境下，直接下载文件
					// #ifdef H5
					const blob = new Blob([content], { type: 'text/plain;charset=utf-8' });
					const url = URL.createObjectURL(blob);
					const link = document.createElement('a');
					link.href = url;
					link.download = `体检报告_${this.reportInfo.personName || '用户'}_${Date.now()}.txt`;
					document.body.appendChild(link);
					link.click();
					document.body.removeChild(link);
					URL.revokeObjectURL(url);
					// #endif
					
					// #ifdef APP-PLUS || MP
					// 在APP和小程序环境下，保存到本地
					const tempFilePath = await this.createReportFile(content);
					await this.saveFileToLocal(tempFilePath);
					// #endif
					
					// 显示成功提示
					uni.showModal({
						title: '下载成功',
						content: '体检报告已下载到本地，快拿去和你的朋友分享吧！',
						showCancel: false,
						confirmText: '知道了',
						confirmColor: '#1296db'
					});
				} catch (error) {
					console.error('保存失败:', error);
					uni.showToast({
						title: '下载失败',
						icon: 'none'
					});
				}
			},
			
			/**
			 * 构建报告内容
			 * 将报告数据转换为文本格式
			 * @returns {string} 格式化的报告内容
			 */
			buildReportContent() {
				const content = [];
				
				// 添加标题
				content.push('体检报告');
				content.push('='.repeat(50));
				content.push('');
				
				// 添加基本信息
				content.push(`体检人：${this.reportInfo.personName || '未知'}`);
				content.push(`性别：${this.reportInfo.gender || '未知'}`);
				content.push(`年龄：${this.reportInfo.age || '未知'}岁`);
				content.push(`体检医院：${this.reportInfo.hospitalName || '未知'}`);
				content.push(`体检时间：${this.reportInfo.examDate || '未知'}`);
				content.push(`报告时间：${this.reportInfo.reportDate || '未知'}`);
				content.push('');
				
				// 添加检查结果
				content.push('检查结果');
				content.push('-'.repeat(30));
				content.push('');
				
				this.filteredResults.forEach((item, index) => {
					content.push(`${index + 1}. ${item.name}`);
					
					if (item.description) {
						content.push(`   结果说明：${item.description}`);
					}
					
					if (item.conclusion && item.conclusion !== '体检结果正在生成') {
						content.push(`   医生结论：${item.conclusion}`);
					}
					
					if (item.advice) {
						content.push(`   健康建议：${item.advice}`);
					}
					
					content.push('');
				});
				
				// 添加页脚
				content.push('='.repeat(50));
				content.push('本报告由医疗系统生成');
				content.push(`生成时间：${new Date().toLocaleString()}`);
				
				return content.join('\n');
			},
			
			/**
			 * 创建报告文件
			 * @param {string} content 报告内容
			 * @returns {Promise<string>} 临时文件路径
			 */
			async createReportFile(content) {
				return new Promise((resolve, reject) => {
					const fileName = `体检报告_${this.reportInfo.personName || '用户'}_${Date.now()}.txt`;
					const filePath = `${uni.env.USER_DATA_PATH}/${fileName}`;
					
					uni.getFileSystemManager().writeFile({
						filePath: filePath,
						data: content,
						encoding: 'utf8',
						success: () => {
							resolve(filePath);
						},
						fail: (error) => {
							reject(error);
						}
					});
				});
			},
			
			/**
			 * 保存文件到本地
			 * @param {string} tempFilePath 临时文件路径
			 */
			async saveFileToLocal(tempFilePath) {
				return new Promise((resolve, reject) => {
					uni.saveFile({
						tempFilePath: tempFilePath,
						success: (res) => {
							resolve(res.savedFilePath);
						},
						fail: (error) => {
							reject(error);
						}
					});
				});
			}
		}
	}
</script>

<style lang="scss">
	/* 
		页面整体样式
		设置页面基本布局和背景色
	*/
	.content {
		min-height: 100vh;
		background: #f5f7fa;
		padding: 20rpx 30rpx;
	}

	/* 
		增强卡片样式
		统一的卡片容器样式，提供阴影和圆角效果
	*/
	.enhanced-card {
		background: #fff;
		border-radius: 20rpx;
		padding: 30rpx;
		margin-bottom: 20rpx;
		box-shadow: 0 4rpx 16rpx rgba(18, 150, 219, 0.08);
	}

	/* 
		报告信息卡片样式
		显示报告基本信息的卡片样式
	*/
	.report-info-card {
		background: linear-gradient(135deg, #fff 0%, #f8fbff 100%);
		border: 1px solid #e6f3ff;
		
		.report-title {
			text-align: center;
			margin-bottom: 30rpx;
			
			.package-name {
				font-size: 36rpx;
				font-weight: 700;
				color: #1296db;
				margin-left: 10rpx;
			}
			
			.report-date {
				font-size: 24rpx;
				color: #999;
				margin-top: 10rpx;
			}
		}
		
		.info-item {
			display: flex;
			justify-content: space-between;
			align-items: center;
			padding: 20rpx 0;
			border-bottom: 1px solid #f0f0f0;
			
			&:last-child {
				border-bottom: none;
			}
			
			.info-label {
				color: #666;
				font-size: 28rpx;
			}
			
			.info-value {
				color: #333;
				font-size: 28rpx;
				font-weight: 600;
				
				&.main {
					color: #1296db;
				}
			}
		}
	}

	/* 
		概要卡片样式
		显示体检结果概要的卡片样式
	*/
	.summary-card {
		.card-title {
			font-size: 32rpx;
			font-weight: 700;
			color: #333;
			margin-bottom: 30rpx;
			text-align: center;
		}
		
		.summary-content {
			display: flex;
			justify-content: space-around;
			text-align: center;
			
			.summary-item {
				flex: 1;
				
				.summary-value {
					display: block;
					font-size: 48rpx;
					font-weight: 700;
					color: #1296db;
					margin-bottom: 10rpx;
					
					&.highlight-abnormal {
						color: #ff4757;
					}
					
					&.highlight-advice {
						color: #20bf6b;
					}
				}
				
				.summary-label {
					font-size: 24rpx;
					color: #666;
				}
			}
		}
	}

	/* 
		结果卡片样式
		显示详细检查结果的卡片样式
	*/
	.result-card {
		.card-title {
			font-size: 32rpx;
			font-weight: 700;
			color: #333;
			margin-bottom: 30rpx;
			text-align: center;
		}
		
		.result-list {
			.result-item {
				border: 1px solid #e6f3ff;
				border-radius: 12rpx;
				margin-bottom: 20rpx;
				background: #fff;
				transition: all 0.3s ease;
				
				&.expanded {
					border-color: #1296db;
					box-shadow: 0 2rpx 8rpx rgba(18, 150, 219, 0.1);
				}
				
				.result-header {
					display: flex;
					justify-content: space-between;
					align-items: center;
					padding: 30rpx;
					
					.result-name {
						flex: 1;
						font-size: 28rpx;
						color: #333;
						font-weight: 600;
					}
					
					.result-value {
						display: flex;
						align-items: center;
						
						.result-unit {
							font-size: 24rpx;
							color: #666;
							margin-right: 15rpx;
						}
						
						.expand-icon {
							font-size: 24rpx;
							color: #1296db;
							font-weight: 700;
						}
					}
				}
				
				.result-detail {
					padding: 0 30rpx 30rpx 30rpx;
					border-top: 1px solid #f0f0f0;
					
					.detail-label {
						font-size: 24rpx;
						color: #666;
						margin-bottom: 10rpx;
					}
					
					.detail-value {
						font-size: 26rpx;
						color: #333;
						line-height: 1.6;
					}
					
					.result-desc {
						margin-bottom: 20rpx;
					}
					
					.conclusion-content {
						.conclusion-text {
							font-size: 26rpx;
							color: #333;
							line-height: 1.6;
							background: #f8fbff;
							padding: 20rpx;
							border-radius: 8rpx;
							margin-top: 10rpx;
						}
						
						.conclusion-list {
							.conclusion-item {
								background: #f8fbff;
								padding: 15rpx;
								border-radius: 8rpx;
								margin-bottom: 10rpx;
								
								.conclusion-category {
									font-size: 24rpx;
									color: #1296db;
									font-weight: 600;
									margin-right: 20rpx;
								}
								
								.conclusion-name {
									font-size: 26rpx;
									color: #333;
									font-weight: 600;
								}
								
								.conclusion-desc {
									font-size: 24rpx;
									color: #666;
									line-height: 1.6;
									margin-top: 5rpx;
								}
							}
						}
					}
				}
			}
		}
	}

	/* 
		底部操作栏样式
		固定在底部的操作按钮样式
	*/
	.enhanced-bottom {
		position: fixed;
		left: 0;
		right: 0;
		bottom: 0;
		background: #fff;
		display: flex;
		padding: 24rpx 30rpx 32rpx 30rpx;
		box-shadow: 0 -4rpx 24rpx rgba(18, 150, 219, 0.08);
		z-index: 10;
		
		.action-btn {
			flex: 1;
			display: flex;
			align-items: center;
			justify-content: center;
			font-size: 30rpx;
			height: 88rpx;
			line-height: 1;
			border-radius: 44rpx;
			margin: 0 12rpx;
			background: #f3f8fd;
			color: #1296db;
			font-weight: 700;
			box-shadow: 0 1rpx 4rpx rgba(18, 150, 219, 0.04);
			transition: all 0.3s ease;
			
			.btn-icon {
				font-size: 36rpx;
				margin-right: 10rpx;
			}
			
			&.primary {
				background: linear-gradient(90deg, #1296db 0%, #6ec6ff 100%);
				color: #fff;
			}
			
			&:active {
				transform: scale(0.98);
			}
			
			&::after {
				border: none;
			}
		}
	}

	/* 
		美化信息列表样式
		优化信息展示的布局和样式
	*/
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
				
				&.main {
					color: #222;
				}
			}
		}
		
		.beautify-info-item:last-child {
			border-bottom: none;
		}
	}

	/* 
		美化标题样式
		优化标题的布局和视觉效果
	*/
	.beautify-title {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: flex-start;
		margin-bottom: 20rpx;
		
		.title-center-group {
			display: flex;
			align-items: center;
			justify-content: center;
		}
		
		.package-icon {
			font-size: 52rpx;
			margin-right: 12rpx;
		}
		
		.package-name {
			font-size: 44rpx;
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

	/* 
		展开动画效果
		为展开的内容添加动画效果
	*/
	.animated-expand {
		animation: fadeInExpand 0.3s ease;
	}

	@keyframes fadeInExpand {
		0% {
			opacity: 0;
			transform: translateY(-10rpx);
		}
		100% {
			opacity: 1;
			transform: translateY(0);
		}
	}
</style>