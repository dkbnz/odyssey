<template>
    <div :class="containerClass">

        <!-- Div for all the user's future trips -->
        <div id="upcomingTrips">
            <h1 class="page-title">Upcoming Trips</h1>
            <p class="page-title"><i>Here are your upcoming trips!</i></p>
            <b-alert dismissible v-model="showError" variant="danger">{{errorMessage}}</b-alert>
            <b-alert
                    :show="dismissCountDown"
                    @dismiss-count-down="countDownChanged"
                    @dismissed="dismissCountDown=0"
                    dismissible
                    variant="success">
                <p>Trip Deleted</p>
                <b-progress
                        :max="dismissSecs"
                        :value="dismissCountDown"
                        height="4px"
                        variant="success"
                ></b-progress>
            </b-alert>

            <!-- Confirmation modal for deleting a trip. -->
            <b-modal hide-footer id="deleteModal" ref="deleteModal" title="Delete Trip">
                <div class="d-block">
                    Are you sure that you want to delete "{{selectedTrip.name}}"?
                </div>
                <b-button
                        class="mr-2 float-right"
                        variant="danger"
                        @click="deleteTrip(selectedTrip)">Delete
                </b-button>
                <b-button
                        @click="dismissModal('deleteModal')"
                        class="mr-2 float-right">Cancel
                </b-button>
            </b-modal>

            <!-- Modal that uses the plan a trip page to edit a selected trip -->
            <b-modal hide-footer id="editTripModal" ref="editTripModal" size="xl" title="Edit Trip">
                <plan-a-trip
                        :containerClass="'noMarginsContainer'"
                        :destinations="destinations"
                        :heading="'Edit a Trip'"
                        :inputTrip="selectedTrip"
                        :profile="profile"
                        :subHeading="'Edit your trip using the form below'"
                        v-if="selectedTrip !== ''"
                        v-on:tripSaved="getAllTrips()">
                </plan-a-trip>
                <b-button @click="dismissModal('editTripModal')" class="mr-2 float-right">Close</b-button>
            </b-modal>

            <!-- Table to show all a profile's future trips -->
            <b-table
                     :items="futureTrips"
                     :fields="fields"
                     :per-page="perPageUpcoming"
                     :current-page="currentPageUpcoming"
                     :busy="futureTrips.length === 0"
                     :sort-by.sync="sortBy"
                     :sort-desc.sync="sortDesc"
                     hover
                     id="myFutureTrips"
                     outlined
                     ref="myFutureTrips"
                     striped
                     responsive
            >
                <div class="text-center my-2" slot="table-busy">
                    <b-spinner class="align-middle" v-if="retrievingTrips"></b-spinner>
                    <strong>Can't find any trips!</strong>
                </div>
                <template slot="more_details" slot-scope="row">
                    <b-button size="sm"
                              @click="row.toggleDetails"
                              variant="warning"
                              class="mr-2"
                              block>
                        Show Details
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

                <template slot="tripStartDate"
                          slot-scope="data">
                    {{formatDate(calculateTripDates(data.item.destinations)[0])}}
                </template>

                <template slot="tripEndDate" slot-scope="data">
                    {{formatDate(calculateTripDates(data.item.destinations)[data.item.destinations.length -1])}}
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
            <h1 class="page-title">Past Trips</h1>
            <p class="page-title"><i>Here are your past trips!</i></p>

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
                         striped
                         responsive>

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

                    <template slot="tripStartDate" slot-scope="data">
                        {{formatDate(calculateTripDates(data.item.destinations)[0])}}
                    </template>

                    <template slot="tripEndDate" slot-scope="data">
                        {{formatDate(calculateTripDates(data.item.destinations)[data.item.destinations.length -1])}}
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
                retrievingTrips: false,
                dismissSecs: 3,
                dismissCountDown: 0,
            }
        },
        mounted() {
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
             *
             * @param destinations in the trip.
             * @returns string of the trip duration.
             */
            calculateDuration(destinations) {
                let tripDates = this.calculateTripDates(destinations);
                if (tripDates.length > 0) {
                    let calculateDur = Math.ceil((Math.abs(new Date(tripDates[tripDates.length -1 ]).getTime()
                        - new Date(tripDates[0]).getTime())))/ (1000 * 3600 * 24) + 1;
                    return calculateDur + " days";

                }
            },


            /**
             * Gathers trip dates into an array, regardless of whether they are start/end date
             */
            calculateTripDates(destinations) {
                let tripDates = [];
                for (let i = 0; i < destinations.length; i++ ) {
                    if (destinations[i].startDate !== null) {
                        tripDates.push(destinations[i].startDate);
                    }
                    if (destinations[i].endDate !== null) {
                        tripDates.push(destinations[i].endDate);
                    }
                }
                return tripDates;
            },

            /**
             * Gets all the trips for a specific profile id. Uses the checkStatus and parseJSON functions to handle the
             * response. This function also splits up the trips into past and future trips based on their date compared
             * to today's date.
             *
             * @returns {Promise<Response | never>} Json body of the trips belonging to the user.
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

                            // Sort the list of destinations of each trip using the list order
                            trips[i].destinations.sort((a, b) => {
                                // This is a comparison function that .sort needs to determine how to order the list

                                if (a.listOrder > b.listOrder) return 1;
                                if (a.listOrder < b.listOrder) return -1;
                                return 0;
                            });

                            let destinationDates = [];
                            for (let j = 0; j < trips[i].destinations.length; j++) {
                                if (trips[i].destinations[j].startDate !== null) {
                                    destinationDates.push(trips[i].destinations[j].startDate)
                                }
                                if (trips[i].destinations[j].endDate !== null) {
                                    destinationDates.push(trips[i].destinations[j].endDate)
                                }
                            }
                            if (destinationDates.length === 0) {
                                self.futureTrips.push(trips[i]);
                            }
                            else if (new Date(destinationDates[destinationDates.length - 1]) < today) {
                                self.pastTrips.push(trips[i]);
                            } else {
                                self.futureTrips.push(trips[i]);
                            }
                            self.futureTrips.sort(self.sortFutureTrips);
                        }
                        self.retrievingTrips = false;
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
                throw error;
            },

            /**
             * Used to turn the response of the fetch method into a usable Json.
             *
             * @param response of the fetch method.
             * @returns the json body of the response.
             */
            parseJSON(response) {
                return response.json();
            },

            /**
             * Used to send a selected trip to a modal that contains the plan a trip page, this is so the trip can be
             * edited.
             *
             * @param trip, the trip that is selected to be edited in the modal.
             */
            sendTripToModal(trip) {
                this.selectedTrip = trip;
            },

            /**
             * Uses a fetch method to delete a users trip. Shows errors if something went wrong, or the user trying to
             * delete the trip is not the logged in user.
             *
             * @param trip, the trip to be deleted.
             */
            deleteTrip: function (trip) {
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
                        self.showAlert();
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
             * Used to check the current user is either admin or the owner of the trip.
             */
            getPermissions() {
                this.hasPermission = (this.userProfile.id === this.profile.id ||
                    (this.userProfile.isAdmin && this.adminView));
            },

            /**
             * Used to dismiss either the delete a trip confirmation modal or the edit a trip modal.
             *
             * @param modal, the modal that is wanting to be dismissed.
             */
            dismissModal(modal) {
                this.$refs[modal].hide();
            },

            /**
             * Formats given date to a local date string. Handles null dates.
             *
             * @param date      the date to be formatted.
             * @returns {*}     the formatted date.
             */
            formatDate(date) {
                return date == null ? null : new Date(date).toLocaleDateString();
            },

            /**
             * Displays the countdown alert on the successful saving of a trip.
             */
            showAlert() {
                this.dismissCountDown = this.dismissSecs
            },

            /**
             * Used to allow an alert to countdown on the successful deletion of a trip.
             * @param dismissCountDown      the name of the alert.
             */
            countDownChanged(dismissCountDown) {
                this.dismissCountDown = dismissCountDown
            }
        }
    }
</script>

<style scoped>
    @import "../../css/yourTrips.css";
</style>