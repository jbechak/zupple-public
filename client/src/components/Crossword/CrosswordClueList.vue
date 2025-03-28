<template>
  <div :class="STYLES.LIST_ITEMS">
    <h4>{{ listTitle }}</h4>
    <div v-for="clue in clueList" :key="clue" class="d-flex">
      <div class="text-start me-2">
        {{ `${clue.split('.')[0]}. ` }}
      </div>
      <div class="text-start me-2">
        {{ clue.split('.')[1] }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { STYLES } from "@/constants";

const props = defineProps({
  listTitle: { type: String, default: null },
  clueList: { type: Array, default: [] },
});
</script>