<template>
    <div class="container">
        <b-form-group
                id="first_name-field"
                label="First Name(s):"
                label-for="first_name">
            <b-form-input id="first_name" v-model="profile.firstName" :type="'text'" trim></b-form-input>
        </b-form-group>
        <b-form-group
                id="middle_name-field"
                label="Middle Name(s):"
                label-for="middle_name">
            <b-form-input id="middle_name" v-model="profile.middleName" :type="'text'" trim></b-form-input>
        </b-form-group>
        <b-form-group
                id="last_name-field"
                label="Last Name(s):"
                label-for="last_name">
            <b-form-input id="last_name" v-model="profile.lastName" :type="'text'" trim></b-form-input>
        </b-form-group>
        <b-form-group
                id="email-field"
                label="Email:"
                label-for="email">
            <b-form-input id="email" v-model="profile.username" :type="'email'" trim></b-form-input>
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
            <b-form-input id="dateOfBirth" v-model="profile.dateOfBirth" :type="'date'" trim></b-form-input>
        </b-form-group>
        <b-row>
            <b-col>
                <b-form-group
                        id="nationalities-field"
                        label="Nationality:"
                        label-for="nationality">
                    <b-form-select id="nationality" v-model="profile.nationalities" multiple trim>
                        <option v-for="nationality in nationalityOptions">{{nationality.nationality}}</option>
                    </b-form-select>
                </b-form-group>
            </b-col>
            <b-col>
                <b-form-group
                        id="passports-field"
                        label="Passport:"
                        label-for="passports">
                    <b-form-select id="passports" v-model="profile.passports" trim multiple>
                        <option v-for="nationality in nationalityOptions">{{nationality.country}}</option>
                    </b-form-select>
                </b-form-group>
            </b-col>
        </b-row>
        <b-form-group
                id="travType-field"
                label="Traveller Type(s):"
                label-for="travType">
            <b-form-select id="travType" v-model="profile.travellerTypes" multiple trim>
                <option v-for="travType in travTypeOptions">{{travType.travellerType}}</option>
            </b-form-select>
        </b-form-group>
        <b-button variant="success" size="lg" block @click="saveProfile">Save Profile</b-button>
    </div>
</template>

<script>
    export default {
        name: "editProfile",
        props: ['profile', 'nationalityOptions', 'travTypeOptions'],
        data: function() {
            return {
                password: "",
                retypeP: ""

            }
        },
        mounted () {
        },
        methods: {
            saveProfile() {

                fetch('/v1/profile/edit', {
                    method: 'POST',
                    headers:{'content-type': 'application/json'},
                    body: JSON.stringify(this.profile)
                }).then(function(response) {
                    return response.json();
                })
            },
            checkStatus (response) {
                if (response.status >= 200 && response.status < 300) {
                    return response;
                }
                const error = new Error(`HTTP Error ${response.statusText}`);
                error.status = response.statusText;
                error.response = response;
                console.log(error); // eslint-disable-line no-console
                throw error;
            },
            parseJSON (response) {
                return response.json();
            }
        },
    }
</script>

<style scoped>

</style>