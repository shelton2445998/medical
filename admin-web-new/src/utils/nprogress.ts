/**
 * 进度条工具模块
 * 封装NProgress进度条的配置和操作方法
 */
import NProgress from 'nprogress'
import 'nprogress/nprogress.css'

/**
 * NProgress配置
 * 自定义进度条的样式和行为
 */
NProgress.configure({
    easing: 'ease',         // 动画方式
    speed: 500,             // 递增进度条的速度
    showSpinner: false,     // 是否显示加载icon
    trickleSpeed: 200,      // 自动递增间隔
    minimum: 0.3,           // 更改启动时使用的最小百分比
    parent: 'body',         // 指定进度条的父容器
})

/**
 * 开启进度条
 * 在路由切换或异步操作开始时调用
 */
export const startNProgress = () => {
    NProgress.start();
};

/**
 * 关闭进度条
 * 在路由切换或异步操作完成时调用
 */
export const closeNProgress = () => {
    NProgress.done();
};
