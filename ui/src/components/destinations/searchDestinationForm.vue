<template>
    <div>
        <h4 class="page-title" v-if="searchPublic">Search Public Destinations</h4>
        <h4 class="page-title" v-else>Search Your Destinations</h4>
        <b-alert dismissible v-model="showError" variant="danger">{{errorMessage}}</b-alert>
        <div>
            <!--Input fields for searching for destinations-->
            <b-form-group
                    id="name-field"
                    label="Destination Name:"
                    label-for="name">
                <b-form-input id="name" v-model="searchName" :state="destinationNameValidation"></b-form-input>
            </b-form-group>

            <b-form-group
                    id="type-field"
                    label="Destination Type:"
                    label-for="type">
                <!--Dropdown field for destination types-->
                <b-form-select id="type" trim v-model="searchType">
                    <template slot="first">
                        <option value="">-- Any --</option>
                    </template>
                    <option :value="destination.id" v-for="destination in destinationTypes"
                            :state="destinationTypeValidation">
                        {{destination.destinationType}}
                    </option>
                </b-form-select>
            </b-form-group>

            <b-form-group
                    id="district-field"
                    label="District:"
                    label-for="district">
                <b-form-input id="district" trim v-model="searchDistrict" :state="destinationDistrictValidation">
                </b-form-input>
            </b-form-group>

            <b-form-group
                    id="latitude-field"
                    label="Latitude:"
                    label-for="latitude">
                <b-form-input id="latitude" trim v-model="searchLatitude" :state="destinationLatitudeValidation">
                </b-form-input>
                <b-form-invalid-feedback :state="destinationLatitudeValidation">
                    {{latitudeErrorMessage}}
                </b-form-invalid-feedback>

            </b-form-group>

            <b-form-group
                    id="longitude-field"
                    label="Longitude:"
                    label-for="longitude">
                <b-form-input id="longitude" trim v-model="searchLongitude" :state="destinationLongitudeValidation">
                </b-form-input>
                <b-form-invalid-feedback :state="destinationLongitudeValidation">
                    {{longitudeErrorMessage}}
                </b-form-invalid-feedback>

            </b-form-group>

            <b-form-group
                    id="country-field"
                    label="Country:"
                    label-for="country">
                <b-form-input id="country" trim v-model="searchCountry" :state="destinationCountryValidation">
                </b-form-input>
                <b-form-invalid-feedback :state="destinationCountryValidation">
                    Country cannot have any numbers in it!
                </b-form-invalid-feedback>
            </b-form-group>

            <b-button @click="searchDestinations" block variant="primary">Search</b-button>
        </div>
    </div>
</template>

