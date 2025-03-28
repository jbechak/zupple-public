<template>
  <div class="mt-3 d-flex flex-column align-items-start">
    <ValidationContainer :validationErrors="validationErrors"/>
    <div class="form-group required mb-2">
      <label for="wsTitle" class="me-1 label">Puzzle Title *</label>
      <input 
        id="wsTitle"
        name="wsTitle"
        type="text"
        v-model="formData.title"
        maxLength="50"
        :class="STYLES.FORM_INPUT"
        required
      />
    </div>
    <div :class="STYLES.FORM_GROUP">
      <label for="wsDirections" class="me-1 label">Word Directions *</label>
      <select
        id="wsDirections"
        name="wsDirections"
        v-model="formData.wordDirections"
        :class="STYLES.FORM_INPUT"
      >
        <option v-for="opt in wordDirections" :key="opt" :value="opt.value">{{ opt.text }}</option>
      </select>
    </div>
    <div class="d-flex mb-2">
      <div class="form-group me-1" style="width: 70px">
        <label for="wsWidth" class="me-1 label">Width *</label>
        <input 
          id="wsWidth"
          name="wsWidth"
          type="number"
          v-model="formData.width"
          min="3"
          max="50"
          :class="STYLES.FORM_INPUT"
          @input="(e) => nullIfEmpty(e, formData, 'width')"
        />
      </div>
      <div class="form-group" style="width: 70px">
        <label for="wsHeight" class="me-1 label">Height *</label>
        <input 
          id="wsHeight"
          name="wsHeight"
          type="number"
          v-model="formData.height"
          min="3"
          max="50"
          :class="STYLES.FORM_INPUT"
          @input="(e) => nullIfEmpty(e, formData, 'height')"
        />
      </div>
    </div>
    <div :class="STYLES.FORM_GROUP">
      <label for="wsWordList" class="me-1 label">{{ wordListLabel }}</label>
    </div>
    <div v-for="key in Object.keys(formData.wordListObj)" :key="key">
      <div :class="STYLES.D_FLEX">
        <input
          type="text"
          v-model="formData.wordListObj[key]"
          maxLength="50"
          :class="STYLES.FORM_INPUT"
          @input="capitalizeObjText(key)"
        />
        <font-awesome-icon
          :class="endIcon"
          size="2xl" 
          icon="xmark" 
          @click="delete formData.wordListObj[key]"
        />
      </div>
    </div>
    <div :class="STYLES.D_FLEX">
      <input
        ref="newWordInput"
        type="text"
        v-model="newWord"
        maxLength="50"
        :class="[STYLES.FORM_INPUT, STYLES.TEXT_SECONDARY]"
        @input="capitalizeNewWord(newWord)"
        @keydown.tab.prevent
        @keyup.tab="addNewWord"
        @keyup.enter="addNewWord"
        placeholder="Enter word here"
      />
      <font-awesome-icon
        class="text-primary"
        :class="endIcon"
        size="2xl" 
        icon="plus" 
        @click="addNewWord"
      />
    </div>
    <label for="difficulty" class="mt-2 label">Estimated Difficulty</label>
    <div id="difficulty" class="form-text mb-2">{{ difficulty }}</div>
    <div class="form-check">
      <input 
        class="form-check-input" 
        type="checkbox" 
        v-model="formData.showDifficulty" 
        id="flexCheckDefault">
      <label class="form-check-label" for="flexCheckDefault">
        Show Difficulty
      </label>
    </div>
    <GenerateButtonBar
      :isNew="!formData.id"
      :mustGenerateToSave="isDependentDataChanged"
      @generate="generatePuzzle"
      @generateAsCopy="generatePuzzle(true)"
      @reset="resetFormData(true)"
    />   
  </div>
</template>

<script setup>
import { reactive, ref, watch, computed } from "vue";
import { CONSTANTS, STYLES } from "@/constants";
import { getGuid } from "@/helpers";
import { displayToast, defaultFormData, nullIfEmpty } from "@/helpers";
import * as yup from 'yup';
import WordsearchService from "@/services/WordsearchService";
import GenerateButtonBar from "@/components/GenerateButtonBar.vue";
import ValidationContainer from "@/components//ValidationContainer.vue";

const props = defineProps({ formData: Object });
const emit = defineEmits(['goToPuzzleView']);
defineExpose({ setUpFormData });

//#region CONSTANTS
const endIcon = "ps-1 pb-1 align-self-center w-5 btn-hover"
const wordDirections = [
  { value: 1, text: 'Words can go horizontally and vertically' },
  { value: 2, text: 'Words can go horizontally, vertically, and in 2 diagonal directions' },
  { value: 3, text: 'Words can go horizontally, vertically, diagonally, and backwards in all directions' }
];

const difficultyEnum = {
  level1: "Level 1 - Very Easy",
  level2: "Level 2 - Easy",
  level3: "Level 3 - Medium",
  level4: "Level 4 - Difficult",
  level5: "Level 5 - Very Difficult",
}
//#endregion

