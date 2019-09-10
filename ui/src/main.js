import Vue from 'vue'
import App from './App.vue'
import BootstrapVue from 'bootstrap-vue'
import router from './router'

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import VueSlider from 'vue-slider-component'
import 'vue-slider-component/theme/default.css'

import RewardToast from "./components/helperComponents/rewardToast";

import VueRouter from 'vue-router';
Vue.use(VueRouter);

Vue.component('VueSlider', VueSlider);

Vue.config.productionTip = false;
Vue.use(BootstrapVue);

Vue.mixin({
    components: {
        RewardToast
    },
    methods: {
        showRewardToast(rewardJson) {
            if((rewardJson.hasOwnProperty('badgesAchieved') && rewardJson.badgesAchieved.length) || (rewardJson.hasOwnProperty('pointsRewarded'))) {
                const h = this.$createElement;

                const toastContent = h(
                    'reward-toast',
                    {props: {rewardJson: rewardJson}}
                );

                this.$bvToast.toast([toastContent], {
                    title: "Yipeeeeeeeeeeeeee",
                    autoHideDelay: 3000,
                    appendToast: true,
                    solid: true,
                    variant: 'success'
                });
            }

        }
    }
});

new Vue({
    el: '#app',
    router: router,
    template: '<App/>',
    components: { App }
});
