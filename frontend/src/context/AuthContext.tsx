import {
    createContext,
    useCallback,
    useContext,
    useEffect,
    useMemo,
    useState,
    type ReactNode
} from 'react';
import { login as loginRequest, register as registerRequest, type LoginPayload, type RegisterPayload } from '../api/authApi';
import type { AuthUser } from '../types/api.types';
import { clearAuthSession, getStoredUser, hasStoredSession, saveAuthSession } from '../utils/tokenStorage';

type AuthContextValue = {
    user: AuthUser | null;
    isAuthenticated: boolean;
    isLoading: boolean;
    login: (payload: LoginPayload) => Promise<void>;
    register: (payload: RegisterPayload) => Promise<void>;
    logout: () => void;
}

const AuthContext = createContext<AuthContextValue | null>(null);

export function AuthProvider({ children }: { children: ReactNode }) {
    const [user, setUser] = useState<AuthUser | null>(null);
    const [isLoading, setIsLoading] = useState(true);

    useEffect(() => {
        if(hasStoredSession()) {
            setUser(getStoredUser());
        }
        setIsLoading(false);
    }, []);

    const login = useCallback(async (payload: LoginPayload) => {
        const data = await loginRequest(payload);
        saveAuthSession(data.accessToken, data.refreshToken, data.user);
        setUser(data.user);
    }, []);

    const register = useCallback(async (payload: RegisterPayload) => {
        await registerRequest(payload);
    }, []);

    const logout = useCallback(() => {
        clearAuthSession();
        setUser(null);
    }, []);

    const value = useMemo<AuthContextValue>(() => ({
        user,
        isAuthenticated: Boolean(user),
        isLoading,
        login,
        register,
        logout,
    }), [user, isLoading, login, register, logout]);

    return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
}

export function useAuth(): AuthContextValue {
    const context = useContext(AuthContext);
    if(!context) {
        throw new Error('useAuth must be used within an AuthProvider');
    }
    return context;
}