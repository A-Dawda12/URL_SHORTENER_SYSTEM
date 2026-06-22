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

export type UrlData = {
    urlId: string;
    shortCode: string;
    shortUrl: string;
    originalUrl: string;
    title: string;
    clickCount: number;
    createdAt: string;
};

export type DailyClickCountData = {
    date: string;
    clicks: number;
};

export type ReferrerCountData = {
    referrer: string;
    clicks: number;
}

export type UrlAnalyticsData = {
    urLId: string;
    shortCode: string;
    totalClicks: number;
    clicksByDay: DailyClickCountData[];
    topReferrers: ReferrerCountData[];
}