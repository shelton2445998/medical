/**
 * Vite 构建配置文件
 * 配置项目的构建、开发服务器、插件等
 */
import {defineConfig,ConfigEnv,loadEnv} from 'vite'
import vue from '@vitejs/plugin-vue'
import path,{resolve} from "path";
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'
import AutoImport from "unplugin-auto-import/vite";
import Components from "unplugin-vue-components/vite";

/**
 * 路径查找函数
 * @param dir 相对路径
 * @returns 绝对路径
 */
const pathResolve = (dir: string): string => {
    return resolve(__dirname, ".", dir);
};

/**
 * 设置路径别名
 * 简化导入路径，提高开发效率
 */
const alias: Record<string, string> = {
    "@": pathResolve("src"),
};

/**
 * 导出 Vite 配置
 * 使用函数形式以支持环境变量
 */
export default defineConfig((mode: ConfigEnv)=>{
    // 加载环境变量
    const env = loadEnv(mode.mode, process.cwd());
    
    return {
        /**
         * 插件配置
         * 包含 Vue 支持、自动导入、SVG 图标等功能
         */
        plugins: [
            // Vue 单文件组件支持
            vue(),
            
            // 自动导入 Vue、Vue Router、Pinia 等常用 API
            AutoImport({
                imports: ["vue","vue-router","pinia"],
                // 生成类型声明文件
                dts: "src/types/auto-imports.d.ts",
            }),
            
            // 自动注册组件
            Components({
                // 生成组件类型声明文件
                dts: "src/types/components.d.ts",
            }),
            
            // SVG 图标插件配置
            createSvgIconsPlugin({
                // 指定需要缓存的图标文件夹
                iconDirs: [path.resolve(process.cwd(), 'src/assets/svg')],
                // 指定 symbolId 格式
                symbolId: 'local-[dir]-[name]',

                /**
                 * 自定义插入位置
                 * @default: body-last
                 */
                // inject?: 'body-last' | 'body-first'

                /**
                 * 自定义 DOM ID
                 * @default: __svg__icons__dom__
                 */
                //customDomId: '__svg__icons__dom__',
            }),
        ],
        
        // 项目根目录
        root: process.cwd(),
        
        // 公共基础路径
        base: env.VITE_PUBLIC_PATH,
        
        // 路径解析配置
        resolve: {
            alias
        },
        
        /**
         * 开发服务器配置
         * 设置主机、端口、代理等
         */
        server: {
            // 监听所有网络接口，支持外部访问
            host: '0.0.0.0',
            // 端口号从环境变量获取
            port: env.VITE_PORT as unknown as number,
            // 是否自动打开浏览器
            open: JSON.parse(env.VITE_OPEN),
            // 代理配置
            proxy: {
                // API 请求代理到后端服务
                '/api': {
                    // 后端服务地址
                    target: 'http://localhost:8888',
                    // 改变请求头中的 origin 字段
                    changeOrigin: true,
                    // 后端已经有 /api 前缀，所以不需要重写路径
                    // rewrite: (path) => path.replace(/^\/api/, '')
                }
            }
        }
    }
})
