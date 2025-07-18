<!--
/**
 * @fileoverview 文件上传组件
 * @description 提供文件上传功能，支持单文件和多文件上传，包括图片预览、进度显示等
 * @author 医疗预约系统开发团队
 * @version 1.0.0
 * @created 2024-01-01
 * @features
 * - 文件上传功能
 * - 图片预览
 * - 上传进度显示
 * - 文件类型限制
 * - 文件大小限制
 * - 多文件上传
 * - 上传状态管理
 */
-->
<template>
  <!-- Element Plus上传组件 -->
  <el-upload
      :before-upload="beforeAvatarUpload"
      :http-request="uploadAvatar"
      :on-success="uploadSuccess"
      :show-file-list="false"
      class="uploader"
  >
    <!-- 已上传文件的预览 -->
    <img :style="customStyle" v-if="data.imageUrl" :src="data.imageUrl" class="uploader-img"/>
    <!-- 上传区域默认显示 -->
    <slot name="tip" v-else>
      <div class="uploader-default flex-center flex-column" :style="customStyle">
        <el-icon class="uploader-icon">
          <ele-upload-filled/>
        </el-icon>
        <div class="mt5">上传</div>
      </div>
    </slot>
  </el-upload>
</template>

<script lang="ts" setup>
/**
 * 文件上传组件逻辑
 * 提供文件上传、类型验证、大小限制等功能
 */
import type {UploadProps, UploadRequestOptions} from 'element-plus'
import {ElMessage} from 'element-plus'
import {uploadApi} from "@/api";

/**
 * 支持的文件类型配置
 * 定义不同文件类型对应的扩展名
 */
const typeList:Record<string, string[]>={
  image:['jpeg','jpg','png'],    // 图片类型
  head:['jpeg','jpg','png'],     // 头像类型
  word:['doc','docx'],           // Word文档类型
  excel:['xls','xlsx'],          // Excel文档类型
  pdf:['pdf'],                   // PDF文档类型
}

/**
 * 组件属性定义
 */
const props = withDefaults(defineProps<{
  value: NullType<string>,       // 当前文件URL
  customStyle?: any,             // 自定义样式
  size?: number                  // 文件大小限制（MB）
  type?: string                  // 文件类型限制
}>(), {
  value: '',
  type:'any',                    // 默认允许任何类型
  size:5,                        // 默认5MB限制
  customStyle:{}
})

/**
 * 组件事件定义
 */
const emits = defineEmits<{
  (event: 'update:value', val: any): void    // 更新值事件
  (event: 'change', url: string): void       // 文件改变事件
}>()

/**
 * 组件响应式数据
 */
const data = reactive({
  // 图片URL计算属性，实现双向绑定
  imageUrl: computed({
    get: () => {
      return props.value
    },
    set: val => {
      emits('update:value', val);
    }
  })
})

/**
 * 上传成功回调
 * @param response 上传接口返回的响应
 */
const uploadSuccess: UploadProps['onSuccess'] = (
    response,
) => {
  data.imageUrl = response.url;
  emits('change', response.url);
}

/**
 * 自定义上传方法
 * @param options 上传选项参数
 * @returns 上传请求的Promise
 */
const uploadAvatar = (options: UploadRequestOptions) => {
  return uploadApi({
    file: options.file,
    type:props.type
  })
}

/**
 * 上传前的文件验证
 * @param rawFile 原始文件对象
 * @returns 验证结果，true表示通过，false表示拒绝
 */
const beforeAvatarUpload: UploadProps['beforeUpload'] = (rawFile) => {
  // 获取文件扩展名
  const extension= rawFile.name.split('.').pop() as string;
  
  // 验证文件类型
  if(props.type!=='any'&&typeList[props.type].indexOf(extension)==-1){
      ElMessage.error('抱歉，文件格式不正确')
      return false;
  }
  // 验证文件大小
  else if (rawFile.size / 1024 / 1024 > props.size) {
    ElMessage.error(`抱歉，暂时不支持大于${props.size}M的文件上传`)
    return false;
  }
  return true
}

/**
 * 文件预览处理
 * @param uploadFile 上传文件对象
 */
const onPreview: UploadProps['onPreview'] = (uploadFile) => {
  // 预览功能待实现
}
</script>

<style lang="scss">
/**
 * 上传组件样式
 * 定义上传区域的外观和交互效果
 */
.uploader {
  .el-upload {
    border: 1px dashed var(--el-border-color);
    cursor: pointer;
    position: relative;
    overflow: hidden;
    transition: var(--el-transition-duration-fast);
    border-radius: 6px !important;

    /* 悬停效果 */
    &:hover {
      border-color: var(--el-color-primary);
    }
  }

  /* 已上传图片的样式 */
  .uploader-img {
    width: 150px;
    height: 150px;
    display: block;
  }

  /* 默认上传区域样式 */
  .uploader-default {
    color: #8c939d;
    width: 150px;
    height: 150px;
    font-size: 14px;
    
    /* 上传图标样式 */
    .uploader-icon{
      font-size: 40px;
    }
  }
}
</style>
