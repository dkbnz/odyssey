<template>
    <div class="containerWithNav">
        <h1 class="page-title">Add a Destination</h1>
        <p class="page-title"><i>Add a destination using the form below</i></p>
        <b-alert dismissible v-model="showError" variant="danger">{{errorMessage}}</b-alert>

        <!--Displays a progress bar alert on submission which ticks down time to act
        as a buffer for destination being added-->
        <b-alert
                :show="dismissCountDown"
                @dismiss-count-down="countDownChanged"
                @dismissed="dismissCountDown=0"
                dismissible
                variant="success"
        >
            <p>Destination Successfully Added</p>
            <b-progress
                    :max="dismissSecs"
                    :value="dismissCountDown"
                    height="4px"
                    variant="success"
            ></b-progress>
        </b-alert>

        <!--Form for adding a destination-->
        <div>
            <b-form>
                <b-form-group
                        id="name-field"
                        label="Destination Name:"
                        label-for="destinationName">
                    <b-form-input id="destinationName" v-model="destinationName" type="text" required
                                  :state="destinationNameValidation"></b-form-input>
                </b-form-group>

                <b-form-group
                        id="type-field"
                        label="Destination Type:"
                        label-for="type">
                    <b-form-select id="type" v-model="destinationType" trim :state="destinationTypeValidation">
                        <option v-for="destination in destinationTypes" :value="destination.id">
                            {{destination.destinationType}}
                        </option>
                    </b-form-select>
                </b-form-group>

                <b-form-group
                        id="district-field"
                        label="District:"
                        label-for="district">
                    <b-form-input id="district" v-model="destinationDistrict" type="text" trim required
                                  :state="destinationDistrictValidation"></b-form-input>
                </b-form-group>

                <b-form-group
                        id="latitude-field"
                        label="Latitude:"
                        label-for="latitude">
                    <b-form-input id="latitude" v-model="destinationLatitude" type="text" trim required
                                  :state="destinationLatitudeValidation"></b-form-input>
                    <b-form-invalid-feedback :state="destinationLatitudeValidation">
                        {{latitudeErrorMessage}}
                    </b-form-invalid-feedback>
                </b-form-group>

                <b-form-group
                        id="longitude-field"
                        label="Longitude:"
                        label-for="longitude">
                    <b-form-input id="longitude" v-model="destinationLongitude" type="text" trim required
                                  :state="destinationLongitudeValidation"></b-form-input>
                    <b-form-invalid-feedback :state="destinationLongitudeValidation">
                        {{longitudeErrorMessage}}
                    </b-form-invalid-feedback>
                </b-form-group>

                <b-form-group
                        id="country-field"
                        label="Country:"
                        label-for="country">
                    <b-form-input id="country" v-model="destinationCountry" type="text" trim required
                                  :state="destinationCountryValidation"></b-form-input>
                    <b-form-invalid-feedback :state="destinationCountryValidation">
                        Country cannot have any numbers in it!
                    </b-form-invalid-feedback>
                </b-form-group>

                <b-button @click="checkDestinationFields" block variant="primary">Add Destination</b-button>
            </b-form>
        </div>
    </div>
</template>

