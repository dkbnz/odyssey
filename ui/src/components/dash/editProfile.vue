<template>
    <div :class="containerClass">
        <h1 class="page-title">Edit Profile</h1>
        <p class="page-title"><i>Edit your profile using the form below!</i></p>
        <b-alert variant="success" v-model="showSuccess">Profile successfully saved!</b-alert>
        <!--First name field, with default set to the user's current first name. Validates inputted text-->
        <b-row>
            <b-col>
                <b-form-group
                        id="firstName-field"
                        label="First Name(s):"
                        label-for="firstName">
                    <b-form-input :state="firstNameValidation"
                                  id="firstName"
                                  trim
                                  type="text" v-model="saveProfile.firstName">
                    </b-form-input>
                    <b-form-invalid-feedback :state="firstNameValidation">
                        Your first name must be between 1-100 characters and contain no numbers.
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="firstNameValidation">
                        Looks Good
                    </b-form-valid-feedback>
                </b-form-group>

                <!--Middle name field with default set to the user's current middle name. Validates inputted text-->
                <b-form-group
                        id="middleName-field"
                        label="Middle Name(s):"
                        label-for="middleName">
                    <b-form-input :state="middleNameValidation"
                                  id="middleName"
                                  trim
                                  type="text" v-model="saveProfile.middleName">
                    </b-form-input>
                    <b-form-invalid-feedback :state="middleNameValidation">
                        Your middle name must be less than 100 characters and contain no numbers.
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="middleNameValidation">
                        Looks Good
                    </b-form-valid-feedback>
                </b-form-group>

                <!--Last name field with default set to the user's current last name. Validates inputted text-->
                <b-form-group
                        id="lastName-field"
                        label="Last Name(s):"
                        label-for="lastName">
                    <b-form-input :state="lastNameValidation"
                                  id="lastName"
                                  trim
                                  type="text" v-model="saveProfile.lastName">
                    </b-form-input>
                    <b-form-invalid-feedback :state="lastNameValidation">
                        Your last name must be between 1-100 characters and contain no numbers.
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="lastNameValidation">
                        Looks Good
                    </b-form-valid-feedback>
                </b-form-group>
            </b-col>
            <b-col>
                <!--Email field with default set to the user's current email. Validates inputted text-->
                <b-form-group
                        id="email-field"
                        label="Email:"
                        label-for="email">
                    <b-form-input :state="emailValidation"
                                  id="email"
                                  trim
                                  type="email" v-model="saveProfile.username">
                    </b-form-input>
                    <b-form-invalid-feedback :state="emailValidation">
                        Your email must be valid and unique!
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="emailValidation">
                        Looks Good
                    </b-form-valid-feedback>
                </b-form-group>

                <!--Date of birth field with default set to the user's current date of birth. Validates inputted text-->
                <b-form-group
                        id="DOB-field"
                        label="Date of Birth:"
                        label-for="dateOfBirth">
                    <b-form-input :state="dateOfBirthValidation"
                                  :type="'date'"
                                  id="dateOfBirth"
                                  trim v-model="saveProfile.dateOfBirth">
                    </b-form-input>
                    <b-form-invalid-feedback :state="dateOfBirthValidation">
                        You need a date of birth.
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="dateOfBirthValidation">
                        Looks Good
                    </b-form-valid-feedback>
                </b-form-group>

                <!--Gender field with default set to the user's current gender. Validates inputted text-->
                <b-form-group
                        id="gender-field"
                        label="Gender:"
                        label-for="gender">
                    <b-form-select :options=genderOptions
                                   :state="genderValidation"
                                   id="gender"
                                   required trim v-model="saveProfile.gender">
                    </b-form-select>
                    <b-form-invalid-feedback :state="genderValidation">
                        Please select a gender.
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="genderValidation">
                        Looks Good
                    </b-form-valid-feedback>
                </b-form-group>
            </b-col>
        </b-row>
        <b-row>
            <b-col>
                <!--Password field which validates inputted text-->
                <b-form-group
                        id="password-field"
                        label="Password:"
                        label-for="password">
                    <b-form-input :state="passwordValidation"
                                  id="password"
                                  placeholder="Unchanged"
                                  trim
                                  type="password" v-model="saveProfile.password">
                    </b-form-input>
                    <b-form-invalid-feedback :state="passwordValidation">
                        Your password must be between 5 and 15 characters and password must contain two of: Uppercase,
                        Lowercase, Number
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="passwordValidation">
                        Looks Good
                    </b-form-valid-feedback>
                </b-form-group>
            </b-col>
            <b-col>
                <!--Password re-entry field which validates inputted text-->
                <b-form-group
                        id="passwordRe-field"
                        label="Retype Password:"
                        label-for="passwordRe">
                    <b-form-input
                            :state="rePasswordValidation"
                            id="passwordRe"
                            placeholder="Unchanged"
                            trim
                            type="password" v-model="rePassword">
                    </b-form-input>
                    <b-form-invalid-feedback :state="rePasswordValidation">
                        This must be the same as the password and your must be between 5 and 15 characters and
                        password must contain two of: Uppercase, Lowercase, Number.
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="rePasswordValidation">
                        Looks Good
                    </b-form-valid-feedback>
                </b-form-group>
            </b-col>
        </b-row>

        <b-row>
            <!--Nationality field which displays user's current nationalities & all other nationalities.
            Validates inputted text-->
            <b-col>
                <b-form-group
                        id="nationalities-field"
                        label="Nationality:"
                        label-for="nationality">
                    <b-form-select :required="true"
                                   :state="nationalityValidation"
                                   id="nationality"
                                   multiple
                                   trim
                                   v-model="saveProfileNationalities">
                        <option
                                v-for="nationality in nationalityOptions"
                                :value="{id: nationality.id, nationality: nationality.nationality, country: nationality.country}">
                            {{nationality.nationality}}
                        </option>
                    </b-form-select>
                    <p class="descriptionText">Use 'Ctrl-Click' to select more than one.</p>
                    <b-form-invalid-feedback :state="nationalityValidation">
                        Please select at least one nationality.
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="nationalityValidation">
                        Looks Good
                    </b-form-valid-feedback>
                </b-form-group>
            </b-col>

            <!--Passports field which displays user's current passports & all other passports.
            Validates inputted text-->
            <b-col>
                <b-form-group
                        id="passports-field"
                        label="Passport:"
                        label-for="passports">
                    <b-form-select :required="true"
                                   :state="passportValidation"
                                   id="passport"
                                   multiple
                                   trim
                                   v-model="saveProfilePassports">
                        <option
                                v-for="passport in nationalityOptions"
                                :value="{id: passport.id, country: passport.country}">
                            {{passport.country}}
                        </option>
                    </b-form-select>
                    <p class="descriptionText">Use 'Ctrl-Click' to select more than one.</p>
                    <b-form-invalid-feedback :state="passportValidation">
                        Please select at least one passport country.
                    </b-form-invalid-feedback>
                    <b-form-valid-feedback :state="passportValidation">
                        Looks Good
                    </b-form-valid-feedback>
                </b-form-group>
            </b-col>
        </b-row>

        <!--Traveller type field which displays user's current traveller types & all other types.
            Validates inputted text-->
        <b-form-group
                id="travType-field"
                label="Traveller Type(s):"
                label-for="travType">
            <b-form-select :required="true"
                           :state="travTypeValidation"
                           id="travType"
                           multiple
                           trim
                           v-model="saveProfileTravellerTypes">
                <option
                        v-for="travellerType in travTypeOptions"
                        :value="{id: travellerType.id, travellerType: travellerType.travellerType}">
                    {{travellerType.travellerType}}
                </option>
            </b-form-select>
            <p class="descriptionText">Use 'Ctrl-Click' to select more than one.</p>
            <b-form-invalid-feedback :state="travTypeValidation">
                Please select at least one traveller type.
            </b-form-invalid-feedback>
            <b-form-valid-feedback :state="travTypeValidation">
                Looks Good
            </b-form-valid-feedback>
        </b-form-group>

        <!--Displayed if there are input errors when "Save Profile" is clicked-->
        <b-alert dismissible v-model="showError" variant="danger">The form contains errors!</b-alert>
        <!--Validates inputs then updates user data if valid-->
        <b-button :disabled="!checkSaveProfile()" @click="submitSaveProfile" block size="lg" variant="success">Save Profile</b-button>
    </div>
