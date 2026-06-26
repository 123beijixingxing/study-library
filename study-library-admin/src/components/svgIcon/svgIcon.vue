<template>
  <svg class="svg-icon" :style="iconStyle" aria-hidden="true">
    <use :href="symbolId" />
  </svg>
</template>

<script setup lang="ts">
import { computed } from 'vue';

const props = withDefaults(
  defineProps<{
    name: string;
    size?: number | string;
    color?: string;
  }>(),
  {
    size: 18,
    color: 'currentColor',
  }
);

const symbolId = computed(() => `#icon-${props.name}`);
const iconStyle = computed(() => ({
  width: typeof props.size === 'number' ? `${props.size}px` : props.size,
  height: typeof props.size === 'number' ? `${props.size}px` : props.size,
  color: props.color,
}));
</script>

<style scoped>
.svg-icon {
  display: inline-block;
  fill: currentColor;
  overflow: hidden;
  vertical-align: middle;
}
</style>
