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
  const [copiedId, setCopiedId] = useState<string | null>(null);

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
      setUrls((current) => current.filter((url) => url.urlId !== urlId));
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
    setAnalyticsUrlId((current) => (current === urlId ? null : urlId));
  }

  async function handleCopy(url: UrlData) {
    try {
      await navigator.clipboard.writeText(url.shortUrl);
      setCopiedId(url.urlId);
      window.setTimeout(() => setCopiedId(null), 2000);
    } catch {
      setError('Unable to copy link.');
    }
  }

  return (
    <section className="mt-8 rounded-2xl border border-gray-100 bg-white p-6 shadow-card sm:p-8">
      <h2 className="mb-6 text-xl font-semibold text-gray-900">My links</h2>

      {loading ? (
        <p className="text-sm text-gray-500">Loading your links…</p>
      ) : error && urls.length === 0 ? (
        <p className="rounded-lg bg-red-50 px-3 py-2 text-sm text-red-700">{error}</p>
      ) : urls.length === 0 ? (
        <p className="text-sm text-gray-500">No short links yet. Create one above.</p>
      ) : (
        <>
          {error ? (
            <p className="mb-4 rounded-lg bg-red-50 px-3 py-2 text-sm text-red-700">{error}</p>
          ) : null}
          <ul className="space-y-4">
            {urls.map((url) => (
              <li
                key={url.urlId}
                className="rounded-xl border border-gray-100 bg-white p-4 shadow-sm sm:p-5"
              >
                <div className="flex gap-4">
                  <div className="flex h-11 w-11 shrink-0 items-center justify-center rounded-full bg-brand-icon-bg text-brand-icon-fg">
                    <LinkIcon />
                  </div>

                  <div className="min-w-0 flex-1">
                    <div className="flex items-start justify-between gap-3">
                      <div className="min-w-0">
                        <p className="font-semibold text-gray-900">
                          {url.title || url.shortCode.toUpperCase()}
                        </p>
                        <a
                          href={url.shortUrl}
                          className="mt-0.5 block break-all text-sm font-medium text-gray-900 hover:underline"
                          target="_blank"
                          rel="noreferrer"
                        >
                          {url.shortUrl}
                        </a>
                        <p className="mt-1 break-all text-sm text-brand-link">{url.originalUrl}</p>
                      </div>

                      <div className="flex shrink-0 items-center gap-1">
                        <button
                          type="button"
                          onClick={() => void handleCopy(url)}
                          className="rounded-lg p-2 text-gray-400 transition hover:bg-gray-100 hover:text-gray-600"
                          aria-label="Copy short link"
                          title={copiedId === url.urlId ? 'Copied!' : 'Copy link'}
                        >
                          <CopyIcon />
                        </button>
                        <button
                          type="button"
                          className="rounded-lg p-2 text-gray-400 transition hover:bg-gray-100 hover:text-gray-600"
                          aria-label="More options"
                        >
                          <MoreIcon />
                        </button>
                      </div>
                    </div>

                    <p className="mt-2 text-xs text-gray-500">
                      Code: {url.shortCode} · {url.clickCount} click{url.clickCount === 1 ? '' : 's'} ·{' '}
                      {new Date(url.createdAt).toLocaleString()}
                    </p>

                    <div className="mt-3 flex flex-wrap gap-4">
                      <button
                        type="button"
                        onClick={() => toggleAnalytics(url.urlId)}
                        className="text-sm font-medium text-brand-button hover:underline"
                      >
                        {analyticsUrlId === url.urlId ? 'Hide analytics' : 'Show analytics'}
                      </button>
                      <button
                        type="button"
                        disabled={deletingId === url.urlId}
                        onClick={() => void handleDelete(url.urlId)}
                        className="text-sm font-medium text-red-500 hover:underline disabled:opacity-50"
                      >
                        {deletingId === url.urlId ? 'Deleting…' : 'Delete'}
                      </button>
                    </div>

                    {analyticsUrlId === url.urlId ? <UrlAnalyticsPanel urlId={url.urlId} /> : null}
                  </div>
                </div>
              </li>
            ))}
          </ul>
        </>
      )}
    </section>
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

function CopyIcon() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" aria-hidden="true">
      <rect x="9" y="9" width="13" height="13" rx="2" ry="2" />
      <path d="M5 15H4a2 2 0 0 1-2-2V4a2 2 0 0 1 2-2h9a2 2 0 0 1 2 2v1" />
    </svg>
  );
}

function MoreIcon() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="currentColor" aria-hidden="true">
      <circle cx="12" cy="5" r="2" />
      <circle cx="12" cy="12" r="2" />
      <circle cx="12" cy="19" r="2" />
    </svg>
  );
}
