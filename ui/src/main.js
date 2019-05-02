// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App.vue'
import BootstrapVue from 'bootstrap-vue'
import router from './router'

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

import VueRouter from 'vue-router';
Vue.use(VueRouter);

Vue.config.productionTip = false;
Vue.use(BootstrapVue);


new Vue({
    el: '#app',
    router: router,
    template: '<App/>',
    components: { App }
});
