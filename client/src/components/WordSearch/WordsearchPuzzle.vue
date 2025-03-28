<template>
  <div v-if="puzzleData">
    <h2 class="mt-2 mb-0">{{ puzzleData.title }}</h2>
    <h5 v-if="puzzleData.showDifficulty" class="mb-0">{{ puzzleData.difficulty }}</h5>
    <div class="mb-3 mt-1">
      {{ puzzleData.instructions }}
    </div>
    <div class="d-flex flex-row justify-content-evenly mt-4">
      <div>
        <div v-for="line in puzzleGrid" :key="line" class="puzzle-letter">
          {{ line }}
        </div>
      </div>
      <div>
        <h5>Word List</h5>
        <div v-for="word in puzzleData.usedWords" :key="word">
          {{ word }}
        </div>
        <div class="mt-3" v-if="!isPrint && puzzleData.unusedWords?.length > 0">
          <div class="form-check">
            <input 
              class="form-check-input" 
              type="checkbox"
              id="flexCheckDefault"
              v-model="showUnusedWords"
            >
            <label class="form-check-label mb-2" for="flexCheckDefault">
              Show Unused Words
            </label>
          </div>
          <div v-if="showUnusedWords" class="text-danger">
            <h5>Unused Words</h5>
            <div v-for="word in puzzleData.unusedWords" :key="word">
              {{ word }}
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, ref } from "vue";

const props = defineProps({
  puzzleData: { type: Object, default: null },
  isPrint: { type: Boolean, default: false },
});

const showUnusedWords = ref(true);

const puzzleGrid = computed(() => {
  if (!props.puzzleData || !Object.keys(props.puzzleData).length > 0) 
    return [];
  
  let lineArray = [];
  for (let line = 1; line <= props.puzzleData.height; line++) {
    const start = line * props.puzzleData.width - props.puzzleData.width;
    const end = line * props.puzzleData.width;
    lineArray.push(props.puzzleData.gridString.slice(start, end));
  }
  return lineArray;
});

</script>

<style scoped>
.puzzle-letter {
  font-size: 20px;
  font-family: monospace;
  letter-spacing: 20px;
}
</style>