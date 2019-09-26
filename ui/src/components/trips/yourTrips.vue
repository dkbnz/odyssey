<template>
    <div class="bg-white m-2 pt-3 pl-3 pr-3 pb-3 rounded-lg">
        <!-- Div for all the user's future trips -->
        <div id="upcomingTrips" class="pt-3">
            <h1 class="page-title">Upcoming Trips</h1>
            <p class="page-title"><i>Here are your upcoming trips!</i></p>
            <b-alert dismissible v-model="showError" variant="danger"><p class="wrapWhiteSpace">{{errorMessage}}</p></b-alert>
            <b-alert
                    :show="dismissCountDown"
                    @dismiss-count-down="countDownChanged"
                    @dismissed="dismissCountDown=0"
                    dismissible
                    variant="success">
                <p>Trip Deleted</p>
                <b-progress
                        :max="dismissSecs"
                        :value="dismissCountDown - 1"
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
            <b-modal hide-footer id="editTripModal" ref="editTripModal" size="lg" title="Edit Trip">
                <plan-a-trip
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
                    :busy="futureTrips.length === 0"
                    :sort-by.sync="sortBy"
                    :sort-desc.sync="sortDesc"
                    hover
                    id="myFutureTrips"
                    outlined
                    ref="myFutureTrips"
                    striped
                    responsive>
                <div class="text-center my-2" slot="table-busy">
                    <b-img alt="Loading" class="align-middle loading" v-if="retrievingTrips" :src="assets['loadingLogo']">
                    </b-img>
                    <strong v-if="!retrievingTrips && !futureTrips.length">Can't find any trips!</strong>
                </div>
                <template v-slot:cell(actions)="row">
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
                    <b-button size="sm"
                              @click="row.toggleDetails"
                              variant="warning"
                              class="mr-2"
                              block>
                        {{ row.detailsShowing ? 'Hide' : 'Show'}} Details
                    </b-button>
                </template>

                <!-- Shows all the trip destinations in a separate nested table -->
                <template v-slot:row-details="row">
                    <b-card>
                        <b-table
                                :fields="subFields"
                                :items="row.item.destinations"
                                id="futureTripsDestinations"
                                ref="destinationsTable">
                            <template v-slot:cell(destInDate)="data" v-if="futureTrips.length > 0">
                                {{formatDate(data.item.startDate)}}
                            </template>
                            <template v-slot:cell(destOutDate)="data" v-if="futureTrips.length > 0">
                                {{formatDate(data.item.endDate)}}
                            </template>

                        </b-table>

                    </b-card>
                </template>

                <template v-slot:cell(tripStartDate)="data">
                    {{formatDate(calculateTripDates(data.item.destinations)[0])}}
                </template>

                <template v-slot:cell(tripEndDate)="data">
                    {{formatDate(calculateTripDates(data.item.destinations)[data.item.destinations.length -1])}}
                </template>

                <template v-slot:cell(tripEndDest)="data" v-if="futureTrips.length > 0">
                    {{data.item.destinations[data.item.destinations.length -1].destination.name}}
                </template>

                <template v-slot:cell(duration)="data" v-if="futureTrips.length > 0">
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
                <b-col cols="10">
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
        <div id="pastTrips" class="mt-3 mb-3 pt-3">
            <h1 class="page-title">Past Trips</h1>
            <p class="page-title"><i>Here are your past trips!</i></p>

            <b-container fluid>
                <!-- Table to show all a profile's past trips -->
                <b-table :busy="pastTrips.length === 0" :fields="fields"
                         :items="pastTrips"
                         :sort-by="sortBy"
                         :sort-desc="sortDescPast"
                         hover
                         id="myPastTrips"
                         outlined
                         ref="myPastTrips"
                         striped
                         responsive>

                    <div slot="table-busy" class="text-center my-2">
                        <b-img alt="Loading" class="align-middle loading" v-if="retrievingTrips" :src="assets['loadingLogo']">
                        </b-img>
                        <strong v-if="!retrievingTrips && !pastTrips.length" >Can't find any trips!</strong>
                    </div>
                    <template v-slot:cell(actions)="row">
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
                        <b-button size="sm"
                                  @click="row.toggleDetails"
                                  variant="warning"
                                  class="mr-2"
                                  block>
                            {{ row.detailsShowing ? 'Hide' : 'Show'}} Details
                        </b-button>
                    </template>

                    <!-- Shows all the trip destinations in a separate nested table -->
                    <template v-slot:row-details="row">
                        <b-card>
                            <b-table
                                    :fields="subFields"
                                    :items="row.item.destinations"
                                    id="pastTripsDestinations"
                                    ref="pastDestinationsTable">
                                <template v-slot:cell(destInDate)="data" v-if="pastTrips.length > 0">
                                    {{formatDate(data.item.startDate)}}
                                </template>
                                <template v-slot:cell(destOutDate)="data" v-if="pastTrips.length > 0">
                                    {{formatDate(data.item.endDate)}}
                                </template>
                            </b-table>
                        </b-card>
                    </template>

                    <template v-slot:cell(tripStartDate)="data">
                        {{formatDate(calculateTripDates(data.item.destinations)[0])}}
                    </template>

                    <template v-slot:cell(tripEndDate)="data">
                        {{formatDate(calculateTripDates(data.item.destinations)[data.item.destinations.length -1])}}
                    </template>

                    <template v-slot:cell(tripEndDest)="data" v-if="pastTrips.length > 0">
                        {{data.item.destinations[data.item.destinations.length -1].destination.name}}
                    </template>

                    <template v-slot:cell(duration)="data" v-if="pastTrips.length > 0">
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
                    <b-col cols="10">
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
            adminView: Boolean
        },

        watch: {
            profile() {
                this.getAllTrips();
                this.retrieveTripCount()
            },


            perPageUpcoming() {
                this.getAllTrips();
            },


            perPagePast() {
                this.getAllTrips();
            },


            currentPageUpcoming() {
                this.getAllTrips();
            },


            currentPagePast() {
                this.getAllTrips();
            }
        },

        components: {
            PlanATrip
        },

        data: function () {
            return {
                optionViews: [
                    {value: 1, text: "1"},
                    {value: 5, text: "5"},
                    {value: 10, text: "10"},
                    {value: 15, text: "15"}
                ],
                perPageUpcoming: 5,
                perPagePast: 5,
                sortBy: 'destinations[0].startDate',
                sortDesc: false,
                sortDescPast: true,
                currentPageUpcoming: 1,
                currentPagePast: 1,
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
                rowsUpcoming: 0,
                rowsPast: 0
            }
        },

        mounted() {
            /**
             *  Mounted function that uses the getAllTrips method to populate all a users trips on the page.
             */
            this.getAllTrips();


            /**
             * Retrieves the total number of trips a profile has to determine the table pagination.
             */
            this.retrieveTripCount();


            /**
             * Determines what permissions the current logged in user has.
             */
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
             * The fields that will displayed in the trips tables.
             */
            fields() {
                return [
                    'name',
                    {key: 'tripStartDate', label: 'Start Date'},
                    {key: 'destinations[0].destination.name', label: 'Start Destination'},
                    {key: 'tripEndDate', label: 'End Date'},
                    {key: 'tripEndDest', label: 'End Destination'},
                    {key: 'duration', label: 'Duration'},
                    'actions'
                ]
            },

        },
        methods: {
            /**
             * Used to calculate the duration of the trip. Returns a blank string if the last destination in the trip
             * has no dates. Otherwise calculates the difference between the first and last destinations in the trip.
             *
             * @param destinations      the destinations in the trip.
             * @returns                 string of the trip duration.
             */
            calculateDuration(destinations) {
                let tripDates = this.calculateTripDates(destinations);
                if (tripDates.length > 0) {
                    let calculateDur = Math.ceil((Math.abs(new Date(tripDates[tripDates.length - 1]).getTime()
                        - new Date(tripDates[0]).getTime()))) / (1000 * 3600 * 24) + 1;
                    return calculateDur + " days";

                }
            },


            /**
             * Gathers trip dates into an array, regardless of whether they are start/end date.
             *
             * @param destinations  the destinations of the trip that is going to be used to display the trip dates.
             * @returns {Array}      the dates of the first and last destination in the trip.
             */
            calculateTripDates(destinations) {
                let tripDates = [];
                for (let i = 0; i < destinations.length; i++) {
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
             * Gets all the trips for a specific profile id. Checks the response status and handles appropriate errors.
             * This method then passes the returned list of trips to the sortTrips() method to determine if they are
             * future or past trips.
             */
            getAllTrips() {
                let userId = this.profile.id;
                this.retrievingTrips = true;
                let self = this;
                let futurePage = Number(this.currentPageUpcoming) - 1;
                let pastPage = Number(this.currentPagePast) - 1;
                let queryString =
                    "?pageSizeFuture=" + this.perPageUpcoming +
                    "&pageSizePast=" + this.perPagePast +
                    "&pageFuture=" + futurePage +
                    "&pagePast=" + pastPage;
                if (userId !== undefined) {
                    fetch(`/v1/trips/` + userId + queryString, {
                        accept: "application/json"
                    }).then(function (response) {
                        if (!response.ok) {
                            throw response;
                        } else {
                            return response.json();
                        }
                    }).then(function (responseBody) {
                        self.showError = false;
                        self.retrievingTrips = false;
                        self.futureTrips = responseBody.futureTrips;
                        self.pastTrips = responseBody.pastTrips;
                    }).catch(function (response) {
                        self.showError = false;
                        self.retrievingTrips = false;
                        self.handleErrorResponse(response);
                    });
                }
            },


            /**
             * Retrieves the total number of trips for the given profile.
             */
            retrieveTripCount() {
                let userId = this.profile.id;
                let self = this;
                fetch(`/v1/trips/` + userId + "/count", {
                    accept: "application/json"
                }).then(function (response) {
                    if (!response.ok) {
                        throw response;
                    } else {
                        return response.json();
                    }
                }).then(function (responseBody) {
                    self.showError = false;
                    self.retrievingTrips = false;
                    self.rowsUpcoming = responseBody.futureTrips;
                    self.rowsPast = responseBody.pastTrips;
                }).catch(function (response) {
                    self.showError = false;
                    self.retrievingTrips = false;
                    self.handleErrorResponse(response);
                });
            },


            /**
             * Used to send a selected trip to a modal that contains the plan a trip page, this is so the trip can be
             * edited.
             *
             * @param trip          the trip that is selected to be edited in the modal.
             */
            sendTripToModal(trip) {
                this.selectedTrip = trip;
            },


            /**
             * Uses a fetch method to delete a users trip. Shows errors if something went wrong, or the user trying to
             * delete the trip is not the logged in user.
             *
             * @param trip          the trip to be deleted.
             */
            deleteTrip: function (trip) {
                this.errorMessage = "";
                this.showError = false;
                this.validDelete = false;
                let self = this;
                fetch('/v1/trips/' + trip.id, {
                    method: 'DELETE',
                }).then(function (response) {
                    if (!response.ok) {
                        throw response;
                    } else {
                        return response.json();
                    }
                }).then(function () {
                    self.showError = false;
                    self.validDelete = true;
                    self.dismissModal('deleteModal');
                    self.getAllTrips();
                    self.showAlert();
                }).catch(function (response) {
                    self.handleErrorResponse(response);
                });
            },


            /**
             * Used to check the current user is either admin or the owner of the trip.
             */
            getPermissions() {
                this.hasPermission = (this.userProfile.id === this.profile.id ||
                    (this.userProfile.admin && this.adminView));
            },


            /**
             * Used to dismiss either the delete a trip confirmation modal or the edit a trip modal.
             *
             * @param modal         the modal that is wanting to be dismissed.
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
             *
             * @param dismissCountDown      the name of the alert.
             */
            countDownChanged(dismissCountDown) {
                this.dismissCountDown = dismissCountDown
            }
        }
    }
</script>