<template>
  <view class="content">
    <view class="page-header">
      <view class="back-btn" @click="goBack">
        <text class="iconfont icon-back"></text>
      </view>
      <!-- <text class="page-title">资讯详情</text> -->
    </view>
    
    <view class="news-detail" v-if="newsDetail">
      <view class="news-title">{{newsDetail.title}}</view>
      <view class="news-meta">
        <text class="news-source">{{newsDetail.source}}</text>
        <text class="news-time">{{newsDetail.time}}</text>
      </view>
      <image :src="newsDetail.image" mode="widthFix" class="news-image"></image>
      <view class="news-content">
        <rich-text :nodes="newsDetail.content"></rich-text>
      </view>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      newsDetail: null
    }
  },
  onLoad(options) {
    if (options.id) {
      this.getNewsDetail(options.id);
    }
  },
  methods: {
    getNewsDetail(id) {
      // 模拟获取新闻详情
      // 实际项目中这里应该是 API 调用
      const newsMap = {
		5: {
			id: 5,
			title: '健康早餐的5个标准，你达标了吗？',
			image: '/static/images/news5.jpg',
			source: '生活健康',
			time: '2023-07-03',
			content: `
			  <p><strong>早餐是一天中最重要的一餐</strong>，但很多人却忽略了它的营养搭配。一个健康的早餐应该包含以下5个标准：</p>
			  <p><strong>1. 含有优质蛋白质</strong>：如鸡蛋、牛奶、豆浆，有助于维持饱腹感和肌肉健康。</p>
			  <p><strong>2. 含有复合碳水化合物</strong>：如全麦面包、燕麦，提供持续能量。</p>
			  <p><strong>3. 含有健康脂肪</strong>：如坚果、牛油果，有助于心脑血管健康。</p>
			  <p><strong>4. 含有膳食纤维</strong>：如水果、蔬菜，促进肠道蠕动。</p>
			  <p><strong>5. 控制糖分摄入</strong>：避免甜饮料、糕点等高糖食物。</p>
			  <p>如果你的早餐能满足以上5点，那么恭喜你，已经迈出了健康生活的重要一步！</p>
			`
		}
        // ... 其他新闻详情
      };
      
      this.newsDetail = newsMap[id];
    },
    goBack() {
      uni.navigateBack();
    }
  }
}
</script>

<style lang="scss">
/* 样式与你的其他页面保持一致 */
.page-header {
  /* 同上 */
}

.news-detail {
  padding: 30rpx;
  
  .news-title {
    font-size: 36rpx;
    font-weight: bold;
    color: #333333;
    margin-bottom: 20rpx;
  }
  
  .news-meta {
    display: flex;
    justify-content: space-between;
    font-size: 24rpx;
    color: #999999;
    margin-bottom: 30rpx;
    
    .news-source {
      color: #1296db;
    }
  }
  
  .news-image {
    width: 100%;
    border-radius: 10rpx;
    margin-bottom: 30rpx;
  }
  
  .news-content {
    font-size: 30rpx;
    line-height: 1.8;
    color: #333333;
  }
}
</style>