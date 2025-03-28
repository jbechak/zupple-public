<template>
  <div :class="[STYLES.BUTTON_CONTAINER, STYLES.W_100]">
    <div class="button-row">
      <button
        v-if="isNew"
        type="button" 
        :class="[STYLES.BUTTON_PRIMARY, STYLES.MB_2]"
        @click="emit('generate')"
      >
        Generate Puzzle
      </button>
      <span v-else>
        <button
          type="button" 
          :class="regenerateButtonClass"
          @click="emit('generate')"
        >
          Re-Generate Puzzle
        </button>
        <button
          type="button" 
          :class="[STYLES.BUTTON_PRIMARY, STYLES.MB_2, STYLES.MS_1]"
          @click="emit('generateAsCopy')"
        >
          Generate as Copy
        </button>
      </span>
      <button
        type="button" 
        :class="[STYLES.BUTTON_SECONDARY, STYLES.MB_2, STYLES.MS_1]"
        @click="emit('reset')"
      >
        Reset
      </button>
    </div>
  </div>
</template>

<script setup>
import { STYLES } from "@/constants";
import { computed } from "vue";

const props = defineProps({
  isNew: { type: Boolean, default: false },
  mustGenerateToSave: { type: Boolean, default: false },
});
const emit = defineEmits(['generate', 'generateAsCopy', 'reset', 'save']);

const regenerateButtonClass = computed(() => props.mustGenerateToSave
  ? [STYLES.BUTTON_WARNING, STYLES.MB_2, STYLES.MS_1]
  : [STYLES.BUTTON_PRIMARY, STYLES.MB_2, STYLES.MS_1]
);
</script>