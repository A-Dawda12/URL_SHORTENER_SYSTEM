 import { useState } from 'react';
import type { FormEvent } from 'react';
import { ApiError } from '../../api/apiError';
import { createUrl } from '../../api/urlApi';
import type { UrlData } from '../../types/api.types';
import { AuthButton } from '../ui/AuthButton';
import { AuthInput, UserIcon } from '../ui/AuthInput';

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

    if (!trimmed) {
      return 'URL is required';
    }

    try {
      const parsed = new URL(trimmed);

      if (
        parsed.protocol !== 'http:' &&
        parsed.protocol !== 'https:'
      ) {
        return 'URL must start with http:// or https://';
      }
    } catch {
      return 'Enter a valid URL';
    }

    return undefined;
  }

  async function handleSubmit(event: FormEvent) {
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
        setSubmitError(
          'Unable to create short link. Is the backend running?'
        );
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <div>
      <form onSubmit={handleSubmit}>
        <AuthInput
          id="create-url-original"
          label="Long URL"
          type="text"
          value={originalUrl}
          placeholder="https://example.com/very/long/path"
          error={urlError}
          icon={<UserIcon />}
          onChange={(value) => {
            setOriginalUrl(value);
            setUrlError('');
            setSubmitError('');
          }}
        />

        <AuthInput
          id="create-url-title"
          label="Title (optional)"
          value={title}
          placeholder="My link label"
          icon={<UserIcon />}
          onChange={(value) => setTitle(value)}
        />

        {submitError ? (
          <p className="mb-4 rounded-md bg-red-50 px-3 py-2 text-sm text-red-700">
            {submitError}
          </p>
        ) : null}

        <AuthButton loading={loading}>
          Shorten URL
        </AuthButton>
      </form>

      {created ? (
        <div className="mt-6 rounded-lg border border-teal-200 bg-teal-50 px-4 py-4 text-sm text-teal-900">
          <p className="mb-1 font-medium">
            Short link created
          </p>

          <a
            href={created.shortUrl}
            className="break-all text-brand-link underline"
            target="_blank"
            rel="noreferrer"
          >
            {created.shortUrl}
          </a>

          <p className="mt-2 text-xs text-teal-800">
            Code: {created.shortCode}
          </p>
        </div>
      ) : null}
    </div>
  );
}