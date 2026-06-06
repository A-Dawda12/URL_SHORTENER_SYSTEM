import type { AuthData, RegisterData } from '../types/api.types';
import { apiRequest } from './client';


export type RegisterPayload = {
    email: string;
    password: string;
    displayName: string;
}

export type LoginPayload = {
    email: string;
    password: string;
}

export function register(payload: RegisterPayload): Promise<RegisterData> {
    return apiRequest<RegisterData>('/api/v1/auth/register', {
        method: 'POST',
        body: payload,
        auth: false
    });
}

export function login(payload: LoginPayload): Promise<AuthData> {
    return apiRequest<AuthData>('/api/v1/auth/login', {
        method: 'POST',
        body: payload,
        auth: false
    });
}

