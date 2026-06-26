import { resolve } from 'path';
import { defineConfig, loadEnv } from 'vite';
import vue from '@vitejs/plugin-vue';
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons';

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  const proxyTarget = env.VITE_DEV_PROXY_TARGET || 'http://127.0.0.1:8080';

  return {
    plugins: [
      vue(),
      createSvgIconsPlugin({
        iconDirs: [resolve(process.cwd(), 'src/assets/images/svg')],
        symbolId: 'icon-[name]',
      }),
    ],
    resolve: {
      alias: {
        '@': resolve(__dirname, './src'),
      },
    },
    server: {
      host: '0.0.0.0',
      port: 10001,
      open: false,
      proxy: {
        '/api': {
          target: proxyTarget,
          changeOrigin: true,
          configure(proxy) {
            proxy.on('proxyReq', proxyReq => {
              proxyReq.removeHeader('origin');
            });
          },
        },
      },
    },
    css: {
      preprocessorOptions: {
        scss: {
          api: 'modern-compiler',
        },
      },
    },
    build: {
      chunkSizeWarningLimit: 950,
      rollupOptions: {
        output: {
          manualChunks(id) {
            if (!id.includes('node_modules')) {
              return undefined;
            }

            if (
              id.includes('element-plus') ||
              id.includes('@element-plus') ||
              id.includes('@floating-ui') ||
              id.includes('async-validator') ||
              id.includes('@vueuse/core') ||
              id.includes('@vueuse/shared') ||
              id.includes('vue-router') ||
              id.includes('vuex') ||
              id.includes('vue-i18n') ||
              id.includes('node_modules/vue/') ||
              id.includes('node_modules\\vue\\')
            ) {
              return 'framework';
            }

            if (id.includes('axios')) {
              return 'network';
            }

            return 'vendor';
          },
        },
      },
    },
  };
});
