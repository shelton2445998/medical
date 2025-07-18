/**
 * Vite 环境类型声明文件
 * 为 Vite 客户端和 .mjs 文件提供类型支持
 */

/// <reference types="vite/client" />

/**
 * 声明 .mjs 模块
 * 允许在 TypeScript 中导入 .mjs 文件
 */
declare module "*.mjs"
