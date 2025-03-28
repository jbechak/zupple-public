<template>
	<div>
    <ValidationContainer :validationErrors="validationErrors"/>
		<div class="form-group mb-2">
			<label for="usernameInput" class="me-1 label">Username *</label>
			<input 
				id="usernameInput"
				name="usernameInput"
				type="text"
				v-model="formData.username"
				maxLength="50"
				:class="STYLES.FORM_INPUT"
				required
			/>
		</div>
		<PasswordInput
			v-model:password="formData.password"
			isRequired
		/>
		<PasswordInput
			labelText="Confirm Password"
			v-model:password="formData.confirmPassword"
			isRequired
		/>
		<div class="form-group mb-2">
			<label for="emailInput" class="me-1 label">Email *</label>
			<input 
				id="emailInput"
				name="emailInput"
				type="text"
				v-model="formData.email"
				maxLength="100"
				:class="STYLES.FORM_INPUT"
				required
			/>
		</div>
		<div :class="[STYLES.BUTTON_CONTAINER, STYLES.W_100]">
			<div class="button-row">
				<button
					type="button" 
					class="btn btn-primary mb-2"
					@click="createAccount"
				>
					Create Account
				</button>
				<button
					type="button" 
					:class="[STYLES.BUTTON_SECONDARY, STYLES.MB_2, STYLES.MS_1]"
					@click="emit('backToLogin')"
				>
					Back to Login
				</button>
			</div>
		</div>
	</div>
</template>

<script setup>
import { reactive, ref, computed } from "vue";
import authService from "@/services/AuthService";
import { CONSTANTS, STYLES } from "@/constants";
import { displayToast } from "@/helpers";
import * as yup from 'yup';
import PasswordInput from "@/components/Authorization/PasswordInput.vue";
import ValidationContainer from "@/components//ValidationContainer.vue";

const emit = defineEmits(['backToLogin']);

const validationErrors = ref(null);
const defaultFormData = {
	username: null,
	password: null,
	confirmPassword: null,
	email: null,
	firstName: null,
	lastName: null,
}
const formData = reactive({});
Object.assign(formData, defaultFormData);

async function createAccount() {
	if (!validateForm())
		return;
	try {
		await authService.register(formData);
    displayToast(`User ${formData.username} created! Please log in.`, CONSTANTS.SUCCESS);
		emit('backToLogin');
	} catch(error) {
    displayToast("Error creating user!", CONSTANTS.ERROR);
	}
}

const validationSchema = yup.object().shape({
  username: yup.string().nullable().required().max(50).label('Username'),
  password: yup.string().nullable().required().max(50).label('Password'),
  confirmPassword: yup.string().nullable().required().max(50).label('Confirm Password')        
		.test(
			'passwordMatch',
			'Confirm Password and Password must be the same',
			(value) => value === formData.password
		),
  email: yup.string().email().nullable().required().max(50).label('Email'),
});

function validateForm() {
  validationErrors.value = [];
  try {
    validationSchema.validateSync(formData, { abortEarly: false });
    return true;
  } catch (error) {
    error.inner.forEach(e => {
        validationErrors.value.push(e.message);
    });
    return false;
  }
}
</script>

<style scoped>
.label { float: left; }

.input-group-text {
	border-top-left-radius: 0;
	border-bottom-left-radius: 0;
}

.text-primary , .input-group-append , .btn-hover:hover {
	cursor: pointer;
}
</style>
