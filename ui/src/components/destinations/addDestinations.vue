<template>
    <div>
        <h3 class="page-title">{{heading}} a Destination</h3>
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

        <b-modal hide-footer id="confirmEditModal" ref="confirmEditModal" size="l" title="Confirm Edit">
            <div>
                Are you sure you want to edit this destination?
                This would affect the following trips:
                <ul v-for="trip in destinationConflicts">
                    <li>{{trip.name}}: {{trip.destinations[0].destination.name}} -
                        {{trip.destinations[trip.destinations.length - 1].destination.name}}</li>
                </ul>
            </div>
            <b-button @click="editDestination" class="mr-2 float-left" variant="success">Confirm</b-button>
            <b-button @click="dismissModal('confirmEditModal')" class="mr-2 float-right" variant="danger">Cancel</b-button>
        </b-modal>

        <!--Form for adding a destination-->
        <div>
            <b-form>
                <b-form-group
                        id="name-field"
                        label="Destination Name:"
                        label-for="name">
                    <b-form-input id="name" @click="showError = false" v-model="inputDestination.name" type="text" required
                                  :state="destinationNameValidation"></b-form-input>
                </b-form-group>
                <b-form-group
                        id="type-field"
                        label="Destination Type:"
                        label-for="type">
                    <b-form-select id="type" v-model="inputDestination.type.id" trim :state="destinationTypeValidation">
                        <option v-for="destination in destinationTypes" :value="destination.id">
                            {{destination.destinationType}}
                        </option>
                    </b-form-select>
                </b-form-group>

                <b-form-group
                        id="district-field"
                        label="District:"
                        label-for="district">
                    <b-form-input id="district" @click="showError = false" v-model="inputDestination.district" type="text" trim required
                                  :state="destinationDistrictValidation"></b-form-input>
                </b-form-group>

                <b-form-group
                        id="latitude-field"
                        label="Latitude:"
                        label-for="latitude">
                    <b-form-input id="latitude" v-model="inputDestination.latitude" type="text" trim required
                                  :state="destinationLatitudeValidation"></b-form-input>
                    <b-form-invalid-feedback :state="destinationLatitudeValidation">
                        {{latitudeErrorMessage}}
                    </b-form-invalid-feedback>
                </b-form-group>

                <b-form-group
                        id="longitude-field"
                        label="Longitude:"
                        label-for="longitude">
                    <b-form-input id="longitude" v-model="inputDestination.longitude" type="text" trim required
                                  :state="destinationLongitudeValidation"></b-form-input>
                    <b-form-invalid-feedback :state="destinationLongitudeValidation">
                        {{longitudeErrorMessage}}
                    </b-form-invalid-feedback>
                </b-form-group>

                <b-form-group
                        id="country-field"
                        label="Country:"
                        label-for="country">
                    <b-form-input id="country" v-model="inputDestination.country" type="text" trim required
                                  :state="destinationCountryValidation"></b-form-input>
                    <b-form-invalid-feedback :state="destinationCountryValidation">
                        Country cannot have any numbers in it!
                    </b-form-invalid-feedback>
                </b-form-group>

                <b-button @click="checkDestinationFields" block variant="primary">{{heading}} Destination</b-button>
            </b-form>
        </div>
    </div>
</template>

