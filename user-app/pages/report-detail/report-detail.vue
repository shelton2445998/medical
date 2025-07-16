<template>
	<view class="content">
		<!-- 报告基本信息 -->
		<view class="report-info-card enhanced-card">
			<view class="report-title beautify-title">
				<view class="title-center-group">
					<text class="package-icon">📄</text>
					<text class="package-name">{{reportInfo.packageName}}</text>
				</view>
				<text class="report-date">{{reportInfo.reportDate}}</text>
			</view>
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

		<!-- 体检结果概要 -->
		<view class="summary-card enhanced-card">
			<view class="card-title">体检结果概要</view>
			<view class="summary-content">
				<view class="summary-item" :class="{'abnormal': reportInfo.abnormalCount > 0}">
					<text class="summary-value highlight-abnormal">{{reportInfo.abnormalCount}}</text>
					<text class="summary-label">异常指标</text>
				</view>
				<view class="summary-item">
					<text class="summary-value">{{reportInfo.totalCount}}</text>
					<text class="summary-label">检查项目</text>
				</view>
				<view class="summary-item">
					<text class="summary-value highlight-advice">{{reportInfo.finishedCount}}</text>
					<text class="summary-label">完成项目</text>
				</view>
			</view>
		</view>

		<!-- 体检结果列表 -->
		<view class="result-card enhanced-card">
			<view class="card-title">体检结果</view>
			<view class="filter-tabs">
				<view class="tab-item" v-for="(item, index) in filterOptions" :key="index" :class="{active: currentFilter === item.value}"
				 @click="switchFilter(item.value)">
					<text>{{item.label}}</text>
				</view>
			</view>

			<!-- 体检项目列表 -->
			<view class="result-list">
				<view class="result-item" v-for="(item, index) in filteredResults" :key="index" :class="{'abnormal': item.isAbnormal, 'expanded': item.expanded}">
					<view class="result-header" @click="toggleExpand(index)">
						<view class="result-name">
							<text>{{item.name}}</text>
						</view>
						<view class="result-value">
							<text :class="{'abnormal-text': item.isAbnormal}">{{item.value}}</text>
							<text class="result-unit">{{item.unit}}</text>
							<text class="expand-icon">{{item.expanded ? '∧' : '∨'}}</text>
						</view>
					</view>
					<view class="result-detail animated-expand" v-if="item.expanded">
						<view class="result-desc" v-if="item.description">
							<text class="detail-label">结果说明：</text>
							<text class="detail-value">{{item.description}}</text>
						</view>
						<view class="result-conclusion">
							<text class="detail-label">医生结论：</text>
							<view class="conclusion-content">
								<view v-if="typeof item.conclusion === 'string'" class="conclusion-text">
									{{'　　' + item.conclusion}}
								</view>
								<view v-else-if="Array.isArray(item.conclusion)" class="conclusion-list">
									<view v-for="(conclusion, index) in item.conclusion" :key="index" class="conclusion-item">
										<text class="conclusion-category">{{conclusion.category}}</text>
										<text class="conclusion-name">{{conclusion.name}}</text>
										<text class="conclusion-desc">{{'　　' + conclusion.desc}}</text>
									</view>
								</view>
								<view v-else class="conclusion-text">
									{{'　　' + item.conclusion}}
								</view>
							</view>
						</view>
						<view class="result-advice" v-if="item.advice">
							<text class="detail-label">健康建议：</text>
							<text class="detail-value">{{item.advice}}</text>
						</view>
					</view>
				</view>
			</view>
		</view>

		<!-- 底部操作栏 -->
		<view class="bottom-actions enhanced-bottom">
			<button class="action-btn" @click="shareReport">
				<text class="iconfont icon-share"></text>
				<text>分享</text>
			</button>
			<button class="action-btn" @click="downloadReport">
				<text class="iconfont icon-download"></text>
				<text>下载</text>
			</button>
			<button class="action-btn primary" @click="makeConsultation">
				<text class="iconfont icon-doctor"></text>
				<text>在线咨询</text>
			</button>
		</view>
	</view>
</template>

