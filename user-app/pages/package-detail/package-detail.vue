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
		<!-- 套餐封面图 -->
			<view class="package-banner-section">
		<image class="package-banner" src="/static/images/package1.jpg" mode="aspectFill"></image>
				<view class="banner-overlay">
					<view class="banner-content">
						<text class="banner-title">{{packageInfo.name}}</text>
						<view class="banner-price">
							<text class="price-symbol">¥</text>
							<text class="price-value">{{packageInfo.price}}</text>
							<text class="price-original" v-if="packageInfo.originalPrice && packageInfo.originalPrice > packageInfo.price">¥{{packageInfo.originalPrice}}</text>
				</view>
					</view>
				</view>
			</view>
			
			<!-- 加载状态提示 -->
			<view class="loading-card" v-if="loading">
				<view class="loading-content">
					<view class="loading-spinner"></view>
					<text class="loading-text">正在加载套餐详情...</text>
				</view>
			</view>
			
			<!-- 套餐基本信息卡片 -->
			<view class="package-info-card" v-if="!loading && !error">
				<view class="card-header">
					<view class="header-icon">🏥</view>
					<view class="header-title">套餐信息</view>
			</view>
			<view class="package-tags">
				<text class="tag" v-for="(tag, index) in packageInfo.tags" :key="index">{{tag}}</text>
			</view>
				<view class="package-desc">
					<text class="desc-text">{{packageInfo.description}}</text>
				</view>
		</view>
		
			<!-- 医院信息卡片 -->
			<view class="hospital-card" v-if="!loading && !error && packageInfo.hospitalName" @click="goToHospitalDetail">
				<view class="card-header">
					<view class="header-left">
						<view class="header-icon">🏥</view>
						<view class="header-title">体检医院</view>
					</view>
					<view class="header-right">
						<button class="change-hospital-btn" @click.stop="selectHospital">更换医院</button>
					</view>
				</view>
				<view class="hospital-content">
					<image class="hospital-image" :src="packageInfo.hospitalImage || '/static/images/hospital1.jpg'" mode="aspectFill"></image>
					<view class="hospital-detail">
						<text class="hospital-name">{{packageInfo.hospitalName || '未指定医院'}}</text>
						<view class="hospital-address">
							<text class="address-icon">📍</text>
							<text class="address-text">{{packageInfo.hospitalAddress || '地址信息待完善'}}</text>
						</view>
					</view>
					<view class="hospital-arrow">
						<text class="arrow-icon">→</text>
					</view>
				</view>
			</view>
		
		<!-- 医院信息为空时的提示 -->
		<view class="hospital-empty-card" v-if="!loading && !error && !packageInfo.hospitalName">
			<view class="card-header">
				<view class="header-icon">🏥</view>
				<view class="header-title">体检医院</view>
			</view>
			<view class="empty-content">
				<text class="empty-text">该套餐暂未指定体检医院</text>
				<text class="empty-desc">您可以在预约时选择体检医院</text>
				<button class="select-hospital-btn" @click="selectHospital">选择医院</button>
			</view>
		</view>
		
			<!-- 检查项目卡片 -->
			<view class="checkitems-card" v-if="!loading && !error">
				<view class="card-header">
					<view class="header-icon">🔬</view>
					<view class="header-title">检查项目</view>
					<view class="header-count">共{{checkItems && checkItems.length ? checkItems.length : 0}}项</view>
			</view>
				<view class="checkitems-content">
					<view class="category-section" v-for="(category, categoryIndex) in categoryItems || []" :key="categoryIndex">
					<view class="category-header">
						<text class="category-name">{{category.name}}</text>
						<text class="category-count">{{category.items && category.items.length ? category.items.length : 0}}项</text>
					</view>
						<view class="items-list">
							<view class="item-item" v-for="(item, itemIndex) in (category.items || [])" :key="itemIndex">
								<view class="item-icon">🔍</view>
								<view class="item-info">
							<text class="item-name">{{item.name}}</text>
							<text class="item-desc">{{item.desc}}</text>
								</view>
						</view>
					</view>
				</view>
			</view>
		</view>
		
			<!-- 适用人群卡片 -->
			<view class="suitable-card" v-if="!loading && !error">
				<view class="card-header">
					<view class="header-icon">👥</view>
					<view class="header-title">适用人群</view>
			</view>
				<view class="suitable-content">
					<text class="suitable-text">{{packageInfo.suitablePeople}}</text>
			</view>
		</view>
		
			<!-- 预约须知卡片 -->
			<view class="notice-card" v-if="!loading && !error">
				<view class="card-header">
					<view class="header-icon">📋</view>
					<view class="header-title">预约须知</view>
			</view>
				<view class="notice-content">
				<view class="notice-item" v-for="(item, index) in packageInfo.notices" :key="index">
						<text class="notice-dot">•</text>
					<text class="notice-text">{{item}}</text>
				</view>
			</view>
		</view>
		
			<!-- 用户评价卡片 -->
			<view class="reviews-card" v-if="!loading && !error">
				<view class="card-header">
					<view class="header-icon">⭐</view>
					<view class="header-title">用户评价</view>
					<view class="more-btn" @click="goToReviews">
					<text>更多</text>
						<text class="arrow-icon">→</text>
				</view>
			</view>
				<view class="reviews-content" v-if="packageInfo.reviews && packageInfo.reviews.length > 0">
				<view class="review-item" v-for="(item, index) in packageInfo.reviews" :key="index">
					<view class="reviewer-info">
						<view class="reviewer-detail">
							<text class="reviewer-name">{{item.name}}</text>
							<text class="review-time">{{item.time}}</text>
						</view>
						<view class="review-rating">
								<text class="star" v-for="n in item.rating" :key="n">⭐</text>
								<text class="star-empty" v-for="n in 5-item.rating" :key="n+5">☆</text>
						</view>
					</view>
					<text class="review-content">{{item.content}}</text>
				</view>
			</view>
			<view class="empty-reviews" v-else>
				<text class="empty-text">暂无评价</text>
				</view>
			</view>
		</view>
		
		<!-- 底部按钮 -->
		<view class="bottom-actions">
			<button class="action-btn consult-btn" @click="consult">
				<text class="btn-icon">💬</text>
				<text class="btn-text">在线咨询</text>
			</button>
			<button class="action-btn appointment-btn" @click="makeAppointment">
				<text class="btn-icon">📅</text>
				<text class="btn-text">立即预约</text>
			</button>
		</view>
	</view>
