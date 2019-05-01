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
                                    <signup v-bind:nationalityOptions="nationalityOptions" v-bind:travTypeOptions="travTypeOptions"></signup>
                                </b-modal>
                            </b-col>
                            <b-col>
                                <b-button v-b-modal.modalLogin  class="btn btn-info btn-lg float-left">Login</b-button>
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
        name: "Index.vue",
        props: {
            'profile': Object,
            'nationalityOptions': Array,
            'travTypeOptions': Array
        },
        created() {
            document.title = "Welcome to TravelEA";
        },
        data: function() {
            return {
                username: '',
                password: ''
            }
        },
        methods: {

        },
        components: {
            Signup,
            Login
        }

    }
</script>

<style>
    body {
        background-color: #e2e6ea;
        padding-top: 0px;


    }

    .bg {
        /* Background image used */
        background-image: url("../../../static/background_image.jpg");
        /* Center and scale the image nicely */
        -webkit-background-size: cover;
        -moz-background-size: cover;
        background-size: cover;
        -o-background-size: cover;
        height: 100vh;
        overflow: hidden;

    }


    #subtitle {
        font-family: Arial, sans-serif;
        font-style: italic;
        color: white;
        -webkit-text-stroke-width: 0.1px;
        -webkit-text-stroke-color: black;
    }

    /* Adds background box to Traveller Type selection */
    .carousel-caption {
        background: #e5e5e5e5;
        border-radius: 10px;
        color: black;
    }

    /* Styling for Traveller Type checkbox */
    .carousel-caption input[type='checkbox'] {
        width:30px;
        height:30px;
        background:white;
        border-radius:5px;
        border:2px solid #555;
    }

    .carousel-caption input[type='checkbox']:checked {
        background: #abd;
    }


</style>