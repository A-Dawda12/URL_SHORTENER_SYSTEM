import { useNavigate } from 'react-router-dom';

import { AuthHeader } from '../components/layout/AuthHeader';
import { AuthButton } from '../components/ui/AuthButton';
import { useAuth } from '../context/AuthContext';
import { CreateUrlForm } from '../components/url/CreateUrlForm';

export function DashboardPage() {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  function handleLogout() {
    logout();
    navigate('/login', { replace: true });
  }

  return (
    <div className="min-h-screen bg-white">
      <AuthHeader />

      <main className="mx-auto flex min-h-[calc(100vh-88px)] max-w-xl flex-col items-center justify-center px-6 py-10">
        <div className="w-full max-w-md text-center">
          <h1 className="mb-2 text-3xl font-extralight tracking-wide text-gray-900">
            Welcome, {user?.displayName}
          </h1>

          <p className="mb-8 text-base font-light text-gray-700">
            {user?.email}
          </p>

          {/* <div className="rounded-lg border border-gray-200 bg-gray-50 px-4 py-6 text-left text-sm text-gray-700">
            <p className="mb-2">You are logged in.</p>

            <p className="text-gray-500">
              URL shortening features come in the next phase.
            </p>
          </div> */}

          <CreateUrlForm />

          <div className="mt-8">
            <AuthButton
              type="button"
              onClick={handleLogout}
            >
              Logout
            </AuthButton>
          </div>
        </div>
      </main>
    </div>
  );
}