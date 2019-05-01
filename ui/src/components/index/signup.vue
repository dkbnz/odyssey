<template>
    <div>
        <div v-if="showFirst" id="firstSignup">
            <b-form>
                <b-form-group
                        id="fname-field"
                        label="First Name(s):"
                        label-for="first_name">
                    <b-form-input id="first_name" v-model="first_name" type="text" :state="fNameValidation" trim autofocus required></b-form-input>
                    <b-form-invalid-feedback :state="fNameValidation"> Your first name must be between 1-100 characters and contain no numbers. </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="fNameValidation"> Looks Good </b-form-valid-feedback>
                </b-form-group>

                <b-form-group
                        id="mname-field"
                        label="Middle Name(s):"
                        label-for="middle_name">
                    <b-form-input id="middle_name" v-model="middle_name" type="text" :state="mNameValidation" trim placeholder="Optional"></b-form-input>
                    <b-form-invalid-feedback :state="mNameValidation"> Your middle name must be less than 100 characters and contain no numbers. </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="mNameValidation"> Looks Good </b-form-valid-feedback>
                </b-form-group>

                <b-form-group
                        id="lname-field"
                        label="Last Name(s):"
                        label-for="last_name">
                    <b-form-input id="last_name" v-model="last_name" type="text" :state="lNameValidation" trim required></b-form-input>
                    <b-form-invalid-feedback :state="lNameValidation"> Your last name must be between 1-100 characters and contain no numbers. </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="lNameValidation"> Looks Good </b-form-valid-feedback>
                </b-form-group>

                <b-form-group
                        id="email-field"
                        description="Note: this will be your username"
                        label="Email:"
                        label-for="email">
                    <b-form-input id="email" v-model="email" type="text" :state="emailValidation" trim required></b-form-input>
                    <b-form-invalid-feedback :state="emailValidation"> Your email must be valid and unique! </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="emailValidation"> Looks Good </b-form-valid-feedback>
                </b-form-group>

                <b-form-group
                        id="password-field"
                        label="Password:"
                        label-for="newPassword">
                    <b-form-input id="newPassword" v-model="password" type="password" :state="passwordValidation" trim required></b-form-input>
                    <b-form-invalid-feedback :state="passwordValidation"> Your password must be between 5 and 15 characters. </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="passwordValidation"> Looks Good </b-form-valid-feedback>
                </b-form-group>

                <b-form-group
                        id="rePassword-field"
                        description="Please re-enter your password"
                        label="Retype Password:"
                        label-for="rePassword">
                    <b-form-input id="rePassword" v-model="rePassword" type="password" :state="rePasswordValidation" trim required></b-form-input>
                    <b-form-invalid-feedback :state="rePasswordValidation"> This isn't the same as the password! </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="rePasswordValidation"> Looks Good </b-form-valid-feedback>
                </b-form-group>

                <b-form-group
                        id="dateOfBirth-field"
                        label="Date of Birth:"
                        label-for="dateOfBirth">
                    <b-form-input id="dateOfBirth" v-model="dateOfBirth" type="date" :state="dateOfBirthValidation" trim required></b-form-input>
                    <b-form-invalid-feedback :state="dateOfBirthValidation"> You need a date of birth. </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="dateOfBirthValidation"> Looks Good </b-form-valid-feedback>
                </b-form-group>

                <b-form-group
                        id="gender-field"
                        label="Gender:"
                        label-for="gender">
                    <b-form-select id="gender" v-model="gender" :options="genderOptions" :state="genderValidation" trim required></b-form-select>
                    <b-form-invalid-feedback :state="genderValidation"> Please select a gender. </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="genderValidation"> Looks Good </b-form-valid-feedback>
                </b-form-group>
                <b-alert v-model="showError" variant="danger" dismissible>The form contains errors!</b-alert>
                <b-button variant="primary" block @click="checkPersonalForm" >Next</b-button>

            </b-form>
        </div>

        <div v-if="showSecond" id="secondSignup">
            <b-form>
                <b-row>
                    <b-col>
                        <b-form-group
                                id="nationalities-field"
                                label="Nationality:"
                                label-for="nationality">
                            <b-form-select id="nationality" v-model="nationalities" :state="nationalityValidation" multiple trim>
                                <option v-for="nationality in nationalityOptions" :value="nationality.id">{{nationality.nationality}}</option>
                            </b-form-select>
                            <b-form-invalid-feedback :state="nationalityValidation"> Please select at least one nationality. </b-form-invalid-feedback>
                            <b-form-valid-feedback :state="nationalityValidation"> Looks Good </b-form-valid-feedback>
                        </b-form-group>
                    </b-col>
                    <b-col>
                        <b-form-group
                                id="passports-field"
                                label="Passport Country:"
                                label-for="passports">
                            <b-form-select id="passports" v-model="passports" :state="passportValidation" trim multiple>
                                <option v-for="nationality in nationalityOptions" :value="nationality.id">{{nationality.country}}</option>
                            </b-form-select>
                            <b-form-invalid-feedback :state="passportValidation"> Please select at least one passport country. </b-form-invalid-feedback>
                            <b-form-valid-feedback :state="passportValidation"> Looks Good </b-form-valid-feedback>
                        </b-form-group>
                    </b-col>
                </b-row>
                <b-form-group
                        id="travType-field"
                        label="Traveller Type:"
                        label-for="travellerTypeCarousel">
                    <b-carousel
                            id="travellerTypeCarousel"
                            :interval="10000"
                            controls
                            indicators
                            background="#ababab"
                            img-width="1920"
                            img-height="1080"
                            style="text-shadow: 1px 1px 2px #333;">
                        <b-carousel-slide v-for="travType in travTypeOptions"
                                          :key="travType.id"
                                          :caption="travType.travellerType"
                                          :text="travType.description"
                                          :img-src="travType.imgUrl"
                                          :state="travTypeValidation">
                            <b-form-checkbox :value="travType.id" v-model="travTypes"></b-form-checkbox>
                        </b-carousel-slide>
                    </b-carousel>
                    <b-form-invalid-feedback :state="travTypeValidation" align="center"> Please select at least one traveller type. </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="travTypeValidation" align="center"> Looks Good </b-form-valid-feedback>
                </b-form-group>
                <b-button @click="previousPage">Back</b-button>
                <b-button @click="checkAssociateForm" variant="primary" class="float-right">Sign Up</b-button>
            </b-form>
        </div>

