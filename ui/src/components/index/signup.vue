<template>
    <div>
        <div v-if="showFirst" id="firstSignup">
            <b-form-group
                    id="fname-field"
                    label="First Name(s):"
                    label-for="first_name">
                <b-form-input id="first_name" v-model="first_name" type="'text'" trim required></b-form-input>
            </b-form-group>

            <b-form-group
                    id="mname-field"
                    label="Middle Name(s):"
                    label-for="middle_name">
                <b-form-input id="middle_name" v-model="middle_name" type="'text'" trim placeholder="Optional"></b-form-input>
            </b-form-group>

            <b-form-group
                    id="lname-field"
                    label="Last Name(s):"
                    label-for="last_name">
                <b-form-input id="last_name" v-model="last_name" type="'text'" trim required></b-form-input>
            </b-form-group>

            <b-form-group
                    id="email-field"
                    description="Note: this will be your username"
                    label="Email:"
                    label-for="email">
                <b-form-input id="email" v-model="email" type="email" trim required></b-form-input>
            </b-form-group>

            <b-form-group
                    id="password-field"
                    label="Password:"
                    label-for="password">
                <b-form-input id="password" v-model="password" type="password" trim required></b-form-input>
            </b-form-group>

            <b-form-group
                    id="rePassword-field"
                    description="Please re-enter your password"
                    label="Retype Password:"
                    label-for="rePassword">
                <b-form-input id="rePassword" v-model="rePassword" type="password" trim required></b-form-input>
            </b-form-group>

            <b-form-group
                    id="dateOfBirth-field"
                    label="Date of Birth:"
                    label-for="dateOfBirth">
                <b-form-input id="dateOfBirth" v-model="dateOfBirth" type="date" trim required></b-form-input>
            </b-form-group>

            <b-form-group
                    id="gender-field"
                    label="Gender:"
                    label-for="gender">
                <b-form-select id="gender" v-model="gender" :options="genderOptions" trim required></b-form-select>
            </b-form-group>
            <b-button variant="primary" block @click="nextPage">Next</b-button>
        </div>
        <div v-if="showSecond" id="secondSignup">
            <b-row>
                <b-col>
                    <b-form-group
                            id="nationalities-field"
                            label="Nationality:"
                            label-for="nationality">
                        <b-form-select id="nationality" v-model="nationalities" multiple trim>
                            <option v-for="nationality in nationalityOptions">{{nationality.nationality}}</option>
                        </b-form-select>
                    </b-form-group>
                </b-col>
                <b-col>
                    <b-form-group
                            id="passports-field"
                            label="Passport:"
                            label-for="passports">
                        <b-form-select id="passports" v-model="passports" trim multiple>
                            <option v-for="nationality in nationalityOptions">{{nationality.country}}</option>
                        </b-form-select>
                    </b-form-group>
                </b-col>
            </b-row>
            <b-button @click="previousPage">Back</b-button>
            <b-button @click="saveProfile" variant="primary" class="float-right">Sign Up</b-button>
        </div>


</div>
</template>

<script>
    export default {
        name: "Signup",
        data: function() {
            return {
                first_name: '',
                middle_name: '',
                last_name: '',
                email: '',
                password: '',
                rePassword: '',
                dateOfBirth: '',
                gender: '',
                genderOptions: [
                    {value: 'male', text: 'Male'},
                    {value: 'female', text: 'Female'},
                    {value: 'other', text: 'Other'}
                ],
                showFirst: true,
                showSecond: false,
                nationalities: [],
                passports: [],
                travTypes: [],
                nationalityOptions: [],
                travTypeOptions: []
            }
        },
        mounted () {
            this.getNationalities(nationalities => this.nationalityOptions = nationalities);
            this.getTravTypes(travTypes => this.travTypeOptions = travTypes);
        },
        computed: {

        },
        methods: {
            nextPage() {
                this.showFirst = false;
                this.showSecond = true;
            },
            previousPage() {
                this.showFirst = true;
                this.showSecond = false;
            },
            getNationalities (cb) {
                return fetch(`/v1/nationalities`, {
                    accept: "application/json"
                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then(cb);
            },
            getTravTypes (cb) {
                return fetch(`/v1/travtypes`, {
                    accept: "application/json"
                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then(cb);
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
            },
            saveProfile() {
                let profile = {
                    first_name: this.first_name,
                    middle_name: this.middle_name,
                    last_name: this.last_name,
                    email: this.email,
                    password: this.password,
                    date_of_birth: this.dateOfBirth,
                    gender: this.gender,
                    nationalities: this.nationalities,
                    passports: this.passports,
                    travTypes: this.travTypes
                };
                console.log(profile);
            }
        }
    }
</script>

<style scoped>

</style>