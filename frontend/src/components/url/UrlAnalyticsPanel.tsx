import { useEffect, useState } from 'react';
import { getUrlAnalytics } from '../../api/urlApi';
import type { UrlAnalyticsData } from '../../types/api.types';

type UrlAnalyticsPanelProps = {
  urlId: string;
};

export function UrlAnalyticsPanel({ urlId }: UrlAnalyticsPanelProps) {
  const [analytics, setAnalytics] = useState<UrlAnalyticsData | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    let cancelled = false;

    async function load() {
      setLoading(true);
      setError('');
      try {
        const data = await getUrlAnalytics(urlId);
        if (!cancelled) {
          setAnalytics(data);
        }
      } catch {
        if (!cancelled) {
          setError('Unable to load analytics.');
        }
      } finally {
        if (!cancelled) {
          setLoading(false);
        }
      }
    }

    void load();
    return () => {
      cancelled = true;
    };
  }, [urlId]);

  if (loading) {
    return (
      <div className="mt-4 rounded-xl bg-gray-50 px-4 py-6 text-sm text-gray-500">
        Loading analytics…
      </div>
    );
  }

  if (error) {
    return (
      <div className="mt-4 rounded-xl bg-red-50 px-4 py-3 text-sm text-red-700">{error}</div>
    );
  }

  if (!analytics) {
    return null;
  }

  const maxDailyClicks = Math.max(...analytics.clicksByDay.map((day) => day.clicks), 1);

  return (
    <div className="mt-4 rounded-xl bg-gray-50 px-4 py-5 sm:px-6">
      <div className="mb-5 flex flex-wrap items-center justify-between gap-3">
        <div className="flex items-center gap-2 text-gray-800">
          <ChartIcon />
          <span className="font-semibold">Analytics</span>
        </div>
        <select
          className="rounded-lg border border-gray-200 bg-white px-3 py-1.5 text-sm text-gray-600"
          defaultValue="30"
          aria-label="Date range"
        >
          <option value="30">Last 30 days</option>
        </select>
      </div>

      <div className="grid gap-6 md:grid-cols-3">
        <div>
          <p className="text-sm font-medium text-gray-600">Total clicks</p>
          <p className="mt-2 text-4xl font-bold text-gray-900">{analytics.totalClicks}</p>
        </div>

        <div>
          <p className="mb-3 text-sm font-medium text-gray-600">Clicks per day</p>
          {analytics.clicksByDay.length === 0 ? (
            <p className="text-sm text-gray-500">No clicks recorded yet.</p>
          ) : (
            <ul className="space-y-2.5">
              {analytics.clicksByDay.map((day) => (
                <li key={day.date} className="flex items-center gap-2 text-sm">
                  <span className="w-[5.5rem] shrink-0 text-gray-600">{day.date}</span>
                  <div className="h-2.5 flex-1 overflow-hidden rounded-full bg-gray-200">
                    <div
                      className="h-full rounded-full bg-brand-button"
                      style={{ width: `${(day.clicks / maxDailyClicks) * 100}%` }}
                    />
                  </div>
                  <span className="w-5 shrink-0 text-right font-medium text-gray-800">{day.clicks}</span>
                </li>
              ))}
            </ul>
          )}
        </div>

        <div>
          <p className="mb-3 text-sm font-medium text-gray-600">Top referrers</p>
          {analytics.topReferrers.length === 0 ? (
            <p className="text-sm text-gray-500">No referrer data yet.</p>
          ) : (
            <ul className="space-y-2 text-sm text-gray-700">
              {analytics.topReferrers.map((row) => (
                <li key={`${row.referrer}-${row.clicks}`} className="flex justify-between gap-2">
                  <span className="break-all">{row.referrer}</span>
                  <span className="shrink-0 font-semibold text-gray-900">{row.clicks}</span>
                </li>
              ))}
            </ul>
          )}
        </div>
      </div>
    </div>
  );
}

function ChartIcon() {
  return (
    <svg width="18" height="18" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" aria-hidden="true">
      <line x1="18" y1="20" x2="18" y2="10" />
      <line x1="12" y1="20" x2="12" y2="4" />
      <line x1="6" y1="20" x2="6" y2="14" />
    </svg>
  );
}
