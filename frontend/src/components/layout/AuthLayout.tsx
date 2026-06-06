import type { ReactNode } from 'react';
import { AuthHeader } from './AuthHeader';

type AuthLayoutProps = {
    title: string;
    subtitle?: string;
    children: ReactNode;
    footer?: ReactNode;
};

export function AuthLayout({ title, subtitle, children, footer }: AuthLayoutProps) {
    return (
        <div className="min-h-screen bg-white">
            <AuthHeader />
            <main className="mx-auto flex min-h-[calc(100vh-88px)] max-w-xl flex-col items-center justify-center px-4 py-10">
                <div className="w-full max-w-md text-center">
                    <h1 className="mb-2 text-3xl font-extralight tracking-wide text-gray-900">{title}</h1>
                    <p className="mb-10 text-base font-light text-gray-700">{subtitle}</p>

                    <div className="text-left">
                        {children}
                    </div>

                    {footer ? <div className="mt-8">{footer}</div> : null}
                </div>
            </main>
        </div>
    )
}