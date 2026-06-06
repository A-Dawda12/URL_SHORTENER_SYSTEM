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

