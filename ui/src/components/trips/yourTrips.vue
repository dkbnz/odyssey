<template>
    <div class="container">

        <!-- Div for all the user's future trips -->
        <div id="upcomingTrips">
            <h1 class="page_title">Upcoming Trips</h1>
            <p class="page_title"><i>Here are your upcoming trips!</i></p>
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
                        @click="dismissModal('deleteModal')
                         deleteTrip(selectedTrip);">Delete
                </b-button>
                <b-button
                        class="mr-2 float-right"
                        @click="dismissModal('deleteModal')">Cancel
                </b-button>
            </b-modal>

            <!-- Modal that uses the plan a trip page to edit a selected trip -->
            <b-modal ref="editTripModal" id="editTripModal" size="xl" hide-footer title="Edit Trip">
                <plan-a-trip
                        v-bind:inputTrip="selectedTrip"
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
                     :sort-by.sync="sortByUpcoming"
                     :sort-desc.sync="sortDesc"
                     :per-page="perPageUpcoming"
                     :current-page="currentPageUpcoming"
                     :busy="futureTrips.length === 0"
            >
                <div slot="table-busy" class="text-center my-2">
                    <strong>Can't find any trips!</strong>
                </div>
                <template slot="more_details" slot-scope="row">
                    <b-button size="sm" @click="row.toggleDetails" class="mr-2">
                        {{ row.detailsShowing ? 'Hide' : 'Show'}} Details
                    </b-button>
                    <b-button size="sm"
                              v-model="editButton"
                              v-b-modal.editTripModal
                              @click="sendTripToModal(row.item)"
                              variant="primary"
                              class="mr-2"
                              v-if="userProfile.id === profile.id">Edit</b-button>
                    <b-button size="sm"
                              v-model="deleteButton"
                              v-b-modal.deleteModal
                              @click="sendTripToModal(row.item)"
                              variant="danger"
                              class="mr-2"
                              v-if="userProfile.id === profile.id">Delete</b-button>
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
                                {{data.item.startDate}}
                            </template>
                            <template v-if="futureTrips.length > 0" slot="destOutDate" slot-scope="data">
                                {{data.item.endDate}}
                            </template>

                        </b-table>

                    </b-card>
                </template>

                <template v-if="futureTrips.length > 0" slot="duration" slot-scope="data">
                    {{calculateDuration(data.item.destinations)}}
                </template>
                <template v-if="data.item.destinations[data.item.destinations.length -1].endDate"
                          slot="tripEndDate" slot-scope="data">
                    {{data.item.destinations[data.item.destinations.length -1].endDate}}
                </template>
                <template v-if="futureTrips.length > 0" slot="tripEndDest" slot-scope="data">
                    {{data.item.destinations[data.item.destinations.length -1].destination.name}}
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
            <h1 class="page_title">Past Trips</h1>
            <p class="page_title"><i>Here are your past trips!</i></p>

            <!-- Table to show all a profile's past trips -->
            <b-table hover striped outlined
                     id="myPastTrips"
                     ref="myPastTrips"
                     :items="pastTrips"
                     :fields="fields"
                     :sort-by.sync= "sortByPast"
                     :sort-desc.sync="sortDesc"
                     :per-page="perPageUpcoming"
                     :current-page="currentPageUpcoming"
                     :busy="pastTrips.length === 0"
            >
                <div slot="table-busy" class="text-center my-2">
                    <strong>Can't find any trips!</strong>
                </div>
                <template slot="more_details" slot-scope="row">
                    <b-button size="sm" @click="row.toggleDetails" class="mr-2">
                        {{ row.detailsShowing ? 'Hide' : 'Show'}} Details
                    </b-button>
                    <b-button size="sm" v-model="editButton" v-b-modal.editTripModal @click="sendTripToModal(row.item)"
                              variant="primary" class="mr-2" v-if="userProfile.id === profile.id">Edit
                    </b-button>
                    <b-button size="sm" v-model="deleteButton" v-b-modal.deleteModal @click="sendTripToModal(row.item)"
                              variant="danger" class="mr-2" v-if="userProfile.id === profile.id">Delete
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
                                {{data.item.startDate}}
                            </template>
                            <template v-if="pastTrips.length > 0" slot="destOutDate" slot-scope="data">
                                {{data.item.endDate}}
                            </template>
                        </b-table>
                    </b-card>
                </template>

                <template v-if="pastTrips.length > 0" slot="duration" slot-scope="data">
                    {{calculateDuration(data.item.destinations)}}
                </template>
                <template v-if="data.item.destinations[data.item.destinations.length -1].endDate" slot="tripEndDate"
                          slot-scope="data">
                    {{data.item.destinations[data.item.destinations.length -1].endDate}}
                </template>
                <template v-if="pastTrips.length > 0" slot="tripEndDest" slot-scope="data">
                    {{data.item.destinations[data.item.destinations.length -1].destination.name}}
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
        props: ['profile', 'userProfile'],
        components: {
            PlanATrip
        },
        data: function() {
            return {
                optionViews: [{value:1, text:"1"}, {value:5, text:"5"}, {value:10, text:"10"}, {value:15, text:"15"}],
                perPageUpcoming: 5,
                perPagePast: 5,
                sortByUpcoming: 'destinations[0].startDate',
                sortByPast: 'destinations[1].endDate',
                sortDesc: true,
                currentPageUpcoming: 1,
                currentPagePast: 1,
                fields: [
                    'name',
                    {key:'destinations[0].startDate', label: 'Start Date'},
                    {key:'destinations[0].destination.name', label: 'Start Destination'},
                    {key:'tripEndDate', label: 'End Date'},
                    {key:'tripEndDest', label: 'End Destination'},
                    {key: 'duration', label: 'Duration'},
                    'more_details'
                ],
                subFields:[
                    {key: 'destination.name', label: "Destination Name"},
                    {key: 'destination.type.destinationType', label: "Destination Type"},
                    {key: 'destination.district', label: "Destination District"},
                    {key: 'destination.latitude', label: "Destination Latitude"},
                    {key: 'destination.longitude', label: "Destination Longitude"},
                    {key: 'destInDate', label: "In Date"},
                    {key: 'destOutDate', label: "Out Date"}],
                pastTrips: [],
                futureTrips:[],
                selectedTrip: "",
                errorMessage: "",
                showError: false,
                validDelete: false,
                editButton: false,
                deleteButton: false,
            }
        },
        mounted () {
            /**
             *  Mounted function that uses the getAllTrips method to populate all a users trips on the page.
             *  This function also splits up the trips into past and future trips based on their date compared to
             *  today's date.
             */
            this.getAllTrips(trips => {
                let todayDate = new Date();
                let self = this;
                for (let i = 0; i < trips.length; i++) {
                    if (trips[i].destinations[0].startDate === null) {
                        self.futureTrips.push(trips[i]);
                    }
                    else if (new Date(trips[i].destinations[0].startDate) <= todayDate) {
                        self.pastTrips.push(trips[i]);
                    } else {
                        self.futureTrips.push(trips[i]);
                    }
                }

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
            }

        },
        methods: {
            /**
             * Used to calculate the duration of the trip. Returns a hyphen if the last destination in the trip has no
             * dates. Otherwise calculates the difference between the first and last destinations in the trip.
             * @param destinations in the trip
             * @returns string of the trip duration
             */
            calculateDuration(destinations) {
                if (destinations[destinations.length -1 ].endDate == null) {
                    return "-"
                }
                let calculateDur = Math.ceil((Math.abs(new Date(destinations[destinations.length -1 ].endDate).getTime()
                    - new Date(destinations[0].startDate).getTime())))/ (1000 * 3600 * 24);
                if (calculateDur >= 365) {
                    return Math.floor(calculateDur/365) + " year(s)"
                } else {
                    return calculateDur + " days"
                }

            },

            /**
             * Gets all the trips for a specific profile id. Uses the checkStatus and parseJSON functions to handle the
             * response.
             * @param trips, populates the trips by using the mounted function
             * @returns {Promise<Response | never>}
             */
            getAllTrips(trips) {
                let userId = this.profile.id;
                return fetch(`/v1/trips/` + userId, {
                    accept: "application/json"
                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then(trips)
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
                console.log(error); // eslint-disable-line no-console
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
             * Makes a alert on the page visible after a trip is successfully deleted.
             */
            successfulDelete() {
                this.validDelete = true;
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
                    console.log(response);
                    if (response.ok) {
                        self.successfulDelete();
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
             * Used to dismiss either the delete a trip confirmation modal or the edit a trip modal.
             * @param modal, the modal that is wanting to be dismissed.
             */
            dismissModal(modal) {
                this.$refs[modal].hide();
            },
        }
    }
</script>

<style scoped>

    #pastTrips {
        margin-top: 20px;
        padding-top: 20px;
        border-top: 1px solid black;
    }
</style>