<template>
    <div class="container">
        <h1 class="page-title">Add a Destination</h1>
        <p class="page-title"><i>Add a destination using the form below</i></p>
        <b-alert v-model="showError" variant="danger" dismissible>{{errorMessage}}</b-alert>

        <!--Displays a progress bar alert on submission which ticks down time to act
        as a buffer for destination being added-->
        <b-alert
                :show="dismissCountDown"
                dismissible
                variant="success"
                @dismissed="dismissCountDown=0"
                @dismiss-count-down="countDownChanged"
        >
            <p>Destination Successfully Added</p>
            <b-progress
                    variant="success"
                    :max="dismissSecs"
                    :value="dismissCountDown"
                    height="4px"
            ></b-progress>
        </b-alert>

        <!--Form for adding a destination-->
        <div>
            <b-form>
                <b-form-group
                        id="name-field"
                        label="Destination Name:"
                        label-for="dName">
                    <b-form-input id="dName" v-model="dName" type="text" required
                                  :state="destinationNameValidation"></b-form-input>
                </b-form-group>

                <b-form-group
                        id="type-field"
                        label="Destination Type:"
                        label-for="type">
                    <b-form-select id="type" v-model="dType" trim :state="destinationTypeValidation">
                        <option v-for="destination in destinationTypes" :value="destination.id">
                            {{destination.destinationType}}
                        </option>
                    </b-form-select>
                </b-form-group>

                <b-form-group
                        id="district-field"
                        label="District:"
                        label-for="district">
                    <b-form-input id="district" v-model="dDistrict" type="text" trim required
                                  :state="destinationDistrictValidation"></b-form-input>
                </b-form-group>

                <b-form-group
                        id="latitude-field"
                        label="Latitude:"
                        label-for="latitude">
                    <b-form-input id="latitude" v-model="dLatitude" type="text" trim required
                                  :state="destinationLatitudeValidation"></b-form-input>
                    <b-form-invalid-feedback :state="destinationLatitudeValidation" align="center">
                        {{latitudeErrorMessage}}
                    </b-form-invalid-feedback>
                </b-form-group>

                <b-form-group
                        id="longitude-field"
                        label="Longitude:"
                        label-for="longitude">
                    <b-form-input id="longitude" v-model="dLongitude" type="text" trim required
                                  :state="destinationLongitudeValidation"></b-form-input>
                    <b-form-invalid-feedback :state="destinationLongitudeValidation" align="center">
                        {{longitudeErrorMessage}}
                    </b-form-invalid-feedback>
                </b-form-group>

                <b-form-group
                        id="country-field"
                        label="Country:"
                        label-for="country">
                    <b-form-input id="country" v-model="dCountry" type="text" trim required
                                  :state="destinationCountryValidation"></b-form-input>
                    <b-form-invalid-feedback :state="destinationCountryValidation" align="center">
                        Country cannot have any numbers in it!
                    </b-form-invalid-feedback>
                </b-form-group>

                <b-button block variant="primary" @click="checkDestinationFields">Add Destination</b-button>
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
                dName: "",
                dType: "",
                dDistrict: "",
                dLatitude: null,
                dLongitude: null,
                dCountry: "",
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
             * Validates the input fields based on regex
             * @returns {*} true if input is valid
             */
            destinationNameValidation() {
                if (this.dName.length === 0) {
                    return null;
                }
                return this.dName.length > 0;
            },
            destinationTypeValidation() {
                if (this.dType.length === 0) {
                    return null;
                }
                return this.dType.length > 0 || this.dType !== null;
            },
            destinationDistrictValidation() {
                if (this.dDistrict.length === 0) {
                    return null;
                }
                return this.dDistrict.length > 0;
            },
            destinationLatitudeValidation() {
                if (this.dLatitude === null) {
                    return null;
                }
                if (isNaN(this.dLatitude)) {
                    this.latitudeErrorMessage = "Latitude: '" + this.dLatitude + "' is not a number!";
                    return false;
                } else if (this.dLatitude > 90 || this.dLatitude < -90) {
                    this.latitudeErrorMessage = "Latitude: '" + this.dLatitude + "' must be between -90 and 90";
                    return false;
                }
                return true;
            },
            destinationLongitudeValidation() {
                if (this.dLongitude === null) {
                    return null;
                }
                if (isNaN(this.dLongitude)) {
                    this.longitudeErrorMessage = "Longitude: '" + this.dLongitude + "' is not a number!";
                    return false;
                } else if (this.dLongitude > 180 || this.dLongitude < -180) {
                    this.longitudeErrorMessage = "Longitude: '" + this.dLongitude + "' must be between -180 and 180";
                    return false;
                }
                return true;
            },
            destinationCountryValidation() {
                if (this.dCountry.length === 0) {
                    return null;
                }
                let countryRegex = /\d/;
                return !countryRegex.test(this.dCountry);
            }
        },
        methods: {
            /**
             * Checks that all fields are present and runs validation
             * On fail shows errors
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
             * Sets all fields to blank
             */
            resetDestForm() {
                this.dName = "";
                this.dType = "";
                this.dDistrict = "";
                this.dLatitude = "";
                this.dLongitude = "";
                this.dCountry = "";
            },

            /**
             * Adds new destination to database, then resets form and shows success alert
             * Checks whether location is duplicate and displays error if so
             * @param cb
             * @returns {Promise<Response | never>}
             */
            addDestination(cb) {
                let self = this;
                fetch(`/v1/destinations`, {
                    method: 'POST',
                    headers: {'content-type': 'application/json'},
                    body: (JSON.stringify({
                        "name": this.dName,
                        "type_id": this.dType,
                        "district": this.dDistrict,
                        "latitude": parseFloat(this.dLatitude),
                        "longitude": parseFloat(this.dLongitude),
                        "country": this.dCountry
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