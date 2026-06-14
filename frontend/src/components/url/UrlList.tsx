import { useCallback, useEffect, useState } from 'react';
import { listUrls } from '../../api/urlApi';
import type { UrlData } from '../../types/api.types';

type UrlListProps = {
  refreshKey?: number;
};

export function UrlList({ refreshKey = 0 }: UrlListProps) {
  const [urls, setUrls] = useState<UrlData[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const loadUrls = useCallback(async () => {
    setLoading(true);
    setError('');

    try {
      const data = await listUrls();
      setUrls(data);
    } catch {
      setError('Unable to load your links. Is the backend running?');
    } finally {
      setLoading(false);
    }
  }, []);

  useEffect(() => {
    void loadUrls();
  }, [loadUrls, refreshKey]);

  if (loading) {
    return (
      <p className="mt-8 text-sm font-light text-gray-500">
        Loading your links…
      </p>
    );
  }

  if (error) {
    return (
      <p className="mt-8 rounded-md bg-red-50 px-3 py-2 text-sm text-red-700">
        {error}
      </p>
    );
  }

  if (urls.length === 0) {
    return (
      <p className="mt-8 text-sm font-light text-gray-500">
        No short links yet. Create one above.
      </p>
    );
  }

  return (
    <div className="mt-8 w-full text-left">
      <h2 className="mb-4 text-lg font-light tracking-wide text-gray-900">
        My links
      </h2>

      <ul className="space-y-3">
        {urls.map((url) => (
          <li
            key={url.urlId}
            className="rounded-lg border border-gray-200 bg-gray-50 px-4 py-3 text-sm text-gray-800"
          >
            {url.title ? (
              <p className="mb-1 font-medium text-gray-900">
                {url.title}
              </p>
            ) : null}

            <a
              href={url.shortUrl}
              className="break-all text-brand-link underline"
              target="_blank"
              rel="noreferrer"
            >
              {url.shortUrl}
            </a>

            <p className="mt-2 break-all text-xs text-gray-600">
              {url.originalUrl}
            </p>

            <p className="mt-1 text-xs text-gray-500">
              Code: {url.shortCode} ·{' '}
              {new Date(url.createdAt).toLocaleString()}
            </p>
          </li>
        ))}
      </ul>
    </div>
  );
}