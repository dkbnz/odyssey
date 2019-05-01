<template>
    <div class="container">
        <h1 class="page_title">Search Destinations</h1>
        <p class="page_title"><i>Search for a destination using the form below</i></p>
        <b-alert v-model="showError" variant="danger" dismissible>{{errorMessage}}</b-alert>
        <div>
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
                <b-form-select id="type" v-model="searchType" trim>
                    <template slot="first">
                        <option :value="null" >-- Any --</option>
                    </template>
                    <option v-for="destination in destinationTypes" :value="destination.id">{{destination.destinationType}}</option>
                </b-form-select>
            </b-form-group>

            <b-form-group
                    id="district-field"
                    label="District:"
                    label-for="district">
                <b-form-input id="district" v-model="searchDistrict" trim></b-form-input>
            </b-form-group>

            <b-form-group
                    id="latitude-field"
                    label="Latitude:"
                    label-for="latitude">
                <b-form-input id="latitude" v-model="searchLat" trim></b-form-input>
            </b-form-group>

            <b-form-group
                    id="longitude-field"
                    label="Longitude:"
                    label-for="longitude">
                <b-form-input id="longitude" v-model="searchLong" trim></b-form-input>
            </b-form-group>

            <b-form-group
                    id="country-field"
                    label="Country:"
                    label-for="country">
                <b-form-input id="country" v-model="searchCountry" trim></b-form-input>
            </b-form-group>

            <b-button block variant="primary" @click="searchDestinations">Search</b-button>
        </div>
        <div style="margin-top: 40px">
            <b-table hover striped outlined
                     id="myFutureTrips"
                     :items="destinations"
                     :fields="fields"
                     :per-page="perPage"
                     :current-page="currentPage"
                     :sort-by.sync="sortBy"
                     :sort-desc.sync="sortDesc"
                     :busy="destinations.length === 0"
            >
                <div slot="table-busy" class="text-center my-2">
                    <b-spinner v-if="retrievingDestinations" class="align-middle"></b-spinner>
                    <strong>Can't find any destinations!</strong>
                </div>
            </b-table>
            <b-row>
                <b-col cols="1">
                    <b-form-group
                            id="profiles-field"
                            label-for="perPage">
                        <b-form-select id="perPage" v-model="perPage" :options="optionViews" size="sm" trim> </b-form-select>
                    </b-form-group>
                </b-col>
                <b-col cols="8">
                    <b-pagination
                            v-model="currentPage"
                            :total-rows="rows"
                            :per-page="perPage"
                            aria-controls="my-table"
                            first-text="First"
                            last-text="Last"
                            align="center"
                            size="sm"
                    ></b-pagination>
                </b-col>
            </b-row>
        </div>
    </div>
</template>

<script>
    export default {
        name: "searchDestinations",
        props: {
            'destinationTypes': Array
        },
        data () {
            return {
                sortBy: 'name',
                sortDesc: false,
                searchName :"",
                destinations: [],
                searchType: "",
                searchDistrict: "",
                searchLat: "",
                searchLong: "",
                searchCountry: "",
                showError: false,
                optionViews: [{value:1, text:"1"}, {value:5, text:"5"}, {value:10, text:"10"}, {value:15, text:"15"}],
                perPage: 10,
                currentPage: 1,
                fields: [{key:'name', value:'name', sortable: true}, {key:'type.destinationType', label:'Type', sortable: true}, {key:'district', value:'district', sortable: true}, 'latitude', 'longitude', {key:'country', value:'country', sortable: true}],
                searchDestination: "",
                errorMessage: "",
                retrievingDestinations: false
            }
        },
        computed: {
            rows() {
                return this.destinations.length
            }

        },
        mounted () {
            this.queryDestinations();
        },
        methods: {
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
            queryDestinations () {
                this.retrievingDestinations = true;
                let searchQuery = "?name=" + this.searchName + "&type_id=" + this.searchType + "&district=" + this.searchDistrict + "&latitude=" + this.searchLat + "&longitude=" + this.searchLong + "&country=" + this.searchCountry;
                return fetch(`/v1/destinations` + searchQuery,  {
                    dataType: 'html',
                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then((data) => {
                        this.destinations = data;
                        this.retrievingDestinations = false;
                    })
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
        }
    }
</script>

<style scoped>

</style>