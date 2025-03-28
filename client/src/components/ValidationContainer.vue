<template>
  <div v-if="validationErrors?.length > 0" class="text-danger text-start mb-2">
    <div v-for="(error, index) in validationErrors" :key="index">
      {{ error }}
    </div>
  </div>
</template>

<script setup>
const props = defineProps({ 
  validationErrors: { type: Array, default: [] } 
});
</script>