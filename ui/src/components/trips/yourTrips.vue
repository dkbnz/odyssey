<template>
    <div :class="containerClass">

        <!-- Div for all the user's future trips -->
        <div id="upcomingTrips">
            <h1 class="page_title">Upcoming Trips</h1>
            <p class="page_title"><i>Here are your upcoming trips!</i></p>
            <b-alert dismissible v-model="showError" variant="danger">{{errorMessage}}</b-alert>
            <b-alert dismissible v-model="validDelete" variant="success">Trip Deleted</b-alert>

            <!-- Confirmation modal for deleting a trip. -->
            <b-modal hide-footer id="deleteModal" ref="deleteModal" title="Delete Trip">
                <div class="d-block">
                    Are you sure that you want to delete "{{selectedTrip.name}}"?
                </div>
                <b-button
                        @click="dismissModal('deleteModal')
                         deleteTrip(selectedTrip);"
                        class="mr-2 float-right"
                        variant="danger">Delete
                </b-button>
                <b-button
                        @click="dismissModal('deleteModal')"
                        class="mr-2 float-right">Cancel
                </b-button>
            </b-modal>

            <!-- Modal that uses the plan a trip page to edit a selected trip -->
            <b-modal hide-footer id="editTripModal" ref="editTripModal" size="xl" title="Edit Trip">
                <plan-a-trip
                        :destinations="destinations"
                        :heading="'Edit a Trip'"
                        :inputTrip="selectedTrip"
                        :profile="profile"
                        :subHeading="'Edit your trip using the form below'"
                        v-if="selectedTrip !== ''"
                        v-on:tripSaved="getAllTrips()">
                </plan-a-trip>
                <b-button @click="dismissModal('editTripModal')" class="mr-2 float-right">Cancel</b-button>
            </b-modal>

            <!-- Table to show all a profile's future trips -->
            <b-table :busy="futureTrips.length === 0" :current-page="currentPageUpcoming" :fields="fields"
                     :items="futureTrips"
                     :per-page="perPageUpcoming"
                     :sort-by.sync="sortBy"
                     :sort-desc.sync="sortDesc"
                     hover
                     id="myFutureTrips"
                     outlined
                     ref="myFutureTrips"
                     striped
            >
                <div class="text-center my-2" slot="table-busy">
                    <b-spinner class="align-middle" v-if="retrievingTrips"></b-spinner>
                    <strong>Can't find any trips!</strong>
                </div>
                <template slot="more_details" slot-scope="row">
                    <b-button @click="row.toggleDetails" class="mr-2" size="sm">
                        {{ row.detailsShowing ? 'Hide' : 'Show'}} Details
                    </b-button>
                    <b-button @click="sendTripToModal(row.item)"
                              class="mr-2"
                              size="sm"
                              v-b-modal.editTripModal
                              v-if="hasPermission"
                              v-model="editButton"
                              variant="primary">Edit
                    </b-button>
                    <b-button @click="sendTripToModal(row.item)"
                              class="mr-2"
                              size="sm"
                              v-b-modal.deleteModal
                              v-if="hasPermission"
                              v-model="deleteButton"
                              variant="danger">Delete
                    </b-button>
                </template>

                <!-- Shows all the trip destinations in a separate nested table -->
                <template slot="row-details" slot-scope="row">
                    <b-card>
                        <b-table
                                :fields="subFields"
                                :items="row.item.destinations"
                                id="futureTripsDestinations"
                                ref="destinationsTable">
                            <template slot="destInDate" slot-scope="data" v-if="futureTrips.length > 0">
                                {{formatDate(data.item.startDate)}}
                            </template>
                            <template slot="destOutDate" slot-scope="data" v-if="futureTrips.length > 0">
                                {{formatDate(data.item.endDate)}}
                            </template>

                        </b-table>

                    </b-card>
                </template>

                <template slot="tripStartDate" slot-scope="data"
                          v-if="data.item.destinations[0].startDate">
                    {{formatDate(data.item.destinations[0].startDate)}}
                </template>

                <template slot="tripEndDate"
                          slot-scope="data" v-if="data.item.destinations[data.item.destinations.length -1].endDate">
                    {{formatDate(data.item.destinations[data.item.destinations.length -1].endDate)}}
                </template>

                <template slot="tripEndDest" slot-scope="data" v-if="futureTrips.length > 0">
                    {{data.item.destinations[data.item.destinations.length -1].destination.name}}
                </template>

                <template slot="duration" slot-scope="data" v-if="futureTrips.length > 0">
                    {{calculateDuration(data.item.destinations)}}
                </template>

            </b-table>

            <!-- Determines pagination and number of results per row of the table -->
            <b-row>
                <b-col cols="1">
                    <b-form-group
                            id="numUpcomingItems-field"
                            label-for="perPageUpcoming">
                        <b-form-select :options="optionViews" id="perPageUpcoming" size="sm"
                                       trim v-model="perPageUpcoming"></b-form-select>
                    </b-form-group>
                </b-col>
                <b-col cols="8">
                    <b-pagination
                            :per-page="perPageUpcoming"
                            :total-rows="rowsUpcoming"
                            align="center"
                            aria-controls="my-table"
                            first-text="First"
                            last-text="Last"
                            size="sm"
                            v-model="currentPageUpcoming"
                    ></b-pagination>
                </b-col>
            </b-row>
        </div>

        <!-- Div for all the user's past trips -->
        <div id="pastTrips">
            <h1 class="page_title">Past Trips</h1>
            <p class="page_title"><i>Here are your past trips!</i></p>

            <b-container fluid>
                <!-- Table to show all a profile's past trips -->
                <b-table :busy="pastTrips.length === 0" :current-page="currentPagePast" :fields="fields"
                         :items="pastTrips"
                         :per-page="perPagePast"
                         :sort-by="sortBy"
                         :sort-desc="sortDescPast"
                         hover
                         id="myPastTrips"
                         outlined
                         ref="myPastTrips"
                         striped>

                    <div class="text-center my-2" slot="table-busy">
                        <b-spinner class="align-middle" v-if="retrievingTrips"></b-spinner>
                        <strong>Can't find any trips!</strong>
                    </div>
                    <template slot="more_details" slot-scope="row">
                        <b-button @click="row.toggleDetails" class="mr-2" size="sm">
                            {{ row.detailsShowing ? 'Hide' : 'Show'}} Details
                        </b-button>
                        <b-button @click="sendTripToModal(row.item)" class="mr-2" size="sm"
                                  v-b-modal.editTripModal
                                  v-if="hasPermission" v-model="editButton" variant="primary">Edit
                        </b-button>
                        <b-button @click="sendTripToModal(row.item)" class="mr-2" size="sm"
                                  v-b-modal.deleteModal
                                  v-if="hasPermission" v-model="deleteButton" variant="danger">Delete
                        </b-button>
                    </template>

                    <!-- Shows all the trip destinations in a separate nested table -->
                    <template slot="row-details" slot-scope="row">
                        <b-card>
                            <b-table
                                    :fields="subFields"
                                    :items="row.item.destinations"
                                    id="pastTripsDestinations"
                                    ref="pastDestinationsTable">
                                <template slot="destInDate" slot-scope="data" v-if="pastTrips.length > 0">
                                    {{formatDate(data.item.startDate)}}
                                </template>
                                <template slot="destOutDate" slot-scope="data" v-if="pastTrips.length > 0">
                                    {{formatDate(data.item.endDate)}}
                                </template>
                            </b-table>
                        </b-card>
                    </template>

                    <template slot="tripStartDate" slot-scope="data"
                              v-if="data.item.destinations[0].startDate">
                        {{formatDate(data.item.destinations[0].startDate)}}
                    </template>

                    <template slot="tripEndDate" slot-scope="data"
                              v-if="data.item.destinations[data.item.destinations.length -1].endDate">
                        {{formatDate(data.item.destinations[data.item.destinations.length -1].endDate)}}
                    </template>

                    <template slot="tripEndDest" slot-scope="data" v-if="pastTrips.length > 0">
                        {{data.item.destinations[data.item.destinations.length -1].destination.name}}
                    </template>

                    <template slot="duration" slot-scope="data" v-if="pastTrips.length > 0">
                        {{calculateDuration(data.item.destinations)}}
                    </template>

                </b-table>

                <!-- Determines pagination and number of results per row of the table -->
                <b-row>
                    <b-col cols="1">
                        <b-form-group
                                id="numItemsPast-field"
                                label-for="perPagePast">
                            <b-form-select :options="optionViews" id="perPage" size="sm" trim v-model="perPagePast">
                            </b-form-select>
                        </b-form-group>
                    </b-col>
                    <b-col cols="8">
                        <b-pagination
                                :per-page="perPagePast"
                                :total-rows="rowsPast"
                                align="center"
                                aria-controls="my-table"
                                first-text="First"
                                last-text="Last"
                                size="sm"
                                v-model="currentPagePast"
                        ></b-pagination>
                    </b-col>
                </b-row>
            </b-container>

        </div>
    </div>
