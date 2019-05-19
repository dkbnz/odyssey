<template>
    <div class="container">

        <!-- Div for all the user's future trips -->
        <div id="upcomingTrips">
            <h1 class="page-title">Upcoming Trips</h1>
            <p class="page-title"><i>Here are your upcoming trips!</i></p>
            <b-alert v-model="showError" variant="danger" dismissible>{{errorMessage}}</b-alert>
            <b-alert v-model="validDelete" variant="success" dismissible>Trip Deleted</b-alert>

            <!-- Confirmation modal for deleting a trip. -->
            <b-modal ref="deleteModal" id="deleteModal" hide-footer title="Delete Trip">
                <div class="d-block">
                    Are you sure that you want to delete "{{selectedTrip.name}}"?
                </div>
                <b-button
                        class="mr-2 float-right"
                        variant="danger"
                        @click="deleteTrip(selectedTrip)">Delete
                </b-button>
                <b-button
                        class="mr-2 float-right"
                        @click="dismissModal('deleteModal')">Cancel
                </b-button>
            </b-modal>

            <!-- Modal that uses the plan a trip page to edit a selected trip -->
            <b-modal ref="editTripModal" id="editTripModal" size="xl" hide-footer title="Edit Trip">
                <plan-a-trip
                        v-on:tripSaved="getAllTrips()"
                        :destinations="destinations"
                        :profile="profile"
                        :inputTrip="selectedTrip"
                        :heading="'Edit a Trip'"
                        :subHeading="'Edit your trip using the form below'"
                        v-if="selectedTrip !== ''">
                </plan-a-trip>
                <b-button class="mr-2 float-right" @click="dismissModal('editTripModal')">Cancel</b-button>
            </b-modal>

            <!-- Table to show all a profile's future trips -->
            <b-table hover striped outlined
                     id="myFutureTrips"
                     ref="myFutureTrips"
                     :items="futureTrips"
                     :fields="fields"
                     :per-page="perPageUpcoming"
                     :current-page="currentPageUpcoming"
                     :busy="futureTrips.length === 0"
            >
                <div slot="table-busy" class="text-center my-2">
                    <strong>Can't find any trips!</strong>
                </div>
                <template slot="more_details" slot-scope="row">
                    <b-button size="sm"
                              @click="row.toggleDetails"
                              variant="warning"
                              class="mr-2"
                              block>
                        {{ row.detailsShowing ? 'Hide' : 'Show'}} Details
                    </b-button>
                    <b-button size="sm"
                              v-model="editButton"
                              v-b-modal.editTripModal
                              @click="sendTripToModal(row.item)"
                              variant="primary"
                              class="mr-2"
                              v-if="hasPermission"
                              block>
                        Edit
                    </b-button>
                    <b-button size="sm"
                              v-model="deleteButton"
                              v-b-modal.deleteModal
                              @click="sendTripToModal(row.item)"
                              variant="danger"
                              class="mr-2"
                              v-if="hasPermission"
                              block>
                        Delete
                    </b-button>
                </template>

                <!-- Shows all the trip destinations in a separate nested table -->
                <template slot="row-details" slot-scope="row">
                    <b-card>
                        <b-table
                        id="futureTripsDestinations"
                        ref="destinationsTable"
                        :items="row.item.destinations"
                        :fields="subFields">
                            <template v-if="futureTrips.length > 0" slot="destInDate" slot-scope="data">
                                {{formatDate(data.item.startDate)}}
                            </template>
                            <template v-if="futureTrips.length > 0" slot="destOutDate" slot-scope="data">
                                {{formatDate(data.item.endDate)}}
                            </template>

                        </b-table>

                    </b-card>
                </template>

                <template slot="tripStartDate"
                          slot-scope="data">
                    {{formatDate(calculateStartDate(data.item.destinations))}}
                </template>

                <template slot="tripEndDate" slot-scope="data">
                    {{formatDate(calculateEndDate(data.item.destinations))}}
                </template>

                <template v-if="futureTrips.length > 0" slot="tripEndDest" slot-scope="data">
                    {{data.item.destinations[data.item.destinations.length -1].destination.name}}
                </template>

                <template v-if="futureTrips.length > 0" slot="duration" slot-scope="data">
                    {{calculateDuration(data.item.destinations)}}
                </template>

            </b-table>

            <!-- Determines pagination and number of results per row of the table -->
            <b-row>
                <b-col cols="1">
                    <b-form-group
                            id="numUpcomingItems-field"
                            label-for="perPageUpcoming">
                        <b-form-select id="perPageUpcoming" v-model="perPageUpcoming" :options="optionViews"
                                       size="sm" trim> </b-form-select>
                    </b-form-group>
                </b-col>
                <b-col cols="8">
                    <b-pagination
                            v-model="currentPageUpcoming"
                            :total-rows="rowsUpcoming"
                            :per-page="perPageUpcoming"
                            aria-controls="my-table"
                            first-text="First"
                            last-text="Last"
                            align="center"
                            size="sm"
                    ></b-pagination>
                </b-col>
            </b-row>
        </div>

        <!-- Div for all the user's past trips -->
        <div id="pastTrips">
            <h1 class="page-title">Past Trips</h1>
            <p class="page-title"><i>Here are your past trips!</i></p>

            <!-- Table to show all a profile's past trips -->
            <b-table hover striped outlined
                     id="myPastTrips"
                     ref="myPastTrips"
                     :items="pastTrips"
                     :fields="fields"
                     :sort-by="sortBy"
                     :sort-desc="sortDescPast"
                     :per-page="perPagePast"
                     :current-page="currentPagePast"
                     :busy="pastTrips.length === 0">

                <div slot="table-busy" class="text-center my-2">
                    <strong>Can't find any trips!</strong>
                </div>
                <template slot="more_details" slot-scope="row">
                    <b-button size="sm"
                              @click="row.toggleDetails"
                              variant="warning"
                              class="mr-2"
                              block>
                        {{ row.detailsShowing ? 'Hide' : 'Show'}} Details
                    </b-button>
                    <b-button size="sm"
                              v-model="editButton"
                              v-b-modal.editTripModal
                              @click="sendTripToModal(row.item)"
                              variant="primary"
                              class="mr-2"
                              v-if="hasPermission"
                              block>
                        Edit
                    </b-button>
                    <b-button size="sm"
                              v-model="deleteButton"
                              v-b-modal.deleteModal
                              @click="sendTripToModal(row.item)"
                              variant="danger"
                              class="mr-2"
                              v-if="hasPermission"
                              block>
                        Delete
                    </b-button>
                </template>

                <!-- Shows all the trip destinations in a separate nested table -->
                <template slot="row-details" slot-scope="row">
                    <b-card>
                        <b-table
                                id="pastTripsDestinations"
                                ref="pastDestinationsTable"
                                :items="row.item.destinations"
                                :fields="subFields">
                            <template v-if="pastTrips.length > 0" slot="destInDate" slot-scope="data">
                                {{formatDate(data.item.startDate)}}
                            </template>
                            <template v-if="pastTrips.length > 0" slot="destOutDate" slot-scope="data">
                                {{formatDate(data.item.endDate)}}
                            </template>
                        </b-table>
                    </b-card>
                </template>

                <template slot="tripStartDate" slot-scope="data">
                    {{formatDate(calculateStartDate(data.item.destinations))}}
                </template>

                <template slot="tripEndDate" slot-scope="data">
                    {{formatDate(calculateEndDate(data.item.destinations))}}
                </template>

                <template v-if="pastTrips.length > 0" slot="tripEndDest" slot-scope="data">
                    {{data.item.destinations[data.item.destinations.length -1].destination.name}}
                </template>

                <template v-if="pastTrips.length > 0" slot="duration" slot-scope="data">
                    {{calculateDuration(data.item.destinations)}}
                </template>

            </b-table>

            <!-- Determines pagination and number of results per row of the table -->
            <b-row>
                <b-col cols="1">
                    <b-form-group
                            id="numItemsPast-field"
                            label-for="perPagePast">
                        <b-form-select id="perPage" v-model="perPagePast" :options="optionViews" size="sm" trim>
                        </b-form-select>
                    </b-form-group>
                </b-col>
                <b-col cols="8">
                    <b-pagination
                            v-model="currentPagePast"
                            :total-rows="rowsPast"
                            :per-page="perPagePast"
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
    import PlanATrip from './planATrip.vue'
    export default {
        name: "YourTrips",
        props: {
            profile: Object,
            userProfile: {
                default: function() {
                    return this.profile
                }
            },
            destinations: Array,
            adminView: Boolean,
        },
        components: {
            PlanATrip
        },
        data: function() {
            return {
                optionViews: [{value:1, text:"1"}, {value:5, text:"5"}, {value:10, text:"10"}, {value:15, text:"15"}],
                perPageUpcoming: 10,
                perPagePast: 10,
                sortBy: 'destinations[0].startDate',
                sortDesc: false,
                sortDescPast: true,
                currentPageUpcoming: 1,
                currentPagePast: 1,
                fields: [
                    'name',
                    {key:'tripStartDate', label: 'Start Date'},
                    {key:'destinations[0].destination.name', label: 'Start Destination'},
                    {key:'tripEndDate', label: 'End Date'},
                    {key:'tripEndDest', label: 'End Destination'},
                    {key: 'duration', label: 'Duration'},
                    'more_details'
                ],
                subFields:[
                    {key: 'destination.name', label: "Name"},
                    {key: 'destination.type.destinationType', label: "Type"},
                    {key: 'destination.district', label: "District"},
                    {key: 'destination.latitude', label: "Latitude"},
                    {key: 'destination.longitude', label: "Longitude"},
                    {key: 'destInDate', label: "Start Date"},
                    {key: 'destOutDate', label: "End Date"}],
                pastTrips: [],
                futureTrips:[],
                selectedTrip: "",
                errorMessage: "",
                showError: false,
                validDelete: false,
                editButton: false,
                deleteButton: false,
                hasPermission: false
            }
        },
        mounted () {
            /**
             *  Mounted function that uses the getAllTrips method to populate all a users trips on the page.
             */
            this.getAllTrips();
            this.getPermissions();

            /**
             * When a modal is hidden, refreshes the trips. This is so the list of trips table is up to date.
             */
            this.$root.$on('bv::modal::hide', () => {
                this.getAllTrips();
            });
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
            },

        },
        methods: {
            /**
             * Used to calculate the duration of the trip. Returns a blank string if the last destination in the trip
             * has no dates. Otherwise calculates the difference between the first and last destinations in the trip.
             * @param destinations in the trip
             * @returns string of the trip duration
             */
            calculateDuration(destinations) {
                let tripStartDates = [];
                let tripEndDates = [];
                for (let i = 0; i < destinations.length; i++ ) {
                    if (destinations[i].startDate !== null) {
                        tripStartDates.push(destinations[i].startDate);
                    }
                    if (destinations[i].endDate !== null) {
                        tripEndDates.push(destinations[i].endDate);
                    }
                }
                if (tripStartDates.length > 0 && tripEndDates.length > 0) {
                    let calculateDur = Math.ceil((Math.abs(new Date(tripEndDates[tripEndDates.length -1 ]).getTime()
                        - new Date(tripStartDates[0]).getTime())))/ (1000 * 3600 * 24) + 1;
                    return calculateDur + " days";

                }
            },

            /**
             * Calculates the trips start date by the first start date it can find.
             */
            calculateStartDate(destinations) {
                let tripStartDates = [];
                for (let i = 0; i < destinations.length; i++) {
                    if (destinations[i].startDate !== null) {
                        tripStartDates.push(destinations[i].startDate);
                    }
                }
                return (tripStartDates[0]);
            },

            /**
             * Calculates the trips end date by the last end date it can find.
             */
            calculateEndDate(destinations) {
                let tripEndDates = [];
                for (let i = 0; i < destinations.length; i++) {
                    if (destinations[i].endDate !== null) {
                        tripEndDates.push(destinations[i].endDate);
                    }
                }
                return (tripEndDates[tripEndDates.length - 1]);
            },

            /**
             * Gets all the trips for a specific profile id. Uses the checkStatus and parseJSON functions to handle the
             * response. This function also splits up the trips into past and future trips based on their date compared
             * to today's date.
             * @returns {Promise<Response | never>}
             */
            getAllTrips() {
                let userId = this.profile.id;
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
                            let destinationsStartDates = [];
                            let destinationsEndDates = [];
                            for (let j = 0; j < trips[i].destinations.length; j++) {
                                if (trips[i].destinations[j].startDate !== null) {
                                    destinationsStartDates.push(trips[i].destinations[j].startDate)
                                }
                                if (trips[i].destinations[j].endDate !== null) {
                                    destinationsEndDates.push(trips[i].destinations[j].endDate)
                                }
                            }
                            if (destinationsStartDates.length === 0 && destinationsEndDates.length === 0) {
                                self.futureTrips.push(trips[i]);
                            }
                            else if (new Date(destinationsEndDates[destinationsEndDates.length - 1]) <= today) {
                                self.pastTrips.push(trips[i]);
                            } else {
                                self.futureTrips.push(trips[i]);
                            }
                            self.futureTrips.sort(self.sortFutureTrips);
                        }
                    });
            },

            /**
             * Orders the future trips by the dates. If there are no dates then they will be at the top. If there are
             * dates, then trips will be ordered chronologically.
             */
            sortFutureTrips(first, next) {
                let firstDestinationsStart = [];
                let nextDestinationsStart = [];
                for (let i = 0; i < first.destinations.length; i++) {
                    if(first.destinations[i].startDate !== null) {
                        firstDestinationsStart.push(first.destinations[i].startDate)
                    }
                }
                for (let i = 0; i < next.destinations.length; i++) {
                    if(next.destinations[i].startDate !== null) {
                        nextDestinationsStart.push(next.destinations[i].startDate)
                    }
                }
                if(firstDestinationsStart[0] < nextDestinationsStart[0] || firstDestinationsStart[0] === undefined) {
                    return -1;
                }

                if(firstDestinationsStart[0] > nextDestinationsStart[0]) {
                    return 1;
                }
                return 0;
            },

            /**
             * Used to check the response of a fetch method. If there is an error code, the code is printed to the
             * console.
             * @param response, passed back to the getAllTrips function to be parsed into a json.
             * @returns throws the error.
             */
            checkStatus (response) {
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
            parseJSON (response) {
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
            deleteTrip: function(trip) {
                this.errorMessage = "";
                this.showError = false;
                this.validDelete = false;
                let self = this;
                fetch('/v1/trips/' + trip.id, {
                        method: 'DELETE',
                }).then(function (response) {
                    if (response.ok) {
                        self.validDelete = true;
                        self.dismissModal('deleteModal');
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