<template>
    <div>
        <b-form @submit.prevent="login">
            <b-alert dismissible v-model="showError" variant="danger">{{alertMessage}}</b-alert>
            <b-form-group
                    description="Please enter your username (email)"
                    id="username-field"
                    label="Username"
                    label-for="username">
                <b-form-input autofocus id="username" trim v-model="username"></b-form-input>
            </b-form-group>
            <b-form-group
                    description="Please enter your password"
                    id="password-field"
                    label="Password"
                    label-for="password">
                <b-form-input :type="'password'" id="password" trim v-model="password"></b-form-input>
            </b-form-group>
            <b-button block id="sign-in" type="submit" variant="primary">Sign In</b-button>
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
                showError: false,
                alertMessage: ""
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
                    if (response.status === 401) {
                        self.alertMessage = "Invalid username or password";
                        self.showError = true;
                    } else if (!response.ok) {
                        throw response;
                    } else {
                        self.setLastSeen();
                        self.showError = false;
                        self.$router.go();
                        return response.json();
                    }
                }).catch(function () {
                    self.showErrorToast(JSON.parse(JSON.stringify([{message: "An unexpected error occurred"}])));
                });
            }
        }
    }
</script>