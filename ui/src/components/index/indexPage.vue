<template>
    <div>
        <div class="bg">
            <div class="centerAlignDiv">
                <div class="animation">
                    <img class="logoTranslate" alt="Odyssey Logo" src="../../../static/logo.png">
                    <h1 class="logoTextAppear">ODYSSEY</h1>
                </div>

                <div class="subtitleSection">
                    <h4 id="subtitle">Earn points and badges by creating and solving quests as you travel around the world!</h4>
                </div>
                <b-row class="loginSignUpButtons">
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
                            <login @profile-received="$emit('profile-received')"></login>
                        </b-modal>
                    </b-col>
                </b-row>
                <a class="ct-btn-scroll ct-js-btn-scroll" href="#section2"><b-img alt="Arrow Down Icon" :src="assets['downArrow']"></b-img></a>
            </div>
        </div>
        <div class="main">
            <section id="section2">
                <div class="container jumbo">
                    <h2 class="page-title">About</h2>
                    <b-row>
                        <b-col md>
                            <h3 class="page-title">General</h3>
                            <p></p>
                        </b-col>
                        <b-col md>
                            <h3 class="page-title">Quests</h3>
                            <p></p>
                        </b-col>
                        <b-col md>
                            <h3 class="page-title">The Team</h3>
                            <p></p>
                        </b-col>
                    </b-row>
                </div>
            </section>
        </div>
    </div>
</template>

<script>
    import Signup from "./signup.vue"
    import Login from "./login.vue"

    export default {
        name: "Index.vue",

        props: ['nationalityOptions', 'travTypeOptions', 'profile'],

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
                    this.updateActivity();
                    if (this.profile.admin) {
                        this.$router.replace("/admin");
                    } else {
                        this.$router.replace("/profile");
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