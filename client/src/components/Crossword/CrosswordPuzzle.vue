<template>
  <div v-if="puzzleData" class="d-flex flex-column align-items-center w-100">
    <h2 class="mt-2 mb-2">{{ puzzleData.title }}</h2>
    <div :class="STYLES.MT_0" class="d-flex justify-content-between">

      
      <div :class="STYLES.W_100">
        <div v-for="(line, y) in puzzleGrid" :key="line" class="d-flex flex-row puzzle-letter">
          <div v-for="(char, x) in line" :key="char">
            <div :class="getSquareClass(x, y, char)">{{ getBlockContent(x, y, char) }}</div>
          </div>         
        </div>
      </div>


      <div :class="STYLES.W_100" class="unused-words ms-5" v-if="!isPrint && Object.keys(puzzleData.unusedWordClues).length > 0">
        <div class="form-check">
          <input 
            class="form-check-input" 
            type="checkbox"
            id="flexCheckDefault"
            v-model="showUnusedWordClues"
          >
          <label class="form-check-label mb-2" for="flexCheckDefault">
            Show Unused Word Clue Pairs
          </label>
        </div>
        <div v-if="showUnusedWordClues" class="text-danger">
          <h5 class="text-start">Unused Word Clue Pairs</h5>
          <!-- <div v-for="word in puzzleData.unusedWordClues" :key="word"> -->
          <div v-for="key in Object.keys(puzzleData.unusedWordClues)" :key="key" class="text-start">
            {{ `${key}: ${puzzleData.unusedWordClues[key]}` }}
          </div>
        </div>
      </div>


    </div>
    <div :class="[STYLES.D_FLEX, STYLES.W_75, STYLES.MT_2]">
      <CrosswordClueList
        listTitle="Across"
        :clueList="puzzleData.acrossClueList"
      />
      <CrosswordClueList
        listTitle="Down"
        :clueList="puzzleData.downClueList"
      />
    </div>
  </div>
</template>

<script setup>
import { computed, ref, reactive, watch } from "vue";
import { STYLES } from "@/constants";
import CrosswordClueList from "@/components/Crossword/CrosswordClueList.vue";

const props = defineProps({
  puzzleData: { type: Object, default: null },
  isPrint: { type: Boolean, default: false },
});

let currentClueNumber = 0;
const showUnusedWordClues = ref(true);

const puzzleGrid = computed(() => {
  if (!props.puzzleData || !Object.keys(props.puzzleData).length > 0) 
    return [];
  
  let horizontalBorder = "";
  for (let i = 0; i < props.puzzleData.width + 2; i++) {
    horizontalBorder += '.';
  }

  let lineArray = [horizontalBorder];
  for (let line = 1; line <= props.puzzleData.height; line++) {
    const start = line * props.puzzleData.width - props.puzzleData.width;
    const end = line * props.puzzleData.width;
    lineArray.push(`.${props.puzzleData.gridString.slice(start, end)}.`);
  }
  lineArray.push(horizontalBorder);
  return lineArray;
});

const getSquareClass = (x, y, char) => {
  const dimensionsClass = y === 0 ? 'top-block-dimensions' : 'block-dimensions';
  let styleClasses = `${dimensionsClass} border-black`;

  if (!isBlank(char))
    return `${styleClasses} border letter-padding`;

  if (hasCharToLeft(x, y))
    styleClasses += " border-start";
  if (hasCharToRight(x, y))
    styleClasses += " border-end";
  if (hasCharAbove(x, y))
    styleClasses += " border-top";
  if (hasCharBelow(x, y))
    styleClasses += " border-bottom";

  return styleClasses;
}
  
// const getSquareClass = (char) => isBlank(char)
//   ? "border border-black block-dimensions black-background"
//   : "border border-black block-dimensions letter-padding";

function getBlockContent(x, y, char) {
  if (!isBlank(char) &&
    ((!hasCharToLeft(x, y) && hasCharToRight(x, y))
    || (!hasCharAbove(x, y) && hasCharBelow(x, y)))
  ) {
    currentClueNumber++;
    return currentClueNumber;
  }
  return null;
}

const isBlank = (char) => char === '.';
const hasCharToLeft = (x, y) => x !== 0 && !isBlank(puzzleGrid.value[y].charAt(x - 1));
const hasCharToRight = (x, y) => puzzleGrid.value[y].length > x + 1 && !isBlank(puzzleGrid.value[y].charAt(x + 1));
const hasCharAbove = (x, y) => y !== 0 && !isBlank(puzzleGrid.value[y - 1].charAt(x));
const hasCharBelow = (x, y) => puzzleGrid.value.length > y + 1 && !isBlank(puzzleGrid.value[y + 1].charAt(x));

</script>

<style scoped>
.puzzle-letter {
  font-size: 10px;
  text-align: start;
}

.letter-padding {
  padding-left: 3px;
}

.block-dimensions {
  width: 35px;
  height: 35px;
}

.top-block-dimensions {
  width: 35px;
  height: 10px;
}

.black-background {
  background-color: black;
}
</style>