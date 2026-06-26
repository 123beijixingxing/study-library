import { defineConfig, loadEnv } from 'vite'; // vite配置
import vue from '@vitejs/plugin-vue'; // vue支持
import vueJsx from '@vitejs/plugin-vue-jsx'; // jsx语法
import { resolve } from 'path'; // 路径解析
// import { fileURLToPath } from 'url' // 文件路径转换

// import VueDevTools from 'vite-plugin-vue-devtools' // vue调试工具
import AutoImport from 'unplugin-auto-import/vite'; // 自动引入api
import Components from 'unplugin-vue-components/vite'; // 自动引入组件
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'; // 自动引入组件
import Icons from 'unplugin-icons/vite'; // 自动引入图标
import IconsResolver from 'unplugin-icons/resolver'; // 自动引入图标
import { createSvgIconsPlugin } from 'vite-plugin-svg-icons'; // 自动引入svg图标
import { createStyleImportPlugin, AndDesignVueResolve, ElementPlusResolve } from 'vite-plugin-style-import'; // 自动引入样式
import postCssPxToRem from 'postcss-pxtorem'; // px转rem
import autoprefixer from 'autoprefixer'; // 自动添加浏览器前缀

// https://vite.dev/config/
export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  return {
    base: './',
    define: {
      'process.env': env,
    }, // 定义全局常量替换方式
    plugins: [
      vue(),
      vueJsx(),
      createSvgIconsPlugin({
        iconDirs: [resolve(process.cwd(), 'src/assets/images/svg')],
        symbolId: 'icon-[name]',
      }),
      // VueDevTools(), // vue调试工具
      // 自动导入api
      AutoImport({
        imports: ['vue', 'vue-router', 'pinia', '@vueuse/core'],
        dts: 'src/auto-imports.d.ts',
        resolvers: [IconsResolver(), ElementPlusResolver()],
      }),
      // 自动导入组件
      Components({
        resolvers: [ElementPlusResolver()],
      }),
      // 自动导入图标
      Icons({
        compiler: 'vue3', // 使用vue3
        autoInstall: true, // 自动安装图标库
      }),
      // 自动引入样式
      createStyleImportPlugin({
        resolves: [AndDesignVueResolve(), ElementPlusResolve()],
      }),
    ],
    css: {
      devSourcemap: true, // 可以查看 CSS 的源码
      postcss: {
        plugins: [
          postCssPxToRem({
            rootValue: 16, // 1rem的大小
            propList: ['*'], // 需要转换的属性，*代表全部转换
          }),
          autoprefixer({
            overrideBrowserslist: ['Android 4.1', 'iOS 7.1', 'Chrome > 31', 'ff > 31', 'ie >= 8'],
          }),
        ],
      },
    },
    resolve: {
      alias: {
        '@': resolve(__dirname, './src'),
        // '@': fileURLToPath(new URL('./src', import.meta.url)),
      },
      // 导入时想要省略的扩展名列表。 vite官方不建议忽略自定义导入类型的扩展名（例如：.vue），因为它会影响 IDE 和类型支持。
      // extensions: ['.mjs', '.js', '.mts', '.ts', '.jsx', '.tsx', '.json'],
    },
    build: {
      // outDir: 'dist', // 默认dist（可省略），打包后输出文件
      // assetsDir: 'assets', // 默认assets（可省略），指定静态资源存放路径
      sourcemap: false, // 默认false（可省略），是否构建sourcemap文件（生产不需要）
      minify: 'terser', // 客户端默认构建是esbuild，需安装terser：`npm i -D terser`
      terserOptions: {
        // 生产环境移除console、debugger
        compress: {
          drop_console: true, // 默认false
          drop_debugger: true, // 默认true
        },
      },
    },
    server: {
      host: '0.0.0.0', // 指定服务器应该监听哪个 IP 地址，默认localhost，可设置为'0.0.0.0'或 true
      port: 9999, // 端口号，默认5173
      open: false, // 开发服务器启动时，自动在浏览器中打开应用程序
      // 本地代理
      proxy: {
        '^/apiTest': {
          target: 'http://127.0.0.1:4523/m1/6762483-6474435-default',
          changeOrigin: true,
          // pathRewrite: {
          //   '^/apiTest': '',
          // },
        },
        // 简写（字符串）
        '/mock': 'http://127.0.0.1:4523/m1/6762483-6474435-default',
        // 带选项写法（对象）
        '/api': {
          target: 'http://192.168.9.243', // 从环境变量文件取值
          changeOrigin: true, // 支持跨域
          rewrite: path => path.replace(/^\/api/, ''), // 路径重写
        },
        // 代理 websockets 或 socket.io 写法：ws://localhost:5173/socket.io -> ws://localhost:5174/socket.io
        '/socket.io': {
          target: 'ws://localhost:5174',
          // 支持 websocket
          ws: true,
        },
      },
    },
  };
});
