type DashboardFooterProps = {
  onLogout: () => void;
};

export function DashboardFooter({ onLogout }: DashboardFooterProps) {
  return (
    <footer className="mt-10 border-t border-gray-200 bg-white px-4 py-8">
      <div className="mx-auto flex max-w-6xl flex-col items-center gap-6">
        <button
          type="button"
          onClick={onLogout}
          className="inline-flex items-center gap-2 rounded-full bg-brand-button px-10 py-3 text-base font-medium text-white shadow-sm transition hover:brightness-95"
        >
          <LogoutIcon />
          Logout
        </button>

        <div className="flex w-full flex-col items-center justify-between gap-4 text-sm text-gray-500 sm:flex-row">
          <p>© 2025 URL Shortener. All rights reserved.</p>
          <div className="flex gap-6">
            <a href="#" className="hover:text-gray-800">
              Terms
            </a>
            <a href="#" className="hover:text-gray-800">
              Privacy
            </a>
            <a href="#" className="hover:text-gray-800">
              Contact
            </a>
          </div>
        </div>
      </div>
    </footer>
  );
}

function LogoutIcon() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" aria-hidden="true">
      <path d="M9 21H5a2 2 0 0 1-2-2V5a2 2 0 0 1 2-2h4" />
      <polyline points="16 17 21 12 16 7" />
      <line x1="21" y1="12" x2="9" y2="12" />
    </svg>
  );
}
