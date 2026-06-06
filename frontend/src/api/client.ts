import type { ApiErrorResponse, ApiResponse } from '../types/api.types';
import { ApiError } from './apiError';
import { clearAuthSession, getAccessToken, getRefreshToken, saveAuthSession } from '../utils/tokenSession';

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL ?? 'http://localhost:8080';

type RequestOptions = {
    method?: string;
    body?: any;
    auth?: boolean;
    retryOnUnauthorized?: boolean;
}

async function parseError(response: Response): Promise<ApiError> {
    try {
        const payload = (await response.json()) as ApiErrorResponse;
        if(payload.error){
            return new ApiError(payload.error.code, payload.error.message, payload.error.status);
        }
    }
    catch {

    }

    return new ApiError('HTTP_ERROR', response.statusText || 'Request failed', response.status);
}

async function refreshAccessToken(): Promise<boolean> {
    const refreshToken = getRefreshToken();
    if(!refreshToken) return false;

    const response = await fetch(`${API_BASE_URL}/api/v1/auth/refresh`, {
        method: 'POST',
        headers: {'Content-Type': 'application/json'},
        body: JSON.stringify({refreshToken})
    });

    if(!response.ok) {
        clearAuthSession();
        return false;
    }

    const payload = (await response.json()) as ApiResponse<{
        accessToken: string,
        refreshToken: string,
        user: { userId: string; email: string; displayName: string};
    }>; 

    saveAuthSession(payload.data.accessToken, payload.data.refreshToken, payload.data.user);
    return true;
}

export async function apiRequest<T>(path: string, options: RequestOptions = {}): Promise<T> {
    const { method = 'GET', body, auth = false, retryOnUnauthorized = true } = options;

    const headers: Record<string, string> = {
        'Content-Type': 'application/json'
    };

    if(auth) {
        const accessToken = getAccessToken();
        if(accessToken) {
            headers.Authorization = `Bearer ${accessToken}`;
        }
    }

    const response = await fetch(`${API_BASE_URL}${path}`, {
        method,
        headers,
        body: body === undefined ? undefined : JSON.stringify(body),
    });

    if(response.status === 401 && auth && retryOnUnauthorized) {
        const refreshed = await refreshAccessToken();
        if(refreshed) {
            return apiRequest<T>(path, { ...options, retryOnUnauthorized: false});
        }
    }

    if(!response.ok) {
        throw await parseError(response);
    }

    const payload = (await response.json()) as ApiResponse<T>;
    return payload.data;
}