<template>

  <div class="form-group mb-2">
    <label for="passwordInput" class="me-1 label">{{ inputLabel }}</label>
    <div class="input-group input-group-sm">			
      <input 
        id="passwordInput"
        name="passwordInput"
        :type="passwordInputType"
        :value="password" 
        @input="$emit('update:password', $event.target.value)" 
        maxLength="50"
        class="form-control"
        required
        aria-describedby="basic-addon2"
      />
      <div class="input-group-append" @click="showPassword = !showPassword">
        <span class="input-group-text px-1 h-100">
          <font-awesome-icon
            icon="eye-slash"
          />
        </span>
      </div>
    </div>
  </div>

</template>

<script setup>
import { ref, computed } from "vue";

const props = defineProps({
  labelText: { type: String, default: 'Password' },
  isRequired: { type: Boolean, default: false },
  password: String,
});

const emit = defineEmits(['update:password']);

const showPassword = ref(false);
const passwordInputType = computed(() => showPassword.value ? 'text' : 'password');
const inputLabel = computed(() => props.isRequired ? `${props.labelText} *` : props.labelText);

</script>

<style scoped>
.label { float: left; }

.input-group-text {
	border-top-left-radius: 0;
	border-bottom-left-radius: 0;
}

.input-group-append:hover {
	cursor: pointer;
}
</style>