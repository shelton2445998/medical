# ҽԺ����ϵͳ API ����˵��

## ����

����Ŀ�Ѽ��ɺ��ҽԺ����ϵͳ�� API �ӿڣ���Ҫ���ܰ�����

- ��ȡ�Ƽ�ҽԺ�б�
- ����ҽԺ
- ��ȡҽԺ����
- ҽԺԤԼ����

## ��˽ӿ�

### ҽԺ��ؽӿ�

#### 1. ��ȡ�Ƽ�ҽԺ
- **�ӿڵ�ַ**: `GET /hospital/recommend`
- **����**: ��ȡ�Ƽ�ҽԺ�б�
- **��������**: `List<AppHospitalVo>`

#### 2. ��ȡҽԺ�б�
- **�ӿڵ�ַ**: `GET /hospital/list`
- **����**: ��ҳ��ȡҽԺ�б�֧������
- **����**: 
  - `name`: ҽԺ���ƣ���ѡ��
  - `pageNum`: ҳ��
  - `pageSize`: ÿҳ����
- **��������**: `Paging<AppHospitalVo>`

#### 3. ��ȡҽԺ����
- **�ӿڵ�ַ**: `GET /hospital/detail/{id}`
- **����**: ����ҽԺID��ȡ��ϸ��Ϣ
- **����**: `id` - ҽԺID
- **��������**: `AppHospitalVo`

## ǰ������

### API �����ļ�

��Ŀʹ��ͳһ�� API �����ļ� `utils/api.js`��

```javascript
const API_BASE_URL = 'http://localhost:8080';

export const hospitalApi = {
  getRecommendHospitals: `${API_BASE_URL}/hospital/recommend`,
  getHospitalList: `${API_BASE_URL}/hospital/list`,
  getHospitalDetail: (id) => `${API_BASE_URL}/hospital/detail/${id}`,
};
```

### ���󹤾ߺ���

��Ŀʹ��ͳһ�����󹤾ߺ��� `utils/request.js`��

```javascript
import { get, post } from '@/utils/request.js';

// ��ȡ�Ƽ�ҽԺ
const result = await get(hospitalApi.getRecommendHospitals);
```

## ���ݸ�ʽ

### AppHospitalVo ���ݽṹ

```java
public class AppHospitalVo {
    private Long id;           // ҽԺID
    private String name;       // ҽԺ����
    private String phone;      // ҽԺ�绰
    private String address;    // ҽԺ��ַ
    private String description; // ҽԺ���
    private Boolean status;    // ״̬
    // ... �����ֶ�
}
```

### ǰ�����ݴ���

ǰ�˻��Զ����������ֶΣ�

- **ͼƬ**: ������û�з���ͼƬ��ǰ�˻�ʹ��Ĭ��ͼƬ `/static/images/hospital1.jpg`
- **��ǩ**: ������û�з��ر�ǩ��ǰ�˻�����Ĭ�ϱ�ǩ `['�ۺ�ҽԺ']`
- **��ַ**: ������û�з��ص�ַ��ǰ�˻���ʾ `'��ַ��Ϣ������'`

## ʹ��ʾ��

### ��ҳ���е��� API

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
        // ��������...
      } catch (error) {
        console.error('��ȡҽԺʧ��:', error);
      } finally {
        this.hospitalLoading = false;
      }
    }
  }
}
```

## ������

### �������
- ����ӿ�����ʧ�ܣ�����ʾ������ʾ
- �ڿ��������У�����ӿڲ����ã���ʹ�ò�������

### ������֤
- ǰ�˻��Զ���֤�������ݵ�������
- ȱʧ���ֶλ�ʹ��Ĭ��ֵ���

## ����ע������

1. **��˷���**: ȷ����˷�����������ȷ�ĵ�ַ
   - ��������: `http://localhost:8888/api`
   - ��������: `http://39.104.57.236:8888/api`
2. **��������**: �����Ҫ���� CORS ֧��ǰ�˿�������
3. **���ݸ�ʽ**: ȷ����˷��ص����ݸ�ʽ��ǰ�������ĸ�ʽһ��
4. **������**: �����ں��Ҳ����ʵ��Ĵ��������־��¼

## ����������

### ��˷�������
- **�˿�**: 8888
- **������·��**: `/api`
- **������ַ**: `http://39.104.57.236:8888/api`

### Redis ����
- **��ַ**: 39.104.57.236
- **�˿�**: 6379
- **ע��**: �������ݿ�˿ڣ����� HTTP ����˿�

## ���������Ų�

### 1. �ӿ�����ʧ��
- ����˷����Ƿ�����
- ���ӿڵ�ַ�Ƿ���ȷ
- ������������Ƿ�����
- �鿴���������̨�Ĵ�����Ϣ

### 2. ���ݸ�ʽ��ƥ��
- ��˷��ص����ݽṹ������ǰ�������Ĳ�һ��
- ��� `ApiResult` ��װ�����ݽṹ
- ����ҳ���ݵ� `records` �ֶ�

### 3. ������ƥ��
- ǰ�˷��͵Ĳ��������������Ĳ�һ��
- ��ҳ���������ʹ�� `pageIndex`��ǰ�˷��� `pageNum`
- �������������ʹ�� `keyword`��ǰ�˷��� `name`

### 4. ���Խӿ�
����ʹ�ò���ҳ�� `/pages/test-api/test-api.vue` ����֤�ӿ��Ƿ���������

## ��������

�����˽ӿڲ����ã�ǰ�˻�ʹ�����²������ݣ�

```javascript
[
  {
    id: 1,
    name: '��������ҽԺ-��ƽ��Ժ',
    image: '/static/images/hospital1.jpg',
    tags: ['����', '�ۺ�ҽԺ'],
    address: '�����к�ƽ���Ͼ��Ͻ�61��'
  },
  {
    id: 2,
    name: '��������ҽԺ-��ӷ�Ժ',
    image: '/static/images/hospital2.jpg',
    tags: ['����', '�ۺ�ҽԺ'],
    address: '�������������վ·33��'
  }
]
```

## ������չ

1. **��֤��Ȩ**: ������� JWT ��֤
2. **�������**: ����������ݻ����������
3. **ͼƬ�ϴ�**: �������ҽԺͼƬ�ϴ�����
4. **�����ֶ�**: �����ں����Ӹ���ҽԺ��Ϣ�ֶ� 