</template>

<script>
	import { get, post } from '@/utils/request.js';
	import { packageApi } from '@/utils/api.js';
	import { checkitemApi } from '@/utils/api.js'; // 新增导入
	export default {
		data() {
			return {
				packageInfo: {
					id: 0,
					name: '加载中...',
					price: 0,
					originalPrice: 0,
					tags: [],
					description: '套餐详细介绍',
					suitablePeople: '适用人群信息',
					checkItems: '',
					notices: ['预约须知信息'],
					reviews: [],
					hospitalName: '',
					hospitalAddress: '',
					hospitalImage: '/static/images/hospital1.jpg'
				},
				loading: false,
				error: false,
				errorMessage: '',
				demoPackages: {
					1: {
						id: 1,
						name: '标准体检套餐',
						price: 299,
						originalPrice: 399,
						tags: ['热门', '体检', '全面'],
						hospitalId: 1,
						hospitalName: '沈阳市云医院-和平分院',
						hospitalAddress: '沈阳市和平区南京南街61号',
						hospitalImage: '/static/images/hospital1.jpg',
						description: '本套餐是针对25-45岁人群设计的基础体检套餐，包含血常规、尿常规、肝功能、肾功能、血脂、血糖等基础检查项目，能够全面了解身体健康状况，及早发现潜在健康风险。',
						suitablePeople: '适合25-45岁人群，特别是长期伏案工作、生活压力大、作息不规律的上班族，建议每年体检一次。',
						items: [
							{ category: '一般检查', name: '身高体重', desc: '测量身高、体重，计算体重指数(BMI)' },
							{ category: '一般检查', name: '血压', desc: '测量收缩压和舒张压' },
							{ category: '一般检查', name: '视力', desc: '检查远视力和近视力' },
							{ category: '血液检查', name: '血常规', desc: '检查红细胞、白细胞、血小板等指标' },
							{ category: '血液检查', name: '肝功能', desc: '检查ALT、AST、总蛋白等肝功能指标' },
							{ category: '血液检查', name: '肾功能', desc: '检查尿素氮、肌酐等肾功能指标' },
							{ category: '血液检查', name: '血脂', desc: '检查总胆固醇、甘油三酯等血脂指标' },
							{ category: '血液检查', name: '血糖', desc: '检查空腹血糖' },
							{ category: '尿液检查', name: '尿常规', desc: '检查尿液颜色、比重、pH值等指标' },
							{ category: '影像检查', name: '胸部X光', desc: '检查肺部、心脏等胸腔器官' },
							{ category: '影像检查', name: 'B超', desc: '肝、胆、脾、胰、肾B超检查' }
						],
						notices: [
							'体检前一天请清淡饮食，避免辛辣、油腻食物',
							'体检当天请空腹，禁食8-12小时',
							'体检前一天晚上请保证充足睡眠',
							'体检当天请携带身份证等有效证件',
							'体检报告一般在3-5个工作日出具，可在APP查看'
						],
						reviews: [
							{ id: 1, name: '张先生', avatar: '/static/images/avatar1.jpg', time: '2023-07-08', rating: 5, content: '套餐内容很全面，医态很态度很好，检查很仔细，报告出来也很快，值得推荐！' },
							{ id: 2, name: '李女士', avatar: '/static/images/avatar2.jpg', time: '2023-07-06', rating: 4, content: '体检很顺利，医护人员很专业，就是人比较多，需要排队等待。' }
						]
					},
					2: {
						id: 2,
						name: '男性专项体检套餐',
						price: 980,
						originalPrice: 1200,
						tags: ['男性', '专项', '健康'],
						hospitalId: 1,
						hospitalName: '沈阳市云医院-和平分院',
						hospitalAddress: '沈阳市和平区南京南街61号',
						hospitalImage: '/static/images/hospital1.jpg',
						description: '针对男性健康，增加前列腺、肿瘤筛查等专项检查，适合30岁以上男性。',
						suitablePeople: '适合30岁以上男性，关注前列腺、肿瘤筛查等专项健康问题。',
						items: [
							{ category: '一般检查', name: '身高体重', desc: '测量身高、体重，计算BMI' },
							{ category: '血液检查', name: '血常规', desc: '检查红细胞、白细胞、血小板等' },
							{ category: '血液检查', name: '肝功能', desc: 'ALT、AST等肝功能指标' },
							{ category: '血液检查', name: '肾功能', desc: '尿素氮、肌酐等肾功能指标' },
							{ category: '血液检查', name: '血脂', desc: '总胆固醇、甘油三酯等' },
							{ category: '血液检查', name: '血糖', desc: '空腹血糖' },
							{ category: '专项检查', name: '前列腺B超', desc: '前列腺结构及病变筛查' },
							{ category: '专项检查', name: '肿瘤标志物', desc: '男性常见肿瘤筛查' },
							{ category: '心电检查', name: '心电图', desc: '心脏电活动检测' },
							{ category: '影像检查', name: '胸部X光', desc: '肺部、心脏等胸腔器官' }
						],
						notices: [
							'体检前避免剧烈运动',
							'体检当天请空腹',
							'如有慢性病请携带相关病历资料'
						],
						reviews: [
							{ id: 1, name: '王先生', avatar: '/static/images/avatar1.jpg', time: '2023-06-10', rating: 5, content: '男性专项很细致，医生讲解到位。' }
						]
					},
					3: {
						id: 3,
						name: '女性专项体检套餐',
						price: 1040,
						originalPrice: 1300,
						tags: ['女性', '专项', '健康'],
						hospitalId: 1,
						hospitalName: '沈阳市云医院-和平分院',
						hospitalAddress: '沈阳市和平区南京南街61号',
						hospitalImage: '/static/images/hospital1.jpg',
						description: '针对女性健康，增加乳腺、妇科、HPV等专项检查，适合30岁以上女性。',
						suitablePeople: '适合30岁以上女性，关注乳腺、妇科健康。',
						items: [
							{ category: '一般检查', name: '身高体重', desc: '测量身高、体重，计算BMI' },
							{ category: '血液检查', name: '血常规', desc: '检查红细胞、白细胞、血小板等' },
							{ category: '血液检查', name: '肝功能', desc: 'ALT、AST等肝功能指标' },
							{ category: '血液检查', name: '肾功能', desc: '尿素氮、肌酐等肾功能指标' },
							{ category: '血液检查', name: '血脂', desc: '总胆固醇、甘油三酯等' },
							{ category: '血液检查', name: '血糖', desc: '空腹血糖' },
							{ category: '专项检查', name: '乳腺彩超', desc: '乳腺结构及病变筛查' },
							{ category: '专项检查', name: '妇科检查', desc: '妇科常规体检' },
							{ category: '专项检查', name: 'HPV检测', desc: '宫颈癌筛查' },
							{ category: '心电检查', name: '心电图', desc: '心脏电活动检测' },
							{ category: '影像检查', name: 'B超', desc: '盆腔、腹部等' }
						],
						notices: [
							'体检前避免性生活',
							'体检当天请空腹',
							'如有特殊生理期请提前告知'
						],
						reviews: [
							{ id: 1, name: '赵女士', avatar: '/static/images/avatar2.jpg', time: '2023-05-20', rating: 5, content: '女性专项很贴心，检查全面。' }
						]
					},
					4: {
						id: 4,
						name: '老年体检套餐',
						price: 1280,
						originalPrice: 1500,
						tags: ['老年', '心脑血管', '骨密度'],
						hospitalId: 1,
						hospitalName: '沈阳市云医院-和平分院',
						hospitalAddress: '沈阳市和平区南京南街61号',
						hospitalImage: '/static/images/hospital1.jpg',
						description: '适合60岁以上老年人，重点检查心脑血管、骨密度、听力等项目。',
						suitablePeople: '适合60岁以上老年人，关注心脑血管、骨密度、听力等健康问题。',
						items: [
							{ category: '一般检查', name: '身高体重', desc: '测量身高、体重，计算BMI' },
							{ category: '血液检查', name: '血常规', desc: '检查红细胞、白细胞、血小板等' },
							{ category: '血液检查', name: '肝功能', desc: 'ALT、AST等肝功能指标' },
							{ category: '血液检查', name: '肾功能', desc: '尿素氮、肌酐等肾功能指标' },
							{ category: '血液检查', name: '血脂', desc: '总胆固醇、甘油三酯等' },
							{ category: '血液检查', name: '血糖', desc: '空腹血糖' },
							{ category: '心电检查', name: '心电图', desc: '心脏电活动检测' },
							{ category: '心脑血管', name: '脑血流图', desc: '脑部血流情况检测' },
							{ category: '骨密度', name: '骨密度', desc: '骨质疏松筛查' },
							{ category: '听力', name: '听力检查', desc: '老年听力筛查' },
							{ category: '视力', name: '视力检查', desc: '老年视力筛查' },
							{ category: '影像检查', name: 'B超', desc: '腹部、盆腔等' }
						],
						notices: [
							'体检前避免剧烈运动',
							'体检当天请空腹',
							'如有慢性病请携带相关病历资料'
						],
						reviews: [
							{ id: 1, name: '刘大爷', avatar: '/static/images/avatar1.jpg', time: '2023-04-15', rating: 5, content: '老年套餐很细致，检查项目多。' }
						]
					},
					5: {
						id: 5,
						name: '高端全身体检套餐',
						price: 2580,
						originalPrice: 3000,
						tags: ['高端', '深度筛查', '基因检测'],
						hospitalId: 1,
						hospitalName: '沈阳市云医院-和平分院',
						hospitalAddress: '沈阳市和平区南京南街61号',
						hospitalImage: '/static/images/hospital1.jpg',
						description: '适合高净值人群，包含全身多系统深度筛查，含肿瘤、心脑血管、基因检测等。',
						suitablePeople: '适合高净值人群，追求健康管理和疾病早筛。',
						items: [
							{ category: '一般检查', name: '身高体重', desc: '测量身高、体重，计算BMI' },
							{ category: '血液检查', name: '血常规', desc: '检查红细胞、白细胞、血小板等' },
							{ category: '血液检查', name: '肝功能', desc: 'ALT、AST等肝功能指标' },
							{ category: '血液检查', name: '肾功能', desc: '尿素氮、肌酐等肾功能指标' },
							{ category: '血液检查', name: '血脂', desc: '总胆固醇、甘油三酯等' },
							{ category: '血液检查', name: '血糖', desc: '空腹血糖' },
							{ category: '专项检查', name: '肿瘤标志物', desc: '多种肿瘤早筛' },
							{ category: '心脏检查', name: '心脏彩超', desc: '心脏结构及功能' },
							{ category: '影像检查', name: '脑MRI', desc: '脑部结构及病变筛查' },
							{ category: '基因检测', name: '基因检测', desc: '疾病易感基因筛查' },
							{ category: '影像检查', name: '全身CT', desc: '全身系统影像筛查' }
						],
						notices: [
							'体检前请提前预约',
							'部分项目需空腹',
							'如需基因检测请提前采样'
						],
						reviews: [
							{ id: 1, name: '高先生', avatar: '/static/images/avatar1.jpg', time: '2023-03-12', rating: 5, content: '高端套餐体验很好，服务周到。' }
						]
					},
					6: {
						id: 6,
						name: '入职体检套餐',
						price: 180,
						originalPrice: 200,
						tags: ['入职', '基础', '经济'],
						hospitalId: 1,
						hospitalName: '沈阳市云医院-和平分院',
						hospitalAddress: '沈阳市和平区南京南街61号',
						hospitalImage: '/static/images/hospital1.jpg',
						description: '适合新员工入职，包含基础健康检查项目，满足大部分企业入职要求。',
						suitablePeople: '适合新员工入职体检。',
						items: [
							{ category: '一般检查', name: '身高体重', desc: '测量身高、体重，计算BMI' },
							{ category: '血液检查', name: '血常规', desc: '检查红细胞、白细胞、血小板等' },
							{ category: '血液检查', name: '肝功能', desc: 'ALT、AST等肝功能指标' },
							{ category: '影像检查', name: '胸部X光', desc: '肺部、心脏等胸腔器官' },
							{ category: '心电检查', name: '心电图', desc: '心脏电活动检测' },
							{ category: '一般检查', name: '血压', desc: '测量收缩压和舒张压' }
						],
						notices: [
							'体检当天请空腹',
							'请携带身份证件',
							'如有特殊要求请提前说明'
						],
						reviews: [
							{ id: 1, name: '新员工', avatar: '/static/images/avatar2.jpg', time: '2023-02-01', rating: 4, content: '入职套餐很实用，流程顺畅。' }
						]
					},
					7: {
						id: 7,
						name: '儿童体检套餐',
						price: 480,
						originalPrice: 600,
						tags: ['儿童', '生长发育', '健康筛查'],
						hospitalId: 1,
						hospitalName: '沈阳市云医院-和平分院',
						hospitalAddress: '沈阳市和平区南京南街61号',
						hospitalImage: '/static/images/hospital1.jpg',
						description: '适合3-14岁儿童，包含生长发育、视力、听力、常规血尿等检查，关注儿童健康成长。',
						suitablePeople: '适合3-14岁儿童，关注成长发育及常见健康问题。',
						items: [
							{ category: '一般检查', name: '生长发育评估', desc: '身高、体重、体格发育测量' },
							{ category: '血液检查', name: '血常规', desc: '检查红细胞、白细胞、血小板等' },
							{ category: '血液检查', name: '肝功能', desc: 'ALT、AST等肝功能指标' },
							{ category: '血液检查', name: '肾功能', desc: '尿素氮、肌酐等肾功能指标' },
							{ category: '血液检查', name: '血糖', desc: '空腹血糖' },
							{ category: '微量元素', name: '微量元素检测', desc: '钙、铁、锌等微量元素筛查' },
							{ category: '尿液检查', name: '尿常规', desc: '尿液常规指标检测' },
							{ category: '视力', name: '视力检查', desc: '远视力、近视力筛查' },
							{ category: '听力', name: '听力筛查', desc: '儿童听力健康筛查' },
							{ category: '心电检查', name: '心电图', desc: '心脏电活动检测' },
							{ category: '影像检查', name: '胸部X光', desc: '肺部、心脏等胸腔器官' }
						],
						notices: [
							'体检前一天请清淡饮食，避免油腻、辛辣食物',
							'体检当天请空腹，禁食8-12小时',
							'请携带儿童身份证或户口本',
							'体检报告一般在3-5个工作日出具，可在APP查看'
						],
						reviews: [
							{ id: 1, name: '小明家长', avatar: '/static/images/avatar1.jpg', time: '2023-07-10', rating: 5, content: '儿童体检很细致，医生很有耐心，孩子也不怕。' },
							{ id: 2, name: '小红妈妈', avatar: '/static/images/avatar2.jpg', time: '2023-07-09', rating: 5, content: '检查项目很全面，报告解读很细致。' }
						]
					},
					8: {
						id: 8,
						name: '高级体检套餐',
						price: 699,
						originalPrice: 899,
						tags: ['高级', '心脑血管', '肿瘤筛查'],
						hospitalId: 1,
						hospitalName: '沈阳市云医院-和平分院',
						hospitalAddress: '沈阳市和平区南京南街61号',
						hospitalImage: '/static/images/hospital1.jpg',
						description: '适合45岁以上人群，包含心脑血管、肿瘤筛查等全面检查，帮助早期发现慢性病和肿瘤风险。',
						suitablePeople: '适合45岁以上关注慢性病、肿瘤风险的人群，建议每年体检一次。',
						items: [
							{ category: '一般检查', name: '身高体重', desc: '测量身高、体重，计算BMI' },
							{ category: '血液检查', name: '血常规', desc: '检查红细胞、白细胞、血小板等' },
							{ category: '血液检查', name: '肝功能', desc: 'ALT、AST等肝功能指标' },
							{ category: '血液检查', name: '肾功能', desc: '尿素氮、肌酐等肾功能指标' },
							{ category: '血液检查', name: '血脂', desc: '总胆固醇、甘油三酯等' },
							{ category: '血液检查', name: '血糖', desc: '空腹血糖' },
							{ category: '心电检查', name: '心电图', desc: '心脏电活动检测' },
							{ category: '心脏检查', name: '心脏彩超', desc: '心脏结构及功能' },
							{ category: '心脑血管', name: '脑血流图', desc: '脑部血流情况检测' },
							{ category: '专项检查', name: '肿瘤标志物', desc: '多种肿瘤早筛' },
							{ category: '影像检查', name: '胸部X光', desc: '肺部、心脏等胸腔器官' },
							{ category: '影像检查', name: '腹部B超', desc: '腹部脏器筛查' }
						],
						notices: [
							'体检前请清淡饮食，避免油腻、辛辣食物',
							'体检当天请空腹，禁食8-12小时',
							'如有慢性病请携带相关病历资料',
							'体检报告一般在3-5个工作日出具，可在APP查看'
						],
						reviews: [
							{ id: 1, name: '孙女士', avatar: '/static/images/avatar2.jpg', time: '2023-07-10', rating: 5, content: '检查项目很全面，医生很专业，推荐给中老年朋友。' },
							{ id: 2, name: '李先生', avatar: '/static/images/avatar1.jpg', time: '2023-07-09', rating: 4, content: '体检体验不错，报告解读很细致。' }
						]
					}
				},
				checkItems: [] // 存储从后端获取的检查项目详细信息
			}
		},
		computed: {
			// 计算折扣
			discount() {
				if (this.packageInfo.originalPrice) {
					return (this.packageInfo.price / this.packageInfo.originalPrice * 10).toFixed(1);
				}
				return '10.0';
			},
			// 按类别分组的检查项目
			categoryItems() {
				const categories = {};
				if (this.checkItems && Array.isArray(this.checkItems)) {
					this.checkItems.forEach(item => {
						// 根据department_id或其他字段确定类别
						const category = this.getCategoryByDepartmentId(item.departmentId) || '其他检查';
						if (!categories[category]) {
							categories[category] = {
								name: category,
								items: []
							};
						}
						categories[category].items.push({
							name: item.name,
							desc: item.description || '暂无描述',
							price: item.price,
							id: item.id
						});
					});
				}
				return Object.values(categories);
			},
			// 检查数据是否已加载
			isDataLoaded() {
				return this.packageInfo.id > 0 && this.packageInfo.name !== '加载中...';
			}
		},
		async onLoad(options) {
			// 清理可能冲突的存储数据
			uni.removeStorageSync('currentPackage');
			
			if (options && options.id) {
				try {
					this.loading = true;
					this.error = false;
					this.errorMessage = '';
					
					// 直接调用套餐详情接口
					const result = await get(packageApi.getPackageDetail(options.id));
					
					if (result && result.data) {
						// 使用后端提供的解析方法获取各个部分的信息
						let packageDescription = result.data.packageDescription || result.data.description || '';
						let suitableCrowd = result.data.suitableCrowd || '';
						let appointmentNotice = result.data.appointmentNotice || '';
						let userReviews = result.data.userReviews || '';
						
						// 解析预约须知（JSON格式）
						let notices = [];
						if (appointmentNotice && appointmentNotice.trim()) {
							try {
								notices = JSON.parse(appointmentNotice);
							} catch (e) {
								// 如果JSON解析失败，尝试作为普通字符串处理
								if (typeof appointmentNotice === 'string' && appointmentNotice.trim()) {
									notices = [appointmentNotice];
								}
							}
						}
						
						// 解析用户评价（JSON格式）
						let reviews = [];
						if (userReviews && userReviews.trim()) {
							try {
								reviews = JSON.parse(userReviews);
							} catch (e) {
								// 如果JSON解析失败，尝试作为普通字符串处理
								if (typeof userReviews === 'string' && userReviews.trim()) {
									reviews = [{ id: 1, name: '用户', time: '2023-01-01', rating: 5, content: userReviews }];
								}
							}
						}
						
						// 设置套餐信息，确保所有字段都有值
						this.packageInfo = {
							id: result.data.id,
							name: result.data.name || '套餐名称',
							price: result.data.discountPrice || result.data.price || 0, // 显示优惠价格
							originalPrice: result.data.price || 0, // 显示原价
							tags: result.data.tags || [],
							description: packageDescription || '套餐详细介绍',
							suitablePeople: suitableCrowd || '适用人群信息',
							checkItems: result.data.checkitemIds || '', // 保存检查项目ID字符串
							notices: notices.length > 0 ? notices : ['预约须知信息'],
							reviews: reviews.length > 0 ? reviews : [],
							// 医院信息（从本地存储获取或使用默认值）
							hospitalName: '',
							hospitalAddress: '',
							hospitalImage: '/static/images/hospital1.jpg'
						};
						
						// 获取检查项目详细信息
						await this.loadCheckItems();
						
						// 检查是否有已选择的医院信息
						this.checkSelectedHospital();
					} else {
						throw new Error('套餐详情数据为空');
					}
				} catch (e) {
					console.error('加载套餐详情失败:', e);
					this.error = true;
					this.errorMessage = e.message || '加载套餐详情失败';
					uni.showToast({ title: '加载套餐详情失败', icon: 'none' });
				} finally {
					this.loading = false;
				}
			}
		},
		onShow() {
			// 页面显示时重新检查医院信息
			this.checkSelectedHospital();
			
			// 检查是否有从医院选择页面返回的更新
			const selectedPackage = uni.getStorageSync('selectedPackage');
			if (selectedPackage) {
				try {
					const packageInfo = JSON.parse(selectedPackage);
					// 如果返回的套餐ID与当前套餐ID相同，则更新医院信息
					if (packageInfo.id === this.packageInfo.id) {
						this.packageInfo.hospitalName = packageInfo.hospitalName;
						this.packageInfo.hospitalAddress = packageInfo.hospitalAddress;
						this.packageInfo.hospitalImage = packageInfo.hospitalImage;
					}
				} catch (e) {
					console.error('解析选择的套餐信息失败:', e);
				}
			}
		},
		methods: {
			// 检查已选择的医院信息
			checkSelectedHospital() {
				const selectedHospital = uni.getStorageSync('selectedHospital');
				
				if (selectedHospital) {
					try {
						const hospitalInfo = JSON.parse(selectedHospital);
						
						// 只有在当前医院信息为空时才使用本地存储的医院信息
						if (!this.packageInfo.hospitalName) {
							// 更新套餐信息中的医院信息
							this.packageInfo.hospitalName = hospitalInfo.name;
							this.packageInfo.hospitalAddress = hospitalInfo.address;
							this.packageInfo.hospitalImage = hospitalInfo.image;
						}
					} catch (e) {
						console.error('解析已选择的医院信息失败:', e);
					}
				}
			},
			// 根据科室ID获取类别名称
			getCategoryByDepartmentId(departmentId) {
				const categoryMap = {
					2005: '心电检查',
					2006: '影像检查', 
					2007: '血液检查',
					2008: '尿液检查',
					2009: '一般检查',
					2010: '专项检查',
					2011: '心脑血管',
					2012: '骨密度',
					2013: '听力',
					2014: '视力',
					2015: '微量元素',
					2016: '心脏检查',
					2017: '基因检测'
				};
				return categoryMap[departmentId] || '其他检查';
			},
			
			// 加载检查项目详细信息
			async loadCheckItems() {
				try {
					if (!this.packageInfo.checkItems) {
						return;
					}
					
					// 解析检查项目ID列表
					const checkItemIds = this.packageInfo.checkItems.split(',').map(id => id.trim()).filter(id => id);
					
					if (checkItemIds.length === 0) {
						return;
					}
					
					// 逐个获取检查项目详情
					const checkItems = [];
					for (const id of checkItemIds) {
						try {
							const result = await post(checkitemApi.getCheckitemDetail(id));
							
							if (result && result.data) {
								checkItems.push(result.data);
							}
						} catch (error) {
							console.error(`获取检查项目${id}详情失败:`, error);
						}
					}
					
					this.checkItems = checkItems;
					
				} catch (error) {
					console.error('加载检查项目失败:', error);
					uni.showToast({ title: '加载检查项目失败', icon: 'none' });
				}
			},
			
			async loadPackageDetail(id) {
				try {
					const res = await uni.request({
						url: `/api/packages/${id}`
					});
					this.packageInfo = res.data;
				} catch (error) {
					uni.showToast({
						title: '加载套餐详情失败',
						icon: 'none'
					});
				}
			},
			// 立即预约
			makeAppointment() {
				// 检查是否已选择医院
				const selectedHospital = uni.getStorageSync('selectedHospital');
				
				if (!selectedHospital) {
					// 没有选择医院，提示用户并跳转到医院选择页面
					uni.showModal({
						title: '提示',
						content: '请先选择体检医院',
						confirmText: '去选择',
						cancelText: '取消',
						success: (res) => {
							if (res.confirm) {
								// 跳转到医院选择页面
								uni.navigateTo({
									url: '/pages/hospital/hospital'
								});
							}
						}
					});
					return;
				}
				
				// 解析选择的医院信息
				let hospitalInfo;
				try {
					hospitalInfo = JSON.parse(selectedHospital);
				} catch (e) {
					console.error('解析医院信息失败:', e);
					uni.showToast({
						title: '医院信息解析失败',
						icon: 'none'
					});
					return;
				}
				
				// 更新套餐信息中的医院信息
				this.packageInfo.hospitalName = hospitalInfo.name;
				this.packageInfo.hospitalAddress = hospitalInfo.address;
				this.packageInfo.hospitalImage = hospitalInfo.image;
				
				// 存储选择的套餐信息
				uni.setStorageSync('selectedPackage', JSON.stringify(this.packageInfo));
				
				// 跳转到预约流程页面
				uni.navigateTo({
					url: '/pages/appointment/appointment-flow'
				});
			},
			// 跳转到专家咨询界面
			consult() {
				uni.navigateTo({
					url: '/pages/consult/consult'
				});
			},
			// 跳转到医院详情页面
			goToHospitalDetail() {
				// 由于不再有hospitalId，直接跳转到医院选择页面
				uni.navigateTo({
					url: '/pages/hospital/hospital'
				});
			},
			// 选择医院
			selectHospital() {
				// 存储当前套餐信息，以便在医院选择页面返回后使用
				uni.setStorageSync('currentPackage', JSON.stringify(this.packageInfo));
				
				uni.navigateTo({
					url: '/pages/hospital/hospital?fromPackage=true'
				});
			},
			goToReviews() {
				uni.navigateTo({
					url: `/pages/reviews/reviews?packageId=${this.packageInfo.id}`
				});
			}
		}
	}