<script>
    export default {
        name: "addDestinations",
        props: {
            profile: Object,
            destinationTypes: Array,
            heading: String,
            inputDestination: {
                default: function () {
                    return {
                        id: null,
                        name: "",
                        type: {
                            id: null,
                            destinationType: "",
                        },
                        // type: "",
                        district: "",
                        latitude: null,
                        longitude: null,
                        country: "",
                    }
                }
            },
        },
        data() {
            return {
                // name: "",
                // type: "",
                // district: "",
                // latitude: null,
                // longitude: null,
                // country: "",
                showError: false,
                errorMessage: "",
                successTripAddedAlert: false,
                dismissSecs: 3,
                dismissCountDown: 0,
                latitudeErrorMessage: "",
                longitudeErrorMessage: "",
                destinationConflicts: [],
            }
        },
        computed: {
            /**
             * Validates the input fields based on regex.
             *
             * @returns {*} true if input is valid.
             */
            destinationNameValidation() {
                if (this.inputDestination.name.length === 0) {
                    return null;
                }
                return this.inputDestination.name.length > 0;
            },
            destinationTypeValidation() {
                if (this.inputDestination.type.id == null) {
                    return null;
                }
                return this.inputDestination.type.length > 0 || this.inputDestination.type !== null;
            },
            destinationDistrictValidation() {
                if (this.inputDestination.district.length === 0) {
                    return null;
                }
                return this.inputDestination.district.length > 0;
            },
            destinationLatitudeValidation() {
                if (this.inputDestination.latitude === null) {
                    return null;
                }
                if (isNaN(this.inputDestination.latitude)) {
                    this.latitudeErrorMessage = "Latitude: '" + this.inputDestination.latitude + "' is not a number!";
                    return false;
                } else if (this.inputDestination.latitude > 90 || this.inputDestination.latitude < -90) {
                    this.latitudeErrorMessage = "Latitude: '" + this.inputDestination.latitude + "' must be between " +
                        "-90 and 90";
                    return false;
                }
                return true;
            },
            destinationLongitudeValidation() {
                if (this.inputDestination.longitude === null) {
                    return null;
                }
                if (isNaN(this.inputDestination.longitude)) {
                    this.longitudeErrorMessage = "Longitude: '" + this.inputDestination.longitude + "' is not a number!";
                    return false;
                } else if (this.inputDestination.longitude > 180 || this.inputDestination.longitude < -180) {
                    this.longitudeErrorMessage = "Longitude: '" + this.inputDestination.longitude + "' must be between " +
                        "-180 and 180";
                    return false;
                }
                return true;
            },
            destinationCountryValidation() {
                if (this.inputDestination.country.length === 0) {
                    return null;
                }
                let countryRegex = /\d/;
                return !countryRegex.test(this.inputDestination.country);
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

                    if (this.inputDestination.id === null) {
                        this.addDestination();
                    } else {
                        this.validateEdit();
                    }
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
                this.inputDestination.name = "";
                this.inputDestination.type = "";
                this.inputDestination.district = "";
                this.inputDestination.latitude = "";
                this.inputDestination.longitude = "";
                this.inputDestination.country = "";
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
                fetch(`/v1/destinations/` + this.profile.id, {
                    method: 'POST',
                    headers: {'content-type': 'application/json'},
                    body: (JSON.stringify({
                        "name": this.inputDestination.name,
                        "type_id": this.inputDestination.type.id,
                        "district": this.inputDestination.district,
                        "latitude": parseFloat(this.inputDestination.latitude),
                        "longitude": parseFloat(this.inputDestination.longitude),
                        "country": this.inputDestination.country
                    }))
                })
                    .then(this.parseJSON)
                    .then(cb)

                    .then(function(response) {
                        if (response.ok) {
                            self.resetDestForm();
                            self.showAlert();
                            self.emit('data-changed');
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
             * Checks whether the destination being edited is present in any trips
             *
             * @param cb.
             */
            validateEdit(cb) {
                let self = this;
                //TODO: Update this request to match route
                fetch(`/v1/destinationCheck/` + 88, {
                    accept: "application/json"
                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then(cb)
                    .then(destinationConflicts => this.destinationConflicts = destinationConflicts)
                    .then(function(response) {
                        if (self.destinationConflicts.length > 0) {
                            self.displayConfirmation();
                        } else {
                            self.heading = "FUCK";
                        }

                        self.emit('data-changed');
                        return JSON.parse(JSON.stringify(response));

                    });
            },

            displayConfirmation() {
                this.$refs["confirmEditModal"].show();
            },

            editDestination() {
                let self = this;
                fetch(`/v1/destinations/` + this.inputDestination.id, {
                    method: 'PATCH',
                    headers: {'content-type': 'application/json'},
                    body: (JSON.stringify({
                        "name": this.inputDestination.name,
                        "type_id": this.inputDestination.type.id,
                        "district": this.inputDestination.district,
                        "latitude": parseFloat(this.inputDestination.latitude),
                        "longitude": parseFloat(this.inputDestination.longitude),
                        "country": this.inputDestination.country
                    }))
                })
                    .then(this.parseJSON)
                    .then(cb)

                    .then(function(response) {
                        if (response.ok) {
                            self.resetDestForm();
                            self.showAlert();
                            self.emit('data-changed');
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

            /**
             * Used to dismiss the edit a destination modal.
             *
             * @param modal, the modal that is wanting to be dismissed.
             */
            dismissModal(modal) {
                this.$refs[modal].hide();
            },

            /**
             * Used to check the response of a fetch method. If there is an error code, the code is printed to the
             * console.
             *
             * @param response, passed back to the getAllTrips function to be parsed into a Json.
             * @returns throws the error.
             */
            checkStatus(response) {
                if (response.status >= 200 && response.status < 300) {
                    return response;
                }
                const error = new Error(`HTTP Error ${response.statusText}`);
                error.status = response.statusText;
                error.response = response;
                console.log(error);

                self.errorMessage = "";
                self.showError = true;
                response.clone().text().then(text => {
                    self.errorMessage = text;
                });

                throw error;
            },

            parseJSON(response) {
                return response.json();
            },
        }
    }
</script>