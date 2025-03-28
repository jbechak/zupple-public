<template>
  <div ref="document">
    <component :is="selectedComponent" :puzzleData="puzzleData" isPrint />
  </div>
</template>

<script setup>
import { computed, watch } from "vue";
import { CONSTANTS } from "@/constants";
import { getComponent } from "@/helpers";
import DynamicService from '@/services/DynamicService';
//import Vue3Html2pdf from 'vue3-html2pdf'

const props = defineProps({
  puzzleType: { type: String, default: null },
  puzzleId: { type: String, default: null },
});

// const html2Pdf = ref(null);

const selectedComponent = computed(() =>
  getComponent(props.puzzleType, CONSTANTS.PUZZLE)
);

const { data: puzzleData, isFetching: isFetching } = DynamicService.useGet(props.puzzleId, props.puzzleType);

async function printPuzzle() {
  setTimeout(() => {
    window.print();
  }, 500);
}

watch(
  isFetching,
  (value) => {
    if (!value && puzzleData.value) {
      printPuzzle();
    }
  },
  { immediate: true }
);
</script>

<style scoped>
@page {
  size: A4;
  margin-top: 0;
  margin-bottom: 0;
}
</style>