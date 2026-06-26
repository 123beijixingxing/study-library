<script lang="ts" setup>
import { computed } from 'vue';
const props = defineProps({
  prefix: {
    type: String,
    default: 'icon',
  },
  name: {
    type: String,
    required: true,
  },
  color: {
    type: String,
    default: '#333333',
  },
});
const symbolId = computed(() => `#${props.prefix}-${props.name}`);
</script>

<template>
  <svg aria-hidden="true" class="icon">
    <use :xlink:href="symbolId" />
  </svg>
</template>

<style lang="scss" scoped>
.icon {
  display: inline-block;
  width: 14px;
  height: 14px;
  vertical-align: middle;
  fill: currentColor;
  color: #bbb;
}
</style>
