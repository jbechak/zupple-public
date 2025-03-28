<template>
  <div v-if="puzzleData">
    <div class="d-flex flex-column align-items-center pt-2">
      <h2 v-if="puzzleData.showTitle" class="mb-0">{{ puzzleData.title }}</h2>
      <h5 v-if="puzzleData.showDifficulty" class="mb-0">{{ `Level ${puzzleData.difficulty}` }}</h5>
      <div :class="STYLES.MT_2" class="border border-black border-3">
        <div v-for="(line, y) in puzzleGrid" :key="line" class="d-flex flex-row puzzle-letter">
          <div v-for="(number, x) in line" :key="number">
            <div :class="getBlockClass(x, y)">{{ number !== '0' ? number : ' ' }}</div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";
import { STYLES } from "@/constants";

const props = defineProps({
  puzzleData: { type: Object, default: null },
});

const puzzleGrid = computed(() => {
  let lineArray = [];
  if (props.puzzleData?.gridString?.length >= 81) {
    for (let line = 1; line <= 9; line++) {
      const start = line * 9 - 9;
      const end = line * 9;
      const numberArray = props.puzzleData.gridString.slice(start, end).split('');
      lineArray.push(numberArray);
    }
  }
  return lineArray;
});

function getBlockClass(x, y) {
  let styleClass = "grid-block";

  if ([2, 5].includes(x))
    styleClass += " thick-border-right"
  if ([2, 5].includes(y))
    styleClass += " thick-border-bottom"
  if ([3, 6].includes(x))
    styleClass += " thick-border-left"
  if ([3, 6].includes(y))
    styleClass += " thick-border-top"

  return styleClass;
}
</script>

<style scoped>
.puzzle-letter {
  font-size: 20px;
  font-family: monospace;
  text-align: center;
}

.grid-block {
  width: 40px;
  height: 40px;
  border: solid;
  border-color: black;
  border-width: 1px;
}

.thick-border-left {
  border-left-width: 2px !important;
}
.thick-border-right {
  border-right-width: 2px !important;
}
.thick-border-top {
  border-top-width: 2px !important;
}
.thick-border-bottom {
  border-bottom-width: 2px !important;
}
</style>