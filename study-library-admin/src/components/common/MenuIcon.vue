<template>
  <span class="menu-icon">
    <SvgIcon :name="icon" :size="16" color="#ffffff" />
    <span v-if="fallbackVisible" class="menu-fallback">{{ title.slice(0, 1) }}</span>
  </span>
</template>

<script setup lang="ts">
import { computed } from 'vue';
import SvgIcon from '@/components/svgIcon/svgIcon.vue';

const props = defineProps<{
  icon: string;
  title: string;
}>();

const fallbackVisible = computed(() => !props.icon);
</script>

<style scoped lang="scss">
.menu-icon {
  width: 22px;
  height: 22px;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  margin-right: 10px;
  border-radius: 8px;
  background: rgba(255, 255, 255, 0.12);
}

.menu-fallback {
  font-size: 12px;
  font-weight: 700;
  color: #ffffff;
}
</style>
