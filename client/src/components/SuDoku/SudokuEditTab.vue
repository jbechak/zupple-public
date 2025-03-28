<template>
  <div class="mt-3 d-flex flex-column align-items-start">
    <ValidationContainer :validationErrors="validationErrors"/>
    <div :class="STYLES.FORM_GROUP">
      <label for="sdTitle" class="me-1 label">Puzzle Title *</label>
      <input 
        id="sdTitle"
        name="sdTitle"
        type="text"
        v-model="formData.title"
        maxLength="50"
        :class="STYLES.FORM_INPUT"
      />
    </div>
    <div class="form-check">
      <input 
        class="form-check-input mb-3" 
        type="checkbox" 
        v-model="formData.showTitle" 
        id="flexCheckDefault">
      <label class="form-check-label" for="flexCheckDefault">
        Show Title
      </label>
    </div>
    <div :class="STYLES.FORM_GROUP">
      <label for="sdDifficulty" class="me-1 label">Difficulty (1 - 10) *</label>
      <input 
        id="sdDifficulty"
        name="sdDifficulty"
        type="number"
        v-model="formData.difficulty"
        min="1"
        max="10"
        :class="STYLES.FORM_INPUT"
        @input="(e) => nullIfEmpty(e, formData, 'difficulty')"
      />
    </div>
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
      :mustGenerateToSave="isdifficultyChanged"
      @generate="generatePuzzle"
      @generateAsCopy="generatePuzzle(true)"
      @reset="resetFormData(true)"
    />
  </div>
</template>

<script setup>
import { ref, watch, computed  } from "vue";
import { CONSTANTS, STYLES } from "@/constants";
import { displayToast, defaultFormData, nullIfEmpty } from "@/helpers";
import * as yup from 'yup';
import DynamicService from "@/services/DynamicService";
import GenerateButtonBar from "@/components/GenerateButtonBar.vue";
import ValidationContainer from "@/components//ValidationContainer.vue";

const props = defineProps({ formData: Object });
const emit = defineEmits(['goToPuzzleView']);
defineExpose({ setUpFormData });

const storedDifficulty = ref(null);
const isdifficultyChanged = computed(() => props.formData.difficulty != storedDifficulty.value);
const validationErrors = ref(null);

//#region SETUP FUNCTIONS
function setUpFormData(record) {
  if (record) {
    Object.assign(props.formData, record); 
    storedDifficulty.value = props.formData.difficulty;
  } else {
    resetFormData();
  }
}

function resetFormData(keepId = false) {
  Object.assign(
    props.formData, 
    keepId 
      ? { ...defaultFormData.sudoku, id: props.formData.id } 
      : defaultFormData.sudoku
  );
}

async function generatePuzzle(isCopy = false) {
  if (!validateForm())
    return;
  try {
    if (isCopy)
      props.formData.id = null;
    const result = await DynamicService.generate(CONSTANTS.SUDOKU, props.formData);
    emit('goToPuzzleView', {...result, id: props.formData.id });
    displayToast("Sudoku Generated!", CONSTANTS.SUCCESS);
  } catch(error) {
    displayToast("Error generating puzzle!", CONSTANTS.ERROR);
  }
}

const validationSchema = yup.object().shape({
  title: yup.string().nullable().required().max(50).label('Puzzle Title'),
  difficulty: yup.number().nullable().positive().required().max(10).label('Difficulty'),
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

watch (
  () => props.formData.id,
  (newValue, oldValue) => {
    if (newValue !== oldValue) {
      setUpFormData(props.formData);
    }
  },
);
</script>

<style scoped>
.label { float: left; }

.btn-hover:hover {
  cursor: pointer;
}
</style>