<script>
    export default {
        name: "addDestinations",
        props: ['profile', 'destinations', 'destinationTypes'],
        data() {
            return {
                destinationName: "",
                destinationType: "",
                destinationDistrict: "",
                destinationLatitude: null,
                destinationLongitude: null,
                destinationCountry: "",
                showError: false,
                errorMessage: "",
                successTripAddedAlert: false,
                dismissSecs: 3,
                dismissCountDown: 0,
                latitudeErrorMessage: "",
                longitudeErrorMessage: "",
            }
        },
        computed: {
            /**
             * Validates the input fields based on regex.
             *
             * @returns {*} true if input is valid.
             */
            destinationNameValidation() {
                if (this.destinationName.length === 0) {
                    return null;
                }
                return this.destinationName.length > 0;
            },
            destinationTypeValidation() {
                if (this.destinationType.length === 0) {
                    return null;
                }
                return this.destinationType.length > 0 || this.destinationType !== null;
            },
            destinationDistrictValidation() {
                if (this.destinationDistrict.length === 0) {
                    return null;
                }
                return this.destinationDistrict.length > 0;
            },
            destinationLatitudeValidation() {
                if (this.destinationLatitude === null) {
                    return null;
                }
                if (isNaN(this.destinationLatitude)) {
                    this.latitudeErrorMessage = "Latitude: '" + this.destinationLatitude + "' is not a number!";
                    return false;
                } else if (this.destinationLatitude > 90 || this.destinationLatitude < -90) {
                    this.latitudeErrorMessage = "Latitude: '" + this.destinationLatitude + "' must be between " +
                        "-90 and 90";
                    return false;
                }
                return true;
            },
            destinationLongitudeValidation() {
                if (this.destinationLongitude === null) {
                    return null;
                }
                if (isNaN(this.destinationLongitude)) {
                    this.longitudeErrorMessage = "Longitude: '" + this.destinationLongitude + "' is not a number!";
                    return false;
                } else if (this.destinationLongitude > 180 || this.destinationLongitude < -180) {
                    this.longitudeErrorMessage = "Longitude: '" + this.destinationLongitude + "' must be between " +
                        "-180 and 180";
                    return false;
                }
                return true;
            },
            destinationCountryValidation() {
                if (this.destinationCountry.length === 0) {
                    return null;
                }
                let countryRegex = /\d/;
                return !countryRegex.test(this.destinationCountry);
            }
        },
        methods: {
            /**
             * Checks that all fields are present and runs validation.
             * On fail shows errors.
             */
            checkDestinationFields() {
                if(this.destinationNameValidation && this.destinationTypeValidation
                    && this.destinationDistrictValidation && this.destinationLatitudeValidation
                    && this.destinationLongitudeValidation && this.destinationCountryValidation) {
                    this.showError = false;
                    this.addDestination();
                }
                else {
                    this.errorMessage = ("Please enter in all fields!");
                    this.showError = true;
                }
            },

            /**
             * Sets all fields to blank.
             */
            resetDestForm() {
                this.destinationName = "";
                this.destinationType = "";
                this.destinationDistrict = "";
                this.destinationLatitude = "";
                this.destinationLongitude = "";
                this.destinationCountry = "";
            },

            /**
             * Adds new destination to database, then resets form and shows success alert.
             * Checks whether location is duplicate and displays error if so.
             *
             * @param cb.
             * @returns {Promise<Response | never>}.
             */
            addDestination(cb) {
                let self = this;
                fetch(`/v1/destinations`, {
                    method: 'POST',
                    headers: {'content-type': 'application/json'},
                    body: (JSON.stringify({
                        "name": this.destinationName,
                        "type_id": this.destinationType,
                        "district": this.destinationDistrict,
                        "latitude": parseFloat(this.destinationLatitude),
                        "longitude": parseFloat(this.destinationLongitude),
                        "country": this.destinationCountry
                    }))
                })
                    .then(this.parseJSON)
                    .then(cb)

                    .then(function(response) {
                        if (response.ok) {
                            self.resetDestForm();
                            self.showAlert();
                            self.emit('data-changed', true);
                            return JSON.parse(JSON.stringify(response));
                        } else {
                            self.errorMessage = "";
                            self.showError = true;
                            response.clone().text().then(text => {
                                self.errorMessage = text;
                            });
                        }
                    });
            },

            /**
             * Used to allow an alert to countdown on the successful saving of a destination.
             *
             * @param dismissCountDown      the name of the alert.
             */
            countDownChanged(dismissCountDown) {
                this.dismissCountDown = dismissCountDown
            },

            /**
             * Displays the countdown alert on the successful saving of a destination.
             */
            showAlert() {
                this.dismissCountDown = this.dismissSecs
            },
        }
    }
</script>