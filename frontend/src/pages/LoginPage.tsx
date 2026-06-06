import { useState } from 'react';
import type { FormEvent } from 'react';
import {Link} from 'react-router-dom';
import {AuthLayout} from '../components/layout/AuthLayout';
import {AuthButton} from '../components/ui/AuthButton';
import {AuthInput, LockIcon, UserIcon} from '../components/ui/AuthInput';
import type {FormErrors, LoginForm} from '../types/auth.types';
import {validateEmail, validatePassword} from '../utils/validation';

export function LoginPage() {
    const [form, setForm] = useState<LoginForm>({ email: '', password: '' });
    const [errors, setErrors] = useState<FormErrors<LoginForm>>({});
    const [submitMessage, setSubmitMessage] = useState('');
    const [loading, setLoading] = useState(false);

    function updateField(field: keyof LoginForm, value: string) {
        setForm((current) => ({ ...current, [field]: value }));
        setErrors((current) => ({ ...current, [field]: undefined }));
        setSubmitMessage('');
    }

    function validate(): boolean {
        const nextErrors: FormErrors<LoginForm> = {
            email: validateEmail(form.email),
            password: validatePassword(form.password)
        };
        setErrors(nextErrors);
        return !Object.values(nextErrors).some((error) => !!error);
    }

    async function handleSubmit(e: FormEvent<HTMLFormElement>) {
        e.preventDefault();
        if (!validate()) {
            return;
        }

        setLoading(true);
        setSubmitMessage('');

        await new Promise((resolve) => setTimeout(resolve, 600));

        setLoading(false);
        setSubmitMessage('Form looks good. API writing comes in step 7.');
    }

    return (
        <AuthLayout
            title="Welcome to URL Shortener"
            subtitle="Sign in with your account"
            footer={
                <p className="text-center text-sm text-brand-link">
                    New Here?{' '}
                    <Link to="/signup" className="font-medium underline-offset-4 hover:underline">
                        Create an account
                    </Link>
                </p>
            }
        >
            <form onSubmit={handleSubmit} noValidate>
                <AuthInput
                    id="login-email"
                    label="Email"
                    type="email"
                    value={form.email}
                    placeholder="User name or email"
                    error={errors.email}
                    icon={<UserIcon />}
                    onChange={(value) => updateField('email', value)}
                />
                <AuthInput
                    id="login-password"
                    label="Password"
                    type="password"
                    value={form.password}
                    placeholder="Your password"
                    error={errors.password}
                    icon={<LockIcon />}
                    onChange={(value) => updateField('password', value)}
                />

                {submitMessage ? (
                    <p className="mb-4 rounded-md bg-teal-100 px-3 py-2 text-sm text-teal-800">
                        {submitMessage}
                    </p>
                ) : null}

                <AuthButton loading={loading}>
                    Login
                </AuthButton>
            </form>
        </AuthLayout>
    );
}