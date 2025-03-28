<template>
  <div class="">
    <nav>
      <div class="nav nav-tabs" id="nav-tab" role="tablist">
        <nav-tab
          :tabName="CONSTANTS.LIST"
          :selected-tab="selectedTab"
          :tabLabel="'Puzzle List'"
          @tab-selected="selectedTab = CONSTANTS.LIST"
        />
        <nav-tab
          :tabName="CONSTANTS.EDIT"
          :selected-tab="selectedTab"
          :tabLabel="'Edit'"
          :disabled="!showEdit"
          @tab-selected="selectedTab = CONSTANTS.EDIT"
        />
        <nav-tab
          :tabName="CONSTANTS.VIEW"
          :selected-tab="selectedTab"
          :tabLabel="'View/Save'"
          :disabled="!showView"
          @tab-selected="selectedTab = CONSTANTS.VIEW"
        />
        <!-- <nav-tab
          :tabName="CONSTANTS.PLAY"
          :selected-tab="selectedTab"
          :tabLabel="'Play'"
          disabled
          @tab-selected="selectedTab = CONSTANTS.PLAY"
        /> -->
      </div>
    </nav>
    <div class="tab-content" id="nav-tabContent">
      <div
        :class="selectedTab === CONSTANTS.LIST ? STYLES.TAB_PANE_ACTIVE : STYLES.TAB_PANE"
        id="nav-list"
        role="tabpanel"
        aria-labelledby="nav-list-tab"
      >
        <ListTab
          ref="listComponent"
          :puzzleType="puzzleType"
          :userId="userId"
          @goToPuzzleEdit="(record) => goToPuzzle(record, CONSTANTS.EDIT)"
          @goToPuzzleView="(record) => goToPuzzle(record, CONSTANTS.VIEW)"
          @login="emit('login')"
        />
      </div>
      <div
        :class="selectedTab === CONSTANTS.EDIT ? STYLES.TAB_PANE_ACTIVE : STYLES.TAB_PANE"
        id="nav-edit"
        role="tabpanel"
        aria-labelledby="nav-edit-tab"
      >
        <component 
          :is="editComponent" 
          ref="editComponentRef"
          v-model:formData="formData[puzzleType]"
          @goToPuzzleView="(record) => goToPuzzle(record, CONSTANTS.VIEW)" 
          @puzzleUpdated="updateListing" 
        />
      </div>
      <div
        :class="selectedTab === CONSTANTS.VIEW ? STYLES.TAB_PANE_ACTIVE : STYLES.TAB_PANE"
        id="nav-view"
        role="tabpanel"
        aria-labelledby="nav-view-tab"
      >
        <ViewTab
          v-if="selectedTab === CONSTANTS.VIEW && formData[puzzleType].gridString"
          :puzzleType="puzzleType"
          :userId="userId"
          v-model:formData="formData[puzzleType]"
          ref="viewComponent"
          @puzzleUpdated="updateListing"
          @login="emit('login')"
        />
      </div>
    </div>
  </div>
</template>

<script setup>
import ListTab from '@/components/ListTab.vue'
import ViewTab from '@/components/ViewTab.vue'
import NavTab from '@/components/NavTab.vue'
import { defaultFormData, getComponent } from "@/helpers";
import { ref, reactive, computed, watch } from 'vue'
import { CONSTANTS, STYLES } from '@/constants'

const props = defineProps({
  puzzleType: { type: String, default: null },
  userId: { type: Number, default: null },
});
const emit = defineEmits(['login']);
defineExpose({ resetAllFormData });

const selectedTab = ref(CONSTANTS.LIST);
const listComponent = ref(null);
const viewComponent = ref(null);
const editComponentRef = ref(null);

const formData = reactive({
  wordsearch: {},
  crossword: {},
  sudoku: {},
});
setDefaultFormData();

const isEditted = reactive({});

const showEdit = computed(() => isEditted[props.puzzleType]);
const showView = computed(() => !!formData[props.puzzleType].gridString);

function setDefaultFormData() {
  Object.assign(formData.wordsearch, defaultFormData.wordsearch);
  Object.assign(formData.crossword, defaultFormData.crossword);
  Object.assign(formData.sudoku, defaultFormData.sudoku);
}

const editComponent = computed(() =>
  getComponent(props.puzzleType, CONSTANTS.EDIT)
);

function goToPuzzle(record, tab) {
  editComponentRef.value.setUpFormData(record);
  isEditted[props.puzzleType] = true;
  selectedTab.value = tab;
}

function updateListing() {
  if (listComponent.value)
    listComponent.value.getListingData();
}

function resetAllFormData() {
  Object.assign(formData, {});
  setDefaultFormData();
}

watch(
  () => props.puzzleType,
  (value) => {
    selectedTab.value = CONSTANTS.LIST;
  }
)
</script>