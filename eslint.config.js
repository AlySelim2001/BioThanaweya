/** @type {import('eslint').Linter.Config[]} */
import tseslint from 'typescript-eslint';

export default [
  {
    files: ["**/*.ts", "**/*.tsx"],
    languageOptions: {
      ecmaVersion: 2024,
      sourceType: "module",
      parser: tseslint.parser,
    },
    plugins: {
      "@typescript-eslint": tseslint.plugin,
    },
    rules: {
      "@typescript-eslint/no-unused-vars": ["error", {"argsIgnorePattern": "^_", "varsIgnorePattern": "^_"}],
      "no-console": ["warn", { allow: ["warn", "error"] }],
    },
  },
  {
    files: ["**/*.js", "**/*.jsx"],
    languageOptions: {
      ecmaVersion: 2024,
      sourceType: "module",
    },
    rules: {
      "no-console": ["warn", { allow: ["warn", "error"] }],
    },
  },
  {
    ignores: ["dist/", "node_modules/", "archive/", "**/*.generated.*", "**/web-dist/", "android/app/build/", "vite.config.ts", "android/app/src/main/assets/public/assets/vendor-core-B2G20Us5.js", "src/test/", "lib/**/*test*.dart"],
  },
];