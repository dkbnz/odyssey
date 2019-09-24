<template>
    <div>
        <h4 class="page-title">New Hint</h4>
        <h5>Riddle: {{objective.riddle}}</h5>
        <h5>Destination: {{objective.destination.name}}</h5>
        <b-form @submit.prevent="createHint">
            <!--Input fields for creating a hint-->
            <b-form-group
                    id="name-field"
                    label="Hint:"
                    label-for="name">
                <b-form-textarea id="name"
                              v-model="hint.message"
                              :state="hintValidation"
                              maxlength="200">
                </b-form-textarea>
            </b-form-group>

            <b-row class="buttonMarginsTop">
                <b-col>
                    <b-button @click="createHint" block variant="primary" :disabled="!this.hintValidation">
                        Submit
                    </b-button>
                </b-col>
                <b-col>
                    <b-button @click="cancelHint" block>
                        Cancel
                    </b-button>
                </b-col>
            </b-row>
        </b-form>
    </div>
</template>

<script>

    export default {
        name: "createHint.vue",

        props: {
            objective: Object,
            profile: Object
        },

        data() {
            return {
                hint: {
                    message: ""
                }
            }
        },

        computed: {
            /**
             * Validates the input fields based on regex.
             *
             * @returns {*} true if input is valid.
             */
            hintValidation() {
                if (this.hint.message.length === 0) {
                    return null;
                }
                return this.hint.message.length > 0;
            },
        },

        methods: {

            /**
             * Creates the hint.
             */
            createHint() {
                let self = this;
                if (this.validateFields) {
                    fetch('/v1/objectives/' + this.objective.id + '/hints/' + this.profile.id, {
                        method: 'POST',
                        headers: {'content-type': 'application/json'},
                        body: JSON.stringify(this.hint)
                    }).then(function (response) {
                        if (response.status !== 201) {
                            throw response;
                        } else {
                            return response.json();
                        }
                    }).then(function (responseBody) {
                        self.$emit('successCreate', responseBody);
                    }).catch(function (response) {
                        if (response.status > 404) {
                            self.showErrorToast([{message: "An unexpected error occurred"}]);
                        } else {
                            response.json().then(function(responseBody) {
                                self.showErrorToast(responseBody);
                            });
                        }
                    });
                }
            },


            /**
             * Cancels the creation of a hint and emits to hide the create sidebar component.
             */
            cancelHint() {
                this.$emit('cancelCreate');
            },


            /**
             * Checks the validation fields for hint creation.
             *
             * @returns {boolean} true if the fields are valid.
             */
            validateFields() {
                if (this.hintValidation === true) {
                    return true;
                }
            }
        }
    }
</script>