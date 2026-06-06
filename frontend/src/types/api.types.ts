export type ApiMeta = {
    requestId: string;
    timestamp: string;
}

export type ApiResponse<T> = {
    success: boolean;
    data: T;
    meta: ApiMeta;
}

export type ApiErrorBody = {
    code: string;
    message: string;
    status: number;
}

export type ApiErrorResponse = {
    success: false;
    error: ApiErrorBody;
    meta: ApiMeta;
}

export type AuthUser = {
    userId: string;
    email: string;
    displayName: string;
}

export type AuthData = {
    accessToken: string;
    refreshToken: string;
    tokenType: string;
    expiresIn: number;
    user: AuthUser;
}

export type RegisterData = {
    userId : string;
    email: string;
    displayName: string;
};