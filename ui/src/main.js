import Vue from 'vue'
import BootstrapVue from 'bootstrap-vue'
import App from './App.vue'
import router from './router'
import VueRouter from 'vue-router';
import VueSlider from 'vue-slider-component'

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import 'vue-slider-component/theme/default.css'
Vue.use(VueRouter);

Vue.component('VueSlider', VueSlider);

Vue.config.productionTip = false;
Vue.use(BootstrapVue);

new Vue({
    el: '#app',
    router: router,
    template: '<App/>',
    components: { App }
});