</template>

<script>
    export default {
        name: "editProfile",
        props: {
            profile: Object,
            nationalityOptions: Array,
            travTypeOptions: Array,
            adminView: {
                default: function () {
                    return false;
                }
            },
            containerClass: {
                default: function() {
                    return 'containerWithNav';
                }
            }
        },
        data: function () {
            return {
                saveProfile: {
                    firstName: this.profile.firstName,
                    middleName: this.profile.middleName,
                    lastName: this.profile.lastName,
                    username: this.profile.username,
                    password: "",
                    dateOfBirth: this.profile.dateOfBirth,
                    gender: this.profile.gender,
                    nationalities: [],
                    passports: [],
                    travellerTypes: []
                },
                rePassword: "",
                validEmail: false,
                showSuccess: false,
                showError: false,
                genderOptions: [
                    {value: 'Male', text: 'Male'},
                    {value: 'Female', text: 'Female'},
                    {value: 'Other', text: 'Other'}
                ],
                nationalitiesSelected: [],
                passportsSelected: [],
                travellerTypesSelected: []

            }
        },
        computed: {
            /**
             * Validates input fields based on regular expression.
             *
             * @returns {boolean} true if input is valid, false if invalid, or null if field remains unselected.
             */
            firstNameValidation() {
                if (this.saveProfile.firstName.length === 0) {
                    return false;
                }
                let nameRegex = new RegExp("^(?=.{1,100}$)([a-zA-Z]+((-|'| )[a-zA-Z]+)*)$");
                return nameRegex.test(this.saveProfile.firstName);
            },
            middleNameValidation() {
                let nameRegex = new RegExp("^(?=.{0,100}$)([a-zA-Z]+((-|'| )[a-zA-Z]+)*)$");
                return nameRegex.test(this.saveProfile.middleName) || this.saveProfile.middleName.length === 0;
            },
            lastNameValidation() {
                if (this.saveProfile.lastName.length === 0) {
                    return false;
                }
                let nameRegex = new RegExp("^(?=.{1,100}$)([a-zA-Z]+((-|'| )[a-zA-Z]+)*)$");
                return nameRegex.test(this.saveProfile.lastName);
            },
            emailValidation() {
                if (this.saveProfile.username.length === 0) {
                    return false;
                }
                let emailRegex = new RegExp("^([a-zA-Z0-9]+(@)([a-zA-Z]+((.)[a-zA-Z]+)*))(?=.{3,15})");
                this.checkUsername();
                return (emailRegex.test(this.saveProfile.username) && this.validEmail);
            },
            passwordValidation() {
                if (this.saveProfile.password.length === 0) {
                    return null;
                }
                let passwordRegex = new RegExp("^(((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])" +
                    "(?=.*[0-9]))|((?=.*[A-Z])(?=.*[0-9])))(?=.{5,15})");
                return passwordRegex.test(this.saveProfile.password)
            },
            rePasswordValidation() {
                if (this.rePassword.length === 0) {
                    return null;
                }
                let passwordRegex =
                    new RegExp("^(((?=.*[a-z])(?=.*[A-Z]))|((?=.*[a-z])(?=.*[0-9]))|((?=.*[A-Z])(?=.*[0-9])))(?=.{5,15})");
                return this.saveProfile.password.length > 0 && this.rePassword === this.saveProfile.password && passwordRegex.test(this.rePassword);
            },
            dateOfBirthValidation() {
                if (this.saveProfile.dateOfBirth.length === 0) {
                    return false;
                }
                return this.saveProfile.dateOfBirth.length > 0;
            },
            genderValidation() {
                if (this.saveProfile.gender.length === 0) {
                    return null;
                }
                return this.saveProfile.gender.length > 0;
            },
            nationalityValidation() {
                if (this.saveProfileNationalities.length === 0) {
                    return false;
                }
                return this.saveProfileNationalities.length > 0;
            },
            passportValidation() {
                if (this.saveProfilePassports.length === 0) {
                    return null;
                }
                return this.saveProfilePassports.length > 0;
            },
            travTypeValidation() {
                if (this.saveProfileTravellerTypes.length === 0) {
                    return false;
                }
                return this.saveProfileTravellerTypes.length > 0;
            },


            /**
             * Default value for the profile nationalities so already selected.
             *
             */
            saveProfileNationalities: {
                get() {
                    return this.profile.nationalities;
                },
                set(nationalities) {
                    this.nationalitiesSelected = nationalities;
                }
            },


            /**
             * Default value for the profile passports so already selected.
             *
             */
            saveProfilePassports: {
                get() {
                    return this.profile.passports;
                },
                set(passports) {
                    this.passportsSelected = passports;
                }
            },


            /**
             * Default value for the profile traveller types so already selected.
             *
             */
            saveProfileTravellerTypes: {
                get() {
                    let travellerTypes = JSON.parse(JSON.stringify(this.profile.travellerTypes));
                    for (let i = 0; i < travellerTypes.length; i++) {
                        delete travellerTypes[i].description;
                        delete travellerTypes[i].imgUrl;
                    }
                    return travellerTypes;
                },
                set(travellerTypes) {
                    this.travellerTypesSelected = travellerTypes;
                }
            }
        },
        methods: {
            /**
             * Checks that username does not exist in database.
             */
            checkUsername() {
                let self = this;
                fetch(`/v1/checkUsername`, {
                    method: 'POST',
                    headers: {'content-type': 'application/json'},
                    body: JSON.stringify({'username': this.saveProfile.username})

                }).then(function (response) {
                    self.validEmail = response.ok || (self.saveProfile.username === self.profile.username)
                })

            },


            /**
             * Runs all field validation. If any errors occur, displays an error.
             */
            checkSaveProfile() {
                if (this.firstNameValidation && this.middleNameValidation && this.lastNameValidation
                    && this.emailValidation && this.dateOfBirthValidation && this.genderValidation
                    && this.nationalityValidation && this.travTypeValidation) {
                    if (this.passwordValidation == null
                        || (this.passwordValidation === true && this.rePasswordValidation === true)) {
                        this.showError = false;
                        return true;
                    } else {
                        this.showError = true;
                        return false;
                    }
                } else {
                    this.showError = true;
                    return false;
                }
            },


            /**
             * Sets the appropriate nationality value for the saved profile.
             */
            retrieveNationalities() {
                if(this.nationalitiesSelected.length !== 0) {
                    this.saveProfile.nationalities = this.nationalitiesSelected;
                } else {
                    this.saveProfile.nationalities = this.profile.nationalities;
                }
            },


            /**
             * Sets the appropriate passport value for the saved profile.
             */
            retrievePassports() {
                if(this.passportsSelected.length !== 0) {
                    this.saveProfile.passports = this.passportsSelected;
                } else {
                    this.saveProfile.passports = this.profile.passports;
                }
            },


            /**
             * Sets the appropriate traveller type value for the saved profile.
             */
            retrieveTravellerTypes() {
                if(this.travellerTypesSelected.length !== 0) {
                    this.saveProfile.travellerTypes = this.travellerTypesSelected;
                } else {
                    this.saveProfile.travellerTypes = this.profile.travellerTypes;
                }
            },


            /**
             * Sends profile changes to profileController and reloads page using the Vue Router.
             */
            submitSaveProfile() {
                let self = this;
                if (this.checkSaveProfile) {
                    this.$emit('profileSaved', true);
                    this.retrieveNationalities();
                    this.retrievePassports();
                    this.retrieveTravellerTypes();

                    fetch('/v1/profile/' + this.profile.id, {
                        method: 'PUT',
                        headers: {'content-type': 'application/json'},
                        body: JSON.stringify(this.saveProfile)
                    }).then(function (response) {
                        if (!self.adminView) {
                            self.$router.go();
                        } else {
                            self.showSuccess = true;
                            setTimeout(function() {
                                self.showSuccess = false;
                            }, 3000)
                        }
                        self.$emit('profile-saved', self.saveProfile);
                        window.scrollTo(0, 0);
                        return response.json();
                    })
                }
            },


            /**
             * Check for duplicates of nationalities in order to remove them from "other" option fields.
             *
             * @param id of nationality to check.
             * @returns {boolean} true if value is duplicate of user's current.
             */
            duplicateNationality(id) {
                for (let i = 0; i < this.profile.nationalities.length; i++) {
                    if (this.profile.nationalities[i].id === id) {
                        return true;
                    }
                }
            },

            /**
             * Check for duplicates of passports in order to remove them from "other" option fields.
             *
             * @param id of passport to check.
             * @returns {boolean} true if value is duplicate of user's current.
             */
            duplicatePassport(id) {
                for (let i = 0; i < this.profile.passports.length; i++) {
                    if (this.profile.passports[i].id === id) {
                        return true;
                    }
                }
            },


            /**
             * Check for duplicates of traveller types in order to remove them from "other" option fields.
             *
             * @param id of traveller type to check.
             * @returns {boolean} true if value is duplicate of user's current.
             */
            duplicateTravType(id) {
                for (let i = 0; i < this.profile.travellerTypes.length; i++) {
                    if (this.profile.travellerTypes[i].id === id) {
                        return true;
                    }
                }
            }
        }
    }
</script>