</script>

<style lang="scss">
.content {
	background: linear-gradient(135deg, #0984e3 0%, #74b9ff 50%, #0984e3 100%);
	min-height: 100vh;
	padding-bottom: 120rpx;
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
}

/* 动态背景装饰 */
.floating-shapes {
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	pointer-events: none;
	z-index: 1;
	
	.shape {
		position: absolute;
		border-radius: 50%;
		background: rgba(255, 255, 255, 0.1);
		animation: float 6s ease-in-out infinite;
		
		&.shape-1 {
			width: 80rpx;
			height: 80rpx;
			top: 10%;
			left: 10%;
			animation-delay: 0s;
		}
		
		&.shape-2 {
			width: 120rpx;
			height: 120rpx;
			top: 20%;
			right: 15%;
			animation-delay: 2s;
		}
		
		&.shape-3 {
			width: 60rpx;
			height: 60rpx;
			bottom: 30%;
			left: 20%;
			animation-delay: 4s;
		}
		
		&.shape-4 {
			width: 100rpx;
			height: 100rpx;
			bottom: 20%;
			right: 10%;
			animation-delay: 1s;
		}
	}
}

.main-content {
	position: relative;
	z-index: 2;
	padding: 20rpx 40rpx 0 40rpx;
}

/* 套餐横幅 */
.package-banner-section {
	position: relative;
	margin: 20rpx 0;
	border-radius: 24rpx;
	overflow: hidden;
	box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.2);

.package-banner {
	width: 100%;
	height: 400rpx;
		display: block;
}

	.banner-overlay {
		position: absolute;
		bottom: 0;
		left: 0;
		right: 0;
		background: linear-gradient(transparent, rgba(0, 0, 0, 0.7));
		padding: 40rpx 30rpx;
	
		.banner-content {
			.banner-title {
		font-size: 36rpx;
		font-weight: bold;
				color: #ffffff;
				margin-bottom: 15rpx;
				text-shadow: 0 2rpx 4rpx rgba(0, 0, 0, 0.5);
			}
			
			.banner-price {
			display: flex;
			align-items: baseline;
				gap: 10rpx;
		
				.price-symbol {
					font-size: 32rpx;
					color: #ffffff;
					font-weight: bold;
				}
				
				.price-value {
					font-size: 48rpx;
			font-weight: bold;
					color: #ffffff;
		}
		
				.price-original {
			font-size: 28rpx;
					color: rgba(255, 255, 255, 0.8);
			text-decoration: line-through;
				}
			}
		}
	}
}

