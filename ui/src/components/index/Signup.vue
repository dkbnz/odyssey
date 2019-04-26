<template>
    <div>
        <b-modal id="modal-1" centered title="Sign Up">
            <b-form-group
                    id="fname-field"
                    description="Please enter your first name"
                    label="First Name"
                    label-for="first_name"
                    :invalid-feedack="finvalidFeedback"
                    :valid-feedback="fvalidFeedback"
                    :state="fstate" >
                <b-form-input id="first_name" v-model="first_name" :state="fstate" trim></b-form-input>
            </b-form-group>
            <p class="my-4">Hello from modal!</p>
        </b-modal>
    </div>
</template>

<script>
    export default {
        name: "Signup",
        data: function() {
            return {
                first_name: ''
            }
        },
        computed: {
            fstate() {
                return this.first_name.length >= 4 ? true : false
            },
            finvalidFeedback() {
                if (this.first_name.length > 4) {
                    return ''
                } else if (this.first_name.length > 0) {
                    return 'Enter at least 4 characters'
                } else {
                    return 'Please enter something'
                }
            },
            fvalidFeedback() {
                return this.state === true ? 'Thank you' : ''
            }
        }
    }
</script>

<style scoped>

</style>