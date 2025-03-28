<template>
  <th 
    :class="getHeaderClass()" 
    scope="col" 
    @click="sortBy(listingData, column, puzzleType, isNumber)"
  >
  {{ label }}
  <span :class="iconContainerClass()">  
    <font-awesome-icon
      v-if="sortColumn[puzzleType].isDesc"
      class="align-self-center"
      size="lg"
      icon="sort-down"
    />
    <font-awesome-icon
      v-else
      class="align-self-center"
      size="lg"
      icon="sort-up"
    />
  </span>
</th>
</template>

<script setup>
import { STYLES } from '@/constants';
import { sortBy, sortColumn } from "@/helpers";

const props = defineProps({
  listingData: { type: Array, default: [] },
  column: { type: String, default: null },
  label: { type: String, default: null },
  puzzleType: { type: String, default: null },
  isNumber: { type: Boolean, default: false },
});

const getHeaderClass = () => props.isNumber ? [STYLES.TABLE_HEADER, STYLES.TEXT_END] : STYLES.TABLE_HEADER;

const iconContainerClass = () => sortColumn[props.puzzleType].name === props.column ? null : "opacity-0";

</script>

