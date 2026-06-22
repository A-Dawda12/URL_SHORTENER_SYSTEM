import { useCallback, useEffect, useState } from 'react';
import { deleteUrl, listUrls } from '../../api/urlApi';
import type { UrlData } from '../../types/api.types';
import { UrlAnalyticsPanel } from './UrlAnalyticsPanel';

type UrlListProps = {
  refreshKey?: number;
};

export function UrlList({ refreshKey = 0 }: UrlListProps) {
  const [urls, setUrls] = useState<UrlData[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [deletingId, setDeletingId] = useState<string | null>(null);
  const [analyticsUrlId, setAnalyticsUrlId] = useState<string | null>(null);

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

  async function handleDelete(urlId: string) {
    setDeletingId(urlId);
    setError('');

    try {
      await deleteUrl(urlId);

      setUrls((current) =>
        current.filter((url) => url.urlId !== urlId)
      );

      if (analyticsUrlId === urlId) {
        setAnalyticsUrlId(null);
      }
    } catch {
      setError('Unable to delete this link. Please try again.');
    } finally {
      setDeletingId(null);
    }
  }

  function toggleAnalytics(urlId: string) {
    setAnalyticsUrlId((current) =>
      current === urlId ? null : urlId
    );
  }

  if (loading) {
    return (
      <p className="mt-8 text-sm font-light text-gray-500">
        Loading your links…
      </p>
    );
  }

  if (error && urls.length === 0) {
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

      {error ? (
        <p className="mb-4 rounded-md bg-red-50 px-3 py-2 text-sm text-red-700">
          {error}
        </p>
      ) : null}

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
              Code: {url.shortCode} · {url.clickCount} click
              {url.clickCount === 1 ? '' : 's'} ·{' '}
              {new Date(url.createdAt).toLocaleString()}
            </p>

            <div className="mt-2 flex flex-wrap gap-3">
              <button
                type="button"
                onClick={() => toggleAnalytics(url.urlId)}
                className="text-xs font-medium text-brand-link hover:underline"
              >
                {analyticsUrlId === url.urlId
                  ? 'Hide analytics'
                  : 'View analytics'}
              </button>

              <button
                type="button"
                disabled={deletingId === url.urlId}
                onClick={() => void handleDelete(url.urlId)}
                className="text-xs font-medium text-red-600 hover:underline disabled:opacity-50"
              >
                {deletingId === url.urlId
                  ? 'Deleting…'
                  : 'Delete'}
              </button>
            </div>

            {analyticsUrlId === url.urlId ? (
              <UrlAnalyticsPanel urlId={url.urlId} />
            ) : null}
          </li>
        ))}
      </ul>
    </div>
  );
}