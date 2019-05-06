<template>
    <div>
        <b-form>
            <b-alert v-model="showError" variant="danger" dismissible>Invalid Username or Password</b-alert>
            <b-form-group
                    id="username-field"
                    description="Please enter your username (email)"
                    label="Username"
                    label-for="username">
                <b-form-input id="username" v-model="username" autofocus trim></b-form-input>
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
        data: function () {
            return {
                username: '',
                password: '',
                showError: false
            }
        },
        methods: {
            /**
             * Used to log a user in based on the username and password that are entered. If the response is not ok()
             * (HTTP 200), then an error is shown.
             */
            login() {
                let self = this;
                fetch('/v1/login', {
                    method: 'POST',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify({username: this.username, password: this.password})
                }).then(function (response) {
                    if (response.ok) {
                        self.showError = false;
                        self.$router.go();
                        return JSON.parse(JSON.stringify(response));
                    } else {
                        self.showError = true;
                        return JSON.parse(JSON.stringify(response));
                    }
                })
            }
        }
    }
</script>