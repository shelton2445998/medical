# 体检预约系统接口文档

本文档整理了医疗体检预约系统中的接口，包括管理后台、医生端和用户端的所有API接口。


## 管理后台接口

### 认证相关

| 接口名称 | 请求方法 | URL | 描述 |
| ------ | ------ | --- | ---- |
| 管理员登录 | POST | `/api/admin/login` | 管理员登录系统 |
| 退出登录 | POST | `/api/admin/logout` | 管理员退出系统 |
| 获取管理员信息 | GET | `/api/admin/info` | 获取当前登录的管理员信息 |

### 仪表盘

| 接口名称 | 请求方法 | URL | 描述 |
| ------ | ------ | --- | ---- |
| 获取仪表盘数据 | GET | `/api/admin/dashboard` | 获取管理后台首页仪表盘数据 |

### 用户管理

| 接口名称 | 请求方法 | URL | 描述 |
| ------ | ------ | --- | ---- |
| 获取用户列表 | GET | `/api/admin/user/list` | 获取用户列表，支持分页 |
| 添加用户 | POST | `/api/admin/user/add` | 添加新用户 |
| 更新用户 | PUT | `/api/admin/user/update` | 更新用户信息 |
| 获取用户详情 | GET | `/api/admin/user/detail/{userId}` | 获取指定用户的详细信息 |
| 更新用户状态 | PUT | `/api/admin/user/status` | 启用或禁用用户账号 |
| 重置用户密码 | PUT | `/api/admin/user/reset-password` | 重置用户密码 |

### 医生管理

| 接口名称 | 请求方法 | URL | 描述 |
| ------ | ------ | --- | ---- |
| 获取医生列表 | GET | `/api/admin/doctor/list` | 获取医生列表，支持分页 |
| 添加医生 | POST | `/api/admin/doctor/add` | 添加新医生 |
| 更新医生 | PUT | `/api/admin/doctor/update` | 更新医生信息 |
| 获取医生详情 | GET | `/api/admin/doctor/detail/{doctorId}` | 获取指定医生的详细信息 |
| 更新医生状态 | PUT | `/api/admin/doctor/status` | 启用或禁用医生账号 |
| 重置医生密码 | PUT | `/api/admin/doctor/reset-password` | 重置医生密码 |

### 体检套餐管理

| 接口名称 | 请求方法 | URL | 描述 |
| ------ | ------ | --- | ---- |
| 获取套餐列表 | GET | `/api/admin/package/list` | 获取体检套餐列表，支持分页 |
| 添加套餐 | POST | `/api/admin/package/add` | 添加新体检套餐 |
| 更新套餐 | PUT | `/api/admin/package/update` | 更新体检套餐信息 |
| 获取套餐详情 | GET | `/api/admin/package/detail/{packageId}` | 获取指定套餐的详细信息 |
| 更新套餐状态 | PUT | `/api/admin/package/status` | 上架或下架体检套餐 |
| 删除套餐 | DELETE | `/api/admin/package/delete/{packageId}` | 删除体检套餐 |

### 体检项目管理

| 接口名称 | 请求方法 | URL | 描述 |
| ------ | ------ | --- | ---- |
| 获取项目列表 | GET | `/api/admin/check-item/list` | 获取体检项目列表，支持分页 |
| 添加项目 | POST | `/api/admin/check-item/add` | 添加新体检项目 |
| 更新项目 | PUT | `/api/admin/check-item/update` | 更新体检项目信息 |
| 获取项目详情 | GET | `/api/admin/check-item/detail/{itemId}` | 获取指定项目的详细信息 |
| 更新项目状态 | PUT | `/api/admin/check-item/status` | 启用或禁用体检项目 |
| 删除项目 | DELETE | `/api/admin/check-item/delete/{itemId}` | 删除体检项目 |

### 医院管理

| 接口名称 | 请求方法 | URL | 描述 |
| ------ | ------ | --- | ---- |
| 获取医院列表 | GET | `/api/admin/hospital/list` | 获取医院列表，支持分页 |
| 添加医院 | POST | `/api/admin/hospital/add` | 添加新医院 |
| 更新医院 | PUT | `/api/admin/hospital/update` | 更新医院信息 |
| 获取医院详情 | GET | `/api/admin/hospital/detail/{hospitalId}` | 获取指定医院的详细信息 |
| 更新医院状态 | PUT | `/api/admin/hospital/status` | 启用或禁用医院 |

### 预约管理

| 接口名称 | 请求方法 | URL | 描述 |
| ------ | ------ | --- | ---- |
| 获取预约列表 | GET | `/api/admin/appointment/list` | 获取预约列表，支持分页 |
| 获取预约详情 | GET | `/api/admin/appointment/detail/{appointmentId}` | 获取指定预约的详细信息 |
| 更新预约状态 | PUT | `/api/admin/appointment/status` | 更新预约状态 |

### 财务管理

| 接口名称 | 请求方法 | URL | 描述 |
| ------ | ------ | --- | ---- |
| 获取财务统计 | GET | `/api/admin/finance/statistics` | 获取财务统计数据 |
| 获取财务列表 | GET | `/api/admin/finance/list` | 获取财务记录列表，支持分页 |

### 系统设置

