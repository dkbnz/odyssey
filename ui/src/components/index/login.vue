<template>
    <div>
        <b-form @submit.prevent="login">
            <b-alert dismissible v-model="showError" variant="danger">{{alertMessage}}</b-alert>
            <b-form-group
                    description="Please enter your username (email)"
                    id="username-field"
                    label="Username"
                    label-for="username">
                <b-form-input autofocus
                              @input="showError = false"
                              id="username"
                              trim
                              v-model="username"></b-form-input>
            </b-form-group>
            <b-form-group
                    description="Please enter your password"
                    id="password-field"
                    label="Password"
                    label-for="password">
                <b-form-input :type="'password'"
                              @input="showError = false"
                              id="password"
                              trim
                              v-model="password">
                </b-form-input>
            </b-form-group>
            <b-button block id="sign-in" type="submit" variant="primary">
                Sign In
            </b-button>
        </b-form>
        <div class="loader" v-if="loading">
            <div class="loader-content">
                <b-img alt="Loading" class="loading" :src="assets['loadingLogoBig']"></b-img>
                <h1>Checking login details...</h1>
            </div>
        </div>
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
                alertMessage: '',
                loading: false
            }
        },

        methods: {
            /**
             * Used to log a user in based on the username and password that are entered. If the response is not ok()
             * (HTTP 200), then an error is shown.
             */
            login() {
                let self = this;
                this.loading = true;
                fetch('/v1/login', {
                    method: 'POST',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify({username: this.username.toLowerCase(), password: this.password})
                }).then(function (response) {
                    if (!response.ok) {
                        throw response;
                    } else {
                        self.showError = false;
                        return response.json();
                    }
                }).then(function(responseBody) {
                    self.loading = false;
                    self.updateActivity();
                    if (responseBody.admin) {
                        self.$router.replace("/admin");
                    } else {
                        self.$router.replace("/profile");
                    }
                    self.$emit('profile-received');
                    return responseBody;
                }).catch(function (response) {
                    self.loading = false;
                    // If user credentials are incorrect, response is unauthorized.
                    if (response.status === 401) {
                        self.alertMessage = "Invalid username or password";
                        self.showError = true;
                    } else {
                        self.handleErrorResponse(response);
                    }
                });
            }
        }
    }
</script>