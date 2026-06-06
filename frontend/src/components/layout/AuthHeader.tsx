type AuthHeaderProps = {
    title?: string;
};

export function AuthHeader({ title = 'URL Shortener' }: AuthHeaderProps) {
    return (
        <header className="bg-brand-header py-5 text-center text-white shadow-sm">
            <div className="mx-auto inline-flex h-12 w-12 items-center justify-center rounded-full border-2 border-white/90 text-sm font-semibold tracking-wide">
                URL
            </div>
            <p className="sr-only">{title}</p>
        </header>
    )
}