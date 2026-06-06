import type { ReactNode } from 'react';

type AuthButtonProps = {
    children: ReactNode;
    loading?: boolean;
    disabled?: boolean;
    type?: 'button' | 'submit';
    onClick?: () => void;
}

export function AuthButton({ 
    children, 
    loading = false, 
    disabled = false, 
    type = 'submit', 
    onClick 
}: AuthButtonProps) {
    return (
        <button 
            type={type}
            onClick={onClick}
            disabled={disabled || loading}
            className="mt-2 w-full rounded-full bg-brand-button py-3 text-lg font-light text-white transition hover:brightness-95 disabled:cursor-not-allowed disabled:opacity-60"
        >
            {loading ? 'Please wait...' : children}
        </button>
    );
}
        