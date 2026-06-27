type DashboardHeaderProps = {
  displayName?: string;
};

export function DashboardHeader({ displayName = 'User' }: DashboardHeaderProps) {
  const initial = displayName.charAt(0).toUpperCase();

  return (
    <header className="bg-brand-header shadow-md">
      <div className="mx-auto flex max-w-6xl items-center justify-between px-4 py-4 sm:px-6">
        <div className="flex items-center gap-3 text-white">
          <div className="flex h-10 w-10 items-center justify-center rounded-full border-2 border-white/80 text-xs font-bold">
            URL
          </div>
          <span className="text-lg font-semibold tracking-wide">URL Shortener</span>
        </div>

        <div className="flex items-center gap-3 sm:gap-4">
          <button
            type="button"
            aria-label="Toggle dark mode"
            className="rounded-full p-2 text-white/90 transition hover:bg-white/10"
          >
            <MoonIcon />
          </button>
          {/* <button
            type="button"
            aria-label="Notifications"
            className="relative rounded-full p-2 text-white/90 transition hover:bg-white/10"
          >
            <BellIcon />
            <span className="absolute right-1 top-1 flex h-4 w-4 items-center justify-center rounded-full bg-red-500 text-[10px] font-bold text-white">
              3
            </span>
          </button> */}
          <div className="flex items-center gap-2 rounded-full bg-white/10 py-1 pl-1 pr-3 text-white">
            <div className="flex h-8 w-8 items-center justify-center rounded-full bg-white text-sm font-semibold text-brand-header">
              {initial}
            </div>
            <span className="hidden text-sm font-medium sm:inline">{displayName}</span>
            <ChevronDownIcon />
          </div>
        </div>
      </div>
    </header>
  );
}

function MoonIcon() {
  return (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" aria-hidden="true">
      <path d="M21 12.79A9 9 0 1 1 11.21 3 7 7 0 0 0 21 12.79z" />
    </svg>
  );
}

// function BellIcon() {
//   return (
//     <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" aria-hidden="true">
//       <path d="M18 8A6 6 0 0 0 6 8c0 7-3 9-3 9h18s-3-2-3-9" />
//       <path d="M13.73 21a2 2 0 0 1-3.46 0" />
//     </svg>
//   );
// }

function ChevronDownIcon() {
  return (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" aria-hidden="true">
      <path d="M6 9l6 6 6-6" />
    </svg>
  );
}
