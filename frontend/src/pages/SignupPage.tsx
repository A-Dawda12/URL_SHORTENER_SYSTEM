import { useEffect, useState } from 'react';
import type { FormEvent } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { ApiError } from '../api/apiError';
import { AuthLayout } from '../components/layout/AuthLayout';
import { AuthButton } from '../components/ui/AuthButton';
import { AuthInput, LockIcon, UserIcon } from '../components/ui/AuthInput';
import { useAuth } from '../context/AuthContext';
import type { FormErrors, SignupForm } from '../types/auth.types';

import {
  validateConfirmPassword,
  validateDisplayName,
  validateEmail,
  validatePassword,
} from '../utils/validation';

export function SignupPage() {
  const { register, isAuthenticated }= useAuth();
  const navigate = useNavigate();

  const [form, setForm] = useState<SignupForm>({
    email: '',
    password: '',
    confirmPassword: '',
    displayName: '',
  });

  const [errors, setErrors] = useState<FormErrors<SignupForm>>({});
  const [submitError, setSubmitError] = useState('');
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    if(isAuthenticated) {
      navigate('/dasboard', {replace : true});
    }
  }, [isAuthenticated, navigate]);

  function updateField(field: keyof SignupForm, value: string) {
    setForm((current) => ({
      ...current,
      [field]: value,
    }));

    setErrors((current) => ({
      ...current,
      [field]: undefined,
    }));

    setSubmitError('');
  }

  function validate(): boolean {
    const nextErrors: FormErrors<SignupForm> = {
      displayName: validateDisplayName(form.displayName),
      email: validateEmail(form.email),
      password: validatePassword(form.password),
      confirmPassword: validateConfirmPassword(
        form.password,
        form.confirmPassword
      ),
    };

    setErrors(nextErrors);

    return Object.values(nextErrors).every((error) => !error);
  }

  async function handleSubmit(event: FormEvent) {
    event.preventDefault();

    if (!validate()) {
      return;
    }

    setLoading(true);
    setSubmitError('');

    try {
      await register({
        email: form.email.trim(),
        password: form.password,
        displayName: form.displayName.trim(),
      });

      navigate('/login', { 
        replace: true,
        state: {
          message: 'Account created successfully. Please sign in.'
        },
      });
    } catch (error) {
      if (error instanceof ApiError) {
        setSubmitError(error.message);
      } else {
        setSubmitError('Unable to reach the server. Is the backend running on port 8080?');
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <AuthLayout
      title="Create your account"
      subtitle="Sign up to start shortening URLs"
      footer={
        <>
          Already have an account?{' '}
          <Link
            to="/login"
            className="font-medium text-teal-600 hover:text-teal-700"
          >
            Login
          </Link>
        </>
      }
    >
      <form onSubmit={handleSubmit}>
        <AuthInput
          id="signup-display-name"
          label="Display Name"
          value={form.displayName}
          placeholder="Display name"
          error={errors.displayName}
          icon={<UserIcon />}
          onChange={(value) => updateField('displayName', value)}
        />

        <AuthInput
          id="signup-email"
          label="Email"
          type="email"
          value={form.email}
          placeholder="Username or email"
          error={errors.email}
          icon={<UserIcon />}
          onChange={(value) => updateField('email', value)}
        />

        <AuthInput
          id="signup-password"
          label="Password"
          type="password"
          value={form.password}
          placeholder="Password"
          error={errors.password}
          icon={<LockIcon />}
          onChange={(value) => updateField('password', value)}
        />

        <AuthInput
          id="signup-confirm-password"
          label="Confirm Password"
          type="password"
          value={form.confirmPassword}
          placeholder="Confirm password"
          error={errors.confirmPassword}
          icon={<LockIcon />}
          onChange={(value) => updateField('confirmPassword', value)}
        />

        {submitError && (
          <p className="mb-4 rounded-md bg-teal-50 px-3 py-2 text-sm text-teal-800">
            {submitError}
          </p>
        )}

        <AuthButton loading={loading} disabled={loading}>
          Sign Up
        </AuthButton>
      </form>
    </AuthLayout>
  );
}