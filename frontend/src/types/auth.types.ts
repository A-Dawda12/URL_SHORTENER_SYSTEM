export type LoginForm = {
    email: string;
    password: string;
}

export type SignupForm = {
    email: string;
    password: string;
    confirmPassword: string;
    displayName: string;
}

export type FormErrors<T> = Partial<Record<keyof T, string>>;