</template>

<script>
    import PlanATrip from './planATrip.vue'

    export default {
        name: "YourTrips",
        props: {
            profile: Object,
            userProfile: {
                default: function () {
                    return this.profile
                }
            },
            destinations: Array,
            adminView: Boolean,
            containerClass: {
                default: function() {
                    return 'containerWithNav';
                }
            }
        },
        components: {
            PlanATrip
        },
        data: function () {
            return {
                optionViews: [{value: 1, text: "1"}, {value: 5, text: "5"}, {value: 10, text: "10"}, {
                    value: 15,
                    text: "15"
                }],
                perPageUpcoming: 10,
                perPagePast: 10,
                sortBy: 'destinations[0].startDate',
                sortDesc: false,
                sortDescPast: true,
                currentPageUpcoming: 1,
                currentPagePast: 1,
                fields: [
                    'name',
                    {key: 'tripStartDate', label: 'Start Date'},
                    {key: 'destinations[0].destination.name', label: 'Start Destination'},
                    {key: 'tripEndDate', label: 'End Date'},
                    {key: 'tripEndDest', label: 'End Destination'},
                    {key: 'duration', label: 'Duration'},
                    'more_details'
                ],
                subFields: [
                    {key: 'destination.name', label: "Name"},
                    {key: 'destination.type.destinationType', label: "Type"},
                    {key: 'destination.district', label: "District"},
                    {key: 'destination.latitude', label: "Latitude"},
                    {key: 'destination.longitude', label: "Longitude"},
                    {key: 'destInDate', label: "Start Date"},
                    {key: 'destOutDate', label: "End Date"}],
                pastTrips: [],
                futureTrips: [],
                selectedTrip: "",
                errorMessage: "",
                showError: false,
                validDelete: false,
                editButton: false,
                deleteButton: false,
                hasPermission: false,
                retrievingTrips: false
            }
        },
        mounted() {
            /**
             *  Mounted function that uses the getAllTrips method to populate all a users trips on the page.
             */
            this.getAllTrips();
            this.getPermissions();
        },
        computed: {
            /**
             *  Computed function to calculate the length of the future trips, this is used for the pagination.
             */
            rowsUpcoming() {
                return this.futureTrips.length
            },

            /**
             *  Computed function to calculate the length of the past trips, this is used for the pagination.
             */
            rowsPast() {
                return this.pastTrips.length
            }

        },
        methods: {
            /**
             * Used to calculate the duration of the trip. Returns a blank string if the last destination in the trip
             * has no dates. Otherwise calculates the difference between the first and last destinations in the trip.
             * @param destinations in the trip
             * @returns string of the trip duration
             */
            calculateDuration(destinations) {
                if (destinations[destinations.length - 1].endDate == null) {
                    return ""
                } else if (destinations[destinations.length - 1].startDate == null) {
                    return ""
                }
                let calculateDur = Math.ceil((Math.abs(new Date(destinations[destinations.length - 1].endDate).getTime()
                    - new Date(destinations[0].startDate).getTime()))) / (1000 * 3600 * 24) + 1;
                if (calculateDur >= 365) {
                    return Math.floor(calculateDur / 365) + " year(s)"
                } else {
                    return calculateDur + " days"
                }

            },

            /**
             * Gets all the trips for a specific profile id. Uses the checkStatus and parseJSON functions to handle the
             * response. This function also splits up the trips into past and future trips based on their date compared
             * to today's date.
             * @returns {Promise<Response | never>}
             */
            getAllTrips() {
                let userId = this.profile.id;
                this.retrievingTrips = true;
                return fetch(`/v1/trips/` + userId, {
                    accept: "application/json"
                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then(trips => {
                        let today = new Date();
                        let self = this;
                        self.futureTrips = [];
                        self.pastTrips = [];
                        for (let i = 0; i < trips.length; i++) {
                            if (trips[i].destinations[0].startDate === null
                                || trips[i].destinations[trips[i].destinations.length - 1].endDate === null) {
                                self.futureTrips.push(trips[i]);
                            } else if (new Date(trips[i].destinations[trips[i].destinations.length - 1].endDate) <=
                                today) {
                                self.pastTrips.push(trips[i]);
                            } else {
                                self.futureTrips.push(trips[i]);
                            }
                        }
                        self.retrievingTrips = false;
                    })
            },

            /**
             * Used to check the response of a fetch method. If there is an error code, the code is printed to the
             * console.
             * @param response, passed back to the getAllTrips function to be parsed into a json.
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
                throw error;
            },

            /**
             * Used to turn the response of the fetch method into a usable JSON.
             * @param response of the fetch method.
             * @returns the json body of the response.
             */
            parseJSON(response) {
                return response.json();
            },

            /**
             * Used to send a selected trip to a modal that contains the plan a trip page, this is so the trip can be
             * edited.
             * @param trip, the trip that is selected to be edited in the modal.
             */
            sendTripToModal(trip) {
                this.selectedTrip = trip;
            },

            /**
             * Uses a fetch method to delete a users trip. Shows errors if something went wrong, or the user trying to
             * delete the trip is not the logged in user.
             * @param trip, the trip to be deleted.
             */
            deleteTrip: function (trip) {
                this.errorMessage = "";
                this.showError = false;
                this.validDelete = false;
                let self = this;
                fetch('/v1/trips/' + trip.id + "/" + this.profile.id, {
                    method: 'DELETE',
                }).then(function (response) {
                    if (response.ok) {
                        self.validDelete = true;
                        self.getAllTrips();
                    } else if (response.status === 403) {
                        throw new Error('You cannot delete another user\'s trips');
                    } else {
                        throw new Error('Something went wrong, try again later.');
                    }
                }).catch((error) => {
                    this.showError = true;
                    this.errorMessage = (error);
                });
            },

            /**
             *
             */
            getPermissions() {
                this.hasPermission = (this.userProfile.id === this.profile.id ||
                    (this.userProfile.isAdmin && this.adminView));
            },

            /**
             * Used to dismiss either the delete a trip confirmation modal or the edit a trip modal.
             * @param modal, the modal that is wanting to be dismissed.
             */
            dismissModal(modal) {
                this.$refs[modal].hide();
            },
            formatDate(date) {
                return date == null ? null : new Date(date).toLocaleDateString();
            }
        }
    }
</script>

<style scoped>
    @import "../../css/yourTrips.css";
</style>