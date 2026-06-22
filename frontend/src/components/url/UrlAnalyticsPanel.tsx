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
    return <div>Loading analytics...</div>;
  }

  if (error) {
    return <div>{error}</div>;
  }

  if (!analytics) {
    return null;
  }

  const maxDailyClicks = Math.max(
    ...analytics.clicksByDay.map((day) => day.clicks),
    1
  );

  return (
    <div className="rounded-lg border bg-white p-4 shadow-sm">
      <h2 className="text-lg font-semibold text-gray-900">Analytics</h2>

      <p className="mt-2 text-sm text-gray-700">
        Total clicks:{' '}
        <span className="font-semibold">{analytics.totalClicks}</span>
      </p>

      {/* Clicks Per Day */}
      <div className="mt-4">
        <p className="mb-2 text-xs font-medium text-gray-700">
          Clicks per day (last 30 days)
        </p>

        {analytics.clicksByDay.length === 0 ? (
          <p className="text-xs text-gray-500">
            No clicks recorded yet.
          </p>
        ) : (
          <ul className="space-y-2">
            {analytics.clicksByDay.map((day) => (
              <li
                key={day.date}
                className="flex items-center gap-2 text-xs"
              >
                <span className="w-20 shrink-0 text-gray-600">
                  {day.date}
                </span>

                <div className="h-2 flex-1 overflow-hidden rounded-full bg-gray-200">
                  <div
                    className="h-full rounded-full bg-brand-button"
                    style={{
                      width: `${(day.clicks / maxDailyClicks) * 100}%`,
                    }}
                  />
                </div>

                <span className="w-6 shrink-0 text-right text-gray-800">
                  {day.clicks}
                </span>
              </li>
            ))}
          </ul>
        )}
      </div>

      {/* Top Referrers */}
      <div className="mt-4">
        <p className="mb-2 text-xs font-medium text-gray-700">
          Top referrers
        </p>

        {analytics.topReferrers.length === 0 ? (
          <p className="text-xs text-gray-500">
            No referrer data yet.
          </p>
        ) : (
          <ul className="space-y-1 text-xs text-gray-700">
            {analytics.topReferrers.map((row) => (
              <li
                key={`${row.referrer}-${row.clicks}`}
                className="flex justify-between gap-2"
              >
                <span className="break-all">
                  {row.referrer}
                </span>

                <span className="shrink-0 font-medium text-gray-900">
                  {row.clicks}
                </span>
              </li>
            ))}
          </ul>
        )}
      </div>
    </div>
  );
}