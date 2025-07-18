/**
 * 表格管理组合式API
 * 提供表格数据获取、分页、排序、搜索等功能
 */
import {ElMessage, ElMessageBox} from "element-plus";
import {toSnakeCase} from "@/utils";
import {calcTableIndex} from "@/utils/util";

/**
 * 表格配置类型
 */
type tableType = {
    request: Function           // 请求方法
    immediate?: boolean         // 是否立即触发，默认为true
    initParam?: Object          // 初始化参数，默认为{}
    searchParam?: Object        // 搜索参数
    isShowPage?: boolean        // 是否显示分页组件，默认为true
    dataCallBack?: Function     // 数据处理回调函数
    afterCallback?: (data: any) => void  // 获取数据后的回调函数
}

/**
 * 表格管理Hook
 * 封装表格的数据获取、分页、排序、搜索等常用功能
 * 
 * @param request 请求方法
 * @param immediate 是否立即触发，默认为true
 * @param initParam 获取数据初始化参数，默认为{}
 * @param isShowPage 是否显示分页组件，默认为true
 * @param dataCallBack 对后台返回的数据进行处理的方法
 * @param afterCallback 获取数据后执行方法
 * @returns 返回表格相关的状态和方法
 */
export function useTable({
    request,
    immediate = true,
    initParam = {},
    isShowPage = true,
    dataCallBack,
    afterCallback
}: tableType) {
    /**
     * 表格状态管理
     */
    const state = reactive({
        // 表格数据
        tableData: [],
        // 加载状态
        loading: false,
        // 分页数据
        pagination: {
            pageIndex: 1,       // 当前页码
            pageSize: 10,       // 每页条数
            total: 0            // 总条数
        },
        // 查询参数
        searchParam: {},
        // 总参数(包含分页和查询参数)
        totalParam: {},
        // 排序参数
        sortParam: {},
        // 接口返回的所有内容
        responseData: null
    })

    /**
     * 获取表格数据
     * 根据当前参数请求数据并更新表格状态
     */
    async function getTableData() {
        // 更新总参数
        updatedTotalParam();
        // 开始加载
        state.loading = true;
        
        try {
            // 调用请求方法获取数据
            const res = await request(state.totalParam);
            
            if (isShowPage) {
                // 分页模式：处理分页数据
                state.tableData = res.list || [];
                state.pagination.total = res.total || 0;
                // 计算表格索引
                state.tableData = calcTableIndex(res, state.pagination);
            } else {
                // 非分页模式：直接使用返回数据
                state.tableData = res || []
            }
            
            // 如果有数据回调函数，则调用它
            try {
                dataCallBack && (state.tableData = dataCallBack(state.tableData));
            } catch (error) {
                console.error(error)
                ElMessage.error('格式化数据错误')
            }
            
            // 调用获取数据后的回调函数
            afterCallback && afterCallback(state.tableData);
        } finally {
            // 结束加载
            state.loading = false;
        }
    }

    /**
     * 组件挂载前的处理
     * 如果设置了立即获取数据，则在组件挂载前获取数据
     */
    if (immediate) {
        onBeforeMount(async () => {
            await getTableData()
        })
    }

    /**
     * 更新总参数
     * 合并初始参数、搜索参数、分页参数和排序参数
     */
    const updatedTotalParam = () => {
        state.totalParam = {};
        Object.assign(
            state.totalParam,
            initParam,
            state.searchParam,
            isShowPage ? state.pagination : {},
            state.sortParam
        );
    };

    /**
     * 搜索功能
     * 重置排序和分页，使用新的搜索参数获取数据
     * @param params 搜索参数
     */
    const search = async (params: any) => {
        // 重置排序参数
        state.sortParam = {};
        // 重置到第一页
        state.pagination.pageIndex = 1;
        // 设置搜索参数
        state.searchParam = params;
        // 更新总参数
        updatedTotalParam();
        // 获取数据
        await getTableData();
    };

    /**
     * 排序功能
     * 根据排序条件重新获取数据
     * @param prop 排序字段
     * @param order 排序方向
     * @param key 排序键名
     */
    const sortChange = async ({prop, order, key}: { prop: string, order: string, key: string }) => {
        // 设置排序参数
        state.sortParam = {
            orderByColumn: key || toSnakeCase(prop),  // 排序字段（转换为下划线格式）
            orderByAsc: order === "ascending"         // 是否升序
        };
        // 重置到第一页
        state.pagination.pageIndex = 1;
        // 更新总参数
        updatedTotalParam();
        // 获取数据
        await getTableData();
    };

    /**
     * 翻页功能
     * 切换到指定页码
     * @param page 页码
     */
    const changePage = async (page: number) => {
        state.pagination.pageIndex = page;
        updatedTotalParam();
        await getTableData()
    };
    
    /**
     * 修改每页显示数量
     * 重置到第一页并设置新的每页条数
     * @param size 每页条数
     */
    const changeSize = async (size: number) => {
        state.pagination.pageIndex = 1;
        state.pagination.pageSize = size;
        updatedTotalParam();
        await getTableData()
    };

    /**
     * 返回表格相关的状态和方法
     */
    return {
        ...toRefs(state),   // 解构状态对象
        getTableData,       // 获取表格数据方法
        changePage,         // 翻页方法
        search,             // 搜索方法
        sortChange,         // 排序方法
        changeSize          // 修改每页显示数量方法
    }
}

/**
 * 删除表格数据配置类型
 */
type delTableType = {
    id: string                           // 要删除的数据ID
    request: (id: string) => Promise<void>  // 删除请求方法
    callback: () => void                 // 删除成功后的回调函数
}

/**
 * 删除表格数据
 * 显示确认对话框，确认后执行删除操作
 * @param id 要删除的数据ID
 * @param request 删除请求方法
 * @param callback 删除成功后的回调函数
 */
export function delTable({id, request, callback}: delTableType): void {
    // 弹出确认框，询问是否确认删除
    ElMessageBox.confirm(
        '是否确认删除本条数据？',
        '提示',
        {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning',
        })
        .then(() => {
            // 发送请求删除表格数据
            request(id).then(() => {
                // 删除成功，弹出提示框
                ElMessage.success('删除成功');
                // 如果有回调函数，则执行回调函数
                if (callback) callback();
            })
        }).catch(() => {
        // 取消删除，不执行任何操作
    })
}
