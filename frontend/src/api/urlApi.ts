import type { UrlData } from '../types/api.types';
import { apiRequest } from './client';

export type CreateUrlPayload = {
    originalUrl: string;
    title?: string;
};

export function createUrl(payload: CreateUrlPayload): Promise<UrlData> {
    return apiRequest<UrlData>('/api/v1/urls', {
        method: 'POST',
        body: payload,
        auth: true
    });
}

export function listUrls(): Promise<UrlData[]> {
    return apiRequest<UrlData[]>('/api/v1/urls', {
        method: 'GET',
        auth: true
    })
}

export function deleteUrl(urlId: string): Promise<void> {
    return apiRequest<void>(`/api/v1/urls/${urlId}`, {
        method: 'DELETE',
        auth: true
    })
}