import './assets/main.css'
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import { VueQueryPlugin } from "vue-query";
import { library } from "@fortawesome/fontawesome-svg-core";
import { faXmark, faPlus, faPencil, faTrash, faEye, faEyeSlash, faPlay, faSortUp, faSortDown, faExternalLink } from "@fortawesome/free-solid-svg-icons";
import axios from 'axios';
const apiUrl = import.meta.env.VITE_API_URL;
library.add(faXmark, faPlus, faPencil, faTrash, faEye, faEyeSlash, faPlay, faSortUp, faSortDown, faExternalLink);
import { FontAwesomeIcon } from "@fortawesome/vue-fontawesome";
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

axios.defaults.baseURL = apiUrl;

const app = createApp(App)
app.use(createPinia())
app.component("font-awesome-icon", FontAwesomeIcon)
app.use(router)
app.use(VueQueryPlugin)
app.mount('#app')
