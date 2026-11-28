/** @type {import('eslint').Linter.Config[]} */
import tseslint from '@typescript-eslint/eslint-plugin';
import tsParser from '@typescript-eslint/parser';
import reactHooks from 'eslint-plugin-react-hooks';
import reactRefresh from 'eslint-plugin-react-refresh';
import importPlugin from 'eslint-plugin-import';

export default [
  {
    files: ['**/*.ts', '**/*.tsx'],
    languageOptions: {
      ecmaVersion: 2024,
      sourceType: 'module',
      parser: tsParser,
    },
    plugins: {
      '@typescript-eslint': tseslint,
      reactHooks,
      reactRefresh,
      import: importPlugin,
    },
    rules: {
      '@typescript-eslint/no-unused-vars': ['error', { argsIgnorePattern: '^_', varsIgnorePattern: '^_' }],
      '@typescript-eslint/no-explicit-any': 'warn',
      '@typescript-eslint/strict-boolean-expressions': 'off',
      'react-hooks/exhaustive-deps': 'warn',
      'react-hooks/rules-of-hooks': 'error',
      'react-refresh/only-export-components': ['warn', { allowConstantExport: true }],
      'no-console': ['warn', { allow: ['warn', 'error'] }],
      'import/no-cycle': ['error', { ignorePatterns: ['**/*.test.*', '**/*.spec.*'] }],
    },
  },
  {
    files: ['**/*.js', '**/*.jsx'],
    languageOptions: {
      ecmaVersion: 2024,
      sourceType: 'module',
    },
    plugins: {
      reactHooks,
      reactRefresh,
      import: importPlugin,
    },
    rules: {
      'react-hooks/exhaustive-deps': 'warn',
      'react-refresh/only-export-components': ['warn', { allowConstantExport: true }],
      'no-console': ['warn', { allow: ['warn', 'error'] }],
    },
  },
  {
    ignores: [
      'dist/',
      'node_modules/',
      'archive/',
      '**/*.generated.*',
      '**/web-dist/',
      'android/app/build/',
      'vite.config.ts',
      'android/app/src/main/assets/public/assets/vendor-core-B2G20Us5.js',
      'src/test/',
      'src/services/*.test.ts',
      'src/server/*.test.ts',
      'lib/**/*test*.dart',
    ],
  },
];