import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';
import tailwindcss from '@tailwindcss/vite';
import { VitePWA } from 'vite-plugin-pwa';

// https://vitejs.dev/config/
export default defineConfig({
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
            console.log(`[vite-diagnostic] Dev server successfully bound to http://${address.address}:${address.port} (family: ${address.family})`);
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
            console.error('[vite-diagnostic-error] CRITICAL: Port 3000 is already in use by another process. Run "npm run clean" or "fuser -k 3000/tcp" to release the port.');
          }
        });
      },
    },
    VitePWA({
      registerType: 'autoUpdate',
      includeAssets: ['icon.svg', 'apple-touch-icon.png'],
      manifest: {
        id: '/',
        name: 'BioThanaweya Biology',
        short_name: 'BioThanaweya',
        description: 'Elite Egyptian Thanaweya Amma (Languages) Biology learning ecosystem and exam simulator.',
        theme_color: '#0B132B',
        background_color: '#0B132B',
        display: 'standalone',
        orientation: 'portrait-primary',
        start_url: '/',
        scope: '/',
        icons: [
          {
            src: '/pwa-192x192.png',
            sizes: '192x192',
            type: 'image/png',
            purpose: 'any',
          },
          {
            src: '/pwa-512x512.png',
            sizes: '512x512',
            type: 'image/png',
            purpose: 'any',
          },
          {
            src: '/pwa-maskable-512x512.png',
            sizes: '512x512',
            type: 'image/png',
            purpose: 'maskable',
          },
        ],
      },
      workbox: {
        globPatterns: ['**/*.{js,css,html,ico,png,svg,woff,woff2,jpg}'],
        navigateFallback: '/index.html',
      },
      devOptions: {
        enabled: true,
        type: 'module',
      },
    }),
  ],
  build: {
    chunkSizeWarningLimit: 600,
    rollupOptions: {
      output: {
        manualChunks(id) {
          if (id.includes('node_modules')) {
            if (id.includes('react') || id.includes('react-dom')) {
              return 'vendor-react';
            }
            if (id.includes('framer-motion')) {
              return 'vendor-motion';
            }
            if (id.includes('lucide-react')) {
              return 'vendor-icons';
            }
            if (id.includes('canvas-confetti') || id.includes('zod') || id.includes('idb')) {
              return 'vendor-utils';
            }
            return 'vendor-core';
          }
        },
      },
    },
  },
});
