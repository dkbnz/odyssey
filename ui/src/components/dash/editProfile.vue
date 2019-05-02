<template>
    <div class="container">
        <b-form-group
                id="first_name-field"
                label="First Name(s):"
                label-for="first_name">
            <b-form-input id="first_name" v-model="saveProfile.first_name" :state="fNameValidation" type="text"
                          trim></b-form-input>
            <b-form-invalid-feedback :state="fNameValidation"> Your first name must be between 1-100 characters and
                contain no numbers.
            </b-form-invalid-feedback>
            <b-form-valid-feedback :state="fNameValidation"> Looks Good</b-form-valid-feedback>
        </b-form-group>
        <b-form-group
                id="middle_name-field"
                label="Middle Name(s):"
                label-for="middle_name">
            <b-form-input id="middle_name" v-model="saveProfile.middle_mame" :state="mNameValidation" type="text"
                          trim></b-form-input>
            <b-form-invalid-feedback :state="mNameValidation"> Your middle name must be less than 100 characters and
                contain no numbers.
            </b-form-invalid-feedback>
            <b-form-valid-feedback :state="mNameValidation"> Looks Good</b-form-valid-feedback>
        </b-form-group>
        <b-form-group
                id="last_name-field"
                label="Last Name(s):"
                label-for="last_name">
            <b-form-input id="last_name" v-model="saveProfile.last_name" :state="lNameValidation" type="text"
                          trim></b-form-input>
            <b-form-invalid-feedback :state="lNameValidation"> Your last name must be between 1-100 characters and
                contain no numbers.
            </b-form-invalid-feedback>
            <b-form-valid-feedback :state="lNameValidation"> Looks Good</b-form-valid-feedback>
        </b-form-group>
        <b-form-group
                id="email-field"
                label="Email:"
                label-for="email">
            <b-form-input id="email" v-model="saveProfile.username" :state="emailValidation" type="email"
                          trim></b-form-input>
            <b-form-invalid-feedback :state="emailValidation"> Your email must be valid and unique!
            </b-form-invalid-feedback>
            <b-form-valid-feedback :state="emailValidation"> Looks Good</b-form-valid-feedback>
        </b-form-group>
        <b-form-group
                id="password-field"
                label="Password:"
                label-for="password">
            <b-form-input id="password" v-model="saveProfile.password" :state="passwordValidation" type="password"
                          placeholder="Unchanged" trim></b-form-input>
            <b-form-invalid-feedback :state="passwordValidation"> Your password must be between 5 and 15 characters.
            </b-form-invalid-feedback>
            <b-form-valid-feedback :state="passwordValidation"> Looks Good</b-form-valid-feedback>
        </b-form-group>
        <b-form-group
                id="passwordRE-field"
                label="Retype Password:"
                label-for="passwordre">
            <b-form-input id="passwordre" v-model="rePassword" :state="rePasswordValidation" type="password"
                          placeholder="Unchanged" trim></b-form-input>
            <b-form-invalid-feedback :state="rePasswordValidation"> This isn't the same as the password!
            </b-form-invalid-feedback>
            <b-form-valid-feedback :state="rePasswordValidation"> Looks Good</b-form-valid-feedback>
        </b-form-group>
        <b-form-group
                id="DOB-field"
                label="Date of Birth:"
                label-for="dateOfBirth">
            <b-form-input id="dateOfBirth" v-model="saveProfile.date_of_birth" :state="dateOfBirthValidation"
                          :type="'date'" trim></b-form-input>
            <b-form-invalid-feedback :state="dateOfBirthValidation"> You need a date of birth.</b-form-invalid-feedback>
            <b-form-valid-feedback :state="dateOfBirthValidation"> Looks Good</b-form-valid-feedback>
        </b-form-group>
        <b-form-group
                id="gender-field"
                label="Gender:"
                label-for="gender">
            <b-form-select id="gender" v-model="saveProfile.gender" :state="genderValidation" trim required>
                <option v-for="gender in genderOptions">{{gender.value}}</option>
            </b-form-select>
            <b-form-invalid-feedback :state="genderValidation"> Please select a gender.</b-form-invalid-feedback>
            <b-form-valid-feedback :state="genderValidation"> Looks Good</b-form-valid-feedback>
        </b-form-group>
        <b-row>
            <b-col>
                <b-form-group
                        id="nationalities-field"
                        label="Nationality:"
                        label-for="nationality">
                    <b-form-select id="nationality" v-model="saveProfile.nationality" :state="nationalityValidation"
                                   :required="true" multiple trim>
                        <optgroup label="Current Nationalities: (Please select these if you want to use them!)">
                            <option v-for="nationality in profile.nationalities" :selected="true"
                                    :value="nationality.id">{{nationality.nationality}}
                            </option>
                        </optgroup>
                        <optgroup label="Other Nationalities:">
                            <option v-for="nationality in nationalityOptions"
                                    v-if="!duplicateNationality(nationality.id)" :value="nationality.id">
                                {{nationality.nationality}}
                            </option>
                        </optgroup>
                    </b-form-select>
                    <b-form-invalid-feedback :state="nationalityValidation"> Please select at least one nationality.
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="nationalityValidation"> Looks Good</b-form-valid-feedback>
                </b-form-group>
            </b-col>
            <b-col>
                <b-form-group
                        id="passports-field"
                        label="Passport:"
                        label-for="passports">
                    <b-form-select id="passports" v-model="saveProfile.passport_country" :state="passportValidation"
                                   :required="true" trim multiple>
                        <optgroup label="Current Passports: (Please select these if you want to use them!)">
                            <option v-for="passport in profile.passports" :selected="true" :value="passport.id">
                                {{passport.country}}
                            </option>
                        </optgroup>
                        <optgroup label="Other Passports:">
                            <option v-for="nationality in nationalityOptions" v-if="!duplicatePassport(nationality.id)"
                                    :value="nationality.id">{{nationality.country}}
                            </option>
                        </optgroup>
                    </b-form-select>
                    <b-form-invalid-feedback :state="passportValidation"> Please select at least one passport country.
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="passportValidation"> Looks Good</b-form-valid-feedback>
                </b-form-group>
            </b-col>
        </b-row>
        <b-form-group
                id="travType-field"
                label="Traveller Type(s):"
                label-for="travType">
            <b-form-select lg id="travType" v-model="saveProfile.traveller_type" :state="travTypeValidation" multiple
                           trim>
                <optgroup label="Current Traveller Types: (Please select these if you want to use them!)">
                    <option v-for="travType in profile.travellerTypes" :value="travType.id">{{travType.travellerType}}
                    </option>
                </optgroup>
                <optgroup label="Other Traveller Types">
                    <option v-for="travType in travTypeOptions" v-if="!duplicateTravType(travType.id)"
                            :value="travType.id">{{travType.travellerType}}
                    </option>
                </optgroup>
            </b-form-select>
        </b-form-group>
        <b-alert v-model="showError" variant="danger" dismissible>The form contains errors!</b-alert>
        <b-button variant="success" size="lg" block @click="checkSaveProfile">Save Profile</b-button>
    </div>
