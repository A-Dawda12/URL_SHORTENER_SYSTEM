import { useState } from 'react';
import type { FormEvent, ReactNode } from 'react';
import { ApiError } from '../../api/apiError';
import { createUrl } from '../../api/urlApi';
import type { UrlData } from '../../types/api.types';

type CreateUrlFormProps = {
  onCreated?: (url: UrlData) => void;
};

export function CreateUrlForm({ onCreated }: CreateUrlFormProps) {
  const [originalUrl, setOriginalUrl] = useState('');
  const [title, setTitle] = useState('');
  const [urlError, setUrlError] = useState('');
  const [submitError, setSubmitError] = useState('');
  const [loading, setLoading] = useState(false);
  const [created, setCreated] = useState<UrlData | null>(null);

  function validateUrl(value: string): string | undefined {
    const trimmed = value.trim();
    if (!trimmed) return 'URL is required';
    try {
      const parsed = new URL(trimmed);
      if (parsed.protocol !== 'http:' && parsed.protocol !== 'https:') {
        return 'URL must start with http:// or https://';
      }
    } catch {
      return 'Enter a valid URL';
    }
    return undefined;
  }

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();
    setSubmitError('');

    const validationError = validateUrl(originalUrl);
    if (validationError) {
      setUrlError(validationError);
      return;
    }

    setUrlError('');
    setLoading(true);

    try {
      const result = await createUrl({
        originalUrl: originalUrl.trim(),
        title: title.trim() || undefined,
      });
      setCreated(result);
      onCreated?.(result);
      setOriginalUrl('');
      setTitle('');
    } catch (error) {
      if (error instanceof ApiError) {
        setSubmitError(error.message);
      } else {
        setSubmitError('Unable to create short link. Is the backend running?');
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="w-full">
      <form onSubmit={handleSubmit} noValidate className="space-y-4">
        <DashboardInput
          id="create-url-original"
          label="Shorten a long URL"
          value={originalUrl}
          placeholder="https://example.com/very/long/path"
          error={urlError}
          icon={<LinkIcon />}
          onChange={(value) => {
            setOriginalUrl(value);
            setUrlError('');
            setSubmitError('');
          }}
        />

        <DashboardInput
          id="create-url-title"
          label="My link label (optional)"
          value={title}
          placeholder="e.g., Blog Post, Campaign Link"
          icon={<TagIcon />}
          onChange={(value) => setTitle(value)}
        />

        {submitError ? (
          <p className="rounded-lg bg-red-50 px-3 py-2 text-sm text-red-700">{submitError}</p>
        ) : null}

        <button
          type="submit"
          disabled={loading}
          className="flex w-full items-center justify-center gap-2 rounded-xl bg-brand-header py-3.5 text-base font-medium text-white transition hover:bg-brand-maroon disabled:cursor-not-allowed disabled:opacity-60"
        >
          {loading ? 'Please wait…' : 'Shorten URL'}
          {!loading ? <ArrowIcon /> : null}
        </button>
      </form>

      {created ? (
        <div className="mt-4 rounded-xl border border-teal-200 bg-teal-50 px-4 py-3 text-sm text-teal-900">
          <p className="mb-1 font-medium">Short link created</p>
          <a href={created.shortUrl} className="break-all text-brand-link underline" target="_blank" rel="noreferrer">
            {created.shortUrl}
          </a>
        </div>
      ) : null}
    </div>
  );
}

type DashboardInputProps = {
  id: string;
  label: string;
  value: string;
  placeholder: string;
  error?: string;
  icon: ReactNode;
  onChange: (value: string) => void;
};

function DashboardInput({ id, label, value, placeholder, error, icon, onChange }: DashboardInputProps) {
  return (
    <div>
      <label htmlFor={id} className="mb-1.5 block text-sm font-medium text-gray-700">
        {label}
      </label>
      <div className="relative">
        <input
          id={id}
          type="text"
          value={value}
          placeholder={placeholder}
          onChange={(event) => onChange(event.target.value)}
          className="w-full rounded-xl border border-gray-200 bg-gray-50 py-3 pl-4 pr-11 text-sm text-gray-900 placeholder:text-gray-400 focus:border-brand-header focus:bg-white focus:outline-none focus:ring-1 focus:ring-brand-header"
        />
        <span className="pointer-events-none absolute right-4 top-1/2 -translate-y-1/2 text-gray-400">
          {icon}
        </span>
      </div>
      {error ? <p className="mt-1.5 text-sm text-red-600">{error}</p> : null}
    </div>
  );
}

function LinkIcon() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" aria-hidden="true">
      <path d="M10 13a5 5 0 0 0 7.54.54l3-3a5 5 0 0 0-7.07-7.07l-1.72 1.71" />
      <path d="M14 11a5 5 0 0 0-7.54-.54l-3 3a5 5 0 0 0 7.07 7.07l1.71-1.71" />
    </svg>
  );
}

function TagIcon() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" aria-hidden="true">
      <path d="M20.59 13.41l-7.17 7.17a2 2 0 0 1-2.83 0L2 12V2h10l8.59 8.59a2 2 0 0 1 0 2.82z" />
      <line x1="7" y1="7" x2="7.01" y2="7" />
    </svg>
  );
}

function ArrowIcon() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" aria-hidden="true">
      <line x1="5" y1="12" x2="19" y2="12" />
      <polyline points="12 5 19 12 12 19" />
    </svg>
  );
}