| 接口名称 | 请求方法 | URL | 描述 |
| ------ | ------ | --- | ---- |
| 获取系统设置 | GET | `/api/admin/settings` | 获取系统设置信息 |
| 更新系统设置 | PUT | `/api/admin/settings` | 更新系统设置信息 |

## 医生端接口


### 认证相关

| 接口名称 | 请求方法 | URL | 描述 |
| ------ | ------ | --- | ---- |
| 医生登录 | POST | `/api/doctor/login` | 医生登录系统 |
| 退出登录 | POST | `/api/doctor/logout` | 医生退出系统 |
| 获取医生信息 | GET | `/api/doctor/info` | 获取当前登录的医生信息 |

### 预约管理

| 接口名称 | 请求方法 | URL | 描述 |
| ------ | ------ | --- | ---- |
| 获取预约列表 | GET | `/api/doctor/appointment/list` | 获取分配给当前医生的预约列表 |
| 获取预约详情 | GET | `/api/doctor/appointment/detail/{id}` | 获取指定预约的详细信息 |
| 更新体检结果 | POST | `/api/doctor/appointment/result` | 提交患者的体检结果 |

### 体检报告

| 接口名称 | 请求方法 | URL | 描述 |
| ------ | ------ | --- | ---- |
| 获取报告列表 | GET | `/api/doctor/report/list` | 获取医生已完成的体检报告列表 |
| 获取报告详情 | GET | `/api/doctor/report/detail/{id}` | 获取指定体检报告的详细信息 |
| 更新报告 | PUT | `/api/doctor/report/update` | 更新体检报告内容 |

## 用户端接口

用户端应用（用户手机APP）的接口

### 用户认证

| 接口名称 | 请求方法 | URL | 描述 |
| ------ | ------ | --- | ---- |
| 用户注册 | POST | `/api/user/register` | 新用户注册 |
| 用户登录 | POST | `/api/user/login` | 用户登录 |
| 更新用户信息 | PUT | `/api/user/update` | 更新用户个人资料 |
| 修改密码 | PUT | `/api/user/password` | 修改用户密码 |

### 体检套餐

| 接口名称 | 请求方法 | URL | 描述 |
| ------ | ------ | --- | ---- |
| 获取套餐列表 | GET | `/api/package/list` | 获取可预约的体检套餐列表 |
| 获取套餐详情 | GET | `/api/package/detail/{id}` | 获取体检套餐详细信息 |
| 获取推荐套餐 | GET | `/api/package/recommend` | 获取推荐的体检套餐 |

### 医院

| 接口名称 | 请求方法 | URL | 描述 |
| ------ | ------ | --- | ---- |
| 获取医院列表 | GET | `/api/hospital/list` | 获取可预约的医院列表 |
| 获取医院详情 | GET | `/api/hospital/detail/{id}` | 获取医院详细信息 |
| 获取推荐医院 | GET | `/api/hospital/recommend` | 获取推荐的医院 |

### 预约

| 接口名称 | 请求方法 | URL | 描述 |
| ------ | ------ | --- | ---- |
| 创建预约 | POST | `/api/appointment/create` | 创建新的体检预约 |
| 获取预约列表 | GET | `/api/appointment/list` | 获取当前用户的预约列表 |
| 获取预约详情 | GET | `/api/appointment/detail/{id}` | 获取预约详情 |
| 取消预约 | PUT | `/api/appointment/cancel/{id}` | 取消已创建的预约 |

### 体检报告

| 接口名称 | 请求方法 | URL | 描述 |
| ------ | ------ | --- | ---- |
| 获取报告列表 | GET | `/api/report/list` | 获取用户的体检报告列表 |
| 获取报告详情 | GET | `/api/report/detail/{id}` | 获取体检报告详情 |

### 支付

| 接口名称 | 请求方法 | URL | 描述 |
| ------ | ------ | --- | ---- |
| 创建支付订单 | POST | `/api/payment/create` | 创建支付订单 |
| 查询支付状态 | GET | `/api/payment/status/{orderId}` | 查询支付订单状态 |
| 获取支付历史 | GET | `/api/payment/history` | 获取用户支付历史记录 |

## 请求/响应格式说明

### 通用响应格式

```json
{
  "code": 200,           // 状态码，200表示成功，其他值表示失败
  "message": "操作成功",  // 状态描述
  "data": {              // 响应数据
    // 具体的业务数据
  }
}
```

### 分页响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "list": [],         // 数据列表
    "total": 100,       // 总记录数
    "pageSize": 10,     // 每页条数
    "pageNum": 1        // 当前页码
  }
}
```

### 错误响应格式

```json
{
  "code": 400,          // 错误码
  "message": "参数错误", // 错误描述
  "data": null
}
```

## 常见错误码

| 错误码 | 描述 |
| ----- | ---- |
| 200 | 成功 |
| 400 | 参数错误 |
| 401 | 未授权，请登录 |
| 403 | 权限不足，禁止访问 |
| 404 | 请求的资源不存在 |
| 500 | 服务器内部错误 |

## 认证方式

系统使用基于 JWT（JSON Web Token）的认证方式。客户端在登录成功后获取 token，并在后续请求的 HTTP 头部 Authorization 字段中携带该 token。

示例：
```
Authorization: Bearer {token}
``` 