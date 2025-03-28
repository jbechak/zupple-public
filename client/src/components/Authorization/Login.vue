<template>
	<div class="mt-3 d-flex flex-column align-items-start">
		<Register v-if="isRegistering" @backToLogin="isRegistering = false"/>
    <div v-else>
			<div class="form-group mb-2">
				<label for="usernameInput" class="me-1 label">Username</label>
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
			/>
			<div class="text-primary mb-1" @click="isRegistering = true">Need an account?</div>
			<div class="d-flex flex-column align-items-start w-100 mb-1">
				<div class="button-row">
					<button
						type="button" 
						class="btn btn-primary mb-2"
						@click="login"
					>
						Log In
					</button>
					<button
						type="button" 
						class="btn btn-secondary mb-2 mx-1"
						@click="emit('close')"
					>
						Close
					</button>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup>
import { reactive, ref, computed } from "vue";
import { CONSTANTS, STYLES } from "@/constants";
import { useUserStore } from "@/stores/userStore";
import authService from "@/services/AuthService";
import { displayToast } from "@/helpers";
import PasswordInput from "@/components/Authorization/PasswordInput.vue";
import Register from "@/components/Authorization/Register.vue";

const userStore = useUserStore();
const emit = defineEmits(['loggedIn', 'clearAllData', 'close']);
const isRegistering = ref(false);

const defaultFormData = {
	username: null,
	password: null,
	role: null,
}
const formData = reactive({});
Object.assign(formData, defaultFormData);

async function login() {
	try {
		const result = (await authService.login(formData)).data;
		const user = result.user;
		userStore.setToken(result.token);
		userStore.setUser(user);
    displayToast(`Logged in as ${user.username}`, CONSTANTS.SUCCESS);
		emit('close');
	} catch(error) {
		emit('clearAllData');
    displayToast("Invalid Login!", CONSTANTS.ERROR);
	}
}
</script>

<style scoped>
.label { float: left; }

.input-group-text {
	border-top-left-radius: 0;
	border-bottom-left-radius: 0;
}

.text-primary, .btn-hover:hover {
	cursor: pointer;
}
</style>