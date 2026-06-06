import type { ReactNode } from 'react';

type AuthInputProps = {
    id: string;
    label: string;
    type?: 'text' | 'email' | 'password';
    value: string;
    placeholder?: string;
    error?: string;
    icon?: ReactNode;
    onChange: (value: string) => void;
};

export function AuthInput({
    id,
    label,
    type = 'text',
    value,
    placeholder,
    error, 
    icon,
    onChange
}: AuthInputProps) {
    return (
        <div className="mb-8">
            <label htmlFor={id} className="sr-only">
                {label}
            </label>
            <div className="relative border-b border-gray-300 pd-2">
                <input
                    id={id}
                    type={type}
                    value={value}
                    placeholder={placeholder}
                    onChange={(event) => onChange(event.target.value)}
                    className="w-full bg-transparent pr-10 text-base text-gray-800 placeholder:text-gray-400 focus:outline-none"
                />
                <span className="pointer-events-none absolute right-0 top-1/2 -translate-y-1/2 text-gray-400">
                    {icon}
                </span>
            </div>
            {error ? (
                <p className="mt-2 text-sm text-red-600">
                    {error}
                </p>
             ) : null}
        </div>
    );
};


export function UserIcon() {
    return (
        <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
            <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z" />
        </svg>
    );
}


export function LockIcon() {
    return (
        <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
            <path d="M18 8h-1V6c0-2.76-2.24-5-5-5S7 3.24 7 6v2H6c-1.1 0-2 .9-2 2v10c0 1.1.9 2 2 2h12c1.1 0 2-.9 2-2V10c0-1.1-.9-2-2-2zm-6 9c-1.1 0-2-.9-2-2s.9-2 2-2 2 .9 2 2-.9 2-2 2zm3.1-9H8.9V6c0-1.71 1.39-3.1 3.1-3.1 1.71 0 3.1 1.39 3.1 3.1v2z" />
        </svg>
    );
}