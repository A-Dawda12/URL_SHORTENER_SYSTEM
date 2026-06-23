import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { CreateUrlForm } from '../components/url/CreateUrlForm';
import { UrlList } from '../components/url/UrlList';
import { DashboardFooter } from '../components/layout/DashboardFooter';
import { DashboardHeader } from '../components/layout/DashboardHeader';
import { useAuth } from '../context/AuthContext';

export function DashboardPage() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [listRefreshKey, setListRefreshKey] = useState(0);

  function handleLogout() {
    logout();
    navigate('/login', { replace: true });
  }

  return (
    <div className="min-h-screen bg-gray-50">
      <DashboardHeader displayName={user?.displayName} />

      <main className="mx-auto max-w-6xl px-4 py-8 sm:px-6">
        <div className="grid gap-6 lg:grid-cols-2">
          <section className="rounded-2xl border border-gray-100 bg-white p-6 shadow-card sm:p-8">
            <div className="flex items-start gap-4">
              <div className="flex h-12 w-12 shrink-0 items-center justify-center rounded-full bg-gray-100 text-gray-500">
                <UserIcon />
              </div>
              <div>
                <h1 className="text-2xl font-semibold text-gray-900">
                  Welcome back, {user?.displayName} 👋
                </h1>
                <p className="mt-1 text-sm text-gray-500">{user?.email}</p>
              </div>
            </div>
          </section>

          <section className="rounded-2xl border border-gray-100 bg-white p-6 shadow-card sm:p-8">
            <CreateUrlForm onCreated={() => setListRefreshKey((key) => key + 1)} />
          </section>
        </div>

        <UrlList refreshKey={listRefreshKey} />
      </main>

      <DashboardFooter onLogout={handleLogout} />
    </div>
  );
}

function UserIcon() {
  return (
    <svg width="24" height="24" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
      <path d="M12 12c2.21 0 4-1.79 4-4s-1.79-4-4-4-4 1.79-4 4 1.79 4 4 4zm0 2c-2.67 0-8 1.34-8 4v2h16v-2c0-2.66-5.33-4-8-4z" />
    </svg>
  );
}
