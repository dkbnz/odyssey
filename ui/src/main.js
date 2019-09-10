import Vue from 'vue'
import App from './App.vue'
import BootstrapVue from 'bootstrap-vue'
import router from './router'

import assets from './assets/assets';

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

/**
 * Allows use of these methods in every single component.
 */
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
                    title: "Congratulations!",
                    autoHideDelay: 5000,
                    appendToast: true,
                    solid: true,
                    variant: 'success'
                });
            }

        },


        /**
         * Iterates through the response body and retrieves the error messages for each error.
         * This can be called from anywhere, and returns a string with a newline character separating each message.
         *
         * @param responseBody  the Json response body containing the list of errors.
         * @returns {string}    the error message string with each error message separated by a new line character.
         */
        getErrorMessage(responseBody) {
            let errorString = "";
            for (let errorMessage = 0; errorMessage < responseBody.length; errorMessage++) {
                errorString += responseBody[errorMessage].message + "\n";
            }
            return errorString;
        }
    },

    computed: {
        assets() {
            return assets
        }
    }
});

new Vue({
    el: '#app',
    router: router,
    template: '<App/>',
    components: { App }
});