/* 卡片通用样式 */
.package-info-card,
.hospital-card,
.checkitems-card,
.suitable-card,
.notice-card,
.reviews-card {
	background: rgba(255, 255, 255, 0.95);
	border-radius: 24rpx;
	margin: 20rpx 0;
	padding: 30rpx;
	box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
	backdrop-filter: blur(10rpx);
	animation: slideInUp 0.6s ease-out;
	transition: all 0.3s ease;
	
	&:hover {
		transform: translateY(-4rpx);
		box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.15);
	}
	
	.card-header {
		display: flex;
		align-items: center;
		margin-bottom: 20rpx;
		
		.header-icon {
			font-size: 32rpx;
			margin-right: 15rpx;
		}
		
		.header-title {
			font-size: 32rpx;
			font-weight: bold;
			color: #333333;
			flex: 1;
		}
		
		.header-count {
			font-size: 24rpx;
			color: #0984e3;
			background: rgba(9, 132, 227, 0.1);
			padding: 8rpx 16rpx;
			border-radius: 20rpx;
		}
		
		.more-btn {
			display: flex;
			align-items: center;
			font-size: 26rpx;
			color: #0984e3;
			cursor: pointer;
			transition: all 0.3s ease;
			
			&:hover {
				transform: translateX(5rpx);
			}
			
			.arrow-icon {
				margin-left: 8rpx;
				font-size: 24rpx;
			}
		}
	}
}

