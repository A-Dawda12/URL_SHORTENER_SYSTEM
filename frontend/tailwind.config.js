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
          button: '#49B2B8',
          link: '#2B6CB0',
        },
      },
    },
    fontFamily: {
      sans: ['Segoe UI', 'sans-serif', 'Arial', 'Helvetica Neue'],
    },
  },
  plugins: [],
};