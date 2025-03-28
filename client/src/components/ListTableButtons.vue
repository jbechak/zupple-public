<template>
    <td class="d-flex justify-content-end">
      <font-awesome-icon
        class="text-primary btn ps-0"
        size="lg"
        icon="pencil"
        @click="emit('edit', record)"
      />
      <font-awesome-icon
        class="text-primary btn ps-0"
        size="lg"
        icon="eye"
        @click="emit('view', record)"
      />
      <!-- <font-awesome-icon class="text-black-50 btn ps-0" size="lg" icon="play" /> -->
      <font-awesome-icon
        class="text-danger btn px-0"
        size="lg"
        icon="trash"
        @click="emit('delete', record)"
      />
    </td>
</template>

<script setup>
const props = defineProps({
  record: { type: Object, default: null },
})
const emit = defineEmits(['edit', 'view', 'delete']);

</script>

<style scoped>
font-awesome-icon:hover {
  cursor: pointer;
}
</style>