//#region OTHER VARIABLES
const validationErrors = ref(null);

const wordListLabel = computed(() => { 
  const wordCount = props.formData.wordCollection.length;
  if (wordCount < 1) 
    return 'Word List *';
  
    const endClause = wordCount > 1 ? 'words) *' : 'word) *';
  return `Word List (${wordCount} ${endClause}`;
});

const difficulty = computed(() => {
  const points = (remainingSpaces() / 50)
    + (props.formData.wordDirections * 2 - 2)
    + ((props.formData.wordCollection.length - 1) / 10);

  if (points >= 8) {
    return difficultyEnum.level5;
  }
  else if (points >= 6) {
    return difficultyEnum.level4;
  }
  else if (points >= 3) {
    return difficultyEnum.level3;
  }
  else if (points >= 1) {
    return difficultyEnum.level2;
  }
  return difficultyEnum.level1;
});

function remainingSpaces() {
  const totalSpaces = props.formData.width * props.formData.height;
  const totalLetters = props.formData.wordCollection.reduce((acc, x) => acc + parseInt(x.length), 0);
  return totalSpaces - totalLetters;
}

const newWordInput = ref(null);
const newWord = ref();
const originalFormData = reactive({});

const isDependentDataChanged = computed(() => 
  JSON.stringify(props.formData.wordCollection) != JSON.stringify(originalFormData.wordCollection)
  || props.formData.width !== originalFormData.width
  || props.formData.height !== originalFormData.height
  || props.formData.wordDirections !== originalFormData.wordDirections
);
//#endregion

//#region FORM INPUT FUNCTIONS
function updateDimensionsIfNeeded() {
  const longestWordLength = props.formData.wordCollection?.length > 0
    ? props.formData.wordCollection?.map(x => x.replace(/\s+/g, '')).reduce((longest, current) => 
      longest.length > current.length ? longest : current)?.length
    : 0;

  const minDimension = longestWordLength + 1;
    if (!props.formData.height || minDimension > props.formData.height) props.formData.height = minDimension;
    if (!props.formData.width || minDimension > props.formData.width) props.formData.width = minDimension;
}

function capitalizeObjText(key) {
  props.formData.wordListObj[key] = props.formData.wordListObj[key].toUpperCase();
}

function capitalizeNewWord(text) {
  newWord.value = text.toUpperCase();
}

function addNewWord() {
  if (!newWord.value)
    return;

    props.formData.wordListObj[getGuid()] = newWord.value;
  newWord.value = null;
  newWordInput.value.focus();
}
//#endregion

//#region STANDARD FUNCTIONS
async function generatePuzzle(isCopy = false) {
  if (!validateForm())
    return;
  try {
    if (isCopy)
      props.formData.id = null;
    const result = await WordsearchService.generate(props.formData);
    emit('goToPuzzleView', {...result, id: props.formData.id, isSaveEnabled: true });
    displayToast("Wordsearch Generated!", CONSTANTS.SUCCESS);
  } catch(error) {
    displayToast("Error generating puzzle!", CONSTANTS.ERROR);
  }
}

const validationSchema = yup.object().shape({
  title: yup.string().nullable().required().max(50).label('Puzzle Title'),
  width: yup.number().nullable().positive().required().max(50).label('Width'),
  height: yup.number().nullable().positive().required().max(50).label('Height'),
  // TO DO word collection validation
  // TO DO test width and height to make sure thy're large enough for max word length
});

function validateForm() {
  validationErrors.value = [];
  try {
    validationSchema.validateSync(props.formData, { abortEarly: false });
    return true;
  } catch (error) {
    error.inner.forEach(e => {
        validationErrors.value.push(e.message);
    });
    return false;
  }
}
//#endregion

//#region SETUP FUNCTIONS
function setUpFormData(record) {
  if (record) {
    Object.assign(props.formData, record); 
    Object.assign(originalFormData, record);
    setUpWordListObj();
  } else {
    resetFormData();
  }
}

function setUpWordListObj() {
  props.formData.wordListObj = {};
  props.formData.wordCollection.forEach(x => props.formData.wordListObj[getGuid()] = x);
}

function resetFormData(keepId = false) {
  Object.assign(
    props.formData, 
    keepId 
      ? { ...defaultFormData.wordsearch, id: props.formData.id } 
      : defaultFormData.wordsearch
  );
  for (var member in props.formData.wordListObj) delete props.formData.wordListObj[member];
}
//#endregion

//#region WATCHERS
watch (
  () => Object.values(props.formData.wordListObj),
  (value) => {
    props.formData.wordCollection = [...value];
    updateDimensionsIfNeeded();
  }
);

watch (
  () => isDependentDataChanged.value,
  (value) => {
    props.formData.isSaveEnabled = !value;
  },
);

watch (
  () => props.formData.id,
  (newValue, oldValue) => {
    if (newValue !== oldValue) {
      setUpFormData(props.formData);
    }
  },
);

//#endregion
</script>

<style scoped>
.label { float: left; }

.btn-hover:hover {
  cursor: pointer;
}
</style>