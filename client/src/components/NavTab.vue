<template>
  <button 
    :class="[STYLES.NAV_LINK, selectedTab === props.tabName ? STYLES.ACTIVE : '', props.disabled ? STYLES.DISABLED : '']" 
    :id="`nav-${tabName}-tab`" 
    data-bs-toggle="tab" 
    :data-bs-target="`#nav-${tabName}`" 
    type="button" 
    role="tab" 
    :aria-controls="`nav-${tabName}`" 
    aria-selected="true" 
    @click="emit('tabSelected', tabName)"
  >
    {{ tabLabel }}
  </button>
</template>

<script setup>
import { STYLES } from "@/constants";
//import { defineProps, defineEmits } from 'vue';

const props = defineProps({
  tabName: {
    type: String,
    default: null
  },
  selectedTab: {
    type: String,
    default: null
  },
  tabLabel: {
    type: String,
    default: null
  },
  disabled: {
    type: Boolean,
    default: false
  },
});

const emit = defineEmits(['tabSelected']);

</script>