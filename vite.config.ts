import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import tailwindcss from '@tailwindcss/vite';
// Matches the React core packages only, so `lucide-react` and other
// React-aware add-ons land in their own chunks instead of vendor-react.
const REACT_CORE_PATTERN = /[\\/]node_modules[\\/](?:react|react-dom|scheduler)[\\/]/;

// The Android shell (android/app/src/main/java/.../MainActivity.kt) serves this
// bundle through WebViewAssetLoader from
// https://appassets.androidplatform.net/assets/public/, so every emitted asset
// URL must be relative. Browser/PWA deployments keep the root-absolute base.
const isCapacitorBuild = process.env.CAPACITOR_BUILD === 'true';

// https://vitejs.dev/config/
export default defineConfig({
  base: isCapacitorBuild ? './' : '/',
  server: {
    host: '0.0.0.0',
    port: 3000,
    strictPort: true,
    hmr: false,
    watch: {
      usePolling: false,
    },
  },
  plugins: [
    react(),
    tailwindcss(),
    {
      name: 'vite-network-diagnostic',
      configureServer(server) {
        server.httpServer?.on('listening', () => {
          const address = server.httpServer?.address();
          if (address && typeof address === 'object') {
            console.log(
              `[vite-diagnostic] Dev server successfully bound to http://${address.address}:${address.port} (family: ${address.family})`
            );
          }
        });
        server.httpServer?.on('error', (err: NodeJS.ErrnoException) => {
          console.error('[vite-diagnostic-error] Server failed to bind to port 3000:', {
            code: err.code,
            errno: err.errno,
            syscall: err.syscall,
            message: err.message,
            stack: err.stack,
          });
          if (err.code === 'EADDRINUSE') {
            console.error(
              '[vite-diagnostic-error] CRITICAL: Port 3000 is already in use by another process. Run "npm run clean" or "fuser -k 3000/tcp" to release the port.'
            );
          }
        });
      },
    },
  ],
  build: {
    chunkSizeWarningLimit: 600,
    rollupOptions: {
      output: {
        manualChunks(id) {
          if (!id.includes('node_modules')) return undefined;
          if (REACT_CORE_PATTERN.test(id)) return 'vendor-react';
          if (id.includes('framer-motion')) return 'vendor-motion';
          if (id.includes('lucide-react')) return 'vendor-icons';
          if (
            id.includes('canvas-confetti') ||
            id.includes('zod') ||
            id.includes('dexie') ||
            id.includes('@google/genai')
          ) {
            return 'vendor-utils';
          }
          return 'vendor-core';
        },
      },
    },
  },
});
