<template>
    <div>
        <div class="bg">

            <div style="padding-top: 10%">
                <div>

                    <div align="center">
                        <img src="../../../static/full_logo_lg.png" width="50%" alt="TravelEA Logo">
                        <h3 id="subtitle" align="center">Your personal Travel Executive Assistant!</h3>
                    </div>

                    <b-row>
                        <b-col>
                            <b-button v-b-modal.modalSignup class="btn btn-info btn-lg float-right">Signup</b-button>
                            <b-modal id="modalSignup" centered hide-footer>
                                <template slot="modal-title"><h2>Sign Up</h2></template>
                                <signup v-bind:nationalityOptions="nationalityOptions"
                                        v-bind:travTypeOptions="travTypeOptions"></signup>
                            </b-modal>
                        </b-col>
                        <b-col>
                            <b-button v-b-modal.modalLogin class="btn btn-info btn-lg float-left">Login</b-button>
                            <b-modal id="modalLogin" hide-footer centered title="Login">
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
        name: "indexPage",
        props: ['profile', 'nationalityOptions', 'travTypeOptions'],
        created() {
            document.title = "Welcome to TravelEA";
        },
        data: function () {
            return {
                username: '',
                password: ''
            }
        },
        methods: {},
        components: {
            Signup,
            Login
        }

    }
</script>

<style>
    @import "../../css/index.css";
</style>