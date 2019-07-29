<template>
    <div>
        <h4 class="page-title">{{heading}} a Destination</h4>
        <b-alert dismissible v-model="showError" variant="danger">{{errorMessage}}</b-alert>

        <!--Displays a progress bar alert on submission which ticks down time to act
        as a buffer for destination being added-->
        <b-alert
                :show="dismissCountDown"
                @dismiss-count-down="countDownChanged"
                @dismissed="dismissCountDown=0"
                dismissible
                variant="success">
            <p>Destination Successfully {{heading}}ed</p>
            <b-progress
                    :max="dismissSecs"
                    :value="dismissCountDown"
                    height="4px"
                    variant="success"
            ></b-progress>
        </b-alert>

        <b-modal id="confirmEditModal" ref="confirmEditModal" size="l" title="Confirm Edit">
            <div>
                Are you sure you want to edit this destination?
                <div v-if="destinationConflicts.matching_trips !== undefined
                        && destinationConflicts.matching_trips.length > 0">
                    <p v-if="destinationConflicts.trip_count === 1">
                        This would affect the following {{destinationConflicts.trip_count}} trip:
                    </p>
                    <p v-else>
                        This would affect the following {{destinationConflicts.trip_count}} trips:
                    </p>
                    <b-list-group
                            style="overflow-y: scroll; height: 30vh;">
                        <b-list-group-item class="flex-column align-items-start"
                                           v-for="trip in destinationConflicts.matching_trips" :key="trip.id">
                            <div class="d-flex w-100 justify-content-between">
                                <h5 class="mb-1">Name: {{trip.name}}</h5>
                            </div>
                            <div class="d-flex w-100 justify-content-between">
                                <h5 class="mb-1">Created by: {{trip.profile.firstName}} {{trip.profile.lastName}}</h5>
                            </div>
                        </b-list-group-item>
                    </b-list-group>
                </div>
                <div v-if="destinationConflicts.matching_destinations !== undefined
                        && destinationConflicts.matching_destinations.length > 0">
                    <p v-if="destinationConflicts.matching_destinations.length === 1">
                        This will merge the following 1 private destination:
                    </p>
                    <p v-else>
                        This will merge the following {{destinationConflicts.matching_destinations.length}} private destinations:
                    </p>
                    <b-list-group
                            style="overflow-y: scroll; height: 30vh;">
                        <b-list-group-item class="flex-column align-items-start"
                                           v-for="destination in destinationConflicts.matching_destinations"
                                           :key="destination.id">
                            <div class="d-flex w-100 justify-content-between">
                                <h5 class="mb-1">Name: {{destination.name}}</h5>
                            </div>
                            <div class="d-flex w-100 justify-content-between">
                                <h5 class="mb-1">Created by: {{destination.owner.firstName}} {{destination.owner.firstName}}</h5>
                            </div>
                        </b-list-group-item>
                    </b-list-group>
                </div>
            </div>
            <template slot="modal-footer">
                <b-col>
                    <b-button @click="dismissModal('confirmEditModal')"
                              class="mr-2 float-right"
                              block
                              variant="danger">Cancel
                    </b-button>
                </b-col>
                <b-col>
                    <b-button @click="editDestination" class="mr-2 float-right" block variant="success">
                        Confirm
                    </b-button>
                </b-col>
            </template>
        </b-modal>

        <!--Form for adding a destination-->
        <div>
            <b-form>
                <b-form-group
                        id="name-field"
                        label="Destination Name:"
                        label-for="name">
                    <b-form-input id="name" @click="showError = false" v-model="inputDestination.name" type="text"
                                  required
                                  :state="destinationNameValidation">
                    </b-form-input>
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
                    <b-form-input id="district" @click="showError = false" v-model="inputDestination.district"
                                  type="text" trim required :state="destinationDistrictValidation">
                    </b-form-input>
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
                <b-form-checkbox
                        v-if="inputDestination.id !== null"
                        switch
                        v-model="inputDestination.public"
                        required>
                    {{isPublic}} Destination
                </b-form-checkbox>

                <b-button :disabled="!validateFields()" @click="checkDestinationFields" block variant="primary">Save</b-button>
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
                            destinationType: ""
                        },
                        district: "",
                        latitude: null,
                        longitude: null,
                        country: "",
                        public: false
                    }
                }
            }
        },

        data() {
            return {
                showError: false,
                errorMessage: "",
                dismissSecs: 3,
                dismissCountDown: 0,
                latitudeErrorMessage: "",
                longitudeErrorMessage: "",
                destinationConflicts: []
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
                if (this.inputDestination.latitude === null || this.inputDestination.latitude.length === 0) {
                    return null;
                } else if (isNaN(this.inputDestination.latitude)) {
                    this.latitudeErrorMessage = "Latitude: '" + this.inputDestination.latitude + "' is not a number!";
                    return false;
                } else if (this.inputDestination.latitude > 90 || this.inputDestination.latitude < -90) {
                    this.latitudeErrorMessage = "Latitude: '" + this.inputDestination.latitude + "' must be between " +
                        "-90 and 90";
                    return false;
                } else {
                    return true;
                }

            },
            destinationLongitudeValidation() {
                if (this.inputDestination.longitude === null || this.inputDestination.longitude.length === 0) {
                    return null;
                } else if (isNaN(this.inputDestination.longitude)) {
                    this.longitudeErrorMessage = "Longitude: '" + this.inputDestination.longitude + "' is not a number!";
                    return false;
                } else if (this.inputDestination.longitude > 180 || this.inputDestination.longitude < -180) {
                    this.longitudeErrorMessage = "Longitude: '" + this.inputDestination.longitude + "' must be between " +
                        "-180 and 180";
                    return false;
                } else {
                    return true;
                }
            },
            destinationCountryValidation() {
                if (this.inputDestination.country.length === 0) {
                    return null;
                }
                let countryRegex = /\d/;
                return !countryRegex.test(this.inputDestination.country);
            },

            isPublic() {
                // Tells users editing a destination whether they've made the destination public or private.
                if (this.inputDestination.public) {
                    return "Public";
                }
                return "Private";
            }
        },

        methods: {
            /**
             * Checks all of the input fields for valid input
             */
            validateFields() {
                return this.destinationNameValidation && this.destinationTypeValidation
                    && this.destinationDistrictValidation && this.destinationLatitudeValidation
                    && this.destinationLongitudeValidation && this.destinationCountryValidation
            },


            /**
             * Checks that all fields are present and runs validation.
             * On fail shows errors.
             */
            checkDestinationFields() {
                if(this.validateFields()) {
                    this.showError = false;

                    if (this.inputDestination.id === null) {
                        this.addDestination();
                    } else {
                        this.validateEdit();
                    }
                    return true;
                }
                else {
                    this.errorMessage = ("Please enter in all fields!");
                    this.showError = true;
                    return false;
                }
            },


            /**
             * Sets all fields to blank.
             */
            resetDestForm() {
                this.inputDestination.name = "";
                this.inputDestination.type = {
                    id: null,
                    destinationType: "",
                };
                this.inputDestination.district = "";
                this.inputDestination.latitude = null;
                this.inputDestination.longitude = null;
                this.inputDestination.country = "";
                this.inputDestination.public = false;
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
                        "country": this.inputDestination.country,
                        "is_public": this.inputDestination.public
                    }))
                })
                    .then(this.checkStatus)
                    .then(function(response) {
                        self.resetDestForm();
                        self.showAlert();
                        self.$emit('data-changed');
                        return JSON.parse(JSON.stringify(response));
                    });
            },


            /**
             * Checks whether the destination being edited is present in any trips. This is to display a confirmation
             * message to the user.
             *
             */
            validateEdit() {
                let self = this;
                fetch(`/v1/destinationCheck/` + this.inputDestination.id, {
                    accept: "application/json"
                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then(destinationConflicts => this.destinationConflicts = destinationConflicts)
                    .then(function(response) {
                        self.displayConfirmation();
                    });
            },


            /**
             * Displays the confirmation edit modal.
             */
            displayConfirmation() {
                this.$refs["confirmEditModal"].show();
            },


            /**
             * Sends the edited destination to the Http endpoint to be saved.
             */
            editDestination() {
                let self = this;
                let jsonBody = JSON.stringify(this.inputDestination);

                fetch(`/v1/destinations/` + this.inputDestination.id, {
                    method: 'PUT',
                    headers: {'content-type': 'application/json'},
                    body: jsonBody
                }).then(function(response) {
                        if (response.ok) {
                            self.showAlert();
                            self.dismissModal('confirmEditModal');
                            self.$emit('destination-saved', self.inputDestination);
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

                this.errorMessage = "";
                response.clone().text().then(text => {
                    this.errorMessage = text;
                    this.showError = true;
                });
                throw error;
            },


            /**
             * Converts the retrieved Http response to a Json format.
             *
             * @param response the Http response.
             * @returns the Http response body as Json.
             */
            parseJSON(response) {
                return response.json();
            },
        }
    }
</script>