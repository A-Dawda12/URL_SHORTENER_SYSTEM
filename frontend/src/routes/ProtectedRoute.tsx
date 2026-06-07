import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';

type ProtectedRouteProps = {
    children: React.ReactNode;
};

export function ProtectedRoute({children }: ProtectedRouteProps) {
    const { isAuthenticated, isLoading } = useAuth();
    const location = useLocation();

    if (isLoading) {
        return (
        <div className="flex min-h-screen items-center justify-center bg-white text-gray-600" >
            Loading...
        </div >
        );
    }

    if (!isAuthenticated) {
        return <Navigate to="/login"  replace state={ { from: location.pathname } } />;
    }

    return children;
}
