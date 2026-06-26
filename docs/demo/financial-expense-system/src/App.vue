<script setup lang="ts">
import { useRoute } from 'vue-router';
const route = useRoute();
</script>

<template>
  <router-view v-slot="{ Component, route }" :key="route.path + route.query.t">
    <keep-alive v-if="route.meta.keepAlive">
      <component :is="Component" />
    </keep-alive>
    <component v-else :is="Component" />
  </router-view>
</template>

<style lang="scss">
@import '@/assets/style/common.scss';
@import '@/assets/style/reset.scss';
@import '@/assets/style/element-plus.reset.scss';
#app {
  height: 100%;
  width: 100%;
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  /* text-align: center; */
  color: #2c3e50;
}
html,
body {
  width: 100%;
  height: 100%;
  /* max-width: 1920px; */
  font-size: 16px;
  margin: 0px auto;
  padding: 0px;
  font-family: 'Myriad Set Pro', 'Helvetica Neue', 'Helvetica,verdana, geneva, arial, helvetica, sans-serif';
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  font-style: normal;
  font-weight: normal;
}
body,
ul,
ol,
li,
span,
p,
h1,
h2,
h3,
h4,
h5,
h6,
form,
fieldset,
table,
td,
img,
div {
  margin: 0px;
  padding: 0px;
  border: 0px;
}
li,
a,
ul {
  text-decoration: none;
  list-style: none;
}
button {
  outline: 0px;
  margin: 0px;
}
img {
  border: 0px;
  // width: 100%;
  // height: 100%;
  // display: block;
}
a {
  color: inherit;
  text-decoration: none !important;
}
</style>