</div>
</template>

<script>
    export default {
        name: "Signup",
        props: {
            'nationalityOptions': Array,
            'travTypeOptions': Array
        },
        data: function() {
            return {
                showError: false,
                first_name: '',
                middle_name: '',
                last_name: '',
                email: '',
                password: '',
                rePassword: '',
                dateOfBirth: '',
                gender: '',
                genderOptions: [
                    {value: 'Male', text: 'Male'},
                    {value: 'Female', text: 'Female'},
                    {value: 'Other', text: 'Other'}
                ],
                showFirst: true,
                showSecond: false,
                nationalities: [],
                passports: [],
                travTypes: [],
                validEmail: false
            }
        },
        computed: {
            fNameValidation() {
                if (this.first_name.length === 0) {
                    return null;
                }
                let nameRegex = new RegExp("^(?=.{1,100}$)([a-zA-Z]+((-|'| )[a-zA-Z]+)*)$");
                return nameRegex.test(this.first_name);
            },
            mNameValidation() {
                let nameRegex = new RegExp("^(?=.{0,100}$)([a-zA-Z]+((-|'| )[a-zA-Z]+)*)$");
                return nameRegex.test(this.middle_name) || this.middle_name.length === 0 ;
            },
            lNameValidation() {
                if (this.last_name.length === 0) {
                    return null;
                }
                let nameRegex = new RegExp("^(?=.{1,100}$)([a-zA-Z]+((-|'| )[a-zA-Z]+)*)$");
                return nameRegex.test(this.last_name);
            },
            emailValidation() {
                if (this.email.length === 0) {
                    return null;
                }
                let emailRegex = new RegExp("^([a-zA-Z0-9]+(@)([a-zA-Z]+((.)[a-zA-Z]+)*))(?=.{3,15})");
                this.checkUsername();
                return (emailRegex.test(this.email) && this.validEmail);
            },
            passwordValidation() {
                if (this.password.length === 0) {
                    return null;
                }
                let passwordRegex = new RegExp("^(((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])(?=.*[0-9]))|((?=.*[A-Z])(?=.*[0-9])))(?=.{5,15})");
                return passwordRegex.test(this.password)
            },
            rePasswordValidation() {
                if (this.rePassword.length === 0) {
                    return null;
                }
                return this.password.length > 0 && this.rePassword === this.password;
            },
            dateOfBirthValidation() {
                if (this.dateOfBirth.length === 0) {
                    return null;
                }
                return this.dateOfBirth.length > 0;
            },
            genderValidation() {
                if (this.gender.length === 0) {
                    return null;
                }
                return this.gender.length > 0;
            },
            nationalityValidation() {
                if (this.nationalities.length === 0) {
                    return null;
                }
                return this.nationalities.length > 0;
            },
            passportValidation() {
                if (this.passports.length === 0) {
                    return null;
                }
                return this.passports.length > 0;
            },
            travTypeValidation() {
                if (this.travTypes.length === 0) {
                    return null;
                }
                return this.travTypes.length > 0;
            }

        },
        methods: {
            checkPersonalForm() {
                if (this.fNameValidation && this.mNameValidation && this.lNameValidation && this.emailValidation
                    && this.passwordValidation && this.rePasswordValidation && this.dateOfBirthValidation && this.genderValidation) {
                    this.showError = false;
                    this.nextPage();
                } else {
                    this.showError = true;
                }
            },
            checkAssociateForm() {
                if (this.nationalityValidation && this.passportValidation && this.travTypeValidation) {
                    let profile = {
                        first_name: this.first_name,
                        middle_name: this.middle_name,
                        last_name: this.last_name,
                        username: this.email,
                        password: this.password,
                        date_of_birth: this.dateOfBirth,
                        gender: this.gender,
                        nationality: this.nationalities,
                        passport_country: this.passports,
                        traveller_type: this.travTypes
                    };
                    this.saveProfile(profile);
                }
            },
            checkUsername() {
                let self = this;
                fetch(`/v1/checkUsername`, {
                    method: 'POST',
                    headers:{'content-type': 'application/json'},
                    body: JSON.stringify({'username': this.email})

                }).then(function(response) {
                    self.validEmail = response.ok;
                })

            },
            nextPage() {
                this.showFirst = false;
                this.showSecond = true;
            },
            previousPage() {
                this.showFirst = true;
                this.showSecond = false;
            },

            saveProfile(profile) {
                fetch('/v1/profiles', {
                    method: 'POST',
                    headers:{'content-type': 'application/json'},
                    body: JSON.stringify(profile)
                }).then(function(response) {
                    window.location.pathname ="/dash";
                    return response.json();
                })
            },
            parseJSON (response) {
                return response.json();
            },
        }
    }
</script>

<style>

</style>