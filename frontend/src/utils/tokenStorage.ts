import type { AuthUser } from "../types/api.types";

const ACCESS_TOKKEN_KEY = 'accessToken';
const REFRESH_TOEN_KEY = 'REFRESHtOKEN';
const USER_KEY = 'authUser';

export function getAccessToken(): string | null {
    return localStorage.getItem(ACCESS_TOKKEN_KEY);
}

export function getRefreshToken(): string | null {
    return localStorage.getItem(REFRESH_TOEN_KEY);
}

export function getStoredUser(): AuthUser | null {
    const raw = localStorage.getItem(USER_KEY);
    if(!raw) return null;

    try {
        return JSON.parse(raw) as AuthUser;
    } catch  {
        return null;
    }
}

export function saveAuthSession(accessToken: string, refreshToken : string, user: AuthUser): void {
    localStorage.setItem(ACCESS_TOKKEN_KEY, accessToken);
    localStorage.setItem(REFRESH_TOEN_KEY, refreshToken);
    localStorage.setItem(USER_KEY, JSON.stringify(user));
}

export function clearAuthSession(): void {
    localStorage.removeItem(ACCESS_TOKKEN_KEY);
    localStorage.removeItem(REFRESH_TOEN_KEY);
    localStorage.removeItem(USER_KEY);
}

export function hasStoredSession(): boolean {
    return Boolean(getAccessToken() && getRefreshToken() && getStoredUser());
}