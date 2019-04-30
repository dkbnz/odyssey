// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App.vue'
import BootstrapVue from 'bootstrap-vue'

import Dash from './components/dash/dashPage'
import Destinations from './components/destinations/destinationsPage'
import Profiles from './components/profiles/profilesPage'
import Trips from './components/trips/tripsPage'

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

import VueRouter from 'vue-router';
Vue.use(VueRouter);

Vue.config.productionTip = false;
Vue.use(BootstrapVue);

const routes = [
    {
        path:"/dash",
        name: "dash",
        component: Dash
    },
    {
        path:"/destinations",
        name: "destinations",
        component: Destinations
    },
    {
        path:"/profiles",
        name: "profiles",
        component: Profiles
    },
    {
        path:"/trips",
        name: "trips",
        component: Trips
    }
];

const router = new VueRouter({
    routes: routes,
    mode: 'history'
});


/* eslint-disable no-new */
new Vue({
    el: '#app',
    router: router,
    template: '<App/>',
    components: { App }
});