/* 套餐信息卡片 */
.package-info-card {
	.package-tags {
		display: flex;
		flex-wrap: wrap;
		gap: 10rpx;
		margin-bottom: 20rpx;
		
		.tag {
			font-size: 24rpx;
			color: #0984e3;
			background: rgba(9, 132, 227, 0.1);
			padding: 8rpx 16rpx;
			border-radius: 20rpx;
			border: 2rpx solid rgba(9, 132, 227, 0.2);
			transition: all 0.3s ease;
			
			&:hover {
				background: rgba(9, 132, 227, 0.2);
				border-color: rgba(9, 132, 227, 0.4);
				transform: translateY(-2rpx);
			}
		}
	}
	
	.package-desc {
		.desc-text {
			font-size: 28rpx;
			color: #666666;
			line-height: 1.6;
		}
	}
}

/* 医院信息卡片 */
.hospital-card {
	background: rgba(255, 255, 255, 0.95);
	border-radius: 24rpx;
	margin: 20rpx 0;
	padding: 30rpx;
	box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
	backdrop-filter: blur(10rpx);
	animation: slideInUp 0.6s ease-out;
	transition: all 0.3s ease;
	cursor: pointer;
	
	&:hover {
		transform: translateY(-4rpx);
		box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.15);
	}
	
	.card-header {
		display: flex;
		align-items: center;
		justify-content: space-between;
		margin-bottom: 20rpx;
		
		.header-left {
			display: flex;
			align-items: center;
			
			.header-icon {
				font-size: 32rpx;
				margin-right: 15rpx;
			}
			
			.header-title {
				font-size: 32rpx;
				font-weight: bold;
				color: #333333;
			}
		}
		
		.header-right {
			.change-hospital-btn {
				background: linear-gradient(135deg, #0984e3, #74b9ff);
				color: white;
				border: none;
				padding: 12rpx 20rpx;
				border-radius: 20rpx;
				font-size: 24rpx;
				font-weight: 500;
				transition: all 0.3s ease;
				box-shadow: 0 4rpx 16rpx rgba(9, 132, 227, 0.3);
				
				&:hover {
					transform: translateY(-2rpx);
					box-shadow: 0 6rpx 20rpx rgba(9, 132, 227, 0.4);
				}
				
				&:active {
					transform: translateY(0);
				}
			}
		}
	}
	
	.hospital-content {
		display: flex;
		align-items: center;
		gap: 20rpx;
		
		.hospital-image {
			width: 120rpx;
			height: 120rpx;
			border-radius: 16rpx;
			object-fit: cover;
			box-shadow: 0 4rpx 16rpx rgba(0, 0, 0, 0.1);
		}
		
		.hospital-detail {
			flex: 1;
			
			.hospital-name {
				font-size: 28rpx;
				font-weight: bold;
				color: #333333;
				margin-bottom: 10rpx;
				display: block;
			}
			
			.hospital-address {
				display: flex;
				align-items: center;
				gap: 8rpx;
				
				.address-icon {
					font-size: 24rpx;
					color: #666666;
				}
				
				.address-text {
					font-size: 24rpx;
					color: #666666;
					flex: 1;
				}
			}
		}
		
		.hospital-arrow {
			.arrow-icon {
				font-size: 32rpx;
				color: #0984e3;
				transition: all 0.3s ease;
			}
		}
	}
}

