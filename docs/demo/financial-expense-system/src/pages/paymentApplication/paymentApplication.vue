<template>
  <div class="paymentApplication_all">
    <router-view v-slot="{ Component, route }" :key="route.path + route.query.t">
      <keep-alive v-if="route.meta.keepAlive">
        <component :is="Component" />
      </keep-alive>
      <component v-else :is="Component" />
    </router-view>
  </div>
</template>

<script setup lang="ts">
import { useStore } from 'vuex';
import { useRoute, useRouter } from 'vue-router';
const store = useStore(); // Vuex store 实例
const route = useRoute(); // 路由信息对象
const router = useRouter(); // 路由实例
// const { proxy } = getCurrentInstance() as any // proxy 替代 this
const { appContext } = getCurrentInstance() as ComponentInternalInstance;
const proxy = appContext.config.globalProperties;
const emit = defineEmits(['btnClick']);

const propObj = defineProps({
  tableData: {
    type: Array,
    default: () => {
      return [];
    },
  },
});

const data = reactive({
  userInfo: store.state.userInfo,
  tableData: propObj.tableData,
  path: route.path,
  moneyTotal: 0,
  number: 0,
  flag: true as any,
}); // 声明一个 reactive响应式对象
// 使用toRefs解构
// let { } = { ...toRefs(data) }
// let { flag } = { ...toRefs(data) }

// 路由跳转
const routerPush = () => {
  router.push({ path: data.path });
};
onBeforeMount(() => {}); // 组件挂载之前执行
onMounted(() => {}); // 组件挂载到页面之后执行
watch(
  () => data.moneyTotal,
  val => {
    console.log('val', val);
  },
  { immediate: true, deep: true }
);
watchEffect(() => {}); // watchEffect 监听响应式数据变化
data.flag = computed(() => data.number % 2 == 0); // computed 计算属性
defineExpose({
  ...toRefs(data),
  routerPush,
  proxy,
}); // 暴露给父组件使用
</script>
<style scoped lang="less">
.paymentApplication_all {
  width: 100%;
  height: 100%;
}
</style>