<script>
    import SingleDestination from "../destinations/singleDestination";

    export default {
        name: "searchDestinationForm.vue",
        props: {
            searchPublic: Boolean,
            destinationTypes: Array,
            profile: Object,
            userProfile: {
                default: function () {
                    return this.profile
                }
            },
        },
        data() {
            return {
                sortBy: 'name',
                sortDesc: false,
                searchName: "",
                destinations: [],
                searchType: "",
                searchDistrict: "",
                searchLatitude: "",
                searchLongitude: "",
                searchCountry: "",
                showError: false,
                optionViews: [
                    {value: 1, text: "1"},
                    {value: 5, text: "5"},
                    {value: 10, text: "10"},
                    {value: 15, text: "15"}
                ],
                perPage: 10,
                currentPage: 1,
                fields: [
                    {key: 'name', value: 'name', sortable: true},
                    {key: 'type.destinationType', label: 'Type', sortable: true},
                    {key: 'district', value: 'district', sortable: true},
                    'latitude',
                    'longitude',
                    {key: 'country', value: 'country', sortable: true}
                ],
                searchDestination: "",
                errorMessage: "",
                retrievingDestinations: false,
                longitudeErrorMessage: "",
                latitudeErrorMessage: ""
            }
        },
        computed: {
            /**
             * @returns {number} number of rows to be displayed based on number of destinations present.
             */
            rows() {
                return this.destinations.length
            },
            /**
             * Validates the input fields based on regex.
             *
             * @returns {*} true if input is valid.
             */
            destinationNameValidation() {
                if (this.searchName.length === 0) {
                    return null;
                }
                return this.searchName.length > 0;
            },
            destinationTypeValidation() {
                if (this.searchType === null) {
                    return null;
                }
                return this.searchType.length > 0 || this.searchType !== null;
            },
            destinationDistrictValidation() {
                if (this.searchDistrict.length === 0) {
                    return null;
                }
                return this.searchDistrict.length > 0;
            },
            destinationLatitudeValidation() {
                if (this.searchLatitude.length === 0) {
                    return null;
                }
                if (isNaN(this.searchLatitude)) {
                    this.latitudeErrorMessage = "Latitude: '" + this.searchLatitude + "' is not a number!";
                    return false;
                } else if (this.searchLatitude > 90 || this.searchLatitude < -90) {
                    this.latitudeErrorMessage = "Latitude: '" + this.searchLatitude + "' must be between " +
                        "-90 and 90";
                    return false;
                }
                return true;
            },
            destinationLongitudeValidation() {
                if (this.searchLongitude.length === 0) {
                    return null;
                }
                if (isNaN(this.searchLongitude)) {
                    this.longitudeErrorMessage = "Longitude: '" + this.searchLongitude + "' is not a number!";
                    return false;
                } else if (this.searchLongitude > 180 || this.searchLongitude < -180) {
                    this.longitudeErrorMessage = "Longitude: '" + this.searchLongitude + "' must be between " +
                        "-180 and 180";
                    return false;
                }
                return true;
            },
            destinationCountryValidation() {
                if (this.searchCountry.length === 0) {
                    return null;
                }
                let countryRegex = /\d/;
                return !countryRegex.test(this.searchCountry);
            }
        },
        methods: {
            /**
             * Sets values for search.
             */
            searchDestinations() {
                if (this.validateFields(this.destinationNameValidation)
                    && this.validateFields(this.destinationTypeValidation)
                    && this.validateFields(this.destinationDistrictValidation)
                    && this.validateFields(this.destinationLatitudeValidation)
                    && this.validateFields(this.destinationLongitudeValidation)
                    && this.validateFields(this.destinationCountryValidation)) {
                    this.$emit('searched-destination', {
                        name: this.searchName,
                        type: this.searchType,
                        district: this.searchDistrict,
                        latitude: this.searchLatitude,
                        longitude: this.searchLongitude,
                        country: this.searchCountry
                    });
                    //this.queryDestinations();
                }

            },

            /**
             * Checks each of the validation fields to ensure they are return either null (no value is given), or the
             * field is valid.
             *
             * @returns {boolean} true if the fields are valid.
             */
            validateFields(validationField) {
                if (validationField === null || validationField === true) {
                    return true;
                }
            },

            /**
             * Runs a query which searches through the destinations in the database and returns all which
             * follow the search criteria.
             *
             * @returns {Promise<Response | never>}
             */
            queryDestinations() {
                this.retrievingDestinations = true;
                let searchTypeLocal = this.searchType;
                if (searchTypeLocal === "Any") {
                    searchTypeLocal = "";
                }
                let searchQuery =
                    "?name=" + this.searchName +
                    "&type_id=" + this.searchType +
                    "&district=" + this.searchDistrict +
                    "&latitude=" + this.searchLatitude +
                    "&longitude=" + this.searchLongitude +
                    "&country=" + this.searchCountry;

                return fetch(`/v1/destinations` + searchQuery, {
                    dataType: 'html'
                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then((data) => {
                        this.$emit('searched-destinations', data);
                        this.retrievingDestinations = false;
                    })
            },

            /**
             * Displays an error if search failed.
             *
             * @param response from database search query.
             * @throws the error if it occurs.
             */
            checkStatus(response) {
                if (response.status >= 200 && response.status < 300) {
                    return response;
                }
                const error = new Error(`HTTP Error ${response.statusText}`);
                error.status = response.statusText;
                error.response = response;
                console.log(error);
                throw error;
            },

            parseJSON(response) {
                return response.json();
            }
        },

        components: {
            SingleDestination
        }
    }
</script>