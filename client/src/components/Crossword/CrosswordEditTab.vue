<template>
  <div class="mt-3 d-flex flex-column align-items-start">
    <ValidationContainer :validationErrors="validationErrors"/>
    <div :class="STYLES.FORM_GROUP">
      <label for="cwTitle" class="me-1 label">Puzzle Title *</label>
      <input 
        id="cwTitle"
        name="cwTitle"
        type="text"
        v-model="formData.title"
        maxLength="50"
        :class="STYLES.FORM_INPUT"
      />
    </div>
    <div :class="STYLES.FORM_GROUP">
      <label class="me-1 label">Word/Clue List</label>
    </div>    
    <div v-for="key in Object.keys(formData.wordClueListObj)" :key="key" :class="STYLES.W_100">
      <div :class="STYLES.D_FLEX">
        <input
          type="text"
          v-model="formData.wordClueListObj[key].word"
          maxLength="50"
          :class="[STYLES.FORM_INPUT, STYLES.W_50]"
          @input="capitalizeObjText(key)"
        />
        <input
          type="text"
          v-model="formData.wordClueListObj[key].clue"
          maxLength="100"
          :class="[STYLES.FORM_INPUT, STYLES.MS_1, STYLES.W_100]"
          @keyup="clueUpdated(key, formData.wordClueListObj[key].clue)"
        />
        <font-awesome-icon
          :class="endIcon"
          size="2xl" 
          icon="xmark" 
          @click="delete formData.wordClueListObj[key]"
        />
      </div>
    </div>
    <div :class="[STYLES.D_FLEX, STYLES.W_100]">
      <input
        ref="newWordClueInput"
        type="text"
        v-model="newWordClue.word"
        maxLength="50"
        :class="[STYLES.FORM_INPUT, STYLES.W_50, STYLES.TEXT_SECONDARY]"
        @input="capitalizeNewWordClue(newWordClue.word)"
        placeholder="Enter word here"
      />
      <input
        type="text"
        v-model="newWordClue.clue"
        maxLength="200"
        :class="[STYLES.FORM_INPUT, STYLES.MS_1, STYLES.W_100, STYLES.TEXT_SECONDARY]"
        @keydown.tab.prevent
        @keyup.tab="addNewWordClue"
        @keyup.enter="addNewWordClue"
        placeholder="Enter clue here"
      />
      <font-awesome-icon
        class="text-primary"
        :class="endIcon"
        size="2xl" 
        icon="plus" 
        @click="addNewWordClue"
      />
    </div>
    <GenerateButtonBar
      :isNew="!formData.id"
      :mustGenerateToSave="isWordsChanged"
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
import { displayToast, defaultFormData } from "@/helpers";
import * as yup from 'yup';
import CrosswordService from "@/services/CrosswordService";
import GenerateButtonBar from "@/components/GenerateButtonBar.vue";
import ValidationContainer from "@/components//ValidationContainer.vue";

const props = defineProps({ formData: Object });
const emit = defineEmits(['goToPuzzleView', 'puzzleUpdated']);
defineExpose({ setUpFormData });

//#region CONSTANTS
const endIcon = "ps-1 pb-1 align-self-center w-5 btn-hover"
//#endregion

//#region OTHER VARIABLES
const storedWordList = ref([]);
const newWordClueInput = ref(null);
const newWordClue = reactive({});
const validationErrors = ref(null);
setUpNewWordClue();

const isWordsChanged = computed(() =>
  JSON.stringify(Object.keys(props.formData.wordClues)) != JSON.stringify(storedWordList.value)
);
//#endregion

//#region FORM INPUT FUNCTIONS
function setUpNewWordClue() {
  newWordClue.word = null;
  newWordClue.clue = null;
}

function capitalizeObjText(key) {
  props.formData.wordClueListObj[key].word = props.formData.wordClueListObj[key].word.toUpperCase();
}

function capitalizeNewWordClue(text) {
  newWordClue.word = text.toUpperCase();
}

function addNewWordClue() {
  if (!newWordClue.word || !newWordClue.clue)
    return;
  
  const newPropName = getGuid();
  props.formData.wordClueListObj[newPropName] = {};
  Object.assign(props.formData.wordClueListObj[newPropName], newWordClue);
  setUpNewWordClue();
  newWordClueInput.value.focus();
}

function clueUpdated(key, clue) {
  updateClueList(key, clue, 'acrossClueList', 'acrossClueMap');
  updateClueList(key, clue, 'downClueList', 'downClueMap');
}

function updateClueList(key, clue, clueListProp, clueMapProp) {
  if (formData[clueMapProp][key]) {
    const clueListIndex = props.formData[clueListProp].findIndex((x) => x.split('.')[0] === props.formData[clueMapProp][key]);
    props.formData[clueListProp][clueListIndex] = `${props.formData[clueMapProp][key]}. ${clue}`;
  }
}
//#endregion

//#region SETUP FUNCTIONS
function setUpFormData(record) {
  if (record) {
    Object.assign(props.formData, record); 
    setUpWordClueListObj();
    setAcrossDownClues();
    storedWordList.value = [...Object.keys(props.formData.wordClues)];
  } else {
    resetFormData();
  }
}

function setUpWordClueListObj() {
  props.formData.wordClueListObj = {};
    Object.entries(props.formData.wordClues).forEach(([key, value]) => {
      props.formData.wordClueListObj[getGuid()] = {
        clue: value,
        word: key
      }
    });
}

function setAcrossDownClues() {
  props.formData.acrossClueMap = {};
  props.formData.downClueMap = {};
  setClueMap('acrossClueList', 'acrossClueMap');
  setClueMap('downClueList', 'downClueMap');
}

function setClueMap(clueListProp, clueMapProp) {
  props.formData[clueListProp].forEach((numberedClue) => {
    const clueParts = numberedClue.split('. ');
    Object.entries(props.formData.wordClueListObj).forEach(([key, value]) => {
      if (value.clue === clueParts[1]) {
        props.formData[clueMapProp][key] = clueParts[0];
      }
    })
  });
}

function resetFormData(keepId = false) {
  Object.assign(
    props.formData, 
    keepId 
      ? { ...defaultFormData.crossword, id: props.formData.id } 
      : defaultFormData.crossword
  );

  for (const key in props.formData.wordClueListObj) {
    delete props.formData.wordClueListObj[key];
  }
}
//#endregion

//#region STANDARD PUZZLE FUNCTIONS
async function generatePuzzle(isCopy = false) {
  if (!validateForm())
    return;
  try {
    if (isCopy)
      props.formData.id = null;

    const result = await CrosswordService.generate(props.formData);
    emit('goToPuzzleView', {...result, id: props.formData.id });
    displayToast("Crossword Generated!", CONSTANTS.SUCCESS);

  } catch(error) {
    displayToast("Error generating puzzle!", CONSTANTS.ERROR);
  }
}
//#endregion

const validationSchema = yup.object().shape({
  title: yup.string().nullable().required().max(50).label('Puzzle Title'),
  // TO DO word clue validation
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

//#region WATCHERS
watch (
  () => Object.values(props.formData.wordClueListObj),
  (value) => {
    props.formData.wordClues = Object.fromEntries(value.map(item => [item.word, item.clue]));
  },
  { deep: true }
);

watch (
  () => isWordsChanged.value,
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