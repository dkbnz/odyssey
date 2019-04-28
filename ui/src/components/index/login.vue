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
            <b-button id="sign-in" variant="primary" type="submit" block @click="login">Sign In</b-button>
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
        methods: {
            login() {
                fetch('/v1/login', {
                    method: 'POST',
                    headers:{'content-type': 'application/json'},
                    body: JSON.stringify({username: this.username, password: this.password})
                }).then(function(response) {
                    return response.json();
                })
            }
        }
    }
</script>

<style scoped>

</style>