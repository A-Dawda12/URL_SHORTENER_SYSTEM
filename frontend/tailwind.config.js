/**@type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html", './src/**/*.{js,ts,jsx,tsx}',
  ],
  theme: {
    extend: {
      colors: {
        brand: {
          header: '#8C1D2F',
          maroon: '#7A1B2E',
          button: '#49B2B8',
          link: '#2B6CB0',
          'icon-bg': '#F8E4E8',
          'icon-fg': '#C23D5C'
        },
      },
    },
    fontFamily: {
      sans: ['Segoe UI', 'sans-serif', 'Arial', 'Helvetica Neue'],
    },
    boxShadow: {
      card: '0 4px 24px rgba(0, 0, 0, 0.6)',
    }
  },
  plugins: [],
};