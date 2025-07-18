<!--
  HelloWorld 示例组件
  
  Vue CLI 生成的默认示例组件，用于展示 Vue.js 相关信息和链接
  主要用于开发阶段的参考和学习
  
  @author Vue CLI
  @date 2024
  @version 1.0.0
-->
<template>
  <div class="hello">
    <!-- 动态标题显示 -->
    <h1>{{ msg }}</h1>
    
    <!-- Vue CLI 文档介绍 -->
    <p>
      For a guide and recipes on how to configure / customize this project,<br>
      check out the
      <a href="https://cli.vuejs.org" target="_blank" rel="noopener">vue-cli documentation</a>.
    </p>
    
    <!-- 已安装的 CLI 插件列表 -->
    <h3>Installed CLI Plugins</h3>
    <ul>
      <li><a href="https://github.com/vuejs/vue-cli/tree/dev/packages/%40vue/cli-plugin-babel" target="_blank" rel="noopener">babel</a></li>
      <li><a href="https://github.com/vuejs/vue-cli/tree/dev/packages/%40vue/cli-plugin-eslint" target="_blank" rel="noopener">eslint</a></li>
    </ul>
    
    <!-- Vue.js 核心文档链接 -->
    <h3>Essential Links</h3>
    <ul>
      <li><a href="https://vuejs.org" target="_blank" rel="noopener">Core Docs</a></li>
      <li><a href="https://forum.vuejs.org" target="_blank" rel="noopener">Forum</a></li>
      <li><a href="https://chat.vuejs.org" target="_blank" rel="noopener">Community Chat</a></li>
      <li><a href="https://twitter.com/vuejs" target="_blank" rel="noopener">Twitter</a></li>
      <li><a href="https://news.vuejs.org" target="_blank" rel="noopener">News</a></li>
    </ul>
    
    <!-- Vue.js 生态系统相关链接 -->
    <h3>Ecosystem</h3>
    <ul>
      <li><a href="https://router.vuejs.org" target="_blank" rel="noopener">vue-router</a></li>
      <li><a href="https://vuex.vuejs.org" target="_blank" rel="noopener">vuex</a></li>
      <li><a href="https://github.com/vuejs/vue-devtools#vue-devtools" target="_blank" rel="noopener">vue-devtools</a></li>
      <li><a href="https://vue-loader.vuejs.org" target="_blank" rel="noopener">vue-loader</a></li>
      <li><a href="https://github.com/vuejs/awesome-vue" target="_blank" rel="noopener">awesome-vue</a></li>
    </ul>
  </div>
</template>

<script>
/**
 * HelloWorld 组件
 * 
 * Vue CLI 生成的默认示例组件
 * 接收一个 msg 属性用于显示标题
 * 
 * @component
 * @example
 * <HelloWorld msg="Welcome to Your Vue.js App"/>
 */
export default {
  name: 'HelloWorld',
  props: {
    /**
     * 消息属性
     * 
     * 用于在组件中显示的标题文本
     * 
     * @type {String}
     * @required
     */
    msg: String
  }
}
</script>

<!-- 限制 CSS 样式只对当前组件有效 -->
<style scoped>
/* 三级标题样式 */
h3 {
  margin: 40px 0 0;
}

/* 列表样式重置 */
ul {
  list-style-type: none;  /* 移除列表符号 */
  padding: 0;             /* 移除默认内边距 */
}

/* 列表项样式 */
li {
  display: inline-block;  /* 横向排列 */
  margin: 0 10px;         /* 设置间距 */
}

/* 链接样式 */
a {
  color: #42b983;         /* 设置 Vue.js 品牌色 */
}
</style>
