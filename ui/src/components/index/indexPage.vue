<template>
    <div>
        <div class="bg">
            <div class="centerAlignDiv">
                <div class="animation">
                    <img class="logoTranslate" alt="TravelEA Logo" src="../../../static/logo.png">
                    <h1 class="logoTextAppear">TravelEA</h1>
                </div>

                <div class="subtitleSection">
                    <h3 id="subtitle">Your personal Travel Executive Assistant!</h3>
                    <b-row>
                        <b-col>
                            <b-button class="btn btn-info btn-lg float-right" v-b-modal.modalSignup>Sign Up</b-button>
                            <b-modal centered hide-footer id="modalSignup">
                                <template slot="modal-title"><h2>Sign Up</h2></template>
                                <signup :createdByAdmin="false"
                                        v-bind:nationalityOptions="nationalityOptions"
                                        v-bind:travTypeOptions="travTypeOptions">
                                </signup>
                            </b-modal>
                        </b-col>

                        <b-col>
                            <b-button class="btn btn-info btn-lg float-left" v-b-modal.modalLogin>Login</b-button>
                            <b-modal centered hide-footer id="modalLogin" title="Login">
                                <template slot="modal-title"><h2>Login</h2></template>
                                <login></login>
                            </b-modal>
                        </b-col>
                    </b-row>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
    import Signup from "./signup.vue"
    import Login from "./login.vue"

    export default {
        name: "Index.vue",

        props: ['assets', 'nationalityOptions', 'travTypeOptions', 'profile'],

        data: function () {
            return {
                username: '',
                password: ''
            }
        },

        watch: {
            /**
             * Redirects the user to their home page when they log in.
             */
            profile: function () {
                if (this.profile.id !== undefined) {
                    if (this.profile.admin) {
                        this.$router.replace("/admin");
                    } else {
                        this.$router.replace("/dash");
                    }
                }
            }
        },

        components: {
            Signup,
            Login
        }
    }
</script>

<style>
    @import "../../css/index.css";
</style>