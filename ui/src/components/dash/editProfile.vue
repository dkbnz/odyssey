<template>
    <div class="container">
        <b-form-group
                id="first_name-field"
                label="First Name(s):"
                label-for="first_name">
            <b-form-input id="first_name" v-model="first_name" :type="'text'" trim></b-form-input>
        </b-form-group>
        <b-form-group
                id="middle_name-field"
                label="Middle Name(s):"
                label-for="middle_name">
            <b-form-input id="middle_name" v-model="middle_name" :type="'text'" trim></b-form-input>
        </b-form-group>
        <b-form-group
                id="last_name-field"
                label="Last Name(s):"
                label-for="last_name">
            <b-form-input id="last_name" v-model="last_name" :type="'text'" trim></b-form-input>
        </b-form-group>
        <b-form-group
                id="email-field"
                label="Email:"
                label-for="email">
            <b-form-input id="email" v-model="email" :type="'email'" trim></b-form-input>
        </b-form-group>
        <b-form-group
                id="password-field"
                label="Password:"
                label-for="password">
            <b-form-input id="password" v-model="password" :type="'password'" trim></b-form-input>
        </b-form-group>
        <b-form-group
                id="passwordRE-field"
                label="Retype Password:"
                label-for="passwordre">
            <b-form-input id="passwordre" v-model="retypeP" :type="'password'" trim></b-form-input>
        </b-form-group>
        <b-form-group
                id="DOB-field"
                label="Date of Birth:"
                label-for="dateOfBirth">
            <b-form-input id="dateOfBirth" v-model="dateOfBirth" :type="'date'" trim></b-form-input>
        </b-form-group>
        <b-row>
            <b-form-group
                    id="nationalities-field"
                    label="Nationalities:"
                    label-for="nationality">
                <b-form-input id="nationality" v-model="nationalities" :type="'text'" trim></b-form-input>
            </b-form-group>
            <b-form-group
                    id="passports-field"
                    label="Passport(s):"
                    label-for="passports">
                <b-form-input id="passports" v-model="passports" :type="'text'" trim></b-form-input>
            </b-form-group>
        </b-row>
        <b-form-group
                id="travellerTypes-field"
                label="Traveller Type(s):"
                label-for="passports">
            <b-form-input id="travellerTypes" v-model="travellerTypes" :type="'text'" trim></b-form-input>
        </b-form-group>
    </div>
</template>

<script>
    export default {
        name: "editProfile",
        data: function() {
            return {
                first_name: "Isaac",
                middle_name: "Taylor",
                last_name: "Worsley",
                email: "itw21@uclive.ac.nz",
                password: "",
                retypeP: "",
                dateOfBirth: "25/09/1998",
                gender: "Male",
                nationalities: [],
                passports: [],
                travellerTypes: []
            }
        }
    }
</script>

<style scoped>

</style>