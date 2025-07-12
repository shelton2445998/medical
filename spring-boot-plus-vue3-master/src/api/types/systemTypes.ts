export interface LoginParamsType {
    mobile: string;
    password: string;
}

export type LoginResult = {
    /** token */
    token: string;
};

/** 获取当前登录用户信息返回数据类型*/
export type LoginUserInfoResult = {
    id: string;
    name: string;
    mobile: string;
    email: string;
    gender: number;
    hospitalId: NullType<string>;
    hospitalName: NullType<string>;
    departmentId: NullType<string>;
    departmentName: NullType<string>;
    title: string;
    introduction: string;
    avatarUrl: NullType<string>;
    status: boolean;
    createTime: string;
    // 用户权限数组
    permissions: [];
};
