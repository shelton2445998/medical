# 医院管理系统 API 集成说明

## 概述

本项目已集成后端医院管理系统的 API 接口，主要功能包括：

- 获取推荐医院列表
- 搜索医院
- 获取医院详情
- 医院预约管理

## 后端接口

### 医院相关接口

#### 1. 获取推荐医院
- **接口地址**: `GET /hospital/recommend`
- **功能**: 获取推荐医院列表
- **返回数据**: `List<AppHospitalVo>`

#### 2. 获取医院列表
- **接口地址**: `GET /hospital/list`
- **功能**: 分页获取医院列表，支持搜索
- **参数**: 
  - `name`: 医院名称（可选）
  - `pageNum`: 页码
  - `pageSize`: 每页数量
- **返回数据**: `Paging<AppHospitalVo>`

#### 3. 获取医院详情
- **接口地址**: `GET /hospital/detail/{id}`
- **功能**: 根据医院ID获取详细信息
- **参数**: `id` - 医院ID
- **返回数据**: `AppHospitalVo`

## 前端配置

### API 配置文件

项目使用统一的 API 配置文件 `utils/api.js`：

```javascript
const API_BASE_URL = 'http://localhost:8080';

export const hospitalApi = {
  getRecommendHospitals: `${API_BASE_URL}/hospital/recommend`,
  getHospitalList: `${API_BASE_URL}/hospital/list`,
  getHospitalDetail: (id) => `${API_BASE_URL}/hospital/detail/${id}`,
};
```

### 请求工具函数

项目使用统一的请求工具函数 `utils/request.js`：

```javascript
import { get, post } from '@/utils/request.js';

// 获取推荐医院
const result = await get(hospitalApi.getRecommendHospitals);
```

## 数据格式

### AppHospitalVo 数据结构

```java
public class AppHospitalVo {
    private Long id;           // 医院ID
    private String name;       // 医院名称
    private String phone;      // 医院电话
    private String address;    // 医院地址
    private String description; // 医院简介
    private Boolean status;    // 状态
    // ... 其他字段
}
```

### 前端数据处理

前端会自动处理以下字段：

- **图片**: 如果后端没有返回图片，前端会使用默认图片 `/static/images/hospital1.jpg`
- **标签**: 如果后端没有返回标签，前端会设置默认标签 `['综合医院']`
- **地址**: 如果后端没有返回地址，前端会显示 `'地址信息待完善'`

## 使用示例

### 在页面中调用 API

```javascript
import { get, hospitalApi } from '@/utils/request.js';

export default {
  data() {
    return {
      hospitalList: [],
      hospitalLoading: false
    }
  },
  methods: {
    async getRecommendHospitals() {
      this.hospitalLoading = true;
      try {
        const result = await get(hospitalApi.getRecommendHospitals);
        this.hospitalList = result.data || [];
        // 处理数据...
      } catch (error) {
        console.error('获取医院失败:', error);
      } finally {
        this.hospitalLoading = false;
      }
    }
  }
}
```

## 错误处理

### 网络错误
- 如果接口请求失败，会显示错误提示
- 在开发环境中，如果接口不可用，会使用测试数据

### 数据验证
- 前端会自动验证返回数据的完整性
- 缺失的字段会使用默认值填充

## 开发注意事项

1. **后端服务**: 确保后端服务运行在正确的地址
   - 开发环境: `http://localhost:8888/api`
   - 生产环境: `http://39.104.57.236:8888/api`
2. **跨域问题**: 后端需要配置 CORS 支持前端跨域请求
3. **数据格式**: 确保后端返回的数据格式与前端期望的格式一致
4. **错误处理**: 建议在后端也添加适当的错误处理和日志记录

## 服务器配置

### 后端服务配置
- **端口**: 8888
- **上下文路径**: `/api`
- **完整地址**: `http://39.104.57.236:8888/api`

### Redis 配置
- **地址**: 39.104.57.236
- **端口**: 6379
- **注意**: 这是数据库端口，不是 HTTP 服务端口

## 常见问题排查

### 1. 接口请求失败
- 检查后端服务是否启动
- 检查接口地址是否正确
- 检查网络连接是否正常
- 查看浏览器控制台的错误信息

### 2. 数据格式不匹配
- 后端返回的数据结构可能与前端期望的不一致
- 检查 `ApiResult` 包装的数据结构
- 检查分页数据的 `records` 字段

### 3. 参数不匹配
- 前端发送的参数名与后端期望的不一致
- 分页参数：后端使用 `pageIndex`，前端发送 `pageNum`
- 搜索参数：后端使用 `keyword`，前端发送 `name`

### 4. 测试接口
可以使用测试页面 `/pages/test-api/test-api.vue` 来验证接口是否正常工作

## 测试数据

如果后端接口不可用，前端会使用以下测试数据：

```javascript
[
  {
    id: 1,
    name: '沈阳市云医院-和平分院',
    image: '/static/images/hospital1.jpg',
    tags: ['三甲', '综合医院'],
    address: '沈阳市和平区南京南街61号'
  },
  {
    id: 2,
    name: '沈阳市云医院-沈河分院',
    image: '/static/images/hospital2.jpg',
    tags: ['三甲', '综合医院'],
    address: '沈阳市沈河区北站路33号'
  }
]
```

## 后续扩展

1. **认证授权**: 可以添加 JWT 认证
2. **缓存机制**: 可以添加数据缓存提高性能
3. **图片上传**: 可以添加医院图片上传功能
4. **更多字段**: 可以在后端添加更多医院信息字段 