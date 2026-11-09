/** @type {import('tailwindcss').Config} */
export default {
  content: [
    "./index.html",
    "./src/**/*.{js,ts,jsx,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        obsidian: {
          950: '#070F1E',
          900: '#0A192F',
          800: '#112240',
          700: '#1E3A5F',
          600: '#233554'
        },
        bioteal: {
          DEFAULT: '#64FFDA',
          400: '#64FFDA',
          500: '#00F5D4',
          600: '#00B4D8'
        },
        bioemerald: {
          DEFAULT: '#10B981',
          500: '#10B981',
          600: '#059669'
        },
        biotrap: {
          DEFAULT: '#F59E0B',
          500: '#F59E0B',
          600: '#D97706',
          danger: '#EF4444'
        }
      },
      fontFamily: {
        cairo: ['Cairo', 'sans-serif'],
        mono: ['JetBrains Mono', 'monospace']
      }
    },
  },
  plugins: [],
}
