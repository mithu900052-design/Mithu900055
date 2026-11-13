/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  darkMode: 'class',
  theme: {
    extend: {
      colors: {
        monexa: {
          dark: '#0A0F1D',
          surface: '#10192E',
          surfaceLight: '#18233C',
          cyan: '#00E5FF',
          blue: '#2979FF',
          emerald: '#00E676',
          amber: '#FFD600',
          red: '#FF5252',
          border: '#1E2F52',
        }
      }
    },
  },
  plugins: [],
}