/* 医院信息为空时的提示 */
.hospital-empty-card {
	background: rgba(255, 255, 255, 0.95);
	border-radius: 24rpx;
	margin: 20rpx 0;
	padding: 30rpx;
	box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
	backdrop-filter: blur(10rpx);
	animation: slideInUp 0.6s ease-out;
	transition: all 0.3s ease;
	
	&:hover {
		transform: translateY(-4rpx);
		box-shadow: 0 12rpx 40rpx rgba(0, 0, 0, 0.15);
	}
	
	.card-header {
		display: flex;
		align-items: center;
		margin-bottom: 20rpx;
		
		.header-icon {
			font-size: 32rpx;
			margin-right: 15rpx;
		}
		
		.header-title {
			font-size: 32rpx;
			font-weight: bold;
			color: #333333;
			flex: 1;
		}
	}
	
	.empty-content {
		.empty-text {
			font-size: 28rpx;
			color: #999999;
			margin-bottom: 10rpx;
		}
		
		.empty-desc {
			font-size: 24rpx;
			color: #666666;
			margin-bottom: 20rpx;
		}
		
		.select-hospital-btn {
			background: linear-gradient(135deg, #74b9ff, #0984e3);
			color: #ffffff;
			border: none;
			border-radius: 25rpx;
			padding: 15rpx 30rpx;
			font-size: 26rpx;
			transition: all 0.3s ease;
			
			&:hover {
				transform: translateY(-2rpx);
				box-shadow: 0 8rpx 24rpx rgba(116, 185, 255, 0.3);
			}
		}
	}
}

.section {
	margin-top: 20rpx;
	background-color: #ffffff;
	padding: 30rpx;
	
	.section-header {
		display: flex;
		justify-content: space-between;
		align-items: center;
		margin-bottom: 20rpx;
		
		.section-title {
			font-size: 32rpx;
			font-weight: bold;
			color: #333333;
			position: relative;
			padding-left: 20rpx;
			
			&::before {
				content: '';
				position: absolute;
				left: 0;
				top: 50%;
				transform: translateY(-50%);
				width: 6rpx;
				height: 30rpx;
				background-color: #1296db;
				border-radius: 3rpx;
			}
		}
		
		.item-count {
			font-size: 24rpx;
			color: #999999;
		}
		
		.more {
			display: flex;
			align-items: center;
			font-size: 26rpx;
			color: #999999;
			
			.iconfont {
				font-size: 24rpx;
				margin-left: 5rpx;
			}
		}
	}
}

.package-desc {
	.desc-text {
		font-size: 28rpx;
		color: #666666;
		line-height: 1.6;
	}
}

.item-list {
	.item-category {
		margin-bottom: 30rpx;
		
		&:last-child {
			margin-bottom: 0;
		}
		
		.category-header {
			display: flex;
			justify-content: space-between;
			align-items: center;
			padding: 15rpx 0;
			border-bottom: 1px solid #f5f5f5;
			margin-bottom: 15rpx;
			
			.category-name {
				font-size: 30rpx;
				font-weight: bold;
				color: #333333;
			}
			
			.category-count {
				font-size: 24rpx;
				color: #999999;
			}
		}
		
		.category-items {
			.item {
				display: flex;
				flex-direction: column;
				padding: 15rpx 0;
				border-bottom: 1px dashed #f5f5f5;
				
				&:last-child {
					border-bottom: none;
				}
				
				.item-name {
					font-size: 28rpx;
					color: #333333;
					margin-bottom: 10rpx;
				}
				
				.item-desc {
					font-size: 24rpx;
					color: #999999;
				}
			}
		}
	}
}



/* 底部按钮 */
.bottom-actions {
	position: fixed;
	bottom: 0;
	left: 0;
	right: 0;
	background: rgba(255, 255, 255, 0.95);
	backdrop-filter: blur(10rpx);
	padding: 15rpx 30rpx;
	box-shadow: 0 -8rpx 32rpx rgba(0, 0, 0, 0.1);
	z-index: 100;
	display: flex;
	gap: 20rpx;
	
	.action-btn {
		flex: 1;
		height: 80rpx;
		border: none;
		border-radius: 40rpx;
		font-size: 28rpx;
		font-weight: bold;
		display: flex;
		align-items: center;
		justify-content: center;
		transition: all 0.3s ease;
		
		.btn-icon {
			font-size: 28rpx;
			margin-right: 8rpx;
			transition: all 0.3s ease;
		}
		
		.btn-text {
			font-size: 28rpx;
			transition: all 0.3s ease;
		}
		
		&::after {
			border: none;
		}
		
		&.consult-btn {
			background: rgba(116, 185, 255, 0.1);
			color: #0984e3;
			border: 2rpx solid rgba(9, 132, 227, 0.3);
			
			&:hover {
				background: rgba(116, 185, 255, 0.2);
				border-color: rgba(9, 132, 227, 0.5);
				transform: translateY(-2rpx);
			}
		}
		
		&.appointment-btn {
			background: linear-gradient(135deg, #74b9ff, #0984e3);
			color: #ffffff;
			box-shadow: 0 8rpx 24rpx rgba(116, 185, 255, 0.3);
			
			&:hover {
				transform: translateY(-4rpx);
				box-shadow: 0 12rpx 32rpx rgba(116, 185, 255, 0.5);
				background: linear-gradient(135deg, #0984e3, #74b9ff);
			}
		}
	}
}

/* 检查项目卡片 */
.checkitems-card {
	.checkitems-content {
		.category-section {
			margin-bottom: 30rpx;
			
			&:last-child {
				margin-bottom: 0;
			}
			
			.category-header {
				display: flex;
				justify-content: space-between;
				align-items: center;
				padding: 15rpx 0;
				border-bottom: 1rpx solid #f0f0f0;
				margin-bottom: 20rpx;
				
				.category-name {
					font-size: 30rpx;
					font-weight: bold;
					color: #333333;
				}
				
				.category-count {
					font-size: 24rpx;
					color: #0984e3;
					background: rgba(9, 132, 227, 0.1);
					padding: 6rpx 12rpx;
					border-radius: 15rpx;
				}
			}
			
			.items-list {
				.item-item {
					display: flex;
					align-items: flex-start;
					padding: 15rpx 0;
					border-bottom: 1rpx dashed #f0f0f0;
					transition: all 0.3s ease;
					
					&:last-child {
						border-bottom: none;
					}
					
					&:hover {
						background: rgba(9, 132, 227, 0.05);
						border-radius: 12rpx;
						padding: 15rpx 10rpx;
						margin: 0 -10rpx;
					}
					
					.item-icon {
						font-size: 24rpx;
						color: #0984e3;
						margin-right: 15rpx;
						margin-top: 4rpx;
						transition: all 0.3s ease;
					}
					
					.item-info {
						flex: 1;
						
						.item-name {
							font-size: 28rpx;
							color: #333333;
							margin-bottom: 8rpx;
							font-weight: 500;
							display: block;
						}
						
						.item-desc {
							font-size: 24rpx;
							color: #666666;
							line-height: 1.4;
							display: block;
						}
					}
				}
			}
		}
	}
}

/* 适用人群卡片 */
.suitable-card {
	.suitable-content {
		.suitable-text {
		font-size: 28rpx;
		color: #666666;
		line-height: 1.6;
		}
	}
}

/* 预约须知卡片 */
.notice-card {
	.notice-content {
	.notice-item {
		display: flex;
		align-items: flex-start;
		margin-bottom: 15rpx;
		
		&:last-child {
			margin-bottom: 0;
		}
		
		.notice-dot {
			width: 12rpx;
			height: 12rpx;
			border-radius: 50%;
				background-color: #0984e3;
			margin-right: 15rpx;
			margin-top: 12rpx;
				flex-shrink: 0;
		}
		
		.notice-text {
			flex: 1;
			font-size: 26rpx;
			color: #666666;
			line-height: 1.6;
			}
		}
	}
}

/* 用户评价卡片 */
.reviews-card {
	.reviews-content {
	.review-item {
		margin-bottom: 30rpx;
		
		&:last-child {
			margin-bottom: 0;
		}
		
		.reviewer-info {
			display: flex;
			align-items: center;
				justify-content: space-between;
			margin-bottom: 15rpx;
			
			.reviewer-detail {
				flex: 1;
				
				.reviewer-name {
					font-size: 28rpx;
					color: #333333;
					margin-bottom: 5rpx;
						display: block;
				}
				
				.review-time {
					font-size: 22rpx;
					color: #999999;
						display: block;
				}
			}
			
			.review-rating {
					.star {
					font-size: 24rpx;
						color: #ff9500;
						margin-left: 2rpx;
					}
					
					.star-empty {
						font-size: 24rpx;
						color: #dddddd;
						margin-left: 2rpx;
				}
			}
		}
		
		.review-content {
			font-size: 26rpx;
			color: #666666;
			line-height: 1.6;
		}
	}
}

.empty-reviews {
	padding: 50rpx 0;
	text-align: center;
	
	.empty-text {
		font-size: 28rpx;
		color: #999999;
	}
}
}

/* 动画关键帧 */
@keyframes float {
	0% {
		transform: translateY(0) translateX(0) scale(1);
		opacity: 0.8;
	}
	25% {
		transform: translateY(-10px) translateX(10px) scale(1.05);
		opacity: 0.9;
	}
	50% {
		transform: translateY(5px) translateX(-5px) scale(1.02);
		opacity: 1;
	}
	75% {
		transform: translateY(-5px) translateX(5px) scale(1.03);
		opacity: 0.9;
	}
	100% {
		transform: translateY(0) translateX(0) scale(1);
		opacity: 0.8;
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
		
@keyframes slideInUp {
	from {
		opacity: 0;
		transform: translateY(30rpx);
	}
	to {
		opacity: 1;
		transform: translateY(0);
	}
}

/* 加载状态卡片 */
.loading-card {
	background: rgba(255, 255, 255, 0.95);
	border-radius: 24rpx;
	margin: 20rpx 0;
	padding: 60rpx 30rpx;
	box-shadow: 0 8rpx 32rpx rgba(0, 0, 0, 0.1);
	backdrop-filter: blur(10rpx);
	animation: slideInUp 0.6s ease-out;
	
	.loading-content {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		
		.loading-spinner {
			width: 60rpx;
			height: 60rpx;
			border: 4rpx solid rgba(9, 132, 227, 0.2);
			border-top: 4rpx solid #0984e3;
			border-radius: 50%;
			animation: spin 1s linear infinite;
			margin-bottom: 20rpx;
		}
		
		.loading-text {
			font-size: 28rpx;
			color: #666666;
		}
	}
}

@keyframes spin {
	0% { transform: rotate(0deg); }
	100% { transform: rotate(360deg); }
}

/* 套餐横幅 */
</style>