</template>

<script>
    export default {
        name: "editProfile",
        props: ['profile', 'nationalityOptions', 'travTypeOptions'],
        data: function () {
            return {
                saveProfile: {
                    first_name: this.profile.firstName,
                    middle_name: this.profile.middleName,
                    last_name: this.profile.lastName,
                    username: this.profile.username,
                    password: "",
                    date_of_birth: new Date(this.profile.dateOfBirth + " 14:48").toISOString().substr(0, 10),
                    gender: {},
                    nationality: [],
                    passport_country: [],
                    traveller_type: []
                },
                rePassword: "",
                validEmail: false,
                showError: false,
                genderOptions: [
                    {value: 'Male', text: 'Male'},
                    {value: 'Female', text: 'Female'},
                    {value: 'Other', text: 'Other'}
                ]

            }
        },
        mounted() {
        },
        computed: {
            fNameValidation() {
                if (this.saveProfile.first_name.length === 0) {
                    return null;
                }
                let nameRegex = new RegExp("^(?=.{1,100}$)([a-zA-Z]+((-|'| )[a-zA-Z]+)*)$");
                return nameRegex.test(this.saveProfile.first_name);
            },
            mNameValidation() {
                let nameRegex = new RegExp("^(?=.{0,100}$)([a-zA-Z]+((-|'| )[a-zA-Z]+)*)$");
                return nameRegex.test(this.saveProfile.middle_name) || this.saveProfile.middle_name.length === 0;
            },
            lNameValidation() {
                if (this.saveProfile.last_name.length === 0) {
                    return null;
                }
                let nameRegex = new RegExp("^(?=.{1,100}$)([a-zA-Z]+((-|'| )[a-zA-Z]+)*)$");
                return nameRegex.test(this.saveProfile.last_name);
            },
            emailValidation() {
                if (this.saveProfile.username.length === 0) {
                    return null;
                }
                let emailRegex = new RegExp("^([a-zA-Z0-9]+(@)([a-zA-Z]+((.)[a-zA-Z]+)*))(?=.{3,15})");
                this.checkUsername();
                return (emailRegex.test(this.saveProfile.username) && this.validEmail);
            },
            passwordValidation() {
                if (this.saveProfile.password.length === 0) {
                    return null;
                }
                let passwordRegex = new RegExp("^(((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])(?=.*[0-9]))|((?=.*[A-Z])(?=.*[0-9])))(?=.{5,15})");
                return passwordRegex.test(this.saveProfile.password)
            },
            rePasswordValidation() {
                if (this.rePassword.length === 0) {
                    return null;
                }
                return this.saveProfile.password.length > 0 && this.rePassword === this.saveProfile.password;
            },
            dateOfBirthValidation() {
                if (this.saveProfile.date_of_birth.length === 0) {
                    return null;
                }
                return this.saveProfile.date_of_birth.length > 0;
            },
            genderValidation() {
                if (this.saveProfile.gender.length === 0) {
                    return null;
                }
                return this.saveProfile.gender.length > 0;
            },
            nationalityValidation() {
                if (this.saveProfile.nationality.length === 0) {
                    return null;
                }
                return this.saveProfile.nationality.length > 0;
            },
            passportValidation() {
                if (this.saveProfile.passport_country.length === 0) {
                    return null;
                }
                return this.saveProfile.passport_country.length > 0;
            },
            travTypeValidation() {
                if (this.saveProfile.traveller_type.length === 0) {
                    return null;
                }
                return this.saveProfile.traveller_type.length > 0;
            }
        },
        methods: {
            checkUsername() {
                let self = this;
                fetch(`/v1/checkUsername`, {
                    method: 'POST',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify({'username': this.saveProfile.username})

                }).then(function (response) {
                    self.validEmail = response.ok;
                })

            },
            checkSaveProfile() {
                if (this.fNameValidation && this.mNameValidation && this.lNameValidation && this.emailValidation
                    && this.dateOfBirthValidation && this.genderValidation) {
                    this.submitSaveProfile()
                } else {
                    this.showError = true;
                }
            },
            submitSaveProfile() {
                fetch('/v1/profile', {
                    method: 'PUT',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify(this.saveProfile)
                }).then(function (response) {
                    location.reload();
                    return response.json();
                })
            },
            duplicateNationality(id) {
                for (let i = 0; i < this.profile.nationalities.length; i++) {
                    if (this.profile.nationalities[i].id === id) {
                        return true;
                    }
                }
            },
            duplicatePassport(id) {
                for (let i = 0; i < this.profile.passports.length; i++) {
                    if (this.profile.passports[i].id === id) {
                        return true;
                    }
                }
            },
            duplicateTravType(id) {
                for (let i = 0; i < this.profile.travellerTypes.length; i++) {
                    if (this.profile.travellerTypes[i].id === id) {
                        return true;
                    }
                }
            }
        },
        components: {}
    }
</script>