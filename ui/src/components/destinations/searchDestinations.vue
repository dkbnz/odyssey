<template>
    <div class="containerWithNav">
        <h1 class="page_title">Search Destinations</h1>
        <p class="page_title"><i>Search for a destination using any of the fields in the the form below</i></p>
        <b-alert dismissible v-model="showError" variant="danger">{{errorMessage}}</b-alert>
        <div>
            <!--Input fields for searching for destinations-->
            <b-form-group
                    id="name-field"
                    label="Destination Name:"
                    label-for="name">
                <b-form-input id="name" v-model="searchName"></b-form-input>
            </b-form-group>

            <b-form-group
                    id="type-field"
                    label="Destination Type:"
                    label-for="type">
                <!--Dropdown field for destination types-->
                <b-form-select id="type" trim v-model="searchType">
                    <template slot="first">
                        <option :value="null">-- Any --</option>
                    </template>
                    <option :value="destination.id" v-for="destination in destinationTypes">
                        {{destination.destinationType}}
                    </option>
                </b-form-select>
            </b-form-group>

            <b-form-group
                    id="district-field"
                    label="District:"
                    label-for="district">
                <b-form-input id="district" trim v-model="searchDistrict"></b-form-input>
            </b-form-group>

            <b-form-group
                    id="latitude-field"
                    label="Latitude:"
                    label-for="latitude">
                <b-form-input id="latitude" trim v-model="searchLat"></b-form-input>
            </b-form-group>

            <b-form-group
                    id="longitude-field"
                    label="Longitude:"
                    label-for="longitude">
                <b-form-input id="longitude" trim v-model="searchLong"></b-form-input>
            </b-form-group>

            <b-form-group
                    id="country-field"
                    label="Country:"
                    label-for="country">
                <b-form-input id="country" trim v-model="searchCountry"></b-form-input>
            </b-form-group>

            <b-button @click="searchDestinations" block variant="primary">Search</b-button>
        </div>

        <!--Table for displaying search results-->
        <div style="margin-top: 40px">
            <b-table :busy="destinations.length === 0" :current-page="currentPage" :fields="fields"
                     :items="destinations"
                     :per-page="perPage"
                     :sort-by.sync="sortBy"
                     :sort-desc.sync="sortDesc"
                     hover
                     id="myFutureTrips"
                     outlined
                     striped>
                <div class="text-center my-2" slot="table-busy">
                    <b-spinner class="align-middle" v-if="retrievingDestinations"></b-spinner>
                    <strong>Can't find any destinations!</strong>
                </div>
            </b-table>

            <!--Settings for pagination & number of results displayed per page-->
            <b-row>
                <b-col cols="1">
                    <b-form-group
                            id="profiles-field"
                            label-for="perPage">
                        <b-form-select :options="optionViews" id="perPage" size="sm" trim
                                       v-model="perPage"></b-form-select>
                    </b-form-group>
                </b-col>
                <b-col cols="8">
                    <b-pagination
                            :per-page="perPage"
                            :total-rows="rows"
                            align="center"
                            aria-controls="my-table"
                            first-text="First"
                            last-text="Last"
                            size="sm"
                            v-model="currentPage"
                    ></b-pagination>
                </b-col>
            </b-row>
        </div>

    </div>
</template>

<script>
    export default {
        name: "searchDestinations",
        props: ['destinationTypes'],
        data() {
            return {
                sortBy: 'name',
                sortDesc: false,
                searchName: "",
                destinations: [],
                searchType: "",
                searchDistrict: "",
                searchLat: "",
                searchLong: "",
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
                retrievingDestinations: false
            }
        },
        computed: {
            /**
             * @returns {number} number of rows to be displayed based on number of destinations present
             */
            rows() {
                return this.destinations.length
            }

        },
        mounted() {
            this.queryDestinations();
        },
        methods: {
            /**
             * Checks that latitude and longitude values are numbers.
             * @returns {boolean} true if fields are valid.
             */
            checkLatLong() {
                let ok = true;
                if (isNaN(this.dLatitude)) {
                    this.showError = true;
                    this.errorMessage = ("'" + this.dLatitude + "' is not a number!");
                    ok = false;
                } else if (isNaN(this.dLongitude)) {
                    this.showError = true;
                    this.errorMessage = ("'" + this.dLongitude + "' is not a number!");
                    ok = false;
                }
                return ok;
            },

            /**
             * Sets values for search.
             */
            searchDestinations() {
                if (this.checkLatLong) {
                    this.searchDestination = {
                        name: this.searchName,
                        type: this.searchType,
                        district: this.searchDistrict,
                        country: this.searchCountry
                    };
                    this.queryDestinations();
                }

            },

            /**
             * Runs a query which searches through the destinations in the database and returns all which
             * follow the search criteria.
             * @returns {Promise<Response | never>}
             */
            queryDestinations() {
                this.retrievingDestinations = true;
                let searchQuery =
                    "?name=" + this.searchName +
                    "&type_id=" + this.searchType +
                    "&district=" + this.searchDistrict +
                    "&latitude=" + this.searchLat +
                    "&longitude=" + this.searchLong +
                    "&country=" + this.searchCountry;
                return fetch(`/v1/destinations` + searchQuery, {
                    dataType: 'html',
                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then((data) => {
                        this.destinations = data;
                        this.retrievingDestinations = false;
                    })
            },

            /**
             * Displays an error if search failed.
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
            },
        }
    }
</script>