/**
 * 对话框管理组合式API
 * 提供弹窗的打开、关闭、数据处理等功能
 */

/**
 * 对话框配置类型
 */
type dialogType={
    idName?: string                                      // ID字段名，默认为'id'
    initFormParam?: Object                               // 初始表单参数
    getDetailsApi: (id: string) => Promise<any>          // 获取详情的API函数
    dataCallBack?: Function                              // 数据回调函数
    afterOpenDialog?: Function                           // 打开对话框后的回调函数
}

/**
 * 对话框管理Hook
 * 提供对话框的状态管理和操作方法
 * 
 * @param config 对话框配置对象
 * @returns 返回对话框相关的状态和方法
 */
export default function useDialog({
    idName = 'id',
    initFormParam = {},
    getDetailsApi,
    dataCallBack,
    afterOpenDialog
}: dialogType) {

    /**
     * 对话框状态
     */
    const state = reactive({
        isShow: false,      // 是否显示对话框
        title: '新增',      // 对话框标题
        id: '',             // 当前操作的数据ID
    })

    /**
     * 表单数据
     * 使用ref包装，便于响应式更新
     */
    const form = ref({...initFormParam})

    /**
     * 获取详情数据
     * 根据ID获取详情数据并填充表单
     * @param id 数据ID
     */
    async function getDetails(id: string) {
        getDetailsApi(id).then(res => {
            // 合并初始参数和API返回的数据
            form.value = Object.assign({}, initFormParam, res);
            // 如果有数据回调函数，则调用它
            dataCallBack && (form.value = dataCallBack(form.value));
        })
    }

    /**
     * 打开对话框
     * 根据传入的行数据决定是新增还是编辑模式
     * @param row 行数据，包含ID则为编辑模式，否则为新增模式
     */
    async function openDialog(row: any) {
        // 如果行数据中有ID，则为编辑模式
        if (row[idName]) {
            state.title = '编辑';
            state.id = row[idName];
            // 获取详情数据
            await getDetails(row[idName]);
        } else {
            // 否则为新增模式
            state.title = '新增';
            state.id = '';
        }
        // 显示对话框
        state.isShow = true;
        // 如果有打开对话框后的回调函数，则调用它
        afterOpenDialog && afterOpenDialog(form.value);
    }

    /**
     * 关闭对话框
     * 隐藏对话框并重置表单数据
     */
    function closeDialog() {
        state.isShow = false;
        // 重置表单数据为初始值
        form.value = {...initFormParam};
    }

    /**
     * 返回对话框相关的状态和方法
     */
    return {
        ...toRefs(state),   // 解构状态对象
        form,               // 表单数据
        closeDialog,        // 关闭对话框方法
        openDialog,         // 打开对话框方法
    }
}
