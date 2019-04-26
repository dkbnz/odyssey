<template>

    <div class="bg">
        <div class="container h-100">
            <div class="row h-100 justify-content-center align-items-center">
                <div align="center">
                    <img src="../../../static/full_logo_lg.png" width="50%" alt="TravelEA Logo">
                    <h3 id="subtitle" align="center">Your personal Travel Executive Assistant!</h3>
                </div>

                <div>
                    <b-button v-b-modal.modalSignup class="btn btn-info btn-lg">Signup</b-button>
                    <b-modal id="modalSignup" centered title="Sign Up">
                        <p class="my-4">Hello from modal!</p>
                    </b-modal>

                    <b-button v-b-modal.modalLogin  class="btn btn-info btn-lg">Login</b-button>
                    <b-modal id="modalLogin" hide-footer centered title="Login">
                        <b-form-group
                                id="username-field"
                                description="Please enter your username (email)"
                                label="Username"
                                label-for="username"
                                :invalid-feedack="userinvalidFeedback"
                                :valid-feedback="uservalidFeedback"
                                :state="userstate" >
                            <b-form-input id="username" v-model="username" :state="userstate" trim></b-form-input>
                        </b-form-group>
                        <b-form-group
                                id="password-field"
                                description="Please enter your password"
                                label="Password"
                                label-for="password">
                            <b-form-input id="password" v-model="password" :type="'password'" trim></b-form-input>
                        </b-form-group>
                        <b-button id="sign-in"type="submit" block @click="submitForm">Sign In</b-button>
                    </b-modal>
                </div>
            </div>
        </div>

    </div>
</template>

<script>
    import Signup from "./signup.vue"

    export default {
        name: "Index.vue",
        created() {
            document.title = "Welcome to TravelEA";
        },
        data: function() {
            return {
                showSignup: false,
                username: '',
                password: '',
                ajaxRequest: false
            }
        },
        methods: {
            toggleSignup: function(){
                this.showSignup = !this.showSignup;
            },
            submitForm: function(e) {
                e.preventDefault();
                this.ajaxRequest = true;
                axios.get("https://jsonplaceholder.typicode.com/todos/")
                    .then(response => console.log(response));
            }
        },
        computed: {
            userstate() {
                return !!(this.username.length >= 4) && this.username.includes('@')
            },
            userinvalidFeedback() {
                if (this.username.length > 4) {
                    return ''
                } else if (this.username.length > 0) {
                    return 'Enter at least 4 characters'
                } else {
                    return 'Please enter something'
                }
            },
            uservalidFeedback() {
                return this.userstate === true ? 'Thank you' : ''
            }
        },
        components: {
            Signup
        }

    }
</script>

<style>
    body {
        background-color: #e2e6ea;
        padding-top: 0px;
        overflow-y: hidden;

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