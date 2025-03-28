<template>
  <div class="mt-3 d-flex flex-column">
    <div class="button-row align-self-end me-0 pe-0">
      <button
        type="button" 
        :class="[STYLES.BUTTON_PRIMARY, STYLES.MB_3]"
        @click="emit('goToPuzzleEdit')"
      >
        Create New Puzzle
      </button>
    </div>
    <div class="text-start w-100">
      <div v-if="!userId" class="text-center">
        <div>
          <span class="text-primary" @click="emit('login')">Log in</span>
          to see your puzzle list,
        </div>
        <div>or click 'Create New Puzzle' to get started.</div>
      </div>
      <ListingTable
        v-else-if="listingData?.length > 0"
        class="h-100"
        :puzzleType="puzzleType"
        :listingData="listingData"
        @edit="(record) => emit('goToPuzzleEdit', record)"
        @view="(record) => emit('goToPuzzleView', record)"
        @delete="deletePuzzle"
      />
      <div v-else class="text-center">
        {{ `You have no ${toTitleCase(puzzleType)} puzzles. Click 'Create New Puzzle' to get started.` }}
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, watch } from 'vue';
import { displayToast, toTitleCase } from '@/helpers';
import { CONSTANTS, STYLES } from '@/constants';
import DynamicService from '@/services/DynamicService';
import ListingTable from '@/components/ListingTable.vue';

const props = defineProps({
  puzzleType: { type: String, default: null },
  userId: { type: Number, default: null },
});

const emit = defineEmits(['goToPuzzleView', 'goToPuzzleEdit', 'login']);
defineExpose({ getListingData });

const isLoggingIn = ref(false);
const listingData = ref([]);
async function getListingData() {
  listingData.value = (await DynamicService.getByUser(props.userId, props.puzzleType)).data;
  formatListingData();
}

function formatListingData() {
  switch (props.puzzleType) {
    case CONSTANTS.WORDSEARCH:
      listingData.value = listingData.value.map(item => (
        { ...item, words: item.wordCollection?.length }
      ));
      break;
    case CONSTANTS.CROSSWORD:
      listingData.value = listingData.value.map(item => (
        { ...item, words: item.wordClues ? Object.keys(item.wordClues)?.length : 0 }
      ));
      break;
    case CONSTANTS.SUDOKU:
      listingData.value = listingData.value.map(item => (
        { ...item, formattedDifficulty: `Level ${item.difficulty}` }
      ));
      break;
  }
}

async function deletePuzzle(record) {
  try {
    await DynamicService.delete(record.id, props.puzzleType);
    await getListingData();
    displayToast(`${toTitleCase(props.puzzleType)} Deleted!`, CONSTANTS.SUCCESS);
  } catch (error) {
    displayToast('Delete Error!', CONSTANTS.ERROR);
  }
}

watch(
  () => [props.userId, props.puzzleType],
  () => {
    if (props.userId && props.puzzleType)
      getListingData()
  },
  { immediate: true }
)
</script>

<style>
.text-primary:hover {
  cursor: pointer;
}
</style>