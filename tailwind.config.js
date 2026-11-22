/** @type {import('tailwindcss').Config} */
export default {
  content: ['./index.html', './src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        obsidian: {
          950: '#0B132B',
          900: '#0F1A3A',
          800: '#1A284A',
          700: '#233554',
          600: '#3A4B6B',
          50: '#E8EEF5',
          100: '#F0F4FA',
        },
        glass: {
          DEFAULT: 'var(--glass-medium)',
          light: 'var(--glass-light)',
          strong: 'var(--glass-strong)',
        },
        bioteal: {
          DEFAULT: '#00F5D4',
          400: '#00F5D4',
          500: '#00B4D8',
          600: '#5AF0E8',
          100: '#CCFCF5',
          200: '#99F6E0',
        },
        bioemerald: {
          DEFAULT: '#10B981',
          500: '#10B981',
          600: '#059669',
          100: '#CCFDF5',
          200: '#99F6E0',
        },
        biotrap: {
          DEFAULT: '#F59E0B',
          500: '#F59E0B',
          600: '#D97706',
          danger: '#EF4444',
          100: '#FEF3C7',
          200: '#FDE68A',
        },
      },
      fontFamily: {
        cairo: ['Cairo', 'sans-serif'],
        mono: ['JetBrains Mono', 'monospace'],
      },
    },
  },
  plugins: [],
};
