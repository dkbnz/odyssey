<template>
    <div>
        <div class="bg" id="start-page">
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
                        <b-modal centered hide-footer id="modalLogin" ref="modalLogin" title="Login">
                            <template slot="modal-title"><h2>Login</h2></template>
                            <login @profile-received="$emit('profile-received')"></login>
                        </b-modal>
                    </b-col>
                </b-row>
                <a id="down-arrow-button" class="ct-btn-scroll ct-js-btn-scroll animatedArrow" href="#section2">
                    <b-img alt="Arrow Down Icon" :src="assets['downArrow']"></b-img>
                </a>
            </div>
        </div>
        <div class="main" id="about-section">
            <section id="section2" class="main text-center">
                <div class="container jumbo">
                    <b-row>
                        <b-col md>
                            <div>
                                <b-img :src="assets['loadingLogoBig']" alt="Odyssey Logo" width="100%"></b-img>
                                <h1 class="page-title mt-1 blurbFontTitle">About Odyssey</h1>
                                <p class="blurbFont">
                                    <i>Odyssey</i> allows you to create and solve quests as you travel around the world, all
                                    while earning points and badges for using the various features of the application. <br />
                                    You can even solve quests on your mobile devices, allowing you to take <i>Odyssey</i> with
                                    you while you solve quests.
                                </p>
                            </div>
                        </b-col>
                        <b-col md></b-col>
                        <b-col md>
                            <b-img :src="assets['teamIcon']" alt="Team Symbol" width="100%"></b-img>
                            <h1 class="page-title mt-1 blurbFontTitle">The Team</h1>
                            <p class="blurbFont">
                                The developers of <i>Odyssey</i> are a group of students, <i>Everyware</i>, in the University of Canterbury's
                                SENG302 2019 class. <br />
                                We wanted to create an application that our PO, Moffat Matthews, could use for university
                                events such as Open Days. <br />
                                This application was developed over the course of eight months of university work, so we hope you
                                enjoy it!
                            </p>
                        </b-col>
                    </b-row>
                    <b-row>
                        <div class="w-100 text-center mt-5 pt-5">
                            <h4>So, what are you waiting for?</h4>
                            <div>
                                <b-button id="get-started" variant="link">Get Started!</b-button>
                            </div>

                        </div>
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
                password: '',
                stepTime: 5,
                showSignupModal: false
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