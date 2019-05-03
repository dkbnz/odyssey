<template>
    <div class="container">
        <div id="upcomingTrips">
            <h1 class="page_title">Upcoming Trips</h1>
            <p class="page_title"><i>Here are your upcoming trips!</i></p>
            <b-alert v-model="showError" variant="danger" dismissible>{{errorMessage}}</b-alert>
            <b-alert v-model="validDelete" variant="success" dismissible>Trip Deleted</b-alert>

            <b-modal ref="deleteModal" id="deleteModal" hide-footer title="Delete Trip">
                <div class="d-block">
                    Are you sure that you want to delete "{{selectedTrip.name}}"?
                </div>
                <b-button class="mr-2 float-right" variant="danger" @click="dismissModal('deleteModal'); deleteTrip(selectedTrip);">Delete</b-button>
                <b-button class="mr-2 float-right" @click="dismissModal('deleteModal')">Cancel</b-button>

            </b-modal>

            <b-modal ref="editTripModal" id="editTripModal" size="xl" hide-footer title="Edit Trip">
                <plan-a-trip v-bind:inputTrip="selectedTrip" v-if="selectedTrip !== ''"></plan-a-trip>
                <b-button class="mr-2 float-right" @click="dismissModal('editTripModal')">Cancel</b-button>
            </b-modal>

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
                <b-button size="sm" v-model="editFutureButton" v-b-modal.editTripModal @click="sendTripToModal(row.item)" variant="primary" class="mr-2" v-if="userProfile.id === profile.id">Edit
                </b-button>
                <b-button size="sm" v-model="deleteFutureButton" v-b-modal.deleteModal @click="sendTripToModal(row.item)" variant="danger" class="mr-2" v-if="userProfile.id === profile.id">Delete
                </b-button>
            </template>
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
            <template v-if="data.item.destinations[data.item.destinations.length -1].endDate" slot="tripEndDate" slot-scope="data">
                {{data.item.destinations[data.item.destinations.length -1].endDate}}
            </template>
            <template v-if="futureTrips.length > 0" slot="tripEndDest" slot-scope="data">
                {{data.item.destinations[data.item.destinations.length -1].destination.name}}
            </template>

            </b-table>
            <b-row>
                <b-col cols="1">
                    <b-form-group
                            id="numUpcomingItems-field"
                            label-for="perPageUpcoming">
                        <b-form-select id="perPageUpcoming" v-model="perPageUpcoming" :options="optionViews" size="sm" trim> </b-form-select>
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

        <div id="pastTrips">
            <h1 class="page_title">Past Trips</h1>
            <p class="page_title"><i>Here are your past trips!</i></p>
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
                    <b-button size="sm" v-model="editPastButton" v-b-modal.editTripModal @click="sendTripToModal(row.item)" variant="primary" class="mr-2" v-if="userProfile.id === profile.id">Edit
                    </b-button>
                    <b-button size="sm" v-model="deletePastButton" v-b-modal.deleteModal @click="sendTripToModal(row.item)" variant="danger" class="mr-2" v-if="userProfile.id === profile.id">Delete
                    </b-button>
                </template>
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
                <template v-if="data.item.destinations[data.item.destinations.length -1].endDate" slot="tripEndDate" slot-scope="data">
                    {{data.item.destinations[data.item.destinations.length -1].endDate}}
                </template>
                <template v-if="pastTrips.length > 0" slot="tripEndDest" slot-scope="data">
                    {{data.item.destinations[data.item.destinations.length -1].destination.name}}
                </template>
            </b-table>
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
                editFutureButton: false,
                deleteFutureButton: false,
                editPastButton: false,
                deletePastButton: false
            }
        },
        mounted () {
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
            rowsUpcoming() {
                return this.futureTrips.length
            },
            rowsPast() {
                return this.pastTrips.length
            }

        },
        components: {
            PlanATrip
        },
        methods: {
            calculateDuration(destinations) {
                let calculateDur = Math.ceil((Math.abs(new Date(destinations[destinations.length -1 ].endDate).getTime() - new Date(destinations[0].startDate).getTime())))/ (1000 * 3600 * 24)
                if (calculateDur >= 365) {
                    return Math.floor(calculateDur/365) + " year(s)"
                } else {
                    return calculateDur + " days"
                }

            },
            getAllTrips(cb) {
                let userId = this.profile.id;
                return fetch(`/v1/trips/` + userId, {
                    accept: "application/json"
                })
                    .then(this.checkStatus)
                    .then(this.parseJSON)
                    .then(cb);
            },
            successfulDelete() {
                this.validDelete = true;
                //location.reload();
            },
            sendTripToModal(trip) {
                this.selectedTrip = trip;
            },
            deleteTrip: function(trip) {
                this.errorMessage = "";
                this.showError = false;
                this.validDelete = false;
                let self = this;
                fetch('/v1/trips/' + trip.id, {
                        method: 'DELETE',
                }).then(function (response) {
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
            dismissModal(modal) {
                this.$refs[modal].hide();
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

    #pastTrips {
        margin-top: 20px;
        padding-top: 20px;
        border-top: 1px solid black;
    }
</style>