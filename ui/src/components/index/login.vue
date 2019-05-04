<template>
    <div>
        <b-form>
            <b-form-group
                    id="username-field"
                    description="Please enter your username (email)"
                    label="Username"
                    label-for="username"
                    :invalid-feedack="userinvalidFeedback"
                    :valid-feedback="uservalidFeedback"
                    :state="userstate" >
                <b-form-input id="username" v-model="username" :state="userstate" autofocus trim></b-form-input>
            </b-form-group>

            <b-form-group
                    id="password-field"
                    description="Please enter your password"
                    label="Password"
                    label-for="password">
                <b-form-input id="password" v-model="password" :type="'password'" trim></b-form-input>
            </b-form-group>
            <b-button id="sign-in" variant="primary" block @click="login">Sign In</b-button>
        </b-form>

    </div>

</template>

<script>
    export default {
        name: "login",
        data: function() {
            return {
                username: '',
                password: ''
            }
        },
        computed: {
            /**
             * Checks email validity
             * @returns {boolean|*} true if email is valid
             */
            userstate() {
                return !!(this.username.length >= 4) && this.username.includes('@')
            },
            /**
             * @returns {string} specific error messages depending on error
             */
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
        methods: {
            /**
             * Attempts to log user in and redirects to dash page if login is valid
             */
            login() {
                fetch('/v1/login', {
                    method: 'POST',
                    headers:{'content-type': 'application/json'},
                    body: JSON.stringify({username: this.username, password: this.password})
                }).then(function(response) {
                    window.location.pathname ="/dash";
                    return response.json();
                })
            }
        }
    }
</script>

<style scoped>

</style>