<script>
	import {
		getAppReportById,
		getAppReportItem,
		getCheckitemById
	} from '@/api/report';
	export default {
		data() {
			return {
				reportId: null,
				currentFilter: 'all', // all, abnormal
				filterOptions: [{
						label: '全部',
						value: 'all'
					},
					{
						label: '异常',
						value: 'abnormal'
					}
				],
				reportInfo: {
					packageName: '',
					reportDate: '',
					hospitalName: '',
					personName: '',
					gender: '',
					age: 0,
					examDate: '',
					abnormalCount: 0,
					totalCount: 0,
					adviceCount: 0,
					finishedCount: 0,
					adviceList: []
				},
				examResults: [],
				filteredResults: [],
				loading: false
			}
		},
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
				if (options.patientGender) {
					let genderValue = decodeURIComponent(options.patientGender);
					if (genderValue === '1') {
						this.reportInfo.gender = '男';
					} else if (genderValue === '0') {
						this.reportInfo.gender = '女';
					} else {
						this.reportInfo.gender = genderValue;
					}
				}
				if (options.patientAge) {
					this.reportInfo.age = decodeURIComponent(options.patientAge);
				}
				// 获取报告详情
				this.getReportDetail();
			}

			// 初始化显示所有检查结果
			this.filteredResults = this.examResults;
		},
		methods: {
			// 获取报告详情
			async getReportDetail() {
			  this.loading = true;
			  try {
			    // 清空之前的检查结果
			    this.examResults = [];
			    
			    // 调用 /api/app/report/getAppReport/{id} 接口，id为reportId
			    const res = await getAppReportById(this.reportId);
			    console.log('getAppReportById返回:', res);
			    if (res && res.success && res.data) {
			      // 赋值到reportInfo
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
			
			      // 遍历reportItemIds，依次调用getAppReportItem
			      if (res.data.reportItemIds) {
			        const ids = res.data.reportItemIds.split(',').map(id => id.trim()).filter(id => id);
			        let abnormalCount = 0;
			        let finishedCount = 0;
			        
			        for (const id of ids) {
			          try {
			            const itemRes = await getAppReportItem(id);
			            console.log('getAppReportItem返回:', itemRes);
			            if (itemRes && itemRes.success && itemRes.data && itemRes.data.itemId) {
			              // 调用 getCheckitemById 获取检查项目名称等信息
			              const checkitemRes = await getCheckitemById(itemRes.data.itemId);
			              console.log('getCheckitemById返回:', checkitemRes);
			              
			              if (checkitemRes && checkitemRes.success && checkitemRes.data) {
			                // 构建检查结果对象
			                const examResult = {
			                  id: itemRes.data.id,
			                  name: checkitemRes.data.name || '未知检查项', // 检查项目名称
			                  value: itemRes.data.value || '--',
			                  unit: checkitemRes.data.unit || '',
			                  referenceRange: checkitemRes.data.referenceRange || '--',
			                  isAbnormal: itemRes.data.isAbnormal === 1,
			                  description: itemRes.data.description || '',
			                  // 如果conclusion为空，显示\"体检结果正在生成\"
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
			                // 统计 conclusion 不为空且不为“体检结果正在生成”
			                if (itemRes.data.conclusion && itemRes.data.conclusion !== '体检结果正在生成') finishedCount++;
			              }
			            }
			          } catch (e) {
			            console.error('getAppReportItem或getCheckitemById调用失败:', id, e);
			          }
			        }
			        
			        // 更新报告概要信息
			        this.reportInfo.abnormalCount = abnormalCount;
			        this.reportInfo.totalCount = this.examResults.length;
			        this.reportInfo.finishedCount = finishedCount;
			        
			        // 更新健康建议数量和列表
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
			// 更新报告基本信息
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
			// 切换筛选条件
			switchFilter(filter) {
				this.currentFilter = filter;

				if (filter === 'all') {
					this.filteredResults = this.examResults;
				} else if (filter === 'abnormal') {
					this.filteredResults = this.examResults.filter(item => item.isAbnormal);
				}
			},
			// 展开/收起检查项
			toggleExpand(index) {
				this.filteredResults[index].expanded = !this.filteredResults[index].expanded;
			},
			// 分享报告
			shareReport() {
				uni.showToast({
					title: '分享功能开发中',
					icon: 'none'
				});
			},
			// 下载报告
			downloadReport() {
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
			// 在线咨询
			makeConsultation() {
				uni.showToast({
					title: '在线咨询功能开发中',
					icon: 'none'
				});
			},
			// 返回上一页
			goBack() {
				uni.navigateBack();
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

	.content {
		background: linear-gradient(135deg, #e0e7ff 0%, #f5f5f5 100%);
		min-height: 100vh;
		padding-bottom: 120rpx;
	}

	// 卡片增强
	.enhanced-card {
		background: #fff;
		margin: 24rpx 16rpx;
		padding: 36rpx 28rpx;
		border-radius: 18rpx;
		box-shadow: 0 6rpx 32rpx rgba(18, 150, 219, 0.08), 0 1rpx 4rpx rgba(0,0,0,0.04);
		transition: box-shadow 0.2s;
	}

	.report-info-card {
		border: 1px solid #e3eaf2;
	}

	.summary-card, .result-card, .advice-card {
	}

	.card-title {
		font-size: 34rpx;
		font-weight: 700;
		color: #222;
		margin-bottom: 22rpx;
		position: relative;
		padding-left: 24rpx;
		letter-spacing: 1rpx;
		&::before {
			content: '';
			position: absolute;
			left: 0;
			top: 50%;
			transform: translateY(-50%);
			width: 8rpx;
			height: 36rpx;
			background: linear-gradient(180deg, #1296db 0%, #6ec6ff 100%);
			border-radius: 4rpx;
		}
	}

	.summary-content {
		display: flex;
		background: #f3f8fd;
		border-radius: 12rpx;
		padding: 24rpx 0;
		box-shadow: 0 2rpx 8rpx rgba(18, 150, 219, 0.04);
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
			&.abnormal .summary-value,
			.highlight-abnormal {
				color: #ff5a5f;
			}
			.highlight-advice {
				color: #1296db;
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

	.filter-tabs {
		display: flex;
		margin-bottom: 24rpx;
		.tab-item {
			padding: 12rpx 36rpx;
			font-size: 30rpx;
			color: #666;
			background: #f3f8fd;
			border-radius: 36rpx;
			margin-right: 24rpx;
			transition: background 0.2s, color 0.2s;
			&.active {
				color: #fff;
				background: linear-gradient(90deg, #1296db 0%, #6ec6ff 100%);
				box-shadow: 0 2rpx 8rpx rgba(18, 150, 219, 0.08);
			}
		}
	}

	.result-list {
		.result-item {
			border-bottom: 1px solid #e3eaf2;
			padding: 24rpx 0;
			transition: background 0.2s;
			&:last-child {
				border-bottom: none;
			}
			&.abnormal {
				background: linear-gradient(90deg, #fff0f0 0%, #fff 100%);
				.result-header .status-icon {
					display: inline-block;
					width: 36rpx;
					height: 36rpx;
					line-height: 36rpx;
					text-align: center;
					background: #ff5a5f;
					color: #fff;
					border-radius: 50%;
					font-size: 26rpx;
					margin-right: 12rpx;
					font-weight: 700;
				}
			}
			&.expanded {
				background: #f7fbff;
			}
			.result-header {
				display: flex;
				justify-content: space-between;
				align-items: center;
				padding: 0 4rpx;
				.result-name {
					font-size: 30rpx;
					color: #222;
					display: flex;
					align-items: center;
					flex-wrap: wrap;
				}
				.result-value {
					font-size: 30rpx;
					color: #222;
					display: flex;
					align-items: center;
					.abnormal-text {
						color: #ff5a5f;
						font-weight: 700;
					}
					.result-unit {
						font-size: 26rpx;
						color: #888;
						margin-left: 8rpx;
					}
					.expand-icon {
						margin-left: 24rpx;
						color: #bbb;
						font-size: 32rpx;
						transition: transform 0.2s;
					}
				}
			}
			.animated-expand {
				margin-top: 18rpx;
				padding: 18rpx;
				background: #f3f8fd;
				border-radius: 10rpx;
				font-size: 28rpx;
				box-shadow: 0 1rpx 4rpx rgba(18, 150, 219, 0.04);
				animation: fadeInExpand 0.25s;
			}
		}
	}

	@keyframes fadeInExpand {
		0% { opacity: 0; transform: scaleY(0.95); }
		100% { opacity: 1; transform: scaleY(1); }
	}

	// 底部操作栏增强
	.enhanced-bottom {
		position: fixed;
		left: 0;
		right: 0;
		bottom: 0;
		background: #fff;
		display: flex;
		padding: 24rpx 12rpx 32rpx 12rpx;
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
			transition: background 0.2s, color 0.2s;
			.iconfont {
				font-size: 36rpx;
				margin-right: 10rpx;
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
					color: #222; // 由 #1296db 改为 #222，显示为黑色
				}
			}
		}
		.beautify-info-item:last-child {
			border-bottom: none;
		}
	}

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
		.exam-date-center {
			margin-top: 8rpx;
			font-size: 26rpx;
			color: #666;
			text-align: center;
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
</style>