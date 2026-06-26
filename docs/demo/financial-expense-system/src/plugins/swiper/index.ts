import type { App } from 'vue';

// swiper 额外组件配置
// import SwiperCore from 'swiper'
// import Pagination from 'swiper'

// swiper 单独样式 （less / scss）
// import 'swiper/swiper.css'
// import 'swiper/css/free-mode'
// import 'swiper/css/pagination' // 轮播图底面的小圆点
// import 'swiper/css/navigation' // 轮播图两边的左右箭头
// import 'swiper/css/scrollbar' // 轮播图的滚动条
// import 'swiper/css/thumbs' // 轮播图的缩略图
// import 'swiper/css/pagination.min.css'
import 'swiper/swiper-bundle.css';

// swiper 必备组件
import { Swiper, SwiperSlide } from 'swiper/vue';

// 使用额外组件
// SwiperCore.use([Pagination])

// 全局注册 swiper 必备组件
const plugins = [Swiper, SwiperSlide];

const swiper = {
  install: function (app: App<Element>) {
    plugins.forEach((item: any) => {
      app.component(item.name, item);
    });
  },
};

export default swiper;

// 引入swiper核心和所需模块
// import { Autoplay, Pagination, Navigation, Scrollbar } from 'swiper'
// const modules = [Swiper, SwiperSlide, Autoplay, Pagination, Navigation, Scrollbar]

// export { Swiper, SwiperSlide }
