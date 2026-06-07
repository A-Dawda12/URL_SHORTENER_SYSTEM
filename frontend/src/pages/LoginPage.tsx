import { useEffect, useState, type FormEvent } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';

import { ApiError } from '../api/apiError';
import { AuthLayout } from '../components/layout/AuthLayout';
import { AuthButton } from '../components/ui/AuthButton';
import { AuthInput, LockIcon, UserIcon } from '../components/ui/AuthInput';
import { useAuth } from '../context/AuthContext';

import type { FormErrors, LoginForm } from '../types/auth.types';

import {
  validateEmail,
  validatePassword,
} from '../utils/validation';

type LoginLocationState = {
  message?: string;
  from?: string;
};

export function LoginPage() {
  const { login, isAuthenticated } = useAuth();

  const navigate = useNavigate();
  const location = useLocation();

  const locationState = (location.state as LoginLocationState | null) ?? {};

  const [form, setForm] = useState<LoginForm>({
    email: '',
    password: '',
  });

  const [errors, setErrors] = useState<FormErrors<LoginForm>>({});
  const [submitMessage, setSubmitMessage] = useState(
    locationState.message ?? ''
  );
  const [submitError, setSubmitError] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if (isAuthenticated) {
      navigate('/dashboard', { replace: true });
    }
  }, [isAuthenticated, navigate]);

  function updateField(
    field: keyof LoginForm,
    value: string
  ) {
    setForm((current) => ({
      ...current,
      [field]: value,
    }));

    setErrors((current) => ({
      ...current,
      [field]: undefined,
    }));

    setSubmitMessage('');
    setSubmitError('');
  }

  function validate(): boolean {
    const nextErrors: FormErrors<LoginForm> = {
      email: validateEmail(form.email),
      password: validatePassword(form.password),
    };

    setErrors(nextErrors);

    return !nextErrors.email && !nextErrors.password;
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    if (!validate()) return;

    setLoading(true);
    setSubmitMessage('');
    setSubmitError('');

    try {
      await login({
        email: form.email.trim(),
        password: form.password,
      });

      const redirectTo =
        locationState.from ?? '/dashboard';

      navigate(redirectTo, {
        replace: true,
      });
    } catch (error) {
      if (error instanceof ApiError) {
        setSubmitError(error.message);
      } else {
        setSubmitError(
          'Unable to reach the server. Is the backend running on port 8080?'
        );
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <AuthLayout
      title="Welcome to URL Shortener"
      subtitle="Sign in with your account"
      footer={
        <>
          New here?{' '}
          <Link
            to="/signup"
            className="font-medium text-teal-600 hover:text-teal-700"
          >
            Create an account
          </Link>
        </>
      }
    >
      <form onSubmit={handleSubmit}>
        <AuthInput
          id="login-email"
          label="Email"
          type="email"
          value={form.email}
          placeholder="Username or email"
          error={errors.email}
          icon={<UserIcon />}
          onChange={(value) =>
            updateField('email', value)
          }
        />

        <AuthInput
          id="login-password"
          label="Password"
          type="password"
          value={form.password}
          placeholder="Password"
          error={errors.password}
          icon={<LockIcon />}
          onChange={(value) =>
            updateField('password', value)
          }
        />

        {submitMessage && (
          <p className="mb-4 rounded-md bg-teal-50 px-3 py-2 text-sm text-teal-800">
            {submitMessage}
          </p>
        )}

        {submitError && (
          <p className="mb-4 rounded-md bg-red-50 px-3 py-2 text-sm text-red-700">
            {submitError}
          </p>
        )}

        <AuthButton
          loading={loading}
          disabled={loading}
        >
          Login
        </AuthButton>
      </form>
    </AuthLayout>
  );
}