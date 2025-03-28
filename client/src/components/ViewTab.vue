<template>
  <div>
    <component 
      :is="selectedComponent"
      :puzzleData="formData"  
    />
    <div :class="STYLES.BUTTON_CONTAINER">
      <div v-if="userId" class="button-row">
        <button
          type="button" 
          class="btn btn-primary mb-2"
          :disabled="!formData.isSaveEnabled"
          @click="savePuzzle"
        >
          Save
        </button>
        <button
          v-if="!formData.id"
          type="button" 
          class="btn btn-primary mb-2 mx-1"
          disabled
        >
          Save first to print
        </button>
        <button
          v-else
          type="button" 
          class="btn btn-primary mb-2 mx-1"
          :disabled="!formData.id"
          @click="printPuzzle"
        >
        <font-awesome-icon icon="external-link" />
          Print
        </button>
      </div>
      
      <button
        v-else
        type="button" 
        class="btn btn-primary mb-2"
        @click="emit('login')"
      >
        Log in to save and print
      </button>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from "vue";
import { CONSTANTS, STYLES } from "@/constants";
import { displayToast, getComponent, toTitleCase } from "@/helpers";
import { useRouter } from "vue-router";
import DynamicService from '@/services/DynamicService';
import { useUserStore } from "@/stores/userStore";

const userStore = useUserStore();
const router = useRouter();

const props = defineProps({
  puzzleType: { type: String, default: null },
  formData: Object,
});

const emit = defineEmits(['puzzleUpdated', 'login']);
const userId = computed(() => userStore.getUser()?.id);

const selectedComponent = computed(() =>
  getComponent(props.puzzleType, CONSTANTS.PUZZLE)
);

async function savePuzzle() {  
  const userId = userStore.getUser().id;
  if (!userId) {
    return;
  }
  props.formData.userId = userId;
  try {
    if (props.formData.id) {
      await DynamicService.update(props.puzzleType, props.formData);
    } else {
      const result = await DynamicService.create(props.puzzleType, props.formData);
      props.formData.id = result.id;
    }
    displayToast(`${toTitleCase(props.puzzleType)} Saved!`, CONSTANTS.SUCCESS);
    emit('puzzleUpdated');
  } catch(error) {
    displayToast("Save Error!", CONSTANTS.ERROR);
  }
}

function printPuzzle() {
  const routeData = router.resolve({ 
    name: 'puzzlePrint', 
    params: { 
      puzzleType: props.puzzleType,
      puzzleId: props.formData.id,
     }
  });
  window.open(routeData.href, '_blank');
}
</script>